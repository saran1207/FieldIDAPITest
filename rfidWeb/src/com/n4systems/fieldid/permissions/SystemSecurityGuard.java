package com.n4systems.fieldid.permissions;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public interface SystemSecurityGuard {

	public String getTenantName();
	public Long getTenantId();
	public Tenant getTenant();
	public PrimaryOrg getPrimaryOrg();
	public boolean isExtendedFeatureEnabled(ExtendedFeature feature);
	public boolean isPartnerCenterEnabled();
	public boolean isIntegrationEnabled();
	public boolean isComplianceEnabled();
	public boolean isJobSitesEnabled();
	public boolean isProjectsEnabled();
	public boolean isBrandingEnabled();
	public boolean isEmailAlertsEnabled();
	public boolean isCustomCertEnabled(); 
	public boolean isDedicatedProgramManagerEnabled(); 
	public boolean isMultiLocationEnabled(); 
	public boolean isAllowIntegrationEnabled();
}