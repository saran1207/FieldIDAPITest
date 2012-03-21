package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingLoader;
import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import com.n4systems.util.views.RowView;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

import java.util.List;

public abstract class SRSResultsPanel<T extends SearchCriteria> extends Panel {

    public static final String WIDGET_DEFINITION_PARAMETER = "wdf";
	public static final String X_PARAMETER = "longX";
	public static final String SERIES_PARAMETER = "series";
	public static final String Y_PARAMETER = "y";
    
	
    protected SimpleDataTable<RowView> dataTable;
    protected FieldIdAPIDataProvider provider;
    protected MultiIdSelection selectedRows;
    protected Label numSelectedLabel;
    protected Label totalResultsLabel;
    protected IModel<T> criteriaModel;

    public SRSResultsPanel(String id, final IModel<T> criteriaModel) {
        super(id);
        this.criteriaModel = criteriaModel;

        selectedRows = criteriaModel.getObject().getSelection();

        final T reportCriteria = criteriaModel.getObject();
        ReportFormatConverter converter = new ReportFormatConverter(getSecurityGuard());

        List<IColumn<RowView>> convertedColumns = converter.convertColumns(reportCriteria);

        SelectUnselectRowColumn selectUnselectRowColumn = new SelectUnselectRowColumn(selectedRows, new PropertyModel<Boolean>(this, "currentPageSelected")) {
            @Override
            protected void onSelectUnselectPage(AjaxRequestTarget target) {
                boolean selected = isCurrentPageSelected();
                if (selected) {
                    dataTable.justSelectedPageWithElements(countItemsOnCurrentPage());
                }
                target.appendJavaScript("setTableSelected('" + dataTable.getTable().getMarkupId() + "', " + selected + ");");
                updateSelectionStatus(target);
            }

            @Override
            protected void onSelectUnselectRow(AjaxRequestTarget target) {
                updateSelectionStatus(target);
            }
        };
        convertedColumns.add(0, selectUnselectRowColumn);
        convertedColumns.add(createActionsColumn());

        provider = createDataProvider(criteriaModel);
        add(dataTable = new SimpleDataTable<RowView>("resultsTable", convertedColumns, provider, 20, selectedRows) {
            @Override
            protected void onPageChanged(AjaxRequestTarget target) {
                ServletWebRequest request = (ServletWebRequest) getRequest();
                new LegacyReportCriteriaStorage().storePageNumber(request.getContainerRequest().getSession(), dataTable.getTable().getCurrentPage());
                target.add(totalResultsLabel);
            }

            @Override
            protected void onSelectionChanged(AjaxRequestTarget target) {
                SRSResultsPanel.this.updateSelectionStatus(target);
            }

            @Override
            protected void onSortChanged(String sortProperty, SortDirection sortDirection) {
                Long id = Long.parseLong(sortProperty);
                ColumnMapping column = new ColumnMappingLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).id(id).load();
                ColumnMappingView columnView = new ColumnMappingConverter().convert(column);
                criteriaModel.getObject().setSortColumn(columnView);
                criteriaModel.getObject().setSortDirection(sortDirection);
                storeCriteriaIfNecessary();
            }

            @Override
            protected void onRowItemCreated(Item<RowView> rowItem, IModel<RowView> rowModel) {
                SRSResultsPanel.this.onRowItemCreated(rowItem, rowModel);
            }
        });

        dataTable.getTable().setCurrentPage(criteriaModel.getObject().getPageNumber());
        selectUnselectRowColumn.setDataTable(dataTable.getTable());

        add(totalResultsLabel = new Label("totalResults", new PropertyModel<Integer>(this, "totalResults")));
        add(numSelectedLabel = new Label("numSelected", new PropertyModel<Integer>(selectedRows, "numSelectedIds")));
        totalResultsLabel.setOutputMarkupId(true);
        numSelectedLabel.setOutputMarkupId(true);
    }

    // package protected method is extract/overridden for testing purposes
    protected SerializableSecurityGuard getSecurityGuard() {
		return new SerializableSecurityGuard(FieldIDSession.get().getTenant());
	}

    protected void updateSelectionStatus(AjaxRequestTarget target) {
        target.add(numSelectedLabel);
        dataTable.updateSelectionStatus(target);
        storeCriteriaIfNecessary();
    }

    public boolean isCurrentPageSelected() {
        for (Long id : provider.getCurrentPageIdList()) {
            if (!selectedRows.containsId(id)) {
                return false;
            }
        }
        return true;
    }

    protected void storeCriteriaIfNecessary() {}
    protected abstract IColumn<RowView> createActionsColumn();
    protected abstract FieldIdAPIDataProvider createDataProvider(IModel<T> criteriaModel);
    protected void onRowItemCreated(Item<RowView> rowItem, IModel<RowView> rowModel) {}

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
