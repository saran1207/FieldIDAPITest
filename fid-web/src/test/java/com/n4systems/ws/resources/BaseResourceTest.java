package com.n4systems.ws.resources;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.utils.ConversionHelper;
import com.n4systems.ws.utils.ResourceContext;

public class BaseResourceTest {
	
	private class TestBaseResource extends BaseResource<String, Long> {
		public TestBaseResource(ResourceContext context, ConversionHelper converter, ResourceDefiner<String, Long> definer) {
			super(context, converter, definer);
		}
	}
	
	private ResourceContext context;
	private ConversionHelper converter;
	private ResourceDefiner<String, Long> definer;
	private LoaderFactory loaderFactory;
	private WsModelConverter<String, Long> modelConverter;
	private TestBaseResource resource;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup_mocks() {
		context = createMock(ResourceContext.class);
		converter = createMock(ConversionHelper.class);
		definer = createMock(ResourceDefiner.class);
		resource = new TestBaseResource(context, converter, definer);
		
		loaderFactory = createMock(LoaderFactory.class);
		modelConverter = createMock(WsModelConverter.class);
		
		expect(context.getLoaderFactory()).andReturn(loaderFactory);
		expect(definer.getResourceConverter()).andReturn(modelConverter);
		
		replay(context, loaderFactory, modelConverter);
	}
	
	@After
	public void verify_mocks() {
		verify(context, converter, definer, loaderFactory, modelConverter);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_get_list() {
		List<Long> wsModels = Arrays.asList(1L, 2L, 3L);
		
		ListLoader<String> loader = createMock(ListLoader.class);
		
		expect(definer.getResourceListLoader(loaderFactory)).andReturn(loader);
		expect(definer.getWsModelClass()).andReturn(Long.class);
		expect(converter.convertList(loader, modelConverter)).andReturn(wsModels);
		replay(loader, definer, converter);
		
		Long[] result = resource.getList();
		
		assertArrayEquals(wsModels.toArray(new Long[wsModels.size()]), result);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_get_single() {
		Long id = 42L;
		Long wsModel = 56L;
		
		FilteredIdLoader loader = createMock(FilteredIdLoader.class);
		
		expect(definer.getResourceIdLoader(loaderFactory)).andReturn(loader);
		expect(loader.setId(id)).andReturn(loader);
		expect(converter.convertSingle(loader, modelConverter)).andReturn(wsModel);
		replay(loader, definer, converter);
		
		assertEquals(wsModel, resource.getSingle(id));
	}
	
}
