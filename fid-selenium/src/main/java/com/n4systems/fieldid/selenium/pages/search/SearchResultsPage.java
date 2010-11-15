package com.n4systems.fieldid.selenium.pages.search;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SearchResultsPage extends FieldIDPage {

    public SearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    public void selectAllItemsOnPage() {
        checkAndFireClick("//table[@class='list']//tr[1]//td[1]/input");
        waitForAjax();
    }

    public void selectItemOnRow(int rowNumber) {
        int tableRow = rowNumber + 1; //adjust for header row
        checkAndFireClick("//table[@class='list']//tr["+tableRow+"]//td[1]/input");
        waitForAjax();
    }

}
