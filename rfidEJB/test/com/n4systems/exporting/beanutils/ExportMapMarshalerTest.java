package com.n4systems.exporting.beanutils;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.n4systems.api.model.FullExternalOrgView;

public class ExportMapMarshalerTest {

	@Test
	public void test_to_bean_map() throws MarshalingException, InstantiationException {
		SerializationHandlerFactory handlerFactory = createMock(SerializationHandlerFactory.class);
		SerializationHandler[] serialHandlers = { createMock(SerializationHandler.class), createMock(SerializationHandler.class) };
		
		FullExternalOrgView orgView = new FullExternalOrgView();
		
		ExportMapMarshaler<FullExternalOrgView> marshaler = new  ExportMapMarshaler<FullExternalOrgView>(FullExternalOrgView.class, handlerFactory);
		
		expect(handlerFactory.createSortedSerializationHandlers(FullExternalOrgView.class)).andReturn(serialHandlers);
		expect(serialHandlers[0].marshal(orgView)).andReturn(Collections.singletonMap("keyone", (Object)"valone"));
		expect(serialHandlers[1].marshal(orgView)).andReturn(Collections.singletonMap("keytwo", (Object)"valtwo"));
		
		replay(serialHandlers[0]);
		replay(serialHandlers[1]);
		replay(handlerFactory);
		
		Map<String, Object> beanMap = marshaler.toBeanMap(orgView);
		
		assertEquals(2, beanMap.size());
		assertEquals("valone", beanMap.get("keyone"));
		assertEquals("valtwo", beanMap.get("keytwo"));
		
		verify(serialHandlers[0]);
		verify(serialHandlers[1]);
		verify(handlerFactory);
	}
	
	@Test
	public void test_full_conversion() throws MarshalingException, InstantiationException {
		ExportMapMarshaler<TestExportBean> marshaler = new  ExportMapMarshaler<TestExportBean>(TestExportBean.class);
		
		Map<String, Object> beanMap = marshaler.toBeanMap(new TestExportBean("mytype", null, 42, new Date()));
		
		assertEquals("mytype", beanMap.get("Type"));
		assertEquals("", beanMap.get("Name"));
		assertEquals("42", beanMap.get("Age"));
	}
}
