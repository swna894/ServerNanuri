package com.order2david.Product;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.Product.model.Category;
import com.order2david.Product.repository.CatetoryRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("api")
public class CategoryController {

	@Autowired
	CatetoryRepository catetoryRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@PostMapping("/categorys")
	@Transactional
	public List<Category> postAll(@RequestBody List<Category> categorys) {

		catetoryRepository.deleteAllByAbbr(categorys.get(0).getAbbr());
//		if (result > 0) {
			categorys = catetoryRepository.saveAll(categorys);
//		}

		return categorys;
	}

	@GetMapping("/categorys")
	public List<String> findByAbbr(@RequestParam String abbr) {
		// 초기 카테고리 필요시
		if (abbr.isEmpty()) {
			Supplier supplier = supplierRepository.findFirstByOrderBySeqAsc();
			abbr = supplier.getAbbr();
		}
		List<Category> products = catetoryRepository.findByAbbrOrderByCategoryAsc(abbr);
		return products.stream().map(item -> item.getCategory()).collect(Collectors.toList());
	}

}
