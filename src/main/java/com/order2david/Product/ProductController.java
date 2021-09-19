package com.order2david.Product;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.qlrm.mapper.JpaResultMapper;
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
import com.order2david.Product.model.ProductManage;
import com.order2david.Product.model.ProductStatus;
import com.order2david.Product.repository.ProductRepository;
import com.order2david.Product.repository.ProductStatusRepository;
import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.order.model.OrderType;
import com.order2david.order.repository.OrderItemRepository;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;
import com.order2david.util.MyDate;

@RestController
@RequestMapping("api")
public class ProductController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());
	static String NEW = "NEW";
	static String SPECIAL = "SPECIAL";
	static String CART = "CART";
	static String SEARCH = "SEARCH";
	static String ORDERED = "ORDERED";
	static String ALL = "All";

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	ProductStatusRepository productStatusRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@PersistenceContext
	EntityManager em;
	
	@PostMapping("/products")
	@Transactional
	public List<Product> postAll(@RequestBody List<Product> products) {
		String abbr = products.get(0).getAbbr();
		Supplier supplier = supplierRepository.findByAbbr(abbr);
		// 0423 supplier.getOrders().clear();
		products.forEach(item -> item.setCompany(supplier.getCompany()));

		if (products.get(0).isCheck()) {
			productRepository.deleteAllByAbbr(abbr);
		}
		System.err.println(products.size());
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
		Supplier supplier = supplierRepository.findFirstByOrderBySeqAsc();
		Pageable sortedBySeq = PageRequest.of(0, 36, Sort.by("seq"));
		Page<Product> page = productRepository.findByAbbrAndIsShow(supplier.getAbbr(), true, sortedBySeq);
		
		updateCartQty(page, principal);
		updateCartHistory(page, principal);
		return page;
	}

	@GetMapping("/products/{abbr}/{category}")
	public Page<Product> findProdutsByPagable(@PathVariable String abbr, @PathVariable String category,
			Pageable pageable, @RequestParam(required = false) String search,
			@RequestParam(required = false) String condition, Boolean noCheckCart, Principal principal) {
		Page<Product> page = null;

		if (category.equals(NEW)) {
			page = productRepository.findByAbbrAndIsNewAndIsShow(abbr, true, true, pageable);

		} else if (category.equals(SPECIAL)) {
			page = productRepository.findByAbbrAndIsSpecialAndIsShow(abbr, true, true, pageable);

		} else if (category.equals(CART)) {
			Page<OrderItem> orderItems = cartRepostory(abbr, principal, pageable);
			page = convertProduct(orderItems);

		} else if (category.equals(ORDERED)) {
			Shop shop = shopRepository.findByEmail(principal.getName());
			String supplier = abbr;

			List<Order> orders = orderRepository.findByStatusAndInvoiceStartsWithAndShopAbbr(OrderType.ORDER, supplier,
					shop.getAbbr());
			List<String> invoices = orders.stream().map(item -> item.getInvoice()).collect(Collectors.toList());
			List<String> codes = orderItemRepository.findByInvoiceInOrderByCodeAsc(invoices);
			page = productRepository.findByAbbrAndIsShowAndCodeIn(abbr, true, codes, pageable);

		} else if (category.equals(SEARCH)) {
			search = search.replaceAll("_", "/");
			if (condition.equals(ALL)) {
				pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
						Sort.by("company").ascending().and(Sort.by("seq")));
				page = productRepository.findByDescriptionContainsAndIsShowOrCodeContainsAndIsShow(search, true, search,
						true, pageable);
			} else {
				page = productRepository.findByAbbrAndDescriptionContainsAndIsShowOrAbbrAndCodeContainsAndIsShow(abbr,
						search, true, abbr, search, true, pageable);
			}

		} else {
			category = category.replaceAll("_", "/");
			page = productRepository.findByAbbrAndIsShowAndCategoryContains(abbr, true, category, pageable);
		}
		
		if (!page.getContent().isEmpty()) {
			updateCartQty(page, principal);
			updateCartHistory(page, principal);
		}
		
		return page;
	}

	// @GetMapping("/products/search/{search}" )
	// public Page<Product> findBySearch(@PathVariable String search,
	// @RequestParam(required = false) String abbr, Pageable pageable, Principal
	// principal ) {
	// Page<Product> page = null;
	// search = search.replaceAll("_", "/");
	// page = productRepository.findByIsShowAndCodeOrDescription(true, search,
	// search, pageable);
	//
	// if(!page.getContent().isEmpty()) {
	// updateCartQty(page, principal);
	// }
	// return page;
	// }

	public Page<Product> updateCartHistory(Page<Product> page, Principal principal) {
		List<Product> products = page.getContent();

		Shop shop = shopRepository.findByEmail(principal.getName());
		String supplier = products.get(0).getAbbr();

		for (Product product : products) {
			// OrderItem orderItem
			// =
			// orderItemRepository.findTopByCodeAndStatusAndInvoiceStartsWithOrderByCreatedDesc(product.getCode(),OrderType.ORDER,invoice);

			OrderItem orderItem = orderItemRepository
					.findTopByCodeAndStatusAndInvoiceStartsWithAndAbbrOrderByCreatedDesc(product.getCode(),
							OrderType.ORDER, supplier, shop.getAbbr());

			if (orderItem != null) {
				LocalDateTime orderDate = orderItem.getOrder().getOrderDate();
				product.setOrderedDate(MyDate.toDay(orderDate));
			} else {
				product.setOrderedDate("");
			}
		}

		return page;

	}

	private Page<Product> convertProduct(Page<OrderItem> orderItems) {
		List<Product> products = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			Product product = productRepository.findByCodeAndAbbr(orderItem.getCode(),
					orderItem.getInvoice().substring(0, 4));
			product.setQty(orderItem.getQty());
			products.add(product);
		}

		return new PageImpl<Product>(products, orderItems.getPageable(), orderItems.getTotalElements());
	}

	private Page<OrderItem> cartRepostory(String abbr, Principal principal, Pageable pageable) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		String invoice = abbr + shop.getAbbr() + "_CART";
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("code"));
		return orderRepository.findByInvoice(invoice, pageable);
	}

	private Page<Product> updateCartQty(Page<Product> page, Principal principal) {
		List<Product> products = page.getContent();
		Optional<Order> cart = getCarts(principal, products.get(0).getAbbr());

		if (cart.isPresent()) {
			List<OrderItem> orderItems = cart.get().getOrderItems();
			for (Product product : products) {
				for (OrderItem orderItem : orderItems) {
					if (product.getCode().equals(orderItem.getCode())) {
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
		String invoice = abbr + shop.getAbbr() + "_CART";
		return orderRepository.findByInvoice(invoice);
	}

	@GetMapping("/products/elements/{abbr}")
	public Long findByAbbrElements(@PathVariable String abbr) {
		Pageable paging = PageRequest.of(0, 100, Sort.by("code"));
		Page<Product> products = productRepository.findByAbbr(abbr, paging);
		// System.err.println(products.getTotalElements());
		// System.err.println(products.getTotalPages());
		return products.getTotalElements();
	}

	@GetMapping("/products/counts/{abbr}")
	public Integer findByAbbrCounts(@PathVariable String abbr) {
		Pageable paging = PageRequest.of(0, 100, Sort.by("code"));
		Page<Product> products = productRepository.findByAbbr(abbr, paging);
		// System.err.println(products.getTotalElements());
		// System.err.println(products.getTotalPages());
		return products.getTotalPages();
	}

	@GetMapping("/products/search/{abbr}/{search}")
	public List<Product> findByAbbrSearch(@PathVariable String abbr, @PathVariable String search) {

		// List<Product> products =
		// productRepository.findByAbbrAndIsShowAndCategoryContains(abbr, true, search);
		List<Product> products = productRepository
				.findByAbbrAndCategoryContainsOrAbbrAndDescriptionContainsOrAbbrAndCodeContainsOrAbbrAndBarcodeContains(
						abbr, search, abbr, search, abbr, search, abbr, search);
		products.forEach(item -> item.setImage(null));
		return products;
	}

	@GetMapping("/products/pageable/{abbr}/{number}/{size}")
	public List<Product> findByAbbrPagable(@PathVariable String abbr, @PathVariable String number,
			@PathVariable String size) {
		Pageable paging = PageRequest.of(Integer.valueOf(number), Integer.valueOf(size), Sort.by("code"));
		List<Product> products = productRepository.findByAbbr(abbr, paging).getContent();
		products.forEach(item -> item.setImage(null));

		return products;
	}

	@GetMapping("/products/{abbr}")
	public Page<Product> findProdutsByPagable(@PathVariable String abbr, Pageable pageable, Boolean newp,
			Principal principal) {
		Page<Product> page = productRepository.findByAbbrAndIsShow(abbr, true, pageable);
		updateCartQty(page, principal);
		updateCartHistory(page, principal);
		return page;
	}

	@GetMapping("/products/category")
	public List<String> findProductsByCompany(@RequestParam String abbr) {
		// 초기 카테고리 필요시
		if (abbr.isEmpty()) {
			Supplier supplier = supplierRepository.findFirstByOrderByCompanyAsc();
			abbr = supplier.getAbbr();
		}
		List<Product> products = productRepository.findByAbbr(abbr);
		// List<String> categories = new ArrayList<>();
		// for (Product product : products) {
		// String category = product.getCategory();
		// if(product.isShow() && !categories.contains(category)) {
		// categories.add(category);
		// }
		// }

		return products.stream().filter(item -> item.isShow()).map(item -> item.getCategory())
				.filter(item -> !item.isEmpty()).distinct().sorted().collect(Collectors.toList());

	}

	@GetMapping("/products/categorys")
	public List<Product> findCategories() {
		List<Product> products = productRepository.findAll();
		List<Product> sortedProducts = products.stream().filter(item -> item.isShow())
				.filter(item -> !item.getCategory().equals("")).filter(distinctByKey(p -> p.getCategory()))
				.sorted(Comparator.comparing(Product::getCategory)).collect(Collectors.toList());
		return sortedProducts;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance();
	
	@GetMapping("/products/status")
	public List<ProductStatus> getcompanies() {
	
		List<ProductStatus> productStatus = new ArrayList<>();
		// 2021-07-06 memeory heap 문제로 변경함
		 List<Supplier> suppliers = supplierRepository.findAllByOrderByCompanyAsc();

		 for (Supplier supplier : suppliers) {
			 
			 String abbr = supplier.getAbbr();
			 ProductStatus status = new ProductStatus();
			 status.setAbbr(abbr);
			 status.setSupplier(supplier.getCompany());
			 
			 Double count = productRepository.countByAbbr(abbr);
			 status.setProducts(formatter.format(count));
			 
			 count = productRepository.countByAbbrAndIsShow(abbr, true);
			 status.setIsShow(formatter.format(count));
			 
			 count = productRepository.countByAbbrAndIsPhoto(abbr, true);
			 status.setImages(formatter.format(count));
			 
			 count = productRepository.countByAbbrAndIsNew(abbr, true);
			 status.setIsNew(formatter.format(count));
			 
			 count = productRepository.countByAbbrAndIsSpecial(abbr, true);
			 status.setIsSpecial(formatter.format(count));

	         status.setActive(supplier.getIsActive());
        	 productStatus.add(status);
		 }
		 
		return productStatus;
	}

	@PostMapping("/products/status")
	public ProductStatus postStatus(@RequestBody ProductStatus status) {

		ProductStatus tmp = productStatusRepository.findByAbbr(status.getAbbr());
		if (tmp != null) {
			status.setId(tmp.getId());
			status.setActive(tmp.isActive());
		}
		return productStatusRepository.save(status);
	}
	
	// NanuriManagemt product 지원
	@GetMapping("/products/abbr/{abbr}")
	public List<ProductManage> findByAbbr(@PathVariable String abbr) {

		String query = "SELECT id, seq, code, abbr, company, category, barcode, description, "
				+ " price, special_price, pack, stock, is_photo, is_show, is_special, is_new, comment "; 
		   query = query +	" FROM product WHERE abbr = :abbr  ";
		   query = query +	" ORDER BY code ASC";

		   Query nativeQuery = em.createNativeQuery(query);
		   nativeQuery.setParameter("abbr", abbr);
		   JpaResultMapper jpaResultMapper = new JpaResultMapper();
		   List<ProductManage> products = jpaResultMapper.list(nativeQuery, ProductManage.class);
		   
		   
		return products;
	}
	

}
