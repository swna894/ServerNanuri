package com.newVista.shop.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
	private  String street;
	private  String city;
	private  String surburb;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSurburb() {
		return surburb;
	}
	public void setSurburb(String surburb) {
		this.surburb = surburb;
	}
	
	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city + ", surburb=" + surburb + "]\n";
	}
	
}
