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
		return orderRepository.save(order);
	}

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
	
		return orderRepository.save(order);

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
	public List<Order> migration(@RequestBody List<Order> orders) {
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

	@DeleteMapping("orders")
	public void delete(@RequestBody List<Order> items) {
		for (Order order : items) {
			orderRepository.deleteById(order.getId());
		}
		// orderRepository.deleteAll(items);
	}

	@PostMapping("order/copycart")
	public Order postCopyCart(@RequestBody Order order) {	
		return orderRepository.save(order);
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
		Supplier supplier = supplierRepository.findByAbbr(cart.getAbbr());
		
		Product product = productRepository.findByCodeAndAbbr(cart.getCode(), abbr);
		product.removeStock(cart.getQty());
		// 특판 제품에 대한 가격 설정
		if(product.isSpecial()) {
		    cart.setPrice(product.getSpecialPrice());
		} else {
			cart.setPrice(product.getPrice());
		}
		cart.setDecription(product.getDescription());
		cart.setInvoice(invoice);
	
		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		Order order = null;
		if(!orderOptional.isPresent() && cart.getQty() != 0) {	
			order = new Order();

			order.setShopAbbr(abbr);
			order.setStatus(OrderType.CART);
			order.setInvoice(invoice);
			order.addOrderItem(new OrderItem(cart));
			order.setOrderDate(LocalDateTime.now());	
		} else {
			if(product != null) {
				product.removeStock(cart.getQty());			
				order = orderOptional.get();
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
		order.setShop(shop);
		if(supplier == null ) {	
			System.err.println("supplier null =============");
		}
		order.setSupplier(supplier);
		order.setAmount(order.getTotalPrice());
		order = orderRepository.save(order);
		productRepository.save(product);
		
		List<OrderItem> items = order.getOrderItems();
		int count = items.size();
		Double amount = items.stream().mapToDouble(item -> item.getAmount()).sum();
		if(count == 0) {
			return null;
		}
		return String.valueOf(count) + " items Total " + String.format("$%,.2f",amount);
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
				return String.valueOf(count) + " items   Total " + String.format("$%,.2f",order.getAmount());	
		} else {
			return null;
		}	
	}
	
	@GetMapping("order/cart/inform")
	public String getInitCartInfrom(Principal principal) {

		Supplier supplier = supplierRepository.findFirstByOrderByCompanyAsc();
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
			return String.valueOf(count) + " items   Total " + String.format("$%,.2f",amount);	
		} else {
			return null;
		}	
	}
	
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
				item.setStatus(OrderType.ORDER);
			});
			order.setStatus(OrderType.ORDER);
			
			order = orderRepository.save(order);
			if(order != null) {
				EmailService service = new  EmailService();
				service.sendMail(order);
			}
			return false;	
		} else {
			return true;
		}	
	}
}
