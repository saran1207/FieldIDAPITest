package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.AssetType;
import com.n4systems.model.assettype.AssetTypeListLoader;
import com.n4systems.model.security.SecurityFilter;

import java.util.List;

public class AssetTypesForTenantModel extends FieldIDSpringModel<List<AssetType>> {

    private SecurityFilter securityFilter;
    private String[] postFetchFields = new String[0];

    public AssetTypesForTenantModel(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Override
    protected List<AssetType> load() {
        return new AssetTypeListLoader(securityFilter).setPostFetchFields(postFetchFields).load();
    }

    public AssetTypesForTenantModel postFetchFields(String... fields) {
        this.postFetchFields = fields;
        return this;
    }

}
