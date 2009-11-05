package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

public class LoginUnitTests extends FieldIDTestCase {

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
	
	@Test
	public void goToN4SystemsFromLoginPage() throws Exception {
		loginPage.gotoLoginPage();
		loginPage.gotoN4Systems();
	}
	
	@Ignore("Only used when testing production environment")
	@Test
	public void goToThawteSiteSealFromLoginPage() throws Exception {
		loginPage.gotoLoginPage("https://fieldid.fieldid.com/fieldid/");
		loginPage.gotoThawteCertificate();
	}
	
	@Test
	public void goToForgotPassword() throws Exception {
		loginPage.gotoLoginPage();
		forgotPasswordPage.gotoIForgotMyPassword();
	}
	
	@Test
	public void returnToLoginPageFromForgotPassword() throws Exception {
		loginPage.gotoLoginPage();
		forgotPasswordPage.gotoIForgotMyPassword();
		loginPage.gotoReturnToSignInFromForgotPassword();
	}
	
	@Test
	public void goToChooseACompany() throws Exception {
		loginPage.gotoLoginPage();
		chooseACompany.gotoNotTheCompanyIWant();
	}
	
	@Test
	public void returnToLoginPageFromChooseACompany() throws Exception {
		loginPage.gotoLoginPage();
		chooseACompany.gotoNotTheCompanyIWant();
		chooseACompany.setCompanyID(tenant);
		loginPage.gotoReturnToSignInFromChooseACompany();
	}
	
	@Test
	public void goToRequestAnAccount() throws Exception {
		loginPage.gotoLoginPage();
		registerNewUser.gotoRequestAnAccount();
	}
	
	@Test
	public void returnToLoginPageFromRequestAnAccount() throws Exception {
		loginPage.gotoLoginPage();
		registerNewUser.gotoRequestAnAccount();
		loginPage.gotoReturnToSignInFromRegisterNewUserPage();
	}
	
	@Test
	public void loginIn() throws Exception {
		loginPage.gotoLoginPage();
		String username = "n4systems";
		String password = "makemore$";
		loginPage.setUserName(username);
		loginPage.setPassword(password);
		homePage.gotoSignInUserName();
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
