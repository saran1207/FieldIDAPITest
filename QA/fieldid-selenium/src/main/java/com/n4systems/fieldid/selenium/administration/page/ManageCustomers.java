package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.n4systems.fieldid.selenium.datatypes.Customer;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

/**
 * Note: For Manage Job Sites, it is really Manage Customers with a label
 * change. So this class can be used for tenants with Job Sites as well as
 * tenants with Customers and Divisions.
 * 
 * @author dgrainge
 *
 */
public class ManageCustomers {
	FieldIdSelenium selenium;
	Misc misc;
	private String manageCustomersPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Customers')]";
	private String filterByNameTextFieldLocator = "xpath=//INPUT[@id='listFilter']";
	private String filterButtonLocator = "xpath=//INPUT[@value='Filter']";
	private String numberOfRowsOnCustomerTableXpath = "//DIV[@id='pageContent']/TABLE[@class='list']/TBODY/TR[position() > 1]/TD[2]";
	private String customerTableLocator = "xpath=//DIV[@id='pageContent']/TABLE[@class='list']";
	private String addCustomerLinkLocator = "xpath=//LI[contains(@class,'add')]/A[contains(text(),'Add')]";
	private String addCustomerCustomerIDTextFieldLocator = "xpath=//INPUT[@id='customerEdit_customerId']";
	private String addCustomerCustomerNameTextFieldLocator = "xpath=//INPUT[@id='customerEdit_customerName']";
	private String addCustomerOrgUnitSelectListLocator = "xpath=//SELECT[@id='customerEdit_parentOrgId']";
	private String addCustomerContactNameTextFieldLocator = "xpath=//INPUT[@id='customerEdit_contactName']";
	private String addCustomerContactEmailTextFieldLocator = "xpath=//INPUT[@id='customerEdit_accountManagerEmail']";
	private String addCustomerStreetAddressTextFieldLocator = "xpath=//INPUT[@id='customerEdit_accountManagerEmail']";
	private String addCustomerCityTextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_city']";
	private String addCustomerStateProvinceTextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_state']";
	private String addCustomerZipTextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_state']";
	private String addCustomerCountryTextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_state']";
	private String addCustomerPhone1TextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_phone1']";
	private String addCustomerPhone2TextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_phone2']";
	private String addCustomerFaxTextFieldLocator = "xpath=//INPUT[@id='customerEdit_addressInfo_fax1']";
	private String addCustomerSaveButtonLocator = "xpath=//INPUT[@id='customerEdit_label_save']";
	private String addCustomerCancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	private String divisionLinkLocator = "xpath=//A[contains(text(),'Divisions')]";
	private String addDivisionLinkLocator = "xpath=//A[contains(text(),'Add Division')]";
	
