package com.n4systems.exporting.beanutils;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.api.conversion.event.CriteriaResultViewBuilder;
public class CritieriaResultSerializationHandlerTest {
	private final Date testDate = new Date();
	private TestExportBean bean = new TestExportBean("mytype", null, 42, testDate, Lists.newArrayList(new Integer(123), new Integer(456)));
	
	@Test
	public void test_marshal() throws MarshalingException, SecurityException, NoSuchFieldException {
		CriteriaResultSerializationHandler testHandler = new CriteriaResultSerializationHandler(TestExportBean.class.getDeclaredField("results"));
		
		bean.setResults(Lists.newArrayList(new CriteriaResultViewBuilder().aCriteriaResultView().build()));
		
		Map<String, Object> values = testHandler.marshal(bean);
		
		assertEquals(3, values.size());
		
		assertEquals("Pass", values.get("aSection:text") );
		assertEquals("deficiency", values.get("aSection:text:D") );
		assertEquals("recommendation", values.get("aSection:text:R") );
	}
	
	
	@Test
	public void test_unmarshal() throws MarshalingException, SecurityException, NoSuchFieldException {
		CriteriaResultSerializationHandler testHandler = new CriteriaResultSerializationHandler(TestExportBean.class.getDeclaredField("results"));
		
		testHandler.unmarshal(bean, "section:criteria", "hello");
		testHandler.unmarshal(bean, "section:criteria:R", "rec");
		testHandler.unmarshal(bean, "section:criteria:D", "def");

		System.out.println(bean.getResults());
		assertEquals("def", bean.getResults().get(0).getDeficiencyString());
		assertEquals("hello", bean.getResults().get(0).getResultString());
		assertEquals("rec", bean.getResults().get(0).getRecommendationString());
	}	
	
	@Test(expected = MarshalingException.class)
	public void test_unmarshal_exception_invalid_suffix() throws MarshalingException, SecurityException, NoSuchFieldException {
		CriteriaResultSerializationHandler testHandler = new CriteriaResultSerializationHandler(TestExportBean.class.getDeclaredField("results"));
		
		testHandler.unmarshal(bean, "section:criteria:XXXX", "def");	// can't recognize title. should barf.

	}	
	
	@Test(expected = MarshalingException.class)
	public void test_unmarshal_exception_no_criteria() throws MarshalingException, SecurityException, NoSuchFieldException {
		CriteriaResultSerializationHandler testHandler = new CriteriaResultSerializationHandler(TestExportBean.class.getDeclaredField("results"));
		
		testHandler.unmarshal(bean, "sectionWithNoCriteria", "def");	// must specify BOTH section and criteria separated by ":"
	}	
	
	@Test(expected = MarshalingException.class)
	public void test_unmarshal_exception_null() throws MarshalingException, SecurityException, NoSuchFieldException {
		CriteriaResultSerializationHandler testHandler = new CriteriaResultSerializationHandler(TestExportBean.class.getDeclaredField("results"));
		
		testHandler.unmarshal(bean, null, "def");	// must specify a section and criteria.
	}	
	
}
