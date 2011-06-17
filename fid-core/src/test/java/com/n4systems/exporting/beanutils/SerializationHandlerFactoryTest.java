package com.n4systems.exporting.beanutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SerializationHandlerFactoryTest {

	@Test
	public void test_get_sorted_handlers() throws InstantiationException {
		SerializationHandlerFactory factory = new SerializationHandlerFactory();
		
		SerializationHandler[] handlers = factory.createSortedSerializationHandlers(TestExportBean.class);
		
		assertTrue(handlers[0] instanceof SimpleSerializationHandler);
		assertEquals("type", handlers[0].getField().getName());
		
		assertTrue(handlers[1] instanceof SimpleSerializationHandler);
		assertEquals("name", handlers[1].getField().getName());
		
		assertTrue(handlers[2] instanceof SimpleSerializationHandler);
		assertEquals("age", handlers[2].getField().getName());
		
		assertTrue(handlers[3] instanceof DummySerializationHandler);
		assertEquals("other", handlers[3].getField().getName());
		
		assertTrue(handlers[4] instanceof SimpleSerializationHandler);
		assertEquals("importOnlyText", handlers[4].getField().getName());
		
		assertTrue(handlers[5] instanceof SimpleSerializationHandler);		
		assertEquals("date", handlers[5].getField().getName());
		
		assertTrue(handlers[6] instanceof MapSerializationHandler);		
		assertEquals("map", handlers[6].getField().getName());
	}	
		
}
