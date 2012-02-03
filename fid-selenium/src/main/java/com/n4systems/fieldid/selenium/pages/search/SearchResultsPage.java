package com.n4systems.fieldid.selenium.pages.search;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.n4systems.util.persistence.search.SortDirection;
import com.thoughtworks.selenium.Selenium;

public abstract class SearchResultsPage extends WicketFieldIDPage {

    public SearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    protected abstract void waitForFrameworkAjax();

    public void selectAllItemsOnPage() {
        checkAndFireClick("//table[@class='list']//tr[1]//th[1]//input");
        waitForFrameworkAjax();
    }

    public void selectItemOnRow(int rowNumber) {
        checkAndFireClick("//table[@class='list']//tr["+rowNumber+"]//td[1]//input");
        waitForFrameworkAjax();
    }

    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<String>();
        int numColumns = selenium.getXpathCount("//table[@class='list']//th").intValue() - 2;
        for (int i = 2; i <= numColumns + 1; i++) {
            columnNames.add(selenium.getText("xpath=(//table[@class='list']//th)["+i+"]"));
        }
        return columnNames;
    }

    public String getSortColumn() {
        return selenium.getText("//table[@class='list']//th[contains(@class, 'wicket_order') and not(contains(@class, 'wicket_orderNone'))]");
    }

    public SortDirection getSortDirection() {
        String sortedClass = selenium.getAttribute("//table[@class='list']//th[contains(@class, 'wicket_order') and not(contains(@class, 'wicket_orderNone'))]/@class");
        // Up arrow class means highest is at the top, so we're descending. down arrow means lowest at top, so ascending
        if (sortedClass.toLowerCase().contains("up")) {
            return SortDirection.ASC;
        } else if (sortedClass.toLowerCase().contains("down")) {
            return SortDirection.DESC;
        } else {
            throw new RuntimeException("Could not determine sort direction from class: " + sortedClass);
        }
    }

    public void clickSortColumn(String sortColumnName) {
        selenium.click("//table[@class='list']//th//a[.='"+sortColumnName+"']");
        waitForPageToLoad();
    }

    public String getValueInCell(int rowNumber, int colNumber) {
        return selenium.getText("//table[@class='list']//tbody//tr["+(rowNumber)+"]//td["+(colNumber+1)+"]");
    }

}
