package com.order2david.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.order2david.order.model.OrderItem;
import com.order2david.order.model.OrderType;



public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	OrderItem findFirstyByCodeAndStatusAndInvoiceStartsWithOrderByCreatedDesc(String code, OrderType order,
			String invoice);
	
	@Query("SELECT DISTINCT code FROM OrderItem WHERE invoice IN (?1)")
	List<String> findByInvoiceInOrderByCodeAsc(List<String> invoices);

    
}
