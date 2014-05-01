package com.n4systems.fieldid.wicket.model.assettype;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetTypeGroupsForTenantModel extends FieldIDSpringModel<List<AssetTypeGroup>> {

    @SpringBean
    private AssetTypeService assetTypeService;

    private boolean filterForProcedures = false;

    public AssetTypeGroupsForTenantModel() {}

    public AssetTypeGroupsForTenantModel(boolean filterForProcedures){
        this.filterForProcedures = filterForProcedures;
    }

    @Override
    protected List<AssetTypeGroup> load() {

        if(!filterForProcedures){
            return assetTypeService.getAssetTypeGroupsByOrder();
        } else {
            return assetTypeService.getAssetTypeGroupsForProceduresByOrder();
        }

    }

}
