package com.n4systems.servicedto.converts;

import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class NullAssignedUserConverter implements AssignedUserConverter {

	public Asset convert(ProductServiceDTO productServiceDTO, Asset product) {
		return product;
	}

	@Override
	public Inspection convert(InspectionServiceDTO inspectionServiceDTO, Inspection inspection) {
		return inspection;
	}
}
