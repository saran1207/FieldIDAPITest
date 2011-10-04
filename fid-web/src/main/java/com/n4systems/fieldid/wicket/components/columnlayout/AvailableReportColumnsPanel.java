package com.n4systems.fieldid.wicket.components.columnlayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.CollapsiblePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.columns.CustomColumnCategory;
import com.n4systems.model.columns.CustomColumnMapping;
import com.n4systems.model.columns.ReportType;

public class AvailableReportColumnsPanel extends Panel {

    // need to remember expanded state of the sections or they will all collapse after being repainted.
    private Map<String, Boolean> sectionsExpandedMap = new HashMap<String, Boolean>();

    public AvailableReportColumnsPanel(String id, IModel<List<ColumnMappingGroup>> columnGroupsModel, ReportType reportType, IModel<List<ColumnMapping>> customAssetColumnsList, IModel<List<ColumnMapping>> customEventsColumnsList) {
        super(id);
        setOutputMarkupId(true);

        add(new ListView<ColumnMappingGroup>("columnGroups", columnGroupsModel) {
            @Override
            protected void populateItem(ListItem<ColumnMappingGroup> item) {
                final FIDLabelModel titleModel = new FIDLabelModel(new PropertyModel<String>(item.getModel(), "label"));
                CollapsiblePanel columnsPanel = new CollapsiblePanel("columnGroup", titleModel, "images/columnlayout/arrow-over.png", "images/columnlayout/arrow-down.png") {
                    @Override
                    protected void onExpandedOrCollapsed(String title, Boolean expanded) {
                        setExpandedStatusForSection(title, expanded);
                    }
                };
                AvailableReportColumnGroupPanel availablePanel = new AvailableReportColumnGroupPanel(columnsPanel.getContainedPanelMarkupId(), new PropertyModel<List<ColumnMapping>>(item.getModel(), "columnMappings")) {
                    @Override
                    protected void onReportColumnAdded(AjaxRequestTarget target, ColumnMapping column) {
                        AvailableReportColumnsPanel.this.onReportColumnAddedToReport(target, column);
                    }
                };
                columnsPanel.addContainedPanel(availablePanel);
                columnsPanel.setExpanded(getExpandedStatusForSection(titleModel.getObject()));
                item.add(columnsPanel);
            }
        });

        CollapsiblePanel assetAttributesContainer = createCustomPanel("assetAttributesContainer", "label.assetattributes", CustomColumnCategory.ASSET, customAssetColumnsList);
        CollapsiblePanel eventAttributesContainer = createCustomPanel("eventAttributesContainer", "label.eventattributes", CustomColumnCategory.EVENT, customEventsColumnsList);

        if (reportType == ReportType.ASSET) {
            eventAttributesContainer.setVisible(false);
        }

        add(assetAttributesContainer);
        add(eventAttributesContainer);
    }

    private CollapsiblePanel createCustomPanel(String componentId, String label, final CustomColumnCategory category, IModel<List<ColumnMapping>> columnsList) {

        CollapsiblePanel assetAttributesContainer = new CollapsiblePanel(componentId, new FIDLabelModel(label), "images/columnlayout/arrow-over.png", "images/columnlayout/arrow-down.png") {
            @Override
            protected void onExpandedOrCollapsed(String title, Boolean expanded) {
                setExpandedStatusForSection(title, expanded);
            }
        };
        AddableColumnsListPanel addableColumnsListPanel = new AddableColumnsListPanel(assetAttributesContainer.getContainedPanelMarkupId(), columnsList) {
            @Override
            protected void onNewCustomColumnAdded(AjaxRequestTarget target, String name) {
                onCustomColumnCreated(target, name, category);
            }

            @Override
            protected void onReportColumnAdded(AjaxRequestTarget target, ColumnMapping column) {
                AvailableReportColumnsPanel.this.onReportColumnAddedToReport(target, column);
            }

            @Override
            protected void onReportColumnRemoved(AjaxRequestTarget target, ColumnMapping columnMapping) {
                onCustomColumnRemoved(target, (CustomColumnMapping) columnMapping);
                target.addComponent(AvailableReportColumnsPanel.this);
            }
        };
        assetAttributesContainer.addContainedPanel(addableColumnsListPanel);
        return assetAttributesContainer;
    }

    private Boolean getExpandedStatusForSection(String sectionLabel) {
        if (!sectionsExpandedMap.containsKey(sectionLabel)) {
            sectionsExpandedMap.put(sectionLabel, false);
        }
        return sectionsExpandedMap.get(sectionLabel);
    }

    private void setExpandedStatusForSection(String sectionLabel, Boolean expanded) {
        sectionsExpandedMap.put(sectionLabel, expanded);
    }

    protected void onReportColumnAddedToReport(AjaxRequestTarget target, ColumnMapping column) { }

    protected void onCustomColumnCreated(AjaxRequestTarget target, String value, CustomColumnCategory category) {}

    protected void onCustomColumnRemoved(AjaxRequestTarget target, CustomColumnMapping customColumnMapping) {}

}
