package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.datatypes.Identifier;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.Order.OrderType;
import com.n4systems.model.orgs.PrimaryOrg;

public class IdentifyAssetsTest extends FieldIDTestCase {

	private static final String COMPANY1 = "test1";
	private static final String COMPANY2 = "test2";
	private static final String ORDER_NUMBER = "11111";
	
	private IdentifyPage identifyPage;
	
	@Override
	public void setupScenario(Scenario scenario) {
		
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(COMPANY1);
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.Integration));
		scenario.save(primaryOrg);
		
		Order order = new Order(OrderType.SHOP, ORDER_NUMBER);
		order.setTenant(scenario.tenant(COMPANY1));
		order.setOwner(primaryOrg);				
		scenario.save(order);
		
		LineItem lineItem = new LineItem(order);
		lineItem.setTenant(scenario.tenant(COMPANY1));
		lineItem.setIndex(0);
		lineItem.setDescription("test line item");
		lineItem.setQuantity(5);
		lineItem.setLineId("1");
		lineItem.setAssetCode("22222");
		scenario.save(lineItem);
		
		scenario.anAssetStatus()
		    .forTenant(scenario.tenant(COMPANY1))
		    .named("test status").build();

		scenario.anAssetStatus()
	    .forTenant(scenario.tenant(COMPANY2))
	    .named("test status").build();

	}
		
	@Test
	public void identify_with_integration_enabled() throws Exception {
		identifyPage = startAsCompany(COMPANY1).systemLogin().clickIdentifyLink();		
		
		assertEquals("Add with Order", identifyPage.getCurrentTab());
	}
	
	@Test
	public void identify_an_asset_using_add_with_order() throws Exception {
		identifyPage = startAsCompany(COMPANY1).systemLogin().clickIdentifyLink();		

		String serialNumber = identifyAssetWithOrderNumber(identifyPage, ORDER_NUMBER);
		checkAssetIdentified(identifyPage, serialNumber);
	}
	
	@Test
	public void identify_multiple_assets_using_add_with_order() throws Exception {
		identifyPage = startAsCompany(COMPANY1).systemLogin().clickIdentifyLink();		

		List<Identifier> identifiers = identifyMultipleAssetsWithOrderNumber(identifyPage, ORDER_NUMBER);
		verifyMultiAddWasSuccessful(identifyPage, identifiers);
	}
	
	@Test
	public void identify_a_single_asset_non_integration_tenant() throws Exception {
		identifyPage = startAsCompany(COMPANY2).systemLogin().clickIdentifyLink();		
		
		String serialNumber = identifyAsset(identifyPage);
		checkAssetIdentified(identifyPage, serialNumber);
	}
	
	@Test
	public void identify_a_single_asset_integration_tenant() throws Exception {
		identifyPage = startAsCompany(COMPANY1).systemLogin().clickIdentifyLink();		

		String serialNumber = identifySingleAssetIntegrationTenant(identifyPage);
		checkAssetIdentified(identifyPage,serialNumber);
	}
	
	@Test
	public void identify_multiple_assets_integration_tenant() throws Exception {
		identifyPage = startAsCompany(COMPANY1).systemLogin().clickIdentifyLink();	
		
		identifyPage.clickMultiAdd();
		List<Identifier> identifiers = identifyMultipleAssetsRange(identifyPage, 3, "*");
		verifyMultiAddWasSuccessful(identifyPage, identifiers);
	}
	
	@Test
	public void identifying_multiple_assets_non_integration_tenant() throws Exception {
		identifyPage = startAsCompany(COMPANY2).systemLogin().clickIdentifyLink();		

		identifyPage.clickMultiAdd();
		List<Identifier> identifiers = identifyMultipleAssetsRange(identifyPage, 3, "*");
		verifyMultiAddWasSuccessful(identifyPage, identifiers);
	}
	
	@Test
	public void identify_a_single_asset_with_errors() throws Exception {
		identifyPage = startAsCompany(COMPANY2).systemLogin().clickIdentifyLink();		
		
		identifyPage.saveNewAsset();
		assertTrue(identifyPage.getFormErrorMessages().size() > 0);
	}
	
	private void verifyMultiAddWasSuccessful(IdentifyPage identifyPage, List<Identifier> identifiers) {
		for(Identifier identifier : identifiers) {
			checkAssetIdentified(identifyPage, identifier.getSerialNumber());
		}
	}

	private List<Identifier> identifyMultipleAssetsRange(IdentifyPage identifyPage, int quantity, String assetType) {
		Calendar now = new GregorianCalendar();
		String prefix = String.format("%1$04d%2$02d%3$02d-", now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		String start = "1";	// start must be a number
		String suffix = String.format("-%1$02d%2$02d%3$02d%4$04d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND));
				
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
		identifyPage.clickIdentifyForOrderLineItem(index + 1);
		identifyPage.checkIdentifyWithOrderNumberPage(orderNumber);
		return identifyAsset(identifyPage);
	}
	
	private List<Identifier> identifyMultipleAssetsWithOrderNumber(IdentifyPage identifyPage, String orderNumber) {
		identifyPage.setOrderNumber(orderNumber);
		identifyPage.clickLoadOrderNumberButton();
		int index = identifyPage.getNumberOfLineItemsInOrder();
		identifyPage.clickIdentifyMultipleForOrderLineItem(index + 1);
		return identifyMultipleAssetsRange(identifyPage, 3, "*");
	}


}
