package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.ProductSearch;
import com.n4systems.fieldid.selenium.identify.Identify;
import com.n4systems.fieldid.selenium.login.Choose;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.datatypes.Product;

public class WEB_1465 extends FieldIDTestCase {

	Login login;
	Choose choose;
	Identify identify;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		choose = new Choose(selenium, misc);
		identify = new Identify(selenium, misc);
	}

	@Test
	public void editAssetShouldHaveASaveAndInspectButton() throws Exception {
		String password = getStringProperty("password");
		String companyID = getStringProperty("company");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			loginAcceptingEULAIfNecessary(username, password);
			gotoEditAnAsset();
			verifyEditAnAssetHasASaveAndInspectButtonWhichWorks();
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void verifyEditAnAssetHasASaveAndInspectButtonWhichWorks() {
		// TODO Auto-generated method stub
		fail("not yet implemented");
	}

	private void gotoEditAnAsset() {
		misc.gotoIdentify();
		if(!identify.isAdd()) {
			identify.gotoAdd();
		}
		Product p = identify.setAddAssetForm(new Product(), true);
		// fill in the form
		// click the save button
		// smart search for the asset we just created
		// go to the edit view
		fail("not yet implemented");
	}

	private void loginAcceptingEULAIfNecessary(String username, String password) {
		login.setUserName(username);
		login.setPassword(password);
		login.gotoSignIn();
		if(misc.isEULA()) {
			misc.scrollToBottomOfEULA();
			misc.gotoAcceptEULA();
		}
		login.verifySignedIn();
	}

	private void setCompany(String companyID) {
		misc.info("Changing to company '" + companyID);
		login.gotoIsNotTheCompanyIWant();
		choose.setCompanyID(companyID);
		choose.gotoFindSignInPage();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
