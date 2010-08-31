package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.StateSet;

public class StateSetImportSummary extends BaseImportSummary {

	private Map<Long, StateSet> importMapping = new HashMap<Long, StateSet>();
	private List<StateSet> stateSetsToCreate = new ArrayList<StateSet>();
	private List<StateSet> createdStateSets = new ArrayList<StateSet>();

	public Map<Long, StateSet> getImportMapping() {
		return importMapping;
	}

	public List<StateSet> getStateSetsToCreate() {
		return stateSetsToCreate;
	}

	public List<StateSet> getCreatedStateSets() {
		return createdStateSets;
	}
	
	public void createdStateSet(StateSet stateSet) {
		createdStateSets.add(stateSet);
	}

}
