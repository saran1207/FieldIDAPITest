package com.n4systems.model.inspectiontype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssociatedInspectionTypesLoader extends ListLoader<AssociatedInspectionType> {

	private AssetType assetType;
	private InspectionType inspectionType;
	
	public AssociatedInspectionTypesLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssociatedInspectionType> load(EntityManager em, SecurityFilter filter) {
		if (inspectionType == null && assetType == null) {
			throw new InvalidArgumentException("You must look up by asset type of inspection type.");
		}
 		
		QueryBuilder<AssociatedInspectionType> query = prepareQuery();
		
		return query.getResultList(em);
	}

	private QueryBuilder<AssociatedInspectionType> prepareQuery() {
		QueryBuilder<AssociatedInspectionType> query = new QueryBuilder<AssociatedInspectionType>(AssociatedInspectionType.class, new OpenSecurityFilter());
		if (assetType != null) {
			query.addSimpleWhere("assetType", assetType);
		}
		
		if (inspectionType != null) {
			query.addSimpleWhere("inspectionType", inspectionType);
		}
		
		query.addOrder("inspectionType.name");
		return query;
	}

	public AssociatedInspectionTypesLoader setAssetType(AssetType assetType) {
		this.assetType = assetType;
		return this;
	}

	public AssociatedInspectionTypesLoader setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

	

}
