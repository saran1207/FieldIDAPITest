package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.inspection.NewestEventsForAssetIdLoader;

public class RealTimeInspectionLookupHandler {

	private final NewestEventsForAssetIdLoader loader;
	
	private long productId;
	private Date lastEventDate;
	private List<Event> events;
	
	public RealTimeInspectionLookupHandler(NewestEventsForAssetIdLoader loader) {
		this.loader = loader;
	}
	
	public List<Event> lookup() {
		events = loader.setAssetId(productId).load();
		clearListIfInspectionNotNewer();
		return events;
	}
	
	private void clearListIfInspectionNotNewer() {
		if (lastEventDate != null && events.size() > 0) {
			Event compareEvent = events.get(0);
			if (!compareEvent.getDate().after(lastEventDate)) {
				events = new ArrayList<Event>();
			}
		}
	}
	
	public RealTimeInspectionLookupHandler setProductId(long productId) {
		this.productId = productId;
		return this;
	}
	
	public RealTimeInspectionLookupHandler setLastEventDate(Date lastEventDate) {
		this.lastEventDate = lastEventDate;
		return this;
	}
	
}
