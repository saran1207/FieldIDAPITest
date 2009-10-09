package com.n4systems.fieldid.testcase;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.datatypes.CustomerUser;
import com.n4systems.fieldid.datatypes.EmployeeUser;
import com.n4systems.fieldid.datatypes.Organization;
import com.n4systems.fieldid.datatypes.Owner;

public class Validate_2009_7_0 extends FieldIDTestCase {

	private String company;
	private String userid;
	private String password;

	protected void setUp() throws Exception {
		super.setUp();
		company = prop.getProperty("company");
		userid = prop.getProperty("userid");
		password = prop.getProperty("password");
	}

	public void testWeb1050() throws Exception {
		String country = prop.getProperty("country");
		String timeZone = prop.getProperty("timezone");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mus.gotoManageUsers();
			mus.gotoAddCustomerUser();
			CustomerUser cu = new CustomerUser(null, null, null, null, null);
			cu = mus.getAddCustomerUser();
			assertEquals(cu.getCountry(), country);
			assertEquals(cu.getTimeZone(), timeZone);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void Broken_testWeb1099() throws Exception {
		String primaryCountry = prop.getProperty("web1099primarycountry");
		String primaryTimeZone = prop.getProperty("web1099primarytimezone");
		String secondaryCountry = prop.getProperty("web1099secondarycountry");
		String secondaryTimeZone = prop.getProperty("web1099secondarytimezone");
		
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			
			// Go to Manage Organizations
			admin.gotoAdministration();
			mos.gotoManageOrganizations();

			// Set the country and time zone for primary organization
			Organization primaryOrgUnit = new Organization(null);
			primaryOrgUnit.setCountryTimeZone(primaryCountry);
			primaryOrgUnit.setTimeZone(primaryTimeZone);
			mos.gotoEditPrimaryOrganization();
			mos.setEditOrganizationForm(primaryOrgUnit, true);
			mos.saveEditOrganization();
			
			// Create a secondary organization with a different country and time zone
			Organization secondaryOrgUnit = new Organization(misc.getRandomString());
			secondaryOrgUnit.setCountryTimeZone(secondaryCountry);
			secondaryOrgUnit.setTimeZone(secondaryTimeZone);
			mos.gotoAddOrganizationalUnit();
			mos.setAddOrganizationForm(secondaryOrgUnit);
			mos.saveAddOrganization();

			// Go to Manage Users
			admin.gotoAdministration();
			mus.gotoManageUsers();
			
			// goto add a system user
			mus.gotoAddEmployeeUser();
			
			//confirm the time zone defaults to primary organization
			String country = mus.getCountryFromAddEmployeeUser();
			String timeZone = mus.getTimeZoneFromAddEmployeeUser();
			assertEquals(primaryCountry, country);
			assertEquals(primaryTimeZone, timeZone);
			
			// add a system user, secondary organization, manage users permission
			String userID = misc.getRandomString(15);
			String email = "darrell.grainger@n4systems.com";
			String firstName = "Darrell";
			String lastName = "Grainger";
			EmployeeUser u = new EmployeeUser(userID, email, firstName, lastName, password);
			u.setCountry(secondaryCountry);
			u.setTimeZone(secondaryTimeZone);
			Owner o = new Owner(secondaryOrgUnit.getName());
			u.setOwner(o);
			u.addPermission(EmployeeUser.sysusers);
			mus.addEmployeeUser(u);
			
			// log out, log in as secondary organization user
			misc.logout();
			login.setUserName(userID);
			login.setPassword(password);
			login.login();
			
			// goto add a system user
			admin.gotoAdministration();
			mus.gotoManageUsers();
			mus.gotoAddEmployeeUser();
			
			//confirm the time zone defaults to primary organization
			country = mus.getCountryFromAddEmployeeUser();
			timeZone = mus.getTimeZoneFromAddEmployeeUser();
			assertEquals(secondaryCountry, country);
			assertEquals(secondaryTimeZone, timeZone);
			
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
