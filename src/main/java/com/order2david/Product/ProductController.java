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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		return productRepository.saveAll(products);
	}

	@PutMapping("/products")
	@Transactional
	public List<Product> putAll(@RequestBody List<Product> products) {
		// if (result > 0) {
		// 0423 products.forEach(item -> item.setSupplier(supplier));
		products = productRepository.saveAll(products);
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

	@GetMapping("/products/init")
	public Page<Product> getInit() {
		Supplier supplier = supplierRepository.findFirstByOrderByCompanyAsc();
		Pageable sortedBySeq = 
				  PageRequest.of(0, 36, Sort.by("seq"));
		Page<Product> page = productRepository.findByAbbrAndIsShow(supplier.getAbbr(), true, sortedBySeq);
		return page ;
	}
	
	@GetMapping("/products/{abbr}/{category}" )
	public Page<Product> findProdutsByPagable(@PathVariable String abbr, 
			@PathVariable String category, Pageable pageable ) {	
			//System.err.println("1. abbr = " + abbr + " category = " + category +  " pageable = " + pageable );
			Page<Product> page = productRepository.findByAbbrAndCategoryAndIsShow(abbr, category, true, pageable);
			//System.err.println("1. " + page.getContent().size());
		return page;

	}
	
	@GetMapping("/products/abbr/{abbr}" )
	public List<Product> findByAbbr(@PathVariable String abbr) {		
			List<Product> products = productRepository.findByAbbrOrderByCodeAsc(abbr); 			
		return products;
	}
	
	@GetMapping("/products/{abbr}" )
	public Page<Product> findProdutsByPagable(@PathVariable String abbr,  Pageable pageable ) {		
			//System.err.println("2. abbr = " + abbr +   " pageable = " + pageable );
			Page<Product> page = productRepository.findByAbbrAndIsShow(abbr, true, pageable); 
			//System.err.println("2. " + page.getContent().size());			
		return page;
	}

	
	@GetMapping("/products/category")
	public List<Product> findProductsByCompany(@RequestParam String abbr) {
		if(abbr.isEmpty()) {
			Supplier supplier = supplierRepository.findFirstByOrderByCompanyAsc();
			abbr = supplier.getAbbr();
		}
		List<Product> products = productRepository.findByAbbr(abbr);
		List<Product> sortedProducts = products.stream()
				.filter(item -> item.isShow())
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
				.filter(item -> item.isShow())
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
