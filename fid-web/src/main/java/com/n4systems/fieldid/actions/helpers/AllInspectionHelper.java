package com.n4systems.fieldid.actions.helpers;

import java.util.Collections;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.safetynetwork.InspectionsByNetworkIdLoader;
import com.n4systems.model.security.SecurityFilter;

public class AllInspectionHelper {
	
	private final LegacyProductSerial legacyProductSerialManager;
	private final Asset asset;
	private final SecurityFilter filter;
	
	private Long inspectionCount;
	private Long localInspectionCount;
	private List<Inspection> inspections;
	private Inspection lastInspection; 
	
	public AllInspectionHelper(LegacyProductSerial legacyProductSerialManager, Asset asset, SecurityFilter filter) {
		this.legacyProductSerialManager = legacyProductSerialManager;
		this.asset = asset;
		this.filter = filter;
	}
	
	public Long getInspectionCount() {
		if (inspectionCount == null) {
			inspectionCount = legacyProductSerialManager.countAllInspections(asset, filter);
		}
		return inspectionCount;
	}

	public List<Inspection> getInspections() {
		if (inspections == null) {
			InspectionsByNetworkIdLoader loader = new InspectionsByNetworkIdLoader(filter);
			inspections = loader.setNetworkId(asset.getNetworkId()).load();
			Collections.sort(inspections);
		}
		return inspections;
	}

	public Inspection getLastInspection() {
		if (lastInspection == null) {
			lastInspection = legacyProductSerialManager.findLastInspections(asset, filter);
		}
		return lastInspection;
	}

	public Long getLocalInspectionCount() {
		if (localInspectionCount == null) {
			localInspectionCount = legacyProductSerialManager.countAllLocalInspections(asset, filter);
		}
		return localInspectionCount;
	}

}
