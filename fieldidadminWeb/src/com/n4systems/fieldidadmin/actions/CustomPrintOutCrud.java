package com.n4systems.fieldidadmin.actions;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.model.PrintOut;
import com.n4systems.model.TenantOrganization;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

public class CustomPrintOutCrud extends DefaultPrintOutCrud {
	private static final long serialVersionUID = 1L;

	private List<TenantOrganization> tenants;
		
	
	@SkipValidation
	public String doList() {
		QueryBuilder<PrintOut> queryBuilder = new QueryBuilder<PrintOut>(PrintOut.class);
		queryBuilder.addSimpleWhere("custom", true);
		queryBuilder.addOrder("type");
		queryBuilder.addOrder("name");
		page = persistenceManager.findAllPaged(queryBuilder, getPageNumber(), 20);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		
		printOut.setCustom(true);
		try {
			persistenceManager.save(printOut);
		} catch (Exception e) {
			addActionError("could not save print out.");
			return ERROR;
		}
		return SUCCESS;
	}


	
	public List<TenantOrganization> getTenants() {
		if (tenants == null) {
			QueryBuilder<TenantOrganization> tenantQuery = new QueryBuilder<TenantOrganization>(TenantOrganization.class);
			tenantQuery.addOrder("name");
			tenants = persistenceManager.findAll(tenantQuery);
		}
		return tenants;
	}

	public Long getTenant() {
		return (printOut.getTenant() != null) ? printOut.getTenant().getId() : null;
	}
	
	@RequiredFieldValidator(message="you must select a tenant")
	public void setTenant(Long tenantId) {
		if (tenantId == null) {
			printOut.setTenant(null);
		} else if (printOut.getTenant() == null || !tenantId.equals(printOut.getTenant())) {
			printOut.setTenant(persistenceManager.find(TenantOrganization.class, tenantId));
		}
	}
}
