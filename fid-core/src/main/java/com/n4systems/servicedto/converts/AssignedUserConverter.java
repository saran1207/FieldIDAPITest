package com.n4systems.servicedto.converts;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public interface AssignedUserConverter {
	
	public Asset convert(ProductServiceDTO productServiceDTO, Asset asset);
	public Event convert(InspectionServiceDTO inspectionServiceDTO, Event event) throws ConversionException;
	

}
