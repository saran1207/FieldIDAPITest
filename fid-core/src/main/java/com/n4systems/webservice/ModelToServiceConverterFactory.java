package com.n4systems.webservice;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Event;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.asset.AssetToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.inspection.EventToServiceConverter;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationServiceDTO;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationToServiceConverter;

public class ModelToServiceConverterFactory {
	private final LoaderFactory loaderFactory;
	private final ServiceDTOBeanConverter legacyConverter;
	
	public ModelToServiceConverterFactory(LoaderFactory loaderFactory, ServiceDTOBeanConverter legacyConverter) {
		this.loaderFactory = loaderFactory;
		this.legacyConverter = legacyConverter;
	}
	
	public PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> createPredefinedLocationToServiceConverter() {
		return new PredefinedLocationToServiceConverter(loaderFactory.createPredefinedLocationLevelsLoader());
	}
	
	public AssetToServiceConverter createAssetToServiceConverter() {
		return new AssetToServiceConverter(legacyConverter, loaderFactory.createLastEventLoader(), createEventToServiceConverter());
	}
	
	public ModelToServiceConverter<Event, InspectionServiceDTO> createEventToServiceConverter() {
		return new EventToServiceConverter(legacyConverter);
	}
}
