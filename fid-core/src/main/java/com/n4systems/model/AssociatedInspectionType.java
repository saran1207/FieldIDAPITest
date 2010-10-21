package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name="associatedinspectiontypes")
public class AssociatedInspectionType extends EntityWithTenant implements Saveable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private InspectionType inspectionType;
	
	@ManyToOne
    @JoinColumn(name="producttype_id")
	private AssetType assetType;
	
	public AssociatedInspectionType() {
	}
	
	public AssociatedInspectionType(InspectionType inspectionType, AssetType assetType) {
		super(inspectionType.getTenant());
		this.inspectionType = inspectionType;
		this.assetType = assetType;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}

}
