package com.c09.GoMovie.cinema.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.cinema.entities.CinemaComment;
import com.c09.GoMovie.cinema.entities.Hall;
import com.c09.GoMovie.cinema.entities.Seat;
import com.c09.GoMovie.cinema.entities.repositories.CinemaRepository;
import com.c09.GoMovie.cinema.entities.repositories.CinemaCommentRepository;
import com.c09.GoMovie.cinema.entities.repositories.HallRepository;
import com.c09.GoMovie.cinema.entities.repositories.SeatRepository;
import com.c09.GoMovie.user.entities.User;

@Service
public class CinemaService {
	@Autowired
	private CinemaRepository cinemaRepository;
	
	@Autowired
	private CinemaCommentRepository cinemaCommentRepository;
	
	@Autowired
	private HallRepository hallRepository;
	
	@Autowired
	private SeatRepository seatRepository;
	
	public Iterable<Cinema> listCinemas() {
		return cinemaRepository.findAll();
	}
	
	public Cinema createCinema(Cinema cinema) {
		return cinemaRepository.save(cinema);
	}
	
	public List<Cinema> listCinemasByCityId(int cityId) {
		return cinemaRepository.findByCityId(cityId);
	}
	
	public Cinema getCinemaById(long id) {
		return cinemaRepository.findOne(id);
	}
	
	public Cinema updataCinema(Cinema cinema) {
		return cinemaRepository.save(cinema);
	}
	
	public void deleteCinemaById(long id) {
		cinemaRepository.delete(id);
	}
	
	public List<CinemaComment> listCommentsByCinemaId(long id) {
		return cinemaCommentRepository.findByCinemaId(id);
	}
	
	public CinemaComment createComment(CinemaComment cinemaComment, Cinema cinema, User user) {
		cinemaComment.setUser(user);
		cinemaComment.setCinema(cinema);
		return cinemaCommentRepository.save(cinemaComment);
	}
	
	public CinemaComment getCommentById(long id) {
		return cinemaCommentRepository.findOne(id);
	}
	
	public void deleteComment(CinemaComment cinemaComment) {
//		cinemaComment.setUser(null);
//		cinemaComment.setCinema(null);
		cinemaCommentRepository.delete(cinemaComment);
	}
	
	public List<Hall> listHallsByCinemaId(long id) {
		return hallRepository.findByCinemaId(id);
	}
	
	public Hall createHall(Hall hall, Cinema cinema) {
		hall.setCinema(cinema);
		return hallRepository.save(hall);
	}
	
	public Hall getHallById(long id) {
		return hallRepository.findOne(id);
	}
	
	public void deleteHall(Hall hall) {
		hallRepository.delete(hall);
	}
	
	public List<Seat> listSeatsByHallId(long id) {
		return seatRepository.findByHallId(id);
	}
	
	public Seat creatSeat(Seat seat, Hall hall) {
		seat.setHall(hall);
		return seatRepository.save(seat);
	}
	
	public Seat getSeatById(long id) {
		return seatRepository.findOne(id);
	}
	
	public void deleteSeat(Seat seat) {
		seatRepository.delete(seat);
	}
	
}