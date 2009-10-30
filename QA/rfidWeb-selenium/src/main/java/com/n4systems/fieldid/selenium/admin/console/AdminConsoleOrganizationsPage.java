package com.n4systems.fieldid.selenium.admin.console;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.selenium.DefaultSelenium;

public class AdminConsoleOrganizationsPage {

	private DefaultSelenium selenium;
	private static final String pageLoadDefaultTimeout = "30000";	// give a page 30 seconds to load
	
	// locators for the elements on the page I want to interact with
	private static final String serialNumberFormatInputLocator = "//input[@id='organizationUpdate_primaryOrg_serialNumberFormat']";
	private static final String dateFormatInputLocator = "//input[@id='organizationUpdate_primaryOrg_dateFormat']";
	private static final String diskSpaceLimitInputLocator = "//input[@id='organizationUpdate_diskSpace']";
	private static final String assetLimitInputLocator = "//input[@id='organizationUpdate_assets']";
	private static final String userLimitInputLocator = "//input[@id='organizationUpdate_users']";
	private static final String secondaryOrgsLimitInputLocator = "//input[@id='organizationUpdate_secondaryOrgs']";
	private static final String integrationLabelLocator = "//LABEL[text()='Integration']@for";
	private static final String jobsitesLabelLocator = "//LABEL[text()='JobSites']@for";
	private static final String projectsLabelLocator = "//LABEL[text()='Projects']@for";
	private static final String brandingLabelLocator = "//LABEL[text()='Branding']@for";
	private static final String partnercenterLabelLocator = "//LABEL[text()='PartnerCenter']@for";
	private static final String emailalertsLabelLocator = "//LABEL[text()='EmailAlerts']@for";
	private static final String customcertLabelLocator = "//LABEL[text()='CustomCert']@for";
	private static final String dedicatedprogrammanagerLabelLocator = "//LABEL[text()='DedicatedProgramManager']@for";
	private static final String multilocationLabelLocator = "//LABEL[text()='MultiLocation']@for";
	private static final String allowintegrationLabelLocator = "//LABEL[text()='AllowIntegration']@for";
	private static final String unlimitedlinkedassetsLabelLocator = "//LABEL[text()='UnlimitedLinkedAssets']@for";
	private static final String submitButtonLocator = "//input[@value='Submit']";
	private static final String cancelButtonLocator = "//input[@value='Cancel']";
	private static final String serialNumberFormatOptionsLocator = "//pre[contains(text(),'Serial Number Format Options.')]";
	private static final String username = "n4systems";
	private static final String password = "makesome$";
	private static final String contextRoot = "/fieldidadmin/";
	private static final String usernameInputLocator = "signIntoSystem_username";
	private static final String passwordInputLocator = "signIntoSystem_password";
	private static final String loginSubmitButtonLocator = "signIntoSystem_0";
	private static final String pageTitle = "Field ID Administration";

	public AdminConsoleOrganizationsPage(DefaultSelenium selenium) {
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
		selenium.waitForPageToLoad(pageLoadDefaultTimeout);
		assertPageTitle();
	}

	/**
	 * Clicks the Cancel button on the Edit Tenant page.
	 */
	public void gotoCancelEditTenant() {
		selenium.click(cancelButtonLocator);
		selenium.waitForPageToLoad(pageLoadDefaultTimeout);
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
		String s1 = selenium.getAttribute(unlimitedlinkedassetsLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(allowintegrationLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(multilocationLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(dedicatedprogrammanagerLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(customcertLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(emailalertsLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(partnercenterLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(brandingLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(projectsLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(jobsitesLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
		String s1 = selenium.getAttribute(integrationLabelLocator);
		String locator = "//INPUT[@id=\"" + s1 + "\"]";
		result = selenium.isChecked(locator);
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
	 * Before calling this method you need to log into the
	 * Field ID Administration Console using the method
	 * gotoFieldIDAdministrationConsole().
	 */
	public void loginToFieldIDAdministrationConsole() {
		selenium.type(usernameInputLocator, username);
		selenium.type(passwordInputLocator, password);
		selenium.click(loginSubmitButtonLocator);
		selenium.waitForPageToLoad(pageLoadDefaultTimeout);
		assertPageTitle();
	}

	/**
	 * Go to the Field ID Administration Console.
	 * 
	 */
	public void gotoFieldIDAdministrationConsole() {
		selenium.open(contextRoot);
		selenium.waitForPageToLoad(pageLoadDefaultTimeout);
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
	 * @param tenantID
	 */
	public void gotoEditTenant(String tenantID) {
		String editTenantLocator = "//td[text()='" + tenantID +"']/../td[3]/a[contains(text(),'Edit')]";
		selenium.click(editTenantLocator);
		selenium.waitForPageToLoad(pageLoadDefaultTimeout);
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
		String tableLocator = "//*[@id='content']/table[1]";
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
		assertNotNull(s1);
		int start = s1.indexOf(totalTenantText) + totalTenantText.length();
		assertTrue(start != -1);
		int end = s1.indexOf(" ", start+1);
		assertTrue(end != -1);
		assertTrue(start < end);
		String s2 = s1.substring(start, end);
		return Integer.parseInt(s2);
	}
}
