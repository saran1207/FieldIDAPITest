package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

public class TestStub extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void goToLoginPage() throws Exception {
		loginPage.gotoLoginPage();
	}
	
	@Test
	public void goToSignInWithSecurityRFIDNumber() throws Exception {
		loginPage.gotoLoginPage();
		loginPage.gotoSignInWithSecurityRFIDNumberLink();
	}
	
	@Test
	public void goToSignInWithUserName() throws Exception {
		loginPage.gotoLoginPage();
		loginPage.gotoSignInWithSecurityRFIDNumberLink();
		loginPage.gotoSignInWithUserNameLink();
	}
	
	@Ignore("Waiting for the unit test code to be completed")
	@Test
	public void loginFeatureTest() throws Exception {
		// put code here to use all LoginPage unit tests
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FieldIDTestCase.tearDownAfterClass();
	}
}
