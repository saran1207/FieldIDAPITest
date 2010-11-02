package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.util.ListingPair;

public class InspectionTypeGroupImportSummary extends BaseImportSummary {


	private List<ListingPair> inspectionsGroupsToCreate = new ArrayList<ListingPair>();
	
	private Map<Long, EventTypeGroup> importMapping = new HashMap<Long, EventTypeGroup>();
	
	private List<EventTypeGroup> createdGroups = new ArrayList<EventTypeGroup>();
	

	public List<ListingPair> getInspectionsGroupsToCreate() {
		return inspectionsGroupsToCreate;
	}

	public Map<Long, EventTypeGroup> getImportMapping() {
		return importMapping;
	}

	public List<EventTypeGroup> getCreatedGroups() {
		return createdGroups;
	}
	
	public void createdGroup(EventTypeGroup group) {
		createdGroups.add(group);
	}

}
