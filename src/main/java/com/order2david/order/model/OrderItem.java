package com.order2david.order.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "ORDER_ITEM"
//	uniqueConstraints={
//        @UniqueConstraint(
//            columnNames={"code", "invoice"}
//        )
//    }	
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
	
	private String invoice;
	private String code;
	private String description;
	private double price; // 주문 가격
	private double amount;
	private int qty; // 주문 수량
	
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
		return "OrderItem [id=" + id + ", order=" + order + ", invoice=" + invoice + ", code="
				+ code + ", price=" + price + ", amount=" + amount + ", qty=" + qty + ", created=" + created
				+ ", updated=" + updated + "]\n\n";
	}



}
