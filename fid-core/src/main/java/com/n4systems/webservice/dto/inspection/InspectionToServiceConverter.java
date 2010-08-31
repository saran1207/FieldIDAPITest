package com.n4systems.webservice.dto.inspection;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Inspection;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;

public class InspectionToServiceConverter implements ModelToServiceConverter<Inspection, InspectionServiceDTO> {
	private final ServiceDTOBeanConverter converter;
	
	public InspectionToServiceConverter(ServiceDTOBeanConverter converter) {
		this.converter = converter;
	}
	
	@Override
	public InspectionServiceDTO toServiceDTO(Inspection model) {
		return converter.convert(model);
	}

}
