package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.identify.Identify;
import com.n4systems.fieldid.selenium.inspect.Inspect;
import com.n4systems.fieldid.selenium.login.Login;
import com.n4systems.fieldid.selenium.assets.Asset;
import com.n4systems.fieldid.selenium.datatypes.Product;

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
		String password = getStringProperty("password");
		String companyID = getStringProperty("company");
		String username = getStringProperty("userid");

		try {
			setCompany(companyID);
			login.loginAcceptingEULAIfNecessary(username, password);
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

	private Product gotoEditAnAsset() {
		misc.gotoIdentify();
		if(!identify.isAdd()) {
			identify.gotoAdd();
		}
		Product p = new Product();
		p = identify.setAddAssetForm(p, true);
		identify.gotoSaveAddAssetForm();
		String serialNumber = p.getSerialNumber();
		misc.setSmartSearch(serialNumber);
		misc.gotoSmartSearch();
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
