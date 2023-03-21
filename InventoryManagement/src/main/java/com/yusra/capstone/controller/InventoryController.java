package com.yusra.capstone.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.yusra.capstone.entity.Inventory;
import com.yusra.capstone.entity.InventoryReport;
import com.yusra.capstone.entity.SalesReport;
import com.yusra.capstone.service.InventoryServiceImpl;

@Controller
public class InventoryController {

	/*
	 * Autowired scans the application context to find the matching bean and allows
	 * the use of the functions within the class without having to instantiate it
	 * manually to loosely couple components.
	 */

	@Autowired
	private InventoryServiceImpl inventoryService;

// *** Landing Page *** //

	/*
	 * This method handles a HTTP GET requests to the root URL ("/") and returns a
	 * ModelAndView object with a view name of "landingPage".
	 */

	@RequestMapping("/")
	public ModelAndView welcomePageController() {
		return new ModelAndView("landingPage");
	}

//	*** View Inventory *** //

	/*
	 * When requested, a new ModelAndView object is created and obtains a list of
	 * all inventory items using the getAllInventory() method from the
	 * inventoryService object.
	 * 
	 * If the inventory list is not empty, the method adds the inventory list to the
	 * ModelAndView object using the key "inventoryList" and sets the view name to
	 * "allInventoryPage".
	 * 
	 * If the inventory list is empty or an exception occurs, the method adds an
	 * error message to the ModelAndView object using the key "message" and sets the
	 * view name to "allInventoryPage".The method returns the ModelAndView object.
	 */

	@RequestMapping("/inventory")
	public ModelAndView allInventoryController() {
		ModelAndView modelAndView = new ModelAndView();

		List<Inventory> inventoryList = inventoryService.getAllInventory();

		if (inventoryList != null) {
			modelAndView.addObject("inventoryList", inventoryList);
			modelAndView.setViewName("allInventoryPage");
		} else {
			modelAndView.addObject("message", "Unable to view inventory list, please try again!");
			modelAndView.setViewName("allInventoryPage");
		}
		return modelAndView;

	}

	/*
	 * When requested, a new ModelAndView object is created and obtains a list of
	 * inventory items that matches the category with type String using method from
	 * the inventoryService object. Also, a list of inventory items that gets all
	 * inventory using method from the inventoryService object.
	 * 
	 * If the inventory list is not empty and does not equal null, the method adds
	 * the inventory list to the ModelAndView object using the key "inventoryList"
	 * and sets the view name to "allInventoryPage". Else, the method adds an error
	 * message to the ModelAndView object using the key "message" and sets the view
	 * name to "allInventoryPage".The method returns the ModelAndView object.
	 * 
	 * If the inventory list is empty then the method adds the inventory list to the
	 * ModelAndView object using the key "inventory" and sets the view name to
	 * "allInventoryPage".
	 *
	 */

	@RequestMapping("/filterByCategory")
	public ModelAndView filterByCategory(@RequestParam("category") String category) {
		ModelAndView modelAndView = new ModelAndView();

		List<Inventory> inventoryList = inventoryService.filterByCategory(category);
		List<Inventory> inventory = inventoryService.getAllInventory();

		if (inventoryList != null) {
			modelAndView.addObject("inventoryList", inventoryList);
			modelAndView.setViewName("allInventoryPage");
		} else {
			modelAndView.addObject("message",
					"Unable to filter by the category | " + category + " | , please try again!");
			modelAndView.setViewName("allInventoryPage");
		}

		if (inventoryList.isEmpty()) {
			modelAndView.addObject("inventory", inventoryList);
			modelAndView.setViewName("allInventoryPage");
		}

		return modelAndView;
	}

	/*
	 * When requested, a new ModelAndView object is created and obtains a list of
	 * inventory items that matches the userInput with type String using method from
	 * the inventoryService object.
	 * 
	 * If the inventory list is not empty, the method adds the inventory list to the
	 * ModelAndView object using the key "inventoryList" and sets the view name to
	 * "allInventoryPage".
	 * 
	 * If the inventory list is empty or an exception occurs, the method adds an
	 * error message to the ModelAndView object using the key "message" and sets the
	 * view name to "allInventoryPage".The method returns the ModelAndView object.
	 */

