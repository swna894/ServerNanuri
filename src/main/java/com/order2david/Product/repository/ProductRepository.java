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

	Page<Product> findByAbbrAndIsNewAndIsShow(String abbr, boolean b, boolean c, Pageable pageable);

	Page<Product> findByAbbrAndIsSpecialAndIsShow(String abbr, boolean b, boolean c, Pageable pageable);

	Page<Product> findByIsShowAndDescriptionContains(boolean b, String search, Pageable pageable);

	Page<Product> findByDescriptionContainsOrCodeContainsAndIsShow(String description, String code, boolean b,
			Pageable pageable);

	Page<Product> findByDescriptionContainsAndIsShowOrCodeContainsAndIsShow(String description, boolean b, String code,
			boolean c, Pageable pageable);

	Page<Product> findByAbbrAndDescriptionContainsAndIsShowOrAbbrAndCodeContainsAndIsShow(String abbr, String search,
			boolean b, String abbr2, String search2, boolean c, Pageable pageable);

	Page<Product> findByAbbrAndCodeIn(String abbr, List<String> codeList, Pageable pageable);

	Page<Product> findByAbbrAndIsShowAndCodeIn(String abbr, boolean b, List<String> codes, Pageable pageable);

	Double countByAbbr(String abbr);

	Page<Product> findByAbbrAndIsShowAndCategory(String abbr, boolean b, String category, Pageable pageable);

	Page<Product> findByAbbrAndIsShowAndCategoryContains(String abbr, boolean b, String category, Pageable pageable);

	List<Product> findByAbbrAndIsShowAndCategoryContains(String abbr, boolean b, String search);


	List<Product> findByAbbrAndCategoryContainsOrAbbrAndDescriptionContains(String abbr, String search, String abbr2,
			String search2);

	List<Product> findByAbbrAndCategoryContainsOrAbbrAndDescriptionContainsOrAbbrAndCodeContainsOrAbbrAndBarcodeContains(
			String abbr, String search, String abbr2, String search2, String abbr3, String search3, String abbr4,
			String search4);

	Double countByAbbrAndIsNew(String abbr, boolean b);

	Double countByAbbrAndIsSpecial(String abbr, boolean b);

	Double countByAbbrAndIsPhoto(String abbr, boolean b);

	Double countByAbbrAndIsShow(String abbr, boolean b);


	List<Product> findByAbbrOrderByCodeAsc(String company);
	
	
}
