/**
 * N4 systems copyright
 */
package com.n4systems.model.utils;

import com.n4systems.model.Event;
import com.n4systems.model.api.DisplayEnum;

import java.util.Arrays;
import java.util.List;

public enum CompressedScheduleStatus implements DisplayEnum {
	INCOMPLETE("Incomplete", Arrays.asList(Event.EventState.OPEN) ),
	COMPLETE("Complete", Arrays.asList(Event.EventState.COMPLETED)),
	ALL("All", Arrays.asList(Event.EventState.OPEN, Event.EventState.COMPLETED));
	
	private String label;
    private List<Event.EventState> eventStates;
	
	private CompressedScheduleStatus(String label, List<Event.EventState> eventStates) {
		this.label = label;
        this.eventStates = eventStates;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name();
	}

    public List<Event.EventState> getEventStates() {
        return eventStates;
    }

}