package com.order2david.Product;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("api")
public class ProductController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ShopRepository shopRepository;
	
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
	public Page<Product> getInit(Principal principal) {
		Supplier supplier = supplierRepository.findFirstByOrderByCompanyAsc();
		Pageable sortedBySeq = 
				  PageRequest.of(0, 36, Sort.by("seq"));
		Page<Product> page = productRepository.findByAbbrAndIsShow(supplier.getAbbr(), true, sortedBySeq);
		updateCartQty(page, principal);
		return page ;
	}
	
	@GetMapping("/products/{abbr}/{category}" )
	public Page<Product> findProdutsByPagable(@PathVariable String abbr, 
			@PathVariable String category, Pageable pageable, Principal principal ) {	
		Page<Product> page = null;
		if(category.equals("NEW")) {
			page = productRepository.findByAbbrAndIsNewAndIsShow(abbr, true, true, pageable);
		} else if(category.equals("SPECIAL")) {
			page = productRepository.findByAbbrAndIsSpecialAndIsShow(abbr, true, true, pageable);
		} else if(category.equals("CART")) {
			Page<OrderItem> orderItems = cartRepostory(abbr, principal, pageable);
			page = convertProduct(orderItems);
		} else {
			page = productRepository.findByAbbrAndCategoryAndIsShow(abbr, category, true, pageable);
		}
			updateCartQty(page, principal);
			return page;
	}
	
	private Page<Product> convertProduct(Page<OrderItem> orderItems) {
		List<Product> products = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			Product product 
				= productRepository.findByCodeAndAbbr(orderItem.getCode(),orderItem.getInvoice().substring(0, 4));
			    product.setQty(orderItem.getQty());
			    products.add(product);
		}	

		return new PageImpl<Product>(products, orderItems.getPageable(), orderItems.getTotalElements());
	}
	
	

	private Page<OrderItem> cartRepostory(String abbr, Principal principal, Pageable pageable) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		String invoice = abbr + shop.getAbbr()+"_CART";
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("code"));
		return orderRepository.findByInvoice(invoice, pageable);
	}

	private Page<Product> updateCartQty(Page<Product> page, Principal principal) {
		List<Product> products = page.getContent();	
		Optional<Order> cart = getCarts(principal, products.get(0).getAbbr());
		
		if(cart.isPresent()) {
			List<OrderItem> orderItems = cart.get().getOrderItems(); 
			for (Product product : products) {
				for (OrderItem orderItem : orderItems) {
					if(product.getCode().equals(orderItem.getCode())) {
						product.setQty(orderItem.getQty());
						break;
					}
				}
			}
		}
		return page;
	}
	
	private Optional<Order> getCarts(Principal principal, String abbr) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		String invoice = abbr + shop.getAbbr()+"_CART";
		return orderRepository.findByInvoice(invoice);
	}

	@GetMapping("/products/abbr/{abbr}" )
	public List<Product> findByAbbr(@PathVariable String abbr) {		
			List<Product> products = productRepository.findByAbbrOrderByCodeAsc(abbr); 			
			return products;
	}
	
	@GetMapping("/products/{abbr}" )
	public Page<Product> findProdutsByPagable(@PathVariable String abbr,  Pageable pageable, Boolean newp, Principal principal ) {		
			Page<Product> page = productRepository.findByAbbrAndIsShow(abbr, true, pageable); 
			updateCartQty(page, principal);
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
