package com.c09.GoMovie.user.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.jpa.criteria.expression.SearchedCaseExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.entities.repositories.UserRepository;

@Service
public class SessionService {
	
	@Autowired
	UserRepository userRepository;
		
	public User getCurrentUser() {
		Authentication authentication = (Authentication) SecurityContextHolder.getContext()
		    .getAuthentication();
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			return null;
		} else {
			return (User) authentication.getPrincipal();
		}
	}
	
	public void login() {
		// Todos
		return;
	}
	
	public void logout() {
		// Todos
		return;
	}
	
}
