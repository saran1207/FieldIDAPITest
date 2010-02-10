package com.n4systems.fieldid.selenium.misc;

import java.util.Random;
import com.n4systems.fieldid.selenium.datatypes.CreateTenant;
import com.n4systems.fieldid.selenium.login.CreateAccount;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.login.SignUpComplete;
import com.n4systems.fieldid.selenium.login.SignUpPackages;
import com.thoughtworks.selenium.Selenium;
import static org.junit.Assert.*;

public class CreateTenants {

	private Login login;
	private SignUpPackages sup;
	private CreateAccount create;
	private SignUpComplete complete;
	private Selenium selenium;
	private Misc misc;
	private Random r;

	public CreateTenants(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
		this.r = new Random();
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
	 * @param packageName
	 * @param promoCode
	 * @return
	 */
	public CreateTenant createARandomNewTenant(String packageName, String promoCode) {
		assertNotNull("Need a package name", packageName);
		if(promoCode == null) {
			promoCode = "";
		}
		String username = misc.getRandomString(10);
		String password = "makemore$";
		String tenantName = misc.getRandomString(10);
		String tenantID = tenantName.toLowerCase();

		login.gotoPlansAndPricing();
		sup.gotoSignUpNow(packageName);
		create.verifyCreateAccountPage(packageName);

		// most the tenant information is preset to some default
		CreateTenant t = new CreateTenant(username, password, tenantName, tenantID);
		t.setPromoCode(promoCode);
		if(packageName.equals(SignUpPackages.packageTypeFree)) {
			t.setNumberOfUsers(CreateTenant.numUsersFreeAccountFlag);
		} else {
			t.setNumberOfUsers(r.nextInt(5)+1);	// pick a random number from 1 to 5
			t.setPhoneSupport(r.nextBoolean());
			t.setPaymentOptions(CreateTenant.paymentOptionsTwoYear);
			t.setPaymentType(CreateTenant.payByPurchaseOrder);
			t.setpurchaseOrderNumber(misc.getRandomString(5));
		}
		create.setCreateYourAccountForm(t);
		create.gotoCreateMyAccount();
		complete.gotoSignInNow();
		
		return t;
	}
}
