package com.order2david.Product.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Component
public class ProductStatus {

	private String abbr;
	private String supplier;
	private String products;
	private String images;
	private String isShow;
	private String isNew;
	private String isSpecial;
	private boolean isActive;
	
}
