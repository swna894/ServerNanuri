package com.order2david.etc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
@NoArgsConstructor
@Entity
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String company;
	private String cellphone;
	private String phone;
	private String city;
	private String surburb;
	private String street;
	
	private String dbms;
	private String dbUser;
	private String dbPassword;
	private String backupExe;
	private String backupFolder;
	private String  backupPhoto;	
	private String  backupProgram;	
	private String  backupReport;
	
	private String  mailPort;	
	private String name;
	private String email;
	private String ccEmail;;
	private String gMailPassword;;
	
}
