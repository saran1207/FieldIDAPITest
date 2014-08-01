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

    @Override
    public boolean isInspectionsEnabled() {
        return false;
    }

    @Override
    public boolean isLotoEnabled() {
        return false;
    }

    @Override
	public boolean isAssignedToEnabled() {
		return false;
	}

    @Override
	public boolean isBrandingEnabled() {
		return false;
	}

    @Override
	public boolean isEmailAlertsEnabled() {
		return false;
	}

    @Override
	public boolean isExtendedFeatureEnabled(ExtendedFeature feature) {
		return false;
	}

    @Override
	public boolean isIntegrationEnabled() {
		return false;
	}

    @Override
	public boolean isJobSitesEnabled() {
		return false;
	}

    @Override
	public boolean isPlansAndPricingAvailable() {
		return false;
	}

    @Override
	public boolean isProjectsEnabled() {
		return false;
	}

	@Override
	public boolean isAdvancedLocationEnabled() {
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

    @Override
    public boolean isLotoProceduresEnabled() {
        return false;
    }
}
