package com.n4systems.fieldid.wicket.model.assetstatus;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.assetstatus.AssetStatusListLoader;

import java.util.List;

public class AssetStatusesForTenantModel extends FieldIDSpringModel<List<AssetStatus>> {

    @Override
    protected List<AssetStatus> load() {
        return new AssetStatusListLoader(getSecurityFilter()).load();
    }

}
