package com.order2david.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.Product.model.Product;
import com.order2david.Product.repository.ProductRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("api")
public class ProductController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@PostMapping("/products")
	@Transactional
	public List<Product> postAll(@RequestBody List<Product> products) {
		String abbr = products.get(0).getAbbr();
		// 0423 Supplier supplier = supplierRepository.findByAbbr(abbr);
		// supplier.getOrders().clear();
		productRepository.deleteAllByAbbr(abbr);

		// if (result > 0) {
		// 0423 products.forEach(item -> item.setSupplier(supplier));
		products = productRepository.saveAll(products);
		// }
		return null;
	}

	@GetMapping("/products")
	public List<Product> getAll() {
		return productRepository.findAllByOrderByCodeAsc();
	}

	@DeleteMapping("/products")
	public void deleteAll(@RequestBody List<Product> items) {
		productRepository.deleteAll(items);
	}

	
//	@GetMapping("/products")
//	public List<Product> findProdutsByPagable() {
//		return productRepository.findAllByOrderByCodeAsc();
//	}
	
	@GetMapping("/products/category")
	public List<Product> findProductsByCompany(@RequestParam String abbr) {
		//Supplier supplier = supplierRepository.findByCompany(company);
		if(abbr.isEmpty()) {
			Supplier supplier = supplierRepository.findFirstByOrderByCompanyAsc();
			abbr = supplier.getAbbr();
		}
		List<Product> products = productRepository.findByAbbr(abbr);
		List<Product> sortedProducts = products.stream()
				.filter(item -> !item.getCategory().equals(""))
				.filter(distinctByKey(p -> p.getCategory()))
				.sorted(Comparator.comparing(Product::getCategory))
				.collect(Collectors.toList());
		return sortedProducts;
	}

	@GetMapping("/products/categorys")
	public List<Product> findCategories() {
		List<Product> products = productRepository.findAll();
		List<Product> sortedProducts = products.stream()
				.filter(item -> !item.getCategory().equals(""))
				.filter(distinctByKey(p -> p.getCategory()))
				.sorted(Comparator.comparing(Product::getCategory))
				.collect(Collectors.toList());
		return sortedProducts;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
