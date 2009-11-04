package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.DateHelper;

@Entity
@Table(name = "producttypeschedules")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProductTypeSchedule extends EntityWithOwner implements Saveable, SecurityEnhanced<ProductTypeSchedule> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false)
	private ProductType productType;
	
	@ManyToOne(optional=false)
	private InspectionType inspectionType;
	

	@Column(name="frequency")
	private Long frequencyInDays;
	private boolean autoSchedule = true;
	
	public ProductTypeSchedule() {}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public Long getFrequency() {
		return frequencyInDays;
	}

	public void setFrequency(Long frequency) {
		this.frequencyInDays = frequency;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public Date getNextDate(Date startDate) {
		return DateHelper.addDaysToDate(startDate, frequencyInDays);
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean isAutoSchedule() {
		return autoSchedule;
	}

	public void setAutoSchedule(boolean autoSchedule) {
		this.autoSchedule = autoSchedule;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean isOverride() {
		return !getOwner().isPrimary();
	}

	public ProductTypeSchedule enhance(SecurityLevel level) {
		ProductTypeSchedule enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setProductType(enhance(productType, level));
		enhanced.setInspectionType(enhance(inspectionType, level));
		return enhanced;
	}
	
	
}
