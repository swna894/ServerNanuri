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
	public List<OrderItem> postOrderItems(@RequestBody List<OrderItem> posItems) {
		String abbr = posItems.get(0).getAbbr();
		String shopId = posItems.get(0).getShopId();
		String invoice = abbr + shopId + "_CART";
		Order order = new Order();
			
		Shop shop = shopRepository.findByAbbr(shopId);
		Supplier supplier = supplierRepository.findByAbbr(abbr);

		posItems = checkIsStock(posItems, invoice);
		if (posItems.isEmpty()) {
			return null;
		}

		Optional<Order> orderOptional = orderRepository.findByInvoice(invoice);
		if (!orderOptional.isPresent()) { // order가 없으면
			
			order = new Order(shop, supplier);
			order.setInvoice(invoice);
	
//			ArrayList<OrderItem> noDupPosItem = new ArrayList<OrderItem>(new HashSet<OrderItem>(posItems));			
			for (OrderItem orderItem : posItems) {			
				orderItem.setOrder(order);
				orderItem.setAmount(orderItem.getTotalPrice());
				orderItem.setServer(true);
			}
			Double sum = posItems.stream().mapToDouble(item -> item.getTotalPrice()).sum();
			order.setOrderItems(posItems);
			order.setAmount(sum);

		} else {		
			order = orderOptional.get();
			List<OrderItem> serveredList = orderOptional.get().getOrderItems();

			for (OrderItem posItem : posItems) {
				posItem.setServer(true);
				OrderItem serverItem = serveredList.stream().filter(item -> item.getCode().equals(posItem.getCode()))
						.findFirst().orElse(null);
				if (serverItem != null) {
					serverItem.setQty(posItem.getQty());
				} else {
					order.addOrderItem(posItem);
				}
			}
			order.setOrderDate(LocalDateTime.now());
			Double sum = posItems.stream().mapToDouble(item -> item.getTotalPrice()).sum();
			order.setAmount(sum);
		}		
		Order tmp = orderRepository.save(order);
		return tmp.getOrderItems();

	}

	List<OrderItem> chekedList = new ArrayList<OrderItem>();

	private List<OrderItem> checkIsStock(List<OrderItem> orderItem, String invoice) {
		List<OrderItem> chekedList = new ArrayList<OrderItem>();
		for (OrderItem item : orderItem) {
			Product product = productRepository.findByCodeAndAbbr(item.getCode(), item.getAbbr().toLowerCase());
			
			if (product != null && product.getStock() > 12) {
				item.setStatus(OrderType.CART);
				item.setInvoice(invoice);
				item.setSeq(product.getSeq());
				chekedList.add(item);
			}
		}
		return chekedList;
	}

}
