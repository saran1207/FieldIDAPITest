package com.n4systems.fieldid.admin;

import static watij.finders.FinderFactory.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.datatypes.CustomerUser;

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
	private Finder editCustomerUserCustomerFinder;
	private Finder editCustomerUserDivisionFinder;
	private Finder editCustomerUserAllOffButtonFinder;
	private Finder editCustomerUserSubmitButtonFinder;

	public ManageUsers(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			listUsersFinder = text(p.getProperty("link"));
			listUsersPageContentHeaderFinder = xpath(p.getProperty("contentheader"));
			listUsersFilterNameFinder = id(p.getProperty("listusersfiltername"));
			listUsersFilterUserTypeFinder = id(p.getProperty("listusersfilterusertype"));
			listUsersSearchButtonFinder = id(p.getProperty("listusersfiltersearchbutton"));
			listUsersClearButtonFinder = id(p.getProperty("listusersfilterclearbutton"));
			listUsersAddEmployeeUserFinder = xpath(p.getProperty("listusersaddemployee"));
			listUsersAddCustomerUserFinder = xpath(p.getProperty("listusersaddcustomer"));
			listAddUserPageContentHeaderFinder = xpath(p.getProperty("addusercontentheader"));
			tagProductsRowFinder = xpath(p.getProperty("addusertagproductsrow"));
			manageSystemConfigurationRowFinder = xpath(p.getProperty("addusermanagesystemconfigurationrow"));
			manageSystemUsersRowFinder = xpath(p.getProperty("addusermanagesystemusersrow"));
			manageEndUsersRowFinder = xpath(p.getProperty("addusermanageendusersrow"));
			createInspectionRowFinder = xpath(p.getProperty("addusercreateinspectionrow"));
			editInspectionRowFinder = xpath(p.getProperty("addusereditinspectionrow"));
			manageJobsRowFinder = xpath(p.getProperty("addusermanagejobsrow"));
			addCustomerUserUserIDFinder = id(p.getProperty("addcustomeruserid"));
			addCustomerUserEmailFinder = id(p.getProperty("addcustomeremail"));
			addCustomerUserFirstNameFinder = id(p.getProperty("addcustomerfirstname"));
			addCustomerUserLastNameFinder = id(p.getProperty("addcustomerlastname"));
			addCustomerUserPositionFinder = id(p.getProperty("addcustomerposition"));
			addCustomerUserInitialsFinder = id(p.getProperty("addcustomerinitials"));
			addCustomerUserSecurityRFIDNumberFinder = id(p.getProperty("addcustomersecurityrfidnumber"));
			addCustomerUserTimeZoneFinder = id(p.getProperty("addcustomertimezone"));
			addCustomerUserCustomerFinder = id(p.getProperty("addcustomercustomer"));
			addCustomerUserDivisionFinder = id(p.getProperty("addcustomerdivision"));
			addCustomerUserPasswordFinder = id(p.getProperty("addcustomerpassword"));
			addCustomerUserVerifyPasswordFinder = id(p.getProperty("addcustomerverifypassword"));
			addCustomerUserAllOffButtonFinder = name(p.getProperty("addcustomeralloffbutton"));
			addCustomerUserSubmitButtonFinder = id(p.getProperty("addcustomersubmitbutton"));
			manageUsersViewAllLinkFinder = xpath(p.getProperty("manageusersviewalllink"));
			manageUsersPageContentHeaderFinder = xpath(p.getProperty("pagecontentheader"));
			listEditUserPageContentHeaderFinder = xpath(p.getProperty("editusercontentheader"));
			editCustomerUserUserIDFinder = xpath(p.getProperty("edituseruserid"));
			editCustomerUserEmailFinder = xpath(p.getProperty("edituseremail"));
			editCustomerUserFirstNameFinder = xpath(p.getProperty("edituserfirstname"));
			editCustomerUserLastNameFinder = xpath(p.getProperty("edituserlastname"));
			editCustomerUserPositionFinder = xpath(p.getProperty("edituserposition"));
			editCustomerUserInitialsFinder = xpath(p.getProperty("edituserinitials"));
			editCustomerUserTimeZoneFinder = xpath(p.getProperty("editusertimezone"));
			editCustomerUserCustomerFinder = xpath(p.getProperty("editusercustomer"));
			editCustomerUserDivisionFinder = xpath(p.getProperty("edituserdivision"));
			editCustomerUserAllOffButtonFinder = xpath(p.getProperty("edituseralloffbutton"));
			editCustomerUserSubmitButtonFinder = xpath(p.getProperty("editusersubmitbutton"));
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
		checkAddUserCustomerPermissions();
	}

	private void checkAddUserCustomerPermissions() throws Exception {
		TableRow createInspections = ie.row(createInspectionRowFinder);
		assertTrue("Could not find the Create Inspections permission selection", createInspections.exists());
		TableRow editInspections = ie.row(editInspectionRowFinder);
		assertTrue("Could not find the Edit Inspections permission selection", editInspections.exists());
	}

	public void addCustomerUser(CustomerUser u) throws Exception {
		misc.stopMonitorStatus();
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

		SelectList addCustomerUserTimeZone = ie.selectList(addCustomerUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone select list on Add User", addCustomerUserTimeZone.exists());
		String tz = u.getTimeZone();
		if(tz != null) {
			tz = "/" + u.getTimeZone() + "/";
			Option o = addCustomerUserTimeZone.option(text(tz));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}

		SelectList addCustomerUserCustomer = ie.selectList(addCustomerUserCustomerFinder);
		assertTrue("Could not find the Customer select list on Add User", addCustomerUserCustomer.exists());
		String customer = u.getCustomer();
		if(customer != null) {
			Option o = addCustomerUserCustomer.option(text(customer));
			assertTrue("Could not find the customer '" + customer + "'", o.exists());
			o.select();
			addCustomerUserCustomer.fireEvent("onchange");
			misc.waitForJavascript();
		}

		SelectList addCustomerUserDivision = ie.selectList(addCustomerUserDivisionFinder);
		assertTrue("Could not find the Divison select list on Add User", addCustomerUserDivision.exists());
		String division = u.getDivision();
		if(division != null) {
			Option o = addCustomerUserDivision.option(text(division));
			assertTrue("Could not find the division '" + division + "'", o.exists());
			o.select();
		}

		TextField addCustomerUserPassword = ie.textField(addCustomerUserPasswordFinder);
		assertTrue("Could not find the Password text field on Add User", addCustomerUserPassword.exists());
		addCustomerUserPassword.set(u.getPassword());

		TextField addCustomerUserVerifyPassword = ie.textField(addCustomerUserVerifyPasswordFinder);
		assertTrue("Could not find the Verify Password text field on Add User", addCustomerUserVerifyPassword.exists());
		addCustomerUserVerifyPassword.set(u.getPassword());

		checkAddUserCustomerPermissions();
		
		// Turn all the permissions off...
		Button allOff = ie.button(addCustomerUserAllOffButtonFinder);
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
		
		Button submit = ie.button(addCustomerUserSubmitButtonFinder);
		assertTrue("Could not find the Submit button", submit.exists());
		submit.click();
		misc.startMonitorStatus();
	}
	
	public void validate() throws Exception {
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
		String userID = u.getUserID();
		Link user = ie.link(text(userID));
		assertTrue("Could not find a link to edit user '" + userID + "'", user.exists());
		user.click();
		this.checkEditUserPageContentHeader(userID);
	}

	public void editCustomerUser(CustomerUser u) throws Exception {
		misc.stopMonitorStatus();
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

		SelectList editCustomerUserCustomer = ie.selectList(editCustomerUserCustomerFinder);
		assertTrue("Could not find the Customer select list on edit User", editCustomerUserCustomer.exists());
		String customer = u.getCustomer();
		if(customer != null) {
			Option o = editCustomerUserCustomer.option(text(customer));
			assertTrue("Could not find the customer '" + customer + "'", o.exists());
			o.select();
			editCustomerUserCustomer.fireEvent("onchange");
			misc.waitForJavascript();
		}

		SelectList editCustomerUserDivision = ie.selectList(editCustomerUserDivisionFinder);
		assertTrue("Could not find the Divison select list on edit User", editCustomerUserDivision.exists());
		String division = u.getDivision();
		if(division != null) {
			Option o = editCustomerUserDivision.option(text(division));
			assertTrue("Could not find the division '" + division + "'", o.exists());
			o.select();
		}

		checkEditUserCustomerPermissions();
		
		// Turn all the permissions off...
		Button allOff = ie.button(editCustomerUserAllOffButtonFinder);
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
		
		Button submit = ie.button(editCustomerUserSubmitButtonFinder);
		assertTrue("Could not find the Submit button", submit.exists());
		submit.click();
		misc.startMonitorStatus();
	}

	private void checkEditUserCustomerPermissions() throws Exception {
		checkAddUserCustomerPermissions();
	}
}
