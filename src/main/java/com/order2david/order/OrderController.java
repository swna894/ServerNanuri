package com.order2david.order;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.Product.ProductController;
import com.order2david.Product.model.Product;
import com.order2david.Product.repository.ProductRepository;
import com.order2david.mail.EmailService;
import com.order2david.order.model.Cart;
import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.order.model.OrderType;
import com.order2david.order.model.QOrder;
import com.order2david.order.repository.OrderDslRepository;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RestController
@RequestMapping("api/")
public class OrderController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDslRepository orderDslRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductController productController;
	
	@PersistenceContext
	private EntityManager entityManager;

	@PostMapping("orders")
	public List<Order> postAll(@RequestBody List<Order> orders) {
		return orderRepository.saveAll(orders);
	}

	@PutMapping("orders")
	public List<Order> putAll(@RequestBody List<Order> orders) {
		List<Long> ids = new ArrayList<Long>();
		for (Order order : orders) {
			Optional<Order> optionalOrder = orderRepository.findById(order.getId());
			if (optionalOrder.isPresent()) {
				Order findOrder = optionalOrder.get();
				findOrder.setStatus(order.getStatus());
				findOrder.setAmount(order.getAmount());
				orderRepository.save(findOrder);
				ids.add(findOrder.getId());
			}

		}
		return orderRepository.findAllById(ids);
	}
	
	@PutMapping("order")
	public Order put(@RequestBody Order order) {
		Supplier supplier = supplierRepository.findByAbbr(order.getInvoice().substring(0,4));
		order.setSupplier(supplier);
		return orderRepository.save(order);
	}

	@Transactional
	@PutMapping("orders/confirm")
	public Order putConfirm(@RequestBody Order order) {
		Supplier supplier = supplierRepository.findByAbbr(order.getShopAbbr());
		String shopAbbr = order.getInvoice().substring(4, order.getInvoice().indexOf("_"));
		Shop shop = shopRepository.findByAbbr(shopAbbr);
		String invoice = genInvoice(order);
		order.setInvoice(invoice);
		order.setStatus(OrderType.ORDER);
		order.setSupplier(supplier);
		order.setShop(shop);
		order.setAmount(order.getTotalPrice());
		
		updateProductStock(order);
	
		return orderRepository.save(order);

	}
	
	private void updateProductStock(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		String abbr = order.getShopAbbr();
		for (OrderItem orderItem : orderItems) {
			Product product = productRepository.findByCodeAndAbbr(orderItem.getCode(), abbr);
			product.setStock(product.getStock() - orderItem.getQty());
			productRepository.save(product);
		}	
	}

	public String genInvoice(Order order) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_MMddHHmmss");
		String formatDateTime = LocalDateTime.now().format(formatter);
		String invoice = order.getInvoice();
		String prefix = invoice.substring(0, invoice.indexOf("_"));

		return prefix + formatDateTime;
	}
	
	@GetMapping("orders/f")
	public List<Order> findAllByParm(@RequestParam Map<String, String> paramater) {
		//System.err.println(paramater);
		OrderType status = OrderType.valueOf(paramater.get("status"));
		LocalDateTime start = LocalDate.parse(paramater.get("start")).atStartOfDay();
		LocalDateTime end = LocalDate.parse(paramater.get("end")).atStartOfDay().plusDays(1).minusSeconds(1);
		String shopAbbr = paramater.get("shop");
		String companyAbbr = paramater.get("supplier");

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QOrder order = QOrder.order;

		BooleanBuilder builder = new BooleanBuilder();
		if (StringUtils.hasText(shopAbbr)) { // 제목 검색
			builder.and(order.shopAbbr.contains(shopAbbr));
		}

		if (StringUtils.hasText(companyAbbr)) { // 제목 검색
			builder.and(order.invoice.contains(companyAbbr));
		}

		List<Order> orders = queryFactory.selectFrom(order)
				.where(order.status.eq(status), order.orderDate.between(start, end), builder)
				.orderBy(order.orderDate.desc()).fetch();
		return orders;
	}

	@GetMapping("orders")
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@PostMapping("orders/migration")
	@Transactional
	public List<Order> migrations(@RequestBody List<Order> orders) {
		List<Supplier> suppliers = supplierRepository.findAll();
		List<Shop> shops = shopRepository.findAll();
		orderRepository.deleteAll();

		for (Order order : orders) {
			Shop shop = shops.stream().filter(item -> item.getAbbr().equals(order.getShopAbbr())).findAny()
					.orElse(null);

			String supplierAbbr = order.getInvoice().substring(0, 4);
			Supplier supplier = suppliers.stream().filter(item -> item.getAbbr().equals(supplierAbbr)).findAny()
					.orElse(null);
			order.setShop(shop);
			order.setSupplier(supplier);
		}
		return orderRepository.saveAll(orders);
		// return null;
	}

	@PostMapping("orders/update")
	@Transactional
	public List<Order> updateOrder(@RequestBody List<Order> orders) {
		List<Supplier> suppliers = supplierRepository.findAll();
		List<Shop> shops = shopRepository.findAll();

		for (Order order : orders) {
			Shop shop = shops.stream().filter(item -> item.getAbbr().equals(order.getShopAbbr())).findAny()
					.orElse(null);

			String supplierAbbr = order.getInvoice().substring(0, 4);
			Supplier supplier = suppliers.stream().filter(item -> item.getAbbr().equals(supplierAbbr)).findAny()
					.orElse(null);
			order.setShop(shop);
			order.setSupplier(supplier);
		}
		return orderRepository.saveAll(orders);
		// return null;
	}
	
	@PostMapping("orders/migration/s")
	@Transactional
	public Order migration(@RequestBody Order order) {
		List<Supplier> suppliers = supplierRepository.findAll();
		List<Shop> shops = shopRepository.findAll();
		orderRepository.deleteAll();


			Shop shop = shops.stream().filter(item -> item.getAbbr().equals(order.getShopAbbr())).findAny()
					.orElse(null);

			String supplierAbbr = order.getInvoice().substring(0, 4);
			Supplier supplier = suppliers.stream().filter(item -> item.getAbbr().equals(supplierAbbr)).findAny()
					.orElse(null);
			order.setShop(shop);
			order.setSupplier(supplier);
	
		return orderRepository.save(order);
		// return null;
	}
	
	@DeleteMapping("orders")
	public void delete(@RequestBody List<Order> items) {
		for (Order order : items) {
			orderRepository.deleteById(order.getId());
		}
		// orderRepository.deleteAll(items);
	}

	@PostMapping("order/copycart")
	@Transactional
	public Order postCopyCart(@RequestBody Order order) {	
		Shop shop = shopRepository.findByAbbr(order.getInvoice().substring(4,7));
		Supplier supplier = supplierRepository.findByAbbr(order.getInvoice().substring(0,4));
		order.setShopAbbr(shop.getAbbr());
		order.setShop(shop);
		order.setSupplier(supplier);
		return orderRepository.save(order);
		//return null;
	}
	
	@GetMapping("order/{invoice}")
	public Order getOrder(@PathVariable String invoice) {	
		Optional<Order> order = orderRepository.findByInvoice(invoice);
		if(order.isPresent()) {
			return order.get();
		}
		return null;
	}
	
	/*
	 * -. order에 cart가 있는지 확인한다.
	 *   1. 있음
	 *       -> item에 내역 있는지 확인 -> 있으면 qty 변경
	 *                            -> 없으면 orderItem 생성
	 *   2. 없음 -> orderItem 생성 추가
	 */
	@PostMapping("order/cart")
	public String postCart(@RequestBody Cart cart) {

		String abbr = cart.getAbbr();
		String invoice = abbr + cart.getId() + "_CART"; 
	
		Shop shop = shopRepository.findByAbbr(String.valueOf(cart.getId()));
		Supplier supplier = supplierRepository.findByAbbr(abbr);
		
		Product product = productRepository.findByCodeAndAbbr(cart.getCode(), abbr);
		//product.removeStock(cart.getQty());
		// 특판 제품에 대한 가격 설정
		if(product.isSpecial()) {
		    cart.setPrice(product.getSpecialPrice());
		} else {
			cart.setPrice(product.getPrice());
		}
		cart.setDecription(product.getDescription());
		cart.setInvoice(invoice);
		cart.setSeq(product.getSeq());
		//cart.setAbbr(shop.getAbbr());
		
	
		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		Order order = null;
		// 재고 수량 정리 방법 개선 필요 21-08-02
		if(!orderOptional.isPresent() && cart.getQty() != 0) {
			// cart가 없을 경우
			order = new Order();

			order.setShop(shop);
			if(supplier == null ) {	
				System.err.println("supplier null =============");
			}
			order.setSupplier(supplier);
			order.setShopAbbr(shop.getAbbr());
			order.setStatus(OrderType.CART);
			order.setInvoice(invoice);
			order.addOrderItem(new OrderItem(cart));
			order.setOrderDate(LocalDateTime.now());	
			product.removeStock(cart.getQty());	
		} else {
			// cart가 있는 경우
			if(product != null) {
				order = orderOptional.get();
				OrderItem orderItem = order.getCartOrderItem(cart);
				if(orderItem != null) {
					product.removeStock(cart.getQty() - orderItem.getQty());
				} else {
					product.removeStock(cart.getQty());	// <= 수량 차이만 재고 감소
				}
				
				order.setOrderDate(LocalDateTime.now());
				if(cart.getQty() == 0) {
					order.removeOrderItem(cart);
				} else {
					if(!order.updateOrderItem(cart)) {
						order.addOrderItem(new OrderItem(cart));
					}
				}		
			}
		}

		order.setAmount(order.getTotalPrice());
		order = orderRepository.save(order);
		//카트에서의 수매 내역을 제품의 재고 반영시 논리적 bug 발생 주의해야함
		productRepository.save(product);
		
		List<OrderItem> items = order.getOrderItems();
		int count = items.size();
		Double amount = items.stream().mapToDouble(item -> item.getAmount()).sum();
		if(count == 0) {
			return null;
		}
		return String.valueOf(count) + " items | Total " + String.format("$%,.2f",amount);
	}
	
	@GetMapping("order/cart/inform/{abbr}")
	public String getCartInfromById(@PathVariable String abbr, Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		String invoice = abbr + shop.getAbbr() + "_CART";

		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		
		if(orderOptional.isPresent()) {
			Order order = orderOptional.get();
			List<OrderItem> items = order.getOrderItems();
			int count = items.size();
			if(count == 0) {
				return null;
			} 
				return String.valueOf(count) + " items | Total " + String.format("$%,.2f",order.getAmount());	
		} else {
			return null;
		}	
	}
	
	@GetMapping("order/cart/inform")
	public String getInitCartInfrom(Principal principal) {

		Supplier supplier = supplierRepository.findFirstByOrderBySeqAsc();
		Shop shop = shopRepository.findByEmail(principal.getName());
		String invoice = supplier.getAbbr() + shop.getAbbr() + "_CART";

		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		
		if(orderOptional.isPresent()) {
			List<OrderItem> items = orderOptional.get().getOrderItems();
			int count = items.size();
			Double amount = items.stream().mapToDouble(item -> item.getAmount()).sum();
			if(count == 0) {
				return null;
			} 
			return String.valueOf(count) + " items | Total " + String.format("$%,.2f",amount);	
		} else {
			return null;
		}	
	}
	@Autowired
	EmailService service;
	
	@PutMapping("order/confirm/{abbr}") 
	public Boolean putOrderConfirm(@PathVariable String abbr, Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		String cart = abbr + shop.getAbbr() + "_CART";

		Optional<Order> orderOptional = orderRepository.findByInvoice(cart);
		
		if(orderOptional.isPresent()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_MMddHHmmss");
			String formatDateTime = LocalDateTime.now().format(formatter);
			
			String invoice = abbr + shop.getAbbr() + formatDateTime;
			Order order = orderOptional.get();
			order.setInvoice(invoice);
			order.getOrderItems().forEach(item -> { 
				item.setInvoice(invoice);
				item.setAbbr(shop.getAbbr());
				item.setStatus(OrderType.ORDER);
			});
			order.setStatus(OrderType.ORDER);
			order.setShopAbbr(shop.getAbbr());
			
			order = orderRepository.save(order);
			if(order != null) {
				//EmailService service = new  EmailService();
				service.sendMail(order);
			}
			return false;	
		} else {
			return true;
		}	
	}
	
	
	@GetMapping("order/checkout/{abbr}/{size}") 
	public Page<Product> getCheckout(@PathVariable String abbr, @PathVariable Integer size, Principal principal) {
		Pageable sortedBySeq = PageRequest.of(0, size, Sort.by("seq"));
		Page<Product> page = productRepository.findByAbbrAndIsShow(abbr, true, sortedBySeq);
		
		productController.updateCartHistory(page, principal);
		
		return page;
	}
	
	
	@GetMapping("orders/history")
	@Transactional
	public List<Order> history( Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		List<Order> orders = orderRepository.findByShopAbbrAndStatusOrderByOrderDateDesc(shop.getAbbr(), OrderType.ORDER);
		orders.stream().forEach(item -> item.setCompany(item.getSupplier().getCompany()));
		return orders;
	}
	
	@GetMapping("orders/history/{abbr}")
	@Transactional
	public List<Order> historyByAbbr(@PathVariable String abbr, Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		List<Order> orders = orderRepository.findByShopAbbrAndInvoiceContainsAndStatusOrderByOrderDateDesc(shop.getAbbr(), abbr, OrderType.ORDER);
		
		orders.stream().forEach(item -> item.setCompany(item.getSupplier().getCompany()));
		return orders;
		// return null;
	}
	
	@GetMapping("orders/ishistory")
	@Transactional
	public Long historyCount( Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		Supplier supplier = supplierRepository.findFirstByOrderBySeqAsc();
		String com = supplier.getAbbr();
		//List<Order> orders = orderRepository.findByInvoiceContainsAndStatusOrderByOrderDateDesc(com+shop.getAbbr(), OrderType.ORDER);
		Long count  = orderRepository.countByInvoiceContainsAndStatusOrderByOrderDateDesc(com+shop.getAbbr(), OrderType.ORDER);
		return count;
	}
	
	@GetMapping("orders/ishistory/{abbr}")
	@Transactional
	public Long historyByAbbrCount(@PathVariable String abbr, Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		if(abbr.equals("init")) {
			Supplier supplier = supplierRepository.findFirstByOrderBySeqAsc();
			abbr = supplier.getAbbr();
		}
		//List<Order> orders = orderRepository.findByShopAbbrAndInvoiceContainsAndStatusOrderByOrderDateDesc(shop.getAbbr(), abbr, OrderType.ORDER);
		Long count  = orderRepository.countByShopAbbrAndInvoiceContainsAndStatusOrderByOrderDateDesc(shop.getAbbr(), abbr, OrderType.ORDER);
		
		//orders.stream().forEach(item -> item.setCompany(item.getSupplier().getCompany()));
		return count;
		// return null;
	}
	
	@PutMapping("orders/server")
	@Transactional
	public List<Order> putFromPos(@PathVariable List<OrderItem> orderItems ) {
		System.err.println(orderItems);
		return null;
		// return null;
	}
}
