package com.n4systems.services;

import rfid.ejb.entity.CommentTempBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionType;
import com.n4systems.model.JobSite;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.StateSet;
import com.n4systems.model.UnitOfMeasure;

public enum SetupDataGroup {
	/*
	 * NOTE - the group classes must implement the HasTenant or CrossTenantEntity interface.
	 * On save, entities implementing HasTenant will notify the SetupDataLastModUpdateService
	 * for their group and specific tenant.  entities implementing CrossTenantEntity will 
	 * update for their group and all tenants.
	 * If the class is not a subclass of HasTenant or CrossTenantEntity, 
	 * SetupDataUpdateEventListener will throw an exception!
	 */
	PRODUCT_TYPE (
			ProductType.class,
			ProductStatusBean.class,
			ProductTypeGroup.class,
			AssociatedInspectionType.class,
			ProductTypeSchedule.class
	),
	INSPECTION_TYPE (
			InspectionType.class,
			StateSet.class,
			InspectionBook.class,
			CommentTempBean.class,
			UnitOfMeasure.class
	),
	AUTO_ATTRIBUTES (
			AutoAttributeCriteria.class,
			AutoAttributeDefinition.class
	),
	OWNERS (
			Customer.class,
			Division.class,
			JobSite.class
	),
	JOBS (
			Project.class
	);
	
	private final Class<?>[] groupClasses;
	
	SetupDataGroup(Class<?> ... groupClasses) {
		this.groupClasses = groupClasses;
	}

	public Class<?>[] getGroupClasses() {
		return groupClasses;
	}
	
}