package com.order2david.signin.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order2david.exception.TokenRefreshException;
import com.order2david.model.Address;
import com.order2david.shop.model.RefreshToken;
import com.order2david.shop.model.Roles;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.shop.service.RefreshTokenService;
import com.order2david.signin.JwtFilter;
import com.order2david.signin.TokenProvider;
import com.order2david.signin.payload.LoginDto;
import com.order2david.signin.payload.TokenDto;
import com.order2david.signin.payload.TokenRefreshResponse;
import com.order2david.signin.service.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	RefreshTokenService refreshTokenService;
	
	
	private UserService userService;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
			UserService userService) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userService = userService;
	}

	@PostMapping("/hello")
	public String hello() {
		return "hello react";
	}

	@PostMapping("/signin")
	public ResponseEntity<TokenDto> signin(@Valid @RequestBody LoginDto loginDto) {

		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					loginDto.getEmail(), loginDto.getPassword());
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			Shop shop = userService.getMyUserWithAuthorities().get();
			
			String jwt = tokenProvider.createToken(authentication);
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(shop.getId());
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
			TokenDto tokenDto = getTokenDto(shop, jwt, refreshToken.getToken());

			return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			TokenDto tokenDto = getTokenDto(null, null, null);
			System.err.println(e.getMessage());
			//e.printStackTrace(); // 오류 출력(방법은 여러가지)
			// throw e; //최상위 클래스가 아니라면 무조건 던져주자
			return new ResponseEntity<>(tokenDto, null, HttpStatus.OK);
		} finally {

		}
	}

	@GetMapping("/signout")
	public ResponseEntity<TokenDto> signout() {

		try {
			TokenDto tokenDto = getTokenDto(null, null, null);
			return new ResponseEntity<>(tokenDto, null, HttpStatus.OK);
		} catch (Exception e) {
			TokenDto tokenDto = getTokenDto(null, null, null);
			System.err.println(e.getMessage());
			//e.printStackTrace(); // 오류 출력(방법은 여러가지)
			// throw e; //최상위 클래스가 아니라면 무조건 던져주자
			return new ResponseEntity<>(tokenDto, null, HttpStatus.OK);
		} finally {

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

    @PostMapping("/refresh")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenDto tokenDto) {
    	//System.err.println("refresh token 시작" + tokenDto);
	    //System.err.println("reflesh = " + requestRefreshToken);
	    //Optional<RefreshToken> ref = refreshTokenService.findByToken(requestRefreshToken);
	    //System.err.println("reflesh = " + ref);
    	String requestRefreshToken = tokenDto.getRefresh();
	    return refreshTokenService.findByToken(requestRefreshToken)
	        .map(refreshTokenService::verifyExpiration)
	        .map(RefreshToken::getShop)
	        .map(shop -> {
	          String token = tokenProvider.generateTokenFromUsername(shop.getEmail());
	          RefreshToken refreshToken = refreshTokenService.createRefreshToken(shop.getId());
	          TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(token, refreshToken.getToken());
	          refreshTokenService.deleteByToken(requestRefreshToken);
	          //System.err.println(tokenRefreshResponse);
	          return ResponseEntity.ok(tokenRefreshResponse);
	        })
	        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
	            "Refresh token is not in database!"));
	  }
	  
	@GetMapping("/auth")
	public ResponseEntity<TokenDto> auth() {
		TokenDto tokenDto;
		try {
			if (userService.getMyUserWithAuthorities().isPresent()) {
				Shop shop = userService.getMyUserWithAuthorities().get();
				tokenDto = getTokenDto(shop, null, null);
			} else {
				tokenDto = getTokenDto(null, null, null);		
			}
			return new ResponseEntity<>(tokenDto, null, HttpStatus.OK);
		} catch (Exception e) {
			tokenDto = getTokenDto(null, null, null);
			// System.err.println(e.getMessage());
			e.printStackTrace(); // 오류 출력(방법은 여러가지)
			// throw e; //최상위 클래스가 아니라면 무조건 던져주자
			return new ResponseEntity<>(tokenDto, null, HttpStatus.OK);
		} finally {

		}
	}

	private TokenDto getTokenDto(Shop shop, String jwt, String refreshToken) {
		TokenDto tokenDto = new TokenDto();
		if (shop != null) {
			tokenDto.setIsAuth(true);
			tokenDto.setToken(jwt);
			tokenDto.setRefresh(refreshToken);
			tokenDto.setId(shop.getAbbr());
			//System.err.println(shop.getRoles());
			tokenDto.setIsAdmin(shop.getRoles().contains(new Roles("ROLE_USER")) ? true : false);
			tokenDto.setCompany(shop.getCompany());
		} else {
			tokenDto.setIsAuth(false);
		}
		return tokenDto;
	}

}
