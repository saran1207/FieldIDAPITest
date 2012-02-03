package com.n4systems.fieldid.selenium.pages.assets;

import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.search.SearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsSearchResultsPage extends SearchResultsPage {

    public AssetsSearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    @Override
    protected void waitForFrameworkAjax() {
        waitForWicketAjax();
    }

    public int getTotalResultsCount() {
        return selenium.getXpathCount("//table[@class='list']//tr").intValue() - 1;
    }

    public AssetsMassUpdatePage clickMassUpdate() {
        selenium.click("//a[.='Mass Update']");
        return new AssetsMassUpdatePage(selenium);
    }

    public AssetPage clickAssetLinkForResult(int resultNumber) {
        selenium.click("//table[@class='list']//tbody//tr["+resultNumber+"]/td//a[@class='identifierLink']");
        return new AssetPage(selenium);
    }

}
