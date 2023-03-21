package com.yusra.capstone.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/* InventoryManagementApplication - is the main java class that runs the SpringBoot Java application 
 * EntityScan scans the class path to identify the entities to automate the configuration when working with SQL
 * EnableJpaRepositories locates where the jpa repository classes are, simplifying the process of working with relational databases
 */

@SpringBootApplication(scanBasePackages = "com.yusra")
@EntityScan(basePackages = "com.yusra.capstone.entity")
@EnableJpaRepositories(basePackages = "com.yusra.capstone.persistence")
public class InventoryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}
}
