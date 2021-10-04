package com.order2david.shop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

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
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	//@JsonManagedReference
	//@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	//private List<Order> orders = new ArrayList<Order>();
	
	   @ManyToMany
	   @JoinTable(
	      name = "shop_roles",
	      joinColumns = {@JoinColumn(name = "shop_id")},
	      inverseJoinColumns = {@JoinColumn(name = "role_name")})
	   private Set<Roles> roles;
	
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

	private String name;
	@Column(unique=true)
	private String abbr;
	@Column(unique=true)
	private String company;
	private String cellphone;
	private String phone;
	private String homephone;
	private String avalonId;
	private String email;
	private String comment;
	private String password;
	private String pass;
	//private String token;
	@Column(columnDefinition = "boolean default true")
	private  Boolean showing;
	//@Column(columnDefinition = "varchar(16) default '")
	//private String roles;

	@Transient
    private Map<String, Boolean> abbrMap;
	@Transient
	private List<IsShow> isShow = new ArrayList<>();
	
	@Embedded
	private Address address;
	
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
		Shop other = (Shop) obj;
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
		return "Shop [roles=" + roles + ", name=" + name + ", abbr=" + abbr + ", company=" + company + ", cellphone="
				+ cellphone + ", phone=" + phone + ", homephone=" + homephone + ", email=" + email + ", comment="
				+ comment + ", password=" + password + ", pass=" + pass + ", showing=" + showing + ", abbrMap="
				+ abbrMap + ", isShow=" + isShow + ", address=" + address + "]\n";
	}


}
