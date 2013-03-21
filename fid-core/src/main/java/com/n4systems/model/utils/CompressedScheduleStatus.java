/**
 * N4 systems copyright
 */
package com.n4systems.model.utils;

import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.DisplayEnum;

import java.util.Arrays;
import java.util.List;

public enum CompressedScheduleStatus implements DisplayEnum {
	INCOMPLETE("Incomplete", Arrays.asList(WorkflowState.OPEN) ),
	COMPLETE("Complete", Arrays.asList(WorkflowState.COMPLETED)),
	ALL("All", Arrays.asList(WorkflowState.OPEN, WorkflowState.COMPLETED));
	
	private String label;
    private List<WorkflowState> workflowStates;
	
	private CompressedScheduleStatus(String label, List<WorkflowState> workflowStates) {
		this.label = label;
        this.workflowStates = workflowStates;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name();
	}

    public List<WorkflowState> getWorkflowStates() {
        return workflowStates;
    }

}