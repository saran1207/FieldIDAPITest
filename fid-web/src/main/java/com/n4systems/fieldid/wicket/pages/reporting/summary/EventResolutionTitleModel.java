package com.n4systems.fieldid.wicket.pages.reporting.summary;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventResolutionTitleModel extends LoadableDetachableModel<String> {

    private IModel<EventReportCriteria> eventReportCriteriaModel;

    public EventResolutionTitleModel(IModel<EventReportCriteria> eventReportCriteriaModel) {
        this.eventReportCriteriaModel = eventReportCriteriaModel;
    }

    @Override
    protected String load() {
        DateRange dateRange = eventReportCriteriaModel.getObject().getDateRange();
        String result = new FIDLabelModel("label.event_resolution").getObject();
        if (dateRange.calculateFromDate() != null || dateRange.calculateToDate() != null) {
            if (dateRange.calculateFromDate() != null && dateRange.calculateToDate() != null) {
                result += " " + formatDate(dateRange.calculateFromDate()) + " - " + formatDate(dateRange.calculateToDate());
            } else if (dateRange.calculateFromDate() != null) {
                result += " from " + formatDate(dateRange.calculateFromDate());
            } else {
                result += " to " + formatDate(dateRange.calculateToDate());
            }
        }
        return result;
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("MMMMM d yyyy").format(date);
    }

}
