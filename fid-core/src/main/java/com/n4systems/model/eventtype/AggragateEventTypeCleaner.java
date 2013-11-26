package com.n4systems.model.eventtype;

import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;

public class AggragateEventTypeCleaner implements Cleaner<ThingEventType> {

	private final Set<Cleaner<ThingEventType>> cleaners = new HashSet<Cleaner<ThingEventType>>();
	
	public void clean(ThingEventType eventType) {
		for (Cleaner<ThingEventType> cleaner : cleaners) {
			cleaner.clean(eventType);
		}
	}

	public void addCleaner(Cleaner<ThingEventType> cleaner) {
		cleaners.add(cleaner);
	}

}
