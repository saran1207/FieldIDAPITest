package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;
import com.n4systems.fieldid.selenium.admin.console.AdminConsoleOrganizationsPage;
import com.thoughtworks.selenium.*;

/**
 * All test cases should extend this class. This class will look for
 * environment variables:
 * 
 *	seleniumserver
 *	seleniumport
 *	seleniumbrowser
 *	seleniumbaseurl
 * 
 * The seleniumserver is the host name for where the selenium-server.jar
 * is running from.
 * 
 * The seleniumport is the port address the selenium-server.jar is using on
 * the specified machine.
 * 
 * The seleniumbrowser is one of the following:
 * 
 * 		*iehta		Runs tests in Internet Explorer
 * 		*chrome		runs tests in Firefox
 * 
 * The seleniumbaseurl is the location of Field ID Web. Examples would be:
 * 
 * 	https://www.grumpy.n4/
 * 	https://n4.team.n4systems.com/
 * 
 * @author Darrell Grainger
 *
 */
public class FieldIDTestCase extends SeleneseTestBase {

	// All these environment variables need to be set
	protected static final String seleniumserverenv = 	"seleniumserver";
	protected static final String seleniumportenv = 	"seleniumport";
	protected static final String seleniumbrowserenv = 	"seleniumbrowser";
	protected static final String seleniumbaseurlenv = 	"seleniumbaseurl";
	
	protected String seleniumServer;
	protected int seleniumPort;
	protected String seleniumBrowserType;
	protected String seleniumURL;
	protected DefaultSelenium selenium;

	// List of different modules uses by the test cases
	AdminConsoleOrganizationsPage adminConsoleOrgPages;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		// Initial all the required environment variables
		initEnvironmentVariables();
		initSeleniumInstance();
		initLibriaries();
		
		// Startup the browser
		selenium.start();
		selenium.windowFocus();
		selenium.windowMaximize();
	}
	
	/**
	 * The functionality of the product will be grouped by pages.
	 * We initialize all the different 'page' classes here so they
	 * are available to all the test cases.
	 * 
	 */
	private void initLibriaries() {
		adminConsoleOrgPages = new AdminConsoleOrganizationsPage(selenium);
	}

	private void initSeleniumInstance() {
		selenium = new DefaultSelenium(seleniumServer, seleniumPort, seleniumBrowserType, seleniumURL);
		String msg = "Could not initialize Selenium with:\n" +
					"Selenium Server: '" + seleniumServer + "'\n" +
					"Selenium Port: '" + seleniumPort + "'\n" +
					"Selenium Browser: '" + seleniumBrowserType + "'\n" +
					"Selenium Base URL: '" + seleniumURL + "'\n";
		assertTrue(msg, selenium != null);
	}

	private void initEnvironmentVariables() {
		seleniumServer = System.getenv(seleniumserverenv);
		assertTrue("Need the environment variable '" + seleniumserverenv + "' set.", seleniumServer != null);
		String tmp = System.getenv(seleniumportenv);
		assertTrue("Need the environment variable '" + seleniumportenv + "' set.", tmp != null);
		seleniumPort = Integer.parseInt(tmp);
		seleniumBrowserType = System.getenv(seleniumbrowserenv);
		assertTrue("Need the environment variable '" + seleniumbrowserenv + "' set.", seleniumBrowserType != null);
		seleniumURL = System.getenv(seleniumbaseurlenv);
		assertTrue("Need the environment variable '" + seleniumbaseurlenv + "' set.", seleniumURL != null);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		if(selenium != null) {
			selenium.stop();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
