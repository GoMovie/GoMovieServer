package com.c09.GoMovie.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.service.SessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="Session", description="管理Session")
@RestController
@RequestMapping("/session")
public class SessionController {
	
	@Autowired
	private SessionService sessionService;
	

    // HTTP Basic Auth
	@ApiOperation(value="用户登录")
	@RequestMapping(value={"", "/"}, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority({'admin', 'user'})")
    public void login() {
    	sessionService.login();
    }
    
	@ApiOperation(value="用户注销")
    @RequestMapping(value={"", "/"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority({'admin', 'user'})")
    public void logout() {
    	sessionService.logout();
    }
	
	@ApiOperation(value="获取在线用户")
    @RequestMapping(value={"", "/"}, method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority({'admin', 'user'})")
    public User getCurrentUser() {
    	return sessionService.getCurrentUser();
    }
}
