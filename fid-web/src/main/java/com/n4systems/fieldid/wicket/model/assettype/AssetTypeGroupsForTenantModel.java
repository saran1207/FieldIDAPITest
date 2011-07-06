package com.n4systems.fieldid.wicket.model.assettype;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.assettype.AssetTypeGroupsLoader;

import java.util.ArrayList;
import java.util.List;

public class AssetTypeGroupsForTenantModel extends FieldIDSpringModel<List<AssetTypeGroup>> {

    @Override
    protected List<AssetTypeGroup> load() {
        return new AssetTypeGroupsLoader(getSecurityFilter()).setOrderBy("orderIdx").load();
    }

}
