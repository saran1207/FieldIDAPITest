package com.n4systems.model.api;

import com.n4systems.model.AbstractEntityCleaner;
import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;

public abstract class EntityWithTenantCleaner<T extends EntityWithTenant> extends AbstractEntityCleaner<T> {
	private final Tenant newTenant;

	public EntityWithTenantCleaner(Tenant newTenant) {
		super();
		this.newTenant = newTenant;
	}

	@Override
	public void clean(T obj) {
		super.clean(obj);
		obj.setTenant(newTenant);
	}
	
}
