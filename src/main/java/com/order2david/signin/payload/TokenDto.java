package com.order2david.signin.payload;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
	private Boolean isAuth;
	private Boolean isAdmin;
    private String token;
    private String refreshToken;
    private String id;
    private String company;
}
