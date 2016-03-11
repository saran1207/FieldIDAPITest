package com.n4systems.fieldid.actions.helpers;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.event.EventsByNetworkIdPaginatedLoader;
import com.n4systems.model.safetynetwork.EventsByNetworkIdLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;

import java.util.Collections;
import java.util.List;

public class AllEventHelper {
	
	private final LegacyAsset legacyAssetManager;
	private final Asset asset;
	private final SecurityFilter filter;
	
	private Long eventCount;
	private Long localEventCount;
	private List<ThingEvent> events;
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

	public List<ThingEvent> getEvents() {
		if (events == null) {
			EventsByNetworkIdLoader loader = new EventsByNetworkIdLoader(filter);
			events = loader.setNetworkId(asset.getNetworkId()).load();
			Collections.sort(events);
			Collections.reverse(events);
		}
		return events;
	}
	
	public Pager<Event> getPaginatedEvents(Integer page, String order, boolean ascending) {
		EventsByNetworkIdPaginatedLoader loader = new EventsByNetworkIdPaginatedLoader(filter);
		return loader.setNetworkId(asset.getNetworkId()).setOrder(order, ascending).setPage(page).load();
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
