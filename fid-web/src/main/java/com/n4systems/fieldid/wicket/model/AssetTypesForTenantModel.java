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

    private boolean filterForProcedures = false;

    public AssetTypesForTenantModel() {
        this(new Model<AssetTypeGroup>(null));
    }

    public AssetTypesForTenantModel(IModel<AssetTypeGroup> assetTypeGroupModel) {
        this.assetTypeGroupModel = assetTypeGroupModel;
    }

    public AssetTypesForTenantModel(IModel<AssetTypeGroup> assetTypeGroupModel, boolean filterForProcedures) {
        this.assetTypeGroupModel = assetTypeGroupModel;
        this.filterForProcedures = filterForProcedures;
    }

    @Override
    protected List<AssetType> load() {
        AssetTypeGroup group = assetTypeGroupModel.getObject();
        if(!filterForProcedures) {
            return assetTypeService.getAssetTypes(group == null ? null : group.getId());
        } else {
            return assetTypeService.getAssetTypesFilteredForProcedures(group == null ? null : group.getId());
        }
    }

}
