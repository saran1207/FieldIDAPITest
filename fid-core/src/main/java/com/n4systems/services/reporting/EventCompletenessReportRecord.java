package com.n4systems.services.reporting;

import com.n4systems.model.Event;
import com.n4systems.util.chart.DateChartable;


@SuppressWarnings("serial")
public class EventCompletenessReportRecord extends DateChartable {

    private final Event.WorkflowState workflowState;

    public EventCompletenessReportRecord(Long value, Event.WorkflowState workflowState, String granularity, Integer year, Integer month, Integer day) {
		super(value, granularity, year, month, day);
        this.workflowState = workflowState;
    }

    public Event.WorkflowState getWorkflowState() {
        return workflowState;
    }
}
