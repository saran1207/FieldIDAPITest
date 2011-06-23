package com.n4systems.exporting.beanutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
public class CollectionSerializationHandlerTest {
	private final Date testDate = new Date();
	private TestExportBean bean = new TestExportBean("mytype", null, 42, testDate, Lists.newArrayList(new Integer(123), new Integer(456)));
	
	@Test
	public void test_marshal() throws MarshalingException, SecurityException, NoSuchFieldException {
		CollectionSerializationHandler<Integer> testHandler = new CollectionSerializationHandler<Integer>(TestExportBean.class.getDeclaredField("numbers"));

		
		Map<String, Object> values = testHandler.marshal(bean);
		
		System.out.println(values);
		assertEquals(2, values.size());
		assertNotNull(values.get("numbers:0"));
		assertNotNull(values.get("numbers:1"));
		assertEquals("123", values.get("numbers:0") );
	}
	
	@Test
	public void test_unmarshal() throws MarshalingException, SecurityException, NoSuchFieldException {
		CollectionSerializationHandler<Integer> testHandler = new CollectionSerializationHandler<Integer>(TestExportBean.class.getDeclaredField("numbers"));
		
		testHandler.unmarshal(bean, "numbers:0", new Integer(22));
		testHandler.unmarshal(bean, "numbers:0", new Integer(33));
		testHandler.unmarshal(bean, "numbers:0", new Integer(44));

		assertEquals(3, bean.getNumbers().size());
		assertEquals(new Integer(22), bean.getNumbers().get(0));
		assertEquals(new Integer(33), bean.getNumbers().get(1));
		assertEquals(new Integer(44), bean.getNumbers().get(2));
	}	
	
}
