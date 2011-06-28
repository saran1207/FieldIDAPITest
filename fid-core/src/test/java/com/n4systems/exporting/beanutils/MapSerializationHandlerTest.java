package com.n4systems.exporting.beanutils;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
public class MapSerializationHandlerTest {

	private static final long TIMESTAMP = 1000L;

	@Test
	public void test_marshal() throws SecurityException, NoSuchFieldException, MarshalingException {
		TestExportBean bean = new TestExportBean();
		bean.getMap().put("f1", "v1");
		bean.getMap().put("f2", "v2");
		
		SerializationHandler<Map<String, String>> handler = new MapSerializationHandler(TestExportBean.class.getDeclaredField("map"));
		
		Map<String, Object> beanMap = handler.marshal(bean);
		
		assertEquals(2, beanMap.size());
		assertEquals("v1", beanMap.get("M:f1"));
		assertEquals("v2", beanMap.get("M:f2"));
	}
	
	@Test
	public void test_unmarshal() throws SecurityException, NoSuchFieldException, MarshalingException {
		TestExportBean bean = new TestExportBean();
		
		SerializationHandler<Map<String, String>> handler = new MapSerializationHandler(TestExportBean.class.getDeclaredField("map"));
			
		handler.unmarshal(bean, "M:f1", "v1");
		handler.unmarshal(bean, "M:f2", "v2");
		handler.unmarshal(bean, "M:f3", new Date(TIMESTAMP));
		
		assertEquals(3, bean.getMap().size());
		assertEquals("v1", bean.getMap().get("f1"));
		assertEquals("v2", bean.getMap().get("f2"));
		assertEquals(TIMESTAMP+"", bean.getMap().get("f3"));
	}
	
}
