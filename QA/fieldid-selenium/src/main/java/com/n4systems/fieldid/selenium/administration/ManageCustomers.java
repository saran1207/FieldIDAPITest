package com.n4systems.fieldid.selenium.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

/**
 * Note: For Manage Job Sites, it is really Manage Customers with a label
 * change. So this class can be used for tenants with Job Sites as well as
 * tenants with Customers and Divisions.
 * 
 * @author dgrainge
 *
 */
public class ManageCustomers extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageCustomersPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Customers')]";
	private String filterByNameTextFieldLocator = "xpath=//INPUT[@id='listFilter']";
	private String filterButtonLocator = "xpath=//INPUT[@value='Filter']";
	private String numberOfRowsOnCustomerTableXpath = "//DIV[@id='pageContent']/TABLE[@class='list']/TBODY/TR[position() > 1]/TD[2]";
	private String customerTableLocator = "xpath=//DIV[@id='pageContent']/TABLE[@class='list']";
	
	public ManageCustomers(Selenium selenium, Misc misc) {
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
}
