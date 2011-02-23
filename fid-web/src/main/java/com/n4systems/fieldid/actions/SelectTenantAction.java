package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.helpers.AbstractActionTenantContextInitializer;
import com.n4systems.fieldid.actions.helpers.TenantContextInitializer;
import com.n4systems.fieldid.actions.helpers.UnbrandedDomainException;
import com.n4systems.fieldid.permissions.NoValidTenantSelectedException;
import com.n4systems.model.Tenant;
import com.n4systems.services.TenantFinder;

public class SelectTenantAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SelectTenantAction.class);
	
	private String companyID;
	private String email;
	private boolean tenantNotFound = false;
	private List<Tenant> tenants = new ArrayList<Tenant>();
	
	public SelectTenantAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doAdd() {
		if (tenantNotFound) { 
			addActionErrorText("error.cannot_find_company_id");
		}
		return SUCCESS;
	}

	public String doCreate() {
		try {
			if (!companyID.equals("")) {
				loadCompany();
				setRedirectUrl(getLoginUrl());
				return REDIRECT_TO_URL;
			} else if (!email.equals("")) {
				loadTenantsByEmail();
				if (!tenants.isEmpty()){
					return SUCCESS;
				}
			}

		} catch (Exception e) {
			logger.debug(getLogLinePrefix() + "Error loading the tenant company", e);
		}

		if (!email.equals("") && tenants.isEmpty()) {
			addActionErrorText("error.company_does_with_email_does_not_exist");
		} else {
			addActionErrorText("error.company_does_not_exists");
		}
		return INPUT;
	}

	private void loadCompany() throws NoValidTenantSelectedException, UnbrandedDomainException {
		TenantContextInitializer intializer = new AbstractActionTenantContextInitializer(this);
		intializer.forceTenantReload().init(companyID);
	}
	
	public String getCompanyID() {
		return companyID;
	}
		
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public boolean isTenantNotFound() {
		return tenantNotFound;
	}

	public void setTenantNotFound(boolean tenantNotFound) {
		this.tenantNotFound = tenantNotFound;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private void loadTenantsByEmail() {
		tenants = TenantFinder.getInstance().findTenantsByEmail(email);
	}
	
	public List<Tenant> getTenants(){
		return tenants;
	}
}
