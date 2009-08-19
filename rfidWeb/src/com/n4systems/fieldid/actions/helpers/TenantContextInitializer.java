package com.n4systems.fieldid.actions.helpers;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.NoValidTenantSelectedException;
import com.n4systems.fieldid.permissions.SessionSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.HostNameParser;

public abstract class TenantContextInitializer {
	private final PersistenceManager persistenceManager;
	private boolean forceTenantReload = false;
	private String unbrandedSubDomain;
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

	public void init(String tenantName) throws NoValidTenantSelectedException, IncorrectTenantDomain {
		setBrandedCompanyId(tenantName);
		init();
	}

	public void init() throws NoValidTenantSelectedException, IncorrectTenantDomain {
		unbrandedSubDomain = ConfigContext.getCurrentContext().getString(ConfigEntry.UNBRANDED_SUBDOMAIN);
		
		try {
			findCurrentTenant();
			loadSecurityGuard();
		} catch (NoValidTenantSelectedException e) {
			forgetSecurityGuard();
			if (storedCookieForTenant()) {
				throw new IncorrectTenantDomain();
			} else {
				throw e;
			}
		}
	}
	public void destroyContext() {
		forgetSecurityGuard();
	}

	private boolean storedCookieForTenant() {
		return !companySpecifiedInURI() && CookieFactory.findCookieValue("companyID", getRequest()) != null;
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
		return forceTenantReload || getSecurityGuard() == null
				|| !brandedCompanyId.equals(getSecurityGuard().getTenantName());
	}

	private void setUpSecurityGuard() throws NoValidTenantSelectedException {
		Tenant tenant = loadTenant();
		try {
			resetSecurityGuard(tenant);
		} catch (Exception e) {
			throw new NoValidTenantSelectedException("tenant does not exist " + brandedCompanyId);
		}
	}

	private Tenant loadTenant() {
		Tenant tenant = persistenceManager.findByName(Tenant.class, brandedCompanyId);
		return tenant;
	}

	

	private void findCurrentTenant() {
		if (brandedCompanyId == null) {
			if (companySpecifiedInURI()) {
				setBrandedCompanyId(getCompanyNameInURI());
			} 
		}
	}

	private boolean companySpecifiedInURI() {
		HostNameParser hostParser = HostNameParser.create(getRequestURI());
		
		return (hostParser.hasSubDomain() && !hostParser.getFirstSubDomain().equals(unbrandedSubDomain));
	}

	private String getCompanyNameInURI() {
		return HostNameParser.create(getRequestURI()).getFirstSubDomain();
	}

	private SystemSecurityGuard getSecurityGuard() {
		if (securityGuard == null) {
			securityGuard = (SystemSecurityGuard) getSession().get(Constants.SECURITY_GUARD);
		}
		return securityGuard;
	}

	private void resetSecurityGuard(Tenant tenant) {
		forgetSecurityGuard();
		securityGuard = new SessionSecurityGuard(tenant);
		rememberSecurityGuard(securityGuard);
	}

	@SuppressWarnings("unchecked")
	private void rememberSecurityGuard(SystemSecurityGuard securityGuard) {
		getSession().put(Constants.SECURITY_GUARD, securityGuard);
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
	
	protected URI getRequestURI() {
		return URI.create(getRequest().getRequestURL().toString());
	}

}
