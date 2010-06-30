package com.n4systems.fieldid.selenium.misc;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;

public class MiscDriver {

	public static Random r = new Random(System.currentTimeMillis());
	public static final String DEFAULT_TIMEOUT = "60000";
	// Current timeout for session is 30 minutes. This is set to 31 minutes
	// just to be safe. That is, session will timeout in 30 minutes but it
	// might take the javascript another minute to detect the session has
	// timed out and display the lightbox.
	public static final int sessionTimeoutLightBoxTimeout = 31 * 60;
	//javascript functions should complete dom updates in under a second (this is not for ajax that can take longer.)
	public static final String JS_TIMEOUT = "1000";
	public static final String AJAX_TIMEOUT = "2000";
	private static String snapshots = null;
	private FieldIdSelenium selenium;
	private Search search;
	
	// Locators
	private String n4systemLinkLocator = "xpath=//DIV[@id='copyright']/A[contains(text(),'N4 Systems Inc.')]";
	private String homeIconLocator = "xpath=//A[@id='menuHome']";
	private String identifyIconLocator = "xpath=//A[@id='menuIdentify']";
	private String inspectIconLocator = "xpath=//A[@id='menuInspect']";
	private String assetsIconLocator = "xpath=//A[@id='menuAssets']";
	private String reportingIconLocator = "xpath=//A[@id='menuReport']";
	private String scheduleIconLocator = "xpath=//A[@id='menuSchedule']";
	private String safetyNetworkIconLocator = "xpath=//A[@id='menuSafetyNetwork']";
	private String adminIconLocator = "xpath=//A[@id='menuSetup']";
	private String projectsIconLocator = "xpath=//A[@id='menuProject']";
	private String n4systemWindowLocator = "title=N4 Systems | Safety Through Innovation";
	private String thawteAboutSSLCertificatesLinkLocator = "xpath=//DIV[@id='sslCert']/A[contains(text(),'ABOUT SSL CERTIFICATES')]";
	private String thawteWindowLocator = "title=Thawte Products- SSL certificates and SSL Certificates with extended validation (EV SSL) from Thawte the global SSL certificate authority";
	private String securedByThawteLinkLocator = "xpath=//DIV[@id='sslCert']/A[contains(@target,'THAWTE_Splash')]";
	private String thawteSiteSealWindowLocator = "THAWTE_Splash";
	private String signOutLinkLocator = "xpath=//A[contains(text(),'Sign Out')]";
	private String errorMessageXpathCount = "//*[@class='errorMessage' and not(contains(@style,'display: none'))]";
	private String errorMessageLocator = "xpath=//UL/LI[1]/*[@class='errorMessage' and not(contains(@style,'display: none'))]";
	private String fieldIDEndUserLicenseAgreementHeaderLocator = "xpath=//H1[contains(text(),'Field ID End User Licence Agreement')]";
	private String eulaLegalTextLocator = "xpath=//FORM[@id='eulaForm']/DIV[@class='sectionContent']/DIV[@class='infoSet']/DIV[@id='eulaLegalText']/PRE";
	private String acceptEULAButtonLocator = "xpath=//INPUT[@id='acceptEula']";
	private String toggleEulaScript = "toggleEula();";
	private String actionMessageLocator = "xpath=//UL/LI[1]/*[@class='actionMessage']";
	private String actionMessageXpathCount = "//*[@class='actionMessage']";
	private String myAccountLinkLocator = "xpath=//A[contains(text(),'My Account')]";
	private String loadFindButtonLocator = "xpath=//INPUT[@id='smartSearchButton']";
	private Object blankFindAlertMessage = "you can not search for a blank serial number or rfid number";
	private String vendorContextNameLinkLocator = "xpath=//SPAN[@id='vendorContextNameLink']/A";
	private String cancelVendorContextChangeLinkLocator = "xpath=//A[@id='cancelVendorSwitch']";
	private String vendorContextSelectListLocator = "xpath=//SELECT[@id='vendorContext']";
	private String lightboxLocator = "xpath=//DIV[@id='lightview' and not(contains(@style,'display: none'))]";
	private String nextPageLinkLocator = "xpath=//A[contains(text(),'Next')]";
	private String prevPageLinkLocator = "xpath=//A[contains(text(),'Previous')]";
	private String firstPageLinkLocator = "xpath=//A[contains(text(),'First')]";
	private String lastPageLinkLocator = "xpath=//A[contains(text(),'Last')]";
	private String goToPageTextFieldLocator = "xpath=//INPUT[@id='toPage']";
	private String labelForLastPageLocator = "xpath=//LABEL[@for='lastPage']";
	private String selectOwnerOrganizationSelectListLocator = "xpath=//SELECT[@id='orgList']";
	private String selectOwnerCustomerSelectListLocator = "xpath=//SELECT[@id='customerList']";
	private String selectOwnerDivisionSelectListLocator = "xpath=//SELECT[@id='divisionList']";
	private String chooseOwnerLinkLocator = "xpath=//A[@class='searchOwner' and contains(text(),'Choose')]";
	private String chooseOwnerSelectButtonLocator = "xpath=//INPUT[@id='selectOrg']";
	private String chooseOwnerCancelButtonLocator = "xpath=//INPUT[@id='cancelOrgSelect']";
	private String orgBrowserLoadingImageLocator = "xpath=//DIV[@id='orgBrowserLoading' and not(contains(@style,'display: none'))]";
	private String smartSearchTextFieldLocator = "xpath=//INPUT[@id='searchText']";
	private String smartSearchLoadButtonLocator = "xpath=//INPUT[@id='smartSearchButton']";
	
