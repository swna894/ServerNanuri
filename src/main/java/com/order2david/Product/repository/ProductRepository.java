package com.order2david.Product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order2david.Product.model.Product;



public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByOrderByCodeAsc();

	void deleteByAbbr(String abbr);
	
//	@Modifying
//	@Query("delete from  product  b  where  b.abbr =:abbr")
	
	@Modifying() 
	@Query("DELETE FROM Product  tm WHERE tm.abbr = :abbr")
    int  deleteAllByAbbr(@Param("abbr") String abbr);

	Product findByCode(String code);

	Product findByCodeAndAbbr(String code, String abbr);

	List<Product> findByAbbr(String company);

	List<Product> findDistinctCategoryByAbbr(String company);

	Page<Product> findByAbbrAndCategory(String abbr, String category, Pageable pageable);

	Page<Product> findByAbbr(String abbr, Pageable pageable);

	Page<Product> findByAbbrAndCategoryAndIsShow(String abbr, String category, boolean b, Pageable pageable);

	Page<Product> findByAbbrAndIsShow(String abbr, boolean b, Pageable pageable);

}
