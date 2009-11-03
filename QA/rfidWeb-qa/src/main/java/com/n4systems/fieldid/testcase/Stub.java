package com.n4systems.fieldid.testcase;

import java.util.List;

import com.n4systems.fieldid.datatypes.EmployeeUser;
import com.n4systems.fieldid.datatypes.Organization;
import com.n4systems.fieldid.datatypes.Owner;

public class Stub extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test() throws Exception {
		String method = getName();

		try {
			String email = "darrell.grainger@n4systems.com";
			String password = "makemore$";
			String country = "Canada";
			String timeZone = "Toronto";
			
			// login
			login.setCompany("illinois");
			login.setUserName("n4systems");
			login.setPassword(password);
			login.login();
			
			// get primary org name
			admin.gotoAdministration();
			mos.gotoManageOrganizations();
			String primaryOrg = mos.getPrimaryOrganizationName();
			
			// get secondary orgs
			List<String> secondaryOrgs = mos.getSecondaryOrganizationNames();
			
			// if none, add one
			if(secondaryOrgs.size() < 1) {
				mos.gotoAddOrganizationalUnit();
				Organization secondOrg = new Organization(misc.getRandomString());
				mos.setAddOrganizationForm(secondOrg);
				mos.saveAddOrganization();
				secondaryOrgs.add(secondOrg.getName());
			}
			
			int index = misc.getRandomInteger(0, secondaryOrgs.size()-1);
			String secondaryOrg = secondaryOrgs.get(index);
			
			// add employees
			admin.gotoAdministration();
			mus.gotoManageUsers();
			EmployeeUser primary = new EmployeeUser("primary", email , "Primary", "Employee", password);
			primary.setCountry(country);
			primary.setTimeZone(timeZone);
			Owner o = new Owner(primaryOrg);
			primary.setOwner(o);
			primary.addAllPermissions();
			mus.setListUsersUserType("Employee");
			mus.setListUsersNameFilter(primary.getUserID());
			mus.gotoListUsersSearch();
			if(mus.isUser(primary.getUserID())) {
				mus.gotoEditEmployeeUser(primary);
				mus.editEmployeeUser(primary);
			} else {
				mus.gotoAddEmployeeUser();
				mus.addEmployeeUser(primary);
			}
			
			// second employee
			admin.gotoAdministration();
			mus.gotoManageUsers();
			EmployeeUser secondary = new EmployeeUser("secondary", email , "Secondary", "Employee", password);
			secondary.setCountry(country);
			primary.setTimeZone(timeZone);
			o = new Owner(secondaryOrg);
			secondary.setOwner(o);
			secondary.addAllPermissions();
			mus.setListUsersUserType("Employee");
			mus.setListUsersNameFilter(secondary.getUserID());
			mus.gotoListUsersSearch();
			if(mus.isUser(secondary.getUserID())) {
				mus.gotoEditEmployeeUser(secondary);
				mus.editEmployeeUser(secondary);
			} else {
				mus.gotoAddEmployeeUser();
				mus.addEmployeeUser(secondary);
			}
			
			Thread.sleep(1);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
