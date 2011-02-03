package com.n4systems.fieldid.selenium.testcase.signup;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.util.ConfigEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TenantNameValidationTest extends PageNavigatingTestCase<SignUpPage> {

	private TenantInfo signUpForm;

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.updateConfigurationForTenant("test1", ConfigEntry.EXTERNAL_PLANS_AND_PRICING_ENABLED, "true");
    }

    @Override
    protected SignUpPage navigateToPage() {
        return startAsCompany("test1").clickPlansAndPricingLink().clickSignUpNowLink("Basic");
    }

    @Before
	public void setUp() {
		signUpForm = new TenantInfo("alex", "makemore$", "test Company", "a");
	}
	
	@Test
	public void should_see_the_tenant_name_too_short_gets_validation_error() throws Exception {
		signUpForm.setSiteAddress("aa");

		page.enterCreateAccountForm(signUpForm);
		page.submitCreateAccountForm();
        page.getFormErrorMessages().contains("The domain name must be at between 3 and 255");
	}
	
	@Test
	public void should_see_the_tenant_name_blank_gets_a_required_validation_error() throws Exception {
        signUpForm.setSiteAddress("");

        page.enterCreateAccountForm(signUpForm);
        page.submitCreateAccountForm();
        page.getFormErrorMessages().contains("You must choose a domain name");
	}
	
	@Test
	public void should_see_the_tenant_name_with_bad_characters_gets_a_validation_error() throws Exception {
		signUpForm.setSiteAddress("msachesnut.msanet");

        page.enterCreateAccountForm(signUpForm);
        page.submitCreateAccountForm();
        page.getFormErrorMessages().contains("The domain name is not in the correct format. You may only user letters, numbers and dashes");
	}
	
	@Test
	public void should_see_the_tenant_name_that_already_exists_in_the_system_gets_a_validation_error() throws Exception {
		signUpForm.setSiteAddress("fieldid");

        page.enterCreateAccountForm(signUpForm);
        page.submitCreateAccountForm();
        page.getFormErrorMessages().contains("This domain name is already in use");
	}
	
}
