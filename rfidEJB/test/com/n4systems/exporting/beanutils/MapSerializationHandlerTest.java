package com.n4systems.exporting.beanutils;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
public class MapSerializationHandlerTest {

	@Test
	public void test_marshal() throws SecurityException, NoSuchFieldException, MarshalingException {
		TestExportBean bean = new TestExportBean();
		bean.getMap().put("f1", "v1");
		bean.getMap().put("f2", "v2");
		
		MapSerializationHandler handler = new MapSerializationHandler(TestExportBean.class.getDeclaredField("map"));
		
		Map<String, Object> beanMap = handler.marshal(bean);
		
		assertEquals(2, beanMap.size());
		assertEquals("v1", beanMap.get("M:f1"));
		assertEquals("v2", beanMap.get("M:f2"));
	}
	
	@Test
	public void test_unmarshal() throws SecurityException, NoSuchFieldException, MarshalingException {
		TestExportBean bean = new TestExportBean();
		
		MapSerializationHandler handler = new MapSerializationHandler(TestExportBean.class.getDeclaredField("map"));
			
		handler.unmarshal(bean, "M:f1", "v1");
		handler.unmarshal(bean, "M:f2", "v2");
		
		assertEquals(2, bean.getMap().size());
		assertEquals("v1", bean.getMap().get("f1"));
		assertEquals("v2", bean.getMap().get("f2"));
	}
	
}
