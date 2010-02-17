package com.n4systems.fieldid.selenium.home.page;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;


public class Home {

	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String homePageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Home')]";
	private String jobsAssignedToMeTableXPathLocator = "//DIV[@id='jobs']/TABLE";
	private String jobsAssignedToMeTableLocator = "xpath=" + jobsAssignedToMeTableXPathLocator;
	private String noOpenJobsAssignedToYouCellLocator = jobsAssignedToMeTableLocator + "/tbody/tr[1]/td[contains(text(),'You currently have no open jobs assigned to you.')]";
	

	public Home(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifyHomePageHeader() {
		assertTrue("Could not find the header for the Home page", selenium.isElementPresent(homePageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public List<String> getJobIDsAssignedToMe() {
		List<String> result = new ArrayList<String>();
		int row = 2;
		int column = 0;
		String jobIDCellLocator = jobsAssignedToMeTableLocator + "." + row + "." + column;
		if(isJobsAssignedToMe() && true) {
			String jobsAssignedToMeRowXPathLocator = jobsAssignedToMeTableXPathLocator + "/tbody/tr[position() > " + row + "]";
			Number n = selenium.getXpathCount(jobsAssignedToMeRowXPathLocator);
			int numRows = n.intValue();
			for(int currentRow = row; currentRow < numRows+row; currentRow++) {
				String s = selenium.getTable(jobIDCellLocator);
				result.add(s);
				jobIDCellLocator = jobIDCellLocator.replaceFirst("\\." + currentRow + "\\.", "." + (currentRow+1) + ".");
			}
		}
		return result;
	}

	public boolean isJobsAssignedToMe() {
		boolean result = !selenium.isElementPresent(noOpenJobsAssignedToYouCellLocator);
		return result;
	}
}
