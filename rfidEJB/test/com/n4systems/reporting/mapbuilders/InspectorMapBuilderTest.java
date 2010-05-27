package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigContextOverridableTestDouble;


public class InspectorMapBuilderTest {

	private ConfigContext oldContext;

	@Before
	public void changeConfigContext() {
		oldContext = ConfigContext.getCurrentContext();
		ConfigContext.setCurrentContext(new ConfigContextOverridableTestDouble());
	}
	
	@After 
	public void removeConfig() {
		ConfigContext.setCurrentContext(oldContext);
	}
	

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		User user = UserBuilder.anEmployee().build();
		user.setFirstName(TestHelper.randomString());
		user.setLastName(TestHelper.randomString());
		user.setPosition(TestHelper.randomString());
		user.setInitials(TestHelper.randomString());
		
		
		InspectorMapBuilder builder = new InspectorMapBuilder();
		builder.addParams(reportMap, user, null);
		
		assertEquals(user.getUserLabel(), reportMap.get(ReportField.INSPECTOR_IDENTIFIED_BY.getParamKey()));
		assertEquals(user.getUserLabel(), reportMap.get(ReportField.INSPECTOR_NAME.getParamKey()));
		assertEquals(user.getPosition(), reportMap.get(ReportField.INSPECTOR_POSITION.getParamKey()));
		assertEquals(user.getInitials(), reportMap.get(ReportField.INSPECTOR_INITIALS.getParamKey()));
		
		// TODO: InspectorMapBuilder this class also adds in the users signature image file stream.  Need a good way to test that part. 
		
	}
}
