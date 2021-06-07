package com.order2david.supplier.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.order2david.model.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */

@Setter @Getter
@NoArgsConstructor
@Entity
@Table(name = "SUPPLIER")
public class Supplier {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	@Column(unique=true)
	private String abbr;
	@Column(unique=true)
	private String company;
	private String cellphone;
	private String phone;
	private String email;
	private String comment;
	@Column(columnDefinition = "boolean default false")
	private Boolean isNew;
	@Column(columnDefinition = "boolean default false")
	private Boolean isSpecial;
	@Column(columnDefinition = "boolean default true")
	private Boolean isActive;
	
	@Embedded
	private Address address;
    
	// 0423 
//	@JsonManagedReference(value="supplier-abbr")
//	@OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//	private List<Order> orders = new ArrayList<Order>();
//	
//    @JsonManagedReference
//	@OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<Product> products = new ArrayList<Product>();
	

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abbr == null) ? 0 : abbr.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Supplier other = (Supplier) obj;
		if (abbr == null) {
			if (other.abbr != null)
				return false;
		} else if (!abbr.equals(other.abbr))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Supplier [id=" + id + ", name=" + name + ", abbr=" + abbr + ", company=" + company + ", cellphone="
				+ cellphone + ", phone=" + phone + ", email=" + email + ", comment=" + comment + ", isNew=" + isNew
				+ ", isSpecial=" + isSpecial + ", isActive=" + isActive + ", address=" + address + "]\n";
	}
	

}
