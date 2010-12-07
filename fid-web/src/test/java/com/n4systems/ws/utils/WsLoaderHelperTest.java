package com.n4systems.ws.utils;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.ws.exceptions.WsInternalErrorException;
import com.n4systems.ws.exceptions.WsResourceNotFoundException;

public class WsLoaderHelperTest {
	
	@Test
	public void loads_and_returns_entity() {
		final String entity = "O Hai";
		
		String result = WsLoaderHelper.load(new Loader<String>() {
			@Override
			public String load() {
				return entity;
			}
			@Override
			protected String load(EntityManager em) {
				return entity;
			}
		});
		
		assertEquals(entity, result);
	}
	
	@Test (expected=WsResourceNotFoundException.class)
	public void throws_exception_on_null_entity() {
		WsLoaderHelper.load(new Loader<String>() {
			@Override
			public String load() {
				return null;
			}
			@Override
			protected String load(EntityManager em) {
				return null;
			}
		});
	}
	
	@Test (expected=WsInternalErrorException.class)
	public void throws_exception_on_load_exception() {
		WsLoaderHelper.load(new Loader<String>() {
			@Override
			public String load() {
				throw new RuntimeException();
			}
			@Override
			protected String load(EntityManager em) {
				throw new RuntimeException();
			}
		});
	}
}
