package com.n4systems.ws.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.ws.exceptions.WsResourceNotFoundException;
import com.n4systems.ws.model.WsModelConverter;

public class ConversionHelperTest {
	private ConversionHelper helper = new ConversionHelper();
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_convert_single() {
		Long model = 10L;
		String convertedModel = "10";
		
		Loader<Long> loader = EasyMock.createMock(Loader.class);
		EasyMock.expect(loader.load()).andReturn(model);
		EasyMock.replay(loader);
		
		WsModelConverter<Long, String> converter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(converter.fromModel(model)).andReturn(convertedModel);
		EasyMock.replay(converter);
		
		String result = helper.convert(loader, converter);
		
		EasyMock.verify(loader);
		EasyMock.verify(converter);

		assertEquals(convertedModel, result);
	}

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
		
		List<String> result = helper.convert(loader, converter);
		
		EasyMock.verify(loader);
		EasyMock.verify(converter);

		assertEquals(convertedModels, result);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=WsResourceNotFoundException.class)
	public void test_convert_single_throws_exception_on_null_load() {
		Loader<Object> loader = new Loader<Object>() {
			@Override
			public Object load() {
				return null;
			}
			@Override
			protected Object load(EntityManager em) {
				return null;
			}
		};
		
		helper.convert(loader, EasyMock.createMock(WsModelConverter.class));
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
		
		helper.convert(loader, (WsModelConverter<Object, Long>)EasyMock.createMock(WsModelConverter.class));
	}
	
	
}
