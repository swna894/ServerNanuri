package com.order2david.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
	
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//    
//	@PostMapping("/signup")
//	public ResponseEntity<User> signup(@Valid @RequestBody UserDto userDto) {
//		return ResponseEntity.ok(userService.signup(userDto));
//	}
//
//	@GetMapping("/user")
//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
//	public ResponseEntity<User> getMyUserInfo() {
//		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
//	}
//
//	@GetMapping("/user/{username}")
//	@PreAuthorize("hasAnyRole('ADMIN')")
//	public ResponseEntity<User> getUserInfo(@PathVariable String username) {
//		return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
//	}
}
