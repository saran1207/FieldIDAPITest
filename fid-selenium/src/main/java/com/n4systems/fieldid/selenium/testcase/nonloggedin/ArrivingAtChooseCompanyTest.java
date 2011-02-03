package com.n4systems.fieldid.selenium.testcase.nonloggedin;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.ChooseCompanyPage;
import org.junit.Test;

import static com.n4systems.fieldid.selenium.asserts.FieldIdAssert.assertSystemLogoIsUsed;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrivingAtChooseCompanyTest extends FieldIDTestCase {

	@Test
	public void should_not_show_warning_message_when_a_user_just_arrives_at_the_main_choose_company_page() throws Exception {
        ChooseCompanyPage chooseCompanyPage = startAtChooseCompany();
        assertSystemLogoIsUsed(selenium);
        assertFalse("No error messages should be present", chooseCompanyPage.isUnableToDetermineCompanyErrorDisplayed());
	}

	@Test
	public void should_show_warning_message_when_a_user_is_redirected_to_choose_company_page_when_they_enter_an_incorrect_tenant() throws Exception {
        ChooseCompanyPage chooseCompanyPage = startAsCompanyExpectingChoose("some-tenant-not-registered");

        assertTrue("error messages should be present", chooseCompanyPage.isUnableToDetermineCompanyErrorDisplayed());
	}

}
