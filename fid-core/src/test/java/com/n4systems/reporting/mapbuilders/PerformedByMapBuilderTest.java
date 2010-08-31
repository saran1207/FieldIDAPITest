package com.n4systems.reporting.mapbuilders;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.user.User;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.ReportMap;


public class PerformedByMapBuilderTest {

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
		
		User user = anEmployee().build();
		user.setFirstName(TestHelper.randomString());
		user.setLastName(TestHelper.randomString());
		user.setPosition(TestHelper.randomString());
		user.setInitials(TestHelper.randomString());
		
		
		PerformedByMapBuilder builder = new PerformedByMapBuilder();
		builder.addParams(reportMap, user, null);
		
		assertEquals(user.getUserLabel(), reportMap.get(ReportField.DEPERCATED_INSPECTOR_NAME.getParamKey()));
		assertEquals(user.getUserLabel(), reportMap.get(ReportField.PERFORM_BY_NAME.getParamKey()));
		assertEquals(user.getPosition(), reportMap.get(ReportField.PERFORMED_BY_POSITION.getParamKey()));
		assertEquals(user.getInitials(), reportMap.get(ReportField.PERFORMED_BY_INITIALS.getParamKey()));
		// TODO: PerformedByMapBuilder this class also adds in the users signature image file stream.  Need a good way to test that part. 
		
	}
}
