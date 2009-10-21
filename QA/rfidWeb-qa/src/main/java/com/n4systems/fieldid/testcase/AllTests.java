package com.n4systems.fieldid.testcase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite to run all the test cases.
 * 
 * @author Darrell
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.n4systems.fieldid.testcase");
		//$JUnit-BEGIN$
		suite.addTestSuite(SmokeTestEx2.class);	// run smoke test first so it creates stuff for Validate to use
		suite.addTestSuite(Validate.class);
		suite.addTestSuite(Validate_2009_6_2.class);
		suite.addTestSuite(Validate_2009_7_0.class);
//		suite.addTestSuite(EmbeddedLogin.class);
//		//suite.addTestSuite(BrandedLogin.class);
//		suite.addTestSuite(EmployeePermissions.class);
//		suite.addTestSuite(WEB_186.class);
//		//suite.addTestSuite(WEB_252.class);	// timeout testing. takes over 3 hours to run
//		suite.addTestSuite(WEB_308.class);
//		suite.addTestSuite(WEB_363.class);
//		suite.addTestSuite(WEB_399.class);
//		//suite.addTestSuite(WEB_400.class);	// TODO: not fully implemented yet
//		suite.addTestSuite(WEB_44.class);	// print manufacturer certificates (heavy load, a lot of emails)
//		suite.addTestSuite(WEB_523.class);
//		suite.addTestSuite(WEB_527.class);
//		suite.addTestSuite(WEB_540.class);
//		suite.addTestSuite(WEB_541.class);
//		suite.addTestSuite(WEB_552.class);
//		suite.addTestSuite(WEB_554.class);
//		suite.addTestSuite(WEB_555.class);
//		suite.addTestSuite(WEB_562.class);
//		suite.addTestSuite(WEB_565.class);
//		suite.addTestSuite(WEB_573.class);
//		suite.addTestSuite(WEB_580.class);
//		suite.addTestSuite(WEB_584.class);
//		//suite.addTestSuite(WEB_665.class);	// Run against www.fieldid.com after releasing 2009.2
//		//suite.addTestSuite(WEB_667.class);	// Run against www.fieldid.com after releasing 2009.2
//		suite.addTestSuite(WEB_680.class);
//		suite.addTestSuite(WEB_703.class);
//		suite.addTestSuite(WEB_706.class);
//		suite.addTestSuite(WEB_709.class);
//		suite.addTestSuite(WEB_715.class);
//		suite.addTestSuite(WEB_722.class);
//		suite.addTestSuite(WEB_723.class);
//		suite.addTestSuite(WEB_738.class);
//		suite.addTestSuite(WEB_763.class);
//		suite.addTestSuite(WEB_773.class);
//		suite.addTestSuite(WEB_775.class);
//		suite.addTestSuite(WEB_782.class);
//		suite.addTestSuite(WEB_765.class);
//		suite.addTestSuite(WEB_772.class);
//		suite.addTestSuite(WEB_778.class);
//		suite.addTestSuite(WEB_785.class);
//		suite.addTestSuite(WEB_796.class);
//		suite.addTestSuite(WEB_813.class);
//		suite.addTestSuite(WEB_819.class);
//		suite.addTestSuite(WEB_826.class);
//		suite.addTestSuite(WEB_827.class);
		//$JUnit-END$
		return suite;
	}
}
