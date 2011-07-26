package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.TableViewAdapterDataProvider;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.Event;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingLoader;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import com.n4systems.util.views.RowView;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;

import java.util.List;

public class ReportResultsPanel extends Panel {

    SimpleDataTable<RowView> dataTable;
    TableViewAdapterDataProvider<Event> provider;
    MultiIdSelection selectedRows;
    Label numSelectedLabel;
    Label totalResultsLabel;
    IModel<EventReportCriteriaModel> criteriaModel;

    public ReportResultsPanel(String id, final IModel<EventReportCriteriaModel> criteriaModel) {
        super(id);
        this.criteriaModel = criteriaModel;

        selectedRows = criteriaModel.getObject().getSelection();

        final EventReportCriteriaModel reportCriteria = criteriaModel.getObject();
        ReportFormatConverter converter = new ReportFormatConverter();

        List<IColumn> convertedColumns = converter.convertColumns(reportCriteria);

        SelectUnselectRowColumn selectUnselectRowColumn = new SelectUnselectRowColumn(selectedRows, new PropertyModel<Boolean>(this, "currentPageSelected")) {
            @Override
            protected void onSelectUnselectPage(AjaxRequestTarget target) {
                boolean selected = isCurrentPageSelected();
                if (selected) {
                    dataTable.justSelectedPageWithElements(countItemsOnCurrentPage());
                }
                target.appendJavascript("setTableSelected('"+dataTable.getTable().getMarkupId()+"', "+selected+");");
                updateSelectionStatus(target);
            }

            @Override
            protected void onSelectUnselectRow(AjaxRequestTarget target) {
                updateSelectionStatus(target);
            }
        };
        convertedColumns.add(0, selectUnselectRowColumn);
        convertedColumns.add(new EventActionsColumn());

        EventSearchContainer searchContainer = converter.convertCriteria(reportCriteria);
        provider = new TableViewAdapterDataProvider<Event>(Event.class, searchContainer, reportCriteria);
        add(dataTable = new SimpleDataTable<RowView>("resultsTable", convertedColumns.toArray(new IColumn[convertedColumns.size()]), provider, 20, selectedRows) {
            @Override
            protected void onPageChanged(AjaxRequestTarget target) {
                WebRequest request = (WebRequest) getRequest();
                new LegacyReportCriteriaStorage().storePageNumber(request.getHttpServletRequest().getSession(), dataTable.getTable().getCurrentPage());
                target.addComponent(totalResultsLabel);
            }

            @Override
            protected void onSelectionChanged(AjaxRequestTarget target) {
                new LegacyReportCriteriaStorage().storeCriteria(reportCriteria, ((WebRequest) getRequest()).getHttpServletRequest().getSession());
                target.addComponent(numSelectedLabel);
            }

            @Override
            protected void onSortChanged(String sortProperty, SortDirection sortDirection) {
                Long id = Long.parseLong(sortProperty);
                ColumnMapping column = new ColumnMappingLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).id(id).load();
                ColumnMappingView columnView = new ColumnMappingConverter().convert(column);
                criteriaModel.getObject().setSortColumn(columnView);
                criteriaModel.getObject().setSortDirection(sortDirection);
                new LegacyReportCriteriaStorage().storeCriteria(reportCriteria, ((WebRequest) getRequest()).getHttpServletRequest().getSession());
            }
        });

        dataTable.getTable().setCurrentPage(criteriaModel.getObject().getPageNumber());
        selectUnselectRowColumn.setDataTable(dataTable.getTable());

        add(totalResultsLabel = new Label("totalResults", new PropertyModel<Integer>(this, "totalResults")));
        add(numSelectedLabel = new Label("numSelected", new PropertyModel<Integer>(selectedRows, "numSelectedIds")));
        totalResultsLabel.setOutputMarkupId(true);
        numSelectedLabel.setOutputMarkupId(true);
    }

    protected void updateSelectionStatus(AjaxRequestTarget target) {
        target.addComponent(numSelectedLabel);
        dataTable.updateSelectionStatus(target);
        new LegacyReportCriteriaStorage().storeCriteria(criteriaModel.getObject(), ((WebRequest) getRequest()).getHttpServletRequest().getSession());
    }

    public boolean isCurrentPageSelected() {
        for (Long id : provider.getCurrentPageIdList()) {
            if (!selectedRows.containsId(id)) {
                return false;
            }
        }
        return true;
    }

    public void setCurrentPageSelected(boolean selected) {
        if (selected) {
            selectedRows.addAllIds(provider.getCurrentPageIdList());
        } else {
            selectedRows.removeAllIds(provider.getCurrentPageIdList());
        }
    }

    protected int countItemsOnCurrentPage() {
        return provider.getCurrentPageIdList().size();
    }

    public int getTotalResults() {
        return provider.size();
    }

}
