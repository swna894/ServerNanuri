package com.order2david.Product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order2david.Product.model.Category;



public interface CatetoryRepository extends JpaRepository<Category, Long> {


	List<Category> findByAbbrOrderByCategoryAsc(String abbr);
	
	@Modifying() 
	@Query("DELETE FROM Category  tm WHERE tm.abbr = :abbr")
    int  deleteAllByAbbr(@Param("abbr") String abbr);
	
}
