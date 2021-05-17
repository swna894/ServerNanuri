package com.order2david.etc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order2david.etc.model.Company;


public interface CompanyRepository extends JpaRepository<Company, Long> {

   
}