	@RequestMapping("/searchForSpecificItem")
	public ModelAndView searchForSpecificItem(@RequestParam("userItem") String userInput) {
		ModelAndView modelAndView = new ModelAndView();

		List<Inventory> inventoryList = inventoryService.searchItemmByUserInput(userInput);

		if (inventoryList != null) {
			modelAndView.addObject("inventoryList", inventoryList);
			modelAndView.setViewName("allInventoryPage");
		} else {
			modelAndView.addObject("message", "Input | " + userInput + " | , could not be found!");
			modelAndView.setViewName("allInventoryPage");
		}

		return modelAndView;

	}
// *** Add Inventory Item *** //

	/*
	 * When the method is called, it returns a new instance of ModelAndView class,
	 * which sets the view name as "newInventoryPage", sets new Inventory object as
	 * the model attribute with the name "newInventory".
	 */

	@RequestMapping("/new-inventory")
	public ModelAndView addPageController() {
		return new ModelAndView("newInventoryPage", "newInventory", new Inventory());
	}

	/*
	 * This method handles a POST request to add a new inventory item. The form data
	 * includes the item's category, description, quantity, quantity sold, price,
	 * and cost.
	 * 
	 * Which then creates a new Inventory object and passes it to the
	 * InventoryService to add to the inventory. If the item is successfully added,
	 * there is a success message and returns the "newInventoryPage".
	 * 
	 * If the item already exists in the inventory, the method sets an error message
	 * and returns the "newInventoryPage" view with the form data populated.
	 * 
	 */

	@RequestMapping("/add-item")
	public ModelAndView addInventoryItemController(@ModelAttribute("newInventory") Inventory newItem,
			@RequestParam("category") String category, @RequestParam("itemDescription") String itemDescription,
			@RequestParam("quantity") int quantity, @RequestParam("quantitySold") int quantitySold,
			@RequestParam("price") BigDecimal price, @RequestParam("cost") BigDecimal cost) {
		ModelAndView modelAndView = new ModelAndView();

		newItem.setCategory(category);
		newItem.setItemDescription(itemDescription);
		newItem.setQuantity(quantitySold);
		newItem.setQuantitySold(quantitySold);
		newItem.setPrice(price);
		newItem.setCost(cost);

		if (inventoryService.addItem(newItem) != false) {
			modelAndView.addObject("newItem", newItem);
			modelAndView.addObject("message",
					"The item |" + newItem.getItemDescription() + "| has been added successfully!");
			modelAndView.setViewName("newInventoryPage");

		} else {
			modelAndView.addObject("message", "Item already exists, please try again!");
			modelAndView.setViewName("newInventoryPage");
		}

		return modelAndView;
	}

// *** Delete Inventory Items *** //

	@RequestMapping("/delete-item")
	public ModelAndView deletePageController() {
		return new ModelAndView("deleteInventoryPage");
	}

	/*
	 * When requested, a new ModelAndView object is created and deletes item by id
	 * using the removeItemById method from the inventoryService object.
	 * 
	 * If the inventory list is true, the method returns message that the item has
	 * been deleted, else a message saying the item does not exist. Then adds the
	 * message to the ModelAndView object using the key "message" and sets the view
	 * name to "deleteInventoryPage".
	 * 
	 */

	@RequestMapping("/deleteItem")
	public ModelAndView deleteItemController(@RequestParam("itemId") int itemId) {
		ModelAndView modelAndView = new ModelAndView();
		String message;

		if (inventoryService.removeItemById(itemId))
			message = "Item | " + itemId + " | deleted!";
		else
			message = "Item | " + itemId + " | does not exist!";

		modelAndView.addObject("message", message);
		modelAndView.setViewName("deleteInventoryPage");

		return modelAndView;
	}

	/*
	 * When requested, a new ModelAndView object is created which takes in parameter
	 * id with type int and sets path variable as int.This then deletes item by id
	 * using the removeItemById method from the inventoryService object.
	 * 
	 * If the inventory list is true, the method returns message that the item has
	 * been deleted, else a message saying the item does not exist. Then adds the
	 * message to the ModelAndView object using the key "message" and sets the view
	 * name to "outputPage".
	 */

