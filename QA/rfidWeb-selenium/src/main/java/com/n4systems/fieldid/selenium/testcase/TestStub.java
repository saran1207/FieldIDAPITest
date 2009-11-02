package com.n4systems.fieldid.selenium.testcase;

import java.util.List;
import org.junit.*;

public class TestStub extends FieldIDTestCase {

	String userid;
	String password;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		userid = "n4systems";
		password = "makesome$";
	}
	
	@Test
	public void proofOfConcept() throws Exception {
		adminConsoleOrgPages.gotoFieldIDAdministrationConsole();
		adminConsoleOrgPages.loginToFieldIDAdministrationConsole();
		int totalTenants = adminConsoleOrgPages.getTotalTenants();
		List<String> tenants = adminConsoleOrgPages.getTenantIDs();
		adminConsoleOrgPages.gotoEditTenant(tenants.get(0));
		String serialNumberFormat = adminConsoleOrgPages.getTenantSerialNumberFormat();
		String dateFormat = adminConsoleOrgPages.getTenantDateFormat();
		long diskSpaceLimit = adminConsoleOrgPages.getTenantDiskSpaceLimit();
		long assetLimit = adminConsoleOrgPages.getTenantAssetLimit();
		long userLimit = adminConsoleOrgPages.getTenantUserLimit();
		long secondaryOrgLimit = adminConsoleOrgPages.getTenantSecondaryOrgLimit();
		boolean integration = adminConsoleOrgPages.hasIntegration();
		boolean jobsites = adminConsoleOrgPages.hasJobSites();
		boolean projects = adminConsoleOrgPages.hasProjects();
		boolean branding = adminConsoleOrgPages.hasBranding();
		boolean partnercenter = adminConsoleOrgPages.hasPartnerCenter();
		boolean emailalerts = adminConsoleOrgPages.hasEmailAlerts();
		boolean customcert = adminConsoleOrgPages.hasCustomCert();
		boolean matt = adminConsoleOrgPages.hasDedicatedProgramManager();
		boolean multilocation = adminConsoleOrgPages.hasMultiLocation();
		boolean allowintegration = adminConsoleOrgPages.hasAllowIntegration();
		boolean unlimitedlinkedassets = adminConsoleOrgPages.hasUnlimitedLinkedAssets();
		String options = adminConsoleOrgPages.getSerialNumberFormatOptions();
		adminConsoleOrgPages.gotoCancelEditTenant();
		adminConsoleOrgPages.gotoEditTenant(tenants.get(0));
		adminConsoleOrgPages.gotoSubmitEditTenant();
		
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
		Thread.sleep(1);
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
