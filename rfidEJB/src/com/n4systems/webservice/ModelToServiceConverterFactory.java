package com.n4systems.webservice;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationServiceDTO;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationToServiceConverter;

public class ModelToServiceConverterFactory {

	private final LoaderFactory loaderFactory;
	
	public ModelToServiceConverterFactory(LoaderFactory loaderFactory) {
		this.loaderFactory = loaderFactory;
	}
	
	public PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> createPredefinedLocationToServiceConverter() {
		return new PredefinedLocationToServiceConverter(loaderFactory.createPredefinedLocationLevelsLoader());
	}
	
}
