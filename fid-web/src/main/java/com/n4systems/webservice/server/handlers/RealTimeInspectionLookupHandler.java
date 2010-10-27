package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Inspection;
import com.n4systems.model.inspection.NewestInspectionsForAssetIdLoader;

public class RealTimeInspectionLookupHandler {

	private final NewestInspectionsForAssetIdLoader loader;
	
	private long productId;
	private Date lastInspectionDate;
	private List<Inspection> inspections;
	
	public RealTimeInspectionLookupHandler(NewestInspectionsForAssetIdLoader loader) {
		this.loader = loader;
	}
	
	public List<Inspection> lookup() {
		inspections = loader.setAssetId(productId).load();
		clearListIfInspectionNotNewer();
		return inspections;
	}
	
	private void clearListIfInspectionNotNewer() {
		if (lastInspectionDate != null && inspections.size() > 0) {
			Inspection compareInspection = inspections.get(0);
			if (!compareInspection.getDate().after(lastInspectionDate)) {
				inspections = new ArrayList<Inspection>();
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
