package com.order2david.order;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.Product.repository.ProductRepository;
import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.order.repository.OrderItemRepository;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("api")
public class OrderItemController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ShopRepository shopRepository;

	@PostMapping("/orderItems/migration")
	@Transactional
	public List<OrderItem> migration(@RequestBody List<OrderItem> orderItems) {

		for (OrderItem orderItem : orderItems) {
			String invoice = orderItem.getInvoice();
			// String abbr = invoice.substring(0, 4);
			Optional<Order> order = orderRepository.findByInvoice(invoice);

			// Product product = productRepository.findByCodeAndAbbr(orderItem.getCode(),
			// abbr);
			if (order.isPresent()) {
				order.get().getOrderItems().add(orderItem);
				orderItem.setOrder(order.get());
			}
			// orderItem.setProduct(product);
			// System.err.println(orderItem);
		}
		return orderItemRepository.saveAll(orderItems);
	}

	@PostMapping("/orderItems")
	@Transactional
	public List<Order> postAll(@RequestBody List<Order> orders) {
		return orderRepository.saveAll(orders);
	}

	@GetMapping("/orderItems")
	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	@DeleteMapping("/orderItems")
	@Transactional
	public void deleteAll(@RequestBody List<OrderItem> items) {
		for (OrderItem orderItem : items) {
			orderItem = orderItemRepository.getOne(orderItem.getId());
			Order order = orderItem.getOrder();
			order.deleteOrderItem(orderItem);
			orderRepository.save(order);
			orderItemRepository.delete(orderItem);
		}

	}

}
