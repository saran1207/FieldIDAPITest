package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetTypesForTenantModel extends FieldIDSpringModel<List<AssetType>> {

    @SpringBean
    private AssetTypeService assetTypeService;

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
        return assetTypeService.getAssetTypes(group == null ? null : group.getId());
    }

}
