package com.c09.GoMovie.product.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.cinema.entities.Hall;
import com.c09.GoMovie.movie.entities.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Screening {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	// 开始时间
	@NotNull
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
	
	// 片长
	@NotNull
	@Column(nullable=false)
	private int runningTime;
	
	@ManyToOne(cascade = { CascadeType.MERGE,CascadeType.REFRESH }, optional = false)
	@JoinColumn(name="movie_id")
	private Movie movie;
	
	@ManyToOne(cascade = { CascadeType.MERGE,CascadeType.REFRESH }, optional = false)
	@JoinColumn(name="hall_id")
	private Hall hall;
	
	@ManyToOne(cascade = { CascadeType.MERGE,CascadeType.REFRESH }, optional = false)
	@JoinColumn(name="cinema_id")
	private Cinema cinema;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "screening", fetch = FetchType.LAZY)    
	List<Ticket> tickets = new ArrayList<Ticket>();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public int getRunningTime() {
		return runningTime;
	}
	
	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public Hall getHall() {
		return hall;
	}
	
	public void setHall(Hall hall) {
		this.hall = hall;
	}
	
	@JsonIgnore
	public List<Ticket> getTickets() {
		return tickets;
	}

	@JsonIgnore
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}
	
	@JsonIgnore
	public Cinema getCinema() {
		return cinema;
	}
	
	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}
}
