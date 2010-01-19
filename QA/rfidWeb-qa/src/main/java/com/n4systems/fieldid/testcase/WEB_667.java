package com.n4systems.fieldid.testcase;

import junit.framework.TestCase;
import watij.elements.Links;
import watij.runtime.ie.IE;
import static watij.finders.FinderFactory.*;

/**
 * This checks to see if the indices for each tenant for each search area (Assets, Reporting and Schedule)
 * is working correctly.
 * 
 */
public class WEB_667 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	final static int productsPerResultPage = 20;
	final static int schedulesPerResultPage = 20;

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://www.team.n4systems.net/fieldid/");
//		helper.setBaseURL("https://www.fieldid.com/fieldid/");
		helper.setEndUser(false);
	}
	
	public void login(String tenant) throws Exception {
		String user = "n4systems";
		String password = "makemore$";
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
	}

	public void checkAssets() throws Exception {
		helper.gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		helper.gotoLastPageSearchResults(ie);
		helper.gotoPreviousPageSearchResults(ie);
		
		// If there is more than one page
		if(ie.link(text("Last")).exists()) {
			// Get a list of all the Info links. There should be one for each product
			Links products = ie.links(text("/Info/"));
			if(products.length() != productsPerResultPage) {
				String tenant = helper.getTenant();
				helper.myWindowCapture(tenant + "-check-Assets-Failed.png", ie);
				throw new TestCaseFailedException();
			}
		}
	}
	
	public void checkSchedules() throws Exception {
		helper.gotoScheduleSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		helper.gotoLastPageSearchResults(ie);
		helper.gotoPreviousPageSearchResults(ie);
		
		// If there is more than one page
		if(ie.link(text("/Last/")).exists()) {
			// Get a list of all the Info links. There should be one for each product
			Links schedules = ie.links(text("/view/"));
			if(schedules.length() != schedulesPerResultPage) {
				String tenant = helper.getTenant();
				helper.myWindowCapture(tenant + "-check-Schedules-Failed.png", ie);
				throw new TestCaseFailedException();
			}
		}
	}
	
	public void testHerculesAssetsIndexing() throws Exception {
		login("hercules");
		checkAssets();
	}

	public void testHerculesScheduleIndexing() throws Exception {
		login("hercules");
		checkSchedules();
	}
	
	public void testSWWRAssetsIndexing() throws Exception {
		login("swwr");
		checkAssets();
	}

	public void testSWWRScheduleIndexing() throws Exception {
		login("swwr");
		checkSchedules();
	}
	
	public void testNischainAssetsIndexing() throws Exception {
		login("nischain");
		checkAssets();
	}

	public void testNischainScheduleIndexing() throws Exception {
		login("nischain");
		checkSchedules();
	}
	
	public void testUniropeAssetsIndexing() throws Exception {
		login("unirope");
		checkAssets();
	}

	public void testUniropeScheduleIndexing() throws Exception {
		login("unirope");
		checkSchedules();
	}
	
	public void testCGLiftAssetsIndexing() throws Exception {
		login("cglift");
		checkAssets();
	}

	public void testCGLiftScheduleIndexing() throws Exception {
		login("cglift");
		checkSchedules();
	}
	
	public void testUniliftAssetsIndexing() throws Exception {
		login("unilift");
		checkAssets();
	}

	public void testUniliftScheduleIndexing() throws Exception {
		login("unilift");
		checkSchedules();
	}
	
	public void testCertexAssetsIndexing() throws Exception {
		login("certex");
		checkAssets();
	}

	public void testCertexScheduleIndexing() throws Exception {
		login("certex");
		checkSchedules();
	}
	
	public void testHaloAssetsIndexing() throws Exception {
		login("halo");
		checkAssets();
	}

	public void testHaloScheduleIndexing() throws Exception {
		login("halo");
		checkSchedules();
	}
	
	public void testPeakWorksAssetsIndexing() throws Exception {
		login("PeakWorks");
		checkAssets();
	}

	public void testPeakWorksScheduleIndexing() throws Exception {
		login("PeakWorks");
		checkSchedules();
	}
	
	public void testWCWRAssetsIndexing() throws Exception {
		login("wcwr");
		checkAssets();
	}

	public void testWCWRScheduleIndexing() throws Exception {
		login("wcwr");
		checkSchedules();
	}
	
	public void testMarineAssetsIndexing() throws Exception {
		login("marine");
		checkAssets();
	}

	public void testMarineScheduleIndexing() throws Exception {
		login("marine");
		checkSchedules();
	}
	
	public void testBRSAssetsIndexing() throws Exception {
		login("brs");
		checkAssets();
	}

	public void testBRSScheduleIndexing() throws Exception {
		login("brs");
		checkSchedules();
	}
	
	public void testJohnSakachAssetsIndexing() throws Exception {
		login("johnsakach");
		checkAssets();
	}

	public void testJohnSakachScheduleIndexing() throws Exception {
		login("johnsakach");
		checkSchedules();
	}
	
	public void testLCraneAssetsIndexing() throws Exception {
		login("lcrane");
		checkAssets();
	}

	public void testLCraneScheduleIndexing() throws Exception {
		login("lcrane");
		checkSchedules();
	}
	
	public void testUTSAssetsIndexing() throws Exception {
		login("UTS");
		checkAssets();
	}

	public void testUTSScheduleIndexing() throws Exception {
		login("UTS");
		checkSchedules();
	}
	
	public void testElkoAssetsIndexing() throws Exception {
		login("elko");
		checkAssets();
	}

	public void testElkoScheduleIndexing() throws Exception {
		login("elko");
		checkSchedules();
	}
	
	public void testJergensAssetsIndexing() throws Exception {
		login("jergens");
		checkAssets();
	}

	public void testJergensScheduleIndexing() throws Exception {
		login("jergens");
		checkSchedules();
	}
	
	public void testHysafeAssetsIndexing() throws Exception {
		login("hysafe");
		checkAssets();
	}

	public void testHysafeScheduleIndexing() throws Exception {
		login("hysafe");
		checkSchedules();
	}
	
	public void testCICBAssetsIndexing() throws Exception {
		login("CICB");
		checkAssets();
	}

	public void testCICBScheduleIndexing() throws Exception {
		login("CICB");
		checkSchedules();
	}
	
	public void testOceaneeringAssetsIndexing() throws Exception {
		login("oceaneering");
		checkAssets();
	}

	public void testOceaneeringScheduleIndexing() throws Exception {
		login("oceaneering");
		checkSchedules();
	}
	
	public void testWiscoLiftAssetsIndexing() throws Exception {
		login("wiscolift");
		checkAssets();
	}

	public void testWiscoLiftScheduleIndexing() throws Exception {
		login("wiscolift");
		checkSchedules();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
