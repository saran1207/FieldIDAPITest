package com.n4systems.fieldid.testcase;

import junit.framework.Test;
import junit.framework.TestSuite;

public class Regression {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.n4systems.fieldid.testcase");
		//$JUnit-BEGIN$
		suite.addTestSuite(ValidateHomePage.class);
		suite.addTestSuite(SmokeTestEx.class);
		//$JUnit-END$
		return suite;
	}

}
