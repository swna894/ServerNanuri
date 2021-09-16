package com.order2david.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.order2david.etc.model.Company;
import com.order2david.etc.repository.CompanyRepository;
import com.order2david.order.excel.ExcelInvoicePrint;
import com.order2david.order.model.Order;
import com.order2david.shop.model.Shop;

@Component
public class EmailService {

	@Autowired
	EmailMessage emailMessage;

	@Autowired
	ExcelInvoicePrint excelInvoicePrint;

	private Company company;

	CompanyRepository companyRepository;

	// private String to_mail = "nave8934@naver.com";
	// private String from_mail = "swna8934@gmail.com";
	// private String smtp_host_name = "smtp.gmail.com";
	// private String smtp_host_port = "587"; // 465
	// private String smtp_auth_user = "swna8934@gmail.com";
	// private String name = "sangwoon na";
	// private String smtp_auth_pwd = "woon098)(*";

	private String to_mail;
	private String from_mail;
	private String smtp_host_name = "smtp.gmail.com";
	private String smtp_host_port; // 465
	private String smtp_auth_user;
	private String name;
	private String smtp_auth_pwd;

	private Shop shop;
	// private Supplier supplier;
	private Order order;
	// private List<OrderItem> orderItem;

	public EmailService(CompanyRepository companyRepository) {
		super();
		this.companyRepository = companyRepository;
		company = companyRepository.findAll().get(0);
		this.name = company.getName();
		// this.to_mail = company.getEmail();
		// this.from_mail = company.getCcEmail();
		// this.smtp_auth_pwd = company.getGMailPassword();
		// this.smtp_auth_user = company.getCcEmail();
		this.to_mail = "nave8934@naver.com";
		this.from_mail = "swna8934@gmail.com";
		this.smtp_auth_pwd = "cjhm skgo hrlp mncp";
		this.smtp_auth_user = "swna8934@gmail.com";
		;
		this.smtp_host_port = company.getMailPort();

	}

	public void sendMail(Order order) {
		this.order = order;
		this.shop = order.getShop();
		// this.supplier = order.getSupplier();
		// this.orderItem = order.getOrderItems();

		try {
			// new Thread(() -> {
			sendMyShopMail();
			sendShopMail();
			sendSupplyMail();
			// }).start();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void sendMyShopMail() {
		String subject = "Order Inform : " + shop.getCompany() + "  [ " + order.getInvoice() + " ]  ";
		String message = emailMessage.myMessage(order);

		List<String> toMailList = Arrays.asList(to_mail);
		sending(toMailList, "", subject, message, null, from_mail);
	}

	public void sendShopMail() {

		String file = excelInvoicePrint.printingShop(order);
		List<File> files = new ArrayList<File>();
		files.add(new File(file));

		List<String> toMailList = Arrays.asList(to_mail);

		// Shop Mail
		String subject = "You placed an order to (" + order.getSupplier().getCompany()
				+ ") at dollarshopsuppliers.co.nz ";
		String message = emailMessage.shopMessage(order);

		sending(toMailList, "", subject, message, files, from_mail);
	}

	public void sendSupplyMail() {

		String file = excelInvoicePrint.printing(order);
		List<File> files = new ArrayList<File>();
		files.add(new File(file));

		List<String> toMailList = Arrays.asList(to_mail);

		// Shop Mail
		String subject = "Orders for : " + order.getShop().getCompany() + ", Order No : " + order.getInvoice();
		String message = emailMessage.supplyMessage(order);

		sending(toMailList, "", subject, message, files, from_mail);

		Arrays.stream(new File("/nanuri7788/tomcat/temp").listFiles()).forEach(File::delete);
	}

	public Boolean sending(List<String> toMailList, String cc, String title, String content, List<File> toFileList,
			String from) {

		Boolean result = false;
		// email,name으로 되어 있는 배열을 ArrayList로 바꾸는 작업

		try {
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", smtp_host_name);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.port", smtp_host_port);
			props.put("mail.smtp.auth", "true");

			MyAuthenticator auth = new MyAuthenticator(smtp_auth_user, smtp_auth_pwd);
			Session sess = Session.getInstance(props, auth);
			sess.setDebug(false);

			MimeMessage message = new MimeMessage(sess);
			message.setHeader("content-type", "text/html; charset=EUC-KR");
			message.setFrom(new InternetAddress(from, name, "euc-kr"));

			InternetAddress[] toAddr = makeReciplents(toMailList);
			message.addRecipients(Message.RecipientType.TO, toAddr);
			// 0318

			if (!cc.equals("")) {
				InternetAddress[] ccAddr = makeReciplents(cc);
				message.addRecipients(Message.RecipientType.CC, ccAddr);
			}

			/*
			 * 메세지 title
			 */
			message.setSubject(title, "euc-kr");
			/*
			 * Fill the message
			 */
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(content, "text/html; charset=EUC-KR");

			// Part two attachment / file 추가
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (toFileList != null) {
				for (File file : toFileList) {
					messageBodyPart = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(file);
					messageBodyPart.setDataHandler(new DataHandler(fds));
					messageBodyPart.setFileName(fds.getName());
					multipart.addBodyPart(messageBodyPart);
				}
			}
			/*
			 * Put parts in message
			 */

			message.setContent(multipart);
			message.setSentDate(new java.util.Date());
			Transport.send(message);
			result = true;

		} catch (NoSuchProviderException e) {
			System.err.println("NoSuchProviderException");
			e.printStackTrace();
			return result;
		} catch (MessagingException e) {
			System.err.println("MessagingException");
			e.printStackTrace();
			return result;
		} catch (Exception e) {
			System.err.println("Exception");
			e.printStackTrace();
			return result;
		}

		return result;
	}

	private static InternetAddress[] makeReciplents(List<String> toMailList) {
		InternetAddress[] addr = new InternetAddress[toMailList.size()];

		int i = 0;
		for (String mail : toMailList) {
			try {
				addr[i++] = new InternetAddress(mail);
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
		return addr;
	}

	public static InternetAddress[] makeReciplents(String addrs) throws AddressException {
		StringTokenizer toker;
		String delim = ""; // 구분자
		InternetAddress[] addr = null;

		if (addrs != null) { // 참조 주소가 있을때
			if (addrs.indexOf(",") != -1) { // 구분자 : , 경우
				delim = ",";
			} else if (addrs.indexOf(";") != -1) { // 구분자 : ; 경우
				delim = ";";
			}
		}

		toker = new StringTokenizer(addrs, delim); // , 또는 ;로 이메일주소 구분하여 코크나이져로

		int count = toker.countTokens();
		addr = new InternetAddress[count];

		int i = 0;

		while (toker.hasMoreTokens()) {
			addr[i++] = new InternetAddress(toker.nextToken().trim());
		} // while

		return addr;
	}

}