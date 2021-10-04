package com.order2david.shop.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
public class IsShow {
	
//	private final StringProperty shop = new SimpleStringProperty();
//	private final StringProperty abbr = new SimpleStringProperty();
//	private final BooleanProperty is = new SimpleBooleanProperty();
//	
//	public   StringProperty shopProperty() { return this.shop; }
//	public   String getShop() { return this.shopProperty().get(); }
//	public   void setShop(final String shop) { this.shopProperty().set(shop);}
//	
//	public   StringProperty abbrProperty() { return this.abbr; }
//	public   String getAbbr() { return this.abbrProperty().get(); }
//	public   void setAbbr(final String abbr) { this.abbrProperty().set(abbr);}
//	
//	public   BooleanProperty isProperty() { return this.is; }
//	public   Boolean getIs() { return this.isProperty().get(); }
//	public   void setIs(final Boolean is) { this.isProperty().set(is);}
//	
//	@Override
//	public String toString() {
//		return "IsShow [shop=" + shop + ", abbr=" + abbr + ", is=" + is + "]\n";
//	}
//	
	
	String abbr;
	String shop;
	Boolean is;
	
//	public String getAbbr() {
//		return abbr;
//	}
//	public void setAbbr(String abbr) {
//		this.abbr = abbr;
//	}
//	public String getShop() {
//		return shop;
//	}
//	public void setShop(String shop) {
//		this.shop = shop;
//	}
//	public Boolean getIs() {
//		return is;
//	}
//	public void setIs(Boolean is) {
//		this.is = is;
//	}
	@Override
	public String toString() {
		return "IsShow [abbr=" + abbr + ", shop=" + shop + ", is=" + is + "]\n";
	}

}
