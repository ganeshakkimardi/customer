package com.sony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sony.entity.Customer;
import com.sony.entity.CustomerList;
import com.sony.entity.ErrorInfo;
import com.sony.service.CustomerService;

@RestController
@RequestMapping("/api/customers")

public class CustomerController {

	@Autowired
	private CustomerService service;

	// display all customers
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public CustomerList handleGetCustomer(@RequestParam(name = "_page", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "_limit", defaultValue = "10") Integer pageSize,
			@RequestParam(name = "_sort", defaultValue = "customerId") String sortBy) {
		return new CustomerList(service.getAllCustomers(pageNum, pageSize, sortBy));
	}

	// find product by id
	@GetMapping("/{customerId}")
	public ResponseEntity<Object> handleGetOneProduct(@PathVariable String customerId) {
		Customer customer = service.getCustomerById(customerId);
		if (customer == null) {
			return ResponseEntity.status(404).body(new ErrorInfo("No Data found for id: " + customerId));
		}
		return ResponseEntity.ok(customer);
	}

	// to add new customer
	@PostMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> handlePost(@RequestBody Customer customer) {
		try {
			customer = service.addNewCustomer(customer);
			return ResponseEntity.status(HttpStatus.CREATED).body(customer);
		} catch (Exception ex) {
			return ResponseEntity.status(500).body(new ErrorInfo(ex.getMessage()));
		}
	}

	// to update CUSTOMER
	@PutMapping(path = "/{customerId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> handlePut(@PathVariable String customerId, @RequestBody Customer customer) {
		try {
			customer = service.updateCustomer(customerId, customer);
			return ResponseEntity.ok(customer);
		} catch (Exception ex) {
			return ResponseEntity.status(500).body(new ErrorInfo(ex.getMessage()));
		}
	}

}
