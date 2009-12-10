package com.n4systems.model.builders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.util.DataUnit;

import static com.n4systems.model.builders.TenantBuilder.*;


public class PrimaryOrgBuilder extends BaseBuilder<PrimaryOrg> {

	private final Set<ExtendedFeature> features;
	private final String name;
	private final Tenant tenant;
	private final Long assetLimit;
	private final Long diskSpaceInBytes;
	private final Long externalId;
	private final Long employeeLimit;
	
	
	public static PrimaryOrgBuilder aPrimaryOrg() {
		return new PrimaryOrgBuilder("first_primary_org", null, aTenant().build(), new HashSet<ExtendedFeature>(), 0L, 0L, 0L, new Random().nextLong());
	}
	
	private PrimaryOrgBuilder(String name, Long id, Tenant tenant, Set<ExtendedFeature> features, Long employeeLimit, Long assetLimit, Long diskSpaceInBytes, Long externalId) {
		super(id);
		this.name = name;
		this.features = features;
		this.tenant = tenant;
		this.assetLimit = assetLimit;
		this.diskSpaceInBytes = 0L;
		this.externalId = externalId;
		this.employeeLimit = employeeLimit;
	}
	
	public PrimaryOrgBuilder withName(String name) {
		return new PrimaryOrgBuilder(name, id, tenant, features, employeeLimit, assetLimit, diskSpaceInBytes, externalId);
	}
	
	public PrimaryOrgBuilder withExtendedFeatures(ExtendedFeature...extendedFeatures) {
		return new PrimaryOrgBuilder(name, id, tenant, new HashSet<ExtendedFeature>(Arrays.asList(extendedFeatures)), employeeLimit, assetLimit, diskSpaceInBytes, externalId);
	}
	public PrimaryOrgBuilder withNoExtendedFeatures() {
		return new PrimaryOrgBuilder(name, id, tenant, new HashSet<ExtendedFeature>(), employeeLimit, assetLimit, diskSpaceInBytes, externalId);
	}

	
	public PrimaryOrgBuilder onTenant(Tenant tenant) {
		return new PrimaryOrgBuilder(name, id, tenant, features, employeeLimit, assetLimit, diskSpaceInBytes, externalId);
	}
	
	public PrimaryOrgBuilder withAssetLimit(long assetLimit) {
		return new PrimaryOrgBuilder(name, id, tenant, features, employeeLimit, assetLimit, diskSpaceInBytes, externalId);
	}
	
	public PrimaryOrgBuilder withDiskSpaceLimit(long diskSpace, DataUnit unit) {
		BigDecimal diskSpaceInBytes = DataUnit.convert(new BigDecimal(diskSpace), unit, DataUnit.BYTES);
		return new PrimaryOrgBuilder(name, id, tenant, features, employeeLimit, assetLimit, diskSpaceInBytes.longValue(), externalId);
	}
	
	public PrimaryOrgBuilder withEmployeeLimit(long employeeLimit) {
		return new PrimaryOrgBuilder(name, id, tenant, features, employeeLimit, assetLimit, diskSpaceInBytes, externalId);
	}
	
	
	@Override
	public PrimaryOrg build() {
		PrimaryOrg primaryOrg = new PrimaryOrg();
		
		primaryOrg.setName(name);
		primaryOrg.setId(id);
		primaryOrg.setExtendedFeatures(features);
		primaryOrg.setTenant(tenant);
		
		primaryOrg.setExternalId(externalId);
		
		primaryOrg.setLimits(new TenantLimit(diskSpaceInBytes, assetLimit, employeeLimit, 0));
		return primaryOrg;
	}

	

	

	

	
}
