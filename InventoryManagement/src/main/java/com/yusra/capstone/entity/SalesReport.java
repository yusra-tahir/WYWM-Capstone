package com.yusra.capstone.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/* The SalesReport class contains the private variables for all the elements.
 * The Annotations provided by LOMBOK provide the methods of getters, setters, constructors and ToString.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesReport {

	private Inventory inventory;
	private String product;
	private int quantitySold;
	private BigDecimal revenuePerUnit;
	private BigDecimal profitPerUnit;
	private BigDecimal costPerUnit;
	private double profitMargin;

}
