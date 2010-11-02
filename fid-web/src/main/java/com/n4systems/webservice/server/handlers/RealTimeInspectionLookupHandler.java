package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.inspection.NewestInspectionsForAssetIdLoader;

public class RealTimeInspectionLookupHandler {

	private final NewestInspectionsForAssetIdLoader loader;
	
	private long productId;
	private Date lastInspectionDate;
	private List<Event> events;
	
	public RealTimeInspectionLookupHandler(NewestInspectionsForAssetIdLoader loader) {
		this.loader = loader;
	}
	
	public List<Event> lookup() {
		events = loader.setAssetId(productId).load();
		clearListIfInspectionNotNewer();
		return events;
	}
	
	private void clearListIfInspectionNotNewer() {
		if (lastInspectionDate != null && events.size() > 0) {
			Event compareEvent = events.get(0);
			if (!compareEvent.getDate().after(lastInspectionDate)) {
				events = new ArrayList<Event>();
			}
		}
	}
	
	public RealTimeInspectionLookupHandler setProductId(long productId) {
		this.productId = productId;
		return this;
	}
	
	public RealTimeInspectionLookupHandler setLastInspectionDate(Date lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
		return this;
	}
	
}
