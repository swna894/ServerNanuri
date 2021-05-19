package com.order2david.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.order.model.OrderType;



public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByInvoice(String invoice);
	
	@Query(value = "SELECT i FROM OrderItem i WHERE i.invoice = :inv")
	Page<OrderItem> findByInvoice(@Param("inv") String invoice, Pageable pageable);

	List<Order> findByStatusAndInvoiceStartsWith(OrderType order, String invoice);

	List<Order> findByInvoiceContains(String cart);

	List<Order> findByShopAbbrOrderByOrderDate(String abbr);

	List<Order> findByShopAbbrAndStatusOrderByOrderDateDesc(String abbr, OrderType order);

	List<Order> findByStatusAndInvoiceStartsWithAndShopAbbr(OrderType order, String supplier, String abbr);

	List<Order> findByShopAbbrAndInvoiceContainsAndStatusOrderByOrderDateDesc(String abbr, String abbr2,
			OrderType order);



    
}
