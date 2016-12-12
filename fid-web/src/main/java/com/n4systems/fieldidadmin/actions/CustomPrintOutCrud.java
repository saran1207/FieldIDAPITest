package com.n4systems.fieldidadmin.actions;

import com.n4systems.model.PrintOut;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

public class CustomPrintOutCrud extends DefaultPrintOutCrud {
	private static final long serialVersionUID = 1L;

	private List<Tenant> tenants;
		
	
	@SkipValidation
	public String doList() {
		QueryBuilder<PrintOut> queryBuilder = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
		queryBuilder.addSimpleWhere("custom", true);
		queryBuilder.addOrder("type");
		queryBuilder.addOrder("name");
		page = persistenceEJBContainer.findAllPaged(queryBuilder, getPageNumber(), 20);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		
		printOut.setCustom(true);
		try {
			persistenceEJBContainer.save(printOut);
		} catch (Exception e) {
			addActionError("could not save print out.");
			return ERROR;
		}
		return SUCCESS;
	}


	
	public List<Tenant> getTenants() {
		if (tenants == null) {
			QueryBuilder<Tenant> tenantQuery = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
			tenantQuery.addOrder("name");
			tenants = persistenceEJBContainer.findAll(tenantQuery);
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
			printOut.setTenant(persistenceEJBContainer.find(Tenant.class, tenantId));
		}
	}
}
