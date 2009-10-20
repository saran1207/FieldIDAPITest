package com.n4systems.fieldid.testcase;

import java.util.List;
import com.n4systems.fieldid.datatypes.Customer;
import com.n4systems.fieldid.datatypes.CustomerDivision;
import com.n4systems.fieldid.datatypes.CustomerUser;
import com.n4systems.fieldid.datatypes.EmployeeUser;
import com.n4systems.fieldid.datatypes.Organization;
import com.n4systems.fieldid.datatypes.Owner;
import com.n4systems.fieldid.datatypes.Product;

public class SmokeTestEx2 extends FieldIDTestCase {

	private String company;
	private String password;
	private String email;
	private String n4systems;
	private boolean jobsites;
	
	/**
	 * Organizations, Customers and Divisions
	 */
	static String primaryOrg;
	static String secondaryOrg;
	static Customer primaryCustomer;
	static CustomerDivision primaryDivision;
	static Customer primaryCustomer2;
	static CustomerDivision primaryDivision2;
	static Customer secondaryCustomer;
	static CustomerDivision secondaryDivision;
	static Customer secondaryCustomer2;
	static CustomerDivision secondaryDivision2;
	
	/**
	 * Users: employee, customer and divisional
	 */
	static EmployeeUser primaryEmployeeUser;
	static EmployeeUser secondaryEmployeeUser;
	static EmployeeUser secondaryEmployeeUser2;
	static CustomerUser primaryCustomerUser;
	static CustomerUser primaryDivisionUser;
	static CustomerUser secondaryCustomerUser;
	static CustomerUser secondaryDivisionUser;
	static CustomerUser primaryCustomerUser2;
	static CustomerUser primaryDivisionUser2;
	static CustomerUser secondaryCustomerUser2;
	static CustomerUser secondaryDivisionUser2;

	/**
	 * Asset serial numbers created in one test case
	 * and used in subsequent test cases.
	 */
	static String primaryEmployeeAssetSerialNumber;
	static String secondaryEmployeeAssetSerialNumber;
	static String primaryCustomerAssetSerialNumber;
	static String secondaryCustomerAssetSerialNumber;
	static String primaryDivisionAssetSerialNumber;
	static String secondaryDivisionAssetSerialNumber;
	static String primaryCustomer2AssetSerialNumber;
	static String secondaryCustomer2AssetSerialNumber;
	static String primaryDivision2AssetSerialNumber;
	static String secondaryDivision2AssetSerialNumber;
	
	protected void setUp() throws Exception {
		super.setUp();
		company = prop.getProperty("company");
		login.setCompany(company);
		password = prop.getProperty("password");
		email = prop.getProperty("email");
		n4systems = prop.getProperty("n4systems");
		jobsites = Boolean.parseBoolean(prop.getProperty("jobsites"));
	}
	
