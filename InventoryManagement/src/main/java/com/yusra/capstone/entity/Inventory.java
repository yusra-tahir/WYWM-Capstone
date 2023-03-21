package com.yusra.capstone.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* The Inventory class contains the private variables for all elements of the table.
 * The Annotations provided my LOMBOK provide the methods of getters, setters and constructors.
 * Entity annotation tells JPA that this is an entity scan and to generate a table in the database
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Inventory {

	/*
	 * JPA entity requires a PK and Id defines the primary key GeneratedValue
	 * defines the way that the database auto increments
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int itemId;

	private String category;

	/*
	 * This sets itemDescription in the database as unique to ensure same items are
	 * not being added to the database
	 */

	@Column(unique = true)
	private String itemDescription;

	private int quantity;
	private int quantitySold;
	private BigDecimal price;
	private BigDecimal cost;

}
