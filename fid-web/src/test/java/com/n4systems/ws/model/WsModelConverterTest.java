package com.n4systems.ws.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WsModelConverterTest {
	
	@Test
	public void converts_list() {
		List<String> strList = new WsModelConverter<Long, String>() {
			@Override
			public String fromModel(Long model) {
				return model.toString();
			}
		}.fromModels(Arrays.asList(1L, 2L, 3L));
		
		assertEquals(3, strList.size());
		assertEquals("1", strList.get(0));
		assertEquals("2", strList.get(1));
		assertEquals("3", strList.get(2));
	}
	
	@Test
	public void converts_empty_list() {
		List<String> strList =  new WsModelConverter<Long, String>() {
			@Override
			public String fromModel(Long model) {
				return model.toString();
			}
		}.fromModels(new ArrayList<Long>());
		
		assertNotNull(strList);
		assertTrue(strList.isEmpty());
	}
}
