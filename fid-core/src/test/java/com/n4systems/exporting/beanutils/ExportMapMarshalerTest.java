package com.n4systems.exporting.beanutils;

import com.n4systems.api.model.FullExternalOrgView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class ExportMapMarshalerTest {

	@Test
	public void test_to_bean_map() throws MarshalingException, InstantiationException {
		SerializationHandlerFactory handlerFactory = createMock(SerializationHandlerFactory.class);
		SerializationHandler[] serialHandlers = { createMock(SerializationHandler.class), createMock(SerializationHandler.class) };
		
		FullExternalOrgView orgView = new FullExternalOrgView();
		
		ExportMapMarshaller<FullExternalOrgView> marshaler = new  ExportMapMarshaller<FullExternalOrgView>(FullExternalOrgView.class, handlerFactory);
		
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
		ExportMapMarshaller<TestExportBean> marshaler = new  ExportMapMarshaller<TestExportBean>(TestExportBean.class);
		
		Map<String, Object> beanMap = marshaler.toBeanMap(new TestExportBean("mytype", null, 42, new Date(), new ArrayList<Integer>()));
		
		assertEquals("mytype", beanMap.get("Type"));
		assertEquals("", beanMap.get("Name"));
		assertEquals("42", beanMap.get("Age"));
	}
}
