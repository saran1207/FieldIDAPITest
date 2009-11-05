package com.n4systems.fieldid.selenium.admin.console;

import java.util.ArrayList;
import java.util.List;
import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.Selenium;

public class AdminConsoleOrganizationsPage extends FieldIDTestCase {

	private Selenium selenium;
	
	// locators for the elements on the page I want to interact with
	private static final String organizationsTableLocator = "//div[@id='content']/table";	// must be xpath
	private static final String serialNumberFormatInputLocator = "css=#organizationUpdate_primaryOrg_serialNumberFormat";
	private static final String dateFormatInputLocator = "css=#organizationUpdate_primaryOrg_dateFormat";
	private static final String diskSpaceLimitInputLocator = "css=#organizationUpdate_diskSpace";
	private static final String assetLimitInputLocator = "css=#organizationUpdate_assets";
	private static final String userLimitInputLocator = "css=#organizationUpdate_users";
	private static final String secondaryOrgsLimitInputLocator = "css=#organizationUpdate_secondaryOrgs";
	private static final String integrationLocator = "css=#organizationUpdate_extendedFeatures_'Integration'_";
	private static final String jobsitesLocator = "css=#organizationUpdate_extendedFeatures_'JobSites'_";
	private static final String projectsLocator = "css=#organizationUpdate_extendedFeatures_'Projects'_";
	private static final String brandingLocator = "css=#organizationUpdate_extendedFeatures_'Branding'_";
	private static final String partnercenterLocator = "css=#organizationUpdate_extendedFeatures_'PartnerCenter'_";
	private static final String emailalertsLocator = "css=#organizationUpdate_extendedFeatures_'EmailAlerts'_";
	private static final String customcertLocator = "css=#organizationUpdate_extendedFeatures_'CustomCert'_";
	private static final String dedicatedprogrammanagerLocator = "css=#organizationUpdate_extendedFeatures_'DedicatedProgramManager'_";
	private static final String multilocationLocator = "css=#organizationUpdate_extendedFeatures_'MultiLocation'_";
	private static final String allowintegrationLocator = "css=#organizationUpdate_extendedFeatures_'AllowIntegration'_";
	private static final String unlimitedlinkedassetsLocator = "css=#organizationUpdate_extendedFeatures_'UnlimitedLinkedAssets'_";
	private static final String submitButtonLocator = "css=#organizationUpdate_0";
	private static final String cancelButtonLocator = "css=[value='Cancel']";
	private static final String serialNumberFormatOptionsLocator = "css=pre";	// assume there is only one <PRE> tag
	private static final String contextRoot = "/fieldidadmin/";
	private static final String usernameInputLocator = "css=#signIntoSystem_username";
	private static final String passwordInputLocator = "css=#signIntoSystem_password";
	private static final String loginSubmitButtonLocator = "css=#signIntoSystem_0";
	private static final String pageTitle = "Field ID Administration";

	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public AdminConsoleOrganizationsPage(Selenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	/**
	 * Checks to make sure we are getting the correct title on the page.
	 */
	private void assertPageTitle() {
		assertEquals(pageTitle, selenium.getTitle());
	}
	
	/**
	 * Clicks the Submit button on the Edit Tenant page.
	 */
	public void gotoSubmitEditTenant() {
		selenium.click(submitButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}

	/**
	 * Clicks the Cancel button on the Edit Tenant page.
	 */
	public void gotoCancelEditTenant() {
		selenium.click(cancelButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * text at the bottom of the page explaining the format options for
	 * the serial number format.
	 * 
	 * @see getTenantSerialNumberFormat
	 * @return serial number format options
	 */
	public String getSerialNumberFormatOptions() {
		String result;
		result = selenium.getText(serialNumberFormatOptionsLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature UnlimitedLinkedAssets.
	 * 
	 * @return true if UnlimitedLinkedAssets is enabled, otherwise false
	 */
	public boolean hasUnlimitedLinkedAssets() {
		boolean result = false;
		result = selenium.isChecked(unlimitedlinkedassetsLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature AllowIntegration.
	 * 
	 * @return true if AllowIntegration is enabled, otherwise false
	 */
	public boolean hasAllowIntegration() {
		boolean result = false;
		result = selenium.isChecked(allowintegrationLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature MultiLocation.
	 * 
	 * @return true if MultiLocation is enabled, otherwise false
	 */
	public boolean hasMultiLocation() {
		boolean result = false;
		result = selenium.isChecked(multilocationLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature DedicatedProgramManager.
	 * 
	 * @return true if DedicatedProgramManager is enabled, otherwise false
	 */
	public boolean hasDedicatedProgramManager() {
		boolean result = false;
		result = selenium.isChecked(dedicatedprogrammanagerLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature CustomCert.
	 * 
	 * @return true if CustomCert is enabled, otherwise false
	 */
	public boolean hasCustomCert() {
		boolean result = false;
		result = selenium.isChecked(customcertLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature EmailAlerts.
	 * 
	 * @return true if EmailAlerts is enabled, otherwise false
	 */
	public boolean hasEmailAlerts() {
		boolean result = false;
		result = selenium.isChecked(emailalertsLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature PartnerCenter.
	 * 
	 * @return true if PartnerCenter is enabled, otherwise false
	 */
	public boolean hasPartnerCenter() {
		boolean result = false;
		result = selenium.isChecked(partnercenterLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature Branding.
	 * 
	 * @return true if Branding is enabled, otherwise false
	 */
	public boolean hasBranding() {
		boolean result = false;
		result = selenium.isChecked(brandingLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature Projects.
	 * 
	 * @return true if Projects is enabled, otherwise false
	 */
	public boolean hasProjects() {
		boolean result = false;
		result = selenium.isChecked(projectsLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature JobSites.
	 * 
	 * @return true if JobSites is enabled, otherwise false
	 */
	public boolean hasJobSites() {
		boolean result = false;
		result = selenium.isChecked(jobsitesLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the extended feature Integration.
	 * 
	 * @return true if Integration is enabled, otherwise false
	 */
	public boolean hasIntegration() {
		boolean result = false;
		result = selenium.isChecked(integrationLocator);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the secondary organization limit.
	 * 
	 * @return secondary organization limit
	 */
	public long getTenantSecondaryOrgLimit() {
		long result;
		String s = selenium.getValue(secondaryOrgsLimitInputLocator);
		result = Long.parseLong(s);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the user limit.
	 * 
	 * @return user limit
	 */
	public long getTenantUserLimit() {
		long result;
		String s = selenium.getValue(userLimitInputLocator);
		result = Long.parseLong(s);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the asset limit.
	 * 
	 * @return asset limit
	 */
	public long getTenantAssetLimit() {
		long result;
		String s = selenium.getValue(assetLimitInputLocator);
		result = Long.parseLong(s);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * value for the disk space limit in bytes.
	 * 
	 * @return disk space limit in bytes
	 */
	public long getTenantDiskSpaceLimit() {
		long result;
		String s = selenium.getValue(diskSpaceLimitInputLocator);
		result = Long.parseLong(s);
		return result;
	}

	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * string for the date format.
	 * 
	 * @return date format string
	 */
	public String getTenantDateFormat() {
		String result;
		result = selenium.getValue(dateFormatInputLocator);
		return result;
	}

	/**
	 * Before calling this method you need to go to the
	 * Field ID Administration Console using the method
	 * gotoFieldIDAdministrationConsole().
	 * 
	 */
	public void loginToFieldIDAdministrationConsole(String username, String password) {
		selenium.type(usernameInputLocator, username);
		selenium.type(passwordInputLocator, password);
		selenium.click(loginSubmitButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}

	/**
	 * Go to the Field ID Administration Console.
	 * 
	 */
	public void gotoFieldIDAdministrationConsole() {
		selenium.open(contextRoot);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	/**
	 * Before calling this method you need to use the gotoEditTenant()
	 * method. Once on the Edit Tenant page, this method will return the
	 * string for the Serial Number format.
	 * 
	 * @return serial number format string
	 */
	public String getTenantSerialNumberFormat() {
		String result;
		result = selenium.getValue(serialNumberFormatInputLocator);
		return result;
	}
	
	/**
	 * Before calling this method, you must be logged into the
	 * Field ID Administration Console, i.e. context root of
	 * (/fieldidadmin/) and on the Organization tab. This method
	 * will find the row containing the tenant ID then click the
	 * Edit link for that tenant.
	 * 
	 * This method assumes the tenant IDs are in a table. It will
	 * search for a cell containing the tenant ID, look for a cell
	 * on the same row which contains a link with the text 'Edit'
	 * then click it. 
	 * 
	 * @param tenantID
	 */
	public void gotoEditTenant(String tenantID) {
		assertTrue("The tenantID must not be null", tenantID != null);
		String editTenantLocator = "//td[text()='" + tenantID +"']/../td/a[text()='Edit']";
		selenium.click(editTenantLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}

	/**
	 * Before calling this method, you must be logged into the
	 * Field ID Administration Console, i.e. context root of
	 * (/fieldidadmin/) and on the Organization tab. This method
	 * will scan the page for the first TABLE element then scan
	 * each row of the table for a string in the second column.
	 *  
	 * @return list of tenant IDs
	 */
	public List<String> getTenantIDs() {
		String tableLocator = organizationsTableLocator;
		String column = "1";	// columns are zero index
		List<String> result = new ArrayList<String>();
		Number rows = selenium.getXpathCount(tableLocator + "/tbody/tr");
		int totalRows = rows.intValue();
		for(int row = 0; row < totalRows; row++) {
			String tableCellAddress = tableLocator + "." + row + "." + column;
			String tenantID = selenium.getTable(tableCellAddress);
			result.add(tenantID);
		}
		return result;
	}

	/**
	 * Before calling this method, you must be logged into the
	 * Field ID Administration Console, i.e. context root of
	 * (/fieldidadmin/) and on the Organization tab. This method
	 * will look for the text preceding the tenant total. It then
	 * parses that value out of the text, converts it to an integer
	 * and returns the int.
	 * 
	 * @return total number of tenants
	 */
	public int getTotalTenants() {
		String totalTenantText = "Total Tenants: ";	// string preceding tenant total
		String s1 = selenium.getBodyText(); 
		assertTrue(s1 != null);
		int start = s1.indexOf(totalTenantText) + totalTenantText.length();
		assertTrue(start != -1);
		int end = s1.indexOf(" ", start+1);
		assertTrue(end != -1);
		assertTrue(start < end);
		String s2 = s1.substring(start, end);
		return Integer.parseInt(s2);
	}
}
