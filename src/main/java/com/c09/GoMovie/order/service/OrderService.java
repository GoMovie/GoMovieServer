package com.c09.GoMovie.order.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c09.GoMovie.order.entities.Order;
import com.c09.GoMovie.order.entities.repositories.OrderRepository;
import com.c09.GoMovie.product.entities.Ticket;
import com.c09.GoMovie.product.entities.repositories.TicketRepository;
import com.c09.GoMovie.user.entities.User;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	public Iterable<Order> listOrders() {
		return orderRepository.findAll();
	}
	
	public Order createOrder(String jsonString, User user) {
		Order order = new Order();
		JSONObject json = new JSONObject(jsonString);
		

		 //解析json

		order.setUser(user);

		 //设置“创建时间”属性:yyyy-mm-dd hh:mm

		String createTimeString = json.getString("createTime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");  
	    Date createTimeDate;
		try {
			createTimeDate = sdf.parse(createTimeString);
			order.setCreateTime(createTimeDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		 //设置Tickets和amount总价

		double amount = 0;
		JSONArray ticketJSONARRAY = json.getJSONArray("ticketsId");
		
		for (int i = 0; i < ticketJSONARRAY.length(); ++i) {
			Ticket ticket = ticketRepository.findOne(ticketJSONARRAY.getLong(i));
			order.addTicket(ticket);
			amount += ticket.getPrice();
		}
		order.setAmount(amount);
		
		return orderRepository.save(order);
	}
	
	
	
	public Order getOrderById(long id) {
		return orderRepository.findOne(id);
	}
	
	public Order updateOrder(Order order) {   
		return orderRepository.save(order);   
	}
	
	public void deleteOrderById(long id) {   
		orderRepository.delete(id);   
	}
}
