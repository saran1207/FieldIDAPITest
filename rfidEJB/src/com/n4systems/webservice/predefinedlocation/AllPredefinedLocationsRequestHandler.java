package com.n4systems.webservice.predefinedlocation;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.webservice.PaginatedModelToServiceConverter;
import com.n4systems.webservice.PaginatedRequestHandler;
import com.n4systems.webservice.exceptions.ServiceException;

public class AllPredefinedLocationsRequestHandler extends PaginatedRequestHandler<PredefinedLocationListResponse> {
	private final PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> converter;
	
	public AllPredefinedLocationsRequestHandler(ConfigContext configContext, LoaderFactory loaderFactory, PaginatedModelToServiceConverter<PredefinedLocation, PredefinedLocationServiceDTO> converter) {
		super(configContext, loaderFactory);
		this.converter = converter;
	}

	@Override
	protected PredefinedLocationListResponse createResponse(LoaderFactory loaderFactory, int currentPage, int pageSize) throws ServiceException {
		PaginatedLoader<PredefinedLocation> loader = createLoader(loaderFactory, currentPage, pageSize);
		
		Pager<PredefinedLocation> pager = loader.load();
		
		PredefinedLocationListResponse response = new PredefinedLocationListResponse(currentPage, pager.getTotalPages(), pageSize);
		response.getLocations().addAll(converter.toServiceDTOList(pager));
		
		return response;
	}

	private PaginatedLoader<PredefinedLocation> createLoader(LoaderFactory loaderFactory, int currentPage, int pageSize) {
		PaginatedLoader<PredefinedLocation> loader = loaderFactory.createAllPredefinedLocationsPaginatedLoader().withSearchIds();
		loader.setPageSize(pageSize);
		loader.setPage(currentPage);
		return loader;
	}
}
