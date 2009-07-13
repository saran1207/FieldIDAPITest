package com.n4systems.fieldid.actions.helpers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.NoValidTenantSelectedException;
import com.n4systems.fieldid.permissions.SessionSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.model.TenantOrganization;

public abstract class TenantContextInitializer {

	private static final String COMPANY_ID = "companyID";
	
	private final PersistenceManager persistenceManager;
	private boolean forceTenantReload = false;
	
	private String brandedCompanyId;
	private SystemSecurityGuard securityGuard;

	public TenantContextInitializer(PersistenceManager persistenceManager) {
		super();
		this.persistenceManager = persistenceManager;
	}
	
	public TenantContextInitializer forceTenantReload() {
		this.forceTenantReload = true;
		return this;
	}
	
	public void init(String tenantName) throws NoValidTenantSelectedException {
		setBrandedCompanyId(tenantName);
		init();
	}
	
	
	public void init() throws NoValidTenantSelectedException {
		try {
			findCurrentTenant();
			loadSecurityGuard();
		} catch (NoValidTenantSelectedException e) {
			forgetSecurityGuard();
			throw e;
		}
	}

	public void refreshSecurityGaurd() throws NoValidTenantSelectedException {
		if (getSecurityGuard() == null) {
			throw new NoValidTenantSelectedException();
		}
		
		setBrandedCompanyId(getSecurityGuard().getTenantName());
		setUpSecurityGuard();
	}
	
	
	private void loadSecurityGuard() throws NoValidTenantSelectedException {
		if (brandedCompanyId == null) {
			throw new NoValidTenantSelectedException("there is not company id given.");
		}
		if (shouldSecurityGuardBeReloaded()) {
			setUpSecurityGuard();
		}
	}

	private boolean shouldSecurityGuardBeReloaded() {
		return forceTenantReload || getSecurityGuard() == null || !brandedCompanyId.equals(getSecurityGuard().getTenantName());
	}


	private void setUpSecurityGuard() throws NoValidTenantSelectedException {
		TenantOrganization tenant = loadTenant();
		try {
			resetSecurityGuard(tenant);
		} catch (Exception e) {
			throw new NoValidTenantSelectedException("tenant does not exist " + brandedCompanyId);
		}
	}

	private TenantOrganization loadTenant() {
		TenantOrganization tenant = persistenceManager.findByName(TenantOrganization.class, brandedCompanyId);
		return tenant;
	}

	private void setCompanyCookie(String tenantName) {
		tenantName = (tenantName != null) ? tenantName.trim().toLowerCase() : null;
		getResponse().addCookie(CookieFactory.createCookie(COMPANY_ID, tenantName, CookieFactory.TTL_DEFAULT));
	}


	

	private void findCurrentTenant() {
		if (brandedCompanyId == null) {
			if (companySpecifiedInRequest()) { 
				setBrandedCompanyId(getCompanyNameInRequest());
			} else if(getSecurityGuard() != null) {
				setBrandedCompanyId(getSecurityGuard().getTenantName());
			} else {
				setBrandedCompanyId(CookieFactory.findCookieValue(COMPANY_ID, getRequest()));
			}
		}
	}

	

	private boolean companySpecifiedInRequest() {
		return getCompanyNameInRequest() != null;
	}

	private String getCompanyNameInRequest() {
		return getRequest().getParameter(COMPANY_ID);
	}
	

	private SystemSecurityGuard getSecurityGuard() {
		if (securityGuard == null) {
			securityGuard = (SystemSecurityGuard)getSession().get(Constants.SECURITY_GUARD);
		}
		return securityGuard;
	}

	private void resetSecurityGuard(TenantOrganization tenant) {
		forgetSecurityGuard();
		securityGuard = new SessionSecurityGuard(tenant);
		rememberSecurityGuard(securityGuard);
	}

	@SuppressWarnings("unchecked")
	private void rememberSecurityGuard(SystemSecurityGuard securityGuard) {
		getSession().put(Constants.SECURITY_GUARD, securityGuard);
		setCompanyCookie(securityGuard.getTenantName());
		
	}

	private void forgetSecurityGuard() {
		getSession().remove(Constants.SECURITY_GUARD);
	}
	
	@SuppressWarnings("unchecked")
	protected abstract Map getSession();
	protected abstract HttpServletRequest getRequest();
	protected abstract HttpServletResponse getResponse();
	

	public void setBrandedCompanyId(String brandedCompanyId) {
		if (brandedCompanyId != null && brandedCompanyId.trim().length() > 0) { 
			this.brandedCompanyId = brandedCompanyId.trim();
		} else {
			this.brandedCompanyId = null;
		}
	}
	

}
