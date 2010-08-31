package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.InspectionType;

public class InspectionTypeImportSummary extends BaseImportSummary {

	private Map<Long, InspectionType> importMapping = new HashMap<Long, InspectionType>();
	private boolean renamed = false;
	private int numberRenamed = 0;
	
	public Map<Long, InspectionType> getImportMapping() {
		return importMapping;
	}

	public boolean isRenamed(Long id, String originalName) {
		if (importMapping.get(id) == null) {
			return false;
		}
		return !importMapping.get(id).getName().equalsIgnoreCase(originalName);
	}
	
	public void renamed() {
		renamed = true;
		numberRenamed++;
	}
	
	public boolean isAnyRenamed() {
		return renamed;
	}

	public int getNumberRenamed() {
		return numberRenamed;
	}

	public Integer numberImported() {
		return importMapping.size();
	}
	
	
	
	
	
	
}