	public void testCreatingCustomersDivisionsAndUsers() throws Exception {
		String method = getName();

		try {
			loginUser(n4systems, password);
			primaryOrg = getPrimaryOrganization();
			secondaryOrg = getSecondaryOrganization();
			
			// Create employee users
			primaryEmployeeUser = createEmployeeUser(primaryOrg);
			secondaryEmployeeUser = createEmployeeUser(secondaryOrg);
			secondaryEmployeeUser2 = createEmployeeUser(secondaryOrg);
			
			// Create Customer, Divisons and Customer Users under Primary Org
			primaryCustomer = createCustomer(primaryOrg);
			primaryDivision = createDivision();
			Owner owner = new Owner(primaryOrg, primaryCustomer.getCustomerName(), "");
			primaryCustomerUser = createCustomerUser(owner, jobsites);
			mcs.gotoCustomerDivisions(jobsites);
			owner.setDivision(primaryDivision.getDivisionName());
			primaryDivisionUser = createCustomerUser(owner, jobsites);
			
			// Create second set of Customer, Divisons and Customer Users under Primary Org
			primaryCustomer2 = createCustomer(primaryOrg);
			primaryDivision2 = createDivision();
			owner = new Owner(primaryOrg, primaryCustomer2.getCustomerName(), "");
			primaryCustomerUser2 = createCustomerUser(owner, jobsites);
			mcs.gotoCustomerDivisions(jobsites);
			owner.setDivision(primaryDivision2.getDivisionName());
			primaryDivisionUser2 = createCustomerUser(owner, jobsites);
			
			// Create Customer, Divisons and Customer Users under Secondary Org
			secondaryCustomer = createCustomer(secondaryOrg);
			secondaryDivision = createDivision();
			owner = new Owner(secondaryOrg, secondaryCustomer.getCustomerName(), "");
			secondaryCustomerUser = createCustomerUser(owner, jobsites);
			mcs.gotoCustomerDivisions(jobsites);
			owner.setDivision(secondaryDivision.getDivisionName());
			secondaryDivisionUser = createCustomerUser(owner, jobsites);

			// Create second set of Customer, Divisons and Customer Users under Secondary Org
			secondaryCustomer2 = createCustomer(secondaryOrg);
			secondaryDivision2 = createDivision();
			owner = new Owner(secondaryOrg, secondaryCustomer2.getCustomerName(), "");
			secondaryCustomerUser2 = createCustomerUser(owner, jobsites);
			mcs.gotoCustomerDivisions(jobsites);
			owner.setDivision(secondaryDivision2.getDivisionName());
			secondaryDivisionUser2 = createCustomerUser(owner, jobsites);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingPrimaryAssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(primaryOrg);
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			p.setPublished(false);
			primaryEmployeeAssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingSecondaryAssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(secondaryOrg);
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			secondaryEmployeeAssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingPrimaryCustomerAssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(primaryOrg, primaryCustomer.getCustomerName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			primaryCustomerAssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingSecondaryCustomerAssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(secondaryOrg, secondaryCustomer.getCustomerName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			secondaryCustomerAssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingPrimaryDivisionAssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(primaryOrg, primaryCustomer.getCustomerName(), primaryDivision.getDivisionName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			primaryDivisionAssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingSecondaryDivisionAssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(secondaryOrg, secondaryCustomer.getCustomerName(), secondaryDivision.getDivisionName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			secondaryDivisionAssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingPrimaryCustomer2AssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(primaryOrg, primaryCustomer2.getCustomerName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			primaryCustomer2AssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingSecondaryCustomer2AssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(secondaryOrg, secondaryCustomer2.getCustomerName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			secondaryCustomer2AssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingPrimaryDivision2AssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(primaryOrg, primaryCustomer2.getCustomerName(), primaryDivision2.getDivisionName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			primaryDivision2AssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testCreatingSecondaryDivision2AssetAsPrimaryEmployee() throws Exception {
		String method = getName();

		try {
			loginUser(primaryEmployeeUser.getUserID(), password);
			identify.gotoAddProduct();
			List<String> productStatuses = identify.getProductStatusFromAddAsset();
			Product p = new Product(misc.getDateString());
			String s = misc.getRandomRFID();
			p.setRFIDNumber(s);
			p.setReferenceNumber(s);
			Owner owner = new Owner(secondaryOrg, secondaryCustomer2.getCustomerName(), secondaryDivision2.getDivisionName());
			p.setOwner(owner);
			p.setLocation(s);
			int n = productStatuses.size();
			int index = (n > 1) ? misc.getRandomInteger(1, n-1) : 0;
			String productStatus = productStatuses.get(index);
			p.setProductStatus(productStatus);
			p.setPurchaseOrder(s);
			p.setComments(s);
			secondaryDivision2AssetSerialNumber = identify.setProduct(p, true);
			identify.addProductSave();
			home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
			assets.validateAsset(p);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private void loginUser(String userID, String password) throws Exception {
		login.setUserName(userID);
		login.setPassword(password);
		login.login();
	}

	public void atest1() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void atest2() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void atest3() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void atest4() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void atest5() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void atest6() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void atest() throws Exception {
		String method = getName();

		try {
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	private CustomerUser createCustomerUser(Owner owner, boolean jobsites) throws Exception {
		mcs.gotoUsers(jobsites);
		mcs.gotoAddCustomerUser(jobsites);

		String firstName = "Smoke";
		String lastName = misc.getRandomString(9);
		String userID = "smoke-" + lastName;
		CustomerUser cu = new CustomerUser(userID, email, firstName, lastName, password);
		cu.setOwner(owner);
		cu.setCountry("Canada");
		cu.setTimeZone("Toronto");
		mcs.addCustomerUser(cu, jobsites);
		return cu;
	}

	private CustomerDivision createDivision() throws Exception {
		String id = misc.getRandomString(9);
		String name = "smoke-" + id;
		CustomerDivision division = new CustomerDivision(id, name);
		division.setContactName("Smoke " + division.getDivisionID());
		division.setContactEmail(email);
		division.setStreetAddress("179 John St.");
		division.setCity("Toronto");
		division.setState("ON");
		division.setZip("M5T 1X4");
		division.setCountry("Canada");
		division.setPhone1("416-599-6464");
		division.setPhone2("800-99-N4SYS");
		division.setFax("416-599-6463");
		mcs.gotoCustomerDivisions(jobsites);
		mcs.gotoAddCustomerDivision(jobsites);
		mcs.setCustomerDivision(division);
		mcs.addCustomerDivision(jobsites);
		return division;
	}

	private Customer createCustomer(String orgUnit) throws Exception {
		admin.gotoAdministration();
		mcs.gotoManageCustomers(jobsites);
		mcs.gotoAddCustomer(jobsites);
		String id = misc.getRandomString();
		String name = "Smoke " + id;
		Customer u = new Customer(id, name, orgUnit);
		u.setContactName(name);
		u.setContactEmail(email);
		u.setStreetAddress("179 John St.");
		u.setCity("Toronto");
		u.setState("ON");
		u.setZip("M5T 1X4");
		u.setCountry("Canada");
		u.setPhone1("416-599-6464");
		u.setPhone2("800-99-N4SYS");
		u.setFax("416-599-6463");
		mcs.addCustomer(u, jobsites);
		return u;
	}

	/**
	 * Gets the secondary organization names. If there are no secondary
	 * organizations, it creates a secondary organization.
	 * 
	 * Assumes you are on the Manage Organizations section.
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getSecondaryOrganization() throws Exception {
		String org = null;
		List<String> secondaryOrgs = mos.getSecondaryOrganizationNames();
		int n = secondaryOrgs.size();
		if(n < 1) {
			mos.gotoAddOrganizationalUnit();
			Organization o = new Organization("Smoke-" + misc.getRandomString(9));
			o.setNameOnCert(o.getName());
			mos.setAddOrganizationForm(o);
			mos.saveAddOrganization();
			secondaryOrgs.add(o.getName());
			n = 1;
		}
		org = secondaryOrgs.get(misc.getRandomInteger(n));
		return org;
	}

	/**
	 * Go to the Manage Organization section and get the primary
	 * organization name.
	 * 
	 * Assumes you can go to Administration and Manage Organization.
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getPrimaryOrganization() throws Exception {
		String org = null;
		admin.gotoAdministration();
		mos.gotoManageOrganizations();
		org = mos.getPrimaryOrganizationName();
		return org;
	}

	private EmployeeUser createEmployeeUser(String orgUnit) throws Exception {
		admin.gotoAdministration();
		mus.gotoManageUsers();
		mus.gotoAddEmployeeUser();
		String firstName = "Smoke";
		String lastName = misc.getRandomString(9);
		String userID = "smoke-" + lastName;
		EmployeeUser employee = new EmployeeUser(userID, email, firstName, lastName, password);
		Owner o = new Owner(orgUnit);
		employee.setOwner(o);
		employee.setCountry("Canada");
		employee.setTimeZone("Toronto");
		employee.addAllPermissions();
		mus.addEmployeeUser(employee);
		return employee;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
