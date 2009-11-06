package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

public class LoginNoPartnerCenterUnitTests extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void goToLoginPage() throws Exception {
		loginPage.gotoLoginPage();
	}
	
	@Test
	public void goToPlansAndPricing() throws Exception {
		goToLoginPage();
		signUpPackages.gotoPlansAndPricing();
	}
	
	@Test
	public void goToSignUpForFreePackage() throws Exception {
		goToPlansAndPricing();
		signUpAdd.gotoSignUpNowFree();
	}
	
	@Test
	public void goToChooseAnotherPackageFromSignUpForFreePackage() throws Exception {
		goToSignUpForFreePackage();
		signUpPackages.gotoChooseAnotherPackage();
	}
	
	@Test
	public void goToSignUpForBasicPackage() throws Exception {
		goToPlansAndPricing();
		signUpAdd.gotoSignUpNowBasic();
	}
	
	@Test
	public void goToChooseAnotherPackageFromSignUpForBasicPackage() throws Exception {
		goToSignUpForBasicPackage();
		signUpPackages.gotoChooseAnotherPackage();
	}
	
	@Test
	public void goToSignUpForPlusPackage() throws Exception {
		goToPlansAndPricing();
		signUpAdd.gotoSignUpNowPlus();
	}
	
	@Test
	public void goToChooseAnotherPackageFromSignUpForPlusPackage() throws Exception {
		goToSignUpForPlusPackage();
		signUpPackages.gotoChooseAnotherPackage();
	}
	
	@Test
	public void goToSignUpForEnterprisePackage() throws Exception {
		goToPlansAndPricing();
		signUpAdd.gotoSignUpNowEnterprise();
	}
	
	@Test
	public void goToChooseAnotherPackageFromSignUpForEnterprisePackage() throws Exception {
		goToSignUpForEnterprisePackage();
		signUpPackages.gotoChooseAnotherPackage();
	}
	
	@Test
	public void goToSignUpForUnlimitedPackage() throws Exception {
		goToPlansAndPricing();
		signUpAdd.gotoSignUpNowUnlimited();
	}
	
	@Test
	public void goToChooseAnotherPackageFromSignUpForUnlimitedPackage() throws Exception {
		goToSignUpForUnlimitedPackage();
		signUpPackages.gotoChooseAnotherPackage();
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
