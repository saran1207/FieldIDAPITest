package com.n4systems.model.builders;

import java.util.Date;


import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;

abstract public class EntityWithTenantBuilder<K extends EntityWithTenant> extends AbstractEntityBuilder<K> {
	protected Tenant tenant;
	
	public EntityWithTenantBuilder(Long id, Date created, Date modified, User modifiedBy, Tenant tenant) {
		super(id, created, modified, modifiedBy);
		this.tenant = tenant;
	}
	
	public EntityWithTenantBuilder() {
		this(generateNewId(), new Date(), new Date(), UserBuilder.aUser().build(), TenantBuilder.aTenant().build());
		
		// make sure the modified by user's tenant is in sync with ours
		modifiedBy.setTenant(tenant);
	}

	@Override
	protected K assignAbstractFields(K model) {
		super.assignAbstractFields(model);
		model.setTenant(tenant);
		return model;
	}
	
}
