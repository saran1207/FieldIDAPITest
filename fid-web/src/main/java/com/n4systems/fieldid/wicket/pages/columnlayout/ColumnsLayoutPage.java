package com.n4systems.fieldid.wicket.pages.columnlayout;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.columnlayout.AvailableReportColumnsPanel;
import com.n4systems.fieldid.wicket.components.columnlayout.SelectedReportColumnsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.columnlayout.CustomColumnsModel;
import com.n4systems.model.columns.ActiveColumnMapping;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.columns.CustomColumnCategory;
import com.n4systems.model.columns.CustomColumnMapping;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.columns.loader.ColumnLayoutLoader;
import com.n4systems.model.columns.loader.ColumnMappingGroupLoader;
import com.n4systems.model.columns.saver.ColumnLayoutService;
import com.n4systems.model.columns.saver.CustomColumnMappingSaver;
import com.n4systems.model.columns.saver.PathCreator;
import com.n4systems.util.persistence.search.SortDirection;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ColumnsLayoutPage extends WebPage {

    private ReportType reportType;

    private List<ColumnMappingGroup> groups;

    private List<ColumnMapping> allColumns = new ArrayList<ColumnMapping>();
    private List<ColumnMapping> inUseColumns = new ArrayList<ColumnMapping>();
    private SortDirection sortDirection = SortDirection.ASC;
    private ColumnMapping sortColumn;

    private AvailableReportColumnsPanel availableReportColumnsPanel;
    private SelectedReportColumnsPanel selectedReportColumnsPanel;

    private FeedbackPanel feedbackPanel;

    public ColumnsLayoutPage(PageParameters pageParams) {
        reportType = pageParams.getAsEnum("type", ReportType.class);
        if (reportType == null)
            reportType = ReportType.EVENT;

        add(CSSPackageResource.getHeaderContribution("style/columnlayout/column_layout.css"));
        add(feedbackPanel = new FeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupPlaceholderTag(true);

        groups = new ColumnMappingGroupLoader(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner().getPrimaryOrg()).reportType(reportType).load();
        storeAndSortAllColumns();
        loadCurrentLayout();
        filterAvailableColumns();

        PropertyModel<List<ColumnMapping>> inUseColumnsModel = new PropertyModel<List<ColumnMapping>>(this, "inUseColumns");

        CustomColumnsModel assetsColumnsModel = new CustomColumnsModel(reportType, CustomColumnCategory.ASSET, inUseColumnsModel);
        CustomColumnsModel eventColumnsModel = new CustomColumnsModel(reportType, CustomColumnCategory.EVENT, inUseColumnsModel);

        add(availableReportColumnsPanel = new AvailableReportColumnsPanel("availableColumns", new PropertyModel<List<ColumnMappingGroup>>(this, "groups"), reportType, assetsColumnsModel, eventColumnsModel) {
            @Override
            protected void onReportColumnAddedToReport(AjaxRequestTarget target, ColumnMapping column) {
                inUseColumns.add(0, column);
                target.addComponent(selectedReportColumnsPanel);
                target.addComponent(feedbackPanel);
            }

            @Override
            protected void onCustomColumnCreated(AjaxRequestTarget target, String value, CustomColumnCategory category) {
                saveNewCustomColumn(target,value,category);
                target.addComponent(availableReportColumnsPanel);
                target.addComponent(feedbackPanel);
            }

            @Override
            protected void onCustomColumnRemoved(AjaxRequestTarget target, CustomColumnMapping customColumnMapping) {
                ColumnLayout layout = new ColumnLayoutLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).reportType(reportType).load();
                if (layout.getUsedColumnMappingList().contains(customColumnMapping)) {
                    error("That column is in use in the currently saved column layout. Please save the layout before deleting it.");
                } else {
                    new CustomColumnMappingSaver().remove(customColumnMapping);
                }
                target.addComponent(feedbackPanel);
            }
        });

        Form form;
        add(form = new SortAndSaveForm("sortAndSaveForm", new PropertyModel<List<ColumnMapping>>(this, "allColumns")));

        form.add(selectedReportColumnsPanel = new SelectedReportColumnsPanel("selectedReportColumnsPanel", inUseColumnsModel) {
            @Override
            protected void onColumnRemoved(AjaxRequestTarget target, ColumnMapping reportColumn) {
                for (ColumnMappingGroup group : groups) {
                    if (group.equals(reportColumn.getGroup())) {
                        group.getColumnMappings().add(reportColumn);
                    }
                }
                target.addComponent(selectedReportColumnsPanel);
                target.addComponent(availableReportColumnsPanel);
                target.addComponent(feedbackPanel);
            }
        });
    }

    private void storeAndSortAllColumns() {
        for (ColumnMappingGroup group : groups) {
            for (ColumnMapping columnMapping : group.getColumnMappings()) {
                allColumns.add(columnMapping);
            }
        }
        Collections.sort(allColumns, new Comparator<ColumnMapping>() {
            @Override
            public int compare(ColumnMapping c1, ColumnMapping c2) {
                String label1Value = new FIDLabelModel(c1.getLabel()).getObject();
                String label2Value = new FIDLabelModel(c2.getLabel()).getObject();
                return label1Value.compareTo(label2Value);
            }
        });
    }

    private void filterAvailableColumns() {
        for (ColumnMappingGroup group : groups) {
            group.getColumnMappings().removeAll(inUseColumns);
        }
    }

    class SortAndSaveForm extends Form {

        DropDownChoice<ColumnMapping> sortColumnSelect;
        DropDownChoice<SortDirection> directionSelect;
        final List<SortDirection> DIRECTIONS = Arrays.asList(SortDirection.ASC, SortDirection.DESC);
        IModel<List<ColumnMapping>> columnMappingsModel;

        public SortAndSaveForm(String id, IModel<List<ColumnMapping>> columnMappingsModel) {
            super(id);
            this.columnMappingsModel = columnMappingsModel;
            add(sortColumnSelect = new DropDownChoice<ColumnMapping>("sortColumnSelect", new PropertyModel<ColumnMapping>(ColumnsLayoutPage.this, "sortColumn"), columnMappingsModel, createColumnChoiceRenderer()));
            add(directionSelect = new DropDownChoice<SortDirection>("directionSelect", new PropertyModel<SortDirection>(ColumnsLayoutPage.this, "sortDirection"), DIRECTIONS, createChoiceRenderer()));
            sortColumnSelect.setNullValid(false);
            directionSelect.setNullValid(false);
            add(new AjaxButton("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    saveColumnLayout();
                    target.appendJavascript("parent.goBackToTemplates();");
                }
            });
            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    target.appendJavascript("parent.goBackToTemplates();");
                }
            });
        }

    }

    protected IChoiceRenderer<ColumnMapping> createColumnChoiceRenderer() {
        return new IChoiceRenderer<ColumnMapping>() {
            @Override
            public Object getDisplayValue(ColumnMapping object) {
                return new FIDLabelModel(new Model<String>(object.getLabel())).getObject();
            }

            @Override
            public String getIdValue(ColumnMapping object, int index) {
                return object.getId()+"";
            }
        };
    }

    protected IChoiceRenderer<SortDirection> createChoiceRenderer() {
        return new IChoiceRenderer<SortDirection>() {
            @Override
            public Object getDisplayValue(SortDirection object) {
                if (object == SortDirection.ASC) {
                    return "Ascending";
                }
                return "Descending";
            }

            @Override
            public String getIdValue(SortDirection object, int index) {
                return object.name();
            }
        };
    }

    private void loadCurrentLayout() {
        List<ColumnMapping> currentColumns = new ArrayList<ColumnMapping>();
        ColumnLayout layout = new ColumnLayoutLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).reportType(reportType).load();
        ArrayList<ActiveColumnMapping> activeMappings = new ArrayList<ActiveColumnMapping>();
        activeMappings.addAll(layout.getColumnMappingList());
        Collections.sort(activeMappings);
        for (ActiveColumnMapping mapping : activeMappings) {
            currentColumns.add(mapping.getMapping());
        }
        inUseColumns = currentColumns;
        sortDirection = layout.getSortDirection();
        sortColumn = layout.getSortColumn();
    }

    private void saveColumnLayout() {
        ColumnLayout cl = new ColumnLayout();
        cl.setReportType(reportType);
        cl.setTenant(FieldIDSession.get().getSessionUser().getTenant());
        int order = 0;
        for (ColumnMapping column : inUseColumns) {
            ActiveColumnMapping mapping = new ActiveColumnMapping();
            mapping.setColumnLayout(cl);
            mapping.setMapping(column);
            mapping.setOrder(order++);
            cl.getColumnMappingList().add(mapping);
            mapping.setTenant(FieldIDSession.get().getSessionUser().getTenant());
        }
        cl.setSortDirection(sortDirection);
        cl.setSortColumn(sortColumn);
        new ColumnLayoutService().saveLayout(FieldIDSession.get().getSessionUser().getSecurityFilter(), cl);
        FieldIDSession.get().storeInfoMessageForStruts("Layout successfully saved");
    }

    private void saveNewCustomColumn(AjaxRequestTarget target, String value, CustomColumnCategory category) {
        CustomColumnMapping customMapping = new CustomColumnMapping();
        customMapping.setLabel(value);
        customMapping.setPathExpression(new PathCreator().createPath(value, reportType, category));
        customMapping.setTenant(FieldIDSession.get().getSessionUser().getTenant());
        customMapping.setName(UUID.randomUUID().toString());
        customMapping.setCategory(category);
        customMapping.setReportType(reportType);
        customMapping.setDefaultOrder(2000);
        new CustomColumnMappingSaver().save(customMapping);
    }

}
