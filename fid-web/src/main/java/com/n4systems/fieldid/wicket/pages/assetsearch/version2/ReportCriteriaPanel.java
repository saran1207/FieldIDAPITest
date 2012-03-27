package com.n4systems.fieldid.wicket.pages.assetsearch.version2;


import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.AbstractColumnsPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.AbstractCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.ReportingColumnsPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.ReportingFilterPanel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.AttributeModifier;
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
                // note : we know that this event can only occur when Filters panel is displayed. that's where the assetType widget lives.
                getReportingColumnsPanel().add(new AttributeModifier("style", "display:none;"));
            }

        };
    }

    public ReportingColumnsPanel getReportingColumnsPanel() {
        return (ReportingColumnsPanel) columns;
    }


}
