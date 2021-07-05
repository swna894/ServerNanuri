package com.order2david.Product.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Sangwoon Na on 2021. 02. 13..
 */
@Setter @Getter
@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"category", "abbr"})
}) 
public class Category {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ToString.Exclude
	@CreationTimestamp
	@CreatedDate
	@Column(name = "created", updatable = false)
	private LocalDateTime created;

	@JsonIgnore
	@ToString.Exclude
	@UpdateTimestamp
	@LastModifiedDate
	@Column(name = "updated", updatable = true)
	private LocalDateTime updated;
	
	private String abbr;
	private String category;
	
	@Override
	public String toString() {
		return "category [abbr=" + abbr + ", category=" + category + "]\n";
	}
	

}
