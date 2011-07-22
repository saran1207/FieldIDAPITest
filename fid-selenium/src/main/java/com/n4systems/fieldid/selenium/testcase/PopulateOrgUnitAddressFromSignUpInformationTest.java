package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.pages.EULAPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SetPasswordPage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.util.SignUpEmailLoginNavigator;

public class PopulateOrgUnitAddressFromSignUpInformationTest extends FieldIDTestCase {

    private static final String REFERRING_TENANT_NAME = TEST_TENANT_NAMES[0];
    private static final String NEW_TENANT_NAME = TEST_CREATED_TENANT_NAMES[0];
    private static final String NEW_USER = "testuser";
    private static final String NEW_PASSWORD = "testuser";

    private static final String TEST_ADDRESS = "123 Main St";
    private static final String TEST_CITY = "Townsville";
    private static final String TEST_STATE = "KY";
    private static final String TEST_COUNTRY = "United States";
    private static final String TEST_ZIP = "90210";
    private static final String TEST_PHONE = "555-OMG1";

    @Test
	public void shouldBePopulatingPrimaryOrganizationAddressWithInformationGivenAtAccountSetUp() throws Exception {
        createANewTenant();
        HomePage homePage = logIntoNewTenant();

        ManageOrganizationsPage orgsPage = homePage.clickSetupLink().clickManageOrganizations();
        orgsPage.clickEditPrimaryOrganization();

        assertEquals(TEST_ADDRESS, orgsPage.getCompanyAddress());
		assertEquals(TEST_CITY, orgsPage.getCompanyCity());
		assertEquals(TEST_STATE, orgsPage.getCompanyState());
		assertEquals(TEST_COUNTRY, orgsPage.getCompanyCountry());
		assertEquals(TEST_ZIP, orgsPage.getCompanyZip());
		assertEquals(TEST_PHONE, orgsPage.getCompanyPhoneNumber());
	}

	private HomePage logIntoNewTenant() {
        LoginPage loginPage = startAsCompany(NEW_TENANT_NAME);
        EULAPage eulaPage = loginPage.loginToEula(NEW_USER, NEW_PASSWORD);
        eulaPage.scrollToBottomOfEULA();
        return eulaPage.clickAcceptEULAToWizard().clickNoThanks();
	}

	private void createANewTenant() {
        SelectPackagePage selectPackagePage = startAsCompany(REFERRING_TENANT_NAME).clickPlansAndPricingLink();
        SignUpPage signUpPage = selectPackagePage.clickSignUpNowLink("Unlimited");

		TenantInfo t = new TenantInfo(NEW_USER, NEW_PASSWORD, NEW_TENANT_NAME, NEW_TENANT_NAME);
		t.setNumberOfUsers(2);
		t.setPhoneSupport(true);
		t.setPromoCode("promo");
		t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
		t.setPaymentType(TenantInfo.payByPurchaseOrder);
		t.setPurchaseOrderNumber("88888");
        t.setCompanyCountry(TEST_COUNTRY);
        t.setCompanyCity(TEST_CITY);
        t.setCompanyState(TEST_STATE);
        t.setCompanyAddress(TEST_ADDRESS);
        t.setCompanyPhoneNumber(TEST_PHONE);
        t.setCompanyZipCode(TEST_ZIP);

		signUpPage.enterCreateAccountForm(t);
		signUpPage.submitCreateAccountForm();

        mailServer.waitForMessages(1);
        MailMessage accountActivationMessage = mailServer.getAndClearMessages().get(0);

        SetPasswordPage page = new SignUpEmailLoginNavigator().navigateToSignInPageSpecifiedIn(accountActivationMessage, selenium);
        page.enterAndConfirmPassword(NEW_PASSWORD);
        page.submitConfirmPassword();
	}
}
