package com.n4systems.fieldid.testcase;

import static watij.finders.FinderFactory.text;
import watij.elements.Links;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Check that the indexing for Reporting Search Results is working properly.
 * 
 * @author dgrainge
 *
 */
public class WEB_665 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	final static int reportsPerResultPage = 20;
	static boolean initialized = false;
	static String timestamp = null;

	protected void setUp() throws Exception {
		super.setUp();
//		helper.setBaseURL("https://www.fieldid.com/fieldid/");
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void login(String tenant) throws Exception {
		String user = "n4systems";
		String password = "Xk43g8!@";
		helper.setUserName(user);
		helper.setPassword(password);
		helper.setTenant(tenant);
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedDefaultRegular(ie, false);
	}

	public void checkReporting() throws Exception {
		helper.gotoReportSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		helper.gotoLastPageSearchResults(ie);
		helper.gotoPreviousPageSearchResults(ie);
		
		// If there is more than one page
		if(ie.link(text("/Last/")).exists()) {
			// Get a list of all the Info links. There should be one for each product
			Links reports = ie.links(text("/View/"));
			if(reports.length() != reportsPerResultPage) {
				String tenant = helper.getTenant();
				helper.myWindowCapture(tenant + "-check-Reporting-Failed.png", ie);
				throw new TestCaseFailedException();
			}
		}
	}
	
	public void testHerculesReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("hercules");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testSWWRReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("SWWR");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testNischainReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("nischain");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testUniropeReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("unirope");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testCGLiftReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("cglift");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testUniliftReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("unilift");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testCertexReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("certex");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testHaloReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("halo");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testPeakWorksReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("PeakWorks");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testWCWRReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("wcwr");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testMarineReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("marine");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testBRSReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("brs");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testJohnSakachReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("johnsakach");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testLCraneReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("lcrane");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testUTSReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("UTS");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testElkoReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("elko");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testJergensReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("jergens");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testHysafeReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("hysafe");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testCICBReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("CICB");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testOceaneeringReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("oceaneering");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	public void testWiscoLiftReportingIndexing() throws Exception {
		String method = helper.getMethodName();

		try {
			login("wiscolift");
			checkReporting();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "FAILURE-" + method + ".png", ie);
			throw e;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
