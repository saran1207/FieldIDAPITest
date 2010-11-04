package com.n4systems.fieldid.actions.helpers;

import java.util.Collections;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.safetynetwork.EventsByNetworkIdLoader;
import com.n4systems.model.security.SecurityFilter;

public class AllEventHelper {
	
	private final LegacyAsset legacyAssetManager;
	private final Asset asset;
	private final SecurityFilter filter;
	
	private Long eventCount;
	private Long localEventCount;
	private List<Event> events;
	private Event lastEvent;
	
	public AllEventHelper(LegacyAsset legacyAssetManager, Asset asset, SecurityFilter filter) {
		this.legacyAssetManager = legacyAssetManager;
		this.asset = asset;
		this.filter = filter;
	}
	
	public Long getEventCount() {
		if (eventCount == null) {
			eventCount = legacyAssetManager.countAllEvents(asset, filter);
		}
		return eventCount;
	}

	public List<Event> getEvents() {
		if (events == null) {
			EventsByNetworkIdLoader loader = new EventsByNetworkIdLoader(filter);
			events = loader.setNetworkId(asset.getNetworkId()).load();
			Collections.sort(events);
		}
		return events;
	}

	public Event getLastEvent() {
		if (lastEvent == null) {
			lastEvent = legacyAssetManager.findLastEvents(asset, filter);
		}
		return lastEvent;
	}

	public Long getLocalEventCount() {
		if (localEventCount == null) {
			localEventCount = legacyAssetManager.countAllLocalEvents(asset, filter);
		}
		return localEventCount;
	}

}
