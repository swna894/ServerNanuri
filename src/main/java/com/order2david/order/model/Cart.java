package com.order2david.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
public class Cart {
	
	String invoice;
	String abbr;
	String code;
	String barcode;
	String decription;
	Double price;
	Long id;
	int qty;
	int seq;

}
