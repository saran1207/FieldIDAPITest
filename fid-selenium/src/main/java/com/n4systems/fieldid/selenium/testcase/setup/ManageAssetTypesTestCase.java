package com.n4systems.fieldid.selenium.testcase.setup;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;

public abstract class ManageAssetTypesTestCase extends PageNavigatingTestCase<ManageAssetTypesPage> {
	
	protected static final String TEST_ASSET_TYPE_NAME = "TestAssetType";
	protected static final String TEST_ASSET_TYPE_WARNINGS = "Test Warnings";
	protected static final String TEST_ASSET_TYPE_INSTRUCTIONS = "Test Instructions";
	protected static final String TEST_ASSET_TYPE_CAUTIONS_URL = "http://cautions.url.com/";
	protected static final String TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT = "Test Manufacturer Certificate Text";
	protected static final String TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE = "Test Asset Description Template";

    @Override
	protected ManageAssetTypesPage navigateToPage() {
		return startAsCompany("test1").login().clickSetupLink().clickAssetTypes();
	}

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.anAssetType()
                .named(TEST_ASSET_TYPE_NAME)
                .warnings(TEST_ASSET_TYPE_WARNINGS)
                .instructions(TEST_ASSET_TYPE_INSTRUCTIONS)
                .cautionsURL(TEST_ASSET_TYPE_CAUTIONS_URL)
                .manufactureCertificate(true)
                .manufactureCertificateText(TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT)
                .descriptionTemplate(TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE)
                .build();
    }

}
