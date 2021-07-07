package com.order2david.Product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.Product.model.ProductStatus;



public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {

	List<ProductStatus> findAllByOrderByAbbrAsc();

	ProductStatus findByAbbr(String abbr);

	
}
