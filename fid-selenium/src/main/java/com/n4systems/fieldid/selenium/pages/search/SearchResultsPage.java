package com.n4systems.fieldid.selenium.pages.search;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.util.persistence.search.SortDirection;
import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends FieldIDPage {

    public SearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    public void selectAllItemsOnPage() {
        checkAndFireClick("//table[@class='list']//tr[1]//th[1]/input");
        waitForAjax();
    }

    public void selectItemOnRow(int rowNumber) {
        int tableRow = rowNumber + 1; //adjust for header row
        checkAndFireClick("//table[@class='list']//tr["+tableRow+"]//td[1]/input");
        waitForAjax();
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
        return selenium.getText("//table[@class='list']//th[contains(@class, 'sorted')]");
    }

    public SortDirection getSortDirection() {
        String sortedClass = selenium.getAttribute("//table[@class='list']//th[contains(@class, 'sorted')]/@class");
        // Up arrow class means highest is at the top, so we're descending. down arrow means lowest at top, so ascending
        if (sortedClass.contains("up")) {
            return SortDirection.DESC;
        } else if (sortedClass.contains("down")) {
            return SortDirection.ASC;
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
