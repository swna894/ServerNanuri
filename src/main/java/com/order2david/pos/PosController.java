package com.order2david.pos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.Product.model.Product;
import com.order2david.Product.repository.ProductRepository;
import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.order.model.OrderType;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopJdbcRepository;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("/pos")
public class PosController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	ShopJdbcRepository shopJdbcRepository;

	@Autowired
	ShopRepository shopRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@GetMapping("/suppliers")
	public List<Supplier> findAll() {
		return supplierRepository.findAllByOrderByCompanyAsc();
	}

	@PostMapping("/orderItems")
	public List<OrderItem> postOrderItems(@RequestBody List<OrderItem> orderItems) {
		
		String abbr = orderItems.get(0).getAbbr();
		String shopId = orderItems.get(0).getShopId();
		String invoice = abbr + shopId + "_CART"; 
		Shop shop = shopRepository.findByAbbr(orderItems.get(0).getShopId());
		Supplier supplier = supplierRepository.findByAbbr(abbr);
		
		orderItems = checkIsStock(orderItems, invoice);
		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		Order order;
		if (!orderOptional.isPresent()) {  // order가 없으면
			Double sum = orderItems.stream().mapToDouble(item -> item.getTotalPrice()).sum();
			order = new Order(shop, supplier);
	
			order.setInvoice(invoice);
			for (OrderItem orderItem : orderItems) {
				orderItem.setOrder(order);
				order.addOrderItem(orderItem);	
			}
			order.setAmount(sum);
			//orderRepository.save(order);
		} else {
			order = orderOptional.get();
			List<OrderItem> serverList = orderOptional.get().getOrderItems();

			for (OrderItem clientItem : orderItems) {
					
				OrderItem findOrder =serverList.stream().filter(item -> item.getCode().equals(clientItem.getCode()))
				               .findFirst().orElse(null);
				if(findOrder != null) {
					findOrder.setQty(clientItem.getQty());
				} else {
					order.addOrderItem(clientItem);
				}
			}
			order.setOrderDate(LocalDateTime.now());
			//orderRepository.save(order);
		}
		Order result = orderRepository.save(order);
		return 	result.getOrderItems();	
	}
	
	private List<OrderItem> checkIsStock(List<OrderItem> orderItem, String invoice) {
		List<OrderItem> chekedList = new ArrayList<OrderItem>();
		for (OrderItem item : orderItem) {
			Product product = productRepository.findByCodeAndAbbr(item.getCode(), item.getAbbr().toLowerCase());
			//logger.debug(item.getCode() +  "제품확인 -->  " + product);
			if (product != null && product.getStock() > 0) {
				item.setStatus(OrderType.CART);
				item.setInvoice(invoice);
				chekedList.add(item);
			}
		}
		return chekedList;
	}
	
}
