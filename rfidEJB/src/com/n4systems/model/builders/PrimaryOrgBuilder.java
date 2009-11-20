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

	private Set<ExtendedFeature> features = new HashSet<ExtendedFeature>();
	private String name;
	private Tenant tenant;
	private Long assetLimit;
	private Long diskSpaceInBytes;
	private Long externalId;
	
	public static PrimaryOrgBuilder aPrimaryOrg() {
		return new PrimaryOrgBuilder("first_primary_org", null, aTenant().build(), new HashSet<ExtendedFeature>(), 0L, 0L, new Random().nextLong());
	}
	
	public PrimaryOrgBuilder(String name, Long id, Tenant tenant, Set<ExtendedFeature> features, Long assetLimit, Long diskSpaceInBytes, Long externalId) {
		super(id);
		this.name = name;
		this.features = features;
		this.tenant = tenant;
		this.assetLimit = assetLimit;
		this.diskSpaceInBytes = 0L;
		this.externalId = externalId;
	}
	
	public PrimaryOrgBuilder withName(String name) {
		return new PrimaryOrgBuilder(name, id, tenant, features, assetLimit, diskSpaceInBytes, externalId);
	}
	
	public PrimaryOrgBuilder withExtendedFeatures(ExtendedFeature...extendedFeatures) {
		return new PrimaryOrgBuilder(name, id, tenant, new HashSet<ExtendedFeature>(Arrays.asList(extendedFeatures)), assetLimit, diskSpaceInBytes, externalId);
	}
	public PrimaryOrgBuilder withNoExtendedFeatures() {
		return new PrimaryOrgBuilder(name, id, tenant, new HashSet<ExtendedFeature>(), assetLimit, diskSpaceInBytes, externalId);
	}

	
	public PrimaryOrgBuilder onTenant(Tenant tenant) {
		return new PrimaryOrgBuilder(name, id, tenant, features, assetLimit, diskSpaceInBytes, externalId);
	}
	
	public PrimaryOrgBuilder withAssetLimit(long assetLimit) {
		return new PrimaryOrgBuilder(name, id, tenant, features, assetLimit, diskSpaceInBytes, externalId);
	}
	
	public PrimaryOrgBuilder withDiskSpaceLimit(long diskSpace, DataUnit unit) {
		BigDecimal diskSpaceInBytes = DataUnit.convert(new BigDecimal(diskSpace), unit, DataUnit.BYTES);
		return new PrimaryOrgBuilder(name, id, tenant, features, assetLimit, diskSpaceInBytes.longValue(), externalId);
	}
	
	
	@Override
	public PrimaryOrg build() {
		PrimaryOrg primaryOrg = new PrimaryOrg();
		
		primaryOrg.setName(name);
		primaryOrg.setId(id);
		primaryOrg.setExtendedFeatures(features);
		primaryOrg.setTenant(tenant);
		
		primaryOrg.setExternalId(externalId);
		
		primaryOrg.setLimits(new TenantLimit(0, assetLimit, 0, 0));
		return primaryOrg;
	}

	

	

	
}
