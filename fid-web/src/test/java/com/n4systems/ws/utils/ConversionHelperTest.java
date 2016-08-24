package com.n4systems.ws.utils;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.ws.exceptions.WsResourceNotFoundException;
import com.n4systems.ws.model.WsModelConverter;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConversionHelperTest {
	private ConversionHelper helper = new ConversionHelper();

	@SuppressWarnings("unchecked")
	@Test
	public void test_convert_list() {
		List<Long> models = Arrays.asList(10L, 20L);
		List<String> convertedModels = Arrays.asList("10", "20");
		
		ListLoader<Long> loader = EasyMock.createMock(ListLoader.class);
		EasyMock.expect(loader.load()).andReturn(models);
		EasyMock.replay(loader);
		
		WsModelConverter<Long, String> converter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(converter.fromModels(models)).andReturn(convertedModels);
		EasyMock.replay(converter);
		
		List<String> result = helper.convertList(loader, converter);
		
		EasyMock.verify(loader);
		EasyMock.verify(converter);

		assertEquals(convertedModels, result);
	}

	@SuppressWarnings("unchecked")
	@Test(expected=WsResourceNotFoundException.class)
	public void test_convert_list_throws_exception_on_null_load() {
		ListLoader<Object> loader = new ListLoader<Object>(null) {
			@Override
			public List<Object> load() {
				return null;
			}
			@Override
			protected List<Object> load(EntityManager em, SecurityFilter filter) {
				return null;
			}
		};
		
		helper.convertList(loader, (WsModelConverter<Object, Long>)EasyMock.createMock(WsModelConverter.class));
	}
	
	
}
