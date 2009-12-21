package com.n4systems.fieldid.testcase;

import java.util.List;
import com.n4systems.fieldid.datatypes.Customer;
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
		company = prop.getProperty("company", "NOT SET");
		userid = prop.getProperty("userid", "NOT SET");
		password = prop.getProperty("password", "NOT SET");
	}

	public void atest() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	/**
	 * OU: Adding a Customer.
	 * 
	 * Requirements
	 * ----------------------------------------------------------------------------------------------------------
	 * 1. Each customer will be assigned to OU
	 * 2. A customer can be assigned to the tenant (all OU's) allowing each OU to see that customer
	 * 3. If the system user adding the customer belongs to one OU they can add the customer to only their OU or on the tenant level
	 * 4. If the system user adding the customer belongs to multiple OU's they can add the customer to any one (1) OU or on the tenant level
	 * 5. If the system user adding the customer belongs to the tenant they can add the customer to any one (1) OU or on the tenant level
	 * 6. If a customer is assigned on the tenant level any system user (with Manage Customer permission) can then assign that customer to their OU. This will provide a warning telling the system user that by moving this to a specific OU all other OU's in your company can now longer see this customer.
	 * 
	 * @throws Exception
	 */
	public void testWeb1078() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();

			admin.gotoAdministration();
			mos.gotoManageOrganizations();
			String primaryOrganizationName = mos.getPrimaryOrganizationName();
			List<String> secondaryOrganizations = mos.getSecondaryOrganizationNames();
			if(secondaryOrganizations.size() < 1) {
				mos.gotoAddOrganizationalUnit();
				Organization o = new Organization(misc.getRandomString());
				mos.setAddOrganizationForm(o);
				mos.saveAddOrganization();
				secondaryOrganizations.add(o.getName());
			}
			admin.gotoAdministration();
			mcs.gotoManageCustomers(false);
			mcs.gotoAddCustomer(false);
			
			String primaryCustomerID = misc.getRandomString();
			String primaryCustomerName = primaryCustomerID;
			Customer primaryCustomer = new Customer(primaryCustomerID, primaryCustomerName, primaryOrganizationName);
			mcs.addCustomer(primaryCustomer, false);
			
			mcs.gotoAddCustomer(false);
			String secondaryCustomerID = misc.getRandomString();
			String secondaryCustomerName = secondaryCustomerID;
			String secondaryOrganizationName = secondaryOrganizations.get(0);
			Customer secondaryCustomer = new Customer(secondaryCustomerID, secondaryCustomerName, secondaryOrganizationName);
			mcs.addCustomer(secondaryCustomer, false);
			
			admin.gotoAdministration();
			mus.gotoManageUsers();
			mus.gotoAddEmployeeUser();
			String secondaryOrganizationEmployeeID = misc.getRandomString(15);
			String email = "darrell.grainger@n4systems.com";
			String firstName = secondaryOrganizationEmployeeID;
			String lastName = "Employee";
			EmployeeUser u = new EmployeeUser(secondaryOrganizationEmployeeID, email , firstName, lastName, password);
			u.addPermission(EmployeeUser.endusers);
			u.addPermission(EmployeeUser.sysusers);
			Owner owner = new Owner(secondaryOrganizationName);
			u.setOwner(owner);
			mus.addEmployeeUser(u);

			misc.logout();
			login.setUserName(secondaryOrganizationEmployeeID);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mcs.gotoManageCustomers(false);
			mcs.gotoAddCustomer(false);
			
			// try to add a customer, primary organization (should work)
			boolean passed = true;
			try {
				primaryCustomerID = misc.getRandomString();
				primaryCustomerName = primaryCustomerID;
				primaryCustomer = new Customer(primaryCustomerID, primaryCustomerName, primaryOrganizationName);
				mcs.addCustomer(primaryCustomer, false);
			} catch (Exception e) {
				// adding a primary should fail and throw an exception
				passed = false;
			}
			assertTrue("Was not able to create a primary customer as a secondary employee", passed);

			// add a customer, secondary organization
			mcs.gotoAddCustomer(false);
			secondaryCustomerID = misc.getRandomString();
			secondaryCustomerName = secondaryCustomerID;
			secondaryCustomer = new Customer(secondaryCustomerID, secondaryCustomerName, secondaryOrganizationName);
			mcs.addCustomer(secondaryCustomer, false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1086() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1080() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1161() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1157() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1159() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1158() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb592() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb547() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1049() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1168() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1002() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1048() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1144() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1162() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1164() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1057() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1079() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1087() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1209() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1077() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1204() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1165() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1208() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1101() throws Exception {

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			// TODO: put your test code here
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void testWeb1050() throws Exception {
		String country = prop.getProperty("country", "NOT SET");
		String timeZone = prop.getProperty("timezone", "NOT SET");

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
	
	public void testWeb1099() throws Exception {
		String primaryCountry = prop.getProperty("web1099primarycountry", "NOT SET");
		String primaryTimeZone = prop.getProperty("web1099primarytimezone", "NOT SET");
		String secondaryCountry = prop.getProperty("web1099secondarycountry", "NOT SET");
		String secondaryTimeZone = prop.getProperty("web1099secondarytimezone", "NOT SET");
		
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
