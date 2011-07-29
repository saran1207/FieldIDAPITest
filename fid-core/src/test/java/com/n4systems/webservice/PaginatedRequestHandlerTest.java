package com.n4systems.webservice;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.NonDataSourceBackedConfigContext;
import com.n4systems.webservice.dto.AbstractListResponse;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.exceptions.ServiceException;

public class PaginatedRequestHandlerTest {

	private class TestAbstractListResponse extends AbstractListResponse {
		public TestAbstractListResponse(int currentPage, int totalPages, int recordsPerPage) {
			super(currentPage, totalPages, recordsPerPage);
		}
	};
	
	private class TestPaginatedRequestHandler extends PaginatedRequestHandler<TestAbstractListResponse> {
		public TestPaginatedRequestHandler(ConfigurationProvider configContext, LoaderFactory loaderFactory) {
			super(configContext, loaderFactory);
		}

		@Override
		protected TestAbstractListResponse createResponse(LoaderFactory loaderFactory, int currentPage, int pageSize) throws Exception {
			return new TestAbstractListResponse(currentPage, 1, pageSize);
		}	
	}
	
	private LoaderFactory loaderFactory;
	
	@Before
	public void mockLoaderFactory() {
		loaderFactory = createMock(LoaderFactory.class);
	}
	
	@Test
	public void gets_current_page_from_request() throws ServiceException {
		TestPaginatedRequestHandler sut = new TestPaginatedRequestHandler(new NonDataSourceBackedConfigContext(), loaderFactory);
		
		PaginatedRequestInformation request = new PaginatedRequestInformation();
		request.setPageNumber(99L);
		
		replay(loaderFactory);
		assertEquals(99, sut.getResponse(request).getCurrentPage());
		
		verify(loaderFactory);
	}
	
	@Test
	public void gets_page_size_from_config_context() throws ServiceException {
		ConfigContextOverridableTestDouble context = new ConfigContextOverridableTestDouble();
		context.addConfigurationValue(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA, 2047);
		
		TestPaginatedRequestHandler sut = new TestPaginatedRequestHandler(context, loaderFactory);
		
		PaginatedRequestInformation request = new PaginatedRequestInformation();
		request.setPageNumber(99L);
		
		replay(loaderFactory);
		assertEquals(2047, sut.getResponse(request).getRecordsPerPage());
		
		verify(loaderFactory);
	}

	@Test(expected=ServiceException.class)
	public void rethrows_exceptions_as_service_exception() throws ServiceException {
		PaginatedRequestHandler<TestAbstractListResponse> sut = new PaginatedRequestHandler<TestAbstractListResponse>(new NonDataSourceBackedConfigContext(), null) {
			@Override
			protected TestAbstractListResponse createResponse(LoaderFactory loaderFactory, int currentPage, int pageSize) throws Exception {
				throw new Exception("problem");
			}
		};
		
		PaginatedRequestInformation request = new PaginatedRequestInformation();
		request.setPageNumber(99L);
		
		sut.getResponse(request);
	}
}
