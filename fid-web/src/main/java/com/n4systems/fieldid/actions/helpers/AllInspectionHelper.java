package com.n4systems.fieldid.actions.helpers;

import java.util.Collections;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.safetynetwork.InspectionsByNetworkIdLoader;
import com.n4systems.model.security.SecurityFilter;

public class AllInspectionHelper {
	
	private final LegacyAsset legacyAssetManager;
	private final Asset asset;
	private final SecurityFilter filter;
	
	private Long inspectionCount;
	private Long localInspectionCount;
	private List<Inspection> inspections;
	private Inspection lastInspection; 
	
	public AllInspectionHelper(LegacyAsset legacyAssetManager, Asset asset, SecurityFilter filter) {
		this.legacyAssetManager = legacyAssetManager;
		this.asset = asset;
		this.filter = filter;
	}
	
	public Long getInspectionCount() {
		if (inspectionCount == null) {
			inspectionCount = legacyAssetManager.countAllInspections(asset, filter);
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
			lastInspection = legacyAssetManager.findLastInspections(asset, filter);
		}
		return lastInspection;
	}

	public Long getLocalInspectionCount() {
		if (localInspectionCount == null) {
			localInspectionCount = legacyAssetManager.countAllLocalInspections(asset, filter);
		}
		return localInspectionCount;
	}

}
