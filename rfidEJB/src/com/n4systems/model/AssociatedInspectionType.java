package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name="associatedinspectiontypes")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class AssociatedInspectionType extends EntityWithTenant implements Saveable {
	private static final long serialVersionUID = 1L;

	
	@ManyToOne
	private InspectionType inspectionType;
	
	
	@ManyToOne
	private ProductType productType;
	
	
	public AssociatedInspectionType() {
		super();
	}
	
	public AssociatedInspectionType(InspectionType inspectionType, ProductType productType) {
		super(inspectionType.getTenant());
		this.inspectionType = inspectionType;
		this.productType = productType;
	}
	

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

}
