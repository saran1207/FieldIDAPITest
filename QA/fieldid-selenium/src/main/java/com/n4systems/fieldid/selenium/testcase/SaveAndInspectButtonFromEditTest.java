package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.page.Asset;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.identify.page.Identify;
import com.n4systems.fieldid.selenium.inspect.page.Inspect;
import com.n4systems.fieldid.selenium.login.page.Login;

/**
 * WEB-1465
 * 
 * @author dgrainge
 *
 */
public class SaveAndInspectButtonFromEditTest extends FieldIDTestCase {

	Login login;
	Identify identify;
	Asset asset;
	Inspect inspect;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		identify = new Identify(selenium, misc);
		asset = new Asset(selenium, misc);
		inspect = new Inspect(selenium, misc);
	}

	@Test
	public void editAssetShouldHaveASaveAndInspectButton() throws Exception {
		String companyID = getStringProperty("company");

		try {
			setCompany(companyID);
			login.signInWithSystemAccount();
			Product p = gotoEditAnAsset();
			verifyEditAnAssetHasASaveAndInspectButtonWhichWorks(p.getSerialNumber());
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void verifyEditAnAssetHasASaveAndInspectButtonWhichWorks(String serialNumber) {
		asset.gotoSaveAndInspect();
		inspect.verifyInspectPage(serialNumber);
	}

	private Product gotoEditAnAsset() throws InterruptedException {
		misc.gotoIdentify();
		if(!identify.isAdd()) {
			identify.gotoAdd();
		}
		Product p = new Product();
		p = identify.setAddAssetForm(p, true);
		identify.gotoSaveAddAssetForm();
		String serialNumber = p.getSerialNumber();
		misc.setSmartSearch(serialNumber);
		misc.submitSmartSearch();
		asset.verifyAssetViewPage(serialNumber);
		asset.verifyAssetViewPageDynamicContents(p);
		asset.gotoEdit();
		asset.verifyAssetEditPage(serialNumber);
		return p;
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
