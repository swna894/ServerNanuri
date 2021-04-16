package com.order2david.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.order.model.Order;
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
			if(optionalOrder.isPresent()) {		
				Order findOrder = optionalOrder.get();
				findOrder.setStatus(order.getStatus());
				findOrder.setAmount(order.getAmount());
				orderRepository.save(findOrder);		
				ids.add(findOrder.getId());
			}
			
		}
		return orderRepository.findAllById(ids);
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
				.where(order.status.eq(status), 
						order.orderDate.between(start, end),
						builder)
				.orderBy(order.orderDate.desc())
				.fetch();
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
		 	//orderRepository.deleteAll(items);
	 }

}
