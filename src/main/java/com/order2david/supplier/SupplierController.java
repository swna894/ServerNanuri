package com.order2david.supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.shop.repository.ShopJdbcRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;


@RestController
@RequestMapping("/api")
public class SupplierController {

	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SupplierRepository supplierRepository;
	
	@Autowired
	ShopJdbcRepository shopJdbcRepository;
	
	@PostMapping("/suppliers/init")
	public List<Supplier> init(@RequestBody List<Supplier> suppliers) {
		supplierRepository.deleteAll();
		List<Supplier> suppliersEntity = supplierRepository.saveAll(suppliers);
		for (Supplier supplier : suppliersEntity) {
			shopJdbcRepository.newColumn(supplier);
		}
		return suppliersEntity;
	}
	
	@PostMapping("/suppliers")
	public List<Supplier> postAll(@RequestBody List<Supplier> suppliers) {
		return supplierRepository.saveAll(suppliers);
	}
	
	@PostMapping("/supplier")
	public  Supplier post(@RequestBody Supplier supplier) {
		// shop column 생성
		shopJdbcRepository.newColumn(supplier);
		return supplierRepository.save(supplier);
	}
	
	@PutMapping("/suppliers")
	public List<Supplier> putAll(@RequestBody List<Supplier> suppliers) {
		return supplierRepository.saveAll(suppliers);
	}
	
	@DeleteMapping("/suppliers")
	public void deleteAll(@RequestBody List<Supplier> suppliers) {
		// shop column 삭제
		for (Supplier supplier : suppliers) {
			shopJdbcRepository.deleteColumn(supplier);
		}
		supplierRepository.deleteAll(suppliers);
	}

	@GetMapping("/suppliers")
	public List<Supplier> findAll() {
		return supplierRepository.findAllByOrderByCompanyAsc();
	}
	
	@GetMapping("/supplier")
	public Supplier find() {
		return supplierRepository.findFirstByOrderByCompanyAsc();
	}
	
	@GetMapping("/supplier/{abbr}")
	public Supplier findByAbbr(@PathVariable String abbr) {
		return supplierRepository.findByAbbr(abbr);
	}
}
