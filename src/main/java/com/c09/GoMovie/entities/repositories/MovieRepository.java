package com.c09.GoMovie.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.entities.Movie;


public interface MovieRepository extends CrudRepository<Movie, Long> {

}
