package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Identifier;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.LoginPage;

public class IdentifyAssetsTest extends FieldIDTestCase {

	private HomePage homePage;
		
	@Test
	public void identify_with_integration_enabled() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");
		
		LoginPage loginPage = startAsCompany(company);		
		homePage = loginPage.login(username, password);
		IdentifyPage identifyPage = homePage.clickIdentifyLink();
		
		assertEquals(identifyPage.getCurrentTab(), "Add with Order");
	}
	
	@Test
	public void identify_an_asset_using_add_with_order() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");
		String orderNumber = getStringProperty("integrationordernumber");
		
		LoginPage loginPage = startAsCompany(company);		
		homePage = loginPage.login(username, password);
		IdentifyPage identifyPage = homePage.clickIdentifyLink();

		String serialNumber = identifyAssetWithOrderNumber(identifyPage, orderNumber);
		checkAssetIdentified(identifyPage, serialNumber);
	}
	
	@Test
	public void identify_a_single_asset_non_integration_tenant() throws Exception {
		String username = getStringProperty("notintegrationusername");
		String password = getStringProperty("notintegrationpassword");
		String company = getStringProperty("notintegrationcompanyid");

		LoginPage loginPage = startAsCompany(company);		
		homePage = loginPage.login(username, password);
		IdentifyPage identifyPage = homePage.clickIdentifyLink();
		String serialNumber = identifyProduct(identifyPage);
		checkAssetIdentified(identifyPage, serialNumber);
	}
	
	@Test
	public void identify_a_single_asset_integration_tenant() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");

		LoginPage loginPage = startAsCompany(company);		
		homePage = loginPage.login(username, password);
		IdentifyPage identifyPage = homePage.clickIdentifyLink();
		String serialNumber = identifySingleAssetIntegrationTenant(identifyPage);
		checkAssetIdentified(identifyPage,serialNumber);
	}
	
	@Test
	public void identify_multiple_assets_integration_tenant() throws Exception {
		String username = getStringProperty("integrationusername");
		String password = getStringProperty("integrationpassword");
		String company = getStringProperty("integrationcompanyid");

		LoginPage loginPage = startAsCompany(company);		
		homePage = loginPage.login(username, password);
		IdentifyPage identifyPage = homePage.clickIdentifyLink();
		int quantity = misc.getRandomNumber(2, 10);
		List<Identifier> identifiers = identifyMultipleAssetsRange(identifyPage, quantity, "*");
		verifyMultiAddWasSuccessful(identifiers);
	}
	
	@Test
	public void identifying_multiple_assets_non_integration_tenant() throws Exception {
		String username = getStringProperty("notintegrationusername");
		String password = getStringProperty("notintegrationpassword");
		String company = getStringProperty("notintegrationcompanyid");

		LoginPage loginPage = startAsCompany(company);		
		homePage = loginPage.login(username, password);
		IdentifyPage identifyPage = homePage.clickIdentifyLink();
		int quantity = misc.getRandomNumber(2, 10);
		List<Identifier> identifiers = identifyMultipleAssetsRange(identifyPage, quantity, "*");
		verifyMultiAddWasSuccessful(identifiers);
	}
	
	private void verifyMultiAddWasSuccessful(List<Identifier> identifiers) {
		for(Identifier identifier : identifiers) {
			checkAssetIdentified(null,identifier.getSerialNumber());
		}
	}

	private List<Identifier> identifyMultipleAssetsRange(IdentifyPage identifyPage, int quantity, String productType) {
		Calendar now = new GregorianCalendar();
		String prefix = String.format("%1$04d%2$02d%3$02d-", now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		String start = "1";	// start must be a number
		String suffix = String.format("-%1$02d%2$02d%3$02d%4$04d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND));
		
		identifyPage.clickMultiAdd();
		
		Product product = new Product();
		product.setLocation("here");
		List<String> productStatuses = identifyPage.getProductStatusesFromMultiAddForm();
		assertTrue("There were no asset status options available", productStatuses.size() > 0);
		product.setProductStatus(productStatuses.get(0));
		product.setProductType(productType);
		product.setPurchaseOrder("PO #888");
		product.setComments("This asset created via Multi Add test automation.");
		identifyPage.setMultiAddStep1Form(product);
		identifyPage.clickContinueButtonMultiAddStep1();
		identifyPage.setMultiAddStep2Form(quantity);
		identifyPage.clickContinueButtonMultiAddStep2();
		identifyPage.setMultiAddStep3FormRange(prefix, start, suffix);
		identifyPage.clickContinueButtonMultiAddStep3();
			
		List<Identifier> identifiers = null;

		identifiers = identifyPage.setMultiAddStep4Form(identifiers);
		identifyPage.clickSaveAndCreateButtonMultiAddStep4();
		
		return identifiers;
	}

	private String identifyProduct(IdentifyPage identifyPage) throws Exception {
		Product product = new Product();
		product = identifyPage.setAddAssetForm(product, true);
		identifyPage.saveNewAsset();

		return product.getSerialNumber();
	}

	private String identifySingleAssetIntegrationTenant(IdentifyPage identifyPage) throws Exception {
		identifyPage.clickAdd();
		return identifyProduct(identifyPage);
	}

	private void checkAssetIdentified(IdentifyPage identifyPage, String serialNumber) {
		AssetPage assetPage = identifyPage.search(serialNumber);
		assertTrue(assetPage.checkHeader(serialNumber));
	}

	private String identifyAssetWithOrderNumber(IdentifyPage identifyPage, String orderNumber) throws Exception {
		identifyPage.setOrderNumber(orderNumber);
		identifyPage.clickLoadOrderNumberButton();
		int index = identifyPage.getNumberOfLineItemsInOrder();
		identifyPage.clickIdentifyForOrderLineItem(index);
		identifyPage.checkIdentifyWithOrderNumberPage(orderNumber);
		return identifyProduct(identifyPage);
	}

}
