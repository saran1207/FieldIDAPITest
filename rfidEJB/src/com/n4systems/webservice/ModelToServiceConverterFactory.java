package com.n4systems.webservice;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationServiceDTO;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationToServiceConverter;

public class ModelToServiceConverterFactory {

	public PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> createPredefinedLocationToServiceConverter() {
		return new PredefinedLocationToServiceConverter();
	}
	
}
