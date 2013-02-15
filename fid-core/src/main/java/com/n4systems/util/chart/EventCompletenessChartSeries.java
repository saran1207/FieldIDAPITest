package com.n4systems.util.chart;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.n4systems.model.Event;
import com.n4systems.services.reporting.EventCompletenessReportRecord;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class EventCompletenessChartSeries extends ChartSeries<LocalDate> {

    public EventCompletenessChartSeries(Event.WorkflowState workflowState, List<EventCompletenessReportRecord> events) {
        super(workflowState, workflowState.getLabel(), new ArrayList());
        add(getEvents(events,workflowState));
    }

    private List<EventCompletenessReportRecord> getEvents(List<EventCompletenessReportRecord> events, final Event.WorkflowState workflowState) {
        return ImmutableList.copyOf(Iterables.filter(events, new Predicate<EventCompletenessReportRecord>() {
            @Override
            public boolean apply(EventCompletenessReportRecord input) {
                return workflowState.equals(input.getWorkflowState());
            }
        }));
    }

}
