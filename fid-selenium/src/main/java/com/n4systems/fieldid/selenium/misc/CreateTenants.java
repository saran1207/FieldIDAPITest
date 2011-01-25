package com.n4systems.fieldid.selenium.misc;

import static org.junit.Assert.assertNotNull;

import java.util.Random;

import com.n4systems.fieldid.selenium.datatypes.TenantInfo;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.login.page.CreateAccount;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.login.page.SignUpComplete;
import com.n4systems.fieldid.selenium.login.page.SignUpPackages;
import com.n4systems.fieldid.selenium.mail.MailMessage;
import com.n4systems.fieldid.selenium.mail.MailServer;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SetPasswordPage;
import com.n4systems.fieldid.selenium.util.SignUpEmailLoginNavigator;
import com.thoughtworks.selenium.Selenium;

public class CreateTenants {

    private Selenium selenium;

	private Login login;
	private SignUpPackages sup;
	private CreateAccount create;
	private SignUpComplete complete;
	private Random r;

	public CreateTenants(FieldIdSelenium selenium, MiscDriver misc) {
		this.r = new Random();
        this.selenium = selenium;
		login = new Login(selenium, misc);
		sup = new SignUpPackages(selenium, misc);
		create = new CreateAccount(selenium, misc);
		complete = new SignUpComplete(selenium, misc);
	}

	/**
	 * Create a random new tenant and goes to their login page.
	 * All the information about the tenant is returned in a
	 * CreateTenant object.
	 * 
	 *
     * @param packageName
     * @param promoCode
     * @param mailServer
     * @return
	 */
	public TenantInfo createARandomNewTenant(String packageName, String promoCode, MailServer mailServer) {
		assertNotNull("Need a package name", packageName);
		if(promoCode == null) {
			promoCode = "";
		}
		String username = MiscDriver.getRandomString(10);
		String password = "makemore$";
		String tenantName = MiscDriver.getRandomString(10);
		String tenantID = tenantName.toLowerCase();

		login.gotoPlansAndPricing();
		sup.gotoSignUpNow(packageName);
		create.verifyCreateAccountPage(packageName);

		// most the tenant information is preset to some default
		TenantInfo t = new TenantInfo(username, password, tenantName, tenantID);
		t.setPromoCode(promoCode);
		if(packageName.equals(SignUpPackages.packageTypeFree)) {
			t.setNumberOfUsers(TenantInfo.numUsersFreeAccountFlag);
		} else {
			t.setNumberOfUsers(r.nextInt(5)+1);	// pick a random number from 1 to 5
			t.setPhoneSupport(r.nextBoolean());
			t.setPaymentOptions(TenantInfo.paymentOptionsTwoYear);
			t.setPaymentType(TenantInfo.payByPurchaseOrder);
			t.setPurchaseOrderNumber(MiscDriver.getRandomString(5));
		}
		create.setCreateYourAccountForm(t);
		create.submitCreateYourAccountForm();

        mailServer.waitForMessages(1);
        MailMessage accountActivationMessage = mailServer.getAndClearMessages().get(0);

        SetPasswordPage setPasswordPage = new SignUpEmailLoginNavigator().navigateToSignInPageSpecifiedIn(accountActivationMessage, selenium);
        setPasswordPage.enterAndConfirmPassword(password);
        LoginPage page = setPasswordPage.submitConfirmPassword();

		t.setLoginPage(page);
		
		return t;
	}


}
