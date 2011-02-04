package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.CustomerConnectionProfilePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.safetynetwork.TypedOrgConnection;

public class SafetyNetworkRegisteredAssetsCountTest extends PageNavigatingTestCase<SafetyNetworkPage> {

	private static String COMPANY1 = "test1";
	private static String COMPANY2 = "test2";
	
	@Override
	public void setupScenario(Scenario scenario) {
        scenario.aSafetyNetworkConnection()
        		.from(scenario.primaryOrgFor(COMPANY1))
        		.to(scenario.primaryOrgFor(COMPANY2))
        		.type(TypedOrgConnection.ConnectionType.VENDOR)
        		.build();
	}
	
    @Override
    protected SafetyNetworkPage navigateToPage() {
        return startAsCompany(COMPANY2).login().clickSafetyNetworkLink();
    }

    @Test
    public void identifying_a_published_asset_should_increment_preassigned_counter() throws Exception {
        CustomerConnectionProfilePage profilePage = page.selectCustomerConnection(COMPANY1);
        int originalPreassignedAssets = profilePage.getNumAssetsPreassigned();

        IdentifyPage identifyPage = identifyAsset(profilePage, true);
        
        profilePage = identifyPage.clickSafetyNetworkLink().selectCustomerConnection(COMPANY1);
        int afterPreassignedAssets = profilePage.getNumAssetsPreassigned();

        assertEquals("Should have one more preassigned asset than we started with", originalPreassignedAssets + 1, afterPreassignedAssets);
    }

    @Test
    public void identifying_a_non_published_asset_should_not_incremented_preassigned_counter() throws Exception {
        CustomerConnectionProfilePage profilePage = page.selectCustomerConnection(COMPANY1);
        int originalPreassignedAssets = profilePage.getNumAssetsPreassigned();

        IdentifyPage identifyPage = identifyAsset(profilePage, false);

        profilePage = identifyPage.clickSafetyNetworkLink().selectCustomerConnection(COMPANY1);
        int afterPreassignedAssets = profilePage.getNumAssetsPreassigned();

        assertEquals("Should have same number of preassigned assets that we started with", originalPreassignedAssets, afterPreassignedAssets);
    }

    private IdentifyPage identifyAsset(FieldIDPage profilePage, boolean published, String serialNumber) {
        IdentifyPage identifyPage = profilePage.clickIdentifyLink();
        Asset p = new Asset();
        p.setPublished(published);
        p.setSerialNumber(serialNumber);
        p.setOwner(new Owner(COMPANY2, COMPANY1));

        boolean generateSerialNumber = serialNumber == null;

        identifyPage.setAddAssetForm(p, generateSerialNumber);
        identifyPage.saveNewAsset();
        return identifyPage;
    }

    private IdentifyPage identifyAsset(FieldIDPage profilePage, boolean published) {
        return identifyAsset(profilePage, published, null);
    }

}
