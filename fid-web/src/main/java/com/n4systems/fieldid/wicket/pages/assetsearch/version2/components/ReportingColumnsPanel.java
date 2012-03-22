package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class ReportingColumnsPanel extends AbstractColumnsPanel<EventReportCriteria> {

    private @SpringBean EventColumnsService eventColumnsService;

    public ReportingColumnsPanel(String id, IModel<EventReportCriteria> model) {
		super(id, model);
		setOutputMarkupId(true);
		setMarkupId(id);
	}

//    @Override
//    protected void udpateColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
//        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
//        GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
//        updateDynamicEventColumns(null, availableAssetTypesModel.getObject());
//    }

//        super.initialize(reportConfiguration);
//        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
//        GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
//        updateDynamicEventColumns(null, availableAssetTypesModel.getObject());

    protected ReportConfiguration loadReportConfiguration() {
        return eventColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }    

    @Override
    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForReporting(assetType, availableAssetTypes);
    }

    @Override
    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
        return dynamicColumnsService.getDynamicEventColumnsForReporting(eventType, availableEventTypes);
    }



    // XXX : called when assetype is changed in filters panel.
//	public void updateAssetTypeOrGroup(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
//		updateDynamicEventColumns(selectedAssetType, availableAssetTypes);
//		target.add(this);
//	}
    
}
