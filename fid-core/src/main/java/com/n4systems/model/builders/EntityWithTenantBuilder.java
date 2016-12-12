package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;

import java.util.Date;

abstract public class EntityWithTenantBuilder<K extends EntityWithTenant> extends AbstractEntityBuilder<K> {
	protected Tenant tenant;
	
	public EntityWithTenantBuilder(Long id, Date created, Date modified, User modifiedBy, Tenant tenant) {
		super(id, created, modified, modifiedBy);
        this.tenant = tenant;
	}

    public EntityWithTenantBuilder() {
		this(null);
    }

    public EntityWithTenantBuilder(Tenant tenant) {
		this(generateNewId(), new Date(), new Date(), null, tenant);
    }
	
	@Override
	protected K assignAbstractFields(K model) {
		super.assignAbstractFields(model);
		model.setTenant(tenant);
		return model;
	}

    public EntityWithTenantBuilder<K> setTenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public Tenant getTenant() {
        return tenant;
    }

}
