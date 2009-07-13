package com.n4systems.fieldid.permissions;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;

public interface SystemSecurityGuard {

	public abstract String getTenantName();
	public abstract Long getTenantId();
	public abstract TenantOrganization getTenant();
	
	public abstract boolean isExtendedFeatureEnabled(ExtendedFeature feature);
	
	public abstract boolean isPartnerCenterEnabled();
	public abstract boolean isIntegrationEnabled();
	public abstract boolean isComplianceEnabled();
	public abstract boolean isJobSitesEnabled();
	public abstract boolean isProjectsEnabled();
	public abstract boolean isBrandingEnabled();
	public abstract boolean isEmailAlertsEnabled();
}