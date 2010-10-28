package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "lineitems")
public class LineItem extends EntityWithTenant implements Listable<Long>, SecurityEnhanced<LineItem> {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_ASSET_CODE = "DEFAULT";
	
	@Column(nullable=false)
	private int idx = -1;		// we initialize with -1 to show that the index has not been set
	
	@Column(nullable=false)
	private long quantity = 0L;
	
	@Column(nullable=false, name="assetcode")
	private String assetCode = DEFAULT_ASSET_CODE;
	private String lineId;
	private String description;
	
	@ManyToOne(optional=false)
	private Order order;
	
	public LineItem() {}

	public LineItem(Order order) {
		setTenant(order.getTenant());
		setOrder(order);
	}
	
	@AllowSafetyNetworkAccess
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@AllowSafetyNetworkAccess
	public int getIndex() {
		return idx;
	}
	
	public void setIndex(int index) {
		this.idx = index;
	}
	
	public int nextIndex() {
		return ++idx;
	}
	
	@AllowSafetyNetworkAccess
	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	@AllowSafetyNetworkAccess
	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	
	@AllowSafetyNetworkAccess
	public String getLineId() {
		return lineId;
	}
	
	public void setLineId(String ident) {
		this.lineId = ident;
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
		return getAssetCode();
	}

	@AllowSafetyNetworkAccess
	public boolean isIndexSet() {
		return (idx >= 0);
	}
	
	public LineItem enhance(SecurityLevel level) {
		LineItem enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setOrder(enhance(order, level));
		return enhanced;
	}
}
