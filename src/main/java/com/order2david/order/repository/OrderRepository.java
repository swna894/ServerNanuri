package com.order2david.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.order.model.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByInvoice(String invoice);

    
}
