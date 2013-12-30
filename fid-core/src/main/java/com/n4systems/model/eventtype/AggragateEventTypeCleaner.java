package com.n4systems.model.eventtype;

import com.n4systems.model.EventType;
import com.n4systems.model.api.Cleaner;

import java.util.HashSet;
import java.util.Set;

public class AggragateEventTypeCleaner implements Cleaner<EventType> {

	private final Set<Cleaner<EventType>> cleaners = new HashSet<Cleaner<EventType>>();
	
	public void clean(EventType eventType) {
		for (Cleaner<EventType> cleaner : cleaners) {
			cleaner.clean(eventType);
		}
	}

	public void addCleaner(Cleaner<EventType> cleaner) {
		cleaners.add(cleaner);
	}

}
