package com.n4systems.fieldid.selenium.testcase;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.page.AssetPage;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
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
	IdentifyPageDriver identify;
	AssetPage asset;
	Inspect inspect;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		identify = new IdentifyPageDriver(selenium, misc);
		asset = new AssetPage(selenium, misc);
		inspect = new Inspect(selenium, misc);
	}

	@Test
	public void editAssetShouldHaveASaveAndInspectButton() throws Exception {
		String companyID = getStringProperty("company");

        startAsCompany(companyID);
        login.signInWithSystemAccount();
        Product p = gotoEditAnAsset();
        verifyEditAnAssetHasASaveAndInspectButtonWhichWorks(p.getSerialNumber());
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
		identify.saveNewAsset();
		String serialNumber = p.getSerialNumber();
		misc.setSmartSearch(serialNumber);
		misc.submitSmartSearch();
		asset.verifyAssetViewPage(serialNumber);
		asset.verifyAssetViewPageDynamicContents(p);
		asset.gotoEdit();
		asset.verifyAssetEditPage(serialNumber);
		return p;
	}

}
