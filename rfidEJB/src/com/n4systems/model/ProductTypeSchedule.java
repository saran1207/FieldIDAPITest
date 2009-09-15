package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.util.DateHelper;

@Entity
@Table(name = "producttypeschedules")
public class ProductTypeSchedule extends EntityWithOwner implements Saveable {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false)
	private ProductType productType;
	
	@ManyToOne(optional=false)
	private InspectionType inspectionType;
	

	@Column(name="frequency")
	private Long frequencyInDays;
	private boolean autoSchedule = true;
	
	public ProductTypeSchedule() {}
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}



	public Long getFrequency() {
		return frequencyInDays;
	}

	public void setFrequency(Long frequency) {
		this.frequencyInDays = frequency;
	}
	
	public Date getNextDate(Date startDate) {
		return DateHelper.addDaysToDate(startDate, frequencyInDays);
	}
	

	public boolean isAutoSchedule() {
		return autoSchedule;
	}

	public void setAutoSchedule(boolean autoSchedule) {
		this.autoSchedule = autoSchedule;
	}
	
	public boolean isOverride() {
		return !getOwner().isPrimary();
	}
}
