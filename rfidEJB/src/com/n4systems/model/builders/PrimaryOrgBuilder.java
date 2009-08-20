package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public class PrimaryOrgBuilder extends BaseBuilder<PrimaryOrg> {

	private Set<ExtendedFeature> features = new HashSet<ExtendedFeature>();
	private String name;
	private Tenant tenant;
	
	public static PrimaryOrgBuilder aPrimaryOrg() {
		return new PrimaryOrgBuilder("first_primary_org", null, null, new HashSet<ExtendedFeature>());
	}
	
	public PrimaryOrgBuilder(String name, Long id, Tenant tenant, Set<ExtendedFeature> features) {
		super(id);
		this.name = name;
		this.features = features;
		this.tenant = tenant;
	}
	
	public PrimaryOrgBuilder withName(String name) {
		return new PrimaryOrgBuilder(name, id, tenant, features);
	}
	
	public PrimaryOrgBuilder withExtendedFeatures(ExtendedFeature...extendedFeatures) {
		return new PrimaryOrgBuilder(name, id, tenant, new HashSet<ExtendedFeature>(Arrays.asList(extendedFeatures)));
	}
	
	public PrimaryOrgBuilder onTenant(Tenant tenant) {
		return new PrimaryOrgBuilder(name, id, tenant, features);
	}
	
	
	@Override
	public PrimaryOrg build() {
		PrimaryOrg primaryOrg = new PrimaryOrg();
		
		primaryOrg.setName(name);
		primaryOrg.setId(id);
		primaryOrg.setExtendedFeatures(features);
		primaryOrg.setTenant(tenant);
		
		return primaryOrg;
	}

}
