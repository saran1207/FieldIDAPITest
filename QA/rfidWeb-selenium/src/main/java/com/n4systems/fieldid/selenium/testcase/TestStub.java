package com.n4systems.fieldid.selenium.testcase;

import java.util.List;
import org.junit.*;

public class TestStub extends FieldIDTestCase {

	String tenant;
	String userid;
	String password;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		tenant = p.getProperty("tenant");
		userid = p.getProperty("userid");
		password = p.getProperty("password");
	}
	
	@Test
	public void test() throws Exception {
		adminConsoleOrgs.gotoFieldIDAdministrationConsole();
		adminConsoleOrgs.loginToFieldIDAdministrationConsole();
		int totalTenants = adminConsoleOrgs.getTotalTenants();
		List<String> tenants = adminConsoleOrgs.getTenantIDs();
		adminConsoleOrgs.gotoEditTenant(tenants.get(0));
		String serialNumberFormat = adminConsoleOrgs.getTenantSerialNumberFormat();
		String dateFormat = adminConsoleOrgs.getTenantDateFormat();
		long diskSpaceLimit = adminConsoleOrgs.getTenantDiskSpaceLimit();
		long assetLimit = adminConsoleOrgs.getTenantAssetLimit();
		long userLimit = adminConsoleOrgs.getTenantUserLimit();
		long secondaryOrgLimit = adminConsoleOrgs.getTenantSecondaryOrgLimit();
		boolean integration = adminConsoleOrgs.hasIntegration();
		boolean jobsites = adminConsoleOrgs.hasJobSites();
		boolean projects = adminConsoleOrgs.hasProjects();
		boolean branding = adminConsoleOrgs.hasBranding();
		boolean partnercenter = adminConsoleOrgs.hasPartnerCenter();
		boolean emailalerts = adminConsoleOrgs.hasEmailAlerts();
		boolean customcert = adminConsoleOrgs.hasCustomCert();
		boolean matt = adminConsoleOrgs.hasDedicatedProgramManager();
		boolean multilocation = adminConsoleOrgs.hasMultiLocation();
		boolean allowintegration = adminConsoleOrgs.hasAllowIntegration();
		boolean unlimitedlinkedassets = adminConsoleOrgs.hasUnlimitedLinkedAssets();
		String options = adminConsoleOrgs.getSerialNumberFormatOptions();
		adminConsoleOrgs.gotoCancelEditTenant();
		adminConsoleOrgs.gotoEditTenant(tenants.get(0));
		adminConsoleOrgs.gotoSubmitEditTenant();
		
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
