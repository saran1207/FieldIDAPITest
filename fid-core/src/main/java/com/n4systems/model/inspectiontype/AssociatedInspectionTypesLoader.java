package com.n4systems.model.inspectiontype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssociatedInspectionTypesLoader extends ListLoader<AssociatedInspectionType> {

	private ProductType productType;
	private InspectionType inspectionType;
	
	public AssociatedInspectionTypesLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssociatedInspectionType> load(EntityManager em, SecurityFilter filter) {
		if (inspectionType == null && productType == null) {
			throw new InvalidArgumentException("You must look up by product type of inspection type.");
		}
 		
		QueryBuilder<AssociatedInspectionType> query = prepareQuery();
		
		return query.getResultList(em);
	}

	private QueryBuilder<AssociatedInspectionType> prepareQuery() {
		QueryBuilder<AssociatedInspectionType> query = new QueryBuilder<AssociatedInspectionType>(AssociatedInspectionType.class, new OpenSecurityFilter());
		if (productType != null) {
			query.addSimpleWhere("productType", productType);
		}
		
		if (inspectionType != null) {
			query.addSimpleWhere("inspectionType", inspectionType);
		}
		
		query.addOrder("inspectionType.name");
		return query;
	}

	public AssociatedInspectionTypesLoader setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	public AssociatedInspectionTypesLoader setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

	

}
