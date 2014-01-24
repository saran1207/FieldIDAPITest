package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.components.GpsModel;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.components.table.HighlightPastDueSchedulesBehavior;
import com.n4systems.fieldid.wicket.data.EventReportDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class ReportResultsPanel extends SRSResultsPanel<EventReportCriteria> {

    public ReportResultsPanel(String id, final IModel<EventReportCriteria> eventCriteriaModel) {
        super(id, eventCriteriaModel);
    }

    @Override
    protected GpsModel<? extends HasGpsLocation> createMapModel(IModel<EventReportCriteria> criteriaModel) {
        throw new UnsupportedOperationException("maps not supported in " + getClass().getSimpleName());
    }


    @Override
    protected IColumn<RowView> createActionsColumn() {
        return new EventActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<EventReportCriteria> criteriaModel) {
        return new EventReportDataProvider(criteriaModel.getObject());
    }

    @Override
    protected void onRowItemCreated(Item<RowView> rowItem, IModel<RowView> rowModel) {
        rowItem.add(new HighlightPastDueSchedulesBehavior(rowModel));
    }
}
