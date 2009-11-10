package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

import com.n4systems.fieldid.selenium.home.HomePage;
import com.n4systems.fieldid.selenium.login.*;
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
 * Additionally, we extend SeleneseTestBase as it implements all the various
 * assert statements from junit.framework.Test. The SeleneseTestCase and
 * SeleneseTestBase are based on jUnit3 so I have implemented the setUp() and
 * tearDown() methods here to be jUnit4 compatible.
 * 
 * @author Darrell Grainger
 *
 */
public class FieldIDTestCase extends SeleneseTestBase {

	// My instance of selenium for all the classes to share.
    //protected DefaultSelenium selenium;

    // Some useful constants:
	public static final String pageLoadDefaultTimeout = "30000";	// give a page 30 seconds to load

	// All these environment variables need to be set
	protected static final String seleniumserverenv = 		"seleniumserver";
	protected static final String seleniumportenv = 		"seleniumport";
	protected static final String seleniumbrowserenv = 		"seleniumbrowser";
	protected static final String seleniumresoureceenv = 	"seleniumresource";
	protected static final String seleniumdomainenv = 		"seleniumdomain";
	protected static final String seleniumtenantenv = 		"seleniumtenant";
	protected static final String seleniumdebugenv = 		"seleniumdebug";
	
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
	protected static String tenant = null;
	protected static String domain = null;
	protected static String seleniumURL = null;
	protected static boolean seleniumdebug = false;

	// List of different modules uses by the test cases
	AdminConsoleOrganizationsPage adminConsoleOrgPage;
	LoginPage loginPage;
	ForgotPasswordPage forgotPasswordPage;
	SendPasswordPage sendPasswordPage;
	ChooseACompanyPage chooseACompany;
	RegisterNewUserPage registerNewUser;
	HomePage homePage;
	SignUpPackagesPage signUpPackages;
	SignUpAddPage signUpAdd;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		// Initial all the required environment variables
		initEnvironmentVariables();
		initSeleniumInstance();
		startSeleniumBrowser();
		
		initLibriaries();
	}

	private void startSeleniumBrowser() {
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
		forgotPasswordPage = new ForgotPasswordPage(selenium);
		sendPasswordPage = new SendPasswordPage(selenium);
		chooseACompany = new ChooseACompanyPage(selenium);
		registerNewUser = new RegisterNewUserPage(selenium);
		homePage = new HomePage(selenium);
		signUpPackages = new SignUpPackagesPage(selenium);
		signUpAdd = new SignUpAddPage(selenium);
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
		if(tenant == null) {
			tenant = System.getenv(seleniumtenantenv);
			assertTrue("Need the environment variable '" + seleniumtenantenv + "' set.", tenant != null);
		}
		if(domain == null) {
			domain = System.getenv(seleniumdomainenv);
			assertTrue("Need the environment variable '" + seleniumdomainenv + "' set.", domain != null);
		}
		if(seleniumURL == null) {
			String tmp = System.getenv(seleniumresoureceenv);
			assertTrue("Need the environment variable '" + seleniumresoureceenv + "' set.", tmp != null);
			seleniumURL = tmp + "://";
			seleniumURL += tenant + ".";
			seleniumURL += domain;
		}
		String s = System.getenv(seleniumdebugenv);
		if(s != null) {
			seleniumdebug = Boolean.parseBoolean(s);
		}
	}
	
	/**
	 * This is called from the TestSuite in order to initial the environment
	 * from the test suite level. By calling this, it will override the
	 * environment variables. If you don't want to change a value, pass in
	 * null for the strings and 0 for the integer.
	 * 
	 * @param server	selenium server name/ip
	 * @param port		selenium port address
	 * @param browser	selenium string for browser type, e.g. "*iehta"
	 * @param url		URL for application under test
	 * @param t			Field ID tenant
	 * @param d			Field ID domain
	 */
	public static void setEnvironmentVariables(String server, int port, String browser, String url, String t, String d) {
		if(server != null) {
			seleniumServer = server;
		}
		if(port != 0) {
			seleniumPort = port;
		}
		if(browser != null) {
			seleniumBrowserType = browser;
		}
		if(url != null) {
			seleniumURL = url;
		}
		if(t != null) {
			tenant = t;
		}
		if(d != null) {
			domain = d;
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
