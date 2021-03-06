package com.c09.GoMovie.user.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.c09.GoMovie.user.entities.User;

public interface UserRepository extends JpaRepository<User, Long> { 
	
	@Query("select u from User u where u.username=?1 and u.password=?2")
	User login(String email, String password);
	
	User findByUsernameAndPassword(String username, String password);
	
	User findByUsername(String username);  
}

