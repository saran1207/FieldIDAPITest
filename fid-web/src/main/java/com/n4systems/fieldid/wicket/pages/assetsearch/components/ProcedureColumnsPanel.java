package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class ProcedureColumnsPanel extends AbstractColumnsPanel<ProcedureCriteria> {

    private @SpringBean EventColumnsService eventColumnsService;

    public ProcedureColumnsPanel(String id, IModel<ProcedureCriteria> model) {
		super(id, model);
		setOutputMarkupId(true);
		setMarkupId(id);
	}

    @Override
    protected void updateColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
        //super.updateColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);
    }

    protected ReportConfiguration loadReportConfiguration() {
        ReportConfiguration reportConfiguration = eventColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());

        // HACK : TODO  need to figure out which columns to show...this will become more clear when modeling done.
        reportConfiguration.getColumnGroups().remove(5);
        reportConfiguration.getColumnGroups().remove(4);
        List<Integer> toRemove = Lists.newArrayList(17,16,15,11,10,8,2,1);
        for (int remove:toRemove) {
            reportConfiguration.getColumnGroups().get(0).getMappings().remove(remove);
        }
        // ------------------

        return reportConfiguration;
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
