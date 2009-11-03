package com.n4systems.fieldid;

import static watij.finders.FinderFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.Notification;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class MyAccount extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	FieldIDMisc misc = null;
	Reporting reporting = null;
	String propertyFile = "myaccount.properties";
	Finder myAccountFinder;
	Finder myAccountContentHeaderFinder;
	Finder myAccountChangeYourPasswordFinder;
	Finder changePasswordContentHeaderFinder;
	Finder currentPasswordFinder;
	Finder confirmPasswordFinder;
	Finder newPasswordFinder;
	Finder changePasswordSaveButtonFinder;
	Finder changePasswordCancelButtonFinder;
	Finder myAccountChangeYourSecurityCardFinder;
	Finder changeSecurityCardContentHeaderFinder;
	Finder securityRfidNumberFinder;
	Finder changeSecurityCardSaveButtonFinder;
	Finder myAccountUserNameFinder;
	Finder myAccountEmailAddressFinder;
	Finder myAccountFirstNameFinder;
	Finder myAccountLastNameFinder;
	Finder myAccountPositionFinder;
	Finder myAccountInitialsFinder;
	Finder myAccountOrgUnitFinder;
	Finder myAccountAddNotificationFinder;
	Finder addNotificationContentHeaderFinder;
	Finder noNotificationsConfiguredFinder;
	Finder notificationNamesFinder;
	Finder addNotificationNameFinder;
	Finder addNotificationCustomerFinder;
	Finder addNotificationDivisionFinder;
	Finder addNotificationAssetTypesFinder;
	Finder addNotificationEventTypesFinder;
	Finder addNotificationFrequencyFinder;
	Finder addNotificationForEventsStartingFinder;
	Finder addNotificationForTheNextFinder;
	Finder addNotificationSaveButtonFinder;
	Finder addNotificationAddEmailButtonFinder;
	Finder addNotificationCancelButtonFinder;
	Finder addNotificationJobSiteFinder;
	Finder notificationTableRowsFinder;
	Finder myAccountNoNotificationsSetAddNotificationFinder;
	Finder myAccountNoSavedReportsGoToReportingFinder;
	Finder myAccountSavedReportsFinder;
	Finder myAccountShareSavedReportSaveButton;
	Finder myAccountShareSavedReportCancelButton;
	Finder savedReportsFinder;
	Finder notificationSettingsFinder;
	Finder addNotificationFinder;
	Finder changePasswordFinder;
	Finder securityCardFinder;
	Finder shareUsersCheckboxIDFinder;
	Finder changeSecurityCardCancelButtonFinder;
	
	public MyAccount(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			reporting = new Reporting(ie);
			myAccountFinder = text(p.getProperty("link", "NOT SET"));
			myAccountContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			myAccountChangeYourPasswordFinder = text(p.getProperty("changeyourpassword", "NOT SET"));
			changePasswordContentHeaderFinder = xpath(p.getProperty("changepasswordcontentheader", "NOT SET"));
			currentPasswordFinder = id(p.getProperty("changepasswordcurrentpassword", "NOT SET"));
			confirmPasswordFinder = id(p.getProperty("changepasswordconfirmpassword", "NOT SET"));
			newPasswordFinder = id(p.getProperty("changepasswordnewpassword", "NOT SET"));
			changePasswordSaveButtonFinder = id(p.getProperty("changepasswordsavebutton", "NOT SET"));
			changePasswordCancelButtonFinder = value(p.getProperty("changepasswordcancelbutton", "NOT SET"));
			myAccountChangeYourSecurityCardFinder = text(p.getProperty("changeyoursecuritycard", "NOT SET"));
			changeSecurityCardContentHeaderFinder = xpath(p.getProperty("changesecuritycardcontentheader", "NOT SET"));
			changeSecurityCardSaveButtonFinder = id(p.getProperty("changesecuritycardsavebutton", "NOT SET"));
			securityRfidNumberFinder = id(p.getProperty("changesecurityrfidnumber", "NOT SET"));
			myAccountUserNameFinder = xpath(p.getProperty("myaccountusername", "NOT SET"));
			myAccountEmailAddressFinder = xpath(p.getProperty("myaccountemailaddress", "NOT SET"));
			myAccountFirstNameFinder = xpath(p.getProperty("myaccountfirstname", "NOT SET"));
			myAccountLastNameFinder = xpath(p.getProperty("myaccountlastname", "NOT SET"));
			myAccountPositionFinder = xpath(p.getProperty("myaccountposition", "NOT SET"));
			myAccountInitialsFinder = xpath(p.getProperty("myaccountinitials", "NOT SET"));
			myAccountOrgUnitFinder = xpath(p.getProperty("myaccountorgunit", "NOT SET"));
			myAccountAddNotificationFinder = xpath(p.getProperty("myaccountaddnotification", "NOT SET"));
			addNotificationContentHeaderFinder = xpath(p.getProperty("addnotificationcontentheader", "NOT SET"));
			noNotificationsConfiguredFinder = xpath(p.getProperty("nonotificationsmessage", "NOT SET"));
			notificationNamesFinder = xpath(p.getProperty("notificationnames", "NOT SET"));
			addNotificationNameFinder = id(p.getProperty("addnotificationname", "NOT SET"));
			addNotificationCustomerFinder = id(p.getProperty("addnotificationcustomer", "NOT SET"));
			addNotificationDivisionFinder = id(p.getProperty("addnotificationdivision", "NOT SET"));
			addNotificationAssetTypesFinder = id(p.getProperty("addnotificationassettype", "NOT SET"));
			addNotificationEventTypesFinder = id(p.getProperty("addnotificationeventtype", "NOT SET"));
			addNotificationFrequencyFinder = id(p.getProperty("addnotificationfrequency", "NOT SET"));
			addNotificationForEventsStartingFinder = id(p.getProperty("addnotificationforeventsstarting", "NOT SET"));
			addNotificationForTheNextFinder = id(p.getProperty("addnotificationforthenext", "NOT SET"));
			addNotificationSaveButtonFinder = id(p.getProperty("addnotificationsavebutton", "NOT SET"));
			addNotificationAddEmailButtonFinder = xpath(p.getProperty("addnotificationaddemailbutton", "NOT SET"));
			addNotificationCancelButtonFinder = xpath(p.getProperty("addnotificationcancelbutton", "NOT SET"));
			addNotificationJobSiteFinder = id(p.getProperty("addnotificationjobsite", "NOT SET"));
			notificationTableRowsFinder = xpath(p.getProperty("notificationrows", "NOT SET"));
			myAccountNoNotificationsSetAddNotificationFinder = xpath(p.getProperty("myaccountaddnotification2", "NOT SET"));
			myAccountNoSavedReportsGoToReportingFinder = text(p.getProperty("myaccountnosavedreports", "NOT SET"));
			myAccountSavedReportsFinder = xpath(p.getProperty("myaccountsavedreportcells", "NOT SET"));
			myAccountShareSavedReportSaveButton = id(p.getProperty("myaccountsharesavedreportsavebutton", "NOT SET"));
			myAccountShareSavedReportCancelButton = value(p.getProperty("myaccountsharesavedreportcancelbutton", "NOT SET"));
			savedReportsFinder = xpath(p.getProperty("savedreportlink", "NOT SET"));
			notificationSettingsFinder = xpath(p.getProperty("notificationsettingslink", "NOT SET"));
			addNotificationFinder = xpath(p.getProperty("addnotificationlink", "NOT SET"));
			changePasswordFinder = xpath(p.getProperty("changepassword", "NOT SET"));
			securityCardFinder = xpath(p.getProperty("securitycard", "NOT SET"));
			shareUsersCheckboxIDFinder = xpath(p.getProperty("sharedreportusers", "NOT SET"));
			changeSecurityCardCancelButtonFinder = xpath(p.getProperty("changesecuritycardcancelbutton", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	/**
	 * Goes to the My Account page
	 * 
	 * @throws Exception
	 */
	public void gotoMyAccount() throws Exception {
		Link myAccount = ie.link(myAccountFinder);
		assertTrue("Could not find the link to My Account", myAccount.exists());
		myAccount.click();
		checkMyAccountContentHeader();
	}
	
	/**
	 * Called by other methods to assure we are on the correct page.
	 * 
	 * @throws Exception
	 */
	public void checkMyAccountContentHeader() throws Exception {
		HtmlElement myAccountContentHeader = ie.htmlElement(myAccountContentHeaderFinder);
		assertTrue("Could not find My Account page content header '" + p.getProperty("contentheader", "NOT SET") + "'", myAccountContentHeader.exists());
	}
	
	/**
	 * Goes to the change password page using the link at the
	 * bottom of every page.
	 * 
	 * @throws Exception
	 */
	public void gotoChangeYourPassword() throws Exception {
		Link changeYourPassword = ie.link(myAccountChangeYourPasswordFinder);
		assertTrue("Could not find a link to 'Change your password'", changeYourPassword.exists());
		changeYourPassword.click();
	}
	
	/**
	 * @deprecated
	 * @throws Exception
	 */
	public void checkChangePasswordContentHeader() throws Exception {
		HtmlElement changePasswordContentHeader = ie.htmlElement(changePasswordContentHeaderFinder);
		assertTrue("Could not find Change Password page content header '" + p.getProperty("changepasswordcontentheader", "NOT SET") + "'", changePasswordContentHeader.exists());
	}

	/**
	 * Sets the Current Password field on the change password page.
	 * 
	 * @param password
	 * @throws Exception
	 */
	public void setCurrentPassword(String password) throws Exception {
		assertNotNull(password);
		TextField currentPassword = checkCurrentPasswordField();
		currentPassword.set(password);
	}
	
	private TextField checkCurrentPasswordField() throws Exception {
		TextField currentPassword = ie.textField(currentPasswordFinder);
		assertTrue("Could not find the text field for current password", currentPassword.exists());
		return currentPassword;
	}

	/**
	 * Sets the Confirm Password field on the change password page.
	 * 
	 * @param password
	 * @throws Exception
	 */
	public void setConfirmPassword(String password) throws Exception {
		assertNotNull(password);
		TextField confirmPassword = checkConfirmPassword();
		confirmPassword.set(password);
	}

	private TextField checkConfirmPassword() throws Exception {
		TextField confirmPassword = ie.textField(confirmPasswordFinder);
		assertTrue("Could not find the text field for Confirm password", confirmPassword.exists());
		return confirmPassword;
	}

	/**
	 * Sets the New Password field on the change password page.
	 * 
	 * @param password
	 * @throws Exception
	 */
	public void setNewPassword(String password) throws Exception {
		assertNotNull(password);
		TextField newPassword = checkNewPassword();
		newPassword.set(password);
	}

	private TextField checkNewPassword() throws Exception {
		TextField newPassword = ie.textField(newPasswordFinder);
		assertTrue("Could not find the text field for new password", newPassword.exists());
		return newPassword;
	}

	/**
	 * Saves the changes to the password. Will throw an exception if
	 * there are any errors during the save.
	 * 
	 * @throws Exception
	 */
	public void saveChangePassword() throws Exception {
		Button save = ie.button(changePasswordSaveButtonFinder);
		assertTrue("Could not find the save button for change password", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkMyAccountContentHeader();
	}

	/**
	 * Selects Cancel from the Change Password page.
	 * 
	 * @throws Exception
	 */
	public void cancelChangePassword() throws Exception {
		Button cancel = ie.button(changePasswordCancelButtonFinder);
		assertTrue("Could not find the cancel button for change password", cancel.exists());
		cancel.click();
		checkMyAccountContentHeader();
	}

	/**
	 * Goes to the page to change security card (RFID) number.
	 * This link no longer exists since Field ID 2009.5
	 * 
	 * @throws Exception
	 * @deprecated
	 */
	public void gotoChangeSecurityCardNumber() throws Exception {
		Link changeSecurityCard = ie.link(myAccountChangeYourSecurityCardFinder);
		assertTrue("Could not find a link to 'Change your security card number'", changeSecurityCard.exists());
		changeSecurityCard.click();
		checkChangeSecurityCardContentHeader();
	}

	/**
	 * @deprecated
	 * @throws Exception
	 */
	private void checkChangeSecurityCardContentHeader() throws Exception {
		HtmlElement changeSecurityCardContentHeader = ie.htmlElement(changeSecurityCardContentHeaderFinder);
		assertTrue("Could not find Change Security Card page content header '" + p.getProperty("contentheader", "NOT SET") + "'", changeSecurityCardContentHeader.exists());
	}

	/**
	 * Sets the field for security card on the Change security card page.
	 * 
	 * @param rfidNumber
	 * @throws Exception
	 */
	public void setSecurityRfidNumber(String rfidNumber) throws Exception {
		assertNotNull(rfidNumber);
		TextField card = checkSecurityRfidNumberField();
		card.set(rfidNumber);
	}

	private TextField checkSecurityRfidNumberField() throws Exception {
		TextField card = ie.textField(securityRfidNumberFinder);
		assertTrue("Could not find the text field for current password", card.exists());
		return card;
	}

	/**
	 * Saves the changes on the change security card page.
	 * 
	 * @throws Exception
	 */
	public void saveChangeSecurityRfidNumber() throws Exception {
		Button save = ie.button(changeSecurityCardSaveButtonFinder);
		assertTrue("Could not find the save button for change security card", save.exists());
		save.click();
		checkMyAccountContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	/**
	 * Gets the User Name value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUserName() throws Exception {
		String result = null;
		Span s = ie.span(myAccountUserNameFinder);
		assertTrue("Could not find the user name", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Gets the Email Address value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getEmailAddress() throws Exception {
		String result = null;
		Span s = ie.span(myAccountEmailAddressFinder);
		assertTrue("Could not find the email address", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Gets the First Name value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFirstName() throws Exception {
		String result = null;
		Span s = ie.span(myAccountFirstNameFinder);
		assertTrue("Could not find the first name", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Gets the Last Name value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLastName() throws Exception {
		String result = null;
		Span s = ie.span(myAccountLastNameFinder);
		assertTrue("Could not find the last name", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Gets the Position value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPosition() throws Exception {
		String result = null;
		Span s = ie.span(myAccountPositionFinder);
		assertTrue("Could not find the position", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Gets the Initials value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getInitials() throws Exception {
		String result = null;
		Span s = ie.span(myAccountInitialsFinder);
		assertTrue("Could not find the initials", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Gets the Organizational Unit value from the Account Details table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrganizationalUnit() throws Exception {
		String result = null;
		Span s = ie.span(myAccountOrgUnitFinder);
		assertTrue("Could not find the organizational unit", s.exists());
		result = s.text();
		return result;
	}

	/**
	 * Goes to the Add Notification page using the Add link which 
	 * is always available on the My Account page. Located after
	 * the "Notifications" text.
	 * 
	 * @throws Exception
	 */
	public void gotoAddNotification() throws Exception {
		Link addNotification = ie.link(myAccountAddNotificationFinder);
		assertTrue("Could not find a link to add notifications", addNotification.exists());
		addNotification.click();
		checkMyAccountContentHeader();
	}

	/**
	 * @deprecated
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void checkAddUpcomingInspectionsNotificationContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(addNotificationContentHeaderFinder);
		assertTrue("Could not find Add Upcoming Inspections Notification page content header '" + p.getProperty("addnotificationcontentheader", "NOT SET") + "'", contentHeader.exists());
	}

	/**
	 * Get a list of the notification names.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getNotificationNames() throws Exception {
		List<String> results = new ArrayList<String>();
		HtmlElement noNotifications = ie.htmlElement(noNotificationsConfiguredFinder);
		if(noNotifications.exists()) {
			return null;	// no notifications configured
		}
		
		TableCells names = ie.cells(notificationNamesFinder);
		assertNotNull(names);
		Iterator<TableCell> i = names.iterator();
		while(i.hasNext()) {
			TableCell name = i.next();
			assertTrue("Could not find a name on one of the rows", name.exists());
			results.add(name.text().trim());
		}
		
		return results;
	}
	
	/**
	 * Add a notification.
	 * 
	 * @param n
	 * @throws Exception
	 */
	public void addNotification(Notification n) throws Exception {
		assertNotNull(n);
		FieldIDMisc.stopMonitor();
		TextField name = ie.textField(addNotificationNameFinder);
		assertTrue("Could not find the Name field for adding a notification", name.exists());
		if(n.getName() != null) {
			name.set(n.getName());
		}
		
		if(n.getJobSite() != null) {
			SelectList jobSite = ie.selectList(addNotificationJobSiteFinder);
			assertTrue("Could not find the Job site field for adding a notification", jobSite.exists());
			Option o = jobSite.option(text(n.getJobSite()));
			assertTrue("Could not find the job site '" + n.getJobSite() + "'", o.exists());
			o.select();
		}
		
		if(n.getCustomer() != null) {
			SelectList customer = ie.selectList(addNotificationCustomerFinder);
			assertTrue("Could not find the Customer field for adding a notification", customer.exists());
			Option o = customer.option(text(n.getCustomer()));
			assertTrue("Could not find the customer '" + n.getCustomer() + "'", o.exists());
			o.select();
			misc.waitForJavascript();
		}

		if(n.getDivision() != null) {
			SelectList division = ie.selectList(addNotificationDivisionFinder);
			assertTrue("Could not find the Division field for adding a notification", division.exists());
			Option o = division.option(text(n.getDivision()));
			assertTrue("Could not find the division '" + n.getDivision() + "'", o.exists());
			o.select();
		}
		
		SelectList assetTypes = ie.selectList(addNotificationAssetTypesFinder);
		assertTrue("Could not find the Asset Types field for adding a notification", assetTypes.exists());
		if(n.getAssetTypes() != null) {
			Option o = assetTypes.option(text(n.getAssetTypes()));
			assertTrue("Could not find the asset type '" + n.getAssetTypes() + "'", o.exists());
			o.select();
		}
		
		SelectList eventTypes = ie.selectList(addNotificationEventTypesFinder);
		assertTrue("Could not find the Event Types field for adding a notification", eventTypes.exists());
		if(n.getEventTypes() != null) {
			Option o = eventTypes.option(text(n.getEventTypes()));
			assertTrue("Could not find the event type '" + n.getEventTypes() + "'", o.exists());
			o.select();
		}
		
		SelectList frequency = ie.selectList(addNotificationFrequencyFinder);
		assertTrue("Could not find the Frequency field for adding a notification", frequency.exists());
		if(n.getFrequency() != null) {
			Option o = frequency.option(text(n.getFrequency()));
			assertTrue("Could not find the frequency '" + n.getFrequency() + "'", o.exists());
			o.select();
		}
		
		SelectList forEventsStarting = ie.selectList(addNotificationForEventsStartingFinder);
		assertTrue("Could not find the For Events Starting field for adding a notification", forEventsStarting.exists());
		if(n.getForEventsStarting() != null) {
			Option o = forEventsStarting.option(text(n.getForEventsStarting()));
			assertTrue("Could not find the For Events Starting '" + n.getForEventsStarting() + "'", o.exists());
			o.select();
		}
		
		SelectList forTheNext = ie.selectList(addNotificationForTheNextFinder);
		assertTrue("Could not find the For The Next field for adding a notification", forTheNext.exists());
		if(n.getForTheNext() != null) {
			Option o = forTheNext.option(text(n.getForTheNext()));
			assertTrue("Could not find the For The Next '" + n.getForTheNext() + "'", o.exists());
			o.select();
		}

		List<String> emails = n.getEmailAddresses();
		if(emails != null && emails.size() > 0) {
			for(int i = 0; i < emails.size(); i++) {
				String s = "addressInput_" + i;
				TextField address = ie.textField(id(s));
				if(!address.exists()) {
					Button add = ie.button(addNotificationAddEmailButtonFinder);
					assertTrue("Could not find the Add button for email addresses", add.exists());
					add.click();
					misc.waitForJavascript();
					address = ie.textField(id(s));
				}
				assertTrue("Could not find the text field to enter email address '" + i + "'", address.exists());
				address.set(emails.get(i));
			}
		}
		
		Button save = ie.button(addNotificationSaveButtonFinder);
		assertTrue("Could not find the save button", save.exists());
		save.click();
		FieldIDMisc.startMonitor();
	}
	
	/**
	 * Click the Cancel button on the add notification page.
	 * 
	 * @throws Exception
	 */
	public void gotoCancelAddNotification() throws Exception {
		Button cancel = ie.button(addNotificationCancelButtonFinder);
		assertTrue("Could not find the cancel button on add notification", cancel.exists());
		cancel.click();
		checkMyAccountContentHeader();
		checkMyAccountNotificationSettingsContent();
	}

	/**
	 * Deletes a notification using the table on My Account.
	 * You need to provide the follow fields in the Notification
	 * object:
	 * 
	 * 	name
	 * 	frequency (sent)
	 * 	for events starting
	 * 	for the next
	 * 
	 * This code will look for a row which has all four items.
	 * If any of the items were not set on the notification
	 * they need to be set to "" on the Notification object
	 * passed in.
	 * 
	 * @param n
	 * @throws Exception
	 */
	public void deleteNotification(Notification n) throws Exception {
		assertNotNull(n);
		assertNotNull(n.getName());
		assertNotNull(n.getFrequency());
		assertNotNull(n.getForEventsStarting());
		assertNotNull(n.getForTheNext());
		TableRows trs = ie.rows(notificationTableRowsFinder);
		assertNotNull("Could not find any rows on the Notification table", trs);
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			String name = tr.cell(0).text().trim();
			String sent = tr.cell(1).text().trim();
			String forEventsStarting = tr.cell(2).text().trim();
			String forTheNext = tr.cell(3).text().trim();
			if(name.equals(n.getName()) && 
					sent.equals(n.getFrequency()) && 
					forEventsStarting.equals(n.getForEventsStarting()) &&
					forTheNext.equals(n.getForTheNext())) {
				// found the row
				Link delete = tr.link(text("Delete"));
				assertTrue("Could not find the Delete link on the row", delete.exists());
				delete.click();
				break;
			}
		}
		misc.checkForErrorMessagesOnCurrentPage();
	}

	/**
	 * If there are no notifications set there is a link to add notifications
	 * in the space you'd normally see the list of notifications. This will
	 * use that link to add the first notification. If there are notifications
	 * this method will throw an exception.
	 * 
	 * @throws Exception
	 */
	public void gotoAddNotification2() throws Exception {
		Link addNotification = ie.link(myAccountNoNotificationsSetAddNotificationFinder);
		assertTrue("Could not find a link to add notifications when no notifications are set", addNotification.exists());
		addNotification.click();
		checkMyAccountContentHeader();
	}

	/**
	 * If there are not saved reports there is a link to Reporting
	 * in the space saved reports would normally be listed. This
	 * method will use that link to go to the Reportingpage. If
	 * there are saved reports, this method will throw an exception.
	 * 
	 * @throws Exception
	 */
	public void gotoReportingNoSavedReports() throws Exception {
		Link gotoReporting = ie.link(myAccountNoSavedReportsGoToReportingFinder);
		assertTrue("Could not find a link to go to reporting from My Accounts", gotoReporting.exists());
		gotoReporting.click();
		reporting.checkReportingPageContentHeader();
	}

	/**
	 * Gets the name of all the save reports from the My Account page.
	 * If there are no saved reports this will return an empty list.
	 * In other words, check the size of the list. If the size is zero
	 * there are no saved reports.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getSavedReports() throws Exception {
		List<String> results = new ArrayList<String>();
		TableCells tds = ie.cells(myAccountSavedReportsFinder);
		assertNotNull("Could not find any saved report entries", tds);
		Iterator<TableCell> i = tds.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			String name = td.link(0).text().trim();
			results.add(name);
		}
		return results;
	}
	
	/**
	 * Goes to the shared report, selects Share, finds the user name,
	 * selects the user name, then saves the settings.
	 * 
	 * @param reportName
	 * @param userName
	 * @throws Exception
	 */
	public void shareSavedReport(String reportName, String userName) throws Exception {
		gotoShareSavedReport(reportName);
		setShareSavedReportUserName(userName);
		saveShareSavedReport();
	}
	
	/**
	 * Goes to the Share saved report.
	 * 
	 * @param reportName
	 * @throws Exception
	 */
	public void gotoShareSavedReport(String reportName) throws Exception {
		assertNotNull(reportName);
		TableRow tr = findSavedReportRow(reportName);
		assertTrue("Could not find the row containing the shared report '" + reportName + "'", tr.exists());
		Link share = tr.link(xpath("TD[4]/A[text()='Share']"));
		assertTrue("Could not find the Share link for the '" + reportName + "' report", share.exists());
		share.click();
		misc.waitForJavascript();
	}
	
	/**
	 * Sets the username given to share a saved report. Assumes
	 * you have already opened the sharing using gotoShareSavedReport.
	 * 
	 * @param userName
	 * @throws Exception
	 */
	public void setShareSavedReportUserName(String userName) throws Exception {
		assertNotNull(userName);
		Label l = ie.label(xpath("//LABEL[contains(text(),'" + userName + "')]"));
		assertTrue("Could not find the user '" + userName + "' in the list of users", l.exists());
		String id = l.htmlFor();
		Checkbox c = ie.checkbox(id(id));
		assertTrue("Could not find the checkbox for '" + userName + "'", c.exists());
		c.set(true);
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	/**
	 * Save the changes to a share saved report.
	 * 
	 * @throws Exception
	 */
	public void saveShareSavedReport() throws Exception {
		Button save = ie.button(myAccountShareSavedReportSaveButton);
		assertTrue("Could not find the save button for share reports.", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void deleteSavedReport(String reportName) throws Exception {
		TableRow tr = findSavedReportRow(reportName);
		Link delete = tr.link(xpath("TD[4]/A[contains(text(),'Delete')]"));
		assertTrue("Could not find the Delete link for the '" + reportName + "' report", delete.exists());
		delete.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	private TableRow findSavedReportRow(String reportName) throws Exception {
		TableRow tr = ie.row(xpath("//DIV[@id='pageContent']/TABLE[@class='list']/TBODY/TR/TD[1]/A[text()='" + reportName + "']/../.."));
		assertTrue("Could not find the row containing the shared report '" + reportName + "'", tr.exists());
		return tr;
	}

	private void gotoSecurityCard() throws Exception {
		Link l = ie.link(securityCardFinder);
		assertTrue("Could not find the link to Security Card", l.exists());
		l.click();
		checkMyAccountSecurityCardContent();
	}

	private void checkMyAccountSecurityCardContent() throws Exception {
		checkMyAccountContentHeader();
		Link l = ie.link(securityCardFinder);
		assertFalse("Found the link to Security Card but was not expecting to", l.exists());
		checkSecurityRfidNumberField();
	}

	private void gotoChangePassword() throws Exception {
		Link l = ie.link(changePasswordFinder);
		assertTrue("Could not find the link to Change Password", l.exists());
		l.click();
		checkMyAccountChangePasswordContent();
	}

	public void checkMyAccountChangePasswordContent() throws Exception {
		checkMyAccountContentHeader();
		Link l = ie.link(changePasswordFinder);
		assertFalse("Found the link to Change Password but was not expecting to", l.exists());
		checkCurrentPasswordField();
		checkNewPassword();
		checkConfirmPassword();
	}

	public void gotoNotificationSettings() throws Exception {
		Link l = ie.link(notificationSettingsFinder);
		assertTrue("Could not find the link to Notification Settings", l.exists());
		l.click();
		checkMyAccountNotificationSettingsContent();
	}

	private void checkMyAccountNotificationSettingsContent() throws Exception {
		checkMyAccountContentHeader();
		Link l = ie.link(notificationSettingsFinder);
		assertFalse("Found the link to Notification Settings but was not expecting to", l.exists());
		Link add = ie.link(addNotificationFinder);
		assertTrue("Could not find the link to add a notification", add.exists());
	}

	public void gotoSavedReports() throws Exception {
		Link l = ie.link(savedReportsFinder);
		assertTrue("Could not find the link to Saved Reports", l.exists());
		l.click();
		checkMyAccountSavedReportsContent();
	}

	private void checkMyAccountSavedReportsContent() throws Exception {
		checkMyAccountContentHeader();
		Link l = ie.link(savedReportsFinder);
		assertFalse("Found the link to Saved Reports but was not expecting to", l.exists());
	}

	public void validate() throws Exception {
		gotoMyAccount();

//		String s;
//		s = getUserName();
//		s = getEmailAddress();
//		s = getFirstName();
//		s = getLastName();
//		s = getPosition();
//		s = getInitials();
//		s = getOrganizationalUnit();
//
//		gotoSavedReports();
//		gotoReportingNoSavedReports();
//		Reporting r = new Reporting(ie);
//		r.gotoReportSearchResults();
//		r.gotoSaveReport();
//		String reportName = "validate";
//		String reportName2 = "validate2";
//		r.setSaveReportName(reportName);
//		r.saveSaveReport();
//		r.gotoSaveReportAs();
//		r.setSaveReportName(reportName2);
//		r.saveSaveReport();
//		
//		gotoMyAccount();
//		gotoSavedReports();
//		List<String> reports = getSavedReports();
//		deleteSavedReport(reports.get(0));
//		String sharedReport = reports.get(1);
//		gotoShareSavedReport(sharedReport);
//		List<String> users = getShareUserNames();
//		String userName = users.get(0);
//		setShareSavedReportUserName(userName);
//		saveShareSavedReport();
//		// check to see if the Shared By reflects the change
//		//List<String> sharedByUsers = getSharedByUserNames(sharedReport);
//		//sharedByUsers.contains(userName);
//		gotoNotificationSettings();
//		gotoAddNotification2();		// no notifications yet
//		Notification n = new Notification();
//		n.setName("validate");
//		n.setFrequency("Daily");
//		n.setForEventsStarting("Today");
//		n.setForTheNext("1 Day");
//		addNotification(n);
//		List<String> notifications = getNotificationNames();
//		assertTrue("getNotificationNames() did not return the notification we just added.", notifications.contains(n.getName()));
//		deleteNotification(n);
//		gotoAddNotification();
//		gotoCancelAddNotification();
//		gotoChangePassword();
//		String password = "makemore$";		// all passwords are hard coded to this values
//		this.setCurrentPassword(password);
//		this.setNewPassword(password);
//		this.setConfirmPassword(password);
//		this.saveChangePassword();
//		this.cancelChangePassword();
//		
		gotoSecurityCard();
		String rfidNumber = "DEADBEEFCAFEF00D";
		setSecurityRfidNumber(rfidNumber);
		cancelChangeSecurityRfidNumber();
		gotoSecurityCard();
		saveChangeSecurityRfidNumber();
		
		gotoChangeYourPassword();
	}

	private void cancelChangeSecurityRfidNumber() throws Exception {
		Button cancel = ie.button(changeSecurityCardCancelButtonFinder);
		assertTrue("Could not find the Cancel button for change security card", cancel.exists());
		cancel.click();
		checkMyAccountContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public List<String> getSharedByUserNames(String sharedReport) throws Exception {
		List<String> results = new ArrayList<String>();
		TableRow tr = findSavedReportRow(sharedReport);
		TableCell td = tr.cell(1);	// assumes Shared By is column 1, indexed 0
		// TODO: once bug is fixed, parse the cell and add names to results
		return results;
	}

	public List<String> getShareUserNames() throws Exception {
		List<String> users = new ArrayList<String>();
		Checkboxes shareUsers = ie.checkboxes(shareUsersCheckboxIDFinder);
		assertNotNull("Could not find any checkboxes for shared report users", shareUsers);
		Iterator<Checkbox> i = shareUsers.iterator();
		while(i.hasNext()) {
			Checkbox user = i.next();
			assertTrue("Could not find the checkbox for a shared report user", user.exists());
			String id = user.id();
			Label l = user.label(xpath("../LABEL[@for='" + id + "']"));
			assertTrue("Could not for the label for the shared report user with id='" + id + "'", l.exists());
			String name = l.text().trim();
			users.add(name);
		}
		return users;
	}

	public List<String> getShareUserIDs() throws Exception {
		List<String> users = new ArrayList<String>();
		Checkboxes shareUsers = ie.checkboxes(shareUsersCheckboxIDFinder);
		assertNotNull("Could not find any checkboxes for shared report users", shareUsers);
		Iterator<Checkbox> i = shareUsers.iterator();
		while(i.hasNext()) {
			Checkbox user = i.next();
			assertTrue("Could not find the checkbox for a shared user", user.exists());
			String id = user.id();
			users.add(id);
		}
		return users;
	}
}
