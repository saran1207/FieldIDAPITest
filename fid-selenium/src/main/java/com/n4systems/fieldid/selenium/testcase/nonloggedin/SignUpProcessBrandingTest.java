package com.n4systems.fieldid.selenium.testcase.nonloggedin;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.util.ConfigEntry;
import org.junit.Test;

public class SignUpProcessBrandingTest extends FieldIDTestCase {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.updateConfigurationForTenant("test1", ConfigEntry.EXTERNAL_PLANS_AND_PRICING_ENABLED, "true");
    }

	@Test
	public void should_have_a_system_logo_on_plans_and_pricing_page() throws Exception {
        SelectPackagePage packagePage = startAsCompany("test1").clickPlansAndPricingLink();
        packagePage.assertSystemLogoIsUsed();
	}
	
	@Test
	public void should_have_a_system_logo_on_sign_up_form() throws Exception {
        SelectPackagePage packagePage = startAsCompany("test1").clickPlansAndPricingLink();
        SignUpPage signUpPage = packagePage.clickSignUpNowLink("Free");
        signUpPage.assertSystemLogoIsUsed();
	}
	
}
