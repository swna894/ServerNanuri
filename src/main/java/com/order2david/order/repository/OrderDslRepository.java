package com.order2david.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.order2david.order.model.Order;

public interface OrderDslRepository extends JpaRepository<Order, Long>,
												QuerydslPredicateExecutor<Order>{

}
	