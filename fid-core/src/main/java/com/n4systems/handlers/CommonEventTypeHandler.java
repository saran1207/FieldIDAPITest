package com.n4systems.handlers;

import com.n4systems.model.ThingEventType;

import java.util.List;
import java.util.Set;

public interface CommonEventTypeHandler {

	public abstract Set<ThingEventType> findCommonEventTypesFor(
			List<Long> assetIds);

}