package com.n4systems.fieldid.selenium.testcase;

import java.util.List;
import org.junit.*;

public class ProofOfConcept extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void proofOfConcept() throws Exception {
		String userid = "n4systems";
		String password = "makesome$";
		
		adminConsoleOrgPage.gotoFieldIDAdministrationConsole();
		adminConsoleOrgPage.loginToFieldIDAdministrationConsole(userid, password);
		int totalTenants = adminConsoleOrgPage.getTotalTenants();
		List<String> tenants = adminConsoleOrgPage.getTenantIDs();
		adminConsoleOrgPage.gotoEditTenant(tenants.get(0));
		String serialNumberFormat = adminConsoleOrgPage.getTenantSerialNumberFormat();
		String dateFormat = adminConsoleOrgPage.getTenantDateFormat();
		long diskSpaceLimit = adminConsoleOrgPage.getTenantDiskSpaceLimit();
		long assetLimit = adminConsoleOrgPage.getTenantAssetLimit();
		long userLimit = adminConsoleOrgPage.getTenantUserLimit();
		long secondaryOrgLimit = adminConsoleOrgPage.getTenantSecondaryOrgLimit();
		boolean integration = adminConsoleOrgPage.hasIntegration();
		boolean jobsites = adminConsoleOrgPage.hasJobSites();
		boolean projects = adminConsoleOrgPage.hasProjects();
		boolean branding = adminConsoleOrgPage.hasBranding();
		boolean partnercenter = adminConsoleOrgPage.hasPartnerCenter();
		boolean emailalerts = adminConsoleOrgPage.hasEmailAlerts();
		boolean customcert = adminConsoleOrgPage.hasCustomCert();
		boolean matt = adminConsoleOrgPage.hasDedicatedProgramManager();
		boolean multilocation = adminConsoleOrgPage.hasMultiLocation();
		boolean allowintegration = adminConsoleOrgPage.hasAllowIntegration();
		boolean unlimitedlinkedassets = adminConsoleOrgPage.hasUnlimitedLinkedAssets();
		String options = adminConsoleOrgPage.getSerialNumberFormatOptions();
		adminConsoleOrgPage.gotoCancelEditTenant();
		adminConsoleOrgPage.gotoEditTenant(tenants.get(0));
		adminConsoleOrgPage.gotoSubmitEditTenant();
		
		System.out.println("Total tenants: " + totalTenants);
		System.out.println("List of tenant IDs:\n" + tenants);
		System.out.println("Serial Number Format: " + serialNumberFormat);
		System.out.println("Date Format: " + dateFormat);
		System.out.println("Disk Space List: " + diskSpaceLimit);
		System.out.println("Asset Limit: " + assetLimit);
		System.out.println("User Limit: " + userLimit);
		System.out.println("Secondary Organization Limit: " + secondaryOrgLimit);
		System.out.println("Integration: " + integration);
		System.out.println("JobSites: " + jobsites);
		System.out.println("Jobs: " + projects);
		System.out.println("Branding: " + branding);
		System.out.println("PartnerCenter: " + partnercenter);
		System.out.println("EmailAlerts: " + emailalerts);
		System.out.println("CustomCert: " + customcert);
		System.out.println("DedicatedProgramManager: " + matt);
		System.out.println("MultiLocation: " + multilocation);
		System.out.println("AllowIntegration: " + allowintegration);
		System.out.println("UnlimitedLinkedAssets" + unlimitedlinkedassets);
		System.out.println(options);
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FieldIDTestCase.tearDownAfterClass();
	}
}
