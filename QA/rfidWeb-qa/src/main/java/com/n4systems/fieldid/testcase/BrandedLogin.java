package com.n4systems.fieldid.testcase;

import junit.framework.TestCase;
import watij.runtime.ie.IE;
import static watij.finders.SymbolFactory.*;

/**
 * Tests the branded login for all the tenants.
 * 
 * @author Darrell
 *
 */
public class BrandedLogin extends TestCase {
	final static IE ie = new IE();
	final static String propFileDir = "src/main/java/com/n4systems/fieldid/testcase";
	final static commonFieldIDMethods helper = new commonFieldIDMethods(propFileDir, "BrandedLogin.properties");
	static boolean initialized = false;
	static String timestamp = null;

	/**
	 * There is a test case for each tenant. We want to keep them
	 * separate so if one tenant fails, it does not fail all the
	 * subsequent tenants as well.
	 */

	protected void setUp() throws Exception {
		super.setUp();
		// create a new directory for each test run but not for each test case
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	/**
	 * Checks the login and home page for a given tenant for all
	 * the appropriate information:
	 * 
	 * - No "Company ID" string
	 * - The text "Login in to account: ..." exists
	 *  - Text "Home" should be on the screen.
	 *  - Should be four links as noted below in the code.
	 *  - Should be a "New Features in ..." section.
	 *  - Should be an "Instructional Videos" section.
	 *  	- Might want to add checks for links to the videos as they are added.
	 *  - Should be a smart search (text, textfield, button)
	 *  
	 *  This will also capture the login and home page for visually
	 *  checking the logo and layout are okay.
	 * 
	 * @param tenant
	 * @throws Exception
	 */
	protected static void commonCode(String tenant) throws Exception {
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		
		/**
		 *  if someone is currently logged in with a different browser
		 */
		if(ie.link(text, "Logout").exists()) {
			helper.logout(ie);
		}
		ie.waitUntilReady();
		
		/**
		 *  Capture the screen and visually confirm the logo looks okay.
		 *  Prepend the file and with "login-" so a search of "login-*.png"
		 *  will find all the login screen captures.
		 */
		helper.myWindowCapture(timestamp + "/" + helper.getCaptureName("png").replaceFirst("^", "login-"), ie);
		
		/**
		 *  Confirm the "Company ID" string does not exist on the screen
		 *  Prior to Field ID 1.21 there was a non-branded login screen
		 *  which required you to input your company ID. We should never
		 *  see this again.
		 */
		assertTrue(!ie.containsText("/Company ID/"));
		
		/**
		 *  Confirm there is text "Login in to account: tenant"
		 *  where 'tenant' is the company ID for the current tenant.
		 */
		assertTrue(ie.containsText(tenant));

		/**
		 *  Log into the system so we can confirm the right display
		 *  for the current tenant as well.
		 */
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);
		ie.waitUntilReady();
		
		/**
		 *  Capture the screen once we logged in and visually confirm
		 *  everything looks good. We will prepend the filename with
		 *  "logged-in-" so a search of "logged-in-*.png" will give a
		 *  list of all the logged in screen shots.
		 */
		helper.myWindowCapture(timestamp + "/" + helper.getCaptureName("png").replaceFirst("^", "logged-in-"), ie);
		
		/**
		 *  Confirm we are on the Home page.
		 *  - Text "Home" should be on the screen.
		 *  - Should be four links as noted below in the code.
		 *  - Should be a "New Features in ..." section.
		 *  - Should be an "Instructional Videos" section.
		 *  	- Might want to add checks for links to the videos as they are added.
		 *  - Should be a smart search (text, textfield, button)
		 */
		assertTrue(ie.containsText("Home"));
		assertTrue(ie.link(text, "View upcoming Inspections").exists());
		assertTrue(ie.link(text, "View the Inspection History for a Product").exists());
		assertTrue(ie.link(text, "Find a Product").exists());
		assertTrue(ie.link(text, "Change your password").exists());
		assertTrue(ie.containsText("New Features in "));
		assertTrue(ie.containsText("Instructional Videos"));
		assertTrue(ie.containsText("Smart Search:"));
		assertTrue(ie.textField(id, "productInformation_search").exists());
		assertTrue(ie.button(id, "productInformation_load").exists());
	}

	public void testAllWay() throws Exception {
		commonCode("allway");
	}

	public void testBRS() throws Exception {
		commonCode("brs");
	}

	public void testCertex() throws Exception {
		commonCode("certex");
	}

	public void testCICB() throws Exception {
		commonCode("CICB");
	}

	public void testCGLift() throws Exception {
		commonCode("cglift");
	}

	public void testElko() throws Exception {
		commonCode("elko");
	}

	public void testHalo() throws Exception {
		commonCode("halo");
	}

	public void testHercules() throws Exception {
		commonCode("hercules");
	}

	public void testJergens() throws Exception {
		commonCode("jergens");
	}

	public void testJohnSakach() throws Exception {
		commonCode("johnsakach");
	}

	public void testKeyConstructors() throws Exception {
		commonCode("key");
	}

	public void testLCrane() throws Exception {
		commonCode("lcrane");
	}

	public void testMarine() throws Exception {
		commonCode("marine");
	}

	public void testN4() throws Exception {
		commonCode("n4");
	}

	public void testNISChain() throws Exception {
		commonCode("nischain");
	}

	public void testOceaneering() throws Exception {
		commonCode("oceaneering");
	}

	public void testPeakWorks() throws Exception {
		commonCode("PeakWorks");
	}

	public void testStellar() throws Exception {
		commonCode("stellar");
	}

	public void testSWWR() throws Exception {
		commonCode("swwr");
	}

	public void testTriState() throws Exception {
		commonCode("tristate");
	}

	public void testUnilift() throws Exception {
		commonCode("unilift");
	}

	public void testUnirope() throws Exception {
		commonCode("unirope");
	}

	public void testUTS() throws Exception {
		commonCode("UTS");
	}

	public void testWCWR() throws Exception {
		commonCode("wcwr");
	}

	public void testWiscoLift() throws Exception {
		commonCode("wiscolift");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		helper.stopMonitorStatus();
		ie.close();
	}
}
