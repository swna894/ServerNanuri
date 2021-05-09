package com.order2david.mail;

import java.util.Arrays;
import java.util.List;

import com.order2david.order.model.Order;

public class EmailService implements MailConstant{


	SendMail mail = new SendMail();
	//@Autowired
	//EnvironmentRepository environmentRepository;
	
    public void sendMail(String subject, String message) {
    	//Environment environment = environmentRepository.findByCompany("NewVista");
 
    	//List<String> toMailList = Arrays.asList(environment.getCcUser());
    	List<String> toMailList = Arrays.asList(to_mail);
    	mail.sending(toMailList, "", subject, message, null, from_mail);
    }

	public void sendMail(Order order) {
		String shop = order.getShop().getCompany();
		String supplier = order.getSupplier().getCompany();
		String invoice = order.getInvoice();
		Double amount = order.getAmount();
		
		String subject = "Order Inform : " + shop + "  [ " + invoice + " ]  ";
		String message = "<p> - Shop : " + shop + "</p>";
		message = message + "<p> - Invoice : " + invoice + "</p>";
		message = message + "<p> - Amount : $ " + String.format("%.2f", amount) + "</p>";
		message = message + "<p> - Supplier : " + supplier;
		
		sendMail(subject, message);
		
	}
}