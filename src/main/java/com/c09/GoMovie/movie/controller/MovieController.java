package com.c09.GoMovie.movie.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.movie.entities.Movie;
import com.c09.GoMovie.movie.entities.MovieComment;
import com.c09.GoMovie.movie.service.MovieService;
import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.service.SessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


@Api(value="Movie", description="Movie的CURD")
@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@Autowired
	private SessionService sessionService;

	@ApiOperation(value="获取当前上映电影")
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public Iterable<Movie> listMovies() {
    	return movieService.listMovies();
    }


	@ApiOperation(value="获取某部电影")
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") long movieId) {
    	Movie movie = movieService.getMovieById(movieId);
    	return new ResponseEntity<Movie>(movie, movie != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
	
	@ApiOperation(value="获取某电影的对应的影院")
    @RequestMapping(value="/{id}/cinemas", method = RequestMethod.GET)
    public List<Cinema> listCinemasByMovieId(@PathVariable("id") long movieId) {
    	return movieService.listCinemasByMovieId(movieId);
    }

	@ApiIgnore
    @RequestMapping(value={"/{id}"}, method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('admin')")
    public void updateMovie(@Valid @RequestBody Movie movie, @PathVariable("id") long id) {
    	movie.setId(id);
    	movieService.updateMovie(movie);
    }

	@ApiIgnore
    @RequestMapping(value={"/{id}"}, method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('admin')")
    public void deleteMovieById(@PathVariable("id") long id) {
    	movieService.deleteMovieById(id);
    }

	@ApiOperation(value="获取某电影的所有评论")
    @RequestMapping(value="/{id}/comments", method = RequestMethod.GET)
    public List<MovieComment> listCommentsByMovieId(@PathVariable("id") long id) {
    	return movieService.listCommentsByMovieId(id);
    }

	@ApiOperation(value="创建评论")
	@RequestMapping(value={"/{id}/comments"}, method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    public MovieComment createComment(@Valid @RequestBody MovieComment movieComment, @PathVariable("id") long id) {
    	User user = sessionService.getCurrentUser();
    	Movie movie = movieService.getMovieById(id);
    	return movieService.createComment(movieComment, movie, user);
    }

	@ApiOperation(value="删除评论")
    @RequestMapping(value={"/{movieId}/comments/{commentId}"}, method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    public void deleteCommentById(@PathVariable("commentId") long id) {
    	User user = sessionService.getCurrentUser();
    	MovieComment movieComment = movieService.getCommentById(id);
		if (user.getId() == movieComment.getUser().getId()) {
			movieService.deleteComment(movieComment);
		}
    }
    
	@ApiOperation(value="获取某部电影详细信息")
    @RequestMapping(value="/{id}/details", method = RequestMethod.GET)
    public Map<String, Object> getMovieDetailsByOriginalId(@PathVariable("id") String id) {
		return movieService.getMovieDetails(id);
	}
	
	@ApiOperation(value="收藏电影")
    @RequestMapping(value={"/{id}/collection"}, method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
	@PreAuthorize("hasAnyAuthority('admin', 'user')")
    public void collectMovie(@PathVariable("id") long id) {
		User user = sessionService.getCurrentUser();
		movieService.collectMovie(id, user);
	}
	
	@ApiOperation(value="取消收藏电影")
    @RequestMapping(value={"/{id}/collection"}, method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyAuthority('admin', 'user')")
    public void cancelCollectMovie(@PathVariable("id") long id) {
		User user = sessionService.getCurrentUser();
		movieService.cancelCollectMovie(id, user);
	}
	
	@ApiOperation(value="获取用户收藏的电影")
    @RequestMapping(value={"/collections"}, method=RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('admin', 'user')")
    public List<Movie> cancelCollectMovie() {
		User user = sessionService.getCurrentUser();
		return movieService.listUserMovieCollections(user);
	}
	
}
