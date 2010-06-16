package com.n4systems.model.inspectiontype;

import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Cleaner;

public class AggragateInspectionTypeCleaner implements Cleaner<InspectionType> {

	private final Set<Cleaner<InspectionType>> cleaners = new HashSet<Cleaner<InspectionType>>();
	
	public void clean(InspectionType inspectionType) {
		for (Cleaner<InspectionType> cleaner : cleaners) {
			cleaner.clean(inspectionType);
		}
	}

	public void addCleaner(Cleaner<InspectionType> cleaner) {
		cleaners.add(cleaner);
	}

}
