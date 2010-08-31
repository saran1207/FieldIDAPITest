package com.n4systems.fieldid.actions.helpers;

import java.util.Collections;
import java.util.List;


import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.safetynetwork.InspectionsByNetworkIdLoader;
import com.n4systems.model.security.SecurityFilter;

public class AllInspectionHelper {
	
	private final LegacyProductSerial legacyProductSerialManager;
	private final Product product;
	private final SecurityFilter filter;
	
	private Long inspectionCount;
	private Long localInspectionCount;
	private List<Inspection> inspections;
	private Inspection lastInspection; 
	
	public AllInspectionHelper(LegacyProductSerial legacyProductSerialManager, Product product, SecurityFilter filter) {
		super();
		this.legacyProductSerialManager = legacyProductSerialManager;
		this.product = product;
		this.filter = filter;
	}
	
	
	public Long getInspectionCount() {
		if (inspectionCount == null) {
			inspectionCount = legacyProductSerialManager.countAllInspections(product, filter);
		}
		return inspectionCount;
	}

	public List<Inspection> getInspections() {
		if (inspections == null) {
			InspectionsByNetworkIdLoader loader = new InspectionsByNetworkIdLoader(filter);
			inspections = loader.setNetworkId(product.getNetworkId()).load();
			Collections.sort(inspections);
		}
		return inspections;
	}

	public Inspection getLastInspection() {
		if (lastInspection == null) {
			lastInspection = legacyProductSerialManager.findLastInspections(product, filter);
		}
		return lastInspection;
	}


	public Long getLocalInspectionCount() {
		if (localInspectionCount == null) {
			localInspectionCount = legacyProductSerialManager.countAllLocalInspections(product, filter);
		}
		return localInspectionCount;
	}


}
