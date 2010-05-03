package com.n4systems.handlers;

import java.util.List;
import java.util.Set;

import com.n4systems.model.InspectionType;

public interface CommonInspectionTypeHandler {

	public abstract Set<InspectionType> findCommonInspectionTypesFor(
			List<Long> assetIds);

}