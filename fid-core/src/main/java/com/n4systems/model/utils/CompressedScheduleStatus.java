/**
 * N4 systems copyright
 */
package com.n4systems.model.utils;

import com.n4systems.model.Event;
import com.n4systems.model.api.DisplayEnum;

import java.util.Arrays;
import java.util.List;

public enum CompressedScheduleStatus implements DisplayEnum {
	INCOMPLETE("Incomplete", Arrays.asList(Event.WorkflowState.OPEN) ),
	COMPLETE("Complete", Arrays.asList(Event.WorkflowState.COMPLETED)),
	ALL("All", Arrays.asList(Event.WorkflowState.OPEN, Event.WorkflowState.COMPLETED));
	
	private String label;
    private List<Event.WorkflowState> workflowStates;
	
	private CompressedScheduleStatus(String label, List<Event.WorkflowState> workflowStates) {
		this.label = label;
        this.workflowStates = workflowStates;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name();
	}

    public List<Event.WorkflowState> getWorkflowStates() {
        return workflowStates;
    }

}