package com.c09.GoMovie.cinema.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.cinema.entities.CinemaComment;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
	List<Cinema> findByCityId(String cityId);
}
