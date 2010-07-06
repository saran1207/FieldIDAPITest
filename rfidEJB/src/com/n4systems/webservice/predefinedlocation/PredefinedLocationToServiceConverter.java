package com.n4systems.webservice.predefinedlocation;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.webservice.PaginatedModelToServiceConverter;

public class PredefinedLocationToServiceConverter extends PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> {

	@Override
	public PredefinedLocationServiceDTO toServiceDTO(PredefinedLocation model) {
		PredefinedLocationServiceDTO dto = new PredefinedLocationServiceDTO();
		
		dto.setId(model.getId());
		dto.setName(model.getName());
		
		Long parentId = (model.getParent() != null) ? model.getParent().getId() : null;
		dto.setParentId(parentId);
		
		return dto;
	}

}
