package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.RandomString;

public class PredefinedLocationBuilder extends BaseBuilder<PredefinedLocation> {

	private final PredefinedLocation parent;
	private final String name;
	private final Tenant tenant;
    private final BaseOrg owner;
    private final Long id;


	public static PredefinedLocationBuilder aPredefinedLocation() {
		return aRootPredefinedLocation();
	}
	
	public static PredefinedLocationBuilder aRootPredefinedLocation() {
		return new PredefinedLocationBuilder(null, RandomString.getString(10), TenantBuilder.aTenant().build(), OrgBuilder.aPrimaryOrg().build(), -1L);
	}
	
	private PredefinedLocationBuilder(PredefinedLocation parent, String name, Tenant tenant, BaseOrg org, Long id) {
		this.parent = parent;
		this.name = name;
		this.tenant = tenant;
        this.owner = org;
        this.id = id;
	}

	public PredefinedLocationBuilder withName(String name) {
		return new PredefinedLocationBuilder(parent, name, tenant, owner, id);
	}

    public PredefinedLocationBuilder withParent(PredefinedLocation parent) {
        return new PredefinedLocationBuilder(parent, name, tenant, owner, id);
    }

    public PredefinedLocationBuilder withOwner(BaseOrg owner) {
        return new PredefinedLocationBuilder(parent, name, tenant, owner, id);
    }

    public PredefinedLocationBuilder withId(Long id) {
        return new PredefinedLocationBuilder(parent, name, tenant, owner, id);
    }

    @Override
	public PredefinedLocation createObject() {
		PredefinedLocation predefinedLocation = new PredefinedLocation(tenant, parent, owner);
		predefinedLocation.setId(getId());
		predefinedLocation.setName(name);
        predefinedLocation.setId(id);
		return predefinedLocation;
	}

}
