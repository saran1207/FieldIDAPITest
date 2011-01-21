package com.n4systems.model.builders;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable.EntityState;

public class AssetStatusBuilder extends BaseBuilder<AssetStatus> {

    private final String name;
    private Tenant tenant;
    private EntityState state;

    public static AssetStatusBuilder anAssetStatus() {
        return new AssetStatusBuilder(null, null, EntityState.ACTIVE);
    }

    private AssetStatusBuilder(String name, Tenant tenant, EntityState state) {
        this.name = name;
        this.tenant = tenant;
        this.state = state;
    }

    public AssetStatusBuilder named(String name) {
        return makeBuilder(new AssetStatusBuilder(name, tenant, state));
    }

    public AssetStatusBuilder forTenant(Tenant tenant) {
        return makeBuilder(new AssetStatusBuilder(name, tenant, state));
    }

    public AssetStatusBuilder withState(EntityState state) {
        return makeBuilder(new AssetStatusBuilder(name, tenant, state));
    }

    @Override
    public AssetStatus createObject() {
        AssetStatus status = new AssetStatus();
        status.setName(name);
        status.setTenant(tenant);
        status.setState(state);
        return status;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

}
