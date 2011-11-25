package com.n4systems.fieldid.wicket.pages.widgets;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;


public class OrgPeriodSubtitleHelperTest {

	private OrgPeriodSubtitleHelper fixture;
	
	@Before
	public void setUp() { 
		fixture = new OrgPeriodSubtitleHelper();
	}
	
	@Test 
	public void testgetSubtitleModel() { 
		BaseOrg org = OrgBuilder.aCustomerOrg().build();
		SubTitleModelInfo result = fixture.getSubTitleModel("bogusModel", org);
		assertEquals("dateRange.week.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());

		result = fixture.getSubTitleModel("bogusModel", null);
		assertEquals("dateRange.week.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
	}
}
