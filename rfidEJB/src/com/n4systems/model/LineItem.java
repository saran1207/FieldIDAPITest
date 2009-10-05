package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lineitems")
public class LineItem extends EntityWithTenant implements Listable<Long> {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_PRODUCT_CODE = "DEFAULT";
	
	@Column(nullable=false)
	private int idx = -1;		// we initialize with -1 to show that the index has not been set
	
	@Column(nullable=false)
	private long quantity = 0L;
	
	@Column(nullable=false)
	private String productCode = DEFAULT_PRODUCT_CODE;
	private String lineId;
	private String description;
	
	@ManyToOne(optional=false)
	private Order order;
	
	public LineItem() {}

	public LineItem(Order order) {
		setTenant(order.getTenant());
		setOrder(order);
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public int getIndex() {
		return idx;
	}
	
	public void setIndex(int index) {
		this.idx = index;
	}
	
	public int nextIndex() {
		return ++idx;
	}
	
	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productIdent) {
		this.productCode = productIdent;
	}
	
	public String getLineId() {
		return lineId;
	}
	
	public void setLineId(String ident) {
		this.lineId = ident;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return getProductCode();
	}

	public boolean isIndexSet() {
		return (idx >= 0);
	}
}