	@RequestMapping("/delete/{id}")
	public ModelAndView deleteItem(@PathVariable(name = "id") int id) {
		ModelAndView modelAndView = new ModelAndView();

		String message;

		if (inventoryService.removeItemById(id))
			message = "Item | " + id + " | deleted!";
		else
			message = "Item | " + id + " | does not exist!";

		modelAndView.addObject("message", message);
		modelAndView.setViewName("outputPage");

		return modelAndView;
	}

// *** Generate Sales Report *** //

	// *** PER ITEM *** //

	/*
	 * When requested, a new ModelAndView object is created which takes in parameter
	 * itemId with type int and sets path variable as itemId.This then generates
	 * salesReport for item with specified id using the generateSalesReportForItem
	 * method from the inventoryService object.
	 * 
	 * If the salesReport is not empty, the method adds salesReport to the
	 * ModelAndView object using the key "salesReport" and sets the view name to
	 * "salesReportPage".
	 * 
	 * If the salesReport is empty or an exception occurs, the method adds an error
	 * message to the ModelAndView object using the key "message" and sets the view
	 * name to "salesReportPage".The method returns the ModelAndView object.
	 */

	@RequestMapping("/salesReport/{itemId}")
	public ModelAndView generateSalesReportPerItem(@PathVariable(name = "itemId") int itemId) {
		ModelAndView modelAndView = new ModelAndView();

		SalesReport salesReport = inventoryService.generateSalesReportForItem(itemId);

		if (salesReport != null) {
			modelAndView.addObject("salesReport", salesReport);
			modelAndView.setViewName("salesReportPage");

		} else {
			modelAndView.addObject("message", "Unable to generate sales report for item " + itemId);
			modelAndView.setViewName("salesReportPage");

		}

		return modelAndView;
	}

	// *** OVERALL *** //

	/*
	 * When requested, a new ModelAndView object is created. This then generates
	 * salesReport using the generateSalesReport method from the inventoryService
	 * object.
	 * 
	 * If the salesReport is not empty, the method adds salesReport to the
	 * ModelAndView object using the key "salesReport" and sets the view name to
	 * "salesReportPage".
	 * 
	 * If the salesReport is empty or an exception occurs, the method adds an error
	 * message to the ModelAndView object using the key "message" and sets the view
	 * name to "salesReportPage".The method returns the ModelAndView object.
	 */

	@RequestMapping("/salesReport")
	public ModelAndView generateSalesReport() {
		ModelAndView modelAndView = new ModelAndView();

		List<SalesReport> salesReport = inventoryService.generateSalesReport();

		if (salesReport != null) {
			modelAndView.addObject("salesReport", salesReport);
			modelAndView.setViewName("salesReportPage");

		} else {
			modelAndView.addObject("message", "Sales report could not be generated!");
			modelAndView.setViewName("allInventoryPage");

		}

		return modelAndView;
	}

// *** Generate Inventory Report *** //

	// *** PER ITEM *** //

	/*
	 * When requested, a new ModelAndView object is created which takes in parameter
	 * itemId with type int and sets path variable as itemId.This then generates
	 * inventoryReport for item with specified id using the
	 * generateInventoryReportForItem method from the inventoryService object.
	 * 
	 * If the inventoryReport is not empty, the method adds inventoryReport to the
	 * ModelAndView object using the key "inventoryReport" and sets the view name to
	 * "inventoryReportPage".
	 * 
	 * If the inventoryReport is empty or an exception occurs, the method adds an
	 * error message to the ModelAndView object using the key "message" and sets the
	 * view name to "inventoryReportPage".The method returns the ModelAndView
	 * object.
	 */

	@RequestMapping("/inventoryReport/{itemId}")
	public ModelAndView generateInventoryReportPerItem(@PathVariable(name = "itemId") int itemId) {
		ModelAndView modelAndView = new ModelAndView();

		InventoryReport inventoryReport = inventoryService.generateInventoryReportForItem(itemId);

		if (inventoryReport != null) {
			modelAndView.addObject("inventoryReport", inventoryReport);
			modelAndView.setViewName("inventoryReportPage");
		} else {
			modelAndView.addObject("message", "Unable to generate inventory report for item " + itemId);
			modelAndView.setViewName("inventoryReportPage");
		}

		return modelAndView;

	}

