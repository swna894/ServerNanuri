package com.order2david.pos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
		Order order = new Order();
			
		Shop shop = shopRepository.findByAbbr(shopId);
		Supplier supplier = supplierRepository.findByAbbr(abbr);

		orderItems = checkIsStock(orderItems, invoice);
		if (orderItems.isEmpty()) {
			return null;
		}
		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		if (!orderOptional.isPresent()) { // order가 없으면
			
			order = new Order(shop, supplier);
			order.setInvoice(invoice);
			
			ArrayList<OrderItem> newlist = new ArrayList<OrderItem>(new HashSet<OrderItem>(orderItems));
			Double sum = newlist.stream().mapToDouble(item -> item.getTotalPrice()).sum();
			
			for (OrderItem orderItem : newlist) {			
				orderItem.setOrder(order);
				orderItem.setAmount(orderItem.getTotalPrice());
				orderItem.setServer(true);
				orderItem.setId(null);
			}
			order.setId(null);

			order.setOrderItems(newlist);
			order.setAmount(sum);

		} else {
			
			order = orderOptional.get();
			List<OrderItem> serverList = orderOptional.get().getOrderItems();

			for (OrderItem clientItem : orderItems) {
				clientItem.setServer(true);
				OrderItem findOrder = serverList.stream().filter(item -> item.getCode().equals(clientItem.getCode()))
						.findFirst().orElse(null);
				if (findOrder != null) {
					findOrder.setQty(clientItem.getQty());
				} else {
					order.addOrderItem(clientItem);
				}
			}
			order.setOrderDate(LocalDateTime.now());
			Double sum = orderItems.stream().mapToDouble(item -> item.getTotalPrice()).sum();
			order.setAmount(sum);
		}		
		Order tmp = orderRepository.save(order);
		return tmp.getOrderItems();

	}

	List<OrderItem> chekedList = new ArrayList<OrderItem>();

	private List<OrderItem> checkIsStock(List<OrderItem> orderItem, String invoice) {
		for (OrderItem item : orderItem) {
			Product product = productRepository.findByCodeAndAbbr(item.getCode(), item.getAbbr().toLowerCase());
			// logger.debug(item.getCode() + "제품확인 --> " + product);
			if (product != null && product.getStock() > 0) {
				item.setStatus(OrderType.CART);
				item.setInvoice(invoice);
				chekedList.add(item);
			}
		}
		return chekedList;
	}

}
