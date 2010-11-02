package com.n4systems.fieldid.actions.helpers;

import java.util.Collections;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.safetynetwork.InspectionsByNetworkIdLoader;
import com.n4systems.model.security.SecurityFilter;

public class AllInspectionHelper {
	
	private final LegacyAsset legacyAssetManager;
	private final Asset asset;
	private final SecurityFilter filter;
	
	private Long inspectionCount;
	private Long localInspectionCount;
	private List<Event> events;
	private Event lastEvent;
	
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

	public List<Event> getInspections() {
		if (events == null) {
			InspectionsByNetworkIdLoader loader = new InspectionsByNetworkIdLoader(filter);
			events = loader.setNetworkId(asset.getNetworkId()).load();
			Collections.sort(events);
		}
		return events;
	}

	public Event getLastInspection() {
		if (lastEvent == null) {
			lastEvent = legacyAssetManager.findLastInspections(asset, filter);
		}
		return lastEvent;
	}

	public Long getLocalInspectionCount() {
		if (localInspectionCount == null) {
			localInspectionCount = legacyAssetManager.countAllLocalInspections(asset, filter);
		}
		return localInspectionCount;
	}

}
