package com.c09.GoMovie.order.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.c09.GoMovie.cinema.entities.Cinema;
import com.c09.GoMovie.order.entities.Order;
import com.c09.GoMovie.order.service.OrderService;
import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.service.SessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="Order", description="Order的CURD")
@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SessionService sessionService;
	
	@ApiOperation(value="获取订单")
	@RequestMapping(value = {""}, method = RequestMethod.GET)
    public Iterable<Order> listOrders() {
		return orderService.listOrders();
	}
	
	@ApiOperation(value="获取某个个订单")
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> getCinemaById(@PathVariable("id") long orderId) {
		Order order = orderService.getOrderById(orderId);
		return new ResponseEntity<Order>(order, order != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@ApiOperation(value="创建订单")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    public Order createOrder(@Valid @RequestBody String jsonString) {
		User user = sessionService.getCurrentUser();
		return orderService.createOrder(jsonString, user);
	}
	
	@ApiOperation(value="删除订单")
	@RequestMapping(value={"/{id}"}, method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user')")
    public void deleteCinemaById(@PathVariable("id") long id) {
		orderService.deleteOrderById(id);
	}
}
