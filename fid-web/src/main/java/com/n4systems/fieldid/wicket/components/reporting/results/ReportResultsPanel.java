package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.FieldIdPropertyColumn;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.components.table.HighlightPastDueSchedulesBehavior;
import com.n4systems.fieldid.wicket.data.EventReportDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class ReportResultsPanel extends SRSResultsPanel<EventReportCriteria,HasGpsLocation> {

    public ReportResultsPanel(boolean googleTranslateEnabled, String id, final IModel<EventReportCriteria> eventCriteriaModel) {
        super(googleTranslateEnabled, id, eventCriteriaModel);
    }

    @Override
    protected IColumn<RowView> createAttachmentColumn(){
        return new EventAttachmentColumn();
    }

    @Override
    protected IColumn<RowView> createActionsColumn() {
        return new EventActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<EventReportCriteria> criteriaModel) {
        return new EventReportDataProvider(criteriaModel.getObject()) {
            @Override protected void storeIdList() {
                super.storeIdList();
                selectedRows.clearIndexes();
                selectedRows.validateIndexes( dataTable.getTable().getCurrentPage(), dataTable.getTable().getItemsPerPage(), getCurrentPageIdList());
            }
        };
    }

    @Override
    protected void onRowItemCreated(Item<RowView> rowItem, IModel<RowView> rowModel) {
        rowItem.add(new HighlightPastDueSchedulesBehavior(rowModel));
    }

    @Override
    protected ReportFormatConverter getReportFormatConverter(SerializableSecurityGuard securityGuard) {
        return new ReportFormatConverter(securityGuard) {
            @Override
            protected FieldIdPropertyColumn createSortableColumn(FIDLabelModel columnLabelModel, ColumnMappingView enabledColumn, int index) {
                return new ReportResultsPropertyColumn(googleTranslateEnabled, columnLabelModel, enabledColumn, index, true);
            }

            @Override
            protected FieldIdPropertyColumn createNonSortableColumn(FIDLabelModel columnLabelModel, ColumnMappingView enabledColumn, int index) {
                return new ReportResultsPropertyColumn(googleTranslateEnabled, columnLabelModel, enabledColumn, index);
            }
        };
    }
}
