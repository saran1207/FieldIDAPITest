package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.n4systems.model.AssetType;

public class ProductTypeImportSummary extends BaseImportSummary {

	private Map<Long, AssetType> importMapping = new HashMap<Long, AssetType>();
	private Map<Long, Long> autoAttributesToImport = new HashMap<Long, Long>();
	private boolean renamed = false;
	private int numberRenamed = 0;

	public int getNumberOfAutoAttributes() {
		int sum = 0;
		for (Entry<Long, Long> autoAttributeCount : autoAttributesToImport.entrySet()) {
			sum += autoAttributeCount.getValue();
		}
		return sum;
	}

	public void setAutoAttributeCountFor(Long productTypeId, Long autoAttributeCount) {
		autoAttributesToImport.put(productTypeId, autoAttributeCount);
	}

	public Map<Long, AssetType> getImportMapping() {
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

	

}
