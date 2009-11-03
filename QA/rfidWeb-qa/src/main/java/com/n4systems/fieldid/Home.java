package com.n4systems.fieldid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.admin.ManageSystemSettings;

import junit.framework.TestCase;
import static watij.finders.FinderFactory.*;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;

public class Home extends TestCase {

	IE ie = null;
	Schedule schedule = null;
	Reporting reporting = null;
	Assets assets = null;
	MyAccount myAccount = null;
	FieldIDMisc misc = null;
	Admin admin = null;
	ManageSystemSettings mss = null;
	Properties p;
	InputStream in;
	String propertyFile = "home.properties";
	Finder homeFinder;
	Finder homeContentHeaderFinder;
	Finder homeSmartSearchTextFieldFinder;
	Finder homeViewUpcomingInspectionsFinder;
	Finder homeViewTheInspectionHistoryForAProductFinder;
	Finder homeFindAProductFinder;
	Finder homeChangeYourPasswordFinder;
	Finder homeSearchButtonFinder;
	Finder homeNewFeatureFinder;
	Finder homeInstructionalVideosFinder;
	Finder instructionalVideosContentHeaderFinder;
	Finder jobsSectionHeaderFinder;
	Finder homeJobsTableMessageFinder;
	Finder homeJobsTableHeaderFinder;
	Finder jobTitlesFinder;
	int maxJobsOnHomePage;
	String jobsTextPart1;
	String jobsTextPart2;
	String jobsTableHeader1;
	String jobsTableHeader2;
	String jobsTableHeader3;
	Finder newFeatureListFinder;
	Finder videoListFinder;
	Finder gotoSectionFinder;
	Finder gotoSectionHeaderFinder;
	Finder companyWebSiteURLFinder;
	
