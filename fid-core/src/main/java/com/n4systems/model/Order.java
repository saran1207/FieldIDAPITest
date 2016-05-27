package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Cacheable
@org.hibernate.annotations.Cache(region = "AssetCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
	
	@AllowSafetyNetworkAccess
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@AllowSafetyNetworkAccess
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@AllowSafetyNetworkAccess
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date date) {
		this.orderDate = date;
	}

	@AllowSafetyNetworkAccess
	public String getPoNumber() {
		return poNumber;
	}
	
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	@AllowSafetyNetworkAccess
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		return getOrderNumber();
	}

	public Order enhance(SecurityLevel level) {
		Order enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		return enhanced;
	}
}
