package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.page.AssetPage;
import com.n4systems.fieldid.selenium.assets.page.AssetSearch;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.datatypes.ProductSelectDisplayColumns;
import com.n4systems.fieldid.selenium.login.page.Login;

public class CheckSerialNumberOnEditProductTest extends FieldIDTestCase {

	private Login login;
	private AssetPage asset;
	private AssetSearch ps;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		asset = new AssetPage(selenium, misc);
		ps = new AssetSearch(selenium, misc);
		login = new Login(selenium, misc);
	}
	
	@Ignore("Currently no way to detect javascript errors")
	@Test
	public void changing_serial_number_on_edit_product_should_have_no_javascript_errors() throws Exception {
		login.signInWithSystemAccount();
		goToProductSearchResults();
		goToEditProduct(0);
		misc.clickGenerateSerialNumber();
		assertThereIsNoJavascriptError();
	}
	
	private void goToEditProduct(int row) {
		ps.gotoAsset(row);
		asset.gotoEdit();
	}

	private void goToProductSearchResults() {
		misc.gotoAssets();
		ProductSelectDisplayColumns sdc = new ProductSelectDisplayColumns();
		sdc.setAll(false);
		sdc.setSerialNumber(true);
		ps.setDisplayColumns(sdc);
		ps.runSearch();
	}

	@Ignore("Currently no way to detect javascript errors")
	@Test
	public void changing_serial_number_on_edit_product_to_existing_serial_number_should_have_no_javascript_errors() throws Exception {
		login.signInWithSystemAccount();
		goToProductSearchResults();
		String serialNumber = ps.getSerialNumberForAsset(0);
		goToEditProduct(1);
		Product p = new Product();
		p.setSerialNumber(serialNumber);
		asset.setAssetForm(p);
		assertThereIsNoJavascriptError();
	}
	
	private void assertThereIsNoJavascriptError() {
		assertFalse("There was a Javascript error.", misc.isJavascriptError());
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
