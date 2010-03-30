package com.n4systems.fieldid.testcase;

import java.util.List;

import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Jobs;
import com.n4systems.fieldid.MyAccount;
import com.n4systems.fieldid.Reporting;
import com.n4systems.fieldid.admin.ManageCustomers;
import com.n4systems.fieldid.admin.ManageEventTypeGroups;
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageOrganizations;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.admin.ManageSystemSettings;
import com.n4systems.fieldid.admin.ManageUsers;
import com.n4systems.fieldid.admin.ManageYourSafetyNetwork;
import com.n4systems.fieldid.datatypes.EmployeeUser;
import com.n4systems.fieldid.datatypes.Owner;

public class Validate extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test_validate_all_the_links_in_the_login_page() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.validate(company, userid, password);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void test_manage_system_settings_in_the_setup_page() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void test_edit_primary_organization_and_create_edit_organization_units() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageOrganizations mos = new ManageOrganizations(ie);
			mos.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void test_create_edit_delete_customer_and_create_customer_user() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with more than 20 customers
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageCustomers mcs = new ManageCustomers(ie);
			mcs.validate(false);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void test_create_edit_customer_user() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageCustomers mcs = new ManageCustomers(ie);
			mcs.gotoManageCustomers(false);
			List<String> customers = mcs.getCustomerNamesFromCurrentPage();
			assertTrue("There should be at least one customer to test Manage Users", customers.size() > 0);
			int index = misc.getRandomInteger(customers.size());
			admin.gotoAdministration();
			ManageUsers mus = new ManageUsers(ie);
			mus.validate(customers.get(index));
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Manage User Registrations
	
	public void test_edit_system_settings() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageSystemSettings mss = new ManageSystemSettings(ie);
			mss.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void test_create_delete_product_type() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// someone with more than 20 product types but not much more
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageProductTypes mpts = new ManageProductTypes(ie);
			mpts.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Manage Product Type Groups
	
	// Manage Product Code Mappings
	
	// Manage Product Statuses
		
	public void test_edit_inspection_type_and_create_master_inspection_type_and_create_inspection_form() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// more than 20 inspection types but not many more
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageInspectionTypes mits = new ManageInspectionTypes(ie);
			mits.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void test_create_event_type_group_and_create_inspection_type_with_event_type_group() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// more than 20 inspection types but not many more
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageEventTypeGroups metgs = new ManageEventTypeGroups(ie);
			metgs.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Manage Inspection Books
	
	// Auto Attribute Wizard
	
	// Manage Comment Templates
	
	// Data Log
	
	public void test_import_catalog_through_safety_network_and_test_auto_publish_assets_feature() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// Needs to be a manufacturer tenant
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String linkedCompany = prop.getProperty("linkedcompany", "NOT SET");
		String linkedUserid = prop.getProperty("linkeduserid", "NOT SET");
		String linkedPassword = prop.getProperty("linkedpassword", "NOT SET");

		try {
			// Log into a company and get its Field ID Access Code
			login.setCompany(linkedCompany);
			login.setUserName(linkedUserid);
			login.setPassword(linkedPassword);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
 			ManageYourSafetyNetwork mysn = new ManageYourSafetyNetwork(ie);
			mysn.gotoManageYourSafetyNetwork();
//			String FIDAC = mysn.getFIDAC();
			misc.logout();

			// Test publishing your catalog
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
//			mysn.validateManufacturer(FIDAC);
			String companyName = admin.getCompanyName();
			misc.logout();

			// Test importing a manufacturer's catalog
			login.setCompany(linkedCompany);
			login.setUserName(linkedUserid);
			login.setPassword(linkedPassword);
			login.login();
			admin.gotoAdministration();
			mysn.validateTenant(companyName);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	public void test_all_the_links_in_the_home_page() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with jobs
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		boolean jobs = Boolean.parseBoolean(prop.getProperty("jobs", "NOT SET"));
		boolean newFeaturesPDF = Boolean.parseBoolean(prop.getProperty("newfeaturespdf", "NOT SET"));

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Home home = new Home(ie);
			home.validate(jobs, newFeaturesPDF);	// second parameter is true of new features PDF ready.
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void test_create_asset_and_test_integration_data_bridge_and_test_multi_add() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with integration and known order number
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String orderNumber = prop.getProperty("ordernumber", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Identify identify = new Identify(ie);
			identify.validate(orderNumber);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	// Inspect
	
	public void test_asset_search_and_test_mass_update_and_test_export_to_excel_and_test_delete_asset() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with 10000+ assets
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String serialtext = prop.getProperty("serialtext", "NOT SET");
		String orgUnit = prop.getProperty("orgunit", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Owner owner = new Owner(orgUnit);
			Assets assets = new Assets(ie);
			assets.validate(serialtext, owner);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void test_reporting_and_test_print_report_and_save_report_and_export_to_excel_and_saved_report() throws Exception {
		String company = prop.getProperty("company", "NOT SET");	// tenant with 10000+ inspections
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Reporting reporting = new Reporting(ie);
			reporting.validate();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	// need a validate for Schedule

	public void test_create_jobs() throws Exception {
		String n4systems = prop.getProperty("n4systems", "NOT SET");
		String n4password = prop.getProperty("n4password", "NOT SET");
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");
		String email = prop.getProperty("email", "NOT SET");
		String firstName = prop.getProperty("firstname", "NOT SET");
		String lastName = prop.getProperty("lastname", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(n4systems);
			login.setPassword(n4password);
			login.login();
			Admin admin = new Admin(ie);
			admin.gotoAdministration();
			ManageUsers mus = new ManageUsers(ie);
			mus.gotoManageUsers();
			mus.setListUsersNameFilter(userid);
			mus.gotoListUsersSearch();
			EmployeeUser u = new EmployeeUser(userid, email, firstName, lastName, password);
			if(!mus.isUser(userid)) {
				mus.gotoAddEmployeeUser();
				u.addPermission(EmployeeUser.tag);
				u.addPermission(EmployeeUser.sysconfig);
				u.addPermission(EmployeeUser.sysusers);
				u.addPermission(EmployeeUser.endusers);
				u.addPermission(EmployeeUser.create);
				u.addPermission(EmployeeUser.edit);
				u.addPermission(EmployeeUser.jobs);
				u.addPermission(EmployeeUser.safety);
				mus.addEmployeeUser(u);
			}
			misc.logout();

			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			Jobs jobs = new Jobs(ie);
			jobs.validate(u);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}

	public void test_links_under_the_my_account() throws Exception {
		String company = prop.getProperty("company", "NOT SET");
		String userid = prop.getProperty("userid", "NOT SET");
		String password = prop.getProperty("password", "NOT SET");

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			MyAccount myAccount = new MyAccount(ie);
			myAccount.validate();
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
