package com.n4systems.webservice;

import org.apache.log4j.Logger;

import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.webservice.dto.AbstractListResponse;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.exceptions.ServiceException;

abstract public class PaginatedRequestHandler<R extends AbstractListResponse>  implements RequestHandler<PaginatedRequestInformation, R> {
	private final Logger logger = Logger.getLogger(PaginatedRequestHandler.class);
	private final LoaderFactory loaderFactory;
	private final int pageSize;
	
	public PaginatedRequestHandler(ConfigContext configContext, LoaderFactory loaderFactory) {
		pageSize = configContext.getInteger(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA).intValue();
		this.loaderFactory = loaderFactory;
	}
	
	abstract protected R createResponse(LoaderFactory loaderFactory, int currentPage, int pageSize) throws Exception;
	
	@Override
	public R getResponse(PaginatedRequestInformation request) throws ServiceException {
		try {
			int currentPage = request.getPageNumber().intValue();
		
			R response = createResponse(loaderFactory, currentPage, pageSize);
			return response;
		} catch (Exception e) {
			logger.error(String.format("%s failed request handling", getClass().getSimpleName()), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
