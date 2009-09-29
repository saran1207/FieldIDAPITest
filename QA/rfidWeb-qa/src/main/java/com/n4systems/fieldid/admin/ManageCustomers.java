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

import com.n4systems.fieldid.*;
import com.n4systems.fieldid.datatypes.Customer;
import com.n4systems.fieldid.datatypes.CustomerDivision;
import com.n4systems.fieldid.datatypes.CustomerUser;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageCustomers extends TestCase {
	IE ie = null;
	FieldIDMisc misc = null;
	ManageUsers mus = null;
	ManageOrganizations mos = null;
	Properties p;
	InputStream in;
	String propertyFile = "managecustomers.properties";
	private Finder manageCustomersFinder;
	private Finder manageCustomersPageContentHeaderFinder;
	private Finder manageCustomersFilterTextFieldFinder;
	private Finder manageCustomersFilterButtonFinder;
	private Finder manageCustomersAddCustomerFinder;
	private Finder editCustomerPageContentHeaderFinder;
	private Finder editCustomerCustomerIDFinder;
	private Finder editCustomerCustomerNameFinder;
	private Finder editCustomerContactNameFinder;
	private Finder editCustomerContactEmailFinder;
	private Finder editCustomerStreetFinder;
	private Finder editCustomerCityFinder;
	private Finder editCustomerStateFinder;
	private Finder editCustomerZipFinder;
	private Finder editCustomerCountryFinder;
	private Finder editCustomerPhone1Finder;
	private Finder editCustomerPhone2Finder;
	private Finder editCustomerFaxFinder;
	private Finder editCustomerSaveButtonFinder;
	private Finder backToCustomerListLinkFinder;
	private Finder editCustomerFromViewCustomerFinder;
	private Finder backToCustomerListFromEditLinkFinder;
	private Finder customerNameAndIDLinkFinder;
	private Finder backToAdministrationLinkFinder;
	private Finder addCustomerUserLinkFinder;
	private Finder manageUsersPageContentHeaderFinder;
	private Finder addCustomerUserUserIDFinder;
	private Finder addCustomerUserEmailFinder;
	private Finder addCustomerUserFirstNameFinder;
	private Finder addCustomerUserLastNameFinder;
	private Finder addCustomerUserPositionFinder;
	private Finder addCustomerUserInitialsFinder;
	private Finder addCustomerUserSecurityRFIDFinder;
	private Finder addCustomerUserTimeZoneFinder;
	private Finder addCustomerUserDivisionFinder;
	private Finder addCustomerUserPasswordFinder;
	private Finder addCustomerUserVerifyPasswordFinder;
	private Finder addCustomerUserPermissionsTableFinder;
	private Finder addCustomerUserSaveButtonFinder;
	private Finder manageUserPageContentHeaderFinder;
	private Finder customerDivisionsLinkFinder;
	private Finder customerUsersLinkFinder;
	private Finder manageCustomerDivisionFinder;
	private Finder manageCustomerDivisionSubmitButtonFinder;
	private Finder updateDivisionErrorMessagesFinder;
	private Finder addDivisionErrorMessagesFinder;
	private Finder manageCustomerUsersTableFinder;
	private Finder manageCustomerEditUserPermissionsAllOnButtonFinder;
	private Finder manageCustomerEditUserPermissionsAllOffButtonFinder;
	private Finder editCustomerUserUserIDFinder;
	private Finder editCustomerUserEmailFinder;
	private Finder editCustomerUserFirstNameFinder;
	private Finder editCustomerUserLastNameFinder;
	private Finder editCustomerUserPositionFinder;
	private Finder editCustomerUserInitialsFinder;
	private Finder editCustomerUserTimeZoneFinder;
	private Finder editCustomerUserDivisionFinder;
	private Finder editCustomerUserPermissionsTableFinder;
	private Finder editCustomerUserSaveButtonFinder;
	private Finder divisionContainerFinder;
	private Finder manageCustomerAddDivisionFinder;
	private Finder manageCustomerAddDivisionIDFinder;
	private Finder manageCustomerAddDivisionNameFinder;
	private Finder manageCustomerAddDivisionContactNameFinder;
	private Finder manageCustomerAddDivisionContactEmailFinder;
	private Finder manageCustomerAddDivisionStreetAddressFinder;
	private Finder manageCustomerAddDivisionCityFinder;
	private Finder manageCustomerAddDivisionStateFinder;
	private Finder manageCustomerAddDivisionZipFinder;
	private Finder manageCustomerAddDivisionCountryFinder;
	private Finder manageCustomerAddDivisionPhone1Finder;
	private Finder manageCustomerAddDivisionPhone2Finder;
	private Finder manageCustomerAddDivisionFaxFinder;
	private Finder manageCustomerAddDivisionSaveFinder;
	private Finder manageCustomerUsersLinkFinder;
	private Finder manageCustomerAddUserLinkFinder;
	private Finder editCustomerUserCountryFinder;
	private Finder addCustomerUserCountryFinder;

	public ManageCustomers(IE ie) {
		this.ie = ie;
		try {
			misc = new FieldIDMisc(ie);
			mus = new ManageUsers(ie);
			mos = new ManageOrganizations(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			manageCustomerAddUserLinkFinder = xpath(p.getProperty("managecustomeradduser"));
			manageCustomerUsersLinkFinder = xpath(p.getProperty("managecustomeruserslink"));
			manageCustomersFinder = text(p.getProperty("link"));
			manageCustomersPageContentHeaderFinder = xpath(p.getProperty("contentheader"));
			manageCustomersFilterTextFieldFinder = id(p.getProperty("managecustomersfiltertextfield"));
			manageCustomersFilterButtonFinder = id(p.getProperty("managecustomersfilterbutton"));
			manageCustomersAddCustomerFinder = xpath(p.getProperty("managecustomersaddcustomer"));
			editCustomerPageContentHeaderFinder = xpath(p.getProperty("editcustomercontentheader"));
			editCustomerCustomerIDFinder = id(p.getProperty("editcustomercustomerid"));
			editCustomerCustomerNameFinder = id(p.getProperty("editcustomercustomername"));
			editCustomerContactNameFinder = id(p.getProperty("editcustomercontactname"));
			editCustomerContactEmailFinder = id(p.getProperty("editcustomercontactemail"));
			editCustomerStreetFinder = id(p.getProperty("editcustomerstreet"));
			editCustomerCityFinder = id(p.getProperty("editcustomercity"));
			editCustomerStateFinder = id(p.getProperty("editcustomerstate"));
			editCustomerZipFinder = id(p.getProperty("editcustomerzip"));
			editCustomerCountryFinder = id(p.getProperty("editcustomercountry"));
			editCustomerPhone1Finder = id(p.getProperty("editcustomerphone1"));
			editCustomerPhone2Finder = id(p.getProperty("editcustomerphone2"));
			editCustomerFaxFinder = id(p.getProperty("editcustomerfax"));
			editCustomerSaveButtonFinder = id(p.getProperty("editcustomersavebutton"));
			backToCustomerListLinkFinder = xpath(p.getProperty("managecustomerbacktocustomerlist"));
			editCustomerFromViewCustomerFinder = xpath(p.getProperty("editcustomerfromviewcustomer"));
			backToCustomerListFromEditLinkFinder = xpath(p.getProperty("editcustomerbacktocustomerlist"));
			customerNameAndIDLinkFinder = xpath(p.getProperty("managecustomernameandidlinks"));
			backToAdministrationLinkFinder = xpath(p.getProperty("backtoadministrationlink"));
			addCustomerUserLinkFinder = xpath(p.getProperty("addcustomeruserlink"));
			manageUsersPageContentHeaderFinder = xpath(p.getProperty("manageuserspageheader"));
			manageUserPageContentHeaderFinder = xpath(p.getProperty("manageuserpageheader"));
			addCustomerUserUserIDFinder = xpath(p.getProperty("addcustomeruseruserid"));
			addCustomerUserEmailFinder = xpath(p.getProperty("addcustomeruseremail"));
			addCustomerUserFirstNameFinder = xpath(p.getProperty("addcustomeruserfirstname"));
			addCustomerUserLastNameFinder = xpath(p.getProperty("addcustomeruserlastname"));
			addCustomerUserPositionFinder = xpath(p.getProperty("addcustomeruserposition"));
			addCustomerUserInitialsFinder = xpath(p.getProperty("addcustomeruserinitials"));
			addCustomerUserSecurityRFIDFinder = xpath(p.getProperty("addcustomeruserrfid"));
			addCustomerUserCountryFinder = xpath(p.getProperty("addcustomerusercountry"));
			addCustomerUserTimeZoneFinder = xpath(p.getProperty("addcustomerusertimezone"));
			addCustomerUserDivisionFinder = xpath(p.getProperty("addcustomeruserdivision"));
			addCustomerUserPasswordFinder = xpath(p.getProperty("addcustomeruserpassword"));
			addCustomerUserVerifyPasswordFinder = xpath(p.getProperty("addcustomeruserverify"));
			addCustomerUserPermissionsTableFinder = xpath(p.getProperty("addcustomeruserpermissionstable"));
			addCustomerUserSaveButtonFinder = xpath(p.getProperty("addcustomerusersavebutton"));
			customerDivisionsLinkFinder = xpath(p.getProperty("managecustomerdivisionslink"));
			customerUsersLinkFinder = xpath(p.getProperty("managecustomeruserslink"));
			manageCustomerDivisionFinder = xpath(p.getProperty("managecustomerdivisiontextfield"));
			manageCustomerDivisionSubmitButtonFinder = xpath(p.getProperty("managecustomerdivisionsubmitbutton"));
			updateDivisionErrorMessagesFinder = xpath(p.getProperty("updatedivisionerrormessages"));
			addDivisionErrorMessagesFinder = xpath(p.getProperty("adddivisionerrormessages"));
			manageCustomerUsersTableFinder = xpath(p.getProperty("managecustomeruserstable"));
			manageCustomerEditUserPermissionsAllOnButtonFinder = xpath(p.getProperty("managecustomeredituserpermissionsallonbutton"));
			manageCustomerEditUserPermissionsAllOffButtonFinder = xpath(p.getProperty("managecustomeredituserpermissionsalloffbutton"));
			editCustomerUserUserIDFinder = xpath(p.getProperty("editcustomeruseruserid"));
			editCustomerUserEmailFinder = xpath(p.getProperty("editcustomeruseremail"));
			editCustomerUserFirstNameFinder = xpath(p.getProperty("editcustomeruserfirstname"));
			editCustomerUserLastNameFinder = xpath(p.getProperty("editcustomeruserlastname"));
			editCustomerUserPositionFinder = xpath(p.getProperty("editcustomeruserposition"));
			editCustomerUserInitialsFinder = xpath(p.getProperty("editcustomeruserinitials"));
			editCustomerUserCountryFinder = xpath(p.getProperty("editcustomerusercountry"));
			editCustomerUserTimeZoneFinder = xpath(p.getProperty("editcustomerusertimezone"));
			editCustomerUserDivisionFinder = xpath(p.getProperty("editcustomeruserdivision"));
			editCustomerUserPermissionsTableFinder = xpath(p.getProperty("editcustomeruserpermissiontable"));
			editCustomerUserSaveButtonFinder = xpath(p.getProperty("editcustomerusersavebutton"));
			divisionContainerFinder = xpath(p.getProperty("divisioncontainer"));
			manageCustomerAddDivisionFinder = xpath(p.getProperty("adddivisionlink"));
			manageCustomerAddDivisionIDFinder = xpath(p.getProperty("managecustomeradddivisionid"));
			manageCustomerAddDivisionNameFinder = xpath(p.getProperty("managecustomeradddivisionname"));
			manageCustomerAddDivisionContactNameFinder = xpath(p.getProperty("managecustomeradddivisioncontactname"));
			manageCustomerAddDivisionContactEmailFinder = xpath(p.getProperty("managecustomeradddivisioncontactemail"));
			manageCustomerAddDivisionStreetAddressFinder = xpath(p.getProperty("managecustomeradddivisionstreetaddress"));
			manageCustomerAddDivisionCityFinder = xpath(p.getProperty("managecustomeradddivisioncity"));
			manageCustomerAddDivisionStateFinder = xpath(p.getProperty("managecustomeradddivisionstate"));
			manageCustomerAddDivisionZipFinder = xpath(p.getProperty("managecustomeradddivisionzip"));
			manageCustomerAddDivisionCountryFinder = xpath(p.getProperty("managecustomeradddivisioncountry"));
			manageCustomerAddDivisionPhone1Finder = xpath(p.getProperty("managecustomeradddivisionphone1"));
			manageCustomerAddDivisionPhone2Finder = xpath(p.getProperty("managecustomeradddivisionphone2"));
			manageCustomerAddDivisionFaxFinder = xpath(p.getProperty("managecustomeradddivisionfax"));
			manageCustomerAddDivisionSaveFinder = xpath(p.getProperty("managecustomeradddivisionsavebutton"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	/**
	 * Goes to the Manage Customer area. Assumes you are
	 * already in the Administration area. Needs a call to
	 * gotoAdministration.
	 * 
	 * @throws Exception
	 */
	public void gotoManageCustomers() throws Exception {
		Link manageCustomers = ie.link(manageCustomersFinder);
		assertTrue("Could not find the link to Manage Customers", manageCustomers.exists());
		manageCustomers.click();
		checkManageCustomersPageContentHeader();
	}

	private void checkManageCustomersPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(manageCustomersPageContentHeaderFinder);
		assertTrue("Could not find the content header on Manage Customers page.", contentHeader.exists());
	}
	
	/**
	 * Goes to the Add Customer page. Assumes you are already
	 * in the Manage Customer area. Needs a call to the
	 * gotoAdministration and then gotoManageCustomers.
	 * 
	 * @throws Exception
	 */
	public void gotoAddCustomer() throws Exception {
		Link addCustomer = ie.link(manageCustomersAddCustomerFinder);
		assertTrue("Could not find the link to add a customer", addCustomer.exists());
		addCustomer.click();
		checkManageCustomersPageContentHeader();
	}

	/**
	 * Sets the value for the Filter By Name text field.
	 * It does not press the Filter button. See the
	 * gotoAddCustomerFilter.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void setAddCustomerFilter(String name) throws Exception {
		assertNotNull(name);
		TextField filter = ie.textField(manageCustomersFilterTextFieldFinder);
		assertTrue("Could not find the text field for Filter by Name", filter.exists());
		filter.set(name);
	}
	
	/**
	 * Press the Filter button on the List Customers page.
	 * 
	 * @throws Exception
	 */
	public void gotoAddCustomerFilter() throws Exception {
		Button filter = ie.button(manageCustomersFilterButtonFinder);
		assertTrue("Could not find the Filter button", filter.exists());
		filter.click();
		checkManageCustomersPageContentHeader();
	}

	/**
	 * Returns true if the customer is on the list of customers.
	 * It is recommended to use setAddCustomerFilter and
	 * gotoAddCustomerFilter to reduce the size of the list to
	 * search for the customer. Assumes you are on the List
	 * Customer page.
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public boolean isCustomer(Customer customer) throws Exception {
		assertNotNull(customer);
		boolean result = false;
		String c = getCustomerString(customer);
		Link customerLink = ie.link(text(c));
		result = customerLink.exists();
		return result;
	}
	
	private String getCustomerString(Customer customer) throws Exception {
		assertNotNull(customer);
		String c = customer.getCustomerName() + " - (" + customer.getCustomerID() + ")";
		return c;
	}

	/**
	 * Goes to the Show Customer page. Assumes you are on the
	 * List Customers page. If there is more than one page of
	 * customers, this assumes the customer is on the current
	 * page. You can use the Filter By Name feature to ensure
	 * the customer is on the current page.
	 * 
	 * @param customer
	 * @throws Exception
	 */
	public void gotoCustomer(Customer customer) throws Exception {
		assertNotNull(customer);
		Link c = ie.link(text(getCustomerString(customer)));
		assertTrue("Could not find a link to the customer name: '" + customer.getCustomerName() + "', customer ID: '" + customer.getCustomerID() + "'", c.exists());
		c.click();
		checkManageCustomerPageContentHeader();
	}

	private void checkManageCustomerPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(editCustomerPageContentHeaderFinder);
		assertTrue("Could not find the content header for Edit Customer page", contentHeader.exists());
	}

	/**
	 * Goes to the Edit Customer page for the given customer from
	 * the List Customers page.
	 * 
	 * To find the Edit link I use an xpath. I find an element with
	 * an ID attribute, traverse down to the Show Customer link then
	 * assumes the Edit link is on the same row (i.e. back up two
	 * elements, down to the next cell and find the Edit link.
	 * 
	 * @param customer
	 * @throws Exception
	 */
	public void gotoEditCustomer(Customer customer) throws Exception {
		assertNotNull(customer);
		String c = getCustomerString(customer);
		Link edit = ie.link(xpath("//DIV[@id='pageContent']/TABLE/TBODY/TR/TD[1]/A[text()='"
				+ c + "']/../../TD[2]/A[text()='Edit']"));
		assertTrue("Could not find the Edit link for customer '" + c + "'", edit.exists());
		edit.click();
		checkEditCustomerPageContentHeader();
	}

	private void checkEditCustomerPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(editCustomerPageContentHeaderFinder);
		assertTrue("Could not find the content header for Edit Customer page", contentHeader.exists());
	}

	/**
	 * This will fill in the Edit Customer form and save it. It assumes
	 * you are already on the Edit Customer page. See gotoEditCustomer.
	 * 
	 * @param customer
	 * @throws Exception
	 */
	public void editCustomer(Customer customer) throws Exception {
		assertNotNull(customer);
		TextField customerID = ie.textField(editCustomerCustomerIDFinder);
		assertTrue("Could not find the text field for Customer ID", customerID.exists());
		String s = customer.getCustomerID();
		if(s != null) {
			customerID.set(s);
		}

		TextField customerName = ie.textField(editCustomerCustomerNameFinder);
		assertTrue("Could not find the text field for Customer Name", customerName.exists());
		s = customer.getCustomerName();
		if(s != null) {
			customerName.set(s);
		}
		
		// TODO: edit Organizational Unit

		TextField contactName = ie.textField(editCustomerContactNameFinder);
		assertTrue("Could not find the text field for Contact Name", contactName.exists());
		s = customer.getContactName();
		if(s != null) {
			contactName.set(s);
		}

		TextField contactEmail = ie.textField(editCustomerContactEmailFinder);
		assertTrue("Could not find the text field for Contact Email", contactEmail.exists());
		s = customer.getContactEmail();
		if(s != null) {
			contactEmail.set(s);
		}

		TextField street = ie.textField(editCustomerStreetFinder);
		assertTrue("Could not find the text field for Street", street.exists());
		s = customer.getStreetAddress();
		if(s != null) {
			street.set(s);
		}

		TextField city = ie.textField(editCustomerCityFinder);
		assertTrue("Could not find the text field for City", city.exists());
		s = customer.getCity();
		if(s != null) {
			city.set(s);
		}

		TextField state = ie.textField(editCustomerStateFinder);
		assertTrue("Could not find the text field for State", state.exists());
		s = customer.getState();
		if(s != null) {
			state.set(s);
		}

		TextField zip = ie.textField(editCustomerZipFinder);
		assertTrue("Could not find the text field for Zip", zip.exists());
		s = customer.getZip();
		if(s != null) {
			zip.set(s);
		}

		TextField country = ie.textField(editCustomerCountryFinder);
		assertTrue("Could not find the text field for Country", country.exists());
		s = customer.getCountry();
		if(s != null) {
			country.set(s);
		}

		TextField phone1 = ie.textField(editCustomerPhone1Finder);
		assertTrue("Could not find the text field for Phone 1", phone1.exists());
		s = customer.getPhone1();
		if(s != null) {
			phone1.set(s);
		}

		TextField phone2 = ie.textField(editCustomerPhone2Finder);
		assertTrue("Could not find the text field for Phone 2", phone2.exists());
		s = customer.getPhone2();
		if(s != null) {
			phone2.set(s);
		}

		TextField fax = ie.textField(editCustomerFaxFinder);
		assertTrue("Could not find the text field for Fax", fax.exists());
		s = customer.getFax();
		if(s != null) {
			fax.set(s);
		}

		Button save = ie.button(editCustomerSaveButtonFinder);
		assertTrue("Could not find the Save button for editing a customer", save.exists());
		save.click();
		this.checkManageCustomerPageContentHeader();
	}

	public void addCustomer(Customer customer) throws Exception {
		assertNotNull(customer);
		assertNotNull(customer.getCustomerID());
		assertNotNull(customer.getCustomerName());
		// Add Customer is using the same form as edit customer
		editCustomer(customer);
	}
	
	/**
	 * Assumes you are already in the Administration section.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		mos.gotoManageOrganizations();
		List<String> orgUnits = mos.getOrganizationNames();
		assertTrue("There are no organizational units for this tenant", orgUnits.size() > 0);
		String orgUnit = orgUnits.get(0);
		mos.gotoBackToAdministration();
		gotoManageCustomers();
		gotoAddCustomer();
		int length = 15;
		String customerID = misc.getRandomString(length);
		String customerName = misc.getRandomString(length);
		Customer customer = new Customer(customerID, customerName, orgUnit);
		addCustomer(customer);
		gotoBackToCustomerList();
		setAddCustomerFilter(customer.getCustomerName());
		gotoAddCustomerFilter();
		gotoCustomer(customer);
		gotoEditCustomerFromViewCustomer();
		gotoBackToCustomerListFromEditCustomer();
		gotoEditCustomer(customer);
		customer.setContactName("Dev");
		customer.setOrgUnit(orgUnit);
		customer.setContactEmail("dev@n4systems.com");
		customer.setStreetAddress("179 John St.");
		customer.setCity("Toronto");
		customer.setState("ON");
		customer.setZip("M5T 1X4");
		customer.setCountry("Canada");
		customer.setPhone1("(416) 599-6464");
		customer.setPhone2("(416) 599-6466");
		customer.setFax("(416) 599-6463");
		editCustomer(customer);
		gotoBackToCustomerList();
		@SuppressWarnings("unused")
		List<String> customers = getCustomerNames();
		gotoBackToAdministration();
		gotoManageCustomers();
		@SuppressWarnings("unused")
		List<String> customerIDs = getCustomerIDs();
		setAddCustomerFilter(customer.getCustomerName());
		gotoAddCustomerFilter();
		assertTrue(isCustomer(customer));
		deleteCustomer(customer.getCustomerName());
		@SuppressWarnings("unused")
		int n = getNumberOfCustomers();
//		addCustomerUser
//		addCustomerDivision
		
//		getRandomCustomer

//		editCustomerUser
//		getCustomerUsers
//		getRandomCustomerUser
//		isCustomerUser
//		deleteCustomerUser

//		gotoCustomerDivision
//		updateCustomerDivision
//		getCustomerDivisions
//		getRandomCustomerDivision
//		isCustomerDivision
//		deleteCustomerDivision
	}
	
	public void gotoAddCustomerUser() throws Exception {
		Link add = ie.link(addCustomerUserLinkFinder);
		assertTrue("Could not find the link to add a customer user", add.exists());
		add.click();
		checkManageUsersPageContentHeader();
	}
	
	private void checkManageUsersPageContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(manageUsersPageContentHeaderFinder);
		assertTrue("Could not find the title to Manage Users page", header.exists());
	}

	/**
	 * Assumes you are on the Manage Customer page.
	 * 
	 * @return
	 */
	public int getNumberOfCustomers() throws Exception {
		int n = 0;
		int fullPages = 0;
		final int customersPerPage = 20;
		
		// go to the last page if it exists
		Link last = ie.link(xpath("//DIV[@id='pageContent']/DIV[@class='pagination']/A[contains(text(),'Last')]"));
		if(last.exists()) {
			last.click();
		}

		// count the number of full pages of customers
		Span s = ie.span(xpath("//DIV[@id='pageContent']/DIV[@class='pagination']/SPAN[@class='currentPage']"));
		if(s.exists()) {
			Link l = s.link(0);
			int lastPage = Integer.parseInt(l.text().trim());
			fullPages = lastPage - 1;
			n = fullPages * customersPerPage;
		}
		
		// count the number of customers on the last page
		Links customersOnLastPage = ie.links(text("/Remove/"));
		if(customersOnLastPage != null && customersOnLastPage.length() > 0) {
			 n += customersOnLastPage.length();
		}
		
		return n;
	}

	public void deleteCustomer(String customerName) throws Exception {
		assertNotNull(customerName);
		Link remove = ie.link(xpath("//A[contains(text(),'" + customerName + "')]/../../TD[2]/A[contains(text(),'Remove')]"));
		assertTrue("Could not find the Remove link for customer '" + customerName + "'", remove.exists());
		misc.createThreadToCloseAreYouSureDialog();
		remove.click();
	}

	public void gotoBackToAdministration() throws Exception {
		Link l = ie.link(backToAdministrationLinkFinder);
		assertTrue("Could not find the link 'back to administration'", l.exists());
		l.click();
		Admin admin = new Admin(ie);
		admin.checkAdminPageContentHeader();
	}

	public List<String> getCustomerNames() throws Exception {
		List<String> results = new ArrayList<String>();
		FieldIDMisc.stopMonitor();
		boolean loopFlag = true;
		do {
			List<String> tmp = getCustomerNamesFromCurrentPage();
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

	private List<String> getCustomerNamesFromCurrentPage() throws Exception {
		List<String> results = new ArrayList<String>();
		
		Links customers = ie.links(customerNameAndIDLinkFinder);
		assertNotNull("Could not find any links to customers", customers);
		Iterator<Link> i = customers.iterator();
		while(i.hasNext()) {
			Link customer = i.next();
			String s = customer.text();
			int j = s.indexOf("- (");
			String c = s.substring(0, j).trim();
			results.add(c);
		}

		return results;
	}

	public List<String> getCustomerIDs() throws Exception {
		List<String> results = new ArrayList<String>();
		
		FieldIDMisc.stopMonitor();
		boolean loopFlag = true;
		do {
			List<String> tmp = getCustomerIDsFromCurrentPage();
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

	private List<String> getCustomerIDsFromCurrentPage() throws Exception {
		List<String> results = new ArrayList<String>();
		
		Links customers = ie.links(customerNameAndIDLinkFinder);
		assertNotNull("Could not find any links to customers", customers);
		Iterator<Link> i = customers.iterator();
		while(i.hasNext()) {
			Link customer = i.next();
			String s = customer.text().trim();
			int j = s.indexOf("(") + 1;
			int k = s.length() - 1;
			String c = s.substring(j, k).trim();
			results.add(c);
		}

		return results;
	}

	public void gotoBackToCustomerListFromEditCustomer() throws Exception {
		Link l = ie.link(backToCustomerListFromEditLinkFinder);
		assertTrue("Could not find the 'Back to customer list' link", l.exists());
		l.click();
		this.checkManageCustomersPageContentHeader();
	}

	public void gotoEditCustomerFromViewCustomer() throws Exception {
		Link edit = ie.link(editCustomerFromViewCustomerFinder);
		assertTrue("Could not find the Edit link", edit.exists());
		edit.click();
	}

	public void gotoBackToCustomerList() throws Exception {
		Link l = ie.link(backToCustomerListLinkFinder);
		assertTrue("Could not find the 'Back to customer list' link", l.exists());
		l.click();
		this.checkManageCustomersPageContentHeader();
	}

	public void addCustomerUser(CustomerUser u) throws Exception {
		assertNotNull(u);
		TextField userID = ie.textField(addCustomerUserUserIDFinder);
		assertTrue("Could not find the User ID field", userID.exists());
		userID.set(u.getUserID());
		TextField email = ie.textField(addCustomerUserEmailFinder);
		assertTrue("Could not find the Email Address field", email.exists());
		email.set(u.getEmail());
		TextField firstName = ie.textField(addCustomerUserFirstNameFinder);
		assertTrue("Could not find the First Name field", firstName.exists());
		firstName.set(u.getFirstName());
		TextField lastName = ie.textField(addCustomerUserLastNameFinder);
		assertTrue("Could not find the Last Name field", lastName.exists());
		lastName.set(u.getLastName());
		TextField position = ie.textField(addCustomerUserPositionFinder);
		assertTrue("Could not find the Position field", position.exists());
		if(u.getPosition() != null) {
			position.set(u.getPosition());
		}
		TextField initials = ie.textField(addCustomerUserInitialsFinder);
		assertTrue("Could not find the Initials field", initials.exists());
		if(u.getInitials() != null) {
			initials.set(u.getInitials());
		}
		TextField rfid = ie.textField(addCustomerUserSecurityRFIDFinder);
		assertTrue("Could not find the Security Rfid Number field", rfid.exists());
		if(u.getSecurityRFIDNumber() != null) {
			rfid.set(u.getSecurityRFIDNumber());
		}
		SelectList country = ie.selectList(addCustomerUserCountryFinder);
		assertTrue("Could not find the Country field", country.exists());
		String c = u.getCountry();
		if(c != null) {
			Option o = country.option(text(c));
			assertTrue("Could not find the Country '" + c + "' in the list of countries", o.exists());
			o.select();
			misc.waitForJavascript();
		}
		SelectList timeZone = ie.selectList(addCustomerUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone field", timeZone.exists());
		String tz = u.getTimeZone();
		if(tz != null) {
			Option o = timeZone.option(text("/" + tz + "/"));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}
		SelectList division = ie.selectList(addCustomerUserDivisionFinder);
		assertTrue("Could not find the Division field", timeZone.exists());
		String d = u.getDivision();
		if(d != null) {
			Option o = division.option(text("/" + d + "/"));
			assertTrue("Could not find the division '" + d + "'", o.exists());
			o.select();
		}
		TextField password = ie.textField(addCustomerUserPasswordFinder);
		assertTrue("Could not find the Password field", password.exists());
		password.set(u.getPassword());
		TextField verify = ie.textField(addCustomerUserVerifyPasswordFinder);
		assertTrue("Could not find the Verify Password field", verify.exists());
		verify.set(u.getPassword());
		
		List<String> p = u.getPermissions();
		Table permissions = ie.table(addCustomerUserPermissionsTableFinder);
		assertTrue("Could not find the table with permission", permissions.exists());

		if(p.contains(CustomerUser.create)) {
			Radio r = permissions.radio(xpath("TBODY/TR/TD[contains(text(),'" + CustomerUser.create + "')]/../TD[2]/INPUT[@value='true']"));
			r.set();
		}
		
		if(p.contains(CustomerUser.edit)) {
			Radio r = permissions.radio(xpath("TBODY/TR/TD[contains(text(),'" + CustomerUser.edit + "')]/../TD[2]/INPUT[@value='true']"));
			r.set();
		}

		Button save = ie.button(addCustomerUserSaveButtonFinder);
		assertTrue("Could not find the Submit button", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageCustomerPageContentHeader();
	}

	private void checkManageUserPageContentHeader(String userID) throws Exception {
		HtmlElement header = ie.htmlElement(manageUserPageContentHeaderFinder);
		assertTrue("Could not find the title to Manage User page", header.exists());
		assertTrue("Could not find the User ID in the header", header.text().contains(userID));
	}
	
	public void gotoAddCustomerDivision() throws Exception {
		Link add = ie.link(manageCustomerAddDivisionFinder);
		assertTrue("Could not find the link to add a division", add.exists());
		add.click();
		checkManageCustomerPageContentHeader();
	}
	
	public void setCustomerDivision(CustomerDivision d) throws Exception {
		assertNotNull(d);
		assertNotNull("Must supply a division ID", d.getDivisionID());
		assertNotNull("Must supply a division Name", d.getDivisionName());
		TextField divisionID = ie.textField(manageCustomerAddDivisionIDFinder);
		assertTrue("Could not find the Division ID text field", divisionID.exists());
		TextField divisionName = ie.textField(manageCustomerAddDivisionNameFinder);
		assertTrue("Could not find the Division Name text field", divisionName.exists());
		TextField contactName = ie.textField(manageCustomerAddDivisionContactNameFinder);
		assertTrue("Could not find the Contact Name text field", contactName.exists());
		TextField contactEmail = ie.textField(manageCustomerAddDivisionContactEmailFinder);
		assertTrue("Could not find the Contact Email text field", contactEmail.exists());
		TextField streetAddress = ie.textField(manageCustomerAddDivisionStreetAddressFinder);
		assertTrue("Could not find the Street Address text field", streetAddress.exists());
		TextField city = ie.textField(manageCustomerAddDivisionCityFinder);
		assertTrue("Could not find the City text field", city.exists());
		TextField state = ie.textField(manageCustomerAddDivisionStateFinder);
		assertTrue("Could not find the State text field", state.exists());
		TextField zip = ie.textField(manageCustomerAddDivisionZipFinder);
		assertTrue("Could not find the Zip text field", zip.exists());
		TextField country = ie.textField(manageCustomerAddDivisionCountryFinder);
		assertTrue("Could not find the Country text field", country.exists());
		TextField phone1 = ie.textField(manageCustomerAddDivisionPhone1Finder);
		assertTrue("Could not find the Phone 1 text field", phone1.exists());
		TextField phone2 = ie.textField(manageCustomerAddDivisionPhone2Finder);
		assertTrue("Could not find the Phone 2 text field", phone2.exists());
		TextField fax = ie.textField(manageCustomerAddDivisionFaxFinder);
		assertTrue("Could not find the Fax text field", fax.exists());
		
		divisionID.set(d.getDivisionID());
		divisionName.set(d.getDivisionName());
		if(d.getContactName() != null) {
			contactName.set(d.getContactName());
		}
		if(d.getContactEmail() != null) {
			contactEmail.set(d.getContactEmail());
		}
		if(d.getStreetAddress() != null) {
			streetAddress.set(d.getStreetAddress());
		}
		if(d.getCity() != null) {
			city.set(d.getCity());
		}
		if(d.getState() != null) {
			state.set(d.getState());
		}
		if(d.getZip() != null) {
			zip.set(d.getZip());
		}
		if(d.getCountry() != null) {
			country.set(d.getCountry());
		}
		if(d.getPhone1() != null) {
			phone1.set(d.getPhone1());
		}
		if(d.getPhone2() != null) {
			phone2.set(d.getPhone2());
		}
		if(d.getFax() != null) {
			fax.set(d.getFax());
		}
	}

	/**
	 * @deprecated
	 * @param d
	 * @throws Exception
	 */
	public void addCustomerDivision(String d) throws Exception {
		TextField division = ie.textField(manageCustomerDivisionFinder);
		assertTrue("Could not find the text field to add a division", division.exists());
		division.set(d);
		Button submit = ie.button(manageCustomerDivisionSubmitButtonFinder);
		assertTrue("Could not find the Submit button for adding a division", submit.exists());
		submit.click();
		checkManageCustomerPageContentHeader();
	}

	public void gotoCustomerDivisions() throws Exception {
		Link d = ie.link(customerDivisionsLinkFinder);
		assertTrue("Could not find the link to customer divisions", d.exists());
		d.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageCustomerPageContentHeader();
	}

	public void gotoCustomerUsers() throws Exception {
		Link u = ie.link(customerUsersLinkFinder);
		assertTrue("Could not find the link to customer users", u.exists());
		u.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageCustomerPageContentHeader();
	}

	public String getAddDivisionMessage() throws Exception {
		String result = "";
		Span error = ie.span(addDivisionErrorMessagesFinder);
		assertNotNull("could not find the message for adding a division", error.exists());
		result = error.text();
		return result;
	}

	public String getUpdateDivisionMessage() throws Exception {
		String result = "";
		Divs errors = ie.divs(updateDivisionErrorMessagesFinder);
		assertNotNull(errors);
		Iterator<Div> i = errors.iterator();
		while(i.hasNext()) {
			Div error = i.next();
			result += error.text();
			result += "\n";
		}
		return result;
	}

	public void checkCustomerDivisionAddSuccessful() throws Exception {
		String result = getAddDivisionMessage();
		assertTrue("Adding a division failed. Error: '" + result + "'", result.contains("successfully added"));
	}

	public void gotoEditCustomerUser(String userID) throws Exception {
		Link user = getLinkToCustomerUser(userID);
		assertTrue("Could not find the link to edit user '" + userID + "'", user.exists());
		user.click();
		checkManageUserPageContentHeader(userID);
	}

	public void editCustomerUser(CustomerUser u) throws Exception {
		assertNotNull(u);
		FieldIDMisc.stopMonitor();
		TextField userID = ie.textField(editCustomerUserUserIDFinder);
		assertTrue("Could not find the User ID field", userID.exists());
		userID.set(u.getUserID());
		TextField email = ie.textField(editCustomerUserEmailFinder);
		assertTrue("Could not find the Email Address field", email.exists());
		email.set(u.getEmail());
		TextField firstName = ie.textField(editCustomerUserFirstNameFinder);
		assertTrue("Could not find the First Name field", firstName.exists());
		firstName.set(u.getFirstName());
		TextField lastName = ie.textField(editCustomerUserLastNameFinder);
		assertTrue("Could not find the Last Name field", lastName.exists());
		lastName.set(u.getLastName());
		TextField position = ie.textField(editCustomerUserPositionFinder);
		assertTrue("Could not find the Position field", position.exists());
		if(u.getPosition() != null) {
			position.set(u.getPosition());
		}
		TextField initials = ie.textField(editCustomerUserInitialsFinder);
		assertTrue("Could not find the Initials field", initials.exists());
		if(u.getInitials() != null) {
			initials.set(u.getInitials());
		}
//		SelectList country = ie.selectList(editCustomerUserCountryFinder);
//		assertTrue("Could not find the Country field", country.exists());
//		String c = u.getCountry();
//		if(c != null) {
//			Option o = country.option(text(c));
//			assertTrue("Could not find the Country '" + c + "' in the list of countries", o.exists());
//			o.select();
//			misc.waitForJavascript();
//		}
		SelectList timeZone = ie.selectList(editCustomerUserTimeZoneFinder);
		assertTrue("Could not find the Time Zone field", timeZone.exists());
		String tz = u.getTimeZone();
		if(tz != null) {
			Option o = timeZone.option(text("/" + tz + "/"));
			assertTrue("Could not find the time zone '" + tz + "'", o.exists());
			o.select();
		}
		SelectList division = ie.selectList(editCustomerUserDivisionFinder);
		assertTrue("Could not find the Division field", timeZone.exists());
		String d = u.getDivision();
		if(d != null) {
			Option o = division.option(text("/" + d + "/"));
			assertTrue("Could not find the division '" + d + "'", o.exists());
			o.select();
		}
		
		List<String> p = u.getPermissions();
		Table permissions = ie.table(editCustomerUserPermissionsTableFinder);
		assertTrue("Could not find the table with permission", permissions.exists());

		if(p.contains(CustomerUser.create)) {
			Radio r = permissions.radio(xpath("TBODY/TR/TD[contains(text(),'" + CustomerUser.create + "')]/../TD[2]/INPUT[@value='true']"));
			r.set();
		} else {
			Radio r = permissions.radio(xpath("TBODY/TR/TD[contains(text(),'" + CustomerUser.create + "')]/../TD[3]/INPUT[@value='false']"));
			r.set();
		}
		
		if(p.contains(CustomerUser.edit)) {
			Radio r = permissions.radio(xpath("TBODY/TR/TD[contains(text(),'" + CustomerUser.edit + "')]/../TD[2]/INPUT[@value='true']"));
			r.set();
		} else {
			Radio r = permissions.radio(xpath("TBODY/TR/TD[contains(text(),'" + CustomerUser.edit + "')]/../TD[3]/INPUT[@value='false']"));
			r.set();
		}

		saveEditCustomerUser();
		checkManageCustomerPageContentHeader();
		FieldIDMisc.startMonitor();
	}

	public void editCustomerUserAllPermissionsOn() throws Exception {
		Button allOn = ie.button(manageCustomerEditUserPermissionsAllOnButtonFinder);
		assertTrue("Could not find the button to turn on all permissions", allOn.exists());
		allOn.click();
		// TODO: check that the permissions are all on
	}

	public void editCustomerUserAllPermissionsOff() throws Exception {
		Button allOff = ie.button(manageCustomerEditUserPermissionsAllOffButtonFinder);
		assertTrue("Could not find the button to turn off all permissions", allOff.exists());
		allOff.click();
		// TODO: check that the permissions are all off
	}
	
	public void saveEditCustomerUser() throws Exception {
		Button save = ie.button(editCustomerUserSaveButtonFinder);
		assertTrue("Could not find the Submit button", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	private Link getLinkToCustomerUser(String userID) throws Exception {
		Table users = ie.table(manageCustomerUsersTableFinder);
		assertTrue("Could not find the table of users for a Customer", users.exists());
		Link user = users.link(xpath("TBODY/TR/TD[1]/A[contains(text(),'" + userID + "')]"));
		return user;
	}

	public boolean isCustomerUser(String userID) throws Exception {
		boolean result = false;
		Link user = getLinkToCustomerUser(userID);
		result = user.exists();
		return result;
	}
	
	/**
	 * @deprecated
	 * @return
	 * @throws Exception
	 */
	private Div getDivisionContainer() throws Exception {
		Div d = ie.div(divisionContainerFinder);
		assertTrue("Could not find the division container", d.exists());
		return d;
	}
	
	/**
	 * @deprecated
	 * @param oldDivision
	 * @param newDivision
	 * @throws Exception
	 */
	public void editCustomerDivision(String oldDivision, String newDivision) throws Exception {
		Div d = this.getDivisionContainer();
		TextField division = d.textField(xpath("//INPUT[@value='" + oldDivision + "']"));
		assertTrue("Could not find the division '" + oldDivision + "'", division.exists());
		division.set(newDivision);
		String update_id = division.id().replace("division", "updateDiv");
		Link update = d.link(id(update_id));
		assertTrue("Could not find the update link", update.exists());
		update.click();
		String result = getUpdateDivisionMessage();
		assertTrue("Update failed: '" + result + "'", result.contains("successfully updated"));
	}

	public boolean isCustomerDivision(String divisionName) throws Exception {
		// TODO support more than one page of divisions
		TableCell div = ie.cell(xpath("//TD[contains(text(),'" + divisionName + "')]"));
		return div.exists();
	}

	public void addCustomerDivision() throws Exception {
		Button save = ie.button(manageCustomerAddDivisionSaveFinder);
		assertTrue("Could not find the Save button on Add Division", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageCustomerPageContentHeader();
	}

	public boolean isUser(String userID) throws Exception {
		Link l = ie.link(xpath("//TABLE[@id='userList']/TBODY/TR/TD[1]/A[contains(text(),'" + userID + "')]"));
		return l.exists();
	}

	public void gotoAddUser() throws Exception {
		Link l = ie.link(manageCustomerAddUserLinkFinder);
		assertTrue("Could not find the link to Add User", l.exists());
		l.click();
		checkManageCustomerPageContentHeader();
	}

	public void gotoUsers() throws Exception {
		Link l = ie.link(manageCustomerUsersLinkFinder);
		assertTrue("Could not find the link to this customer's Users", l.exists());
		l.click();
		checkManageCustomerPageContentHeader();
	}

	public void gotoEditUser(String userID) throws Exception {
		Link l = ie.link(xpath("//TABLE[@id='userList']/TBODY/TR/TD[1]/A[contains(text(),'" + userID + "')]"));
		assertTrue("Could not find the link to user '" + userID + "'", l.exists());
		l.click();
		checkManageCustomerPageContentHeader();
	}
}
