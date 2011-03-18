package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.pages.SetupPage;
import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.n4systems.util.persistence.search.SortDirection;
import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

public class ColumnLayoutPage extends WicketFieldIDPage {

    public ColumnLayoutPage(Selenium selenium) {
        super(selenium);
    }

    public List<String> getCurrentColumns() {
        selenium.selectFrame("//iframe");

        List<String> currentColumns = new ArrayList<String>();
        int currentColumnCount = selenium.getXpathCount("//div[contains(@class,'reportColumnTitle')]").intValue();

        for (int i = 1; i <= currentColumnCount; i++) {
            String columnTitle = selenium.getText("xpath=(//div[contains(@class,'reportColumnTitle')])[" + i + "]");
            currentColumns.add(columnTitle);
        }

        selenium.selectFrame("relative=up");
        return currentColumns;
    }

    public void clickRemoveSelectedColumn(String columnName) {
        selenium.selectFrame("//iframe");

        selenium.click("//div[contains(@class,'reportColumnTitle') and .='" + columnName + "']/../../div[@class='deleteColumnImageContainer']//img");
        waitForWicketAjax();

        selenium.selectFrame("relative=up");
    }

    public SetupPage clickSave() {
        selenium.selectFrame("//iframe");
        selenium.click("//button[.='Save']");
        return new SetupPage(selenium);
    }

    public void expandColumnGroup(String groupTitle) {
        selenium.selectFrame("//iframe");
        selenium.click("//a[@class='collapseExpandLink']//span[text()='"+groupTitle+"']");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    public void clickAddColumn(String columnName) {
        selenium.selectFrame("//iframe");
        selenium.click("//div[@class='leftBar']//div[@class='reportColumnTitle' and text()='"+columnName+"']");
        waitForWicketAjax();
        selenium.selectFrame("relative=up");
    }

    public void clearLayout() {
        selenium.selectFrame("//iframe");
        for (String column : getCurrentColumns()) {
            clickRemoveSelectedColumn(column);
            waitForWicketAjax();
        }
        selenium.selectFrame("relative=up");
    }

    public SortDirection getSortDirection() {
        selenium.selectFrame("//iframe");
        String sortDirectionStr = selenium.getSelectedLabel("//select[@name='directionSelect']");
        selenium.selectFrame("relative=up");

        if ("Ascending".equals(sortDirectionStr)) {
            return SortDirection.ASC;
        } else if ("Descending".equals(sortDirectionStr)) {
            return SortDirection.DESC;
        } else {
            throw new RuntimeException("Unable to determine sort direction from selected value: " + sortDirectionStr);
        }
    }

    public String getSortColumnName() {
        selenium.selectFrame("//iframe");
        String sortColumn = selenium.getSelectedLabel("//select[@name='sortColumnSelect']");
        selenium.selectFrame("relative=up");
        return sortColumn;
    }

    public void selectSortColumn(String sortColumnName) {
        selenium.selectFrame("//iframe");
        selenium.select("//select[@name='sortColumnSelect']", sortColumnName);
        selenium.selectFrame("relative=up");
    }

    public void selectSortDirection(SortDirection sortDirection) {
        selenium.selectFrame("//iframe");
        String sortDirectionStr = null;
        if (sortDirection == SortDirection.ASC)
            sortDirectionStr = "Ascending";
        if (sortDirection == SortDirection.DESC)
            sortDirectionStr = "Descending";
        selenium.select("//select[@name='directionSelect']", sortDirectionStr);
        selenium.selectFrame("relative=up");
    }

}
