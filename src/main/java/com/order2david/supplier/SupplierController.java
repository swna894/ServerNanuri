package com.order2david.supplier;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.order.model.Order;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.IsShow;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopJdbcRepository;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

@RestController
@RequestMapping("/api")
public class SupplierController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	ShopJdbcRepository shopJdbcRepository;

	@Autowired
	ShopRepository shopRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@PostMapping("/suppliers/init")
	public List<Supplier> init(@RequestBody List<Supplier> suppliers) {
		supplierRepository.deleteAll();
		List<Supplier> suppliersEntity = supplierRepository.saveAll(suppliers);
		for (Supplier supplier : suppliersEntity) {
			shopJdbcRepository.newColumn(supplier);
		}
		return suppliersEntity;
	}

	@PostMapping("/suppliers")
	public List<Supplier> postAll(@RequestBody List<Supplier> suppliers) {
		return supplierRepository.saveAll(suppliers);
	}

	@PostMapping("/supplier")
	public Supplier post(@RequestBody Supplier supplier) {
		// shop column 생성
		shopJdbcRepository.newColumn(supplier);
		supplierRepository.flush();
		return supplierRepository.save(supplier);
	}

	@PutMapping("/suppliers")
	public List<Supplier> putAll(@RequestBody List<Supplier> suppliers) {
		return supplierRepository.saveAll(suppliers);
	}

	@DeleteMapping("/suppliers")
	public void deleteAll(@RequestBody List<Supplier> suppliers) {
		// shop column 삭제
		for (Supplier supplier : suppliers) {
			shopJdbcRepository.deleteColumn(supplier);
		}
		supplierRepository.deleteAll(suppliers);
	}

	@GetMapping("/suppliers")
	public List<Supplier> findAll() {
		return supplierRepository.findAllByOrderByCompanyAsc();
	}

	@GetMapping("/pos/suppliers")
	public List<Supplier> findAllForPos() {
		return supplierRepository.findAllByOrderByCompanyAsc();
	}
	
	
	@GetMapping("/supplier")
	public Supplier find() {
		return supplierRepository.findFirstByOrderByCompanyAsc();
	}

	
	@GetMapping("/supplier/{abbr}")
	public Supplier findByAbbr(@PathVariable String abbr) {
		return supplierRepository.findByAbbr(abbr);
	}

	
	@GetMapping("/suppliers/order")
	public List<Supplier> findSuppliersOfOrder(Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		List<Supplier> suppliers = supplierRepository.findAllByOrderByAbbrAsc();
		Map<String, List<IsShow>> maps = shopJdbcRepository.findAllAbbrs();
		List<IsShow> shows = maps.get(shop.getCompany());
		List<Supplier> list = new ArrayList<>();
		for (Supplier supplier : suppliers) {			
			for (IsShow isShow : shows) {
				if (isShow.getIs() != null && isShow.getAbbr().equals(supplier.getAbbr()) && isShow.getIs() && supplier.getIsActive()) {
					list.add(supplier);
				}
			}
		}
		findSuppliersOfCart(principal);
		return list;
	}
	
	@GetMapping("/suppliers/cart")
	public List<Supplier> findSuppliersOfCart(Principal principal) {
		Shop shop = shopRepository.findByEmail(principal.getName());
		List<Supplier> suppliers = supplierRepository.findAllByOrderByAbbrAsc();
		String cart = shop.getAbbr() + "_CART";
		List<Order> carts = orderRepository.findByInvoiceContains(cart);
		
		List<Supplier> list = new ArrayList<>();
		for (Supplier supplier : suppliers) {	
			for (Order order : carts) {
				if(order.getInvoice().contains(supplier.getAbbr())) {
					list.add(supplier);
				}	
			}
		}
		return list;
	}
}
