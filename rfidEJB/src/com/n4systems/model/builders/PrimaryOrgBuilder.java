package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class PrimaryOrgBuilder extends BaseBuilder<PrimaryOrg> {

	private Set<ExtendedFeature> features = new HashSet<ExtendedFeature>();
	private String name;
	
	public static PrimaryOrgBuilder aPrimaryOrg() {
		return new PrimaryOrgBuilder("first_primary_org", null, new HashSet<ExtendedFeature>());
	}
	
	public PrimaryOrgBuilder(String name, Long id, Set<ExtendedFeature> features) {
		super(id);
		this.name = name;
		this.features = features;
	}
	
	public PrimaryOrgBuilder withName(String name) {
		return new PrimaryOrgBuilder(name, id, features);
	}
	
	public PrimaryOrgBuilder withExtendedFeatures(ExtendedFeature...extendedFeatures) {
		return new PrimaryOrgBuilder(name, id, new HashSet<ExtendedFeature>(Arrays.asList(extendedFeatures)));
	}
	
	
	@Override
	public PrimaryOrg build() {
		PrimaryOrg primaryOrg = new PrimaryOrg();
		
		primaryOrg.setName(name);
		primaryOrg.setId(id);
		primaryOrg.setExtendedFeatures(features);
		
		return primaryOrg;
	}

}
