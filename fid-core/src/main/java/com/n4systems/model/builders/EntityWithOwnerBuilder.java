package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithOwner;

public abstract class EntityWithOwnerBuilder<K extends EntityWithOwner> extends EntityWithTenantBuilder<K> {

    protected BaseOrg owner;

    public EntityWithOwnerBuilder() {
        this(null, null);
    }

    public EntityWithOwnerBuilder(Tenant tenant, BaseOrg owner) {
        super(tenant);
        this.owner = owner;
    }

    @Override
    protected K assignAbstractFields(K model) {
        K obj = super.assignAbstractFields(model);

        obj.setOwner(owner);

        return obj;
    }

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }
}
