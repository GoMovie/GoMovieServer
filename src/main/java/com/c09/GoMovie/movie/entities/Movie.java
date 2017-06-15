package com.c09.GoMovie.movie.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.product.entities.Screening;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Movie {

	@Id
	@NotNull
//	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(nullable=false)
	private String title;

	private String originalTitle;

	@NotNull
	@Column(nullable=false)
	private double rating;

	private String genres;

	private String imageUrl;
	
	
	private boolean onShow;
	
	public boolean isOnShow() {
		return onShow;
	}

	@JsonIgnore
	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}
	
	public void addCinema(Cinema cinema) {
		cinemas.add(cinema);
	}
	
	/*
	 * 新增Screening List，和Screening形成一对多关系
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "movie", fetch = FetchType.LAZY)    
	List<Screening> screenings = new ArrayList<Screening>();
	
	@JsonBackReference
	public List<Screening> getScreenings() {
		return screenings;
	}

	@JsonIgnore
	public void setScreenings(List<Screening> screenings) {
		this.screenings = screenings;
	}
	
	public void addScreening(Screening screening) {
		screenings.add(screening);
	}
}