	// *** OVERALL *** //

	/*
	 * When requested, a new ModelAndView object is created. This then generates
	 * inventoryReport using the generateInventoryReport method from the
	 * inventoryService object.
	 * 
	 * If the inventoryReport is not empty, the method adds inventoryReport to the
	 * ModelAndView object using the key "inventoryReport" and sets the view name to
	 * "inventoryReportPage".
	 * 
	 * If the inventoryReport is empty or an exception occurs, the method adds an
	 * error message to the ModelAndView object using the key "message" and sets the
	 * view name to "inventoryReportPage".The method returns the ModelAndView
	 * object.
	 */

	@RequestMapping("/inventoryReport")
	public ModelAndView generateInventoryReport() {
		ModelAndView modelAndView = new ModelAndView();

		List<InventoryReport> inventoryReport = inventoryService.generateInventoryReport();

		if (inventoryReport != null) {
			modelAndView.addObject("inventoryReport", inventoryReport);
			modelAndView.setViewName("inventoryReportPage");
		} else {
			modelAndView.addObject("message", "Inventory report could not be generated");
			modelAndView.setViewName("allInventoryPage");
		}

		return modelAndView;

	}

// *** Update Inventory Items *** //

	/*
	 * This method calls the getId() method from the inventoryService object to
	 * retrieve the inventory item with the given itemId.
	 * 
	 * Then, the method creates a new ModelAndView object and adds the retrieved
	 * inventory item to the model with the key "updateInventory". It also sets the
	 * view name to "updateInventoryPage". Then it returns the ModelAndView object.
	 */

	@RequestMapping("/update/{itemId}")
	public ModelAndView updatePageController(@PathVariable(name = "itemId") int itemId) {
		ModelAndView modelAndView = new ModelAndView();
		Inventory updateInventory = inventoryService.getId(itemId);

		modelAndView.addObject("updateInventory", updateInventory);
		modelAndView.setViewName("updateInventoryPage");

		return modelAndView;
	}

	/*
	 * This method takes in the parameters which are taken from a form. The method
	 * creates a new inventory object with the updated details and then calls the
	 * updateItem() method of the inventoryService class to update the item in the
	 * inventory.
	 * 
	 * Then return a ModelAndView object with a message saying that the inventory
	 * with Item Id has been successfully updated, else that it is unable to update
	 * item.
	 * 
	 * If the update was successful, the updated item object and a success message
	 * is added to the model and the updateInventoryPage is returned. If the update
	 * was unsuccessful, an error message is added to the model and the
	 * updateInventoryPage is returned.
	 * 
	 */

	@RequestMapping("/updateItem")
	public ModelAndView updateInventoryItemController(@ModelAttribute("updateInventory") Inventory updateInventory,
			@RequestParam("category") String category, @RequestParam("itemDescription") String itemDescription,
			@RequestParam("quantity") int quantity, @RequestParam("quantitySold") int quantitySold,
			@RequestParam("price") BigDecimal price, @RequestParam("cost") BigDecimal cost) {
		ModelAndView modelAndView = new ModelAndView();

		updateInventory.getItemId();
		updateInventory.setCategory(category);
		updateInventory.setItemDescription(itemDescription);
		updateInventory.setQuantity(quantity);
		updateInventory.setQuantitySold(quantitySold);
		updateInventory.setPrice(price);
		updateInventory.setCost(cost);

		if (inventoryService.updateItem(updateInventory.getItemId(), updateInventory) != false) {
			modelAndView.addObject("updateInventory", updateInventory);
			modelAndView.addObject("message", "Item | " + updateInventory.getItemId() + " | has been updated!");
			modelAndView.setViewName("updateInventoryPage");

		} else {
			modelAndView.addObject("message", "Unable to update item, please try again!");
			modelAndView.setViewName("updateInventoryPage");
		}

		return modelAndView;

	}

}
