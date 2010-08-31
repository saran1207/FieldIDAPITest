package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.util.ListingPair;

public class InspectionTypeGroupImportSummary extends BaseImportSummary {


	private List<ListingPair> inspectionsGroupsToCreate = new ArrayList<ListingPair>();
	
	private Map<Long, InspectionTypeGroup> importMapping = new HashMap<Long, InspectionTypeGroup>();
	
	private List<InspectionTypeGroup> createdGroups = new ArrayList<InspectionTypeGroup>();
	

	public List<ListingPair> getInspectionsGroupsToCreate() {
		return inspectionsGroupsToCreate;
	}

	public Map<Long, InspectionTypeGroup> getImportMapping() {
		return importMapping;
	}

	public List<InspectionTypeGroup> getCreatedGroups() {
		return createdGroups;
	}
	
	public void createdGroup(InspectionTypeGroup group) {
		createdGroups.add(group);
	}

}
