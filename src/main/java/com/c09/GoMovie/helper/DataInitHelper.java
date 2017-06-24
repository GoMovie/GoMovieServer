package com.c09.GoMovie.helper;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.cinema.entities.CinemaComment;
import com.c09.GoMovie.cinema.entities.Hall;
import com.c09.GoMovie.cinema.entities.Seat;
import com.c09.GoMovie.cinema.entities.repositories.CinemaCommentRepository;
import com.c09.GoMovie.cinema.entities.repositories.CinemaRepository;
import com.c09.GoMovie.cinema.entities.repositories.HallRepository;
import com.c09.GoMovie.cinema.entities.repositories.SeatRepository;
import com.c09.GoMovie.movie.entities.Movie;
import com.c09.GoMovie.movie.entities.MovieComment;
import com.c09.GoMovie.movie.entities.repositories.MovieCommentRepository;
import com.c09.GoMovie.movie.entities.repositories.MovieRepository;
import com.c09.GoMovie.movie.service.MovieService;
import com.c09.GoMovie.order.entities.Order;
import com.c09.GoMovie.order.entities.repositories.OrderRepository;
import com.c09.GoMovie.product.entities.Screening;
import com.c09.GoMovie.product.entities.Ticket;
import com.c09.GoMovie.product.entities.repositories.ScreeningRepository;
import com.c09.GoMovie.product.entities.repositories.TicketRepository;
import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.entities.repositories.UserRepository;
import com.jayway.jsonpath.JsonPath;


@Component
public class DataInitHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieCommentRepository movieCommentRepository;
    
    @Autowired
    CinemaRepository cinemaRepository;
    
    @Autowired
    CinemaCommentRepository cinemaCommentRepository;
    
    @Autowired
    HallRepository hallRepository;
    
    @Autowired
    SeatRepository seatRepository;

    @Autowired
    MovieService movieService;
    
    @Autowired
    ScreeningRepository screeningRepository;

    @Autowired
    TicketRepository ticketRepository;
    
    @Autowired
	OrderRepository orderRepository;
    
    
    @PostConstruct
    public void dateInit() {
    	//Init User
    	User admin = new User();
        admin.setPassword("admin");
        admin.setUsername("admin");
        admin.setRole(User.ROLE.admin);
        userRepository.save(admin);
    	
        
        //Init Movie
        Iterable<Movie> movies = movieService.listMovies();   
        User user = new User();
        user.setUsername("movie");
        user.setPassword("movie");
        user.setRole(User.ROLE.user);
        user.setNickname("movie");
        userRepository.save(user);

        //for all movie add comments
        for (Movie movie: movies) {
  		        MovieComment movieComment = new MovieComment();
  		        movieComment.setScore(5);
  		        movieComment.setContent("Really nice movie!!!!");
  		        movieComment.setUser(user);
  		        movieComment.setMovie(movie);
  		        movieCommentRepository.save(movieComment);
        }
        
        //Init cinema
        user = new User();
        user.setUsername("cinema");
        user.setPassword("cinema");
        userRepository.save(user);
        
        
        movies = movieRepository.findAll();
        
        
        //Init product
        String[] cinemaName = {"大学城珠江国际影城", "横店电影城（广州南沙店）", "中影火山湖（番禺区店）"};
        String[] hallType = {"2D影厅", "3D影厅", "IMAX影厅"};
        String[] cinemaAddresses = {"番禺区小谷围街贝岗村中二横路1号高高新天地商业广场B2B001铺", "南沙区进港大道奥园海景城4楼(蕉地铁站)", "广州市番禺区桥南街桥南路108号3楼"};
        
        for (int k = 0 ; k < 3 ; k++) {
        	
	        Cinema cinema = new Cinema();
	        cinema.setName(cinemaName[k]);
	        cinema.setAddress(cinemaAddresses[k]);
	        cinema.setPhone("020-66666666");
	        cinema.setLongitude(113.0);
	        cinema.setLatitude(23.0);
	        cinema.setCityId("020");
	        cinema.setScore(3.0);
	        cinema.setIntroduction("看你想看的，嘿嘿嘿");
	        
	        
	        for (int i = 0; i < 3; i++) {
	        	Hall hall = new Hall();
	        	hall.setName(hallType[i]);
	        	
	        	for (int j = 0; j < 10; j++) {
	        		for (int c = 0 ; c < 10 ; c++) {
	        			if ((j < 4 && c < 2) || (j < 4 && c > 7)) {
	        				continue;
	        			}
		        		Seat seat = new Seat();
		        		seat.setCol(c);
		        		seat.setRow(j);
		        		seat.setCoordinateX(j);
		        		seat.setCoordinateY(c);
		        		
		        		hall.addSeat(seat);
	        		}
	        	}
	        	
	        	cinema.addHall(hall);
	        }
	        
	        for (int i = 0 ; i < movies.size() ; i++) {
	        	cinema.addMovie(movies.get(i));
	        }

	        for (int i = 0 ; i < 3 ; i++) {
		        CinemaComment cinemaComment = new CinemaComment();
		        cinemaComment.setScore(i);
		        cinemaComment.setContent("能嘻嘻嘻的电影院");

		        cinemaComment.setUser(user);

		        cinema.addCinemaComment(cinemaComment);
	        }
	        cinemaRepository.save(cinema);

        }
        
    	movies = movieRepository.findAll();
    	List<Cinema> cinemas = cinemaRepository.findAll();
    	
    	for (Movie movie: movies) {
    		for (Cinema cinema: cinemas) {
		        List<Hall> hallList = cinema.getHallls();
		        for (Hall hall : hallList) {
		        	Screening screening = new Screening();
		    		screening.setCinema(cinema);
		    		screening.setHall(hall);
		    		screening.setMovie(movie);
		    		screening.setRunningTime(120);
		    		
		    		try {
		    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						Date startTimeDate = sdf.parse("2017-06-24 12:00");
						screening.setStartTime(startTimeDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
		    		
		    		//add screening
		    		screeningRepository.save(screening);
		    		
		    		List<Seat> seatList = hall.getSeats();
		    		for (Seat seat : seatList) {
		    			Ticket ticket = new Ticket();
		        		ticket.setPrice(20);
		        		ticket.setScreening(screening);
		        		ticket.setSeat(seat);
		        		ticketRepository.save(ticket);
		    		}
		        }
    		}
    	}
    
    //all init!!
    }
    


    @PostConstruct
    public void foobarDataInit(){
    	// TODO movie data init
    	System.err.println("Init Error");
    }
}
