package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "lineitems")
public class LineItem extends EntityWithTenant implements Listable<Long>, SecurityEnhanced<LineItem> {
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
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public int getIndex() {
		return idx;
	}
	
	public void setIndex(int index) {
		this.idx = index;
	}
	
	public int nextIndex() {
		return ++idx;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productIdent) {
		this.productCode = productIdent;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getLineId() {
		return lineId;
	}
	
	public void setLineId(String ident) {
		this.lineId = ident;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getDisplayName() {
		return getProductCode();
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isIndexSet() {
		return (idx >= 0);
	}
	
	public LineItem enhance(SecurityLevel level) {
		LineItem enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setOrder(enhance(order, level));
		return enhanced;
	}
}
