package com.n4systems.servicedto.converts;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class NullAssignedUserConverter implements AssignedUserConverter {

	public Asset convert(ProductServiceDTO productServiceDTO, Asset asset) {
		return asset;
	}

	@Override
	public Event convert(InspectionServiceDTO inspectionServiceDTO, Event event) {
		return event;
	}
}
