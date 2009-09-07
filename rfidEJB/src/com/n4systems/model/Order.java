package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithOwner;

@Entity
@Table(name = "orders")
public class Order extends EntityWithOwner implements Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	public enum OrderType { 
		SHOP("shoporder"), CUSTOMER("customerorder");
		
		private String legacyName;
		
		OrderType(String legacyName) {
			this.legacyName = legacyName;
		}
		
		public static OrderType resolveByLegacyName(String name) {
			OrderType resolvedType = null;
			for(OrderType type: OrderType.values()) {
				if(type.legacyName.equals(name)) {
					resolvedType = type;
					break;
				}
			}
			return resolvedType;
		}
	}
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	@Column(nullable=false)
	private String orderNumber;
	
	@Temporal(TemporalType.DATE)
	private Date orderDate;
	
	// TODO: REMOVE_ME
//	@ManyToOne
//	private Customer customer;
//	
//	@ManyToOne
//	private Division division;
	
	private String poNumber;
	private String description;
	
	public Order() {}
	
	public Order(OrderType orderType) {
		this(orderType, null);
	}
	
	public Order(OrderType orderType, String orderNumber) {
		this.orderType = orderType;
		this.orderNumber = orderNumber;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date date) {
		this.orderDate = date;
	}

	// TODO: REMOVE_ME
//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
//
//	public Division getDivision() {
//		return division;
//	}
//
//	public void setDivision(Division division) {
//		this.division = division;
//	}

	public String getPoNumber() {
		return poNumber;
	}
	
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return getOrderNumber();
	}

}
