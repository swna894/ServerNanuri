package com.order2david.shop;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.order.model.Order;
import com.order2david.order.repository.OrderRepository;
import com.order2david.shop.model.IsShow;
import com.order2david.shop.model.Roles;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopJdbcRepository;
import com.order2david.shop.repository.ShopRepository;


@RestController
@RequestMapping("/api")
public class ShopController {

	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ShopRepository shopRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ShopJdbcRepository shopJdbcRepository;
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostConstruct
	public void init() {
		//shopJdbcRepository.initialAbbrColumn();
	}
	
	@PostMapping("/shops/init")
	public List<Shop> init(@RequestBody List<Shop> shops) {
		shopRepository.deleteAll();
		return shopRepository.saveAll(shops);
	}
	
	@PostMapping("/shops")
	public List<Shop> postAll(@RequestBody List<Shop> shops) {
		return shopRepository.saveAll(shops);
	}
	
	@PostMapping("/shop")
	public Shop post(@RequestBody Shop shop) {
		Shop lastShop = shopRepository.findTopByOrderByAbbrDesc();
		shop.setAbbr(String.valueOf(Integer.valueOf(lastShop.getAbbr()) + 1));	
		if (shop.getPass() != null) {
			shop.setPassword(passwordEncoder.encode(shop.getPass()));
			Set<Roles> roles = new HashSet<>();
			shop.setRoles(roles);
			shop.getRoles().add(new Roles("ROLE_USER"));
		}
		return shopRepository.save(shop);
	}
	
	@PutMapping("/shops")
	public List<Shop> putAll(@RequestBody List<Shop> shops) {
		shops.forEach(item -> item.setPassword(passwordEncoder.encode(item.getPass())));
		shopJdbcRepository.postColumns(shops);	
		return shopRepository.saveAll(shops);
	}


	
	@GetMapping("/shops")
	public List<Shop> findAll() {
		Map<String, List<IsShow>> maps = shopJdbcRepository.findAllAbbrs();
		List<Shop> shopEntities = shopRepository.findAllByOrderByCompanyAsc();
		shopEntities.stream().forEach(item -> { 
			          item.setIsShow(maps.get(item.getCompany()));
		});
		return shopEntities;
	}

	@GetMapping("company")
	public Shop findShop(@RequestParam Map<String, String> param) {	
		Shop shop = shopRepository.findByCompany(param.get("company"));
		return shop;
		
	}

	
	@DeleteMapping("/shops")
	public void deleteAll(@RequestBody List<Shop> shops) {
		for (Shop shop : shops) {
			List<Order> orders = orderRepository.findByShopAbbr(shop.getAbbr());
			if(orders != null) {
				Shop dummyshop = shopRepository.findByCompany("Deleted shop");
				orders.forEach(item -> item.setShop(dummyshop));
			}
			shopRepository.deleteById(shop.getId());
		}
		
		//shopRepository.deleteAll(shops);
	}




}
