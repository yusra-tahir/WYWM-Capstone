package com.yusra.capstone.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yusra.capstone.entity.Inventory;
import com.yusra.capstone.entity.InventoryReport;
import com.yusra.capstone.entity.SalesReport;
import com.yusra.capstone.persistence.InventoryDao;

@Service
public class InventoryServiceImpl implements InventoryService {

	/*
	 * Autowired scans the application context to find the matching bean and allows
	 * the use of the functions within the class without having to instantiate it
	 * manually to loosely couple components.
	 */

	@Autowired
	InventoryDao inventoryDao;

// *** GET ID *** //

	/*
	 * getId function passes id with int type as a parameter. Then uses the
	 * inventoryDao objects method findByID, using id as an argument. If the item is
	 * found it is returned if not then returns null if an exception occurs.
	 * 
	 */

	@Override
	public Inventory getId(int id) {
		try {
			return inventoryDao.findById(id).get();

		} catch (Exception ex) {
			return null;
		}
	}

// *** GET ALL INVENTORY *** //

	/*
	 * getAllInventory function has no parameters. It uses the inventoryDao objects
	 * method findAll. If the item is found a List of Inventory is returned, if not
	 * then returns null.Also, in catch clause returns null if an exception occurs.
	 * 
	 */

	@Override
	public List<Inventory> getAllInventory() {
		try {

			return inventoryDao.findAll();

		} catch (Exception ex) {
			return null;
		}
	}

// *** SORT BY CATEGORY *** //

	/*
	 * filterByCategory function takes userCategory with type String as a parameter.
	 * It uses the inventoryDao objects method filterByCategory with argument
	 * userCategory. If the item is found a List of Inventory is returned filtered
	 * by category. Also, in catch clause returns null if an exception occurs.
	 */

	@Override
	public List<Inventory> filterByCategory(String userCategory) {
		try {
			return inventoryDao.filterByCategory(userCategory);

		} catch (Exception ex) {
			return null;
		}

	}

//	*** ADD ITEM ** //

	/*
	 * addItem function takes entity class Inventory newItem as a parameter. It uses
	 * the inventoryDao objects method save with argument newItem. If the item is
	 * found and does not equal null, it returns true, else if it is equal to null
	 * then returns false. Also returns null if an exception occurs.
	 */

	@Override
	public boolean addItem(Inventory newItem) {
		try {
			if (inventoryDao.save(newItem) != null)
				return true;
			return false;

		} catch (Exception ex) {
			return false;
		}
	}

//	*** DELETE ITEM BY ID ** //

	/*
	 * removeItemById function takes parameter itemId with type int. It uses the
	 * inventoryDao objects method searchItemByItem to find items. If it does not
	 * equal to null, the inventoryDao method deleteById with parameter itemId is
	 * called and returns true. Otherwise, returns false. Also, in catch clause
	 * returns null if an exception occurs.
	 */

