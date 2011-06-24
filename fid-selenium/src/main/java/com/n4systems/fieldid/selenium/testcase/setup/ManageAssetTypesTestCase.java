package com.n4systems.fieldid.selenium.testcase.setup;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public abstract class ManageAssetTypesTestCase extends PageNavigatingTestCase<ManageAssetTypesPage> {
	
	protected static final String TEST_ASSET_TYPE_GROUP = "TestGroup";
	protected static final String TEST_ASSET_TYPE_NAME = "TestAssetType";
	protected static final String TEST_ASSET_TYPE_WARNINGS = "Test Warnings";
	protected static final String TEST_ASSET_TYPE_INSTRUCTIONS = "Test Instructions";
	protected static final String TEST_ASSET_TYPE_CAUTIONS_URL = "http://cautions.url.com/";
	protected static final String TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT = "Test Manufacturer Certificate Text";
	protected static final String TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE = "Test Asset Description Template";
	
	protected static final String TEST_EVENT_TYPE_NAME = "Simple Event Type";
    @Override
	protected ManageAssetTypesPage navigateToPage() {
		return startAsCompany("test1").login().clickSetupLink().clickAssetTypes();
	}

    @Override
    public void setupScenario(Scenario scenario) {
		PrimaryOrg primaryOrg = scenario.primaryOrgFor("test1");
		
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.ManufacturerCertificate));
		
		scenario.updatePrimaryOrg(primaryOrg);
		
		AssetTypeGroup assetTypeGroup = scenario.anAssetTypeGroup()
											    .withTenant(scenario.tenant("test1"))
											    .withName(TEST_ASSET_TYPE_GROUP)
		                                        .build();
        scenario.anAssetType()
                .named(TEST_ASSET_TYPE_NAME)
                .warnings(TEST_ASSET_TYPE_WARNINGS)
                .instructions(TEST_ASSET_TYPE_INSTRUCTIONS)
                .cautionsURL(TEST_ASSET_TYPE_CAUTIONS_URL)
                .manufactureCertificate(true)
                .manufactureCertificateText(TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT)
                .descriptionTemplate(TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE)
                .withGroup(assetTypeGroup)
                .build();
        
        scenario.anEventType()
        		.named(TEST_EVENT_TYPE_NAME)
        		.build();
    }

}
