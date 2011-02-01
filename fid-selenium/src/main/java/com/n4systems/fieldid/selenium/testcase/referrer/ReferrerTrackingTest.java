package com.n4systems.fieldid.selenium.testcase.referrer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ReferrerTrackingTest extends FieldIDTestCase {
	
	@Test
	public void should_find_users_referral_link_on_refer_fieldid_tab_of_my_account() throws Exception {
		goToReferFieldId();
		String referralLink = getReferralLink();
		
		assertReferrerLinkIsInTheCorrectForm(referralLink);
	}
	
	@Test
	public void should_send_us_to_plans_and_pricing_when_we_go_to_the_sign_up_link() throws Exception {
		selenium.open("/signup/somerefcode");
	}
	
	@Test
	public void should_allow_user_to_see_that_a_tenant_has_just_siged_up_through_their_referral() throws Exception {
		goToReferFieldId();
		
		String referralLink = getReferralLink();
		int rowCount = getNumberOfRowsInReferrerTable();
		
		signOutAndGoToReferalLink(referralLink);
		
		createANewUnlimitedTenant("admin", "password");


		goToReferFieldId();
		
		assertTrue(selenium.isElementPresent("css=#referralsBottom table"));
		assertEquals(rowCount + 1, getNumberOfRowsInReferrerTable());
	}

	@Test
	public void should_find_new_companies_name_in_the_list_of_referred_accounts() throws Exception {
		goToReferFieldId();
		
		String referralLink = getReferralLink();
		
		signOutAndGoToReferalLink(referralLink);
		
		TenantInfo t = createANewUnlimitedTenant("admin", "password");
		
		goToReferFieldId();
		
		assertTrue(selenium.isElementPresent("css=#referralsBottom table"));
		assertTrue(selenium.isTextPresent(t.getCompanyName()));
	}

	private String getReferralLink() {
		return selenium.getText("css=#referralLinkBox p");
	}

	private void signOutAndGoToReferalLink(String referralLink) {
		selenium.open("/fieldid/logout.action");
		misc.waitForPageToLoadAndCheckForOopsPage();
		selenium.open(referralLink);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}
	
	@Test
	public void should_not_count_referral_if_it_is_done_under_a_tenant_that_is_not_the_same_as_the_refering_user() throws Exception {
		goToReferFieldId();
		
		String referralLink = getReferralLink();
		int rowCount = getNumberOfRowsInReferrerTable();
		
		selenium.open("/fieldid/logout.action");
		misc.waitForPageToLoadAndCheckForOopsPage();
		selenium.deleteAllVisibleCookies();
		
		String aCompanyThatTheReferrerIsNotPartOf = "msa";
		gotoReferralLink(aCompanyThatTheReferrerIsNotPartOf, extractReferrerCodeFromUrl(referralLink));
		Thread.sleep(10000);
		
		createANewUnlimitedTenant("admin", "password");
		
		goToReferFieldId();
		
		assertTrue(selenium.isElementPresent("css=#referralsBottom table"));
		assertEquals(rowCount, getNumberOfRowsInReferrerTable());
	}
	
	private int getNumberOfRowsInReferrerTable() {
		return selenium.getXpathCount("//div[@id='referralsBottom']/table//tr").intValue();
	}
	
	private TenantInfo createANewUnlimitedTenant(String username, String password) {
		String tenantName = MiscDriver.getRandomString(8);
//        String tenantName = TEST_CREATED_TENANT_NAMES[0];
		String tenantID = tenantName.toLowerCase();

		CreateAccount create = new CreateAccount(selenium, misc);

		SignUpPackages sup = new SignUpPackages(selenium, misc);
		
		String packageName = "Unlimited";	// must be unlimited
		sup.gotoSignUpNow(packageName);
		
		create.verifyCreateAccountPage(packageName);

		TenantInfo t = new TenantInfo(username, password, tenantName, tenantID);
		
		t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
		t.setPaymentType(TenantInfo.payByPurchaseOrder);
		t.setPurchaseOrderNumber("88888");
		
		create.setCreateYourAccountForm(t);
		create.submitCreateYourAccountForm();
		
		return t;
	}

	@Test
	public void should_hold_ref_code_to_sign_up_form_from_plans_and_pricing() throws Exception {
		String refcode = "somerefcode";
		selenium.open("/fieldid/public/signUpPackages.action?refCode=" + refcode);
		misc.waitForPageToLoadAndCheckForOopsPage();
		new SignUpPackages(selenium, misc).gotoSignUpNow("Free");
		assertEquals(refcode, selenium.getValue("css=#refCode"));
	}
	
	private void goToReferFieldId() {
		selenium.open("/fieldid/login.action");
		Login loginPage = new Login(selenium, misc);
		
		loginPage.signInWithSystemAccount();
		
		selenium.open("/fieldid/refer.action");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
	}

	private void assertReferrerLinkIsInTheCorrectForm(String referralLink) {
		Pattern linkMatcher = Pattern.compile("https?:\\/\\/.*\\/signup\\/(\\w)+$");
		assertTrue(linkMatcher.matcher(referralLink).matches());
	}
	
	private String extractReferrerCodeFromUrl(String referrerLink) {
		return referrerLink.split("\\/signup\\/")[1];
	}

}
