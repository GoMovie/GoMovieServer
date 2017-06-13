package com.c09.GoMovie.movie.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.movie.entities.Movie;


public interface MovieRepository extends JpaRepository<Movie, Long> {

}