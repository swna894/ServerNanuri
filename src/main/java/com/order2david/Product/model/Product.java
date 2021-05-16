package com.order2david.Product.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */
@Setter @Getter
@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"code", "abbr"})
}) 
public class Product {

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
	
	private int seq;
	private String code; // 이름
	private String abbr;
	private String company;
	private String barcode;
	private String description; // 제품설명
	private String category;
	private String imagePath;
	private double price; // 가격
	private double specialPrice;
	private int stock; // 재고수량
	private int pack;
	private boolean isShow;
	private boolean isSpecial;
	private boolean isNew;
	@Lob
	private byte[] image;
	
	@Transient
	private int qty;
	@Transient
	private String orderedDate;
	

	// 0423
	// == 연관 관계 == //
//	@JsonBackReference
//	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "SUPPLIER_ID")
//	private Supplier supplier; // 공급자 정보
	
	
	// ==Biz Method== //
	public void addStock(int quantity) {
		this.stock += quantity;
	}

	// 0423
//	public void setSupplier(Supplier supplier) {
//		this.supplier = supplier;
//		supplier.getProducts().add(this);
//	}

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
				+ ", description=" + description + ", category=" + category + ", imagePath=" + imagePath + ", price="
				+ price + ", stock=" + stock + ", pack=" + pack + "]\n\n";
	}

}
