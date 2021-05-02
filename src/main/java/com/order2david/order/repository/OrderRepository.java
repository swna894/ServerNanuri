package com.order2david.order.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;



public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByInvoice(String invoice);
	
	@Query(value = "SELECT i FROM OrderItem i WHERE i.invoice = :inv")
	Page<OrderItem> findByInvoice(@Param("inv") String invoice, Pageable pageable);

    
}
