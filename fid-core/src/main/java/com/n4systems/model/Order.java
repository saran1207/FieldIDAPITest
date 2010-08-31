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
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "orders")
public class Order extends EntityWithOwner implements Listable<Long>, SecurityEnhanced<Order> {
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
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date date) {
		this.orderDate = date;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getPoNumber() {
		return poNumber;
	}
	
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getDisplayName() {
		return getOrderNumber();
	}

	public Order enhance(SecurityLevel level) {
		Order enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		return enhanced;
	}
}
