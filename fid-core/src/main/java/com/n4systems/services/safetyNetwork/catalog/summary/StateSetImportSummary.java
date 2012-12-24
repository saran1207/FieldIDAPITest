package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.ButtonGroup;

public class StateSetImportSummary extends BaseImportSummary {

	private Map<Long, ButtonGroup> importMapping = new HashMap<Long, ButtonGroup>();
	private List<ButtonGroup> stateSetsToCreate = new ArrayList<ButtonGroup>();
	private List<ButtonGroup> createdButtonGroups = new ArrayList<ButtonGroup>();

	public Map<Long, ButtonGroup> getImportMapping() {
		return importMapping;
	}

	public List<ButtonGroup> getStateSetsToCreate() {
		return stateSetsToCreate;
	}

	public List<ButtonGroup> getCreatedButtonGroups() {
		return createdButtonGroups;
	}
	
	public void createdStateSet(ButtonGroup buttonGroup) {
		createdButtonGroups.add(buttonGroup);
	}

}
