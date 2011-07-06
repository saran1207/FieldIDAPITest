package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.assettype.AssetTypeListLoader;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class AssetTypesForTenantModel extends FieldIDSpringModel<List<AssetType>> {

    private String[] postFetchFields = new String[0];
    private IModel<AssetTypeGroup> assetTypeGroupModel;

    public AssetTypesForTenantModel() {
        this(new Model<AssetTypeGroup>(null));
    }

    public AssetTypesForTenantModel(IModel<AssetTypeGroup> assetTypeGroupModel) {
        this.assetTypeGroupModel = assetTypeGroupModel;
    }

    @Override
    protected List<AssetType> load() {
        AssetTypeGroup group = assetTypeGroupModel.getObject();
        return new AssetTypeListLoader(getSecurityFilter())
                .setGroupFilter(group == null ? null : group.getId())
                .setPostFetchFields(postFetchFields).load();
    }

    public AssetTypesForTenantModel postFetchFields(String... fields) {
        this.postFetchFields = fields;
        return this;
    }

}
