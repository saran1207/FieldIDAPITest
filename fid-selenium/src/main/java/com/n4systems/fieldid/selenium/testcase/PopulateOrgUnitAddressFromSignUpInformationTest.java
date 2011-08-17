package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.pages.EULAPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SetPasswordPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminCreateTenantPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.util.SignUpEmailLoginNavigator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        AdminCreateTenantPage createTenantPage = startAdmin().login().clickCreateANewTenant();

        createTenantPage.enterName(NEW_TENANT_NAME);
        createTenantPage.enterUserId(NEW_USER);
        createTenantPage.enterFirstName("First");
        createTenantPage.enterLastName("Last");
        createTenantPage.enterEmailAddress("at@dot.com");

        createTenantPage.selectPackage("Unlimited");
        createTenantPage.enterNumEmployeeUsers(2);
        createTenantPage.enterPrimaryOrgName("POrgName");
        createTenantPage.enterPrimaryNetsuiteUser("nsuser");
        createTenantPage.enterPrimaryNetsuitePassword("nspassword");
        createTenantPage.enterPrimaryOrgCountry(TEST_COUNTRY);
        createTenantPage.enterPrimaryOrgCity(TEST_CITY);
        createTenantPage.enterPrimaryOrgState(TEST_STATE);
        createTenantPage.enterPrimaryOrgAddress(TEST_ADDRESS);
        createTenantPage.enterPrimaryOrgPhone(TEST_PHONE);
        createTenantPage.enterPrimaryOrgZip(TEST_ZIP);

        createTenantPage.submitForm();
        createTenantPage.signOutAdmin();

        mailServer.waitForMessages(1);
        MailMessage accountActivationMessage = mailServer.getAndClearMessages().get(0);

        SetPasswordPage page = new SignUpEmailLoginNavigator().navigateToSignInPageSpecifiedIn(accountActivationMessage, selenium);
        page.enterAndConfirmPassword(NEW_PASSWORD);
        page.submitConfirmPassword();
	}
}
