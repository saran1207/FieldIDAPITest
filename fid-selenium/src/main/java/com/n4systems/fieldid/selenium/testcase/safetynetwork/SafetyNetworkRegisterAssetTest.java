package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkRegisterAssetForm;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkVendorAssetListPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;

public class SafetyNetworkRegisterAssetTest extends PageNavigatingTestCase<SafetyNetworkPage> {
	
	private static final String ASSET_TYPE = "TestType1";
	private static final String STATUS = "Asset Status";
	private static final String SERIAL_NUMBER = "1111111";
	private static String COMPANY1 = "test1";
	private static String COMPANY2 = "test2";
		
	@Override
	protected SafetyNetworkPage navigateToPage() {
        return startAsCompany(COMPANY2).login().clickSafetyNetworkLink();
    }
	
	@Override
	public void setupScenario(Scenario scenario) {
		PrimaryOrg customer = scenario.primaryOrgFor(COMPANY1);
		PrimaryOrg vendor = scenario.primaryOrgFor(COMPANY2);
		OrgConnection connection = new OrgConnection(vendor, customer);
		scenario.save(connection);
	
        AssetType type = scenario.assetType(COMPANY2, ASSET_TYPE);

        scenario.anAsset()
                .forTenant(scenario.tenant(COMPANY2))
                .withOwner(vendor)
                .withSerialNumber(SERIAL_NUMBER)
                .ofType(type)
                .published(true)
                .build();
        
		scenario.anAssetStatus()
                .forTenant(scenario.tenant(COMPANY1))
                .named(STATUS)
                .build();
	}
	
	@Before
	public void setup() {
		AssetPage assetPage = page.search(SERIAL_NUMBER);
		assetPage.clickEditTab();
		assetPage.setOwner(new Owner(COMPANY2, COMPANY1));
		assetPage.clickSave();
		page.clickSignOut();
		page = startAsCompany(COMPANY1).login().clickSafetyNetworkLink();
	}
	
	@Test
	public void basic_registration_confirm_ok_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection(COMPANY2).clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		registerPage.clickRegisterAsset();
		
		assertTrue(registerPage.isConfirmPage());
		
		assetListPage = registerPage.clickOk();
	}
	
	@Test
	public void basic_registration_confirm_perform_event_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection(COMPANY2).clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		registerPage.clickRegisterAsset();		
		registerPage.clickIdentifyLink();
	}
	
	@Test
	public void basic_registration_confirm_view_asset_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection(COMPANY2).clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		registerPage.clickRegisterAsset();
		registerPage.clickViewAsset();
	}
	
	@Test
	public void basic_registration_missing_serial_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection(COMPANY2).clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		String serialNumber = registerPage.getSerialNumber();
		
		registerPage.enterSerialNumber("");
		registerPage.clickRegisterAsset();
		
		assertFalse(registerPage.isConfirmPage());
		
		registerPage.enterSerialNumber(serialNumber);
		registerPage.clickRegisterAsset();

		assetListPage = registerPage.clickOk();
	}
	
	@Test
	public void detailed_registration_confirm_ok_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection(COMPANY2).clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		setDetailedAssetFields(registerPage);
				
		registerPage.clickRegisterAsset();
		
		assertTrue(registerPage.isConfirmPage());
		
		assetListPage = registerPage.clickOk();
	}
	
	private void setDetailedAssetFields(SafetyNetworkRegisterAssetForm page) {
		page.setRFIDNumber("rfidNumber");
		page.setReferenceNumber("referenceNumber");
		page.setLocation("location");
		page.setAssetStatus(STATUS);
		page.setOwner(new Owner(COMPANY1));
		page.setPurchaseOrder("purchaseOrder");
		page.setNonIntegrationOrderNumber("nonIntegrationOrderNumber");
		page.enterComments("Destroy");
	}


}