	public MiscDriver(FieldIdSelenium selenium) {
		this.selenium = selenium;
		search = new Search(selenium, this);
	}
	
	public Search getSearch() {
		return search;
	}
	
	/**
	 * Wait for a page to use. Uses the Misc.defaultTimeout. After the page
	 * has loaded we do a check for the Oops page and fail if an Oops page was
	 * found.
	 */
	public void waitForPageToLoadAndCheckForOopsPage() {
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		if(isOopsPage()) {
			fail("Got an Oops page. Check Field ID logs.");
		}
	}
	
	
	
	/**
	 * Select the link on the Login page for the N4 System Inc. web page.
	 * This will open the web page in a new window, confirm it was found then
	 * close the window.
	 * 
	 */
	public void popupN4SystemsInc() {
		if(selenium.isElementPresent(n4systemLinkLocator)) {
			selenium.click(n4systemLinkLocator);
			if(!confirmPopupWindow(n4systemWindowLocator)) {
				fail("Could not find the N4 System Inc. window");
			}
		} else {
			fail("Could not find the link N4 Systems Inc.");
		}
	}
	
	/**
	 * Clicks the Secured By Thawte logo on the login page.
	 * @deprecated times out for some reason. don't use it!
	 */
	public void popupThawteSiteSeal() {
		if(selenium.isElementPresent(securedByThawteLinkLocator)) {
			selenium.click(securedByThawteLinkLocator);
			if(!confirmPopupWindow(thawteSiteSealWindowLocator)) {
				fail("Could not find the Thawte siteseal window");
			}
		}
	}
	
