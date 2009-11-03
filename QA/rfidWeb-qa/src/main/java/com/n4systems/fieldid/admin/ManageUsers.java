package com.n4systems.fieldid.admin;

import static watij.finders.FinderFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.datatypes.CustomerUser;
import com.n4systems.fieldid.datatypes.EmployeeUser;
import com.n4systems.fieldid.datatypes.Organization;
import com.n4systems.fieldid.datatypes.Owner;
import com.n4systems.fieldid.datatypes.SystemUser;

import watij.BaseHtmlFinder;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageUsers extends TestCase {
	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "managesystemusers.properties";
	FieldIDMisc misc;
	ManageOrganizations mos;
	Finder listUsersFinder;
	Finder listUsersPageContentHeaderFinder;
	Finder listUsersFilterNameFinder;
	Finder listUsersFilterUserTypeFinder;
	Finder listUsersSearchButtonFinder;
	Finder listUsersClearButtonFinder;
	Finder listUsersAddEmployeeUserFinder;
	Finder listUsersAddCustomerUserFinder;
	Finder listAddUserPageContentHeaderFinder;
	Finder tagProductsRowFinder;
	Finder manageSystemConfigurationRowFinder;
	Finder manageSystemUsersRowFinder;
	Finder manageEndUsersRowFinder;
	Finder createInspectionRowFinder;
	Finder editInspectionRowFinder;
	Finder manageJobsRowFinder;
	Finder addCustomerUserUserIDFinder;
	Finder addCustomerUserEmailFinder;
	Finder addCustomerUserFirstNameFinder;
	Finder addCustomerUserLastNameFinder;
	Finder addCustomerUserPositionFinder;
	Finder addCustomerUserInitialsFinder;
	Finder addCustomerUserSecurityRFIDNumberFinder;
	Finder addCustomerUserTimeZoneFinder;
	Finder addCustomerUserCustomerFinder;
	Finder addCustomerUserDivisionFinder;
	Finder addCustomerUserPasswordFinder;
	Finder addCustomerUserVerifyPasswordFinder;
	Finder addCustomerUserAllOffButtonFinder;
	Finder addCustomerUserSubmitButtonFinder;
	private Finder manageUsersViewAllLinkFinder;
	private Finder manageUsersPageContentHeaderFinder;
	private Finder listEditUserPageContentHeaderFinder;
	private Finder editCustomerUserUserIDFinder;
	private Finder editCustomerUserEmailFinder;
	private Finder editCustomerUserFirstNameFinder;
	private Finder editCustomerUserLastNameFinder;
	private Finder editCustomerUserPositionFinder;
	private Finder editCustomerUserInitialsFinder;
	private Finder editCustomerUserTimeZoneFinder;
	private Finder editCustomerUserSubmitButtonFinder;
	private Finder addEmployeeUserUserIDFinder;
	private Finder addEmployeeUserEmailFinder;
	private Finder addEmployeeUserFirstNameFinder;
	private Finder addEmployeeUserLastNameFinder;
	private Finder addEmployeeUserPositionFinder;
	private Finder addEmployeeUserInitialsFinder;
	private Finder addEmployeeUserSecurityRFIDNumberFinder;
	private Finder addEmployeeUserTimeZoneFinder;
	private Finder addEmployeeUserOrgUnitFinder;
	private Finder addEmployeeUserPasswordFinder;
	private Finder addEmployeeUserVerifyPasswordFinder;
	private Finder addEmployeeUserAllOffButtonFinder;
	private Finder addEmployeeUserSubmitButtonFinder;
	private Finder addCustomerUserCountryFinder;
	private Finder manageSafetyNetworkRowFinder;
	private Finder addEmployeeUserCountryFinder;
	private Finder userIDLinkFinder;
	private Finder editEmployeeUserUserIDFinder;
	private Finder editEmployeeUserEmailFinder;
	private Finder editEmployeeUserFirstNameFinder;
	private Finder editEmployeeUserLastNameFinder;
	private Finder editEmployeeUserPositionFinder;
	private Finder editEmployeeUserInitialsFinder;
	private Finder editEmployeeUserTimeZoneFinder;
	private Finder editEmployeeUserSubmitButtonFinder;
	private Finder editEmployeeUserCountryFinder;

	public ManageUsers(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			mos = new ManageOrganizations(ie);
			editEmployeeUserUserIDFinder = xpath(p.getProperty("editemployeeuseruserid", "NOT SET"));
			editEmployeeUserEmailFinder = xpath(p.getProperty("editemployeeuseremail", "NOT SET"));
			editEmployeeUserFirstNameFinder = xpath(p.getProperty("editemployeeuserfirstname", "NOT SET"));
			editEmployeeUserLastNameFinder = xpath(p.getProperty("editemployeeuserlastname", "NOT SET"));
			editEmployeeUserPositionFinder = xpath(p.getProperty("editemployeeuserposition", "NOT SET"));
			editEmployeeUserInitialsFinder = xpath(p.getProperty("editemployeeuserinitials", "NOT SET"));
			editEmployeeUserCountryFinder = xpath(p.getProperty("editemployeeusercountry", "NOT SET"));
			editEmployeeUserTimeZoneFinder = xpath(p.getProperty("editemployeeusertimezone", "NOT SET"));
			editEmployeeUserSubmitButtonFinder = xpath(p.getProperty("editemployeeusersubmit", "NOT SET"));
			userIDLinkFinder = xpath(p.getProperty("useridlinks", "NOT SET"));
			addEmployeeUserCountryFinder = xpath(p.getProperty("addemployeecountry", "NOT SET"));
			manageSafetyNetworkRowFinder = xpath(p.getProperty("addusermanagesafetynetworkrow", "NOT SET"));
			listUsersFinder = xpath(p.getProperty("link", "NOT SET"));
			listUsersPageContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			listUsersFilterNameFinder = id(p.getProperty("listusersfiltername", "NOT SET"));
			listUsersFilterUserTypeFinder = id(p.getProperty("listusersfilterusertype", "NOT SET"));
			listUsersSearchButtonFinder = id(p.getProperty("listusersfiltersearchbutton", "NOT SET"));
			listUsersClearButtonFinder = id(p.getProperty("listusersfilterclearbutton", "NOT SET"));
			listUsersAddEmployeeUserFinder = xpath(p.getProperty("listusersaddemployee", "NOT SET"));
			listUsersAddCustomerUserFinder = xpath(p.getProperty("listusersaddcustomer", "NOT SET"));
			listAddUserPageContentHeaderFinder = xpath(p.getProperty("addusercontentheader", "NOT SET"));
			tagProductsRowFinder = xpath(p.getProperty("addusertagproductsrow", "NOT SET"));
			manageSystemConfigurationRowFinder = xpath(p.getProperty("addusermanagesystemconfigurationrow", "NOT SET"));
			manageSystemUsersRowFinder = xpath(p.getProperty("addusermanagesystemusersrow", "NOT SET"));
			manageEndUsersRowFinder = xpath(p.getProperty("addusermanageendusersrow", "NOT SET"));
			createInspectionRowFinder = xpath(p.getProperty("addusercreateinspectionrow", "NOT SET"));
			editInspectionRowFinder = xpath(p.getProperty("addusereditinspectionrow", "NOT SET"));
			manageJobsRowFinder = xpath(p.getProperty("addusermanagejobsrow", "NOT SET"));
			addCustomerUserUserIDFinder = xpath(p.getProperty("addcustomeruserid", "NOT SET"));
			addCustomerUserEmailFinder = xpath(p.getProperty("addcustomeremail", "NOT SET"));
			addCustomerUserFirstNameFinder = xpath(p.getProperty("addcustomerfirstname", "NOT SET"));
			addCustomerUserLastNameFinder = xpath(p.getProperty("addcustomerlastname", "NOT SET"));
			addCustomerUserPositionFinder = xpath(p.getProperty("addcustomerposition", "NOT SET"));
			addCustomerUserInitialsFinder = xpath(p.getProperty("addcustomerinitials", "NOT SET"));
			addCustomerUserSecurityRFIDNumberFinder = xpath(p.getProperty("addcustomersecurityrfidnumber", "NOT SET"));
			addCustomerUserCountryFinder = xpath(p.getProperty("addcustomercountry", "NOT SET"));
			addCustomerUserTimeZoneFinder = xpath(p.getProperty("addcustomertimezone", "NOT SET"));
			addCustomerUserPasswordFinder = xpath(p.getProperty("addcustomerpassword", "NOT SET"));
			addCustomerUserVerifyPasswordFinder = xpath(p.getProperty("addcustomerverifypassword", "NOT SET"));
			addCustomerUserSubmitButtonFinder = xpath(p.getProperty("addcustomersubmitbutton", "NOT SET"));
			manageUsersViewAllLinkFinder = xpath(p.getProperty("manageusersviewalllink", "NOT SET"));
			manageUsersPageContentHeaderFinder = xpath(p.getProperty("pagecontentheader", "NOT SET"));
			listEditUserPageContentHeaderFinder = xpath(p.getProperty("editusercontentheader", "NOT SET"));
			editCustomerUserUserIDFinder = xpath(p.getProperty("edituseruserid", "NOT SET"));
			editCustomerUserEmailFinder = xpath(p.getProperty("edituseremail", "NOT SET"));
			editCustomerUserFirstNameFinder = xpath(p.getProperty("edituserfirstname", "NOT SET"));
			editCustomerUserLastNameFinder = xpath(p.getProperty("edituserlastname", "NOT SET"));
			editCustomerUserPositionFinder = xpath(p.getProperty("edituserposition", "NOT SET"));
			editCustomerUserInitialsFinder = xpath(p.getProperty("edituserinitials", "NOT SET"));
			editCustomerUserTimeZoneFinder = xpath(p.getProperty("editusertimezone", "NOT SET"));
			editCustomerUserSubmitButtonFinder = xpath(p.getProperty("editusersubmitbutton", "NOT SET"));
			addEmployeeUserUserIDFinder = xpath(p.getProperty("addemployeeuserid", "NOT SET"));
			addEmployeeUserEmailFinder = xpath(p.getProperty("addemployeeemail", "NOT SET"));
			addEmployeeUserFirstNameFinder = xpath(p.getProperty("addemployeefirstname", "NOT SET"));
			addEmployeeUserLastNameFinder = xpath(p.getProperty("addemployeelastname", "NOT SET"));
			addEmployeeUserPositionFinder = xpath(p.getProperty("addemployeeposition", "NOT SET"));
			addEmployeeUserInitialsFinder = xpath(p.getProperty("addemployeeinitials", "NOT SET"));
			addEmployeeUserSecurityRFIDNumberFinder = xpath(p.getProperty("addemployeerfid", "NOT SET"));
			addEmployeeUserTimeZoneFinder = xpath(p.getProperty("addemployeetimezone", "NOT SET"));		
			addEmployeeUserOrgUnitFinder = xpath(p.getProperty("addemployeeorgunit", "NOT SET"));
			addEmployeeUserPasswordFinder = xpath(p.getProperty("addemployeepassword", "NOT SET"));
			addEmployeeUserVerifyPasswordFinder = xpath(p.getProperty("addemployeeverifypassword", "NOT SET"));
			addEmployeeUserAllOffButtonFinder = xpath(p.getProperty("addemployeealloffbutton", "NOT SET"));
			addEmployeeUserSubmitButtonFinder = xpath(p.getProperty("addemployeesubmitbutton", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}

	/**
	 * Goes to the Manage System Users page. Assumes you are
	 * already on the Administration page.
	 * 
	 * @throws Exception
	 */
	public void gotoManageUsers() throws Exception {
		Link manageUsers = ie.link(listUsersFinder);
		assertTrue("Could not find the link to Manage Users", manageUsers.exists());
		manageUsers.click();
		checkListUsersPageContentHeader();
	}

	private void checkListUsersPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(listUsersPageContentHeaderFinder);
		assertTrue("Could not find the content header on List Users page.", contentHeader.exists());
	}
	
	/**
	 * Sets the Name text field on the List Users page.
	 * 
	 * @param userNameOrID
	 * @throws Exception
	 */
	public void setListUsersNameFilter(String userNameOrID) throws Exception {
		assertNotNull(userNameOrID);
		TextField name = ie.textField(listUsersFilterNameFinder);
		assertTrue("Could not find the text field for filter name", name.exists());
		name.set(userNameOrID);
	}
	
	/**
	 * Sets the User Type select list on the List Users page.
	 * Currently, the valid inputs for this are "All", "Employee"
	 * or "Customers". All other values will throw an exception.
	 * 
	 * @param userType
	 * @throws Exception
	 */
	public void setListUsersUserType(String userType) throws Exception {
		assertNotNull(userType);
		SelectList ut = ie.selectList(listUsersFilterUserTypeFinder);
		assertTrue("Could not find the select list for filter user type", ut.exists());
		Option o = ut.option(text(userType));
		assertTrue("Could not find the option '" + userType + "'", o.exists());
		o.select();
	}
	
	/**
	 * Clicks the Search button on the List Users page. Assumes you
	 * are on the List Users page.
	 * 
	 * @throws Exception
	 */
	public void gotoListUsersSearch() throws Exception {
		Button b = ie.button(listUsersSearchButtonFinder);
		assertTrue("Could not find the Search button on List Users", b.exists());
		b.click();
		checkListUsersPageContentHeader();
	}

	/**
	 * Clicks the Clear button on the List Users page. Assumes you
	 * are on the List Users page.
	 * 
	 * @throws Exception
	 */
	public void gotoListUsersClear() throws Exception {
		Button b = ie.button(listUsersClearButtonFinder);
		assertTrue("Could not find the Clear button on List Users", b.exists());
		b.click();
		checkListUsersPageContentHeader();
	}
	
	/**
	 * Goes to the Add Employee User page. Assumes you are on the List Users page
	 * under Manage System Users.
	 * 
	 * @throws Exception
	 */
	public void gotoAddEmployeeUser() throws Exception {
		Link addEmployeeUser = ie.link(listUsersAddEmployeeUserFinder);
		assertTrue("Could not find the link to Add Employee User", addEmployeeUser.exists());
		addEmployeeUser.click();
		checkAddUserPageContentHeader();
		checkAddUserEmployeePermissions();
	}

	/**
	 * When on the Add User page, the Add Employee has more permissions
	 * they can select from. This checks that all the permissions are
	 * available.
	 * 
	 * @throws Exception
	 */
	private void checkAddUserEmployeePermissions() throws Exception {
		checkAddUserCustomerPermissions();
		TableRow tagProducts = ie.row(tagProductsRowFinder);
		assertTrue("Could not find the Tag Products permission selection", tagProducts.exists());
		TableRow manageSystemConfiguration = ie.row(manageSystemConfigurationRowFinder);
		assertTrue("Could not find the Manage System Configuration permission selection", manageSystemConfiguration.exists());
		TableRow manageSystemUsers = ie.row(manageSystemUsersRowFinder);
		assertTrue("Could not find the Manage System Users permission selection", manageSystemUsers.exists());
		TableRow manageEndUsers = ie.row(manageEndUsersRowFinder);
		assertTrue("Could not find the Manage End Users permission selection", manageEndUsers.exists());
		TableRow manageJobs = ie.row(manageJobsRowFinder);
		assertTrue("Could not find the Manage Jobs permission selection", manageJobs.exists());
		TableRow manageSafetyNetwork = ie.row(manageSafetyNetworkRowFinder);
		assertTrue("Could not find the Manage Safety Network permission selection", manageSafetyNetwork.exists());
	}

	private void checkAddUserPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(listAddUserPageContentHeaderFinder);
		assertTrue("Could not find the content header on Add User page.", contentHeader.exists());
	}

	private void checkEditUserPageContentHeader(String userID) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(listEditUserPageContentHeaderFinder);
		assertTrue("Could not find the content header on Edit User page.", contentHeader.exists());
		assertTrue("Could not find the userID '" + userID + "' in the content header", contentHeader.text().contains(userID));
	}

	/**
	 * Goes to the Add Customer User page. Assumes you are on the List Users page
	 * under Manage System Users.
	 * 
	 * @throws Exception
	 */
	public void gotoAddCustomerUser() throws Exception {
		Link addCustomerUser = ie.link(listUsersAddCustomerUserFinder);
		assertTrue("Could not find the link to Add Customer User", addCustomerUser.exists());
		addCustomerUser.click();
		checkAddUserPageContentHeader();
	}

	private void checkAddUserCustomerPermissions() throws Exception {
		misc.waitForJavascript();
		TableRow createInspections = ie.row(createInspectionRowFinder);
		assertTrue("Could not find the Create Inspections permission selection", createInspections.exists());
		TableRow editInspections = ie.row(editInspectionRowFinder);
		assertTrue("Could not find the Edit Inspections permission selection", editInspections.exists());
	}

	public void addCustomerUser(CustomerUser u) throws Exception {
		FieldIDMisc.stopMonitor();
		assertNotNull(u);
		assertNotNull("Must set a User ID for the customer", u.getUserID());
		assertNotNull("Must set an email address for the customer", u.getEmail());
		assertNotNull("Must set a first name for the customer", u.getFirstName());
		assertNotNull("Must set a last name for the customer", u.getLastName());
		assertNotNull("Must set a password for the customer", u.getPassword());
		assertFalse("User ID cannot be blank", u.getUserID().trim().equals(""));
		assertFalse("Email cannot be blank", u.getEmail().trim().equals(""));
		assertFalse("First Name cannot be blank", u.getFirstName().trim().equals(""));
		assertFalse("Last Name cannot be blank", u.getLastName().trim().equals(""));
		assertFalse("Password cannot be blank", u.getPassword().trim().equals(""));
		TextField addCustomerUserUserID = ie.textField(addCustomerUserUserIDFinder);
		assertTrue("Could not find the User ID text field on Add User", addCustomerUserUserID.exists());
		addCustomerUserUserID.set(u.getUserID());

		TextField addCustomerUserEmail = ie.textField(addCustomerUserEmailFinder);
		assertTrue("Could not find the Email text field on Add User", addCustomerUserEmail.exists());
		addCustomerUserEmail.set(u.getEmail());

		TextField addCustomerUserFirstName = ie.textField(addCustomerUserFirstNameFinder);
		assertTrue("Could not find the First Name text field on Add User", addCustomerUserFirstName.exists());
		addCustomerUserFirstName.set(u.getFirstName());

		TextField addCustomerUserLastName = ie.textField(addCustomerUserLastNameFinder);
		assertTrue("Could not find the Last Name text field on Add User", addCustomerUserLastName.exists());
		addCustomerUserLastName.set(u.getLastName());

		TextField addCustomerUserPosition = ie.textField(addCustomerUserPositionFinder);
		assertTrue("Could not find the Position text field on Add User", addCustomerUserPosition.exists());
		if(u.getPosition() != null) {
			addCustomerUserPosition.set(u.getPosition());
		}

		TextField addCustomerUserInitials = ie.textField(addCustomerUserInitialsFinder);
		assertTrue("Could not find the Initials text field on Add User", addCustomerUserInitials.exists());
		if(u.getInitials() != null) {
			addCustomerUserInitials.set(u.getInitials());
		}

		TextField addCustomerUserSecurityRFIDNumber = ie.textField(addCustomerUserSecurityRFIDNumberFinder);
		assertTrue("Could not find the Security Rfid Number text field on Add User", addCustomerUserSecurityRFIDNumber.exists());
		if(u.getSecurityRFIDNumber() != null) {
			addCustomerUserSecurityRFIDNumber.set(u.getSecurityRFIDNumber());
		}

		SelectList addCustomerUserCountry = ie.selectList(addCustomerUserCountryFinder);
		assertTrue("Could not find the Country select list on Add User", addCustomerUserCountry.exists());
		String c = u.getCountry();
		if(c != null) {
			c = "/" + c + "/";
			Option o = addCustomerUserCountry.option(text(c));
			assertTrue("Could not find the country '" + c + "'", o.exists());
			o.select();
			misc.waitForJavascript();
		}

		SelectList addCustomerUserTimeZone = ie.selectList(addCustomerUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone select list on Add User", addCustomerUserTimeZone.exists());
		String tz = u.getTimeZone();
		if(tz != null) {
			tz = "/" + u.getTimeZone() + "/";
			Option o = addCustomerUserTimeZone.option(text(tz));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}

		TextField addCustomerUserPassword = ie.textField(addCustomerUserPasswordFinder);
		assertTrue("Could not find the Password text field on Add User", addCustomerUserPassword.exists());
		addCustomerUserPassword.set(u.getPassword());

		TextField addCustomerUserVerifyPassword = ie.textField(addCustomerUserVerifyPasswordFinder);
		assertTrue("Could not find the Verify Password text field on Add User", addCustomerUserVerifyPassword.exists());
		addCustomerUserVerifyPassword.set(u.getPassword());
		
		misc.gotoChooseOwner();
		Owner o = u.getOwner();
		misc.setOwner(o);
		misc.selectOwner();
		
		Button submit = ie.button(addCustomerUserSubmitButtonFinder);
		assertTrue("Could not find the Submit button", submit.exists());
		submit.click();
		misc.checkForErrorMessagesOnCurrentPage();
		FieldIDMisc.startMonitor();
	}
	
	public void validate(String customer) throws Exception {
		mos.gotoManageOrganizations();
		mos.gotoEditPrimaryOrganization();
		Organization org = mos.getOrganization(true);
		String orgName = org.getName();
		mos.gotoBackToAdministration();
		gotoManageUsers();
		String userType = "Customers";
		setListUsersUserType(userType);
		String userNameOrID = "n4systems";
		setListUsersNameFilter(userNameOrID);
		gotoListUsersSearch();
		gotoListUsersClear();
		gotoAddEmployeeUser();
		gotoViewAll();
		gotoAddCustomerUser();
		String userID = misc.getRandomString(10);
		String email = "dev@n4systems.com";
		String firstName = "Validate";
		String lastName = userID;
		String password = "makemore$";
		CustomerUser u = new CustomerUser(userID, email, firstName, lastName, password);
		Owner o = new Owner(orgName);
		o.setCustomer(customer);
		u.setOwner(o);
		addCustomerUser(u);
		gotoViewAll();
		setListUsersNameFilter(u.getUserID());
		gotoListUsersSearch();
		gotoEditCustomerUser(u);
		if(isUser(u.getUserID())) {
			u.setPosition("edit");
			editCustomerUser(u);
		}
	}

	public void gotoViewAll() throws Exception {
		Link l = ie.link(manageUsersViewAllLinkFinder);
		assertTrue("Could not find the View All link", l.exists());
		l.click();
		checkManageUsersPageContentHeader();
	}

	private void checkManageUsersPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(manageUsersPageContentHeaderFinder);
		assertTrue("Could not find the content header on Manage Users page.", contentHeader.exists());
	}

	/**
	 * Checks to see if there is a link for the userid on the current page.
	 * It is recommended you do something like:
	 * 
	 *		gotoManageUsers();
	 *		setListUsersNameFilter(userid);
	 *		gotoListUsersSearch();
	 *		boolean b = isUser(userid);
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public boolean isUser(String userid) throws Exception {
		boolean result = false;
		
		Link user = ie.link(text(userid));
		result = user.exists();
		
		return result;
	}

	public void gotoEditCustomerUser(CustomerUser u) throws Exception {
		gotoEditUser(u);
	}
	
	public void gotoEditUser(SystemUser u) throws Exception {
		String userID = u.getUserID();
		Link user = ie.link(text(userID));
		assertTrue("Could not find a link to edit user '" + userID + "'", user.exists());
		user.click();
		this.checkEditUserPageContentHeader(userID);
	}

	public void editCustomerUser(CustomerUser u) throws Exception {
		FieldIDMisc.stopMonitor();
		assertNotNull(u);
		assertFalse("User ID cannot be blank", u.getUserID().trim().equals(""));
		assertFalse("Email cannot be blank", u.getEmail().trim().equals(""));
		assertFalse("First Name cannot be blank", u.getFirstName().trim().equals(""));
		assertFalse("Last Name cannot be blank", u.getLastName().trim().equals(""));
		TextField editCustomerUserUserID = ie.textField(editCustomerUserUserIDFinder);
		assertTrue("Could not find the User ID text field on Edit User", editCustomerUserUserID.exists());
		if(u.getUserID() != null) {
			editCustomerUserUserID.set(u.getUserID());
		}
		TextField editCustomerUserEmail = ie.textField(editCustomerUserEmailFinder);
		assertTrue("Could not find the Email text field on edit User", editCustomerUserEmail.exists());
		if(u.getEmail() != null) {
			editCustomerUserEmail.set(u.getEmail());
		}

		TextField editCustomerUserFirstName = ie.textField(editCustomerUserFirstNameFinder);
		assertTrue("Could not find the First Name text field on edit User", editCustomerUserFirstName.exists());
		if(u.getFirstName() != null) {
			editCustomerUserFirstName.set(u.getFirstName());
		}

		TextField editCustomerUserLastName = ie.textField(editCustomerUserLastNameFinder);
		assertTrue("Could not find the Last Name text field on edit User", editCustomerUserLastName.exists());
		if(u.getLastName() != null) {
			editCustomerUserLastName.set(u.getLastName());
		}

		TextField editCustomerUserPosition = ie.textField(editCustomerUserPositionFinder);
		assertTrue("Could not find the Position text field on edit User", editCustomerUserPosition.exists());
		if(u.getPosition() != null) {
			editCustomerUserPosition.set(u.getPosition());
		}

		TextField editCustomerUserInitials = ie.textField(editCustomerUserInitialsFinder);
		assertTrue("Could not find the Initials text field on edit User", editCustomerUserInitials.exists());
		if(u.getInitials() != null) {
			editCustomerUserInitials.set(u.getInitials());
		}

		SelectList editCustomerUserTimeZone = ie.selectList(editCustomerUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone select list on edit User", editCustomerUserTimeZone.exists());
		String tz = "/" + u.getTimeZone() + "/";
		if(tz != null) {
			Option o = editCustomerUserTimeZone.option(text(tz));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}

		// TODO: orgunit

		Button submit = ie.button(editCustomerUserSubmitButtonFinder);
		assertTrue("Could not find the Submit button", submit.exists());
		submit.click();
		FieldIDMisc.startMonitor();
	}

	private void checkEditUserCustomerPermissions() throws Exception {
		checkAddUserCustomerPermissions();
	}

	public void addEmployeeUser(EmployeeUser u) throws Exception {
		FieldIDMisc.stopMonitor();
		assertNotNull(u);
		assertNotNull("Must set a User ID for the customer", u.getUserID());
		assertNotNull("Must set an email address for the customer", u.getEmail());
		assertNotNull("Must set a first name for the customer", u.getFirstName());
		assertNotNull("Must set a last name for the customer", u.getLastName());
		assertNotNull("Must set a password for the customer", u.getPassword());
		assertFalse("User ID cannot be blank", u.getUserID().trim().equals(""));
		assertFalse("Email cannot be blank", u.getEmail().trim().equals(""));
		assertFalse("First Name cannot be blank", u.getFirstName().trim().equals(""));
		assertFalse("Last Name cannot be blank", u.getLastName().trim().equals(""));
		assertFalse("Password cannot be blank", u.getPassword().trim().equals(""));

		TextField addEmployeeUserUserID = ie.textField(addEmployeeUserUserIDFinder);
		assertTrue("Could not find the User ID text field on Add User", addEmployeeUserUserID.exists());
		addEmployeeUserUserID.set(u.getUserID());

		TextField addEmployeeUserEmail = ie.textField(addEmployeeUserEmailFinder);
		assertTrue("Could not find the Email text field on Add User", addEmployeeUserEmail.exists());
		addEmployeeUserEmail.set(u.getEmail());

		TextField addEmployeeUserFirstName = ie.textField(addEmployeeUserFirstNameFinder);
		assertTrue("Could not find the First Name text field on Add User", addEmployeeUserFirstName.exists());
		addEmployeeUserFirstName.set(u.getFirstName());

		TextField addEmployeeUserLastName = ie.textField(addEmployeeUserLastNameFinder);
		assertTrue("Could not find the Last Name text field on Add User", addEmployeeUserLastName.exists());
		addEmployeeUserLastName.set(u.getLastName());

		TextField addEmployeeUserPosition = ie.textField(addEmployeeUserPositionFinder);
		assertTrue("Could not find the Position text field on Add User", addEmployeeUserPosition.exists());
		if(u.getPosition() != null) {
			addEmployeeUserPosition.set(u.getPosition());
		}

		TextField addEmployeeUserInitials = ie.textField(addEmployeeUserInitialsFinder);
		assertTrue("Could not find the Initials text field on Add User", addEmployeeUserInitials.exists());
		if(u.getInitials() != null) {
			addEmployeeUserInitials.set(u.getInitials());
		}

		TextField addEmployeeUserSecurityRFIDNumber = ie.textField(addEmployeeUserSecurityRFIDNumberFinder);
		assertTrue("Could not find the Security Rfid Number text field on Add User", addEmployeeUserSecurityRFIDNumber.exists());
		if(u.getSecurityRFIDNumber() != null) {
			addEmployeeUserSecurityRFIDNumber.set(u.getSecurityRFIDNumber());
		}

		SelectList addEmployeeUserCountry = getAddEmployeeUserCountrySelectList();
		String country = u.getCountry();
		if(country != null) {
			Option o = addEmployeeUserCountry.option(text(country));
			assertTrue("Could not find the country '" + country + "'", o.exists());
			o.select();
			misc.waitForJavascript();
		}
		SelectList addEmployeeUserTimeZone = getAddEmployeeUserTimeZoneSelectList();
		String tz = u.getTimeZone();
		if(tz != null) {
			tz = "/" + u.getTimeZone() + "/";
			Option o = addEmployeeUserTimeZone.option(text(tz));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}

		misc.gotoChooseOwner();
		misc.setOwner(u.getOwner());
		misc.selectOwner();
		
		TextField addEmployeeUserPassword = ie.textField(addEmployeeUserPasswordFinder);
		assertTrue("Could not find the Password text field on Add User", addEmployeeUserPassword.exists());
		addEmployeeUserPassword.set(u.getPassword());

		TextField addEmployeeUserVerifyPassword = ie.textField(addEmployeeUserVerifyPasswordFinder);
		assertTrue("Could not find the Verify Password text field on Add User", addEmployeeUserVerifyPassword.exists());
		addEmployeeUserVerifyPassword.set(u.getPassword());

		checkAddUserEmployeePermissions();
		
		// Turn all the permissions off...
		Button allOff = ie.button(addEmployeeUserAllOffButtonFinder);
		assertTrue("Could not find the All Off button for Permissions", allOff.exists());
		allOff.click();
		
		// then turn on the ones we need on
		List<String> p = u.getPermissions();
		Iterator<String> i = p.iterator();
		while(i.hasNext()) {
			String permission = i.next();
			String xpath = "//DIV[@id='pageContent']/FORM/DIV/TABLE[@class='list']/TBODY/TR/TD[contains(text(),'" + permission + "')]/..";
			TableRow tr = ie.row(xpath(xpath));
			assertTrue("Could not find the Permission row containing '" + permission + "'", tr.exists());
			Radio r = tr.radio(xpath("TD/INPUT[@value='true']"));
			assertTrue("Could not find a radio button with value='true' on permission '" + permission + "'", r.exists());
			r.set();
		}
		
		Button submit = ie.button(addEmployeeUserSubmitButtonFinder);
		assertTrue("Could not find the Submit button", submit.exists());
		submit.click();
		FieldIDMisc.startMonitor();
	}

	private SelectList getAddEmployeeUserTimeZoneSelectList() throws Exception {
		SelectList addEmployeeUserTimeZone = ie.selectList(addEmployeeUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone select list on Add User", addEmployeeUserTimeZone.exists());
		return addEmployeeUserTimeZone;
	}

	private SelectList getAddEmployeeUserCountrySelectList() throws Exception {
		SelectList addEmployeeUserCountry = ie.selectList(addEmployeeUserCountryFinder);
		assertTrue("Could not find the Country select list on Add User", addEmployeeUserCountry.exists());
		return addEmployeeUserCountry;
	}

	public CustomerUser getAddCustomerUser() throws Exception {
		CustomerUser u = new CustomerUser(null, null, null, null, null);

		TextField addCustomerUserUserID = ie.textField(addCustomerUserUserIDFinder);
		assertTrue("Could not find the User ID text field on Add User", addCustomerUserUserID.exists());
		u.setUserID(addCustomerUserUserID.value());

		TextField addCustomerUserEmail = ie.textField(addCustomerUserEmailFinder);
		assertTrue("Could not find the Email text field on Add User", addCustomerUserEmail.exists());
		u.setEmail(addCustomerUserEmail.value());

		TextField addCustomerUserFirstName = ie.textField(addCustomerUserFirstNameFinder);
		assertTrue("Could not find the First Name text field on Add User", addCustomerUserFirstName.exists());
		u.setFirstName(addCustomerUserFirstName.value());

		TextField addCustomerUserLastName = ie.textField(addCustomerUserLastNameFinder);
		assertTrue("Could not find the Last Name text field on Add User", addCustomerUserLastName.exists());
		u.setLastName(addCustomerUserLastName.value());

		TextField addCustomerUserPosition = ie.textField(addCustomerUserPositionFinder);
		assertTrue("Could not find the Position text field on Add User", addCustomerUserPosition.exists());
		u.setPosition(addCustomerUserPosition.value());

		TextField addCustomerUserInitials = ie.textField(addCustomerUserInitialsFinder);
		assertTrue("Could not find the Initials text field on Add User", addCustomerUserInitials.exists());
		u.setInitials(addCustomerUserInitials.value());

		TextField addCustomerUserSecurityRFIDNumber = ie.textField(addCustomerUserSecurityRFIDNumberFinder);
		assertTrue("Could not find the Security Rfid Number text field on Add User", addCustomerUserSecurityRFIDNumber.exists());
		u.setSecurityRFIDNumber(addCustomerUserSecurityRFIDNumber.value());

		SelectList addCustomerUserCountry = ie.selectList(addCustomerUserCountryFinder);
		assertTrue("Could not find the Country select list on Add User", addCustomerUserCountry.exists());
		Option countrySelected = addCustomerUserCountry.option(value(addCustomerUserCountry.value()));
		u.setCountry(countrySelected.text());

		SelectList addCustomerUserTimeZone = ie.selectList(addCustomerUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone select list on Add User", addCustomerUserTimeZone.exists());
		Option timeZoneSelected = addCustomerUserTimeZone.option(value(addCustomerUserTimeZone.value()));
		u.setTimeZone(timeZoneSelected.text());

		misc.gotoChooseOwner();
		Owner o = misc.getOwner();
		u.setOwner(o);
		misc.cancelOwner();

		return u;
	}

	public String getCountryFromAddEmployeeUser() throws Exception {
		SelectList addEmployeeUserCountry = getAddEmployeeUserCountrySelectList();
		return addEmployeeUserCountry.getSelectedItems().get(0);
	}

	public String getTimeZoneFromAddEmployeeUser() throws Exception {
		SelectList addEmployeeUserTimeZone = getAddEmployeeUserTimeZoneSelectList();
		return addEmployeeUserTimeZone.getSelectedItems().get(0);
	}

	public List<String> getUserIDs() throws Exception {
		List<String> results = new ArrayList<String>();
		
		FieldIDMisc.stopMonitor();
		
		boolean loopFlag = true;
		do {
			List<String> tmp = getUserIDsFromCurrentPage();
			results.addAll(tmp);
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		FieldIDMisc.startMonitor();

		return results;
	}

	private List<String> getUserIDsFromCurrentPage() throws Exception {
		List<String> results = new ArrayList<String>();

		Links userIDs = ie.links(userIDLinkFinder);
		assertNotNull("Could not find any links to user ids", userIDs);
		Iterator<Link> i = userIDs.iterator();
		while(i.hasNext()) {
			Link userID = i.next();
			String s = userID.text();
			results.add(s.trim());
		}

		return results;
	}

	public void gotoEditEmployeeUser(EmployeeUser u) throws Exception {
		gotoEditUser(u);
	}

	public void editEmployeeUser(EmployeeUser u) throws Exception {
		FieldIDMisc.stopMonitor();
		assertNotNull(u);
		assertFalse("User ID cannot be blank", u.getUserID().trim().equals(""));
		assertFalse("Email cannot be blank", u.getEmail().trim().equals(""));
		assertFalse("First Name cannot be blank", u.getFirstName().trim().equals(""));
		assertFalse("Last Name cannot be blank", u.getLastName().trim().equals(""));
		TextField editEmployeeUserUserID = ie.textField(editEmployeeUserUserIDFinder);
		assertTrue("Could not find the User ID text field on Edit User", editEmployeeUserUserID.exists());
		if(u.getUserID() != null) {
			editEmployeeUserUserID.set(u.getUserID());
		}
		TextField editEmployeeUserEmail = ie.textField(editEmployeeUserEmailFinder);
		assertTrue("Could not find the Email text field on edit User", editEmployeeUserEmail.exists());
		if(u.getEmail() != null) {
			editEmployeeUserEmail.set(u.getEmail());
		}

		TextField editEmployeeUserFirstName = ie.textField(editEmployeeUserFirstNameFinder);
		assertTrue("Could not find the First Name text field on edit User", editEmployeeUserFirstName.exists());
		if(u.getFirstName() != null) {
			editEmployeeUserFirstName.set(u.getFirstName());
		}

		TextField editEmployeeUserLastName = ie.textField(editEmployeeUserLastNameFinder);
		assertTrue("Could not find the Last Name text field on edit User", editEmployeeUserLastName.exists());
		if(u.getLastName() != null) {
			editEmployeeUserLastName.set(u.getLastName());
		}

		TextField editEmployeeUserPosition = ie.textField(editEmployeeUserPositionFinder);
		assertTrue("Could not find the Position text field on edit User", editEmployeeUserPosition.exists());
		if(u.getPosition() != null) {
			editEmployeeUserPosition.set(u.getPosition());
		}

		TextField editEmployeeUserInitials = ie.textField(editEmployeeUserInitialsFinder);
		assertTrue("Could not find the Initials text field on edit User", editEmployeeUserInitials.exists());
		if(u.getInitials() != null) {
			editEmployeeUserInitials.set(u.getInitials());
		}

		SelectList editEmployeeUserCountry = ie.selectList(editEmployeeUserCountryFinder);
		assertTrue("Could not find the Country select list on edit User", editEmployeeUserCountry.exists());
		String c = u.getCountry();
		if(c != null) {
			c = "/" + c + "/";
			Option o = editEmployeeUserCountry.option(text(c));
			assertTrue("Could not find the country '" + c + "'", o.exists());
			o.select();
		}

		SelectList editEmployeeUserTimeZone = ie.selectList(editEmployeeUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone select list on edit User", editEmployeeUserTimeZone.exists());
		String tz = u.getTimeZone();
		if(tz != null) {
			tz = "/" + tz + "/";
			Option o = editEmployeeUserTimeZone.option(text(tz));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}

		// TODO: orgunit

		// Turn all the permissions off...
		Button allOff = ie.button(addEmployeeUserAllOffButtonFinder);
		assertTrue("Could not find the All Off button for Permissions", allOff.exists());
		allOff.click();
		
		// then turn on the ones we need on
		List<String> p = u.getPermissions();
		Iterator<String> i = p.iterator();
		while(i.hasNext()) {
			String permission = i.next();
			String xpath = "//DIV[@id='pageContent']/FORM/DIV/TABLE[@class='list']/TBODY/TR/TD[contains(text(),'" + permission + "')]/..";
			TableRow tr = ie.row(xpath(xpath));
			assertTrue("Could not find the Permission row containing '" + permission + "'", tr.exists());
			Radio r = tr.radio(xpath("TD/INPUT[@value='true']"));
			assertTrue("Could not find a radio button with value='true' on permission '" + permission + "'", r.exists());
			r.set();
		}

		Button submit = ie.button(editEmployeeUserSubmitButtonFinder);
		assertTrue("Could not find the Submit button", submit.exists());
		submit.click();
		FieldIDMisc.startMonitor();
	}
}
