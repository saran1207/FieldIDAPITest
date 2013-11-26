package com.n4systems.handlers;

import java.util.List;
import java.util.Set;

import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;

public interface CommonEventTypeHandler {

	public abstract Set<ThingEventType> findCommonEventTypesFor(
			List<Long> assetIds);

}