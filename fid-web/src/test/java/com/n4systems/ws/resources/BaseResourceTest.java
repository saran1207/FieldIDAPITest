package com.n4systems.ws.resources;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.lastmod.WsLastModified;
import com.n4systems.ws.utils.ConversionHelper;
import com.n4systems.ws.utils.DateParam;
import com.n4systems.ws.utils.ResourceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

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
		
		replay(loaderFactory, lastModifiedConverter);
	}
	
	@After
	public void verify_mocks() {
		verify(context, converter, definer, loaderFactory, modelConverter, lastModifiedConverter);
	}

	@Test
	public void test_get_list() {
		DateParam after = new DateParam("1322760138224");
		
		List<WsLastModified> wsModels = Arrays.asList(new WsLastModified(), new WsLastModified());
		
		LastModifiedListLoader loader = createMock(LastModifiedListLoader.class);
		
		expect(definer.getLastModifiedLoader(loaderFactory)).andReturn(loader);
		expect(loader.modifiedAfter(after)).andReturn(loader);
		expect(converter.convertList(loader, lastModifiedConverter)).andReturn(wsModels);
		replay(loader, context, definer, converter, modelConverter);
		
		WsLastModified[] result = resource.list(after);
		
		assertArrayEquals(wsModels.toArray(new WsLastModified[wsModels.size()]), result);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_get_single() {
		Long id = 42L;
		List<String> models = Arrays.asList("56");
		List<Long> wsModels = Arrays.asList(56L);

		FilteredIdLoader loader = createMock(FilteredIdLoader.class);
		
		expect(definer.getResourceIdLoader(loaderFactory)).andReturn(loader);
		expect(loader.setId(id)).andReturn(loader);
		expect(loader.load()).andReturn(models.get(0));
		expect(definer.getResourceConverter()).andReturn(modelConverter);
		expect(modelConverter.fromModels(models)).andReturn(wsModels);
		expect(definer.getWsModelClass()).andReturn(Long.class);
		
		replay(loader, context, definer, converter, modelConverter);
		
		Long[] res = resource.get(id.toString());
		
		assertTrue(res.length == 1);
		assertEquals(wsModels.get(0), res[0]);
	}
	
}
