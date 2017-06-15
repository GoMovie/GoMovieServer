package com.c09.GoMovie.order.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c09.GoMovie.order.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
