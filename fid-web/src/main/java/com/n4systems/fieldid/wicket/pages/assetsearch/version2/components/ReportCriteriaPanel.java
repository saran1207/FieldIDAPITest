package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;


import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.List;

public class ReportCriteriaPanel extends AbstractCriteriaPanel<EventReportCriteria> {

    public ReportCriteriaPanel(String id, Model<EventReportCriteria> model) {
        super(id,model);
    }

    @Override
    protected AbstractColumnsPanel createColumnsPanel(String id, Model<EventReportCriteria> model) {
        return new ReportingColumnsPanel(id, model);
    }

    @Override
    protected Panel createFiltersPanel(String filters, Model<EventReportCriteria> model) {
        return new ReportingFilterPanel("filters",model) {
            @Override
            protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<EventType> availableEventTypes) {
                getReportingColumnsPanel().onEventTypeOrGroupUpdated(target, selectedEventType, availableEventTypes);
                updateDisplay();
            }

            @Override
            protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
                getReportingColumnsPanel().updateAssetTypeOrGroup(target, selectedAssetType, availableAssetTypes);
                updateDisplay();
            }

            private void updateDisplay() {
            }

        };
    }

    public ReportingColumnsPanel getReportingColumnsPanel() {
        return (ReportingColumnsPanel) columns;
    }


}
