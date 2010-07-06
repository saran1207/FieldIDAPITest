package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.util.RandomString;

public class PredefinedLocationBuilder extends BaseBuilder<PredefinedLocation> {

	
	private final PredefinedLocation parent;
	private final String name;
	private final Tenant tenant;


	public static PredefinedLocationBuilder aPredefinedLocation() {
		return aRootPredefinedLocation();
	}
	
	public static PredefinedLocationBuilder aRootPredefinedLocation() {
		return new PredefinedLocationBuilder(null, RandomString.getString(10), TenantBuilder.aTenant().build());
	}
	
	private PredefinedLocationBuilder(PredefinedLocation parent, String name, Tenant tenant) {
		this.parent = parent;
		this.name = name;
		this.tenant = tenant;
	}

	
	public PredefinedLocationBuilder withName(String name) {
		return new PredefinedLocationBuilder(parent, name, tenant);
	}
	
	public PredefinedLocationBuilder withParent(PredefinedLocation parent) {
		return new PredefinedLocationBuilder(parent, name, tenant);
	}

	
	@Override
	public PredefinedLocation build() {
		PredefinedLocation predefinedLocation = new PredefinedLocation(tenant, parent);
		predefinedLocation.setName(name);
		return predefinedLocation;
	}

}
