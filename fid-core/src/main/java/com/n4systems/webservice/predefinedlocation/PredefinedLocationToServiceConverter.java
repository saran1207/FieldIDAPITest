package com.n4systems.webservice.predefinedlocation;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.webservice.PaginatedModelToServiceConverter;

public class PredefinedLocationToServiceConverter extends PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> {

	private PredefinedLocationLevels locationLevels;
	
	public PredefinedLocationToServiceConverter(Loader<PredefinedLocationLevels> locationLevelLoader) {
		preloadLocationLevels(locationLevelLoader);
	}
	
	private void preloadLocationLevels(Loader<PredefinedLocationLevels> locationLevelLoader) {
		locationLevels = locationLevelLoader.load();
	}
	
	@Override
	public PredefinedLocationServiceDTO toServiceDTO(PredefinedLocation model) {
		PredefinedLocationServiceDTO dto = new PredefinedLocationServiceDTO();
		
		dto.setId(model.getId());
		dto.setName(model.getName());
		dto.setSearchIds(model.getSearchIds());
		dto.setLevelName(locationLevels.getNameForLevel(model).getName());
		
		Long parentId = (model.getParent() != null) ? model.getParent().getId() : null;
		dto.setParentId(parentId);
		dto.setDeleted(!model.isActive());
		
		return dto;
	}
}
