package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import org.junit.Test;
import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkRegisterAssetForm;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkVendorAssetListPage;

public class SafetyNetworkRegisterAssetTest extends PageNavigatingTestCase<SafetyNetworkPage> {

	@Override
	protected SafetyNetworkPage navigateToPage() {
        return startAsCompany("nischain").login().clickSafetyNetworkLink();
    }
	
	@Test
	public void basic_registration_confirm_ok_test() throws Exception {
		IdentifyPage identifyPage = identifyAsset(page);
		identifyPage.clickSignOut();
		page = startAsCompany("halo").login().clickSafetyNetworkLink();
		
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		int numPreAssignedAssets = assetListPage.getAssetList().size();
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(1);
		
		registerPage.clickRegisterAsset();
		
		assertTrue(registerPage.isConfirmPage());
		
		assetListPage = registerPage.clickOk();
		
		if (numPreAssignedAssets > 1) {
			assertEquals(numPreAssignedAssets-1, assetListPage.getAssetList().size());
		} else {
			assertFalse(assetListPage.hasAssetList());
		}
	}
	
	@Test
	public void basic_registration_confirm_perform_event_test() throws Exception {
		IdentifyPage identifyPage = identifyAsset(page);
		identifyPage.clickSignOut();
		page = startAsCompany("halo").login().clickSafetyNetworkLink();
		
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(1);
		
		registerPage.clickRegisterAsset();
		
		registerPage.clickIdentifyLink();
	}
	
	@Test
	public void basic_registration_confirm_view_asset_test() throws Exception {
		IdentifyPage identifyPage = identifyAsset(page);
		identifyPage.clickSignOut();
		page = startAsCompany("halo").login().clickSafetyNetworkLink();
		
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(1);
		
		registerPage.clickRegisterAsset();
		
		registerPage.clickViewAsset();
	}
	
	@Test
	public void basic_registration_missing_serial_test() throws Exception {
		IdentifyPage identifyPage = identifyAsset(page);
		identifyPage.clickSignOut();
		
		page = startAsCompany("halo").login().clickSafetyNetworkLink();
		
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		int numPreAssignedAssets = assetListPage.getAssetList().size();
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(1);
		
		String serialNumber = registerPage.getSerialNumber();
		
		registerPage.enterSerialNumber("");
		
		registerPage.clickRegisterAsset();
		
		assertFalse(registerPage.isConfirmPage());
		
		registerPage.enterSerialNumber(serialNumber);

		registerPage.clickRegisterAsset();

		assetListPage = registerPage.clickOk();
		
		if (numPreAssignedAssets > 1) {
			assertEquals(numPreAssignedAssets-1, assetListPage.getAssetList().size());
		} else {
			assertFalse(assetListPage.hasAssetList());
		}
	}
	
	@Test
	public void detailed_registration_confirm_ok_test() throws Exception {
		IdentifyPage identifyPage = identifyAsset(page);
		identifyPage.clickSignOut();
		page = startAsCompany("halo").login().clickSafetyNetworkLink();
		
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		int numPreAssignedAssets = assetListPage.getAssetList().size();
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(1);
		
		registerPage.setProduct(getDetailedProduct());
				
		registerPage.clickRegisterAsset();
		
		assertTrue(registerPage.isConfirmPage());
		
		assetListPage = registerPage.clickOk();
		
		if (numPreAssignedAssets > 1) {
			assertEquals(numPreAssignedAssets-1, assetListPage.getAssetList().size());
		} else {
			assertFalse(assetListPage.hasAssetList());
		}
	}
	
	private Product getDetailedProduct() {
		Product product = new Product();
		product.setRFIDNumber("rfidNumber");
		product.setReferenceNumber("referenceNumber");
		product.setLocation("location");
		product.setProductStatus("In Service");
		product.setOwner(new Owner("HALO", "Apache"));
		product.setPurchaseOrder("purchaseOrder");
		product.setNonIntegrationOrderNumber("nonIntegrationOrderNumber");
		product.setComments("Inspection Summary");
		return product;
	}

	private IdentifyPage identifyAsset(SafetyNetworkPage profilePage) {
        IdentifyPage identifyPage = profilePage.clickIdentifyLink();
        identifyPage.clickAdd();
        Product p = new Product();
        p.setPublished(true);
        p.setOwner(new Owner("NIS Chain", "HALO"));

        identifyPage.setAddAssetForm(p, true);
        identifyPage.saveNewAsset();
        return identifyPage;
    }

}
