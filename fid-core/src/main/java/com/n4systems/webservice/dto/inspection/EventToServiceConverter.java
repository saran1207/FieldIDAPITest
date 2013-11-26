package com.n4systems.webservice.dto.inspection;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.ThingEvent;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;

public class EventToServiceConverter implements ModelToServiceConverter<ThingEvent, InspectionServiceDTO> {
	private final ServiceDTOBeanConverter converter;
	
	public EventToServiceConverter(ServiceDTOBeanConverter converter) {
		this.converter = converter;
	}
	
	@Override
	public InspectionServiceDTO toServiceDTO(ThingEvent model) {
		return converter.convert(model);
	}

}
