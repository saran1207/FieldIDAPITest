package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import rfid.ejb.entity.AssetStatus;

public class AssetStatusBuilder extends BaseBuilder<AssetStatus> {

    private final String name;
    private final Tenant tenant;

    public static AssetStatusBuilder anAssetStatus() {
        return new AssetStatusBuilder(null, null);
    }

    private AssetStatusBuilder(String name, Tenant tenant) {
        this.name = name;
        this.tenant = tenant;
    }

    public AssetStatusBuilder named(String name) {
        return makeBuilder(new AssetStatusBuilder(name, tenant));
    }

    public AssetStatusBuilder forTenant(Tenant tenant) {
        return makeBuilder(new AssetStatusBuilder(name, tenant));
    }

    @Override
    public AssetStatus createObject() {
        AssetStatus status = new AssetStatus();
        status.setName(name);
        status.setTenant(tenant);
        
        return status;
    }

}
