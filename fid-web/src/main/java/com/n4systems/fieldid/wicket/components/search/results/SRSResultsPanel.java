package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingLoader;
import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public abstract class SRSResultsPanel<T extends SearchCriteria, S extends HasGpsLocation> extends Panel {

    private static final String TOGGLE_PANEL_JS = "$('.tipsy').remove();";  // TODO : add js to show loading bar/text.

    protected SimpleDataTable<RowView> dataTable;
    protected FieldIdAPIDataProvider provider;
    protected MultiIdSelection selectedRows;
    protected IModel<T> criteriaModel;
    protected Component map;
    protected WebMarkupContainer resultButtons;

    public SRSResultsPanel(String id, final IModel<T> criteriaModel) {
        super(id);
        this.criteriaModel = criteriaModel;
        setOutputMarkupId(true);

        selectedRows = criteriaModel.getObject().getSelection();

        final T reportCriteria = criteriaModel.getObject();
        ReportFormatConverter converter = new ReportFormatConverter(getSecurityGuard());

        boolean isTextSearch = criteriaModel.getObject().getQuery() != null;
        List<IColumn<RowView>> convertedColumns = converter.convertColumns(reportCriteria, isTextSearch);

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
            @Override protected IModel<Integer> createSelectedModel() {
                return new PropertyModel<Integer>(SRSResultsPanel.this, "totalResults");
            }

            @Override protected IModel<Integer> createTotalModel() {
                return new PropertyModel<Integer>(selectedRows, "numSelectedIds");
            }

            @Override protected void onSelectionChanged(AjaxRequestTarget target) {
                SRSResultsPanel.this.updateSelectionStatus(target);
            }

            @Override protected void onSortChanged(String sortProperty, SortDirection sortDirection) {
                Long id = Long.parseLong(sortProperty);
                ColumnMapping column = new ColumnMappingLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).id(id).load();
                ColumnMappingView columnView = new ColumnMappingConverter().convert(column);
                criteriaModel.getObject().setSortColumn(columnView);
                criteriaModel.getObject().setSortDirection(sortDirection);
            }

            @Override protected void onRowItemCreated(Item<RowView> rowItem, IModel<RowView> rowModel) {
                SRSResultsPanel.this.onRowItemCreated(rowItem, rowModel);
            }
        });

        dataTable.getTable().setCurrentPage(criteriaModel.getObject().getPageNumber());
        selectUnselectRowColumn.setDataTable(dataTable.getTable());

        add(map = createMap("resultsMap"));

        resultButtons = new WebMarkupContainer("resultButtons");
        resultButtons.add(new IndicatingAjaxLink("table") {
            @Override public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript(TOGGLE_PANEL_JS);
                showTable(target);
            }
        }.setOutputMarkupId(true));
        resultButtons.add(new IndicatingAjaxLink("map") {
            @Override public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript(TOGGLE_PANEL_JS);
                showMap(target);
            }
        }.setOutputMarkupId(true));
        add(resultButtons.setVisible(true));
    }

    protected Component createMap(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    protected void showTable(AjaxRequestTarget target) {
        map.setVisible(false);
        dataTable.setVisible(true);
        target.add(this);
    }

    protected void showMap(AjaxRequestTarget target) {
        map.setVisible(true);
        dataTable.setVisible(false);
        target.add(this);
    }

    // package protected method is extract/overridden for testing purposes
    protected SerializableSecurityGuard getSecurityGuard() {
		return new SerializableSecurityGuard(FieldIDSession.get().getTenant());
	}

    protected void updateSelectionStatus(AjaxRequestTarget target) {
        dataTable.updateSelectionStatus(target);
    }

    public boolean isCurrentPageSelected() {
        int startingIndexOnThisPage = dataTable.getTable().getCurrentPage()*dataTable.getTable().getItemsPerPage();
        for (int i = 0; i < countItemsOnCurrentPage(); i++) {
            if (!selectedRows.containsIndex(startingIndexOnThisPage + i)) {
                return false;
            }
        }
        return true;
    }

    protected abstract IColumn<RowView> createActionsColumn();
    protected abstract FieldIdAPIDataProvider createDataProvider(IModel<T> criteriaModel);
    protected void onRowItemCreated(Item<RowView> rowItem, IModel<RowView> rowModel) {}

    public void setCurrentPageSelected(boolean selected) {
        List<Long> currentPageIdList = provider.getCurrentPageIdList();
        int startingIndexOnThisPage = dataTable.getTable().getCurrentPage()*dataTable.getTable().getItemsPerPage();
        int offset = 0;

        for (Long id : currentPageIdList) {
            if (selected) {
                selectedRows.addId(startingIndexOnThisPage + offset, id);
            } else {
                selectedRows.removeIndex(startingIndexOnThisPage + offset);
            }
            offset++;
        }
    }

    protected int countItemsOnCurrentPage() {
        return provider.getCurrentPageIdList().size();
    }

    public int getTotalResults() {
        return provider.size();
    }

    public SimpleDataTable<RowView> getDataTable() {
        return dataTable;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
    }
}