	public ManageCustomers(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	/**
	 * Verifies there are no error messages on the current page and that the
	 * current page has the header 'Manage Customers'.
	 */
	public void verifyManageCustomersPage() {
		misc.info("Verify going to Manage Customers page went okay.");
		misc.checkForErrorMessages("verifyManageCustomersPage");
		if(!selenium.isElementPresent(manageCustomersPageHeaderLocator )) {
			fail("Could not find the header for 'Manage Customers'.");
		}
	}
	
	/**
	 * Enters the given string into the Filter by Name text field on the
	 * Manage Customers page. It does not click the Filter button. If this
	 * string contains a newline character, it would be the same as the user
	 * pressing ENTER.
	 * 
	 * @param s
	 */
	public void setFilterByName(String s) {
		misc.info("Enter the string '" + s + "' into the Filter by Name field.");
		if(selenium.isElementPresent(filterByNameTextFieldLocator)) {
			selenium.type(filterByNameTextFieldLocator, s);
		} else {
			fail("Could not locate the Filter by Name text field.");
		}
	}
	
	/**
	 * Clicks the Filter button and checks to see if we received an Oops
	 * page. If you are expecting anything else, you need to use other
	 * methods to check for them.
	 */
	public void gotoFilterByName() {
		misc.info("Click the Filter button.");
		if(selenium.isElementPresent(filterButtonLocator)) {
			selenium.click(filterButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not locate the Filter button.");
		}
	}
	
	/**
	 * Gets a list of all the Customer IDs from the Manage Customers page.
	 * It assumes you are on the Manage Customers page and that it should
	 * start getting the IDs from the current page. If you want a list of
	 * all the Customer IDs you want to be on page 1 when you call this.
	 * 
	 * @return list of Customer IDs
	 */
	public List<String> getCustomerIDs() {
		misc.info("Create a list of all the Customer IDs");
		List<String> result = new ArrayList<String>();
		
		boolean loopFlag = true;
		do {
			List<String> tmp = getCustomerIDsFromCurrentPage();
			result.addAll(tmp);
			loopFlag = misc.gotoNextPage();
		} while (loopFlag);

		return result;
	}

	/**
	 * Get a list of the Customer IDs from the current page on the Manage
	 * Customers screen. If there are no customer IDs to be found it will
	 * return an empty list. It does not do any error checking. If you are
	 * not on the Manage Customer page it will return an empty list.
	 * We cannot return an error if the table with Customer IDs is not
	 * present. If there are actually no customers, the table will not be
	 * present. So finding no customers on the Manage Customer page is a
	 * valid possibility.
	 * 
	 * NOTE: we do not trim the fields. A leading space is a valid value
	 * for a CustomerID. We must therefore NOT trim whitespace.
	 * 
	 * @return list of Customer IDs
	 */
	public List<String> getCustomerIDsFromCurrentPage() {
		List<String> result = new ArrayList<String>();
		int numCustomers = -1;
		Number n = selenium.getXpathCount(numberOfRowsOnCustomerTableXpath);
		numCustomers = n.intValue();
		for(int i = 0; i < numCustomers; i++) {
			int row = i + 1;	// first row is the column headers so start at + 1
			String cellLocator = customerTableLocator + "." + row + ".1";
			String customerID = selenium.getTable(cellLocator);
			result.add(customerID);
		}
		
		return result;
	}

	/**
	 * Gets a list of all the Customer Names from the Manage Customers page.
	 * It assumes you are on the Manage Customers page and that it should
	 * start getting the names from the current page. If you want a list of
	 * all the Customer Names you want to be on page 1 when you call this.
	 * 
	 * @return list of Customer Names
	 */
	public List<String> getCustomerNames() {
		misc.info("Create a list of all the Customer Names");
		List<String> result = new ArrayList<String>();
		
		boolean loopFlag = true;
		do {
			List<String> tmp = getCustomerNamesFromCurrentPage();
			result.addAll(tmp);
			loopFlag = misc.gotoNextPage();
		} while (loopFlag);

		return result;
	}

	/**
	 * Get a list of the Customer Names from the current page on the Manage
	 * Customers screen. If there are no customer names to be found it will
	 * return an empty list. It does not do any error checking. If you are
	 * not on the Manage Customer page it will return an empty list.
	 * We cannot return an error if the table with Customer names is not
	 * present. If there are actually no customers, the table will not be
	 * present. So finding no customers on the Manage Customer page is a
	 * valid possibility.
	 * 
	 * @return list of Customer Names
	 */
	public List<String> getCustomerNamesFromCurrentPage() {
		List<String> result = new ArrayList<String>();
		int numCustomers = -1;
		Number n = selenium.getXpathCount(numberOfRowsOnCustomerTableXpath);
		numCustomers = n.intValue();
		for(int i = 0; i < numCustomers; i++) {
			int row = i + 1;	// first row is the column headers so start at + 1
			String cellLocator = customerTableLocator + "." + row + ".0";
			String customerName = selenium.getTable(cellLocator);
			result.add(customerName.trim());
		}
		
		return result;
	}

	public Map<String, String> getCustomerIDAndOrganizationMap() {
		misc.info("Create a map of all the Customer ID, Organizations");
		Map<String, String> map = new TreeMap<String, String>();
		
		boolean loopFlag = true;
		do {
			Map<String, String> tmp = getCustomerIDAndOrganizationMapFromCurrentPage();
			map.putAll(tmp);
			loopFlag = misc.gotoNextPage();
		} while (loopFlag);

		return map;
	}

	public Map<String, String> getCustomerIDAndOrganizationMapFromCurrentPage() {
		Map<String, String> result = new TreeMap<String, String>();

		int numCustomers = -1;
		Number n = selenium.getXpathCount(numberOfRowsOnCustomerTableXpath);
		numCustomers = n.intValue();
		for(int i = 0; i < numCustomers; i++) {
			int row = i + 1;	// first row is the column headers so start at + 1
			String customerIDCellLocator = customerTableLocator + "." + row + ".1";
			String customerID = selenium.getTable(customerIDCellLocator);
			String organizationCellLocator = customerTableLocator + "." + row + ".2";
			String organization = selenium.getTable(organizationCellLocator);
			result.put(customerID, organization);
		}
		
		return result;
	}

	public void gotoAddCustomer() {
		misc.info("click Add to add a new customer");
		if(selenium.isElementPresent(addCustomerLinkLocator)) {
			selenium.click(addCustomerLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
			verifyAddCustomerPage();
		} else {
			fail("Could not find the link to add a customer");
		}
	}

	private void verifyAddCustomerPage() {
		assertTrue("Could not find the Customer ID text field", selenium.isElementPresent(addCustomerCustomerIDTextFieldLocator));
		assertTrue("Could not find the Customer Name text field", selenium.isElementPresent(addCustomerCustomerNameTextFieldLocator));
		assertTrue("Could not find the Organizational Unit select list", selenium.isElementPresent(addCustomerOrgUnitSelectListLocator));
		assertTrue("Could not find the Contact Name text field", selenium.isElementPresent(addCustomerContactNameTextFieldLocator));
		assertTrue("Could not find the Contact Email text field", selenium.isElementPresent(addCustomerContactEmailTextFieldLocator));
		assertTrue("Could not find the Street Address text field", selenium.isElementPresent(addCustomerStreetAddressTextFieldLocator));
		assertTrue("Could not find the City text field", selenium.isElementPresent(addCustomerCityTextFieldLocator));
		assertTrue("Could not find the State/Province text field", selenium.isElementPresent(addCustomerStateProvinceTextFieldLocator));
		assertTrue("Could not find the Zip text field", selenium.isElementPresent(addCustomerZipTextFieldLocator));
		assertTrue("Could not find the Country text field", selenium.isElementPresent(addCustomerCountryTextFieldLocator));
		assertTrue("Could not find the Phone 1 text field", selenium.isElementPresent(addCustomerPhone1TextFieldLocator));
		assertTrue("Could not find the Phone 2 text field", selenium.isElementPresent(addCustomerPhone2TextFieldLocator));
		assertTrue("Could not find the Fax text field", selenium.isElementPresent(addCustomerFaxTextFieldLocator));
		assertTrue("Could not find the Save button", selenium.isElementPresent(addCustomerSaveButtonLocator));
		assertTrue("Could not find the Cancel link", selenium.isElementPresent(addCustomerCancelLinkLocator));
	}

	public void setAddCustomer(Customer c) {
		verifyAddCustomerPage();
		if(c.getCustomerID() != null)	selenium.type(addCustomerCustomerIDTextFieldLocator, c.getCustomerID());
		if(c.getCustomerName() != null)	selenium.type(addCustomerCustomerNameTextFieldLocator, c.getCustomerName());
		if(c.getOrganizationalUnit() != null)	selenium.type(addCustomerOrgUnitSelectListLocator, c.getOrganizationalUnit());
		if(c.getContactName() != null)	selenium.type(addCustomerContactNameTextFieldLocator, c.getContactName());
		if(c.getContactEmail() != null)	selenium.type(addCustomerContactEmailTextFieldLocator, c.getContactEmail());
		if(c.getStreetAddress() != null)	selenium.type(addCustomerStreetAddressTextFieldLocator, c.getStreetAddress());
		if(c.getCity() != null)	selenium.type(addCustomerCityTextFieldLocator, c.getCity());
		if(c.getState() != null)	selenium.type(addCustomerStateProvinceTextFieldLocator, c.getState());
		if(c.getZipCode() != null)	selenium.type(addCustomerZipTextFieldLocator, c.getZipCode());
		if(c.getCountry() != null)	selenium.type(addCustomerCountryTextFieldLocator, c.getCountry());
		if(c.getPhone1() != null)	selenium.type(addCustomerPhone1TextFieldLocator, c.getPhone1());
		if(c.getPhone2() != null)	selenium.type(addCustomerPhone2TextFieldLocator, c.getPhone2());
		if(c.getFax() != null)	selenium.type(addCustomerFaxTextFieldLocator, c.getFax());
	}

	public void gotoSaveCustomer() {
		misc.info("Click the Save button");
		if(selenium.isElementPresent(addCustomerSaveButtonLocator)) {
			selenium.click(addCustomerSaveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button");
		}
	}

	public void gotoDivisions() {
		misc.info("Click the link to Divisions");
		if(selenium.isElementPresent(divisionLinkLocator)) {
			selenium.click(divisionLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Divisions");
		}
	}

	public void gotoAddDivision() {
		misc.info("Click the link to add a division");
		if(selenium.isElementPresent(addDivisionLinkLocator)) {
			selenium.click(addDivisionLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to add a division");
		}
	}
}
