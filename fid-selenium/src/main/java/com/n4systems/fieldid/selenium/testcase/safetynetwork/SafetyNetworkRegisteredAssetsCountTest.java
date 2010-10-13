package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.CustomerConnectionProfilePage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SafetyNetworkRegisteredAssetsCountTest extends PageNavigatingTestCase<SafetyNetworkPage> {

    @Override
    protected SafetyNetworkPage navigateToPage() {
        return startAsCompany("aacm").login().clickSafetyNetworkLink();
    }

    @Test
    public void identifying_a_published_asset_should_increment_preassigned_counter() throws Exception {
        CustomerConnectionProfilePage profilePage = page.selectCustomerConnection("MSA");
        int originalPreassignedAssets = profilePage.getNumAssetsPreassigned();

        IdentifyPage identifyPage = identifyAsset(profilePage, true);
        
        profilePage = identifyPage.clickSafetyNetworkLink().selectCustomerConnection("MSA");
        int afterPreassignedAssets = profilePage.getNumAssetsPreassigned();

        assertEquals("Should have one more preassigned asset than we started with", originalPreassignedAssets + 1, afterPreassignedAssets);
    }

    @Test
    public void identifying_a_non_published_asset_should_not_incremented_preassigned_counter() throws Exception {
        CustomerConnectionProfilePage profilePage = page.selectCustomerConnection("MSA");
        int originalPreassignedAssets = profilePage.getNumAssetsPreassigned();

        IdentifyPage identifyPage = identifyAsset(profilePage, false);

        profilePage = identifyPage.clickSafetyNetworkLink().selectCustomerConnection("MSA");
        int afterPreassignedAssets = profilePage.getNumAssetsPreassigned();

        assertEquals("Should have same number of preassigned assets that we started with", originalPreassignedAssets, afterPreassignedAssets);
    }

    private IdentifyPage identifyAsset(FieldIDPage profilePage, boolean published, String serialNumber) {
        IdentifyPage identifyPage = profilePage.clickIdentifyLink();
        Product p = new Product();
        p.setPublished(published);
        p.setSerialNumber(serialNumber);
        p.setOwner(new Owner("All American Crane Maintenance", "MSA"));

        boolean generateSerialNumber = serialNumber == null;

        identifyPage.setAddAssetForm(p, generateSerialNumber);
        identifyPage.saveNewAsset();
        return identifyPage;
    }

    private IdentifyPage identifyAsset(FieldIDPage profilePage, boolean published) {
        return identifyAsset(profilePage, published, null);
    }

}
