package com.n4systems.servicedto.converts;

import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class NullAssignedUserConverter implements AssignedUserConverter {

	public Asset convert(ProductServiceDTO productServiceDTO, Asset asset) {
		return asset;
	}

	@Override
	public ThingEvent convert(InspectionServiceDTO inspectionServiceDTO, ThingEvent event) {
		return event;
	}
}
