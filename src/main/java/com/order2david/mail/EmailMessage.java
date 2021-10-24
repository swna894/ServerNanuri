package com.order2david.mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;

@Component
public class EmailMessage{
	
	private LocalDateTime localDate;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public String myMessage(Order order) {
		String shop = order.getShop().getCompany();
		String supplier = order.getSupplier().getCompany();
		String invoice = order.getInvoice();
		Double amount = order.getAmount();
		
		String message = "<p><strong> - Shop : " + shop + "</strong></p>";
		message = message + "<p><strong> - Order No : " + invoice + "</strong></p>";
		message = message + "<p><strong> - Amount : $ " + String.format("%.2f", amount) + "</strong></p>";
		message = message + "<p><strong> - Supplier : " + supplier + "</strong></p>";
		
	  	return message;

	}

	public String shopMessage(Order order) {
		localDate = LocalDateTime.now();
		String shop = order.getShop().getCompany();
		String supplier = order.getSupplier().getCompany();
		String invoice = order.getInvoice();
		Double amount = order.getAmount();
		
		String message = "<p><strong> Thank you very much for your order </strong></p>";
		message = message + "<br>";
		message = message + "<p><strong><i> - Shop Name : </i>" + shop + "</strong></p>";
		message = message + "<p><strong><em> - Your order to : </em>" + supplier + "</strong></p>";
		message = message + "<p><strong><em> - Order No : </em>" + invoice + "</strong></p>";
		message = message + "<p><strong><em> - Date & Time Ordered : </em>" + localDate.format(formatter) + "</strong></p>";
		message = message + "<br>";
		message = message + "<p><strong><em> Order Details </em></strong></p>";
		message = message + "<p><strong> Amount (exclude GST) : $" + String.format("%.2f", amount)  + "</strong></p>";
		
		message = message + "<table border=\"1\" >";
		message = message + "<tr>"
						  + "<td> </td>"
						  + "<td align = \"center\">Code </td>"
						  + "<td align = \"center\">Desc</td>"
						  + "<td align = \"center\">Qty</td>"
						  + "<td align = \"center\">Price</td>"
						  + "<td align = \"center\">Total</td>"
						  + "</tr>";
		
		int index = 1;
		for (OrderItem item : order.getOrderItems()) {		
			message = message + "<tr>"
							  + "<td width =\"30\" align = \"center\"> " + index++ + " </td>"
							  + "<td width =\"100\" > " + item.getCode() + " </td>"
							  + "<td> "+ item.getDescription() + " </td>"
							  + "<td width =\"50\" align = \"center\"> " + item.getQty() + " </td>"
							  + "<td width =\"50\" align = \"center\"> " + item.getPrice()  + " </td>"
							  + "<td width =\"50\" align = \"center\"> " + item.getAmount() + " </td>"
							  + "</tr> ";
		}
		message = message + "</table>";
		
		
		message = message + "<br>";
		message = message + "<br>";
		message = message + "<p><em> Your order will be arrived within 2 - 5 working days. </em></p>";
		message = message + "<p><em> If you have any concerns, please email to </em><a href=\"mailto:davidna@xtra.co.nz\"> davidna@xtra.co.nz </a><em>or contact David Na 027-652-1111. </em></p>";
		message = message + "<br>";
		message = message + "<p><strong>Best Regards</strong></p>";
		message = message + "<p><strong>David Na</strong></p>";
		
		return message;
	}
	
	public String supplyMessage(Order order) {
		localDate = LocalDateTime.now();
		String shop = order.getShop().getCompany();
		String invoice = order.getInvoice();
		Double amount = order.getAmount();
		
		String message = "<p><strong> Hi </strong></p>";
		message = message + "<br>";
		message = message + "<p><strong> You’ve got an order from dollarshopsuppliers.co.nz</strong></p>";
		message = message + "<p><strong> Please find an attached file of order sheet.</strong></p>";
		message = message + "<br>";
		message = message + "<br>";
		message = message + "<p><strong><em> - Shop Name : </em>" + shop + "</strong></p>";
		message = message + "<p><strong><em> - Order No : </em>" + invoice + "</strong></p>";
		message = message + "<p><strong><em> - Date & Time Ordered : </em>" + localDate.format(formatter) + "</strong></p>";
		message = message + "<br>";
		message = message + "<p><strong><em> Order Details </em></strong></p>";
		message = message + "<p><strong> Amount (exclude GST) : $" + String.format("%.2f", amount)  + "</strong></p>";
		
		message = message + "<table border=\"1\" >";
		message = message + "<tr>"
						  + "<td> </td>"
						  + "<td align = \"center\">Code </td>"
						  + "<td align = \"center\">Desc</td>"
						  + "<td align = \"center\">Qty</td>"
						  + "<td align = \"center\">Price</td>"
						  + "<td align = \"center\">Total</td>"
						  + "</tr>";
		
		int index = 1;
		for (OrderItem item : order.getOrderItems()) {		
			message = message + "<tr>"
							  + "<td width =\"30\" align = \"center\"> " + index++ + " </td>"
							  + "<td width =\"100\" > " + item.getCode() + " </td>"
							  + "<td> "+ item.getDescription() + " </td>"
							  + "<td width =\"50\" align = \"center\"> " + item.getQty() + " </td>"
							  + "<td width =\"50\" align = \"center\"> " + item.getPrice()  + " </td>"
							  + "<td width =\"50\" align = \"center\"> " + item.getAmount() + " </td>"
							  + "</tr> ";
		}
		message = message + "</table>";
		
		message = message + "<br>";
		message = message + "<br>";
		message = message + "<p><em> If you have any concerns, please email to </em><a href=\"mailto:davidna@xtra.co.nz\"> davidna@xtra.co.nz </a><em>or contact David Na 027-652-1111. </em></p>";
		message = message + "<br>";
		message = message + "<p><strong>Best Regards</strong></p>";
		message = message + "<p><strong>David Na</strong></p>";
		
		return message;
	}

}