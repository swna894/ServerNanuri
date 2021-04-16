package com.order2david.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.order.model.OrderItem;



public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    
}
