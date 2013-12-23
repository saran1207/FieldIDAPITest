package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import java.util.List;


public class SearchColumnsPanel extends AbstractColumnsPanel<AssetSearchCriteria> {

	public SearchColumnsPanel(String id, IModel<AssetSearchCriteria> model) {
		super(id, model);		
	}


    protected ReportConfiguration loadReportConfiguration() {
        return assetColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }

    @Override
    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);
    }

    @Override
    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<? extends EventType> availableEventTypes) {
        return null;
    }

    public void updateAssetTypeOrGroup(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
		updateDynamicAssetColumns(selectedAssetType, availableAssetTypes);
		target.add(this);
	}
    
}
