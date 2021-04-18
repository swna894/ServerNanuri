package com.order2david.jwt.dto;

import com.order2david.shop.model.Shop;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

	private Boolean loginSuccess;
	private Boolean isAuth;
	private Boolean isAdmin;
    private String token;
    private Shop shop;
}
