package com.n4systems.fieldid.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.*;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.*;

public class FieldIDTestCase extends SeleneseTestBase {

	// NOTE: if you have -Dfieldid-companyid=unirope tests will default to unirope
	// Otherwise, they will default to fieldid.

	/** 
	 * A test case will require the following:
	 * 
	 * variable		default						define				comment
	 * --------		-------						------				-------
	 * host					localhost					selenium-server		the computer running the selenium remote control (RC) server
	 * port					4444						selenium-port		the port the selenium remote control server is listening on
	 * snapshots			C:\\selenium-snapshots\\	selenium-snapshots	the root location for screen captures to be stored. 
	 * browser				*firefox					fieldid-browser		the selenium string for which browser to run the test case with
	 * protocol				http						fieldid-protocol	either 'http' or 'https' for which ever protocol to run fieldid with
	 * initCompany			fieldid						fieldid-companyid	the tenant to start fieldid with
	 * domain				team.n4systems.net			fieldid-domain		the domain, i.e. the computer running fieldid on
	 * contextRoot			/fieldid/					fieldid-context		the context root for the fieldid application
	 * actionDelay			null						fieldid-delay		the number of milliseconds to delay between selenium actions
	 * log4jConfig			log4j.xml					fieldid-log4j		the log4j configuration file to be used
	 * 
	 * The host and port are pretty straight forward. If I run Selenium RC on localhost
	 * listening on port 4444 then host = "localhost" and port = 4444. You can override
	 * the default values using command line -D options. For example, if the selenium
	 * server is running on port 5656 I can use the following on the command line to
	 * override the default:
	 * 
	 *  		-Dselenium-port=5656
	 *  
	 * or to change the default browser:
	 *  
	 *  		-Dfieldid-browser=*iehta
	 *  
	 * Remember to use quotes or escape things like * when using the command line.
	 * 
	 * The snapshots is the location for where to store screen captures. This is
	 * location will have the current timestamp appended to it. This way if you
	 * have multiple runs, the snapshots will not get overwritten. For MSDOS file
	 * paths, don't forget to escape the slash character in the path. There will
	 * be a different timestamp for each testcase.
	 * 
	 * The browser is defined by Selenium. At the time of creating this framework
	 * Selenium has the following:
	 * 
	 * 		*firefox		http
	 * 		*iexplore		http
	 * 		*chrome			https
	 * 		*iehta			https
	 * 		*pifirefox	
	 * 		*piiexplore
	 * 		*custom c:\\program files\\internet explorer\\iexplore.exe
	 * 
	 * Which you use depends on how the Selenium RC server is running. If
	 * it is running with https and non-proxy injection mode then *chrome
	 * or *iehta should be used. If you are using proxy injection mode then
	 * *piiexplore or *pifirefox. If you are using http then use *firefox
	 * and *iexplore. If you want to run a browser that is not supported,
	 * use the *custom option. See the Selenium documentation for more about
	 * the browser strings.
	 * 
	 * The rest the of variables are for building the URL for opening the
	 * browser. The default will be:
	 * 
	 * 		http://n4.team.n4systems.net/fieldid/
	 * 
	 * the general format is:
	 * 
	 * 		protocol + "://" + initCompany + "." + domain + contextRoot;
	 * 
	 * The fieldid-delay property should normally not be used. If you set
	 * this to a numeric value, it will cause selenium to delay, by the
	 * equivalent number of milliseconds, between each action. This property
	 * was designed to slow down the play back, in case you wanted to watch
	 * what was happening as it happened. Can be used for GUI testing. You
	 * set this to 2000, run the test suite and watch the browser for any GUI
	 * issues. You can set it to a higher amount but I have found 2 seconds
	 * between selenium actions is usually fast enough that you won't get
	 * bored but slow enough you can see what it is doing. Remember that some
	 * pages will have 3 or more selenium calls, so a 2 second delay might
	 * mean you are sitting on one page for 6 to 10 seconds. Obviously the
	 * more there is on the page for the code to 'view' and validate, the
	 * slow things will be BUT it probably means there is more on the page
	 * for you to check visually as well.
	 * 
	 * Additional things to know about FieldIDTestCase:
	 * 
	 * If the class extending FieldIDTestCase is called Foo then this
	 * code will attempt to load the property file Foo.properties. Then
	 * getStringProperty, getIntegerProperty, etc. will use the properties
	 * loaded from that file. If a property is not found it will default
	 * to the value of badProperty, currently set to "INVALID".
	 * 
	 * If a property exists as a System property, it will override the
	 * loaded properties. For example, if I have a property called 'userid'
	 * in the file Foo.properties but I also have:
	 * 
	 * 		-Duserid=n4systems
	 * 
	 * then userid will equal n4systems. Note: if multiple test cases have
	 * the same property, you can override all of them by setting the
	 * System property. This can be helpful or this can be a problem. Be
	 * careful.
	 * 
	 * The class variable misc can be used to log information. We are
	 * using log4j. The Misc class wraps calls to info(), debug(), etc.
	 * So test cases can output log messages using:
	 * 
	 * 		misc.debug("some debug message");
	 * 		misc.info("some message");
	 * 
	 * The log4j.xml file determines which level of logging to output.
	 * Set the priority value to which ever level suits you. Typically,
	 * I have been using debug for debugging a test case and info for
	 * printing the 'testcase' out.
	 * 
	 * The verify*() methods will not stop the execution of a test case.
	 * It will set a flag so that during tearDown for a test case it will
	 * send a failure to jUnit.
	 * 
	 */
	private String host = System.getProperty("selenium-server", "localhost");
	private int port = Integer.parseInt(System.getProperty("selenium-port", "4444"));
	private String snapshots = System.getProperty("selenium-snapshots", "C:\\selenium-snapshots\\");
	private String browser = System.getProperty("fieldid-browser", "*firefox");
	private String protocol = System.getProperty("fieldid-protocol", "http");
	private String initCompany = System.getProperty("fieldid-companyid", "n4");
	private String domain = System.getProperty("fieldid-domain", "grumpy.n4systems.net");
	private String contextRoot = System.getProperty("fieldid-contextroot", "/fieldid/");
	private String actionDelay = System.getProperty("fieldid-delay", null);
	private String log4jConfig = System.getProperty("fieldid-log4j", "log4j.xml");
	private String supportFileLocation = System.getProperty("supportFileLocation", "file:///T:");

