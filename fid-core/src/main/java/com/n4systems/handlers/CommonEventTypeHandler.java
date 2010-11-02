package com.n4systems.handlers;

import java.util.List;
import java.util.Set;

import com.n4systems.model.EventType;

public interface CommonEventTypeHandler {

	public abstract Set<EventType> findCommonEventTypesFor(
			List<Long> assetIds);

}