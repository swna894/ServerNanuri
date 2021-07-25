package com.order2david.Product.model;

import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */
@Setter @Getter
public class ProductManage {

	private Long id;
	private LocalDateTime created;
	private LocalDateTime updated;
	
	private int seq;
	private String code; // 이름
	private String abbr;
	private String company;
	private String barcode;
	private String description; // 제품설명
	private String category;
	private String comment;
	private double price; // 가격
	private double specialPrice;
	private int stock; // 재고수량
	private int pack;
	private boolean isPhoto;
	private boolean isShow;
	private boolean isSpecial;
	private boolean isNew;
	
	private int qty;
	private boolean isCheck;
	private String orderedDate;
	
	public ProductManage(BigInteger id, Integer seq, String code, String abbr, String company, String category, 
			     String barcode, String descripton, Double price, Double special_price, Integer pack,
			     Integer stock, Boolean is_photo, Boolean is_show,  Boolean is_special, Boolean is_new, String comment) {
		this.id = id.longValue();
		this.seq = seq;
		this.code = code;
		this.abbr = abbr;
		this.company = company;
		this.category = category;
		this.barcode = barcode;
		this.description = descripton;
		this.price = price;
		this.specialPrice = special_price;
		this.pack = pack;
		this.stock = stock;
		this.isPhoto = is_photo;
		this.isShow =  is_show;
		this.isSpecial = is_special;
		this.isNew = is_new;
		this.comment = comment;
	}

	public void addStock(int quantity) {
		this.stock += quantity;
	}


	public void removeStock(int quantity) {
		int restStock = this.stock - quantity;
		//if (restStock < 0) {
		//	throw new NotEnoughStockException("need more stock");
		//}
		this.stock = restStock;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", seq=" + seq + ", code=" + code + ", abbr=" + abbr + ", barcode=" + barcode
				+ ", description=" + description + ", category=" + category + ", price="
				+ price + ", stock=" + stock + ", pack=" + pack + "]\n\n";
	}

}
