package com.n4systems.exporting.beanutils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
public class SimpleSerializationHandlerTest {
	private final Date testDate = new Date();
	private TestExportBean bean = new TestExportBean("mytype", null, 42, testDate, new ArrayList<Integer>());
	
	@Test
	public void test_get_string_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("type"));
		
		Map<String, Object> values = testHandler.marshal(bean);
		
		assertEquals(1, values.size());
		assertEquals("mytype", values.get("Type"));
	}
	
	@Test
	public void test_get_null_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("name"));
		
		Map<String, Object> values = testHandler.marshal(bean);
		
		assertEquals(1, values.size());
		assertEquals("", values.get("Name"));
	}
	
	@Test
	public void test_get_int_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("age"));
		
		Map<String, Object> values = testHandler.marshal(bean);
		
		assertEquals(1, values.size());
		assertEquals("42", values.get("Age"));
	}
	
	@Test
	public void test_set_string_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("type"));
		
		TestExportBean test = new TestExportBean();
		
		testHandler.unmarshal(test, "", "asdasdasd");
		
		assertEquals("asdasdasd", test.getType());
	}
	
	@Test
	public void test_set_null_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("name"));
		
		TestExportBean test = new TestExportBean();
		
		testHandler.unmarshal(test, "", "");
		
		assertNull(test.getName());
	}
	
	@Test
	public void test_set_int_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("age"));
		
		TestExportBean test = new TestExportBean();
		
		testHandler.unmarshal(test, "", "99");
		
		assertEquals(99, test.getAge().intValue());
	}
	
	@Test
	public void test_set_date_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("date"));
		
		Date expected = new Date();
		
		TestExportBean test = new TestExportBean();
		
		testHandler.unmarshal(test, "", expected);
		
		assertEquals(expected, test.getDate());
	}
	
	@Test
	public void test_get_date_value() throws MarshalingException, SecurityException, NoSuchFieldException {
		SerializationHandler<String> testHandler = new SimpleSerializationHandler<String>(TestExportBean.class.getDeclaredField("date"));
		
		Map<String, Object> values = testHandler.marshal(bean);
		
		assertEquals(1, values.size());
		assertEquals(testDate, values.get("Date"));
	}
	
	@Test
	public void test_masked_serialization() throws Exception {
		SerializationHandler<String> testHandler = new MaskedSerializationHandler(TestExportBean.class.getDeclaredField("name"));
		
		Map<String, Object> values = testHandler.marshal(bean);
		
		assertEquals(1, values.size());
		assertEquals("", values.get("Name"));
	}
	
	
}
