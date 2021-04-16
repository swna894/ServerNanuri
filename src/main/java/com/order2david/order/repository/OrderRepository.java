package com.order2david.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.order.model.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {

	Order findByInvoice(String invoice);

    
}
