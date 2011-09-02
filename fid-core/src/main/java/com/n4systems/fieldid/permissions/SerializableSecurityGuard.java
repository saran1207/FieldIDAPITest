package com.n4systems.fieldid.permissions;

import static com.google.common.base.Preconditions.*;

import java.io.Serializable;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantFinder;

public class SerializableSecurityGuard implements SystemSecurityGuard, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Tenant tenant;
	private final PrimaryOrg primaryOrg;
	
	public SerializableSecurityGuard(Tenant tenant) {
		this(tenant, TenantFinder.getInstance().findPrimaryOrg(tenant.getId()));
	}
	
	@Deprecated // should only be called directly in tests.  
	public SerializableSecurityGuard(Tenant tenant, PrimaryOrg primaryOrg) {
		checkNotNull(tenant, "Tenant cannot be null");
		checkNotNull(primaryOrg, "Could not find PrimaryOrg for Tenant: " + tenant.toString());
		this.tenant = tenant;
		this.primaryOrg = primaryOrg;
	}
	
	@Override
	public boolean isExtendedFeatureEnabled(ExtendedFeature feature) {
		return primaryOrg.hasExtendedFeature(feature);
	}

	@Override
	public boolean isBrandingEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.Branding);
	}

	@Override
	public boolean isIntegrationEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.Integration);
	}

	@Override
	public boolean isProjectsEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.Projects);
	}
	
	@Override
	public boolean isEmailAlertsEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.EmailAlerts);
	}

	@Override
	public Tenant getTenant() {
		return tenant;
	}
	
	@Override
	public Long getTenantId() {
		return tenant.getId();
	}

	@Override
	public String getTenantName() {
		return tenant.getName();
	}
	
	@Override
	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	@Override
	public boolean isJobSitesEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.JobSites);
	}
	
	@Override
	public boolean isAssignedToEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo);
	}

	@Override
	public boolean isPlansAndPricingAvailable() {
		return primaryOrg.isPlansAndPricingAvailable();
	}
	
	@Override
	public boolean isAdvancedLocationEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.AdvancedLocation);
	}

	@Override
	public boolean isProofTestIntegrationEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.ProofTestIntegration);
	}

	@Override
	public boolean isManufacturerCertificateEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);
	}

	@Override
	public boolean isOrderDetailsEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.OrderDetails);
	}
	
}
