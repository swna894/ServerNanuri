package com.order2david.jwt.controller;


import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.jwt.JwtFilter;
import com.order2david.jwt.TokenProvider;
import com.order2david.jwt.dto.LoginDto;
import com.order2david.jwt.dto.TokenDto;
import com.order2david.jwt.service.UserService;
import com.order2david.model.Address;
import com.order2david.shop.model.Roles;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;


@RestController
@RequestMapping("/api")
public class AuthController {
	
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

		
	@Autowired
	ShopRepository shopRepository;
	
	private UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    
    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @PostMapping("/hello")
    public String hello() {
    	return "hello react";
    }
    
    @PostMapping("/signin")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        //logger.info("api/signin =" + loginDto);
    	TokenDto tokenDto = new TokenDto();
    	try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
      
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            tokenDto.setLoginSuccess(true);
            tokenDto.setToken(jwt);
            tokenDto.setShop(userService.getMyUserWithAuthorities().get());
            
            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    	}catch (Exception e){
             tokenDto.setLoginSuccess(false);
    	     e.printStackTrace(); //오류 출력(방법은 여러가지)
    	     //throw e; //최상위 클래스가 아니라면 무조건 던져주자
    	     return new ResponseEntity<>(tokenDto, null, HttpStatus.OK);	    
    	}finally{

    	} 
    }
    
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @PostMapping("/signup")
    public Shop singup(@Valid @RequestBody Shop shop) {
    	String abbr = shopRepository.findTopByOrderByAbbrDesc().getAbbr();
    	Address address = new Address();
    	address.setCity("");
    	address.setStreet("");
    	address.setSurburb("");
    	
    	shop.setPass(shop.getPassword());
    	shop.setPassword(passwordEncoder.encode(shop.getPassword()));
    	shop.setAbbr(String.valueOf(Integer.valueOf(abbr) + 1));
    	Set<Roles> roles = new HashSet<>();
    	shop.setRoles(roles);
    	shop.getRoles().add(new Roles("ROLE_USER"));
    	shop.setShowing(false);
    	shop.setAddress(address);
    	
    	return shopRepository.save(shop);    
    }
}
