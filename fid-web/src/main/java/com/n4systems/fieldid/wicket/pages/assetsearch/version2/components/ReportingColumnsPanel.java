package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class ReportingColumnsPanel extends AbstractColumnsPanel<EventReportCriteria> {

    private @SpringBean EventColumnsService eventColumnsService;

    public ReportingColumnsPanel(String id, IModel<EventReportCriteria> model) {
		super(id, model);
		setOutputMarkupId(true);
		setMarkupId(id);
	}

    @Override
    protected void updateColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
        super.updateColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);
        final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(getDefaultModel(), "eventTypeGroup");
        final IModel<EventType> eventTypeModel = new PropertyModel<EventType>(getDefaultModel(), "eventType");
        EventTypesForTenantModel availableEventTypesModel  = new EventTypesForTenantModel(eventTypeGroupModel);
        updateDynamicEventColumns(null, availableEventTypesModel.getObject());
    }

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

	public void updateAssetTypeOrGroup(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
		updateDynamicAssetColumns(selectedAssetType, availableAssetTypes);
		target.add(this);
	}

    public void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<EventType> availableEventTypes) {
        updateDynamicEventColumns(selectedEventType,availableEventTypes);
        target.add(this);
    }
}
