package com.n4systems.fieldid.actions;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.helpers.AbstractActionTenantContextInitializer;
import com.n4systems.fieldid.actions.helpers.TenantContextInitializer;
import com.n4systems.fieldid.actions.helpers.UnbrandedDomainException;
import com.n4systems.fieldid.permissions.NoValidTenantSelectedException;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class SelectTenantAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SelectTenantAction.class);
	
	private String companyID;
	
	public SelectTenantAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doAdd() {
		return SUCCESS;
	}
	
	public String doCreate() {
		try {
			loadCompany();
			setRedirectUrl(getLoginUrl());
			return REDIRECT_TO_URL;
		} catch (Exception e) {
			logger.debug(getLogLinePrefix() + "Error loading the tenant company", e);
		}
		
		addActionErrorText("error.company_does_not_exists");
		return INPUT;
	}

	private void loadCompany() throws NoValidTenantSelectedException, UnbrandedDomainException {
		TenantContextInitializer intializer = new AbstractActionTenantContextInitializer(this);
		intializer.forceTenantReload().init(companyID);
	}
	
	public String getCompanyID() {
		return companyID;
	}
		
	@RequiredStringValidator(message="", key="error.company_id_required")
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

		
}
