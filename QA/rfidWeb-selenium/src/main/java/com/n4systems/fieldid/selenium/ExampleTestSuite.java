package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.n4systems.fieldid.selenium.testcase.TestStub;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ExampleTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Field ID");

		/**
		 * This is an example of setting up the environment.
		 * Rather than hard code the values here we can also
		 * make a test suite which reads the information from
		 * some other dynamic source or calls the same test
		 * cases with different information, over and over.
		 */
		String server = "localhost";
		int port = 4444;
		String browser = "*chrome";
		String url = "https://unirope.team.n4systems.com";
		FieldIDTestCase.setEnvironmentVariables(server, port, browser, url);
		
		//$JUnit-BEGIN$
		suite.addTestSuite(TestStub.class);
		//$JUnit-END$
		return suite;
	}

}
