package com.n4systems.webservice;

import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.predefinedlocation.AllPredefinedLocationsRequestHandler;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationListResponse;

public class RequestHandlerFactory {
	private final ConfigContext configContext;
	private final LoaderFactory loaderFactory;
	private final ModelToServiceConverterFactory converterFactory;
	
	public RequestHandlerFactory(ConfigContext configContext, LoaderFactory loaderFactory, ModelToServiceConverterFactory converterFactory) {
		this.configContext = configContext;
		this.loaderFactory = loaderFactory;
		this.converterFactory = converterFactory;
		
	}
	
	public RequestHandler<PaginatedRequestInformation, PredefinedLocationListResponse> createAllPredefinedLocationsRequestHandler() {
		return new AllPredefinedLocationsRequestHandler(configContext, loaderFactory, converterFactory.createPredefinedLocationToServiceConverter());
	}
}
