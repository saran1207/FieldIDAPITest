package com.n4systems.model.inspectiontype;

import javax.persistence.EntityManager;

import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssociatedInspectionTypeExistsLoader extends SecurityFilteredLoader<Boolean> {
	private InspectionType inspectionType;
	private ProductType productType;
	
	
	public AssociatedInspectionTypeExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Boolean> builder = new QueryBuilder<Boolean>(AssociatedInspectionType.class, filter);
		builder.addWhere(WhereClauseFactory.create("inspectionType", inspectionType));
		builder.addWhere(WhereClauseFactory.create("productType", productType));
		
		boolean exists = builder.entityExists(em);
		return exists;
	}

	public AssociatedInspectionTypeExistsLoader setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

	public AssociatedInspectionTypeExistsLoader setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

}
