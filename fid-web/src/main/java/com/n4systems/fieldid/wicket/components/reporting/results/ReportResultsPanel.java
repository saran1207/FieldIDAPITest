package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.reporting.service.ColumnMappingConverter;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.TableViewAdapterDataProvider;
import com.n4systems.fieldid.wicket.model.reporting.EventReportCriteriaModel;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.Event;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingLoader;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;

import java.util.Iterator;
import java.util.List;

public class ReportResultsPanel extends Panel {

    SimpleDataTable<Event> dataTable;
    TableViewAdapterDataProvider<Event> provider;
    MultiIdSelection selectedRows;
    Label numSelectedLabel;
    Label totalResultsLabel;

    public ReportResultsPanel(String id, final IModel<EventReportCriteriaModel> criteriaModel) {
        super(id);

        selectedRows = criteriaModel.getObject().getSelection();

        EventReportCriteriaModel reportCriteria = criteriaModel.getObject();
        ReportFormatConverter converter = new ReportFormatConverter();

        List<IColumn> convertedColumns = converter.convertColumns(reportCriteria);

        SelectUnselectRowColumn selectUnselectRowColumn = new SelectUnselectRowColumn(selectedRows, new PropertyModel<Boolean>(this, "currentPageSelected")) {
            @Override
            protected void onSelectUnselectPage(AjaxRequestTarget target) {
                target.addComponent(dataTable);
                if (isCurrentPageSelected()) {
                    dataTable.justSelectedPageWithElements(getItemsOnCurrentPage());
                }
                target.addComponent(numSelectedLabel);
            }

            @Override
            protected void onSelectUnselectRow(AjaxRequestTarget target) {
                target.addComponent(dataTable);
                target.addComponent(numSelectedLabel);
            }
        };
        convertedColumns.add(0, selectUnselectRowColumn);
        convertedColumns.add(new EventActionsColumn());

        EventSearchContainer searchContainer = converter.convertCriteria(reportCriteria);
        provider = new TableViewAdapterDataProvider<Event>(Event.class, searchContainer, reportCriteria);
        add(dataTable = new SimpleDataTable<Event>("resultsTable", convertedColumns.toArray(new IColumn[convertedColumns.size()]), provider, 20, selectedRows) {
            @Override
            protected void onPageChanged(AjaxRequestTarget target) {
            	/*
            	 * TODO: TableViewAdapterDataProvider caches the results.  This MUST be called when the selection or
            	 * sort changes.  This is a part of a temporary fix to avoid going back to the database when rows
            	 * are selected - mf
            	 */
            	provider.clearResultCache();
                WebRequest request = (WebRequest) getRequest();
                new LegacyReportCriteriaStorage().storePageNumber(request.getHttpServletRequest().getSession(), dataTable.getTable().getCurrentPage());
                target.addComponent(totalResultsLabel);
            }

            @Override
            protected void onSelectionChanged(AjaxRequestTarget target) {
                target.addComponent(numSelectedLabel);
            }

            @Override
            protected void onSortChanged(String sortProperty, SortDirection sortDirection) {
            	/*
            	 * TODO: TableViewAdapterDataProvider caches the results.  This MUST be called when the selection or
            	 * sort changes.  This is a part of a temporary fix to avoid going back to the database when rows
            	 * are selected - mf
            	 */
            	provider.clearResultCache();
                Long id = Long.parseLong(sortProperty);
                ColumnMapping column = new ColumnMappingLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).id(id).load();
                ColumnMappingView columnView = new ColumnMappingConverter().convert(column);
                criteriaModel.getObject().setSortColumn(columnView);
                criteriaModel.getObject().setSortDirection(sortDirection);
            }
        });

        dataTable.getTable().setCurrentPage(criteriaModel.getObject().getPageNumber());

        add(totalResultsLabel = new Label("totalResults", new PropertyModel<Integer>(this, "totalResults")));
        add(numSelectedLabel = new Label("numSelected", new PropertyModel<Integer>(selectedRows, "numSelectedIds")));
        totalResultsLabel.setOutputMarkupId(true);
        numSelectedLabel.setOutputMarkupId(true);
    }

    public boolean isCurrentPageSelected() {
        Iterator<? extends Event> iterator = provider.iterator(20 * dataTable.getTable().getCurrentPage(), 20);
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (!selectedRows.containsId(event.getId())) {
                return false;
            }
        }
        return true;
    }

    public void setCurrentPageSelected(boolean selected) {
        Iterator<? extends Event> iterator = provider.iterator(20 * dataTable.getTable().getCurrentPage(), 20);
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (selected) {
                selectedRows.addId(event.getId());
            } else {
                selectedRows.removeId(event.getId());
            }
        }
    }

    protected int getItemsOnCurrentPage() {
        Iterator<? extends Event> iterator = provider.iterator(20 * dataTable.getTable().getCurrentPage(), 20);
        int count = 0;
        while(iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    public int getTotalResults() {
        return provider.size();
    }

}
