package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ProductTypeScheduleLoader extends SecurityFilteredLoader<ProductTypeSchedule> {
	private Long productTypeId;
	private Long inspectionTypeId;
	private Long customerId;
	
	public ProductTypeScheduleLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductTypeSchedule load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductTypeSchedule> builder = null;
		
		if (customerId != null) {
			builder = getCustomerOverrideBuilder(filter);
		}

		// if we're not looking for customer overrides, or no customer override exists, fall back to the default schedule
		if (builder == null || !builder.entityExists(em)) {
			builder = getDefaultScheduleBuilder(filter);
		}
				
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
		return getBaseBuilder(filter).addSimpleWhere("owner.customer_id", customerId);
	}
	
	private QueryBuilder<ProductTypeSchedule> getDefaultScheduleBuilder(SecurityFilter filter) {
		return getBaseBuilder(filter).addWhere(new WhereParameter<Object>(Comparator.NULL, "owner.customer_id"));
	}	
	
	public ProductTypeScheduleLoader setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
		return this;
	}
	
	public ProductTypeScheduleLoader setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
		return this;
	}

	public ProductTypeScheduleLoader setCustomerId(Long customerId) {
		this.customerId = customerId;
		return this;
	}

}