	private SeleneseTestBase stb = new SeleneseTestBase();
    protected Selenium selenium;
	protected Misc misc;
	static Logger log = Logger.getLogger(FieldIDTestCase.class.getName());
	protected Properties p;
	public static final String badProperty = "INVALID";
	
	@Before
	public void setUp() throws Exception {
		initializeLogger();
		loadingProperties();
		log.info("-=-=-=-=-=-=-=-=-=-=- Start Test Case -=-=-=-=-=-=-=-=-=-=-");
		createWebBrowser();
		setWebBrowserSpeed();
		createMiscClasses();
		misc.createTimestampDirectory(snapshots);
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

	@After
	public void tearDown() throws Exception {
		log.debug("[tearDown]: selenium.close()");
		selenium.close();
		log.debug("[tearDown]: selenium.stop()");
		selenium.stop();
		log.debug("[tearDown]: super.tearDown()");
		log.info("-=-=-=-=-=-=-=-=-=-=-= End Test Case =-=-=-=-=-=-=-=-=-=-=-");
        stb.tearDown();
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
	private void loadingProperties() {
		InputStream in;
		String propertyFile = "";
		File f;
		try {
			p = new Properties();
			// if className.properties exists, load it
			propertyFile = getClass().getSimpleName() + ".properties";
			f = new File(propertyFile);
			if(f.exists()) {
				in = new FileInputStream(propertyFile);
				p.load(in);
				in.close();
			}
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing the test case");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch(Exception e) {
			fail("Unknown error while loading properties file.");
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
		boolean result = false;
		String s = System.getProperty(key, badProperty);
		if(s.equals(badProperty)) {
			s = p.getProperty(key, badProperty);
		}
		if(s.equals(badProperty)) {
			fail("The key '" + key + "' was missing or set incorrectly.");
		}
		result = Boolean.parseBoolean(s);
		return result;
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

    /** Asserts that there were no verification errors during the current test, failing immediately if any are found */
    public void checkForVerificationErrors() {
        stb.checkForVerificationErrors();
    }
    
    /** Clears out the list of verification errors */
    public void clearVerificationErrors() {
        stb.clearVerificationErrors();
    }
    
    /** Returns the body text of the current page */
    public String getText() {
        return stb.getText();
    }
    
    /** Sleeps for the specified number of milliseconds */
    public void pause(int millisecs) {
        stb.pause(millisecs);
    }
    
    /** Like assertEquals, but fails at the end of the test (during tearDown) */
    public void verifyEquals(boolean arg1, boolean arg2) {
        stb.verifyEquals(arg1, arg2);
    }
    
    /** Like assertEquals, but fails at the end of the test (during tearDown) */
    public void verifyEquals(Object s1, Object s2) {
        stb.verifyEquals(s1, s2);
    }
    
    /** Like assertEquals, but fails at the end of the test (during tearDown) */
    public void verifyEquals(String[] s1, String[] s2) {
        stb.verifyEquals(s1, s2);
    }
    
    /** Like assertFalse, but fails at the end of the test (during tearDown) */
    public void verifyFalse(boolean b) {
        stb.verifyFalse(b);
    }
    
    /** Like assertNotEquals, but fails at the end of the test (during tearDown) */
    public void verifyNotEquals(boolean s1, boolean s2) {
        stb.verifyNotEquals(s1, s2);
    }
    
    /** Like assertNotEquals, but fails at the end of the test (during tearDown) */
    public void verifyNotEquals(Object s1, Object s2) {
        stb.verifyNotEquals(s1, s2);
    }
    
    /** Like assertTrue, but fails at the end of the test (during tearDown) */
    public void verifyTrue(boolean b) {
        stb.verifyTrue(b);
    }

    /** Like JUnit's assertEquals, but knows how to compare string arrays */
    public static void assertEquals(Object s1, Object s2) {
        SeleneseTestBase.assertEquals(s1, s2);
    }
    
    /** Like JUnit's assertEquals, but handles "regexp:" strings like HTML Selenese */
    public static void assertEquals(String s1, String s2) {
        SeleneseTestBase.assertEquals(s1, s2);
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
    
    /** Like JUnit's assertEquals, but joins the string array with commas, and 
     * handles "regexp:" strings like HTML Selenese
     */
    public static void assertEquals(String s1, String[] s2) {
        SeleneseTestBase.assertEquals(s1, s2);
    }
    
    /** Asserts that two string arrays have identical string contents */
    public static void assertEquals(String[] s1, String[] s2) {
        SeleneseTestBase.assertEquals(s1, s2);
    }
    
    /** Asserts that two booleans are not the same */
    public static void assertNotEquals(boolean b1, boolean b2) {
        SeleneseTestBase.assertNotEquals(b1, b2);
    }
    
    /** Asserts that two objects are not the same (compares using .equals()) */
    public static void assertNotEquals(Object obj1, Object obj2) {
        SeleneseTestBase.assertNotEquals(obj1, obj2);
    }
    
    /** Compares two objects, but handles "regexp:" strings like HTML Selenese
     * @see #seleniumEquals(String, String)
     * @return true if actual matches the expectedPattern, or false otherwise
     */
    public static boolean seleniumEquals(Object expected, Object actual) {
        return SeleneseTestBase.seleniumEquals(expected, actual);
    }
    
    /** Compares two strings, but handles "regexp:" strings like HTML Selenese
     * 
     * @param expectedPattern
     * @param actual
     * @return true if actual matches the expectedPattern, or false otherwise
     */
    public static boolean seleniumEquals(String expected, String actual) {
        return SeleneseTestBase.seleniumEquals(expected, actual);
    }
    
    protected boolean isCaptureScreenShotOnFailure() {
        return isCaptureScreenShotOnFailure();
    }
    
    protected String runtimeBrowserString() {
        return runtimeBrowserString();
    }
    
    protected void setCaptureScreenShotOnFailure(boolean b) {
        setCaptureScreenShotOnFailure(b);
    }
    
    protected void setTestContext() {
        selenium.setContext(getClass().getSimpleName() + "." + getClass().getName());
    }
    	
	/**
	 * The Misc object is a group of methods which should be available anywhere.
	 * In some cases these are utility methods like generating a random RFID
	 * number. In other cases it is an interface to Log4j. It is also all the
	 * functionality which exists in the header or footer of the Field ID web
	 * pages. Things like the Home, Identify, Inspect, etc. icons or the Sign Out
	 * and My Account links. 
	 */
	private void createMiscClasses() {
		log.debug("[setUp]: Initializing Misc object");
		misc = new Misc(selenium, log);
	}

	/**
	 * Create an instance of Selenium with the pre-determined web browser
	 * pointing to the /fieldid/ application on the test machine. I also
	 * have this method maximize the window in case we want to do a screen
	 * capture or someone wants to watch the automation run.
	 */
	private void createWebBrowser() {
		String url = protocol + "://" + initCompany + "." + domain;
		log.debug("[setUp]: Selenium Host: " + host);
		log.debug("[setUp]: Selenium Port: " + port);
		log.debug("[setUp]: Browser: " + browser);
		log.debug("[setUp]: Base URL: " + url);
		selenium = new DefaultSelenium(host, port, browser, url);
		selenium.start();
		selenium.setTimeout(Misc.defaultTimeout);
		log.debug("[setUp]: Open " + contextRoot);
		log.info("Open web browser '" + browser + "' to '" + url + contextRoot + "'");
		selenium.open(contextRoot);
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		log.debug("[setUp]: Maximizing browser window");
		selenium.windowMaximize();
	}

	/**
	 * Configure the log4j logger using an XML file.
	 */
	private void initializeLogger() {
		DOMConfigurator.configure(log4jConfig);
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
		misc.info("Changing to company '" + companyID + "'");
		String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() + getFieldIDContextRoot();
		selenium.open(url);
		selenium.waitForPageToLoad(Misc.defaultTimeout);
	}
	
	
	public String getSupportFileLocation() {
		return supportFileLocation;
	}
	
	protected void waitForAjax() throws InterruptedException {
        selenium.waitForCondition("selenium.browserbot.getCurrentWindow().Ajax.activeRequestCount == 0;", Misc.defaultTimeout);
	}
}
