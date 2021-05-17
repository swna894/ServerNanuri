package com.order2david.etc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.etc.model.Company;
import com.order2david.etc.repository.CompanyRepository;

@RestController
@RequestMapping("api/")
public class CompanyController {

	@Autowired
	CompanyRepository companyRepository;
	
	@GetMapping("/etc/company")
	public Company findOne() {
		return companyRepository.findAll().get(0);
	}
	
	@PutMapping("company")
	public Company put(@RequestBody Company company) {
		return companyRepository.save(company);
	}
}
