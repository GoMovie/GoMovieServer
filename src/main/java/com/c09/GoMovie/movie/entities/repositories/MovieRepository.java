package com.c09.GoMovie.movie.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.movie.entities.Movie;


public interface MovieRepository extends CrudRepository<Movie, Long> {

}
