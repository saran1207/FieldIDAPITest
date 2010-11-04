package com.n4systems.webservice.dto.inspection;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Event;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;

public class EventToServiceConverter implements ModelToServiceConverter<Event, InspectionServiceDTO> {
	private final ServiceDTOBeanConverter converter;
	
	public EventToServiceConverter(ServiceDTOBeanConverter converter) {
		this.converter = converter;
	}
	
	@Override
	public InspectionServiceDTO toServiceDTO(Event model) {
		return converter.convert(model);
	}

}
