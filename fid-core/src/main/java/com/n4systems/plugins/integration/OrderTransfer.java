package com.n4systems.plugins.integration;

import java.io.Serializable;
import java.util.Date;

abstract public class OrderTransfer implements Serializable {
	private static final long serialVersionUID = 2227272744622768144L;
	
	private String orderNumber;
	private String customerName;
	private String customerId;
	private String divisionName;
	private String poNumber;
	private String orderDescription;
	private Date orderDate;
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}


	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String description) {
		this.orderDescription = description;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
}
