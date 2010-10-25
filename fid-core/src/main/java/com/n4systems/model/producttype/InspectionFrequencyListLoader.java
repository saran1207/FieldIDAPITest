package com.n4systems.model.producttype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionFrequencyListLoader extends ListLoader<AssetTypeSchedule> {
	
	private Long assetTypeId;
	private Long inspectionTypeId;
	
	
	public InspectionFrequencyListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<AssetTypeSchedule> load(EntityManager em, SecurityFilter filter) {
		if (inspectionTypeId == null && assetTypeId == null) {
			throw new InvalidArgumentException("you must choose an asset type or an inspection type or both.  You didn't select either");
		}
		
		return createQuery(em, filter);
	}

	private List<AssetTypeSchedule> createQuery(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetTypeSchedule> builder = new QueryBuilder<AssetTypeSchedule>(AssetTypeSchedule.class, filter);
		
		if (inspectionTypeId != null) { 
			builder.addSimpleWhere("inspectionType.id", inspectionTypeId); 
		}
		
		if (assetTypeId != null) {
			builder.addSimpleWhere("assetType.id", assetTypeId);
		}
		
		return builder.getResultList(em);
	}

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public InspectionFrequencyListLoader setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
		return this;
	}

	public Long getInspectionTypeId() {
		return inspectionTypeId;
	}

	public InspectionFrequencyListLoader setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
		return this;
	}
	
}