	@Override
	public boolean removeItemById(int itemId) {
		try {
			if (inventoryDao.searchItemByItemId(itemId) != null) {
				inventoryDao.deleteById(itemId);
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

// *** SEARCH FOR SPECIFIC ITEM *** //

	/*
	 * searchItemmByUserInput function takes parameter userInput with type String.
	 * It uses the inventoryDao objects method searchForSpecificItems to find items
	 * and returns this method. Catch clause returns null if an exception occurs.
	 */

	@Override
	public List<Inventory> searchItemmByUserInput(String userInput) {
		try {
			return inventoryDao.searchForSpecificItems(userInput);
		} catch (Exception ex) {
			return null;
		}
	}

// *** UPDATE ITEM *** //

	/*
	 * updateItem function takes parameter itemId with type int and entity class
	 * Inventory. It uses the inventoryDao objects method updateItem to update
	 * items. If it is more than 0 then returns true, else returns false. In catch
	 * clause returns null if an exception occurs.
	 */

	@Override
	public boolean updateItem(int itemId, Inventory inventory) {
		try {
			if (inventoryDao.updateItem(itemId, inventory.getCategory(), inventory.getItemDescription(),
					inventory.getQuantity(), inventory.getQuantitySold(), inventory.getPrice(),
					inventory.getCost()) > 0)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

// *** GENERATE SALES REPORT FOR ITEM *** //

	/*
	 * generateSalesReportForItem function takes parameter itemId with type int. It
	 * uses the inventoryDao objects method searchItemByItemId to find items by Id.
	 * If the inventory is found, the method is used to calculate metrics. The
	 * method then creates SalesReport using these metrics and returns it. If the
	 * inventory is not found or an exception occurs during the execution, the
	 * method returns a null value.
	 */

	@Override
	public SalesReport generateSalesReportForItem(int itemId) {

		try {
			Inventory inventory = inventoryDao.searchItemByItemId(itemId);

			if (inventory != null) {

				MathContext m = new MathContext(5);
				BigDecimal quanSold = new BigDecimal(inventory.getQuantitySold());
				BigDecimal quan = new BigDecimal(inventory.getQuantity());
				BigDecimal perc = new BigDecimal(100);
				BigDecimal price = inventory.getPrice();
				BigDecimal cost = inventory.getCost();

				BigDecimal revenuePerUnit = (price.multiply(quanSold, m)).divide(quanSold, 2, RoundingMode.HALF_EVEN);
				BigDecimal profitPerUnit = price.subtract(cost, m);
				BigDecimal costPerUnit = cost.divide(quan, 2, RoundingMode.HALF_EVEN);
				BigDecimal profitMarginPerUnit = ((price.subtract(cost, m)).divide(price, m)).multiply(perc,
						new MathContext(6));

				int profitM = profitMarginPerUnit.intValue();

				SalesReport salesReport = new SalesReport(inventory, inventory.getItemDescription(),
						inventory.getQuantitySold(), revenuePerUnit, profitPerUnit, costPerUnit, profitM);
				return salesReport;
			}

		} catch (Exception ex) {
			return null;
		}
		return null;

	}

	// *** GENERATE OVERALL SALES REPORT *** //

	/*
	 * generateSalesReport function does not have any parameters. It uses the
	 * inventoryDao objects method findAll to find all inventory items. If the
	 * inventory is found, the method is used to calculate metrics. The method then
	 * creates SalesReport using these metrics and returns it. If the inventory is
	 * not found or an exception occurs during the execution, the method returns a
	 * null value.
	 */

	@Override
	public List<SalesReport> generateSalesReport() {
		try {

			List<Inventory> inventoryList = inventoryDao.findAll();
			List<SalesReport> salesReportList = new ArrayList<>();

			for (int i = 0; i < inventoryList.size(); i++) {

				SalesReport salesReport = new SalesReport();

				Inventory inventory = inventoryList.get(i);

				MathContext m = new MathContext(5);
				BigDecimal quanSold = new BigDecimal(inventory.getQuantitySold());
				BigDecimal quan = new BigDecimal(inventory.getQuantity());
				BigDecimal perc = new BigDecimal(100);
				BigDecimal price = inventory.getPrice();
				BigDecimal cost = inventory.getCost();

				BigDecimal revenuePerUnit = (price.multiply(quanSold, m)).divide(quanSold, 2, RoundingMode.HALF_EVEN);
				BigDecimal profitPerUnit = price.subtract(cost, m);
				BigDecimal costPerUnit = cost.divide(quan, 2, RoundingMode.HALF_EVEN);
				BigDecimal profitMarginPerUnit = ((price.subtract(cost, m)).divide(price, m)).multiply(perc,
						new MathContext(6));

				int profitM = profitMarginPerUnit.intValue();

				salesReport.setInventory(inventory);
				salesReport.setProduct(inventory.getItemDescription());
				salesReport.setQuantitySold(inventory.getQuantitySold());
				salesReport.setRevenuePerUnit(revenuePerUnit);
				salesReport.setProfitPerUnit(profitPerUnit);
				salesReport.setCostPerUnit(costPerUnit);
				salesReport.setProfitMargin(profitM);

				salesReportList.add(salesReport);

			}

			return salesReportList;

		} catch (Exception ex) {
			return null;
		}
	}

	// *** GENERATE INVENTORY REPORT FOR ITEM *** //

	// *** STOCK LEVEL CHECK *** //

	// *** GENERATE INVENTROY REPORT FOR ITEM *** //

	/*
	 * stockLevels function takes parameter percStock with type double from the
	 * method below and returns a String indication stockLevels. String is equal to
	 * "STOCK AVAILABLE" for percentage greater than or equal to 50%, "OUT OF STOCK"
	 * for percentage equal to 0%, and "LOW STOCK" for any other percentage value
	 * below 50%.
	 */

	@Override
	public String stockLevels(double percStock) {
		String stockLevels = null;

		if (percStock >= 50.0)
			stockLevels = "STOCK AVAILABLE";

		if (percStock == 0)
			stockLevels = "OUT OF STOCK";

		if (percStock < 50.0 & percStock != 0)
			stockLevels = "LOW STOCK";

		return stockLevels;
	}

	// *** GENERATE INVENTROY REPORT FOR ITEM *** //

	/*
	 * generateInventoryReportForItem function takes parameter itemId with type int.
	 * It uses the inventoryDao objects method searchItemByItemId to find items by
	 * Id. If the inventory is found, the method is used to calculate metrics. The
	 * method then creates InventoryReport using these metrics and returns it. If
	 * the inventory is not found or an exception occurs during the execution, the
	 * method returns a null value.
	 */

	@Override
	public InventoryReport generateInventoryReportForItem(int itemId) {

		try {

			Inventory inventory = inventoryDao.searchItemByItemId(itemId);
			if (inventory != null) {

				double cost = inventory.getCost().doubleValue();
				double inventoryOnHand = inventory.getQuantity() - inventory.getQuantitySold();

				int stockRemaining = (int) inventoryOnHand;

				double percStock = (inventoryOnHand / inventory.getQuantity()) * 100;

				String stockLevels = stockLevels(percStock);

				InventoryReport inventoryReport = new InventoryReport(inventory, inventory.getItemDescription(),
						inventory.getQuantity(), stockRemaining, stockLevels);
				return inventoryReport;

			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

	// *** GENERATE INVENTROY REPORT *** //

	/*
	 * generateInventoryReport function does not have any parameters. It uses the
	 * inventoryDao objects method findAll to find inventory . If the inventory is
	 * found, the method is used to calculate metrics. The method then creates
	 * InventoryReport using these metrics and returns it. If the inventory is not
	 * found or an exception occurs during the execution, the method returns a null
	 * value.
	 */

	@Override
	public List<InventoryReport> generateInventoryReport() {

		try {

			List<Inventory> inventoryList = inventoryDao.findAll();
			List<InventoryReport> inventoryReportList = new ArrayList<>();

			for (int i = 0; i < inventoryList.size(); i++) {
				InventoryReport inventoryReport = new InventoryReport();

				Inventory inventory = inventoryList.get(i);

				double cost = inventory.getCost().doubleValue();
				double inventoryOnHand = inventory.getQuantity() - inventory.getQuantitySold();

				int stockRemaining = (int) inventoryOnHand;

				double percStock = (inventoryOnHand / inventory.getQuantity()) * 100;

				String stockLevels = stockLevels(percStock);

				inventoryReport.setInventory(inventory);
				inventoryReport.setProduct(inventory.getItemDescription());
				inventoryReport.setQuant(inventory.getQuantity());
				inventoryReport.setInventoryOnHand(stockRemaining);
				inventoryReport.setStockLevels(stockLevels);

				inventoryReportList.add(inventoryReport);

			}

			return inventoryReportList;

		} catch (Exception ex) {
			return null;
		}
	}

}
