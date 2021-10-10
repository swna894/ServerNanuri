package com.order2david.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.order.model.Order;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("sync")
public class SyncOrderController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	ShopRepository shopRepository;

	@PostMapping("")
	@Transactional
	public List<Order> syncOrder(@RequestBody List<Order> orders) {
		List<Order> saveOrder = new ArrayList<>();
		for (Order order : orders) {
			Optional<Order> isOrder = orderRepository.findByInvoice(order.getInvoice());
			if (!isOrder.isPresent()) {
				String abbr = order.getInvoice().substring(0, 4);
				String shopAbbr = order.getShopAbbr();
				Supplier supplier = supplierRepository.findByAbbr(abbr);
				Shop shop = shopRepository.findByAbbr(shopAbbr);
				order.setShop(shop);
				order.setSupplier(supplier);
				saveOrder.add(order);
			}
		}
		return orderRepository.saveAll(saveOrder);

	}

	@PostMapping("/newvista")
	@Transactional
	public String syncVistaOrder(@RequestBody List<Order> orders) {
		List<Order> saveOrder = new ArrayList<>();
		for (Order order : orders) {
			String invoice = order.getInvoice();
			Long isOrder = orderRepository.countByInvoiceContains(invoice);
			if (isOrder == 0) {
				String abbr = order.getInvoice().substring(0, 4);
				String company = order.getShopAbbr();
				Supplier supplier = supplierRepository.findByAbbr(abbr);
				Shop shop = shopRepository.findByCompany(company);
				order.setInvoice(invoice + "N");
				order.setShopAbbr(shop.getAbbr());
				order.setShop(shop);
				order.setSupplier(supplier);
				order.getOrderItems().forEach(item -> {
					item.setAbbr(shop.getAbbr());
					item.setInvoice(invoice + "N");
				});
				saveOrder.add(order);
			}
		}
		if(saveOrder.size() > 0) {
			orderRepository.saveAll(saveOrder);	
			return "success";
		} 
		
		return "fail";

	}
}
