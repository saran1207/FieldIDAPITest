package com.n4systems.fieldid.selenium.testcase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.*;

import com.n4systems.fieldid.PropertyLoader;
import com.n4systems.fieldid.selenium.admin.console.AdminConsoleOrganizationsPage;
import com.thoughtworks.selenium.*;

/**
 * All test cases should extend this class. This class will look for a
 * property file matching the test case class name. For example, if I
 * create the test case:
 * 
 * 	public class LoginTestCases extends FieldIDTestCase
 * 
 * then LoginTestCases.properties will be the name of the property
 * file for this test case class. This property file must have as a
 * minimum the following properties:
 * 
 * 	browsertype
 * 	seleniumserver
 * 	port
 * 	baseurl
 * 
 * The browsertype is one of the following:
 * 
 * 		*iehta		Runs tests in Internet Explorer
 * 		*chrome		runs tests in Firefox
 * 
 * The seleniumserver is the host name for where the selenium-server.jar
 * is running from.
 * 
 * The port is the port address the selenium-server.jar is using on the
 * specified machine.
 * 
 * The baseurl is the location of Field ID Web. Examples would be:
 * 
 * 	https://www.grumpy.n4/
 * 	https://n4.team.n4systems.com/
 * 	https://www.fieldid.com/
 * 
 * @author Darrell Grainger
 *
 */
public class FieldIDTestCase extends SeleneseTestBase {

	protected PropertyLoader p;
	protected String browserType;
	protected DefaultSelenium selenium;
	protected String server;
	protected int port;
	protected String baseURL;

	// List of different modules uses by the test cases
	AdminConsoleOrganizationsPage adminConsoleOrgs;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		p = new PropertyLoader(getClass());
		browserType = p.getProperty("browsertype");
		server = p.getProperty("seleniumserver");
		port = Integer.parseInt(p.getProperty("port"));
		baseURL = p.getProperty("baseurl");
		selenium = new DefaultSelenium(server, port, browserType, baseURL);
		adminConsoleOrgs = new AdminConsoleOrganizationsPage(selenium);
		selenium.start();
		selenium.windowFocus();
		selenium.windowMaximize();
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
