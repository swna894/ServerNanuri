package com.order2david.supplier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.supplier.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	List<Supplier> findAllByOrderByCompanyAsc();

	Supplier findByAbbr(String abbr);

	Supplier findFirstByOrderByCompanyAsc();

	Supplier findByCompany(String company);

	List<Supplier> findAllByOrderByAbbrAsc();

	List<Supplier> findByIsActiveOrderByCompanyAsc(boolean b);



    
}
