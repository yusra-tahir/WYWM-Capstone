package com.yusra.capstone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yusra.capstone.entity.Inventory;
import com.yusra.capstone.persistence.InventoryDao;
import com.yusra.capstone.service.InventoryService;

/* The RestController contains the functions that are mapped using REST API,
 * using postman to see the data on the URL paths.
 */

@RestController
public class InventoryRestController {

	/*
	 * Autowired scans the application context to find the matching bean and allows
	 * the use of the functions within the class without having to instantiate it
	 * manually to loosely couple components.
	 */

	@Autowired
	InventoryDao inventoryDao;

	@Autowired
	InventoryService inventoryService;

	/*
	 * GET HTTP request from the database getAllInventory does not take any
	 * parameters and returns a List of inventory, .findAll method provided by JPA
	 * CrudRepository gets all the entities from the database.
	 * 
	 * 
	 */

	@GetMapping(path = "/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Inventory> getAllInventory() {
		return inventoryDao.findAll();
	}

	/*
	 * GET HTTP request from the database getSpecificItem takes String type
	 * userInput as a parameter and sets the path mapping to it, returns a List of
	 * inventory from the database. Calls the class iventoryDao and the function
	 * searchForSpecificItem.
	 */

	@GetMapping(path = "/inventory/desc/{userinput}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Inventory> getSpecificItem(@PathVariable("userinput") String userinput) {
		return inventoryDao.searchForSpecificItems(userinput);
	}

	/*
	 * GET HTTP request from the database getItemsByCategory takes String type
	 * userInput as a parameter and sets the path mapping to it, returns a List of
	 * inventory. Calls the class inventoryDao and the function
	 * searchForSpecificItem.
	 */

	@GetMapping(path = "/inventory/cat/{selectedcat}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Inventory> getItemsByCategory(@PathVariable("selectedcat") String selectedCat) {
		return inventoryDao.filterByCategory(selectedCat);
	}

	/*
	 * POST HTTP request to the database addItem takes the takes Inventory as the
	 * request body and returns a String to postman. Calls the class
	 * inventoryService and the function addItem. accepts JSON and produces text
	 * plain value. If the item was added successfully then returns a String
	 * "New Item Added", otherwise if it has not been added returns the String
	 * "Unable to add new item!"
	 */

	@PostMapping(path = "/newItem", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addItem(@RequestBody Inventory newItem) {
		if (inventoryService.addItem(newItem))
			return "New Item Added";
		return "Unable to add new item!";

	}

	/*
	 * DELETE HTTP request to the database deleteItem takes int type itemId as a
	 * parameter and sets the path mapping to it and returns a String. Calls the
	 * class inventoryService and the function removeItemById. If the item was
	 * deleted successfully then returns a String "Item removed", otherwise if it
	 * has not been deleted returns the String "Item not deleted"
	 */

	@DeleteMapping(path = "/inventory/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteItem(@PathVariable("id") int itemId) {
		if (inventoryService.removeItemById(itemId))
			return "Item removed";
		return "Item not deleted";
	}

	/*
	 * POST HTTP request to the database updateItem takes the takes Inventory
	 * updateItem as the request body and takes int type itemId as a parameter and
	 * sets the path mapping to it, this returns a String. Calls the class
	 * inventoryService and the function updateItem which takes in them both in as
	 * parameters. accepts JSON and produces text plain value. If the item was
	 * updated successfully then returns a String "Item ID " + itemId + " Updated",
	 * otherwise if it has not been updated returns the String "Item ID " +
	 * " NOT Updated!"
	 */

	@PostMapping(path = "/update/{itemId}", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateItem(@PathVariable("itemId") int itemId, @RequestBody Inventory updateItem) {
		if (inventoryService.updateItem(itemId, updateItem))
			return "Item ID " + itemId + " Updated";
		return "Item ID " + " NOT Updated!";
	}

}