package com.order2david.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.jwt.service.UserService;
import com.order2david.shop.model.Shop;

@RestController
@RequestMapping("/api")
public class UserController {
	
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
     
	@GetMapping("/shop")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<Shop> getMyUserInfo() {
		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
	}

	@GetMapping("/shop/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Shop> getUserInfo(@PathVariable String email) {
		return ResponseEntity.ok(userService.getUserWithAuthorities(email).get());
	}
}
