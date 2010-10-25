package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Identifier;
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
		String serialNumber = identifyAsset(identifyPage);
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

	private List<Identifier> identifyMultipleAssetsRange(IdentifyPage identifyPage, int quantity, String assetType) {
		Calendar now = new GregorianCalendar();
		String prefix = String.format("%1$04d%2$02d%3$02d-", now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		String start = "1";	// start must be a number
		String suffix = String.format("-%1$02d%2$02d%3$02d%4$04d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND));
		
		identifyPage.clickMultiAdd();
		
		Asset asset = new Asset();
		asset.setLocation("here");
		List<String> assetStatuses = identifyPage.getAssetStatusesFromMultiAddForm();
		assertTrue("There were no asset status options available", assetStatuses.size() > 0);
		asset.setAssetStatus(assetStatuses.get(0));
		asset.setAssetType(assetType);
		asset.setPurchaseOrder("PO #888");
		asset.setComments("This asset created via Multi Add test automation.");
		identifyPage.setMultiAddStep1Form(asset);
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

	private String identifyAsset(IdentifyPage identifyPage) throws Exception {
		Asset asset = new Asset();
		asset = identifyPage.setAddAssetForm(asset, true);
		identifyPage.saveNewAsset();

		return asset.getSerialNumber();
	}

	private String identifySingleAssetIntegrationTenant(IdentifyPage identifyPage) throws Exception {
		identifyPage.clickAdd();
		return identifyAsset(identifyPage);
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
		return identifyAsset(identifyPage);
	}

}
