package com.yusra.capstone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/* The InventoryReport class contains the private variables for all the elements.
 * The Annotations provided by LOMBOK provide the methods of getters, setters, constructors and ToString.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InventoryReport {

	private Inventory inventory;
	private String product;
	private int quant;
	private int inventoryOnHand;
	private String stockLevels;

}
