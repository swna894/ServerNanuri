package com.order2david.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.shop.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

	
	@EntityGraph(attributePaths = "roles")
	 Optional<Shop> findOneWithRolesByEmail(String email);
	   
	List<Shop> findAllByOrderByCompanyAsc();

	Shop findByEmail(String email);

	Shop findTopByOrderByAbbrDesc();

	Shop findByAbbr(String abbr);

    
}
