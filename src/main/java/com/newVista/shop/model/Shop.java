package com.newVista.shop.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */

@Entity
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@CreationTimestamp
	@CreatedDate
	@Column(name = "created", updatable = false)
	private LocalDateTime created;

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
	@Column(columnDefinition = "boolean default true")
	private  Boolean showing;
	//@Column(columnDefinition = "varchar(16) default '")
	//private String roles;
	
	@Embedded
	private Address address;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHomephone() {
		return homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}

	public String getAvalonId() {
		return avalonId;
	}

	public void setAvalonId(String avalonId) {
		this.avalonId = avalonId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Boolean getShowing() {
		return showing;
	}

	public void setShowing(Boolean showing) {
		this.showing = showing;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

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
		return "Shop [id=" + id + ", created=" + created + ", updated=" + updated + ", name=" + name + ", abbr=" + abbr
				+ ", company=" + company + ", cellphone=" + cellphone + ", phone=" + phone + ", homephone=" + homephone
				+ ", avalonId=" + avalonId + ", email=" + email + ", comment=" + comment + ", password=" + password
				+ ", pass=" + pass + ", showing=" + showing + ", address=" + address + "]\n";
	}

}
