package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import org.junit.Before;
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
	
	@Before
	public void setup() {
		IdentifyPage identifyPage = identifyAsset(page);
		identifyPage.clickSignOut();
		page = startAsCompany("halo").login().clickSafetyNetworkLink();
	}

	
	@Test
	public void basic_registration_confirm_ok_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
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
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		registerPage.clickRegisterAsset();
		
		registerPage.clickIdentifyLink();
	}
	
	@Test
	public void basic_registration_confirm_view_asset_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		registerPage.clickRegisterAsset();
		
		registerPage.clickViewAsset();
	}
	
	@Test
	public void basic_registration_missing_serial_test() throws Exception {
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
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
		SafetyNetworkVendorAssetListPage assetListPage = page.selectVendorConnection("NIS Chain").clickViewPreassignedAssets();
		
		if (assetListPage.getNumberOfPages() > 1) {
			assetListPage.clickLastPage();
		}
		
		SafetyNetworkRegisterAssetForm registerPage = assetListPage.clickRegister(assetListPage.getAssetList().size());
		
		registerPage.setProduct(getDetailedProduct());
				
		registerPage.clickRegisterAsset();
		
		assertTrue(registerPage.isConfirmPage());
		
		assetListPage = registerPage.clickOk();
		
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

        p = identifyPage.setAddAssetForm(p, true);
        identifyPage.saveNewAsset();
        return identifyPage;
    }

}
