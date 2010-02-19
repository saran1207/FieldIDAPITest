package com.n4systems.exporting.beanutils;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
public class ExportMapUnmarshalerTest {
	private static final String[] titles = {"Type", "Name", "Age"};
	
	private static final Map<String, String> beanMap = new LinkedHashMap<String, String>();
	static {
		beanMap.put("Type", "mytype");
		beanMap.put("Age", "42");
	}
	
	@Test
	public void test_to_bean() throws MarshalingException, InstantiationException {
		SerializationHandlerFactory handlerFactory = createMock(SerializationHandlerFactory.class);

		SerializationHandler[] serialHandler = {createMock(SerializationHandler.class)};  
		
		ExportMapUnmarshaler<TestExportBean> unmarshaler = new ExportMapUnmarshaler<TestExportBean>(TestExportBean.class, titles, handlerFactory);
		
		expect(handlerFactory.createSortedSerializationHandlers(TestExportBean.class)).andReturn(serialHandler);
		
		for (String title: titles) {
			expect(serialHandler[0].handlesField(title)).andReturn(true);
			
			serialHandler[0].unmarshal(anyObject(), (String)anyObject());
		}
		
		replay(handlerFactory);
		replay(serialHandler[0]);
		
		TestExportBean bean = unmarshaler.toBean(beanMap);
		
		assertNotNull(bean);
		
		verify(handlerFactory);
		verify(serialHandler[0]);
	}
	
	@Test
	public void test_full_conversion() throws MarshalingException, InstantiationException {
		ExportMapUnmarshaler<TestExportBean> marshaler = new ExportMapUnmarshaler<TestExportBean>(TestExportBean.class, titles);
		
		TestExportBean bean = marshaler.toBean(beanMap);
		
		assertEquals("mytype", bean.getType());
		assertNull(bean.getName());
		assertEquals(42, bean.getAge().intValue());
	}
	
}
