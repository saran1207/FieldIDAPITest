package com.n4systems.fieldid.permissions;

import java.io.Serializable;

import com.n4systems.exceptions.InvalidArgumentException;
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
	
	public SerializableSecurityGuard(Tenant tenant, PrimaryOrg primaryOrg) {
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

	public boolean isJobSitesEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.JobSites);
	}
	
	public boolean isAssignedToEnabled() {
		return primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo);
	}

	public boolean isPlansAndPricingAvailable() {
		return primaryOrg.isPlansAndPricingAvailable();
	}
	
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
	
}
