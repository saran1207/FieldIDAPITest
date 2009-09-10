package com.n4systems.fieldid.actions.utils;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;

public class DummyOwnerHolder implements HasOwner {
	
	private BaseOrg owner;

	public DummyOwnerHolder() {
		this(null);
	}

	public DummyOwnerHolder(BaseOrg owner) {
		super();
		this.owner = owner;
	}

	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public Tenant getTenant() {
		throw new NotImplementedException();
	}

	public void setTenant(Tenant tenant) {
		throw new NotImplementedException();
	}
}
