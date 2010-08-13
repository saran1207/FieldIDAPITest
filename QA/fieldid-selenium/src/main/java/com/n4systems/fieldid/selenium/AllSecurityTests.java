package com.n4systems.fieldid.selenium;

import org.junit.runner.RunWith;

import com.n4systems.fieldid.selenium.PackageJunitRunner.AfterPackage;
import com.n4systems.fieldid.selenium.PackageJunitRunner.BeforePackage;
import com.n4systems.fieldid.selenium.PackageJunitRunner.SuitePackage;

@RunWith(PackageJunitRunner.class)
@SuitePackage("com.n4systems.fieldid.selenium.testcase")
public class AllSecurityTests {

	@BeforePackage
	public static void setupSeleniumForSuite() {
		FieldIDTestCase.runningInsideSuite = true;
	}
	
	@AfterPackage
	public static void teardownSelenium() {
		FieldIDTestCase.runningInsideSuite = false;
		FieldIDTestCase.shutDownAllSeleniums();
	}

}
