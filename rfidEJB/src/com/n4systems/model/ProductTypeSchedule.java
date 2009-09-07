package com.n4systems.model;

import java.util.Date;

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
	
	// TODO: REMOVE_ME
//	@ManyToOne
//	private Customer customer;

	//in days
	private Long frequency;
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

	// TODO: REMOVE_ME
//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}

	public Long getFrequency() {
		return frequency;
	}

	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}
	
	public Date getNextDate(Date startDate) {
		return DateHelper.addDaysToDate(startDate, frequency);
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof ProductTypeSchedule) {
			return this.equals((ProductTypeSchedule) obj);
		} else {
			return super.equals(obj);
		}

	}

	public boolean equals(ProductTypeSchedule schedule) {
		if (schedule == null)
			return false;
		if (getId() == null)
			return super.equals(schedule);

		return getId().equals(schedule.getId());
	}

	public boolean isAutoSchedule() {
		return autoSchedule;
	}

	public void setAutoSchedule(boolean autoSchedule) {
		this.autoSchedule = autoSchedule;
	}
	
//	public boolean isCustomerOverride() {
//		return customer != null;
//	}
}
