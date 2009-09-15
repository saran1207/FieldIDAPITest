package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductTypeScheduleLoader extends SecurityFilteredLoader<ProductTypeSchedule> {
	private Long productTypeId;
	private Long inspectionTypeId;
	private Long ownerId;
	
	public ProductTypeScheduleLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductTypeSchedule load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductTypeSchedule> builder = null;
		
		
		builder = getCustomerOverrideBuilder(filter);
	

				
		ProductTypeSchedule schedule = builder.getSingleResult(em);
		return schedule;
	}

	private QueryBuilder<ProductTypeSchedule> getBaseBuilder(SecurityFilter filter) {
		QueryBuilder<ProductTypeSchedule> builder = new QueryBuilder<ProductTypeSchedule>(ProductTypeSchedule.class, filter);
		builder.addSimpleWhere("inspectionType.id", inspectionTypeId);
		builder.addSimpleWhere("productType.id", productTypeId);
		return builder;
	}
	
	private QueryBuilder<ProductTypeSchedule> getCustomerOverrideBuilder(SecurityFilter filter) {
		return getBaseBuilder(filter).addSimpleWhere("owner.id", ownerId);
	}
	
	
	public ProductTypeScheduleLoader setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
		return this;
	}
	
	public ProductTypeScheduleLoader setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
		return this;
	}

	public ProductTypeScheduleLoader setOwner(Long ownerId) {
		this.ownerId = ownerId;
		return this;
	}

}