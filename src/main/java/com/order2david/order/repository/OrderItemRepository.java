package com.order2david.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.order.model.OrderItem;
import com.order2david.order.model.OrderType;



public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	OrderItem findFirstyByCodeAndStatusAndInvoiceStartsWithOrderByCreatedDesc(String code, OrderType order,
			String invoice);

    
}
