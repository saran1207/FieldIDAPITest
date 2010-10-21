package com.n4systems.fieldid.selenium.misc;

import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;

public class Search {
	FieldIdSelenium selenium;
	MiscDriver misc;

	// Locators
	private String clearFormButtonLocator = "xpath=//INPUT[@type='reset' and @value='Clear Form']";
	private String runButtonLocator = "xpath=//INPUT[@id='reportForm_label_Run']";
	private String openSelectDisplayColumnsLinkLocator = "xpath=//A[@id='open_selectColumnForm' and not(contains(@style,'display: none'))]";
	private String closeSelectDisplayColumnsLinkLocator = "xpath=//A[@id='close_selectColumnForm' and not(contains(@style,'display: none'))]";
	private String availableColumnsUpdatingLocator = "xpath=//SPAN[id='selectColumnNotificationArea' and not(contains(@style,'display: none')) and contains(text(),'Available Columns Updating')]";
	private String availableColumnsUpdatedLocator = "xpath=//SPAN[id='selectColumnNotificationArea' and not(contains(@style,'display: none')) and contains(text(),'Available Columns Updated')]";
	private String closeSearchCriteriaLinkLocator = "xpath=//A[@id='collapseSection_reportForm' and not(contains(@style,'display: none'))]";
	private String openSearchCriteriaLinkLocator = "xpath=//A[@id='expandSection_reportForm' and not(contains(@style,'display: none'))]";
	private String resultTableXpath = "//TABLE[@id='resultsTable']";
	private String numberOfColumnsXpath = resultTableXpath + "/TBODY/TR/TH";
	private String numberOfRowsXpath = resultTableXpath + "/TBODY/TR/TD[1]";

	public Search(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void clearSearchCriteriaForm() {
		if(selenium.isElementPresent(clearFormButtonLocator)) {
			selenium.click(clearFormButtonLocator);
		}
	}

	public void expandSelectDisplayColumns() {
		if(selenium.isElementPresent(openSelectDisplayColumnsLinkLocator)) {
			selenium.click(openSelectDisplayColumnsLinkLocator);
		} else {
			fail("Could not locate the link to expand the Select Display Columns");
		}
	}
	
	public void collapseSelectDisplayColumns() {
		if(selenium.isElementPresent(closeSelectDisplayColumnsLinkLocator)) {
			selenium.click(closeSelectDisplayColumnsLinkLocator);
		} else {
			fail("Could not locate the link to collapse the Select Display Columns");
		}
	}


	public void expandSearchCriteria() {
		if(selenium.isElementPresent(openSearchCriteriaLinkLocator)) {
			selenium.click(openSearchCriteriaLinkLocator);
		} else {
			fail("Could not locate the link to expand the Search Criteria");
		}
	}
	
	public void collapseSearchCriteria() {
		if(selenium.isElementPresent(closeSearchCriteriaLinkLocator)) {
			selenium.click(closeSearchCriteriaLinkLocator);
		} else {
			fail("Could not locate the link to collapse the Search Criteria");
		}
	}
	public void gotoRunSearchCriteria() {
		if(selenium.isElementPresent(runButtonLocator)) {
			selenium.click(runButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	public void waitForDisplayColumnsToUpdate(String defaultTimeout) {
		int maxSeconds = Integer.parseInt(defaultTimeout);
		boolean updating = true;
		int secondsLeft = maxSeconds;
		do {
			misc.sleep(1000);
			updating = selenium.isElementPresent(availableColumnsUpdatingLocator);
			secondsLeft--;
		} while(updating && secondsLeft > 0);

		do {
			try { Thread.sleep(1000); } catch(Exception e) { }
			updating = selenium.isElementPresent(availableColumnsUpdatedLocator);
			secondsLeft--;
		} while(updating && secondsLeft > 0);
	}

	/**
	 * This method will get a list of the attributes from the Select Display Columns
	 * section. For example, if you are on the Schedules page and you select a
	 * specific Asset Type, the Asset Attributes section of Select Display
	 * Columns will update to the asset attributes for the asset type you
	 * selected.
	 * 
	 * @param dynamicColumnLocator
	 * @param dynamicCheckBoxXpath
	 * @return
	 */
	public List<String> getAttributesDisplayColumns(String dynamicColumnLocator, String dynamicCheckBoxXpath) {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(dynamicColumnLocator)) {
			Number n = selenium.getXpathCount(dynamicCheckBoxXpath);
			int num = n.intValue();
			String productStatusDisplayColumnLabelLocator = dynamicColumnLocator + "/DIV[0]/LABEL";
			for(int i = 0; i < num; i++) {
				productStatusDisplayColumnLabelLocator = productStatusDisplayColumnLabelLocator.replaceFirst("\\[" + i + "\\]/LABEL", "\\[" + (i+1) + "\\]/LABEL");
				String s = selenium.getText(productStatusDisplayColumnLabelLocator);
				result.add(s);
			}
		}
		return null;
	}

	public void setDisplayColumn(String label, boolean b) {
		misc.setCheckBoxByAssociatedLabel(label, b);
	}
	
	public List<String> getColumnHeaders() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfColumnsXpath);
		int numColumns = n.intValue();
		String resultColumnHeaderLocator = "xpath=" + resultTableXpath + ".0.0";
		for(int i = 0; i < numColumns; i++) {
			String columnHeader = selenium.getTable(resultColumnHeaderLocator);
			result.add(columnHeader);
			resultColumnHeaderLocator = resultColumnHeaderLocator.replaceFirst(".0." + i, ".0." + (i+1));
		}
		return result;
	}

	public List<String> getSortableColumnHeaders() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfColumnsXpath);
		int numColumns = n.intValue();
		String resultColumnHeaderLocator = "xpath=" + resultTableXpath + ".0.0";
		for(int i = 0; i < numColumns; i++) {
			String columnHeader = selenium.getTable(resultColumnHeaderLocator);
			if(selenium.isElementPresent("xpath=" + resultTableXpath + "/TBODY/TR/TH/A[text()='" + columnHeader + "']")) {
				result.add(columnHeader);
			}
			resultColumnHeaderLocator = resultColumnHeaderLocator.replaceFirst(".0." + i, ".0." + (i+1));
		}
		return result;
	}

	public List<String> getColumnDataCurrentPage(String columnHeaderString) {
		List<String> results = new ArrayList<String>();
		
		String columnHeaderLocator = "xpath=" + numberOfColumnsXpath + "/A[contains(text(),'" + columnHeaderString + "')]/..";
		Number n = selenium.getElementIndex(columnHeaderLocator);
		int columnPosition = n.intValue();
		n = selenium.getXpathCount(numberOfRowsXpath);
		int numRows = n.intValue();
		int currentRow = 1;
		String cellLocator = "xpath=" + resultTableXpath + "." + currentRow + "." + columnPosition;
		for(int row = 0; row < numRows; row++, currentRow += 2) {
			String s = selenium.getTable(cellLocator);
			results.add(s.trim());
			cellLocator = cellLocator.replaceFirst("\\." + currentRow + "\\." + columnPosition, "." + (currentRow+2) + "." + columnPosition);
		}
		return results;
	}
}
