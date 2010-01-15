package com.n4systems.fieldid.permissions;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;

public class SessionSecurityGuard implements SystemSecurityGuard {
	private static final long serialVersionUID = 1L;
	
	private final Tenant tenant;
	private final PrimaryOrg primaryOrg;
	
	public SessionSecurityGuard(Tenant tenant) {
		this(tenant, TenantCache.getInstance().findPrimaryOrg(tenant.getId()));
	}
	
	public SessionSecurityGuard(Tenant tenant, PrimaryOrg primaryOrg) {
		if (tenant == null) {
			throw new InvalidArgumentException("Tenant cannot be null");
		}
		if (primaryOrg == null) {
			throw new InvalidArgumentException("Could not find PrimaryOrg for Tenant: " + tenant.toString());
		}
		this.tenant = tenant;
		this.primaryOrg = primaryOrg;
	}
	
	public boolean isExtendedFeatureEnabled(ExtendedFeature feature) {
		return primaryOrg.hasExtendedFeature(feature);
	}

	public boolean isBrandingEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.Branding);
	}

	public boolean isIntegrationEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.Integration);
	}

	public boolean isPartnerCenterEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.PartnerCenter);
	}

	public boolean isProjectsEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.Projects);
	}
	
	public boolean isEmailAlertsEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.EmailAlerts);
	}

	public Tenant getTenant() {
		return tenant;
	}
	
	public Long getTenantId() {
		return tenant.getId();
	}

	public String getTenantName() {
		return tenant.getName();
	}
	
	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public boolean isAllowIntegrationEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.AllowIntegration);
	}

	public boolean isCustomCertEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.CustomCert);
	}

	public boolean isDedicatedProgramManagerEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.DedicatedProgramManager);
	}

	public boolean isMultiLocationEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.MultiLocation);
	}

	public boolean isJobSitesEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.JobSites);
	}

	public boolean isUnlimitedLinkedAssetsEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.UnlimitedLinkedAssets);
	}

	public boolean isPlansAndPricingAvailable() {
		if (!isPartnerCenterEnabled() || primaryOrg.isPlansAndPricingAvailable()) {
			return true;
		}
		return false;
	}
	
	
	
	
}
