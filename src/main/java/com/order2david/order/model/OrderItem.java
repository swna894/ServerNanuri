package com.order2david.order.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */
@Setter
@Getter
@Entity
@Table(
	uniqueConstraints={
        @UniqueConstraint(
            columnNames={"code", "invoice"}
        )
    }	
)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ToString.Exclude
	@CreationTimestamp
	@CreatedDate
	@Column(name = "created", updatable = false)
	private LocalDateTime created;

	@ToString.Exclude
	@UpdateTimestamp
	@LastModifiedDate
	@Column(name = "updated", updatable = true)
	private LocalDateTime updated;
	
	@Enumerated(EnumType.STRING)
	private OrderType status; // 주문상태
	private String invoice;
	private String code;
	private String description;
	private double price; // 주문 가격
	private double amount;
	private String abbr;
	private int qty; // 주문 수량
	private int stock; // shop 보유 수량  21년 09월 21일 추가
	
	@Transient
	private String shop;
	@Transient
	private String shopId;
	// ==연관 관계== //
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID")
	private Order order; // 주문

	//@JsonBackReference
//	@OneToOne
//	@JoinColumn(name = "PRODUCT_ID")
//	private Product product; // 주문 상품
	
	public OrderItem() {
		super();
	}
	
	public OrderItem(Cart cart) {
		this.code = cart.getCode();
		this.qty = cart.getQty();
		this.price = cart.getPrice();
		this.description = cart.getDecription();
		this.invoice = cart.getInvoice();
		this.amount = getTotalPrice();	
		this.abbr = cart.getAbbr();
		this.status = OrderType.CART;
	}
	
	// ==생성 메서드== //
//	public OrderItem createOrderItem(Product product, double price, int count) {
//
//		OrderItem orderItem = new OrderItem();
//		//orderItem.setProduct(product);
//		orderItem.setPrice(price);
//		orderItem.setCount(count);
//		amount = getTotalPrice();
//		product.removeStock(count);
//		
//		return orderItem;
//	}

	// ==비즈니스 로직==//
	/** 주문 취소 */
//	public void cancel() {
//		getProduct().addStock(count);
//	}

	// ==조회 로직==//
	/** 주문상품 전체 가격 조회 */
	public double getTotalPrice() {
		this.amount = Double.valueOf(String.format("%.2f", this.qty * this.price));
		return amount;
	}

	@Override
	public String toString() {
		return "OrderItem [status=" + status + ", invoice=" + invoice + ", code=" + code + ", description="
				+ description + ", price=" + price + ", amount=" + amount + ", abbr=" + abbr + ", qty=" + qty
				+ ", stock=" + stock + ", shop=" + shop + ", shopId=" + shopId + "]\n";
	}




}
