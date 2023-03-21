package com.yusra.capstone.service;

import java.util.List;

import com.yusra.capstone.entity.Inventory;
import com.yusra.capstone.entity.InventoryReport;
import com.yusra.capstone.entity.SalesReport;

/* This interface defines a set of methods that can be used by other parts of the application to manage the inventory items.
 */

public interface InventoryService {

	Inventory getId(int id);

	List<Inventory> getAllInventory();

	boolean addItem(Inventory newItem);

	boolean removeItemById(int itemId);

	boolean updateItem(int itemId, Inventory inventory);

	String stockLevels(double percStock);

	List<Inventory> filterByCategory(String userCategory);

	List<Inventory> searchItemmByUserInput(String userInput);

	SalesReport generateSalesReportForItem(int itemId);

	InventoryReport generateInventoryReportForItem(int itemId);

	List<InventoryReport> generateInventoryReport();

	List<SalesReport> generateSalesReport();

}
