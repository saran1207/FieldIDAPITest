package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class Order {
	String orderNumber;
	String orderDate;
	String description;
	String purchaseOrder;
	String customer;
	String division;
	List<LineItem> lineItems = new ArrayList<LineItem>();
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("\n  Order Number: " + orderNumber);
		s.append("\n    Order Date: " + orderDate);
		s.append("\n   Description: " + description);
		s.append("\nPurchase Order: " + purchaseOrder);
		s.append("\n      Customer: " + customer);
		s.append("\n      Division: " + division);
		s.append(String.format("\n\n%1$-12s  %2$-100s  %3$-8s  %4$-10s", "Product Code", "Description", "Quantity", "Identified"));
		for(LineItem lineItem : lineItems) {
			String tmp = String.format("\n%1$-12s  %2$-100s  %3$-8s  %4$-10s", lineItem.getProductCode(), lineItem.getDescription(), lineItem.getQuantity(), lineItem.getIdentifiedProducts());
			s.append(tmp);
		}
		return s.toString();
	}
	
	public Order(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public LineItem getLineItem(int index) {
		return lineItems.get(index);
	}
	
	public void addLineItem(LineItem lineItem) {
		lineItems.add(lineItem);
	}
	
	public void removeLineItem(int index) {
		lineItems.remove(index);
	}
	
	public void removeLineItem(LineItem lineItem) {
		lineItems.remove(lineItem);
	}
}
