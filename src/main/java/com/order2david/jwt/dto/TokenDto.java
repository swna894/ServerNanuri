package com.order2david.jwt.dto;

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
    private Long id;
    private String company;
}
