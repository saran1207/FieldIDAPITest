package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;
import com.n4systems.fieldid.selenium.login.LoginPage;
import com.n4systems.fieldid.selenium.admin.console.AdminConsoleOrganizationsPage;
import com.thoughtworks.selenium.*;

/**
 * All test cases should extend this class. This class will look for
 * environment variables:
 * 
 *	seleniumserver
 *	seleniumport
 *	seleniumbrowser
 *	seleniumresource
 *	seleniumdomain
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
 * The seleniumresource is the protocol you want to use to access the
 * website. It must be one of the following:
 * 
 * 		http
 * 		https
 * 
 * The seleniumdomain is the base domain for the website. For example, if
 * the website is http://unirope.grumpy.n4/fieldid/ then the domain would
 * be grumpy.n4. If the website was https://foo.team.n4systems.com/fieldid/
 * then the domain would be team.n4systems.com
 * 
 * @author Darrell Grainger
 *
 */
public class FieldIDTestCase extends SeleneseTestBase {

	// Some useful constants:
	public static final String pageLoadDefaultTimeout = "30000";	// give a page 30 seconds to load

	// All these environment variables need to be set
	protected static final String seleniumserverenv = 		"seleniumserver";
	protected static final String seleniumportenv = 		"seleniumport";
	protected static final String seleniumbrowserenv = 		"seleniumbrowser";
	protected static final String seleniumresoureceenv = 	"seleniumresource";
	protected static final String seleniumdomainenv = 		"seleniumdomain";
	protected static final String seleniumtenantenv = 		"seleniumtenant";
	
	/**
	 * All these variables need to be set. They can either be set at the
	 * Test Suite level or they can be set using the environment variables
	 * listed above. If you run the individual test case or test class,
	 * you will need the environment variables defined. If you 
	 */
	protected static String seleniumServer = null;
	protected static int seleniumPort = -1;
	protected static String seleniumBrowserType = null;
	protected String resourceType = null;
	protected String tenant = null;
	protected String domain = null;
	protected static String seleniumURL = null;
	protected DefaultSelenium selenium;

	// List of different modules uses by the test cases
	AdminConsoleOrganizationsPage adminConsoleOrgPage;
	LoginPage loginPage;
	
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
		adminConsoleOrgPage = new AdminConsoleOrganizationsPage(selenium);
		loginPage = new LoginPage(selenium);
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
		if(seleniumServer == null) {
			seleniumServer = System.getenv(seleniumserverenv);
			assertTrue("Need the environment variable '" + seleniumserverenv + "' set.", seleniumServer != null);
		}
		if(seleniumPort == -1) {
			String tmp = System.getenv(seleniumportenv);
			assertTrue("Need the environment variable '" + seleniumportenv + "' set.", tmp != null);
			seleniumPort = Integer.parseInt(tmp);
		}
		if(seleniumBrowserType == null) {
			seleniumBrowserType = System.getenv(seleniumbrowserenv);
			assertTrue("Need the environment variable '" + seleniumbrowserenv + "' set.", seleniumBrowserType != null);
		}
		if(seleniumURL == null) {
			String tmp = System.getenv(seleniumresoureceenv);
			assertTrue("Need the environment variable '" + seleniumresoureceenv + "' set.", tmp != null);
			seleniumURL = tmp + "://";
			tmp = System.getenv(seleniumtenantenv);
			assertTrue("Need the environment variable '" + seleniumtenantenv + "' set.", tmp != null);
			seleniumURL += tmp + ".";
			tmp = System.getenv(seleniumdomainenv);
			assertTrue("Need the environment variable '" + seleniumdomainenv + "' set.", tmp != null);
			seleniumURL += tmp;
		}
	}
	
	/**
	 * This is called from the TestSuite in order to initial the environment
	 * from the test suite level. By calling this, it will override the
	 * environment variables.
	 * 
	 * @param server
	 * @param port
	 * @param browser
	 * @param url
	 */
	public static void setEnvironmentVariables(String server, int port, String browser, String url) {
		seleniumServer = server;
		seleniumPort = port;
		seleniumBrowserType = browser;
		seleniumURL = url;
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
