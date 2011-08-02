package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;

public class SaveAndInspectButtonFromEditTest extends PageNavigatingTestCase<AssetPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.anAsset()
                .withIdentifier("424242QQ")
                .ofType(scenario.assetType("test1", TEST_ASSET_TYPE_1))
                .build();
    }

    @Override
    protected AssetPage navigateToPage() {
        return startAsCompany("test1").login().search("424242QQ");
    }

    @Test
	public void editAssetShouldHaveASaveAndInspectButton() throws Exception {
        page.clickEditTab();
        page.clickSaveAndStartEvent();
	}

}