	/**
	 * Assumes the home.properties file is in the correct directory.
	 *  
	 * @param ie - An initialized instance of Internet Explorer, logged into Field ID.
	 */
	public Home(IE ie) {
		this.ie = ie;
		try {
			schedule = new Schedule(ie);
			reporting = new Reporting(ie);
			assets = new Assets(ie);
			myAccount = new MyAccount(ie);
			misc = new FieldIDMisc(ie);
			admin = new Admin(ie);
			mss = new ManageSystemSettings(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			homeFinder = id(p.getProperty("link", "NOT SET"));
			homeContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			homeSmartSearchTextFieldFinder = id(p.getProperty("smartsearchtextfield", "NOT SET"));
			homeViewUpcomingInspectionsFinder = text(p.getProperty("viewupcominginspections", "NOT SET"));
			homeViewTheInspectionHistoryForAProductFinder = text(p.getProperty("viewinspectionhistory", "NOT SET"));
			homeFindAProductFinder = text(p.getProperty("findaproduct", "NOT SET"));
			homeChangeYourPasswordFinder = xpath(p.getProperty("changeyourpassword", "NOT SET"));
			homeSearchButtonFinder = id(p.getProperty("searchbutton", "NOT SET"));
			homeNewFeatureFinder = xpath(p.getProperty("newfeatures", "NOT SET"));
			homeInstructionalVideosFinder = xpath(p.getProperty("instructionalvideos", "NOT SET"));
			instructionalVideosContentHeaderFinder = xpath(p.getProperty("instructionalvideoscontentheader", "NOT SET"));
			jobsSectionHeaderFinder = xpath(p.getProperty("jobssectionheader", "NOT SET"));
			homeJobsTableMessageFinder = xpath(p.getProperty("jobtablemessage", "NOT SET"));
			homeJobsTableHeaderFinder = xpath(p.getProperty("jobtableheader", "NOT SET"));
			jobTitlesFinder = xpath(p.getProperty("jobtitles", "NOT SET"));
			maxJobsOnHomePage = Integer.parseInt(p.getProperty("maxjobslisted", "NOT SET"));
			jobsTextPart1 = p.getProperty("jobstextpart1", "NOT SET");
			jobsTextPart2 = p.getProperty("jobstextpart2", "NOT SET");
			jobsTableHeader1 = p.getProperty("jobstableheader1", "NOT SET");
			jobsTableHeader2 = p.getProperty("jobstableheader2", "NOT SET");
			jobsTableHeader3 = p.getProperty("jobstableheader3", "NOT SET");
			newFeatureListFinder = xpath(p.getProperty("newfeaturelist", "NOT SET"));
			videoListFinder = xpath(p.getProperty("videolist", "NOT SET"));
			gotoSectionFinder = id(p.getProperty("gotosection", "NOT SET"));
			gotoSectionHeaderFinder = xpath(p.getProperty("gotosectionheader", "NOT SET"));
			companyWebSiteURLFinder = xpath(p.getProperty("companywebsiteurl", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}

	/**
	 * Go to the Home page by clicking on the Home icon in the navigation bar.
	 * 
	 * @throws Exception
	 */
	public void gotoHome() throws Exception {
		Link homeLink = ie.link(homeFinder);
		assertTrue("Could not find the anchor to Home page", homeLink.exists());
		homeLink.click();
		ie.waitUntilReady();
		checkHomePageContentHeader();
	}
	
	public void validateHomePage(boolean jobs) throws Exception {
		checkHomePageContentHeader();
		checkHomePageGoToSection();
		checkGotoViewUpcomingInspections();
		checkGotoViewTheInspectionHistoryForAProduct();
		checkGotoFindAProduct();
		checkGotoChangeYourPassword();
		if(jobs) {
			checkJobsSectionHeader();
		}
	}
	
	private void checkHomePageGoToSection() throws Exception {
		Div gotoSection = ie.div(gotoSectionFinder);
		assertTrue("Could not find the section for 'Go To'", gotoSection.exists());
		HtmlElement header = ie.htmlElement(gotoSectionHeaderFinder);
		assertTrue("Could not find the 'Go To: ' header", header.exists());
	}

	public void checkHomePageContentHeader() throws Exception {
		HtmlElement homeContentHeader = ie.htmlElement(homeContentHeaderFinder);
		assertTrue("Could not find Home page content header '" + p.getProperty("contentheader", "NOT SET") + "'", homeContentHeader.exists());
	}

	/**
	 * Puts focus on the Smart Search text field.
	 * 
	 * @throws Exception
	 * @deprecated
	 */
	public void gotoHomeSmartSearchTextBox() throws Exception {
		TextField ss = helperSmartSearch();
		ss.click();
	}
	
	/**
	 * Go to the Product Information page using the Smart Search on
	 * the Home page. This is like the gotoProductInformation method
	 * but much faster.
	 * 
	 * As of 2009.5 the smart search has been moved to be available on
	 * the main header section, i.e. every main page.
	 * 
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoProductInformationViaSmartSearch(String serialNumber) throws Exception {
		assertNotNull(serialNumber);
		assertFalse(serialNumber.equals(""));
		assets.SmartSearch(serialNumber);
	}
	
	private TextField helperSmartSearch() throws Exception {
		gotoHome();
		TextField ss = ie.textField(homeSmartSearchTextFieldFinder);
		assertTrue("Could not find Home page Smart Search Text Field '" + p.getProperty("homesmartsearchtextfield", "NOT SET") + "'", ss.exists());
		return ss;
	}
	
	/**
	 * Goes to the View Upcoming Inspections page and checks the page header.
	 * 
	 * @throws Exception
	 */
	public void gotoViewUpcomingInspections() throws Exception {
		gotoHome();
		Link viewUpcomingInspections = checkGotoViewUpcomingInspections();
		viewUpcomingInspections.click();
		ie.waitUntilReady();
		schedule.checkSchedulePageContentHeader();
	}
	
	private Link checkGotoViewUpcomingInspections() throws Exception {
		Link viewUpcomingInspections = ie.link(homeViewUpcomingInspectionsFinder);
		assertTrue(viewUpcomingInspections.exists());
		return viewUpcomingInspections;
	}
	
	/**
	 * goes to the View The Inspection History For A Product link.
	 * 
	 * @throws Exception
	 */
	public void gotoViewTheInspectionHistoryForAProduct() throws Exception {
		gotoHome();
		Link viewInspectionHistory = checkGotoViewTheInspectionHistoryForAProduct();
		viewInspectionHistory.click();
		ie.waitUntilReady();
		reporting.checkReportingPageContentHeader();
	}
	
	private Link checkGotoViewTheInspectionHistoryForAProduct() throws Exception {
		Link viewInspectionHistory = ie.link(homeViewTheInspectionHistoryForAProductFinder);
		assertTrue(viewInspectionHistory.exists());
		return viewInspectionHistory;
	}
	
	/**
	 * Goes to the Find A Product link on Home page.
	 * 
	 * @throws Exception
	 */
	public void gotoFindAProduct() throws Exception {
		gotoHome();
		Link findAProduct = checkGotoFindAProduct();
		findAProduct.click();
		assets.checkAssetsPageContentHeader();
	}
	
	private Link checkGotoFindAProduct() throws Exception {
		Link findAProduct = ie.link(homeFindAProductFinder);
		assertTrue("Could not find a link to 'Find A Product'", findAProduct.exists());
		return findAProduct;
	}
	
	/**
	 * Goes to the Change Your Password link on the Home page.
	 * 
	 * @throws Exception
	 */
	public void gotoChangeYourPassword() throws Exception {
		gotoHome();
		Link changeYourPassword = checkGotoChangeYourPassword();
		changeYourPassword.click();
		myAccount.checkMyAccountChangePasswordContent();
	}
	
	private Link checkGotoChangeYourPassword() throws Exception {
		Link changeYourPassword = ie.link(homeChangeYourPasswordFinder);
		assertTrue("Could not find a link to 'Change your password'", changeYourPassword.exists());
		return changeYourPassword;
	}
	
	/**
	 * Goes to the New Feature link.
	 * 
	 * @throws Exception
	 */
	public void gotoNewFeatures() throws Exception {
		Link newFeatureLink = ie.link(homeNewFeatureFinder);
		assertTrue("Could not find the 'More' link to New Features", newFeatureLink.exists());
		newFeatureLink.click();
		ie.waitUntilReady();
		
		// Release Notes will open in a new browser.
		IE child = ie.childBrowser(0);
		assertTrue("A new window with the release notes could not be found.", child.exists());
		misc.checkForOopsPage(child);
		child.close();
	}
	
	/**
	 * Goes to the Instructional Video link.
	 * 
	 * @throws Exception
	 */
	public void gotoInstructionalVideos() throws Exception {
		Link instructionalVideosLink = ie.link(homeInstructionalVideosFinder);
		assertTrue("Could not find the 'More' link to Instructional Videos", instructionalVideosLink.exists());
		instructionalVideosLink.click();
		checkInstructionalVideosContentHeader();
	}
	
	/**
	 * Checks the Instructional Video page header.
	 * 
	 * @throws Exception
	 */
	private void checkInstructionalVideosContentHeader() throws Exception {
		HtmlElement instVideosContentHeader = ie.htmlElement(instructionalVideosContentHeaderFinder);
		assertTrue("Could not find Instructional Videos page content header '" + p.getProperty("contentheader", "NOT SET") + "'", instVideosContentHeader.exists());
	}
	
	/**
	 * Returns a list of the Jobs listed on the Home page.
	 * If there are no open jobs assigned to the user, this
	 * will return an empty list, i.e. the result will have
	 * (getJobsOnHomePage().isEmpty() == true) if there are
	 * no open jobs.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getJobsOnHomePage() throws Exception {
		List<String> results = new ArrayList<String>();
		boolean nojobs = checkForNoOpenJobs();
		if(!nojobs) {
			checkJobsSectionHeader();
			checkJobsTable();
			TableCells jobTitles = ie.cells(jobTitlesFinder);
			assertNotNull("Could not find the cells of the Jobs table", jobTitles);
			int numJobs = jobTitles.length();
			assertTrue("More than " + maxJobsOnHomePage + " jobs on Jobs table.", numJobs <= maxJobsOnHomePage);
			for(int i = 0; i < numJobs; i++) {
				TableCell jobTitle = jobTitles.get(i);
				assertTrue("Could not find the title for job " + i, jobTitle.exists());
				results.add(jobTitle.text());
			}
		}
		return results;
	}

	/**
	 * Checks to see if there are not open jobs. Returns
	 * true if no jobs.
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkForNoOpenJobs() throws Exception {
		boolean result = true;
		TableCell jobsMessage = ie.cell(homeJobsTableMessageFinder);
		String message = jobsMessage.text();
		result = message.contains("You currently have no open jobs assigned to you.");
		return result;
	}

	/**
	 * If there are jobs, this checks the table to make sure
	 * it is well formed.
	 * 
	 * @throws Exception
	 */
	private void checkJobsTable() throws Exception {
		TableCell jobsMessage = ie.cell(homeJobsTableMessageFinder);
		String message = jobsMessage.text();
		assertTrue("The message '" + jobsTextPart1 + "' is missing from the Jobs table.", message.contains(jobsTextPart1));
		assertTrue("The message '" + jobsTextPart2 + "' is missing from the Jobs table.", message.contains(jobsTextPart2));
		TableRow jobsTableHeader = ie.row(homeJobsTableHeaderFinder);
		TableCell jobID = jobsTableHeader.cell(0);
		TableCell jobTitle = jobsTableHeader.cell(1);
		TableCell jobStatus = jobsTableHeader.cell(2);
		String jobIDText = jobID.text();
		String jobTitleText = jobTitle.text();
		String jobStatusText = jobStatus.text();
		assertTrue("First column of Jobs table is not '" + jobsTableHeader1 + "'. Got '" + jobIDText + "'.", jobsTableHeader1.equals(jobIDText));
		assertTrue("Second column of Jobs table is not '" + jobsTableHeader2 + "'. Got '" + jobTitleText + "'.", jobsTableHeader2.equals(jobTitleText));
		assertTrue("Third column of Jobs table is not '" + jobsTableHeader3 + "'. Got '" + jobStatusText + "'.", jobsTableHeader3.equals(jobStatusText));
	}

	/**
	 * Checks for the Jobs header tag on the Jobs section of the Home page.
	 * 
	 * @throws Exception
	 */
	public void checkJobsSectionHeader() throws Exception {
		HtmlElement jobsSectionHeader = ie.htmlElement(jobsSectionHeaderFinder);
		assertTrue("Could not find Jobs section header '" + p.getProperty("jobssectionheader", "NOT SET") + "'", jobsSectionHeader.exists());
	}
	
	/**
	 * Get the list of bullets under the New Features section.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getNewFeatures() throws Exception {
		List<String> results = new ArrayList<String>();
		HtmlElement newFeatureList = ie.htmlElement(newFeatureListFinder);
		assertTrue("Could not find the New Feature list.", newFeatureList.exists());
		HtmlElements newFeatures = newFeatureList.htmlElements(tag("LI"));
		for(int i = 0; i < newFeatures.length(); i++) {
			String newFeature = newFeatures.get(i).text().trim();
			results.add(newFeature);
		}
		return results;
	}
	
	/**
	 * Get a list of the bullets under the Instructional Videos section.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getInstructionalVideoList() throws Exception {
		List<String> results = new ArrayList<String>();
		HtmlElement videoList = ie.htmlElement(videoListFinder);
		assertTrue("Could not find the Instructional Video list.", videoList.exists());
		HtmlElements videos = videoList.htmlElements(tag("LI"));
		for(int i = 0; i < videos.length(); i++) {
			String video = videos.get(i).text().trim();
			results.add(video);
		}
		return results;
	}

	public Link getCompanyWebSiteLink() throws Exception {
		Link companyURL = ie.link(companyWebSiteURLFinder);
		assertTrue("Could not find a link to the company website", companyURL.exists());
		return companyURL;
	}

	public void checkCompanyWebSiteLink() throws Exception {
		admin.gotoAdministration();
		String linkText = admin.getCompanyName() + " Web Site";
		gotoHome();
		Link companyLink = getCompanyWebSiteLink();
		assertTrue("The text for the company web site does not match the company name", linkText.equals(companyLink.text()));
	}

	public void gotoCompanyWebsite() throws Exception {
		Link companyLink = getCompanyWebSiteLink();
		companyLink.click();
	}

	public boolean isCompanyWebsite() throws Exception {
		Link companyURL = ie.link(companyWebSiteURLFinder);
		return companyURL.exists();
	}

	/**
	 * 
	 * @param jobs
	 * @param newFeaturePDF - if new features PDF not ready, set to false
	 * @throws Exception
	 */
	public void validate(boolean jobs, boolean newFeaturePDF) throws Exception {
		String testURL = "http://www.google.com/";
		String testTitle = "Google";	// can be full title or part of title
		admin.gotoAdministration();
		mss.gotoManageSystemSettings();
		String original = mss.getWebSiteAddress();
		mss.setWebSiteAddress(testURL);
		mss.saveChangesToSystemSettings();
		try {
			gotoHome();
			validateHomePage(jobs);
			if(isCompanyWebsite()) {
				checkCompanyWebSiteLink();
				Link url = getCompanyWebSiteLink();
				assertEquals("The getCompanyWebSiteLink() method did not return the correct value.", testURL, url.href());
				gotoCompanyWebsite();
				IE ie2 = ie.childBrowser();
				assertNotNull("Could not find the new browser with the 'company' website", ie2);
				String title = ie2.title();
				assertTrue("Was expecting the 'company' website to be " + testTitle + " but found '" + title + "'", title.contains(testTitle));
				ie2.close();
			}
			admin.gotoAdministration();
			mss.gotoManageSystemSettings();
			mss.setWebSiteAddress(original);
			mss.saveChangesToSystemSettings();
			gotoHome();
			if(jobs) {
				checkJobsSectionHeader();
				getJobsOnHomePage();
				
			}
			@SuppressWarnings("unused")
			List<String> videos = getInstructionalVideoList();
			gotoInstructionalVideos();
			gotoHome();
			gotoChangeYourPassword();
			gotoHome();
			gotoViewTheInspectionHistoryForAProduct();
			gotoHome();
			gotoFindAProduct();
			gotoHome();
			gotoViewUpcomingInspections();
			gotoHome();
			@SuppressWarnings("unused")
			List<String> newFeatures = getNewFeatures();
			if(newFeaturePDF) {
				gotoNewFeatures();
			}
//			gotoProductInformationViaSmartSearch(serialNumber);	// tested by Smoke Test
			gotoHome();
		} catch (Exception e) {
			throw e;
		} finally {
			// if anything fails, restore the original company website
			admin.gotoAdministration();
			mss.gotoManageSystemSettings();
			mss.setWebSiteAddress(original);
			mss.saveChangesToSystemSettings();
		}
	}
}
