package com.yusra.capstone.persistence;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yusra.capstone.entity.Inventory;

/* Repository annotation allows access to the data in the database
 * InventoryDao is the interface that extends the JpaRepository Spring Data interface which provides method for CRUD operations.
 * The JpaReposotry will manage the entity class Inventory and the entity primary key Integer, both given as parameters.
 */

@Repository
public interface InventoryDao extends JpaRepository<Inventory, Integer> {

	Inventory searchItemByItemId(int id);

// ***	SORT BY CATEGORY NATIVE QUERY *** //

	/*
	 * The annotations indicate that the query will modify the database. This query
	 * selects all the rows from the Inventory table where the category matches the
	 * selectedCat parameter which is passed as parameters for the method
	 * filterByCategory. This returns a List of Inventory where the category matches
	 * the specified category.
	 */

	@Modifying
	@Transactional
	@Query(value = " select * from inventory where category like %:selectedcat%", nativeQuery = true)
	List<Inventory> filterByCategory(@Param("selectedcat") String selectedCat);

// ***	SEARCH NATIVE QUERY *** //

	/*
	 * The annotations indicate that the query will modify the database. This query
	 * selects all the rows from the Inventory table where the itemDescription is
	 * like the userInput parameter which is passed as a parameter with type String,
	 * for the method searchForSpecificItems. This returns a List of Inventory where
	 * the itemDescription is like the specified userInput.
	 */

	@Modifying
	@Transactional
	@Query(value = "select * from Inventory where itemDescription like %:userinput%", nativeQuery = true)
	List<Inventory> searchForSpecificItems(@Param("userinput") String userInput);

// ***	UPDATE NATIVE QUERY *** //

	/*
	 * The annotations indicate that the query will modify the database. This query
	 * updates all the rows from the Inventory table with new values based on the
	 * parameter itemId with type int passed into the method.
	 */

	@Modifying
	@Transactional
	@Query(value = "update Inventory set category=:newcat, itemDescription=:newdesc, quantity=:newquant, quantitySold=:newsales,"
			+ " price=:newprice, cost=:newcost where itemId=:itemId", nativeQuery = true)
	int updateItem(@Param("itemId") int itemId, @Param("newcat") String category, @Param("newdesc") String itemDesc,
			@Param("newquant") int quantity, @Param("newsales") int quantitySold, @Param("newprice") BigDecimal price,
			@Param("newcost") BigDecimal cost);
}
