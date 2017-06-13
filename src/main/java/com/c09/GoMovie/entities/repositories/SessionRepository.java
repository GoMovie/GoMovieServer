package com.c09.GoMovie.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.entities.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
	Session findByToken(String token);
}

