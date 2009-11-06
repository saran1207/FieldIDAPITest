package com.n4systems.fieldid.selenium;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.n4systems.fieldid.selenium.testcase.*;

/**
 * Each FieldIDTestCase is actually a suite of test cases. If you want to 
 * setup for all test cases and run them at one big group, you'd add them to
 * the @Suite.SuiteClasses array.
 * 
 * @author Darrell Grainger
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ LoginCommonUnitTests.class, LoginNoPartnerCenterUnitTests.class })
public class ExampleTestSuite extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String server = "localhost";
		int port = 4444;
		String tenant = "rtc";
		String domain = "grumpy.n4";
		String url = "https://" + tenant + "." + domain;
		String browser = "*iehta";
		FieldIDTestCase.setEnvironmentVariables(server, port, browser, url, tenant, domain);
	}
}
