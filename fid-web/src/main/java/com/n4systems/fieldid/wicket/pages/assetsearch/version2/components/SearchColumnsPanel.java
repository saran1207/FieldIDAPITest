package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;


public class SearchColumnsPanel extends AbstractColumnsPanel<AssetSearchCriteria> {

	public SearchColumnsPanel(String id, IModel<AssetSearchCriteria> model) {
		super(id, model);		
		setOutputMarkupId(true);
		setMarkupId(id);
	}

    @Override
    protected void initialize(ReportConfiguration reportConfiguration) {
        super.initialize(reportConfiguration);
        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
        GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
        updateDynamicAssetColumns(null, availableAssetTypesModel.getObject());
    }

    protected ReportConfiguration loadReportConfiguration() {
        return assetColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }    

    public void updateDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
    	dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
    }

    private List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);
    }    

	public void updateAssetTypeOrGroup(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
		updateDynamicAssetColumns(selectedAssetType, availableAssetTypes);
		target.add(this);
	}
    
}
