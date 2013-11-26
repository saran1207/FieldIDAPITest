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


	public boolean isReadOnlyUsersEnabled() {
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


	@Override
	public boolean isAdvancedLocationEnabled() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isProofTestIntegrationEnabled() {
		return false;
	}


	@Override
	public boolean isManufacturerCertificateEnabled() {
		return false;
	}

	@Override
	public boolean isOrderDetailsEnabled() {
		return false;
	}

    @Override
    public boolean isLotoProceduresEnabled() {
        return false;
    }

    @Override
    public boolean isGlobalSearchEnabled() {
        return false;
    }

    @Override
    public boolean isCriteriaTrendsEnabled() {
        return false;
    }

    @Override
    public boolean isUserGroupFilteringEnabled() {
        return false;
    }

    @Override
    public boolean isAdvancedEventSearchEnabled() {
        return false;
    }
}
