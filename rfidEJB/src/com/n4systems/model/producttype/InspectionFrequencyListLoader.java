package com.n4systems.model.producttype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionFrequencyListLoader extends ListLoader<ProductTypeSchedule> {
	
	private Long productTypeId;
	private Long inspectionTypeId;
	
	
	public InspectionFrequencyListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<ProductTypeSchedule> load(EntityManager em, SecurityFilter filter) {
		if (inspectionTypeId == null && productTypeId == null) {
			throw new InvalidArgumentException("you must choose a product type or an inspection type or both.  You didn't select either");
		}
		
		return createQuery(em, filter);
	}

	private List<ProductTypeSchedule> createQuery(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductTypeSchedule> builder = new QueryBuilder<ProductTypeSchedule>(ProductTypeSchedule.class, filter.prepareFor(ProductTypeSchedule.class));
		
		if (inspectionTypeId != null) { 
			builder.addSimpleWhere("inspectionType.id", inspectionTypeId); 
		}
		
		if (productTypeId != null) {
			builder.addSimpleWhere("productType.id", productTypeId);
		}
		
		return builder.getResultList(em);
	}

	public Long getProductTypeId() {
		return productTypeId;
	}

	public InspectionFrequencyListLoader setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
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
