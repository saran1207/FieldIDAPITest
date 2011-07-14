package com.n4systems.fieldid.wicket.model.assetstatus;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.assetstatus.AssetStatusListLoader;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetStatusesForTenantModel extends FieldIDSpringModel<List<AssetStatus>> {

    @SpringBean
    private AssetStatusService assetStatusService;

    @Override
    protected List<AssetStatus> load() {
        return assetStatusService.getActiveStatuses();
    }

}
