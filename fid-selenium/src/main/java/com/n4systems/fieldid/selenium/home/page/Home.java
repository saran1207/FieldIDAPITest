package com.n4systems.fieldid.selenium.home.page;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class Home {

	private FieldIdSelenium selenium;
	private MiscDriver misc;
	
	// Locators
	private String homePageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Home')]";
	private String jobsAssignedToMeTableXPathLocator = "//DIV[@id='jobs']/TABLE";
	private String jobsAssignedToMeTableLocator = "xpath=" + jobsAssignedToMeTableXPathLocator;
	private String noOpenJobsAssignedToYouCellLocator = jobsAssignedToMeTableLocator + "/tbody/tr[1]/td[contains(text(),'You currently have no open jobs assigned to you.')]";
	private String quickLinksLocator = "xpath=//div[@id='quickLinks']";
	private String gotoSectionHeaderLocator = quickLinksLocator + "/h3[contains(text(),'Go To:')]";
	private String quickLinksListLocator = "xpath=//div[@id='quickLinks']/UL[@id='quickLinkList']";
	private String viewUpcomingInspectionsLinkLocator = quickLinksListLocator + "/li/a[contains(text(),'View upcoming Inspections')]";
	private String viewInspectionHistoryForProductLinkLocator = quickLinksListLocator + "/li/a[contains(text(),'View the Inspection History for a Asset')]";
	private String findAProductLinkLocator = quickLinksListLocator + "/li/a[contains(text(),'Find a Asset')]";
	private String helpVideosLocator = "//div[@id='helpVideos']";
	private String instructionalVideosHeaderLocator = helpVideosLocator + "/h3[contains(text(),'Instructional Videos')]";
	private String instructionalVideosMoreLinkLocator = instructionalVideosHeaderLocator + "/span/a[contains(text(),'more')]";
	private String fieldIDIntroVideoLinkLocator = "xpath=//div[@id='introVideo']/a[@id='showVideo' and contains(text(),'Watch')]";
	private String quickSetupWizardLinkLocator = quickLinksListLocator + "/li/a[contains(text(),'Quick Setup Wizard')]";
	private String jobsSectionHeaderLocator = "xpath=//div[@id='jobs']/h3[contains(text(),'Jobs')]";

	public Home(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void assertHomePageHeader() {
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

	/**
	 * Confirms we are on the Home page and the basic elements are there.
	 * If the tenant does not have Jobs, there will be no Jobs section
	 * so we test that separately.
	 * If there is no company website information, there will be no link
	 * for the company website in the Go To section. We'll check that
	 * separately. If we aren't the tenant administrator, there will be
	 * no link for Quick Setup Wizard, so we test that separate.
	 */
	public void assertHomePage() {
		assertHomePageHeader();
		assertGoToSection();
		assertInstructionalVideosSection();
	}

	private void assertInstructionalVideosSection() {
		assertTrue("Could not find the header for the 'Instructional Videos' section", selenium.isElementPresent(instructionalVideosHeaderLocator));
		assertTrue("Could not find a 'more' link to the Instructional Videos section", selenium.isElementPresent(instructionalVideosMoreLinkLocator));
		assertTrue("Could not find the 'New to Field ID?' intro video link", selenium.isElementPresent(fieldIDIntroVideoLinkLocator));
	}

	

	public void assertGoToSection() {
		assertTrue("Could not find the header for the 'Go To:' section", selenium.isElementPresent(gotoSectionHeaderLocator));
		assertTrue("Could not find a link to 'View upcoming Inspections'", selenium.isElementPresent(viewUpcomingInspectionsLinkLocator));
		assertTrue("Could not find a link to 'View the Inspection History for a Asset'", selenium.isElementPresent(viewInspectionHistoryForProductLinkLocator));
		assertTrue("Could not find a link to 'Find a Asset'", selenium.isElementPresent(findAProductLinkLocator));
		// if there is no company website set up, there will be no link to company web site
		// if we are not the admin, there will be no quick setup wizard link
	}

	public void assertHomePageQuickSetupWizard() {
		assertTrue("Could not find the 'Qucik Setup Wizard' link", selenium.isElementPresent(quickSetupWizardLinkLocator));
	}

	public void assertHomePageJobsSection() {
		assertTrue("Could not find the header for the 'Jobs' section", selenium.isElementPresent(jobsSectionHeaderLocator));
	}

	public void clickViewUpcomingInspections() {
		assertGoToSection();
		selenium.click(viewUpcomingInspectionsLinkLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public void clickViewInspectionHistoryForAProduct() {
		assertGoToSection();
		selenium.click(viewInspectionHistoryForProductLinkLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public void clickFindAProduct() {
		assertGoToSection();
		selenium.click(findAProductLinkLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public void clickMoreForInstructionalVideos() {
		assertGoToSection();
		selenium.click(instructionalVideosMoreLinkLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	/**
	 * Given the job title, go to the job from the Home page.
	 * Assumes the job is assigned to the current user and
	 * visible on the Home page list of jobs.
	 * 
	 * @param jobTitle
	 */
	public void gotoJob(String jobTitle) {
		String locator = jobsAssignedToMeTableLocator + "/tbody/tr/td[position()=2 and text()='" + jobTitle + "']/../td/a[contains(text(),'view')]";
		if(selenium.isElementPresent(locator)) {
			selenium.click(locator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a 'view' link to the Job with title '" + jobTitle + "'");
		}
	}
}
