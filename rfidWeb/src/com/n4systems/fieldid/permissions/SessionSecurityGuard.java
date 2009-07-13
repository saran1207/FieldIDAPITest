package com.n4systems.fieldid.permissions;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;

public class SessionSecurityGuard implements SystemSecurityGuard {

	private static final long serialVersionUID = 1L;
	
	private final TenantOrganization tenant;
	

	public SessionSecurityGuard(TenantOrganization tenant) {
		super();
		if (tenant == null) {
			throw new InvalidArgumentException("tenant must not be null");
		}
		
		this.tenant = tenant;
	}

	public TenantOrganization getTenant() {
		return tenant;
	}
	
	public boolean isExtendedFeatureEnabled(ExtendedFeature feature) {
		return tenant.hasExtendedFeature(feature);
	}

	public boolean isBrandingEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.Branding);
	}

	public boolean isComplianceEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.Compliance);
	}

	public boolean isIntegrationEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.Integration);
	}
	
	public boolean isJobSitesEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.JobSites);
	}

	public boolean isPartnerCenterEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.PartnerCenter);
	}

	public boolean isProjectsEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.Projects);
	}
	
	
	public boolean isEmailAlertsEnabled() {
		return tenant.hasExtendedFeature(ExtendedFeature.EmailAlerts);
	}
	

	public Long getTenantId() {
		return tenant.getId();
	}

	public String getTenantName() {
		return tenant.getName();
	}
		
}
