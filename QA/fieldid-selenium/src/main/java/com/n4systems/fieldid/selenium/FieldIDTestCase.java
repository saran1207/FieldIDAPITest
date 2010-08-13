package com.n4systems.fieldid.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.After;
import org.junit.Before;

import com.n4systems.fieldid.selenium.lib.DefaultFieldIdSelenium;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public abstract class FieldIDTestCase extends SeleneseTestBase {
	
	public static boolean runningInsideSuite = false;
	private static Map<String, FieldIdSelenium> tenantToSelenium = new HashMap<String, FieldIdSelenium>();

	// NOTE: if you have -Dfieldid-companyid=unirope tests will default to unirope
	// Otherwise, they will default to fieldid.

	private String host = System.getProperty("selenium-server", "localhost");
	private int port = Integer.parseInt(System.getProperty("selenium-port", "4444"));
//	private String snapshots = System.getProperty("selenium-snapshots", "C:\\selenium-snapshots\\");
	private String browser = System.getProperty("fieldid-browser", "*firefox");
	private String protocol = System.getProperty("fieldid-protocol", "http");
	private String initCompany = System.getProperty("fieldid-companyid", "n4");
	private String domain = System.getProperty("fieldid-domain", "team.n4systems.net");
	private String contextRoot = System.getProperty("fieldid-contextroot", "/fieldid/");
	private String actionDelay = System.getProperty("fieldid-delay", null);
	private String supportFileLocation = System.getProperty("supportFileLocation", "file:///T:");

    public static FieldIdSelenium selenium;
	protected MiscDriver misc;
	protected Properties p;
	public static final String badProperty = "INVALID";
	public SystemDriverFactory systemDriverFactory;
	
	protected void setInitialCompany(String initCompany) {
		this.initCompany = initCompany;
	}
	
	@Before
	public final void setupSelenium() {
		loadProperties();
		if (!runningInsideSuite) {
			selenium = createWebBrowser();
		} else if (selenium == null) {
			selenium = createWebBrowser();
		}
		openBaseSite(selenium);
		setWebBrowserSpeed();
		initializeSystemDrivers();
	}
	
	private void openBaseSite(Selenium selenium) {
		String url = generateUrl();
		selenium.open(url);
		selenium.open(contextRoot);
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		selenium.windowMaximize();
	}
	
	protected FieldIdSelenium createOpenedWebBrowser() {
		FieldIdSelenium sel = createWebBrowser();
		openBaseSite(sel);
		return sel;
	}

	@After
	public final void tearDownSelenium() throws Exception {
		if (!runningInsideSuite) {
			shutDownSelenium(selenium);
			selenium = null;
		} else {
			selenium.deleteAllVisibleCookies();
		}
	}
	
	public static void shutDownAllSeleniums() {
		shutDownSelenium(selenium);
	}

	protected static void shutDownSelenium(FieldIdSelenium selenium) {
		selenium.close();
		selenium.stop();
	}

	private void initializeSystemDrivers() {
		systemDriverFactory = new SystemDriverFactory(selenium);
		misc = systemDriverFactory.createMiscDriver();
	}

	/**
	 * Uses the selenium.setSpeed(milliseconds) to slow the system down
	 * if the actionDelay variable is set. This variable is set via the
	 * property fieldid-delay. If this property is not set there will be
	 * no delay.
	 */
	private void setWebBrowserSpeed() {
		if(actionDelay != null) {
			selenium.setSpeed(actionDelay);
		}
	}

	/**
	 * If a property file exists, load the key/value pairs
	 * into the Test accessible variable p. These properties
	 * are not available to the libraries. The name of the
	 * property file will be the name of the class running
	 * the test. For example,
	 * 
	 * 		class SmokeTests {
	 * 			@Test
	 * 			public void Some_Test() {
	 * 				// your code here
	 * 			}
	 * 		}
	 * 
	 * The property file will be SmokeTests.properties.
	 */
	private void loadProperties() {
		try {
			p = new Properties();
			// if className.properties exists, load it
			String propertyFile = getClass().getSimpleName() + ".properties";
			File f = new File(propertyFile);
			if(f.exists()) {
				InputStream in = new FileInputStream(propertyFile);
				p.load(in);
				in.close();
			}
		} catch(Exception e) {
			fail("Error while loading properties file.");
			e.printStackTrace();
		}
	}
	
	/**
	 * If a System property exists, this will return the System property.
	 * Otherwise it will check to see if the property was loaded by the
	 * loadProperties() method. If the key is not set this will cause the
	 * test case to <STRONG>fail</STRONG>.
	 * 
	 * @param key
	 * @return the associated value for the given key.
	 * @see loadingProperties
	 */
	public String getStringProperty(String key) {
		String result = System.getProperty(key, badProperty);
		if(result.equals(badProperty)) {
			result = p.getProperty(key, badProperty);
		}
		if(result.equals(badProperty)) {
			fail("The key '" + key + "' was missing or set incorrectly.");
		}
		return result;
	}
	
	/**
	 * If a System property exists, this will return the System property.
	 * Otherwise it will check to see if the property was loaded by the
	 * loadProperties() method. If the key is not set it will cause the
	 * test case to <STRONG>fail</STRONG>.
	 * 
	 * If the key is set to something other than "true" this will
	 * return <STRONG>false</STRONG>.
	 * 
	 * @param key
	 * @return the key value or false if not set or set incorrectly.
	 * @see loadingProperties
	 */
	public boolean getBooleanProperty(String key) {
		String s = System.getProperty(key, badProperty);
		if(s.equals(badProperty)) {
			s = p.getProperty(key, badProperty);
		}
		if(s.equals(badProperty)) {
			fail("The key '" + key + "' was missing or set incorrectly.");
		}
		return Boolean.parseBoolean(s);
	}
	
	/**
	 * If a System property exists, this will return the System property.
	 * Otherwise it will check to see if the property was loaded by the
	 * loadProperties() method. If the key is not set or it is not a valid
	 * integer this will cause the test case to <STRONG>fail</STRONG>.
	 * 
	 * @param key
	 * @return the key value or fail if not set or set incorrectly.
	 * @see loadingProperties
	 */
	public int getIntegerProperty(String key) {
		String s = System.getProperty(key, badProperty);
		if(s.equals(badProperty)) {
			s = p.getProperty(key, badProperty);
		}
		if(s.equals(badProperty)) {
			fail("The key '" + key + "' was missing.");
		}
		int i = -1;
		try {
			i = Integer.parseInt(s);
		} catch(NumberFormatException nfe) {
			fail("The key '" + key + "' is not a valid integer.");
		}
		return i;
	}

    public static void assertEquals(Map<String, String> expected, Map<String, String> actual) {
    	boolean equals = expected.equals(actual);
    	if(!equals) {
	    	StringBuffer mismatchKeys = new StringBuffer("\n");
	    	StringBuffer failureMsg = new StringBuffer("\n\n");
	    	
	    	Set<String> expectedKeys = expected.keySet();
	    	Iterator<String> i = expectedKeys.iterator();
	    	while(i.hasNext()) {
	    		String expectedKey = i.next();
	    		String expectedValue = expected.get(expectedKey);
	    		if(!actual.containsKey(expectedKey)) {
	    			failureMsg.append("Expected key '");
	    			failureMsg.append(expectedKey);
	    			failureMsg.append("' was not found in Actual list of keys.\n");
	    		} else {
	    			String actualValue = actual.get(expectedKey);
	    			if(!actualValue.equals(expectedValue)) {
	    				mismatchKeys.append("For key '");
	    				mismatchKeys.append(expectedKey);
	    				mismatchKeys.append("' expected '");
	    				mismatchKeys.append(expectedValue);
	    				mismatchKeys.append("' but saw '");
	    				mismatchKeys.append(actualValue);
	    				mismatchKeys.append("'\n");
	    			}
	    		}
	    	}
	    	failureMsg.append(mismatchKeys.toString());
	    	failureMsg.append("\n");
	    	fail(failureMsg.toString());
    	}
    }
    
	/**
	 * Create an instance of Selenium with the pre-determined web browser
	 * pointing to the /fieldid/ application on the test machine. I also
	 * have this method maximize the window in case we want to do a screen
	 * capture or someone wants to watch the automation run.
	 */
	protected FieldIdSelenium createWebBrowser() {
		String url = generateUrl();
		FieldIdSelenium selenium = new DefaultFieldIdSelenium(new DefaultSelenium(host, port, browser, url));
		
		selenium.start();
		selenium.setTimeout(MiscDriver.DEFAULT_TIMEOUT);
		
		return selenium;
	}
	
	protected String generateUrl() {
		return protocol + "://" + initCompany + "." + domain;
	}
	
	/**
	 * Get the domain we are currently using. This is typically
	 * team.n4systems.net or grumpy.n4systems.net.
	 * 
	 * @return
	 */
	public String getFieldIDDomain() {
		return domain;
	}

	/**
	 * Get the protocol we are currently using. This is typically
	 * https but can be http as well.
	 * 
	 * @return
	 */
	public String getFieldIDProtocol() {
		return protocol;
	}

	/**
	 * Get the context root of the Field ID application. This is typically
	 * /fieldid/ but it can be /fieldidadmin/ or we might change the name
	 * entirely.
	 * 
	 * @return
	 */
	public String getFieldIDContextRoot() {
		return contextRoot;
	}

	/**
	 * The test suite will start up with whatever the System property
	 * fieldid-companyid is set to. If it is not set it will default to
	 * n4. If you need to change the company, use this method. This is
	 * helpful in situations were you need to log in and out as different
	 * tenants.
	 * 
	 * NOTE: if you need a tenant who is guaranteed to have a link to
	 * Plans and Pricing, use "msa".
	 * 
	 * @param companyID
	 */
	public void setCompany(String companyID) {
		String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() + getFieldIDContextRoot();
		selenium.deleteAllVisibleCookies();
		selenium.open(url);
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
	}
	
	public String getSupportFileLocation() {
		return supportFileLocation;
	}
	
}
