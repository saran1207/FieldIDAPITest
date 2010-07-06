package com.n4systems.webservice;

import org.apache.log4j.Logger;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.webservice.dto.AbstractListResponse;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.dto.RequestInformation;
import com.n4systems.webservice.exceptions.ServiceException;

abstract public class PaginatedRequestHandler<R extends AbstractListResponse>  implements RequestHandler<PaginatedRequestInformation, R> {
	private final Logger logger = Logger.getLogger(PaginatedRequestHandler.class);
	private final int pageSize;
	
	public PaginatedRequestHandler(ConfigContext configContext) {
		pageSize = configContext.getInteger(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA).intValue();
	}
	
	abstract protected R createResponse(LoaderFactory loaderFactory, int currentPage, int pageSize) throws Exception;
	
	@Override
	public R getResponse(PaginatedRequestInformation request) throws ServiceException {
		try {
			int currentPage = request.getPageNumber().intValue();
			LoaderFactory loaderFactory = createLoaderFactory(request);
		
			R response = createResponse(loaderFactory, currentPage, pageSize);
			return response;
		} catch (Exception e) {
			logger.error(String.format("%s failed request handling", getClass().getSimpleName()), e);
			throw new ServiceException(e.getMessage());
		}
	}

	private LoaderFactory createLoaderFactory(RequestInformation request) {
		return new LoaderFactory(createSecurityFilter(request));
	}
	
	private SecurityFilter createSecurityFilter(RequestInformation request) {
		return new TenantOnlySecurityFilter(request.getTenantId());
	}
}
