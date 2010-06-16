package com.n4systems.fieldid.permissions;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;

public class AlwaysOffSystemSecurityGuardTestDouble implements SystemSecurityGuard {
	public PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().build();
	
	

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}


	public Tenant getTenant() {
		return primaryOrg.getTenant();
	}


	public Long getTenantId() {
		return primaryOrg.getTenant().getId();
	}


	public String getTenantName() {
		return primaryOrg.getTenant().getName();
	}


	public boolean isAllowIntegrationEnabled() {
		return false;
	}


	public boolean isAssignedToEnabled() {
		return false;
	}


	public boolean isBrandingEnabled() {
		return false;
	}


	public boolean isCustomCertEnabled() {
		return false;
	}


	public boolean isDedicatedProgramManagerEnabled() {
		return false;
	}


	public boolean isEmailAlertsEnabled() {
		return false;
	}


	public boolean isExtendedFeatureEnabled(ExtendedFeature feature) {
		return false;
	}


	public boolean isIntegrationEnabled() {
		return false;
	}


	public boolean isJobSitesEnabled() {
		return false;
	}


	public boolean isMultiLocationEnabled() {
		return false;
	}


	public boolean isPartnerCenterEnabled() {
		return false;
	}


	public boolean isPlansAndPricingAvailable() {
		return false;
	}


	public boolean isProjectsEnabled() {
		return false;
	}


	public boolean isUnlimitedLinkedAssetsEnabled() {
		return false;
	}

}
