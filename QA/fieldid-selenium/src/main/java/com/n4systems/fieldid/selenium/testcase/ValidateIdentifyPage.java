package com.n4systems.fieldid.selenium.testcase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.page.AssetPage;
import com.n4systems.fieldid.selenium.assets.page.AssetSearch;
import com.n4systems.fieldid.selenium.datatypes.Identifier;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.identify.page.IdentifyPageDriver;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.reporting.page.Reporting;
import com.n4systems.fieldid.selenium.schedule.page.Schedules;

public class ValidateIdentifyPage extends FieldIDTestCase {

	Login login;
	Schedules schedule;
	Reporting reporting;
	AssetSearch assets;
	IdentifyPageDriver identify;
	AssetPage asset;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		schedule = new Schedules(selenium, misc);
		reporting = new Reporting(selenium, misc);
		assets = new AssetSearch(selenium, misc);
		identify = new IdentifyPageDriver(selenium, misc);
		asset = new AssetPage(selenium, misc);
	}
	
	@Test
	public void validate_identify_with_integration_enabled() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");

		setCompany(company);
		login.signInAllTheWay(username, password);
		misc.gotoIdentify();
		assertWeAreOnAddWithOrderPage();
	}
	
	@Test
	public void validate_identifying_an_asset_using_add_with_order() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");
		String orderNumber = getStringProperty("integrationordernumber");

		setCompany(company);
		login.signInAllTheWay(username, password);
		String serialNumber = identifyAssetWithOrderNumber(orderNumber);
		assertAssetIdentified(serialNumber);
	}
	
	@Test
	public void validate_identifying_a_single_asset_non_integration_tenant() throws Exception {
		String username = getStringProperty("notintegrationusername");
		String password = getStringProperty("notintegrationpassword");
		String company = getStringProperty("notintegrationcompanyid");

		setCompany(company);
		login.signInAllTheWay(username, password);
		String serialNumber = identifyAssetNoIntegration();
		assertAssetIdentified(serialNumber);
	}
	
	@Test
	public void validate_identifying_a_single_asset_integration_tenant() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");

		setCompany(company);
		login.signInAllTheWay(username, password);
		String serialNumber = identifySingleAssetIntegrationTenant();
		assertAssetIdentified(serialNumber);
	}
	
	@Test
	public void validate_identifying_multiple_assets_integration_tenant() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");

		setCompany(company);
		login.signInAllTheWay(username, password);
		int quantity = misc.getRandomNumber(2, 10);
		List<Identifier> identifiers = identifyMultipleAssetsRange(quantity, "*");
		assertMultiAddWasSuccessful(identifiers);
	}
	
	@Test
	public void validate_identifying_multiple_assets_non_integration_tenant() throws Exception {
		String username = getStringProperty("notintegrationusername");
		String password = getStringProperty("notintegrationpassword");
		String company = getStringProperty("notintegrationcompanyid");

		setCompany(company);
		login.signInAllTheWay(username, password);
		int quantity = misc.getRandomNumber(2, 10);
		List<Identifier> identifiers = identifyMultipleAssetsRange(quantity, "*");
		assertMultiAddWasSuccessful(identifiers);
	}
	
	private void assertMultiAddWasSuccessful(List<Identifier> identifiers) {
		for(Identifier identifier : identifiers) {
			assertAssetIdentified(identifier.getSerialNumber());
		}
	}

	private List<Identifier> identifyMultipleAssetsRange(int quantity, String productType) {
		Calendar now = new GregorianCalendar();
		String prefix = String.format("%1$04d%2$02d%3$02d-", now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		String start = "1";	// start must be a number
		String suffix = String.format("-%1$02d%2$02d%3$02d%4$04d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND));
		misc.gotoIdentify();
		identify.gotoMultiAdd();
		Product product = new Product();
		product.setLocation("here");
		List<String> productStatuses = identify.getProductStatusesFromMultiAddForm();
		assertTrue("There were no product status options available", productStatuses.size() > 0);
		product.setProductStatus(productStatuses.get(0));
		product.setProductType(productType);
		product.setPurchaseOrder("PO #888");
		product.setComments("This asset created via Multi Add test automation.");
		identify.setMultiAddStep1Form(product);
		identify.clickContinueButtonMultiAddStep1();
		identify.setMultiAddStep2Form(quantity);
		identify.clickContinueButtonMultiAddStep2();
		identify.setMultiAddStep3FormRange(prefix, start, suffix);
		identify.clickContinueButtonMultiAddStep3();
		List<Identifier> identifiers = null;
		identifiers = identify.setMultiAddStep4Form(identifiers);
		identify.clickSaveAndCreateButtonMultiAddStep4();
		
		return identifiers;
	}

	private String identifyAssetNoIntegration() throws Exception {
		misc.gotoIdentify();
		return identifyProduct();
	}

	private String identifyProduct() throws Exception {
		Product product = new Product();
		product = identify.setAddAssetForm(product, true);
		identify.saveNewAsset();
		return product.getSerialNumber();
	}

	private String identifySingleAssetIntegrationTenant() throws Exception {
		misc.gotoIdentify();
		identify.gotoAdd();
		return identifyProduct();
	}

	private void assertAssetIdentified(String serialNumber) {
		misc.setSmartSearch(serialNumber);
		misc.submitSmartSearch();
		asset.verifyAssetViewPage(serialNumber);
	}

	private String identifyAssetWithOrderNumber(String orderNumber) throws Exception {
		misc.gotoIdentify();
		identify.setOrderNumber(orderNumber);
		identify.clickLoadOrderNumberButton();
		int index = identify.getNumberOfLineItemsInOrder();
		identify.clickIdentifyForOrderLineItem(index-1);
		identify.assertIdentifyWithOrderNumberPage(orderNumber);
		return identifyProduct();
	}

	private void assertWeAreOnAddWithOrderPage() {
		identify.assertIdentifyPageHeader();
		String tabText = "Add with Order";
		assertEquals(identify.getSelectedTab(), tabText);
		identify.assertIdentifyAddWithOrderPage();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
