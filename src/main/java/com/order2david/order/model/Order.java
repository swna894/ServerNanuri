package com.order2david.order.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.order2david.shop.model.Shop;
import com.order2david.supplier.model.Supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */

@Setter
@Getter
@Entity
@Table(name = "ORDERS")
public class Order {

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
	private Double amount;
	@Column(unique = true)
	private String invoice;
	private String shopAbbr;
	private String comment;
	@Transient
	private String company;
	private LocalDateTime orderDate; // 주문시간

	// ==연관 관계==//
	@JsonBackReference(value = "supplier-abbr")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUPPLIER_ID")
	private Supplier supplier; // 배송정보

	// @JsonBackReference
	@OneToOne
	@JoinColumn(name = "SHOP_ID")
	private Shop shop; // 주문 회원

	@JsonManagedReference
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	@OrderBy("code")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>(); // 주문 ITEM

	public Order( ) {
		
	}
	public Order(Shop shop, Supplier supplier) {
		this.setShop(shop);
		this.setShopAbbr(shop.getAbbr());
		// order.setSupplier(supplier);
		this.setStatus(OrderType.CART);
		this.setOrderDate(LocalDateTime.now());
		invoice = getInvoice();
		amount = 0.0;
	}
	// ==생성 메서드==//
	public Order createOrder(Shop shop, Supplier supplier) {
		Order order = new Order();
		order.setShop(shop);

		// order.setSupplier(supplier);
		order.setStatus(OrderType.CART);
		order.setOrderDate(LocalDateTime.now());
		invoice = getInvoice();
		amount = 0.0;
		return order;
	}

	// ==비즈니스 로직==//
	/** 주문 진행 */
	// public void ordering() {
	//
	// if (status == OrderStatus.ORDER) {
	// throw new RuntimeException("이미 배송주문된 상품은 주문이 불가능합니다.");
	// }
	//
	// this.setStatus(OrderStatus.ORDER);
	// for (OrderItem orderItem : orderItems) {
	// orderItem.cancel();
	// }
	// }

	// ==조회 로직==//
	/** 전체 주문 가격 조회 */

	public Double getTotalPrice() {
		Double totalPrice = 0.0;
		for (OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		return Double.valueOf(String.format("%.2f", totalPrice));
	}

	// ==연관관계 메서드==//
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
		orderItem.setAmount(getTotalPrice());
	}

	public Boolean updateOrderItem(Cart cart) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getCode().equals(cart.code)) {
				orderItem.setQty(cart.getQty());
				orderItem.getTotalPrice();
				amount = getTotalPrice();
				return true;
			}
		}
		return false;
	}

	public OrderItem getCartOrderItem(Cart cart) {
		return orderItems.stream().filter(item -> item.getCode().equals(cart.getCode())).findAny().orElse(null);
	}
	public void removeOrderItem(Cart cart) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getCode().equals(cart.code)) {
				orderItems.remove(orderItem);
				break;
			}
		}
		amount = getTotalPrice();
	}

	public void setShop(Shop shop) {
		this.shop = shop;
		// shop.getOrders().add(this);
	}
	// 0423
	// public void setSupplier(Supplier supplier) {
	// this.supplier = supplier;
	// supplier.getOrders().add(this);
	// }

	// public String genInvoice() {
	// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
	// String orderTime = LocalDateTime.now().format(formatter);
	// String accountId = this.shop.getAbbr();
	// String supplier = this.supplier.getAbbr();
	//
	// return supplier + accountId + "_" + orderTime;
	// }

	public void deleteOrderItem(OrderItem item) {
		if (this.orderItems.contains(item)) {
			this.orderItems.remove(item);
			item.setOrder(null);
			updateAmount();
		}
	}


	
	private void updateAmount() {
		Double doubleSum = orderItems.stream().mapToDouble(OrderItem::getAmount).sum();
		this.setAmount(doubleSum);
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", created=" + created + ", updated=" + updated + ", status=" + status + ", amount="
				+ amount + ", invoice=" + invoice + ", shopAbbr=" + shopAbbr + ", comment=" + comment + ", company="
				+ company + ", orderDate=" + orderDate + ", supplier=" + supplier + ", shop=" + shop + ", orderItems="
				+ orderItems + "]\n";
	}




}
