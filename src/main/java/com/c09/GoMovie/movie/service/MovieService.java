package com.c09.GoMovie.movie.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.movie.entities.Movie;
import com.c09.GoMovie.movie.entities.MovieComment;
import com.c09.GoMovie.movie.entities.repositories.MovieCommentRepository;
import com.c09.GoMovie.movie.entities.repositories.MovieRepository;
import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.entities.repositories.UserRepository;

import org.neo4j.cypher.internal.compiler.v2_2.commands.indexQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import com.jayway.jsonpath.JsonPath;

@Service
@CacheConfig(cacheNames="movieService")
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MovieCommentRepository movieCommentRepository;

    static private String DOUBAN_ON_SHOW_API = "http://api.douban.com/v2/movie/in_theaters?count=100";
    
    static private String DOUBAN_DETAIL_API = "http://api.douban.com/v2/movie/subject/";
    //从豆瓣获取电影列表
    @Cacheable
	public Iterable<Movie> listMovies() {

    	System.out.println("Make all movies off show");
    	List<Movie> movies = movieRepository.findAll();
    	for (Movie movie: movies) {
    		movie.setOnShow(false);
    	}
    	movieRepository.save(movies);
    	
    	System.out.println("Fetching movies from douban");    	
    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> responseEntity = restTemplate.getForEntity(DOUBAN_ON_SHOW_API, String.class);
    	String jsonResponse = responseEntity.getBody();
    	
    	int total = JsonPath.read(jsonResponse, "$.total");
    	List<Object> ratings = JsonPath.read(jsonResponse, "$.subjects[*].rating.average");    	
    	List<List<String>> geners = JsonPath.read(jsonResponse, "$.subjects[*].genres");
    	List<String> titles = JsonPath.read(jsonResponse, "$.subjects[*].title");
    	List<String> originaltitles =  JsonPath.read(jsonResponse, "$.subjects[*].original_title");
    	List<String> originalIdList = JsonPath.read(jsonResponse, "$.subjects[*].id");
    	List<String> imageUrlList = JsonPath.read(jsonResponse, "$.subjects[*].images.medium");

    	List<Movie> onShowMovies = new ArrayList<Movie>();
    	
        for (int k = 0 ; k < total ; k++) {

	        Movie movie = new Movie();
	        
	        Double rating;
	        try {
	        	rating = (Double) ratings.get(k);
	        } catch (Exception e) {
	        	Integer temp = (Integer) ratings.get(k);
	        	rating = temp.doubleValue();
	        }
	        movie.setRating(rating);
	        movie.setGenres(StringUtils.collectionToDelimitedString(geners.get(k), ","));
	        movie.setTitle(titles.get(k));
	        movie.setId(Long.parseLong(originalIdList.get(k)));
	        movie.setOriginalTitle(originaltitles.get(k));
	        movie.setImageUrl(imageUrlList.get(k));
	        movie.setOnShow(true);
	        
    		onShowMovies.add(movie);
    		
	    }
        
        movieRepository.save(onShowMovies);
		return onShowMovies;
	}

    
    //获取单部电影
	@Cacheable(condition = "#result != null")
	public Movie getMovieById(long id) {
		return movieRepository.findOne(id);
	}
	
	//推荐电影
	@Cacheable(condition = "#result != null")
	public Movie getRecommendedMovie() {
		return movieRepository.findOne(0);
	}
	
	//获取单部电影的详情
	@Cacheable
	public Map<String, Object> getMovieDetails(String originalId) {
    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> responseEntity = restTemplate.getForEntity(DOUBAN_DETAIL_API + originalId, String.class);
    	String detailString = responseEntity.getBody();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("summary", JsonPath.read(detailString, "$.summary"));
    	map.put("wishCount", JsonPath.read(detailString, "$.wish_count"));
    	
    	List<Map<String, String>> casts = new ArrayList<Map<String, String>>();
    	List<String> castNameList = JsonPath.read(detailString, "$.casts[*].name");
    	List<String> castAvatarUrlList = JsonPath.read(detailString, "$.casts[*].avatars.small");    	
    	for (int i = 0 ; i < castNameList.size() ; i++) {
    		Map<String, String> m = new HashMap<String, String>();
    		m.put("name", castNameList.get(i));
    		m.put("avatarUrl", castAvatarUrlList.get(i));
    		casts.add(m);
    	}
    	// map.put("casts", casts);
    	
    	List<Map<String, String>> directors = new ArrayList<Map<String, String>>();
    	List<String> directorNameList = JsonPath.read(detailString, "$.casts[*].name");
    	List<String> directorAvatarUrlList = JsonPath.read(detailString, "$.casts[*].avatars.small");    	
    	for (int i = 0 ; i < directorNameList.size() ; i++) {
    		Map<String, String> m = new HashMap<String, String>();
    		m.put("name", directorNameList.get(i));
    		m.put("avatarUrl", directorAvatarUrlList.get(i));
    		directors.add(m);
    	}
    	// map.put("directors", directors);

    	return map;
	}

	//获取电影获取评论
	public List<MovieComment> listCommentsByMovieId(long id) {
		return movieCommentRepository.findByMovieId(id);
	}

	

	//获取某个评论
	public MovieComment getCommentById(long id) {
		return movieCommentRepository.findOne(id);
	}

	//创建评论
	public MovieComment createComment(MovieComment movieComment, Movie movie, User user) {
        movieComment.setUser(user);
        movieComment.setMovie(movie);
        return movieCommentRepository.save(movieComment);
	}

	//删除评论
	public void deleteComment(MovieComment movieComment) {
		movieComment.setUser(null);
		movieComment.setMovie(null);
		movieCommentRepository.delete(movieComment);
	}

	

	//根据id删除评论
	public void deleteMovieById(long id) {
		movieRepository.delete(id);
	}


	public List<Cinema> listCinemasByMovieId(long id) {
		return movieRepository.findOne(id).getCinemas();
	}
	

	
	public void collectMovie(long movieId, User user) {
		movieRepository.findOne(movieId).addUser(user);
		userRepository.save(user);
	}
	
	public void cancelCollectMovie(long movieId, User user) {
		movieRepository.findOne(movieId).removeUser(user);
		userRepository.save(user);
	}
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Movie> listUserMovieCollections(User user) {
		return userRepository.findOne(user.getId()).getMovies();
	}
}
