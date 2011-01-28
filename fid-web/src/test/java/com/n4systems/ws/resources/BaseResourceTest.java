package com.n4systems.ws.resources;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.lastmod.WsLastModified;
import com.n4systems.ws.utils.ConversionHelper;
import com.n4systems.ws.utils.ResourceContext;

public class BaseResourceTest {
	
	private class TestBaseResource extends BaseResource<String, Long> {
		public TestBaseResource(ResourceContext context, ConversionHelper converter,WsModelConverter<LastModified, WsLastModified> lastModifiedConverter, ResourceDefiner<String, Long> definer) {
			super(context, converter, lastModifiedConverter, definer);
		}
	}
	
	private ResourceContext context;
	private ConversionHelper converter;
	private ResourceDefiner<String, Long> definer;
	private LoaderFactory loaderFactory;
	private WsModelConverter<String, Long> modelConverter;
	private WsModelConverter<LastModified, WsLastModified> lastModifiedConverter;
	private TestBaseResource resource;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup_mocks() {
		context = createMock(ResourceContext.class);
		converter = createMock(ConversionHelper.class);
		definer = createMock(ResourceDefiner.class);
		lastModifiedConverter = createMock(WsModelConverter.class);
		
		resource = new TestBaseResource(context, converter, lastModifiedConverter, definer);
		
		loaderFactory = createMock(LoaderFactory.class);
		modelConverter = createMock(WsModelConverter.class);
		
		expect(context.getLoaderFactory()).andReturn(loaderFactory);
		
		replay(context, loaderFactory, modelConverter, lastModifiedConverter);
	}
	
	@After
	public void verify_mocks() {
		verify(context, converter, definer, loaderFactory, modelConverter, lastModifiedConverter);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_get_list() {
		List<WsLastModified> wsModels = Arrays.asList(new WsLastModified(), new WsLastModified());
		
		ListLoader<LastModified> loader = createMock(ListLoader.class);
		
		expect(definer.getLastModifiedLoader(loaderFactory)).andReturn(loader);
		expect(converter.convertList(loader, lastModifiedConverter)).andReturn(wsModels);
		replay(loader, definer, converter);
		
		WsLastModified[] result = resource.getList();
		
		assertArrayEquals(wsModels.toArray(new WsLastModified[wsModels.size()]), result);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_get_single() {
		Long id = 42L;
		Long wsModel = 56L;
		
		expect(definer.getResourceConverter()).andReturn(modelConverter);
		
		FilteredIdLoader loader = createMock(FilteredIdLoader.class);
		
		expect(definer.getResourceIdLoader(loaderFactory)).andReturn(loader);
		expect(loader.setId(id)).andReturn(loader);
		expect(converter.convertSingle(loader, modelConverter)).andReturn(wsModel);
		replay(loader, definer, converter);
		
		assertEquals(wsModel, resource.getSingle(id));
	}
	
}
