package com.c09.GoMovie.cinema.entities.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.cinema.entities.CinemaComment;

public interface CinemaCommentRepository extends CrudRepository<CinemaComment, Long> {
	List<CinemaComment> findByCinemaId(long id);
}