	private boolean confirmPopupWindow(String locator) {
		try {
			selenium.waitForPopUp("", DEFAULT_TIMEOUT);
			selenium.selectWindow(locator);
			selenium.close();
			selenium.selectWindow("");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public String promptForUserInput(String timeout) {
		String jsFunctionName = "promptUser()";
		String jsFunction = "function " + jsFunctionName + " { var result = prompt(\"Input: \", \"\"); return result; }";
		selenium.setTimeout(timeout);
		selenium.addScript(jsFunction, "");
		return selenium.getEval(jsFunctionName);
	}
	
	/**
	 * If present this will click the link "ABOUT SSL CERTIFICATES" on the
	 * login page. This link is only present when the domain is *.fieldid.com.
	 * If this link is not present, this method will do nothing.
	 * 
	 * To simulate this on a test machine, set www.field.com to the IP address
	 * of the test machine in the test machine's hosts file. Then run the
	 * tests with the host set to fieldid.fieldid.com.
	 * 
	 */
	public void popupThawteDotCom() {
		if(selenium.isElementPresent(thawteAboutSSLCertificatesLinkLocator)) {
			selenium.click(thawteAboutSSLCertificatesLinkLocator);
			if(!confirmPopupWindow(thawteWindowLocator)) {
				fail("Could not find the Thawte Products window");
			}
		}
	}
	
	/**
	 * This message will return a List<String> object. If there were
	 * any error messages on the FORM, the list will not be empty. If
	 * the list is empty, no error messages were found on the FORM.
	 * 
	 * The error messages will be those which appear at the top of the
	 * page. These are different from the error messages which occur
	 * when submitting a form.
	 * 
	 * The danger of this method is that if the error message locator
	 * changes, this method will return an empty list even when there
	 * are errors. There MUST be a test case that creates errors on a
	 * page and confirms they exist. If the locator changes, that test
	 * case will fail and we know this method is broken.
	 * 
	 * @return a list of error messages or an empty list if no errors.
	 */
	public List<String> getFormErrorMessages() {	// gets class="errorMessage"
		List<String> result = new ArrayList<String>();
		String iterableErrorMessageLocator = errorMessageLocator;
		if(selenium.isElementPresent(errorMessageLocator)) {
			Number n = selenium.getXpathCount(errorMessageXpathCount);
			int maxIndex = n.intValue();
			for(int i = 1; i <= maxIndex; i++) {
				String s = selenium.getText(iterableErrorMessageLocator);
				result.add(s);
				String oldIndex = String.valueOf(i);
				String newIndex = String.valueOf(i+1);
				iterableErrorMessageLocator = iterableErrorMessageLocator.replace(oldIndex, newIndex);
			}
		}
		return result;
	}
	
	/**
	 * Takes a given List<String> and turns it into a single String
	 * value. It will concatenate all the Strings in the list but
	 * separating them with newline characters.
	 *  
	 * @param in
	 * @return All the strings concatenated into a single string
	 */
	public String convertListToString(List<String> in) {
		String result = "";
		Iterator<String> i = in.iterator();
		while(i.hasNext()) {
			result += "\n";
			result += i.next();
		}
		return result;
	}
	
	/**
	 * When something is successful and stays on the same page a message is
	 * displayed at the top of the form with a green background and a class of
	 * "actionMessage". This method will collect all the actionMessage entries
	 * and return them as a List of strings.
	 * 
	 * @return
	 */
	public List<String> getActionMessages() {	// gets class="actionMessage"
		List<String> result = new ArrayList<String>();
		String iterableActionMessageLocator = actionMessageLocator;
		if(selenium.isElementPresent(actionMessageLocator)) {
			Number n = selenium.getXpathCount(actionMessageXpathCount);
			int maxIndex = n.intValue();
			for(int i = 1; i <= maxIndex; i++) {
				String s = selenium.getText(iterableActionMessageLocator);
				result.add(s);
				String oldIndex = String.valueOf(i);
				String newIndex = String.valueOf(i+1);
				iterableActionMessageLocator = iterableActionMessageLocator.replace(oldIndex, newIndex);
			}
			
		}
		return result;
	}
	
	/**
	 * A little helper method for all the goto*() method below.
	 * The text is the text of the anchor we are trying to click. This is for
	 * log and error messages. It is purely cosmetic. The locator is what
	 * finds the link and clicks it.
	 * 
	 * @param text
	 * @param locator
	 */
	private void gotoHelper(String text, String locator) {
		if(selenium.isElementPresent(locator)) {
			selenium.click(locator);
		} else {
			fail("Could not find the '" + text + "' link.");
		}
		waitForPageToLoadAndCheckForOopsPage();
	}
	
	/**
	 * Click the Home icon and check for an Oops page.
	 */
	public void gotoHome() {
		gotoHelper("Home", homeIconLocator);
	}
	
	/**
	 * Click the Identify icon and check for an Oops page.
	 */
	public void gotoIdentify() {
		gotoHelper("Identify", identifyIconLocator);
	}
	
	/**
	 * Click the Inspect icon and check for an Oops page.
	 */
	public void gotoInspect() {
		gotoHelper("Inspect", inspectIconLocator);
	}
	
	/**
	 * Click the Assets icon and check for an Oops page.
	 */
	public void gotoAssets() {
		gotoHelper("Assets", assetsIconLocator);
	}
	
	/**
	 * Click the Reporting icon and check for an Oops page.
	 */
	public void gotoReporting() {
		gotoHelper("Reporting", reportingIconLocator);
	}
	
	/**
	 * Click the Schedule icon and check for an Oops page.
	 */
	public void gotoSchedule() {
		gotoHelper("Schedule", scheduleIconLocator);
	}
	
	/**
	 * Click the Safety Network icon and check for an Oops page.
	 */
	public void gotoSafetyNetwork() {
		gotoHelper("Safety Network", safetyNetworkIconLocator);
	}
	
	/**
	 * Click the Jobs icon and check for an Oops page.
	 */
	public void gotoJobs() {
		gotoHelper("Jobs", projectsIconLocator);
	}
	
	/**
	 * Click the Adminstration icon and check for an Oops page.
	 */
	public void gotoAdministration() {
		gotoHelper("Administration", adminIconLocator);
	}
	
	/**
	 * Click the My Account link and check for an Oops page.
	 */
	public void gotoMyAccount() {
		gotoHelper("My Account", myAccountLinkLocator);
	}
	
	
	
	
	
	/**
	 * Click the Sign Out link. Should return you to the Login page.
	 * Might behave differently for embedded logins.
	 */
	public void gotoSignOut() {
		gotoHelper("Sign Out", signOutLinkLocator);
	}
	
	/**
	 * Use the smart search to find the given input string. The string s can
	 * be a serial number, reference number or rfid number. If it is not found
	 * we expect a No Results Returned. If one asset is found it will return
	 * the View for that asset. If multiple assets are found it will give us
	 * a page with a list of the matches and we can then select one of them
	 * to end up at the View for that asset.
	 * 
	 * If the input is null or a blank string this will expect a dialog and
	 * will close it. It will confirm the alert message matches the correct
	 * string.
	 * 
	 * @param s
	 */
	public void gotoFind(String s) {
		if(selenium.isElementPresent(loadFindButtonLocator)) {
			selenium.click(loadFindButtonLocator);
			if(s == null || s.trim().equals("")) {
				String alertMessage = selenium.getAlert();
				assertEquals(blankFindAlertMessage, alertMessage);
			} else {
				waitForPageToLoadAndCheckForOopsPage();
			}
		} else {
			fail("Could not find the Load button for the Find field.");
		}
	}
	
	/**
	 * Check to see if the Identify icon is present.
	 * 
	 * @return true if Identify icon is present.
	 */
	public boolean isIdentify() {
		boolean b = selenium.isElementPresent(identifyIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Inspect icon is present.
	 * 
	 * @return true if Inspect icon is present.
	 */
	public boolean isInspect() {
		boolean b = selenium.isElementPresent(inspectIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Assets icon is present.
	 * 
	 * @return true if Assets icon is present.
	 */
	public boolean isAssets() {
		boolean b = selenium.isElementPresent(assetsIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Reporting icon is present.
	 * 
	 * @return true if Reporting icon is present.
	 */
	public boolean isReporting() {
		boolean b = selenium.isElementPresent(reportingIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Schedule icon is present.
	 * 
	 * @return true if Schedule icon is present.
	 */
	public boolean isSchedule() {
		boolean b = selenium.isElementPresent(scheduleIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Safety Network icon is present.
	 * 
	 * @return true if Safety Network icon is present.
	 */
	public boolean isSafetyNetwork() {
		boolean b = selenium.isElementPresent(safetyNetworkIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Jobs icon is present.
	 * 
	 * @return true if Jobs icon is present.
	 */
	public boolean isJobs() {
		boolean b = selenium.isElementPresent(projectsIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Administration icon is present.
	 * 
	 * @return true if Administration icon is present.
	 */
	public boolean isAdministration() {
		boolean b = selenium.isElementPresent(adminIconLocator);
		return b;
	}
	
	/**
	 * Check to see if the Field ID Store link is present.
	 * 
	 * @return true if Field ID Store link is present.
	 */
	public boolean isFieldIDStore() {
		boolean b = true;
		return b;
	}
	
	/**
	 * Check to see if we got an Oops page.
	 * 
	 * @return true if Oops page.
	 */
	public boolean isOopsPage() {
		boolean b = true;
		if(!selenium.isTextPresent("Oops - Page does not exist"))
			b = false;
		return b;
	}
	
	/**
	 * Check to see if the Home icon is present.
	 * 
	 * @return true if Home icon is present.
	 */
	public boolean isHome() {
		boolean b = selenium.isElementPresent(homeIconLocator);
		return b;
	}
	
	/**
	 * Look for the Windows process 'iexplore.exe' and kill it.
	 */
	public void killInternetExplorer() {
		String imageName = "iexplore.exe";
		killProcess(imageName);
	}
	
	/**
	 * Look for the Windows process 'firefox.exe' and kill it.
	 */
	public void killFirefox() {
		String imageName = "firefox.exe";
		killProcess(imageName);
	}
	
	private void killProcess(String imageName) {
		Runtime r = Runtime.getRuntime();
		// check to see if the process is running
		Process p = null;
		String listCommand = "tasklist /FI \"IMAGENAME eq " + imageName + "\" /NH /FO CSV";
		String killCommand = "taskkill /f /im " + imageName + " /t";
		try {
			p = r.exec(listCommand);
		} catch (IOException e) {
			fail("An IOException occurred while trying to exec '" + listCommand + "'");
		}
		
		try {
			// if running, error stream will be empty, i.e. null
			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			if(err.readLine() == null) {
				// if it is running, kill it
				r.exec(killCommand);
				Thread.sleep(5000);	// give it 5 seconds to die
			}
		} catch (IOException e) {
			fail("An IOException occurred while trying to exec '" + killCommand + "'");
		} catch (InterruptedException e) {
			// Do nothing of the sleep gets interrupted.
		} catch (Exception e) {
			fail("Some unknown error occurred in misc.killProcess(" + imageName + ")");
		}
	}
	
	/**
	 * Get today's date in the format MM/dd/yy.
	 * 
	 * @return
	 */
	public String getDateString() {
		String today = getSimpleDateFormat("MM/dd/yy");
		return today;
	}
	
	/**
	 * Get today's date in the format MM/dd/yy h:mm aa
	 * 
	 * @return
	 */
	public String getDateTimeString() {
		String today = getSimpleDateFormat("MM/dd/yy h:mm aa");
		return today;
	}
	
	private String getSimpleDateFormat(String format) {
		SimpleDateFormat now = new SimpleDateFormat(format);
		String today = now.format(new Date());
		return today;
	}
	
	/**
	 * Get a date string in the format MM/dd/yy but offset it from today by
	 * 'months'. If months is positive this will be in the future. If months
	 * is negative this will be in the past. For example, if today is 01/05/10
	 * (January 5th, 2010) then getDateStringNMonthsFromNow(-3) will return
	 * 10/05/09 (October 5th, 2009) and getDateStringNMonthsFromNow(1) will
	 * return 02/05/10 (February 5th, 2010). This method use the Calendar.add
	 * and SimpleDateFormat.format. See JDK documentation for them.
	 * 
	 * @param months
	 * @return
	 */
	public String getDateStringNMonthsFromNow(int months) {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, months);
		String monthOffset = now.format(c.getTime());
		return monthOffset;
	}
	
	/**
	 * Get a date string in the format MM/dd/yy but offset it from today by
	 * 'years'. If years is positive this will be in the future. If years
	 * is negative this will be in the past. For example, if today is 01/05/10
	 * (January 5th, 2010) then getDateStringNYearsFromNow(-3) will return
	 * 01/05/07 (January 5th, 2007) and getDateStringNYearsFromNow(1) will
	 * return 01/05/11 (January 5th, 2011). This method use the Calendar.add
	 * and SimpleDateFormat.format. See JDK documentation for them.
	 * 
	 * @param months
	 * @return
	 */
	public String getDateStringNYearsFromNow(int years) {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, years);
		String yearOffset = now.format(c.getTime());
		return yearOffset;
	}
	
	/**
	 * Generate a random RFID number. The string will be all uppercase, length
	 * of 16 characters and hexidecimal.
	 * @return
	 */
	public String getRandomRFIDNumber() {
		int maxLength = 16;
		String validCharacters = "01234567890ABCDEF";
		StringBuffer s = getRandomString(validCharacters, maxLength);
		return s.toString();
	}
	
	private static StringBuffer getRandomString(String validCharacters, int maxLength) {
		StringBuffer s = new StringBuffer(maxLength);
		for(int i = 0; i < maxLength; i++) {
			int n = r.nextInt(validCharacters.length());
			char c = validCharacters.charAt(n);
			s.append(c);
		}
		return s;
	}
	
	/**
	 * Generates a random string of all alphabetic characters. The first
	 * character will be uppercase and subsequent characters will be lowercase.
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String validCharacters = "abcdefghijklmnopqrstuvwxyz";
		StringBuffer s = getRandomString(validCharacters, length);
		
		// get the first character and convert it to uppercase
		char ch = String.valueOf(s.charAt(0)).toUpperCase().charAt(0);
		s.setCharAt(0, ch);
		return s.toString();
	}
	
	/**
	 * Waits for the session timeout lightbox to appear. It will wait for a
	 * set period of time. If the light box appears before the the timeout
	 * this will stop waiting right away. If the method reaches the timeout
	 * this will <strong>fail</strong>. 
	 */
	public void waitForSessionTimeoutLightBox() {
		assertTrue("Timeout for waitForLightBox must be positive", MiscDriver.sessionTimeoutLightBoxTimeout > 0);
		int seconds = MiscDriver.sessionTimeoutLightBoxTimeout;
		while(!selenium.isElementPresent(lightboxLocator) && seconds > 0) {
			try { Thread.sleep(1000); } catch (Exception e) { }
			seconds--;
		}
		if(seconds <= 0) {
			fail("After " + MiscDriver.sessionTimeoutLightBoxTimeout + " seconds, no light box appeared.");
		}
	}

	/**
	 * Check if the Vendor Context link is available. Assumes if it cannot
	 * find the link then it is false. You must have a test case which 
	 * expects this to return true.
	 * 
	 * @return true of Vendor Context link is available.
	 */
	public boolean isVendorContext() {
		boolean b = selenium.isElementPresent(vendorContextNameLinkLocator);
		return b;
	}
	
	/**
	 * Get the current Vendor context. This is the setting which
	 * affects the Smart Search. If the vendor context is not present
	 * this will <STRONG>fail</STRONG> the test case.
	 * 
	 * @return The name of the vendor currently set for Smart Search.
	 * @see isVendorContext
	 */
	public String getVendorContext() {
		String s = null;
		if(!isVendorContext()) {
			fail("Could not find the vendor context link");
		}
		s = selenium.getText(vendorContextNameLinkLocator);
		return s;
	}
	
	/**
	 * Click the vendor context link. This will change it into a
	 * select list. If the vendor context is not present this will
	 * <STRONG>fail</STRONG> the test case.
	 * 
	 * @see isVendorContext
	 */
	public void clickVendorContext() {
		if(isVendorContext()) {
			selenium.click(vendorContextNameLinkLocator);
		} else {
			fail("Could not find a vendor context link");
		}
	}
	
	/**
	 * Once you have confirmed vendor context is available and clicked
	 * the link, this will get the list of options from the select list.
	 * 
	 * @return list of vendors from vendor context select list 
	 */
	public List<String> getVendorContextList() {
		List<String> result = new ArrayList<String>();
		if(selenium.isElementPresent(vendorContextSelectListLocator)) {
			String[] s = selenium.getSelectOptions(vendorContextSelectListLocator);
			for(int i = 0; i < s.length; i++) {
				result.add(s[i]);
			}
		}
		return result;
	}
	
	/**
	 * Set the vendor context to the given vendor. If the vendor provided
	 * is not on the list the test case will <STRONG>fail</STRONG>.
	 * 
	 * @param s a valid vendor name
	 * @see getVendorContextList
	 */
	public void setVendorContext(String s) {
		if(selenium.isElementPresent(vendorContextSelectListLocator)) {
			selenium.select(vendorContextSelectListLocator, s);
		} else {
			fail("Could not find the vendor select list to change the vendor context");
		}
	}
	
	/**
	 * After you have selected the vendor context link you can call this
	 * to click on the cancel option.
	 */
	public void cancelVendorContext() {
		if(selenium.isElementPresent(cancelVendorContextChangeLinkLocator)) {
			selenium.click(cancelVendorContextChangeLinkLocator);
		}
	}

	/**
	 * Create a directory under the 'root' directory provided. This assumes the
	 * root directory exists and creates a new directory using the current date
	 * and time. See the SimpleDateFormat below for the exact format.
	 * 
	 * @param root
	 * @return the directory created for screen captures and other test artifacts.
	 */
	public String createTimestampDirectory(String root) {
		SimpleDateFormat now = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String timestamp = root + now.format(new Date());
		File d = new File(timestamp);
		if(!d.exists()) {
			if (d.mkdirs()) {
				snapshots = timestamp;
			}
		}
		return snapshots;
	}

	/**
	 * Returns the location where screen captures and other test artifacts
	 * can be stored.
	 * 
	 * @return directory name or null if not initialized.
	 */
	public String getTestArtifactDirectory() {
		return snapshots;
	}

	/**
	 * If the System property 'snapshots' is set to 'true' we will output
	 * snapshots whenever this method is called. By default, if the property
	 * is not set, this will not output snapshots.
	 * 
	 * @param filename
	 */
	public void captureScreenshot(String filename) {
		if(System.getProperty("snapshots", "false").equalsIgnoreCase("true")) {
			String fileSeparator = System.getProperty("file.separator", "\\");
			filename = snapshots + fileSeparator + filename;
			selenium.captureScreenshot(filename);
		}
	}

	/**
	 * Checks to see if we are on the page for accepting the End User License
	 * Agreement (EULA).
	 * 
	 * @return
	 */
	public boolean isEULA() {
		boolean result = false;
		if(selenium.isElementPresent(fieldIDEndUserLicenseAgreementHeaderLocator)) {
			result = true;
		}
		return result;
	}

	/**
	 * Ideally, this method should scroll to the bottom of the EULA
	 * thus enabling the Accept button. Since that does not seem to
	 * be possible with Selenium, we call the toggleEula() javascript.
	 * Basically, scrolling to the bottom of the EULA will cause the
	 * toggleEula() script to be executed. So we simulate the scroll
	 * by just calling the script ourselves.
	 */
	public void scrollToBottomOfEULA() {
		if(selenium.isElementPresent(eulaLegalTextLocator)) {
			try {
				selenium.runScript(toggleEulaScript);
			} catch (Exception e) {
				fail("Could not execute the '" + toggleEulaScript + "' javascript to enable the Accept button.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * This assumes you have scrolled to the bottom of the EULA and the
	 * Accept button is available. It will then accept the EULA and should
	 * continue on to the Home page.
	 */
	public void gotoAcceptEULA() {
		if(selenium.isElementPresent(acceptEULAButtonLocator)) {
			selenium.click(acceptEULAButtonLocator);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Accept button for the EULA.");
		}
	}
	
	/**
	 * Checks to see if any error messages appeared on the page.
	 * If the input is not null, it will create a screen capture using
	 * the input as the filename. It will append ".png" to the input
	 * string.
	 * 
	 * @param png
	 */
	public void checkForErrorMessages(String png) {

		List<String> errors = getFormErrorMessages();
		int otherErrors = getNonFormErrorMessages();
		if(isOopsPage()) {
			fail("Got the Oops page. Check the fieldid.log.");
		} else if(errors.size() > 0) {
			fail("There were errors on the page: " + convertListToString(errors));
		} else if(otherErrors > 0) {
			fail("There were non-form errors on the page");
		}
	}
	
	private int getNonFormErrorMessages() {
		Number n = selenium.getXpathCount(errorMessageXpathCount);
		return n.intValue();
	}

	/**
	 * This will delete the session cookie then call the javascript to check
	 * for a session timeout. The testSession() method is part of Field ID.
	 * The domain is the domain the jsessionid cookie is associated with. For
	 * example, halo.team.n4systems.net or .team.n4systems.net. The selenium
	 * deleteCookie method will recurse through all the subdomains. So if you
	 * use .n4systems.net it would work for *.grumpy.n4systems.net and for
	 * *.team.n4systems.net.
	 * 
	 * @param domain
	 */
	public void forceSessionTimeout(String domain) {
		selenium.deleteCookie("JSESSIONID", "path=/,domain=" + domain + ",recurse=true");
		selenium.runScript("testSession();");
		int timeSlice = 5;	// seconds
		int maxSeconds = 720;
		int currentSeconds = 0;
		do {
			if(isSessionExpired())
				return;
			sleep(timeSlice);
			currentSeconds += timeSlice;
		} while(currentSeconds < maxSeconds);
		fail("Session Expired lightbox never appeared. Waited " + maxSeconds + " seconds after forcing a timeout");
	}

	/**
	 * Go to the 'Next >' page on any paginated page.
	 * 
	 * @return true if it found a 'next' page link.
	 */
	public boolean gotoNextPage() {
		boolean result = true;
		if(selenium.isElementPresent(nextPageLinkLocator)) {
			selenium.click(nextPageLinkLocator);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Go to the '< Previous' page on any paginated page.
	 * 
	 * @return true if it found a 'previous' page link.
	 */
	public boolean gotoPrevPage() {
		boolean result = true;
		if(selenium.isElementPresent(prevPageLinkLocator)) {
			selenium.click(prevPageLinkLocator);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Go to the 'First' page on any paginated page.
	 * 
	 * @return true if it found a 'First' page link.
	 */
	public boolean gotoFirstPage() {
		boolean result = true;
		if(selenium.isElementPresent(firstPageLinkLocator)) {
			selenium.click(firstPageLinkLocator);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Go to the 'Last' page on any paginated page.
	 * 
	 * @return true if it found a 'Last' page link.
	 */
	public boolean gotoLastPage() {
		boolean result = true;
		if(selenium.isElementPresent(lastPageLinkLocator)) {
			selenium.click(lastPageLinkLocator);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Enters a number into the Go To Page field. You can use this to enter
	 * invalid numbers as well as valid numbers. For example, if there are
	 * seven pages you can enter 8 and it should handle that with an error
	 * message.
	 * 
	 * @param pageNumber
	 */
	public void gotoPage(int pageNumber) {
		if(selenium.isElementPresent(goToPageTextFieldLocator)) {
			String s = String.valueOf(pageNumber) + "\n";
			selenium.type(goToPageTextFieldLocator, s);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not locate the Go To Page text field.");
		}
	}

	/**
	 * Gets the number of pages of the current paged screen
	 * 
	 * @return
	 */
	public int getNumberOfPages() {
		int result = -1;
		if(selenium.isElementPresent(labelForLastPageLocator)) {
			String s = selenium.getText(labelForLastPageLocator);
			String numStr = s.replaceAll("\\D", "");
			result = Integer.parseInt(numStr);
		}
		return result;
	}

	/**
	 * The selenium getValue() method will return "on" or "off" rather than
	 * a boolean value. This is just a wrapper to convert the "on" and "off"
	 * to true and false, respectively.
	 * 
	 * @param locator
	 * @return
	 */
	public boolean getCheckBoxValue(String locator) {
		boolean result = false;
		if(selenium.isElementPresent(locator)) {
			result = selenium.getValue(locator).equals("on") ? true : false;
		} else {
			fail("Could not locate the checkbox");
		}
		return result;
	}
	
	/**
	 * Sets the checkbox to the given boolean value. If b is true then check
	 * the checkbox. Otherwise, uncheck the checkbox.
	 * 
	 * @param locator
	 * @param b
	 */
	public void setCheckBoxValue(String locator, boolean b) {
		if(selenium.isElementPresent(locator)) {
			if(b){
				selenium.check(locator);
			} else {
				selenium.uncheck(locator);
			}
		} else {
			fail("Could not locate the checkbox");
		}
	}

	/**
	 * Checks the options of a given select list and returns true if the
	 * option exists in the select list. Otherwise it returns false.
	 * 
	 * @param locator
	 * @param option
	 * @return true if option exists, otherwise false.
	 */
	public boolean isOptionPresent(String locator, String option) {
		boolean result = false;
		if(selenium.isElementPresent(locator)) {
			String options[] = selenium.getSelectOptions(locator);
			for(int i = 0; i < options.length; i++) {
				if(options[i].equals(option)) {
					result = true;
					break;
				}
			}
		} else {
			fail("Could not find the select list");
		}
		return result;
	}

	/**
	 * Sets the organization, customer and division of a Choose owner dialog.
	 * 
	 * This code will take as input an Owner object. The customer and division
	 * fields will be set to the customer and division. This method will use
	 * the fields to build the appropriate option strings. For example, if I
	 * was to give the Owner:
	 * 
	 * 	new Owner("NIS Chain", "1-Example Company", "Customer A");
	 * 
	 * this code will search for the options:
	 * 
	 * Organization:	NIS Chain
	 * Customer:		1-Example Company (NIS Chain)
	 * Division:		Customer A, 1-Example Company (NIS Chain)
	 * 
	 * It also assumes if the division is set that the customer and
	 * organization are set as well and that if the customer is set the
	 * organization is set as well.
	 * 
	 * @param owner
	 */
	public void setOwner(Owner owner) {
		if (!owner.specifiesOrg()) {
			selenium.select(selectOwnerOrganizationSelectListLocator, "index=0");
			waitForLoadingToFinish(MiscDriver.DEFAULT_TIMEOUT);
		}
		
		String organization = owner.getOrganization();
		if(organization != null) {
			if(isOptionPresent(selectOwnerOrganizationSelectListLocator, organization)) {
				selenium.select(selectOwnerOrganizationSelectListLocator, organization);
				waitForLoadingToFinish(MiscDriver.DEFAULT_TIMEOUT);
			} else {
				fail("Could not find the organization '" + organization + "' in choose owner dialog");
			}
		}

		String customer = "";
		if(owner.getCustomer() != null) {
			customer = owner.getCustomer() + " (" + owner.getOrganization() + ")";
			if(isOptionPresent(selectOwnerCustomerSelectListLocator, customer)) {
				selenium.select(selectOwnerCustomerSelectListLocator, customer);
				waitForLoadingToFinish(MiscDriver.DEFAULT_TIMEOUT);
			} else {
				fail("Could not find the customer '" + customer + "' in choose owner dialog");
			}
		}

		String division = "";
		if(owner.getDivision() != null) {
			division = owner.getDivision() + ", " + customer;
			if(isOptionPresent(selectOwnerDivisionSelectListLocator, division)) {
				selenium.select(selectOwnerDivisionSelectListLocator, division);
				waitForLoadingToFinish(MiscDriver.DEFAULT_TIMEOUT);
			} else {
				fail("Could not find the division '" + division + "' in choose owner dialog");
			}
		}
	}
	
	/**
	 * When you open the Choose owner dialog or change any of the drop downs
	 * it will change the subsequent drop downs as well. For example, if I
	 * change the customer, it will dynamically change the list of divisions.
	 * While it is changing the drop downs, a 'loading' image is visible.
	 * This method will wait for that image to become non-visible.
	 * 
	 * If the time runs out before the dialog finishes loading, this will fail
	 * the current test.
	 * 
	 * @param maxMilliseconds
	 */
	private void waitForLoadingToFinish(String maxMilliseconds) {
		int maxSeconds = Integer.parseInt(maxMilliseconds) / 1000;
		assertTrue("Must give a positive timeout for waitForLoadingToFinish", maxSeconds > 0);
		boolean loading  = true;
		int secondsLeft = maxSeconds;
		do {
			try { Thread.sleep(1000); } catch(Exception e) { }
			loading = selenium.isElementPresent(orgBrowserLoadingImageLocator);
			secondsLeft--;
		} while(loading && secondsLeft > 0);
		
		if(secondsLeft == 0) {
			fail("Timeout before the Owner dialog finished loading");
		}
	}

	/**
	 * Click the 'Choose' link and open the choose owner dialog.
	 */
	public void gotoChooseOwner() {
		if(selenium.isElementPresent(chooseOwnerLinkLocator)) {
			selenium.click(chooseOwnerLinkLocator);
			waitForLoadingToFinish(MiscDriver.DEFAULT_TIMEOUT);
		} else {
			fail("Could not find a link to Choose owner");
		}
	}
	
	/**
	 * After you have opened the Choose owner dialog and set the drop downs,
	 * calling this method will click the Select button.
	 */
	public void gotoSelectOwner() {
		if(selenium.isElementPresent(chooseOwnerSelectButtonLocator)) {
			selenium.click(chooseOwnerSelectButtonLocator);
		} else {
			fail("Could not find the Select button to choose the owner");
		}
	}
	
	/**
	 * After you have opend the Choose owner dialog, calling this method will
	 * click the Cancel button.
	 */
	public void gotoCancelOwner() {
		if(selenium.isElementPresent(chooseOwnerCancelButtonLocator)) {
			selenium.click(chooseOwnerCancelButtonLocator);
		} else {
			fail("Could not find the Cancel button to choose the owner");
		}
	}

	/**
	 * This method will look for the following HTML snippet:
	 * 
	 * 	<*>
	 * 		<LABEL>label</LABEL>
	 * 		<INPUT type="checkbox"/>
	 * 	</*>
	 * 
	 * where <*> can be any tag, e.g. <DIV> or <SPAN>. It will find the label
	 * then use xpath to go up one level then back down to the checkbox. If the
	 * code has more then one checkbox at the same level as the label, this
	 * code will select the first checkbox it finds in the DOM.
	 * 
	 * @param label
	 * @param b
	 */
	public void setCheckBoxByAssociatedLabel(String label, boolean b) {
		String labelLocator = "xpath=//LABEL[text()='" + label + "']";
		if(selenium.isElementPresent(labelLocator)) {
			String checkboxLocator = labelLocator + "/../INPUT[@type='checkbox']";
			if(!selenium.isElementPresent(checkboxLocator)) {
				String errorMsg = "Could not find an associated checkbox with the label '" + label + "'.\n";
				errorMsg += "Or there are multiple labels with the same text.";
				fail(errorMsg);
			}
			if(b) {
				selenium.check(checkboxLocator);
			} else {
				selenium.uncheck(checkboxLocator);
			}
		} else {
			fail("Could not find a checkbox with the associated label '" + label + "'");
		}
	}

	public List<String> convertStringArrayToList(String[] array) {
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < array.length; i++) {
			result.add(array[i]);
		}
		return result;
	}

	/**
	 * When you change the country for a form, the timezones will update.
	 * This method will wait for the timezones to finish updating before
	 * returning.
	 * 
	 */
	public void waitForTimeZoneToUpdate() {
		selenium.waitForAjax();
	}

	/**
	 * A wrapper to Thread.sleep(millis) so we don't have to wrap it in a try/catch block.
	 * 
	 * @param millis
	 */
	public void sleep(long millis) {
		try { Thread.sleep(millis); } catch (InterruptedException e) { }
	}

	

	/**
	 * Gets the Owner information from the Choose Owner dialog. It assumes
	 * you have used the gotoChooseOwner() to open the dialog. It then gets
	 * the organization, customer and division information and returns it
	 * as an Owner object.
	 * 
	 * @return
	 */
	public Owner getOwner() {
		
		String organization = selenium.getSelectedLabel(selectOwnerOrganizationSelectListLocator);
		String customer = null;
		String division = null;
		
		String[] s = selenium.getSelectOptions(selectOwnerCustomerSelectListLocator);
		;
		if(s.length > 0 && !s.equals("")) {
			String customerOrganization = selenium.getSelectedLabel(selectOwnerCustomerSelectListLocator);
			customer = customerOrganization.replace(" (" + organization + ")", "").trim();
		}
		s = selenium.getSelectOptions(selectOwnerDivisionSelectListLocator);
		if(s.length > 0 && !s[0].equals("")) {
			String divisionCustomerOrganization = selenium.getSelectedLabel(selectOwnerDivisionSelectListLocator);
			division = divisionCustomerOrganization.replace(", " + customer, "").replace(" (" + organization + ")", "").trim();
		}
		return new Owner(organization.trim(), customer, division);
	}

	/**
	 * Enters text into the Find text field but does not click the Load button.
	 * 
	 * @param s
	 */
	public void setSmartSearch(String s) {
		if(selenium.isElementPresent(smartSearchTextFieldLocator)) {
			selenium.type(smartSearchTextFieldLocator, s);
		} else {
			fail("Could not find the Smart Search (Find:) text field");
		}
	}

	/**
	 * Click the Load button on the Find text field.
	 */
	public void submitSmartSearch() {
		if(selenium.isElementPresent(smartSearchLoadButtonLocator)) {
			selenium.click(smartSearchLoadButtonLocator);
			waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Load button for the Smart Search (Find:) text field");
		}
	}

	/**
	 * This is an alias for gotoAdministation().
	 * 
	 */
	public void gotoSetup() {
		this.gotoAdministration();
	}


	public boolean isSessionExpired() {
		boolean result = false;
		String sessionExpiredLightboxLocator = "xpath=//DIV[@class='lv_Title' and contains(text(),'Session Expired')]";
		result = selenium.isElementPresent(sessionExpiredLightboxLocator) && selenium.isVisible(sessionExpiredLightboxLocator);
		return result;
	}

	public int getRandomNumber(int low, int high) {
		int result = r.nextInt((high-low+1)) + low;
		return result;
	}


	public String getContentTitle() {
		return selenium.getText("css=#contentTitle");
	}
}
