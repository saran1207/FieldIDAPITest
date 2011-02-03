package com.n4systems.fieldid.selenium.testcase.referrer;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.pages.MyAccountPage;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReferrerTrackingTest extends FieldIDTestCase {
	
	@Test
	public void should_find_users_referral_link_on_refer_fieldid_tab_of_my_account() throws Exception {
        MyAccountPage myAccountPage = goToReferFieldId();
        String referralLink = myAccountPage.getReferralLink();
		
		assertReferrerLinkIsInTheCorrectForm(referralLink);
	}
	
	@Test
	public void should_allow_user_to_see_that_a_tenant_has_just_signed_up_through_their_referral() throws Exception {
        MyAccountPage myAccountPage = goToReferFieldId();

        String referralCode = myAccountPage.getReferralCode();
		int rowCount = myAccountPage.getNumberOfRowsInReferrerTable();

        SelectPackagePage packagesPage = gotoReferralLink("test1", referralCode);

        createANewUnlimitedTenant(packagesPage, "admin", "password");

        myAccountPage = goToReferFieldId();

		assertEquals(rowCount + 1, myAccountPage.getNumberOfRowsInReferrerTable());
	}

	@Test
	public void should_find_new_companies_name_in_the_list_of_referred_accounts() throws Exception {
        MyAccountPage myAccountPage = goToReferFieldId();

        String referralCode = myAccountPage.getReferralCode();
		
		SelectPackagePage packagesPage = gotoReferralLink("test1", referralCode);
		
		createANewUnlimitedTenant(packagesPage, "admin", "password");

		myAccountPage = goToReferFieldId();

        List<String> companyNamesInReferralTable = myAccountPage.getCompanyNamesInReferralTable();
        assertTrue(companyNamesInReferralTable.contains(TEST_CREATED_TENANT_NAMES[0]));
	}

	@Test
	public void should_not_count_referral_if_it_is_done_under_a_tenant_that_is_not_the_same_as_the_refering_user() throws Exception {
        MyAccountPage myAccountPage = goToReferFieldId();

        String referralLink = myAccountPage.getReferralLink();
		int rowCount = myAccountPage.getNumberOfRowsInReferrerTable();
        killSession();
		
		String aCompanyThatTheReferrerIsNotPartOf = "test2";
		SelectPackagePage packagesPage = gotoReferralLink(aCompanyThatTheReferrerIsNotPartOf, extractReferrerCodeFromUrl(referralLink));

		createANewUnlimitedTenant(packagesPage, "admin", "password");
		
		myAccountPage = goToReferFieldId();
		
		assertEquals(rowCount, myAccountPage.getNumberOfRowsInReferrerTable());
	}
	
	private TenantInfo createANewUnlimitedTenant(SelectPackagePage selectPackagePage, String username, String password) {
        String tenantName = TEST_CREATED_TENANT_NAMES[0];
		String tenantID = tenantName.toLowerCase();

        SignUpPage signUpPage = selectPackagePage.clickSignUpNowLink("Unlimited");

		TenantInfo t = new TenantInfo(username, password, tenantName, tenantID);
		
		t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
		t.setPaymentType(TenantInfo.payByPurchaseOrder);
		t.setPurchaseOrderNumber("88888");
		
		signUpPage.enterCreateAccountForm(t);
		signUpPage.submitCreateAccountForm();
		
		return t;
	}

	@Test
	public void should_hold_ref_code_to_sign_up_form_from_plans_and_pricing() throws Exception {
		String refcode = "somerefcode";
        SelectPackagePage packagePage = gotoReferralLink("test1", refcode);
        SignUpPage signUpPage = packagePage.clickSignUpNowLink("Free");
		assertEquals(refcode, signUpPage.getRefCode());
	}
	
	private MyAccountPage goToReferFieldId() {
        MyAccountPage myAccountPage = startAsCompany("test1").login().clickMyAccount();
        myAccountPage.clickReferFieldID();
        return myAccountPage;
	}

	private void assertReferrerLinkIsInTheCorrectForm(String referralLink) {
		Pattern linkMatcher = Pattern.compile("https?:\\/\\/.*\\/signup\\/(\\w)+$");
		assertTrue(linkMatcher.matcher(referralLink).matches());
	}
	
	private String extractReferrerCodeFromUrl(String referrerLink) {
		return referrerLink.split("\\/signup\\/")[1];
	}

}
