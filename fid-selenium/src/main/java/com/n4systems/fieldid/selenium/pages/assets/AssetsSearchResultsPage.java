package com.n4systems.fieldid.selenium.pages.assets;

import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.search.SearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsSearchResultsPage extends SearchResultsPage {

    public AssetsSearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    public int getTotalResultsCount() {
        return selenium.getXpathCount("//table[@id='resultsTable']//tr").intValue() - 1;
    }

    public AssetsMassUpdatePage clickMassUpdate() {
        selenium.click("//a[.='Mass Update']");
        return new AssetsMassUpdatePage(selenium);
    }

    public AssetPage clickAssetLinkForResult(int resultNumber) {
        int rowNumber = resultNumber + 1;
        selenium.click("//table[@id='resultsTable']//tr["+rowNumber+"]/td[contains(@id, 'serialnumber')]//a");
        return new AssetPage(selenium);
    }

}
