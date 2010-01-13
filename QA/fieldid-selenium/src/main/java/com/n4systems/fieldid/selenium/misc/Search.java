package com.n4systems.fieldid.selenium.misc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import com.thoughtworks.selenium.Selenium;

public class Search extends Assert {
	Selenium selenium;
	Misc misc;

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

	public Search(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void clearSearchCriteriaForm() {
		if(selenium.isElementPresent(clearFormButtonLocator)) {
			selenium.click(clearFormButtonLocator);
		}
	}

	public void expandSelectDisplayColumns() {
		misc.info("Expand the Select Display Columns");
		if(selenium.isElementPresent(openSelectDisplayColumnsLinkLocator)) {
			selenium.click(openSelectDisplayColumnsLinkLocator);
		} else {
			fail("Could not locate the link to expand the Select Display Columns");
		}
	}
	
	public void collapseSelectDisplayColumns() {
		misc.info("Collapse the Select Display Columns");
		if(selenium.isElementPresent(closeSelectDisplayColumnsLinkLocator)) {
			selenium.click(closeSelectDisplayColumnsLinkLocator);
		} else {
			fail("Could not locate the link to collapse the Select Display Columns");
		}
	}


	public void expandSearchCriteria() {
		misc.info("Expand the Search Criteria");
		if(selenium.isElementPresent(openSearchCriteriaLinkLocator)) {
			selenium.click(openSearchCriteriaLinkLocator);
		} else {
			fail("Could not locate the link to expand the Search Criteria");
		}
	}
	
	public void collapseSearchCriteria() {
		misc.info("Collapse the Search Criteria");
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
			try { Thread.sleep(1000); } catch(Exception e) { }
			updating = selenium.isElementPresent(availableColumnsUpdatingLocator);
			secondsLeft--;
		} while(updating && secondsLeft > 0);

		do {
			try { Thread.sleep(1000); } catch(Exception e) { }
			updating = selenium.isElementPresent(availableColumnsUpdatedLocator);
			secondsLeft--;
		} while(updating && secondsLeft > 0);
	}

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
}
