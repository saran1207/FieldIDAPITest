package com.n4systems.fieldid.testcase;

import static watij.finders.SymbolFactory.*;
import static watij.finders.FinderFactory.*;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import com.jniwrapper.win32.shdocvw.IWebBrowser2;
import com.jniwrapper.win32.ui.Wnd;
import watij.runtime.NotImplementedYetException;
import watij.elements.*;
import watij.runtime.ie.*;

/**
 * This is a set if common methods in order to centralize maintenance. All test
 * cases should use this class to interact with the application. If the way we
 * identify a product changes, we update this class in, hopefully, one location
 * and it updates all test cases.
 * 
 * @author dgrainge
 * 
 */
public class commonFieldIDMethods {

	String ef = "extendedfeatures"; // keyword in the property file
	String eu = "enduser"; // keyword in the property file
	String pm = "permissions"; // keyword in the property file
	String on = "orders"; // keyword in the property file
	Properties p = new Properties();
	String extendedFeatures[] = { "" };
	boolean enduser = false; // getEndUser defaults to false
	String permissions[] = { "" };
	String orderNumbers[] = { "" };
	
	/**
	 * The waitForReady() method does not wait for the javascript engine to
	 * complete. If you have code which modifies the DOM you need to wait for
	 * the code to finish. Because we cannot detect it, we will wait for 'hack'
	 * milliseconds.
	 */
	static long hack = 5000;
	
	/**
	 * The product has a lightbox feature implemented in javascript. Since the
	 * waitForReady() method does not wait for the javascript engine, we need
	 * to account for the delay in the lightbox. Currently, it is programmed to
	 * expand open and takes approximately 5 seconds. On top of this is other
	 * overhead so we set a delay of 10 seconds just to be safe.
	 */
	static long lightbox = 10000;

	/**
	 * Constructor
	 * 
	 */
	public commonFieldIDMethods() {
	}

	/**
	 * Constructor
	 * 
	 * @param directory - the directory to read the file from
	 * @param filename  - the file to read in for setting the properties
	 */
	public commonFieldIDMethods(String directory, String filename) {
		FileInputStream in;
		String qualifiedFilename = directory + "/" + filename;
		try {
			in = new FileInputStream(qualifiedFilename);
			p.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + qualifiedFilename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Attempt to load properties,"
					+ qualifiedFilename + ", failed.");
			e.printStackTrace();
		}

		String s = getTenantFeatures();
		int i = s.indexOf(ef); // find the location of 'extendedfeatures'
		int j;
		String t;
		if (i != -1) {
			j = s.indexOf(':', i) + 1; // find the start of 'extendedfeatures'
										// values
			i = s.indexOf(';', j); // find the end of 'extendedfeatures' values
			t = s.substring(j, i);
			extendedFeatures = t.split(",");
		}

		i = s.indexOf(eu); // find the location of 'enduser'
		if (i != -1) {
			j = s.indexOf(':', i) + 1; // find the start of 'enduser' value
			i = s.indexOf(';', j); // find the end of 'enduser' value
			t = s.substring(j, i);
			if (t.equalsIgnoreCase("true"))
				enduser = true;
			else
				enduser = false;
		}

		i = s.indexOf(pm); // find the location of 'permissions'
		if (i != -1) {
			j = s.indexOf(':', i) + 1; // find the start of 'permissions' values
			i = s.indexOf(';', j); // find the end of 'permissions' values
			t = s.substring(j, i);
			permissions = t.split(",");
		}

		i = s.indexOf(on); // find the location of 'orders'
		if (i != -1) {
			j = s.indexOf(':', i) + 1; // find the start of 'orders' values
			i = s.indexOf(';', j); // find the end of 'orders' values
			t = s.substring(j, i);
			orderNumbers = t.split(",");
		}
	}

	/**
	 * Go to the List Users page. It also checks for certain elements to be
	 * present. Specifically, a text field for entering a filter name, a
	 * select list for picking the type of user and two buttons for clearing
	 * the filter and applying the filter.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoListUsers(IE ie) throws Exception {
		gotoManageSystemUsers(ie);
		if (!ie.textField(id("nameFilter")).exists())
			throw new TestCaseFailedException();
		if (!ie.selectList(id("userType")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(id("userList_search")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(id("userList_clear")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Checks to see if the current user being edited has Inspection
	 * permissions. This means the user will have Create Inspection permission.
	 * If either of these are true, return true. Only if both are false do we
	 * return false.
	 * 
	 * Assumes you are on the Edit User page for the user you are asking about.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @return true if the Create Inspection permission is enabled for the user record being viewed.
	 * @throws Exception
	 */
	public boolean isUserWithCreateInspectPermission(IE ie) throws Exception {
		boolean result = false;
		if (ie.radio(id("userSave_userPermissions_'" + InspectPermissions.CREATE.value() + "'_true")).getState()) // Create Inspection
			result = true;
		return result;
	}

	/**
	 * Checks to see if the current user being edited has Inspection
	 * permissions. This means the user will have Edit Inspection permission. If
	 * either of these are true, return true. Only if both are false do we
	 * return false.
	 * 
	 * Assumes you are on the Edit User page for the user you are asking about.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @return true if the Edit Inspection permission is enabled for the user record being viewed.
	 * @throws Exception
	 */
	public boolean isUserWithEditInspectPermission(IE ie) throws Exception {
		boolean result = false;
		if (ie.radio(id("userSave_userPermissions_'" + InspectPermissions.EDIT.value() + "'_true")).getState()) // Edit Inspection
			result = true;
		return result;
	}

	/**
	 * Get a list of users (Employee or Customers) who have inspection
	 * permissions. This would return a user who has either Create Inspection or
	 * Edit Inspection permission.
	 * 
	 * It is going to give you the user name, i.e. n4systems or sricci, and not
	 * the user's real name.
	 * 
	 * It assumes you are logged in with someone who has privileges for
	 * Administration. Specifically, Manage System Users.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user ids with Edit or Create Inspection permission enabled
	 * @throws Exception
	 */
	public String[] getListOfUsersWithInspectPermission(IE ie,
			String nameFilter, String userType, boolean clear) throws Exception {
		
		String[] create = getListOfUsersWithCreateInspectionPermission(ie, nameFilter, userType, clear);
		String[] edit = getListOfUsersWithEditInspectionPermission(ie, nameFilter, userType, clear);
		Set<String> both = new HashSet<String>();
		
		if (create != null) {
			both.addAll(Arrays.asList(create));
		}
		
		if (edit != null) {
			both.addAll(Arrays.asList(edit));
		}
		
		both.remove(null);
		
		if (both.size() == 0) {
			return null;
		}
		else {
			return both.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of users (Employee or Customers) who have inspection
	 * permissions. This would return a user who has either Create Inspection or
	 * Edit Inspection permission.
	 * 
	 * It is going to give you the user's real name, i.e. Shaun Ricci, and not
	 * the user name.
	 * 
	 * It assumes you are logged in with someone who has privileges for
	 * Administration. Specifically, Manage System Users.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user names with Edit or Create Inspection permission enabled
	 * @throws Exception
	 */
	public String[] getListOfUserNamesWithInspectPermission(IE ie,
			String nameFilter, String userType, boolean clear) throws Exception {

		String[] create = getListOfUserNamesWithCreateInspectionPermission(ie, nameFilter, userType, clear);
		String[] edit = getListOfUserNamesWithEditInspectionPermission(ie, nameFilter, userType, clear);
		Set<String> both = new HashSet<String>();
		
		if (create != null) {
			both.addAll(Arrays.asList(create));
		}
		
		if (edit != null) {
			both.addAll(Arrays.asList(edit));
		}
		
		both.remove(null);
		if (both.size() == 0) {
			return null;
		}
		else {
			return both.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of users (Employee or Customers) who have the create
	 * inspection permissions. This would return a user who has Create
	 * Inspection permission.
	 * 
	 * It is going to give you the user name, i.e. n4systems or sricci, and not
	 * the user's real name.
	 * 
	 * It assumes you are logged in with someone who has privileges for
	 * Administration. Specifically, Manage System Users.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user ids with Create Inspection permission enabled
	 * @throws Exception
	 */
	public String[] getListOfUsersWithCreateInspectionPermission(IE ie,
			String nameFilter, String userType, boolean clear) throws Exception {

		gotoListUsers(ie);
		filterListOfUsers(ie, nameFilter, userType, clear);

		List<String> result = new ArrayList<String>();

		boolean loopFlag = true;
		do {
			List<String> tmp = getUsersFromCurrentPage(ie);
			for (String user : tmp) {
				ie.link(text(user)).click();
				ie.waitUntilReady();
				if (isUserWithCreateInspectPermission(ie)) {
					result.add(user);
				}
				ie.back();
			}
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0) {
			return null;
		}
		else {
			return result.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of users (Employee or Customers) who have the create
	 * inspection permissions. This would return a user who has Create
	 * Inspection permission.
	 * 
	 * It is going to give you the user's real name, i.e. Shaun Ricci, and not
	 * the user name.
	 * 
	 * It assumes you are logged in with someone who has privileges for
	 * Administration. Specifically, Manage System Users.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user names with Create Inspection permission enabled
	 * @throws Exception
	 */
	public String[] getListOfUserNamesWithCreateInspectionPermission(IE ie,
			String nameFilter, String userType, boolean clear) throws Exception {

		gotoListUsers(ie);
		filterListOfUsers(ie, nameFilter, userType, clear);
		List<String> result = new ArrayList<String>();

		boolean loopFlag = true;
		do {
			List<String> tmp = getUsersFromCurrentPage(ie);
			for (String user : tmp) {
				ie.link(text(user)).click();
				ie.waitUntilReady();
				if (isUserWithCreateInspectPermission(ie)) {
					result.add(getUserNameFromEditUserPage(ie));
				}
				ie.back();
			}
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} 
			else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0) {
			return null;
		}
		else {
			return result.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of users (Employee or Customers) who have the edit inspection
	 * permissions. This would return a user who has Edit Inspection permission.
	 * 
	 * It is going to give you the user name, i.e. n4systems or sricci, and not
	 * the user's real name.
	 * 
	 * It assumes you are logged in with someone who has privileges for
	 * Administration. Specifically, Manage System Users.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user ids with Edit Inspection permission enabled
	 * @throws Exception
	 */
	public String[] getListOfUsersWithEditInspectionPermission(IE ie,
			String nameFilter, String userType, boolean clear) throws Exception {

		gotoListUsers(ie);
		filterListOfUsers(ie, nameFilter, userType, clear);
		List<String> result = new ArrayList<String>();

		boolean loopFlag = true;
		do {
			List<String> tmp = getUsersFromCurrentPage(ie);
			Iterator<String> i = tmp.iterator();
			while (i.hasNext()) {
				String user = i.next();
				ie.link(text(user)).click();
				ie.waitUntilReady();
				if (isUserWithEditInspectPermission(ie)) {
					result.add(user);
				}
				ie.back();
			}
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0) {
			return null;
		}
		else {
			return result.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of users (Employee or Customers) who have the edit inspection
	 * permissions. This would return a user who has Edit Inspection permission.
	 * 
	 * It is going to give you the user's real name, i.e. Shaun Ricci, and not
	 * the user name.
	 * 
	 * It assumes you are logged in with someone who has privileges for
	 * Administration. Specifically, Manage System Users.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user names with Edit Inspection permission enabled
	 * @throws Exception
	 */
	public String[] getListOfUserNamesWithEditInspectionPermission(IE ie,
			String nameFilter, String userType, boolean clear) throws Exception {

		gotoListUsers(ie);
		filterListOfUsers(ie, nameFilter, userType, clear);
		List<String> result = new ArrayList<String>();
		
		boolean loopFlag = true;
		do {
			List<String> tmp = getUsersFromCurrentPage(ie);
			Iterator<String> i = tmp.iterator();
			while (i.hasNext()) {
				String user = i.next();
				ie.link(text(user)).click();
				ie.waitUntilReady();
				if (isUserWithEditInspectPermission(ie)) {
					result.add(getUserNameFromEditUserPage(ie));
				}
				ie.back();
			}
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0) {
			return null;
		}
		else {
			return result.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of all the users for the current tenant. You can filter in
	 * 'name' or user type. The nameFilter will match against the userid,
	 * firstname or lastname. The user type is currently restricted to:
	 * 
	 * All Employee Customers
	 * 
	 * Any other value will throw an exception.
	 * 
	 * If there are no users which match the search filters this will return
	 * null.
	 * 
	 * If the clear parameter is set to true, it will press the Clear button to
	 * reset the search criteria before using the criteria provided.
	 * 
	 * This will return a list of user ids. There is another method for
	 * returning a list of user names.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user ids for the current tenant
	 * @throws Exception
	 */
	public String[] getListOfUsers(IE ie, String nameFilter, String userType,
			boolean clear) throws Exception {

		gotoListUsers(ie);
		filterListOfUsers(ie, nameFilter, userType, clear);

		List<String> tmp = new ArrayList<String>();
		List<String> result = new ArrayList<String>();

		boolean loopFlag = true;
		do {
			tmp = getUsersFromCurrentPage(ie);
			if (tmp.size() != 0) {
				result.addAll(tmp);
			}
			
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0) {
			return null;
		}
		else {
			return result.toArray(new String[0]);
		}
	}

	/**
	 * Get a list of all the users for the current tenant. You can filter in
	 * 'name' or user type. The nameFilter will match against the userid,
	 * firstname or lastname. The user type is currently restricted to:
	 * 
	 * All Employee Customers
	 * 
	 * Any other value will throw an exception.
	 * 
	 * If there are no users which match the search filters this will return
	 * null.
	 * 
	 * If the clear parameter is set to true, it will press the Clear button to
	 * reset the search criteria before using the criteria provided.
	 * 
	 * This will return a list of user's name. There is another method for
	 * returning a list of user id.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param nameFilter - used to narrow down the list of users on the web page
	 * @param userType - can be Employee or Customer
	 * @param clear - true causes the method to click the clear button on the filter
	 * @return a list of user names for the current tenant
	 * @throws Exception
	 */
	public String[] getListOfUserNames(IE ie, String nameFilter, String userType, boolean clear) throws Exception {
		
		gotoListUsers(ie);
		filterListOfUsers(ie, nameFilter, userType, clear);

		List<String> tmp = new ArrayList<String>();
		List<String> result = new ArrayList<String>();

		boolean loopFlag = true;
		do {
			tmp = getUserNamesFromCurrentPage(ie);
			if (tmp.size() != 0)
				result.addAll(tmp);
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0)
			return null;
		else
			return result.toArray(new String[0]);
	}

	private List<String> getUserNamesFromCurrentPage(IE ie) throws Exception {
		Links links = ie.links();
		List<String> users = new ArrayList<String>();
		for (int i = 0; i < links.length(); i++) {
			Link link = links.get(i);
			if (link.href().indexOf("userEdit.action?uniqueID=") != -1) {
				link.click();
				ie.waitUntilReady();
				users.add(getUserNameFromEditUserPage(ie));
				ie.back();
			}
		}
		return users;
	}

	/**
	 * Gets the user's first and last name from the Edit User page. Assumes you
	 * are already on the Edit User page.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @return firstname + " " + lastname
	 * @throws Exception
	 */
	public String getUserNameFromEditUserPage(IE ie) throws Exception {
		ie.waitUntilReady();
		String firstname = ie.textField(id("firstname")).value().trim();
		String lastname = ie.textField(id("lastname")).value().trim();
		String name = firstname + " " + lastname;
		return name;
	}

	private void filterListOfUsers(IE ie, String nameFilter, String userType,
			boolean clear) throws Exception {
		if (clear) {
			ie.button(id("userList_clear")).click();
			ie.waitUntilReady();
		}
		if (nameFilter != null) {
			ie.textField(id("nameFilter")).set(nameFilter);
		}
		if (userType != null) {
			ie.selectList(id("userType")).option(text(userType)).select();
		}
		if (userType != null || nameFilter != null) {
			ie.button(id("userList_search")).click();
			ie.waitUntilReady();
		}
	}

	private List<String> getUsersFromCurrentPage(IE ie) throws Exception {
		Links links = ie.links();
		List<String> users = new ArrayList<String>();
		for (int i = 0; i < links.length(); i++) {
			Link link = links.get(i);
			if (link.href().indexOf("userEdit.action?uniqueID=") != -1)
				users.add(link.text());
		}
		return users;
	}

	/**
	 * Go to the Manage System Users area. Assumes you have the necessary
	 * privileges to go to this area.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageSystemUsers(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage System Users")).click();
		ie.waitUntilReady();
		if (!ie.containsText("List Users"))
			throw new TestCaseFailedException();
		if (!ie.containsText("Filter"))
			throw new TestCaseFailedException();
		if (!ie.textField(id("nameFilter")).exists())
			throw new TestCaseFailedException();
		if (!ie.selectList(id("userType")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(id("userList_search")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(id("userList_clear")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Checks to see if the default user, as defined in the property file, has
	 * the extended feature of Integration. The type of display and features we
	 * need to test for will change depending on if this is set or not.
	 * 
	 * @return true if the property file had the tenant set for Integration
	 *         enabled
	 */
	public boolean hasIntegration() {
		for (int i = 0; i < extendedFeatures.length; i++) {
			if (extendedFeatures[i].equalsIgnoreCase("INTEGRATION"))
				return true;
		}
		return false;
	}

	/**
	 * Checks to see if the default user, as defined in the property file, has
	 * the extended feature of Compliance. The type of display and features we
	 * need to test for will change depending on if this is set or not.
	 * 
	 * @return true if property file had Compliance set for the tenant
	 */
	public boolean hasCompliance() {
		for (int i = 0; i < extendedFeatures.length; i++) {
			if (extendedFeatures[i].equalsIgnoreCase("COMPLIANCE"))
				return true;
		}
		return false;
	}

	/**
	 * Checks to see if the default user, as defined in the property file, has
	 * the extended feature of Job Sites. The type of display and features we
	 * need to test for will change depending on if this is set or not.
	 * 
	 * @return true if property file had Jobsites set for the tenant
	 */
	public boolean hasJobsites() {
		for (int i = 0; i < extendedFeatures.length; i++) {
			if (extendedFeatures[i].equalsIgnoreCase("JOBSITES"))
				return true;
		}
		return false;
	}

	/**
	 * This method sets focus on the Smart Search text field on the Home page of
	 * Field ID. If the text box cannot be located this method will return
	 * false.
	 * 
	 * If the text box is found, this method will return true. There is no way
	 * to determine whether or not the text box actually received focus.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if we can click in the Smart Search text box on the Home
	 *         page
	 * @throws Exception
	 */
	public boolean gotoHomeSmartSearchTextBox(IE ie) throws Exception {
		gotoHome(ie);
		TextField ss = ie.textField(id("searchText"));
		if (ss.exists()) {
			ss.click();
			return true;
		}
		return false;
	}

	/**
	 * This method will set the focus on the text box then enter the text
	 * provide in the keys input. It does not submit the form.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param text
	 *            - the text to be typed in the Smart Search box. See Microsoft
	 *            Sendkeys function.
	 * @throws Exception
	 */
	public void setHomeSmartSearchTextBox(IE ie, String text) throws Exception {
		if (gotoHomeSmartSearchTextBox(ie)) {
			ie.sendKeys(text);
		}
	}

	/**
	 * Checks for the logout link and clicks it. Also confirms we are at the
	 * login screen with done. Requires as input the IE session we are currently
	 * logged into.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void logout(IE ie) throws Exception {
		ie.link(text("Logout")).click(); // Logout
		ie.waitUntilReady();
	}

	/**
	 * Most customers will log into a page with a branded display. The page
	 * should contain a logo image and not require the user to input a company
	 * ID. Otherwise this login is the same as the generic regular login.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param userID
	 * @param password
	 * @param remember
	 *            - if true causes web browser to remember the userID via a
	 *            cookie
	 * @throws Exception
	 */
	public void loginBrandedRegular(IE ie, String userID, String password,
			boolean remember) throws Exception {
		loginCommonRegular(ie, userID, password, remember);
	}

	/**
	 * Like the regular branded login, this page requires an RFID security card
	 * value. It uses the RFID value rather than user name and password to log
	 * into the systems.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param companyID
	 * @param securityRFID
	 * @param remember
	 *            - if true causes web browser to remember the userID via a
	 *            cookie
	 * @throws Exception
	 */
	public void loginBrandedSecurityCard(IE ie, String companyID,
			String securityRFID, boolean remember) throws Exception {
		loginCommonSecurityCard(ie, companyID, securityRFID, remember);
	}

	/**
	 * Similar to the brandedRegularLogin but this assumes you want to log in
	 * with the default information from the property file.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param remember
	 *            - if true causes web browser to remember the userID via a
	 *            cookie
	 * @throws Exception
	 */
	public void loginBrandedDefaultRegular(IE ie, boolean remember)
			throws Exception {
		loginBrandedRegular(ie, getUserName(), getPassword(), remember);
	}

	private void loginCommonSecurityCard(IE ie, String companyID,
			String securityRFID, boolean remember) throws Exception {
		ie.link("Security Card").click();
		ie.textField(name("secureRfidNumber")).set(securityRFID);
		ie.checkbox(id("logIntoSystem_rememberMe")).set(remember);
		ie.button(id("loginButton")).click();
		ie.waitUntilReady();
	}

	private void loginCommonRegular(IE ie, String userID, String password,
			boolean remember) throws Exception {
		ie.textField(name("userName")).set(userID);
		ie.textField(name("password")).set(password);
		ie.checkbox(id("logIntoSystem_rememberMe")).set(remember);
		ie.button(id("loginButton")).click();
		ie.waitUntilReady();
	}

	/**
	 * When the session times out, if you attempt to do anything a lightbox will
	 * appear prompting you for your password. This method will enter the given
	 * password to the lightbox. It assumes the lightbox exists and it open. If
	 * it is not this will throw an exception.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param password
	 * @throws Exception
	 */
	public void loginSessionTimeLightbox(IE ie, String password)
			throws Exception {
		ie.textField(id("password")).set(password);
		ie.button(id("loginButton")).click();
		Thread.sleep(lightbox); // wait for the dialog to disappear
		if (ie.button(id("sessionTimeOutClose")).exists()) {
			ie.button(id("sessionTimeOutClose")).click();
		}
	}

	/**
	 * Check to see if the session timeout lightbox has opened. If the session
	 * has timed out and the lightbox is prompting the user for a password, this
	 * method will return true.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if session timeout lightbox is open
	 * @throws Exception
	 */
	public boolean isSessionTimeoutLightboxOpen(IE ie) throws Exception {
		boolean result = true;
		if (!ie.containsText("Were sorry, your session has expired."))
			result = false;
		if (!ie
				.containsText("Please enter your password below to log back in."))
			result = false;
		if (!ie.form(id("quickLoginForm")).exists())
			result = false;

		return result;
	}

	/**
	 * Creates a PNG format screen capture of the current Window. This is useful
	 * for saving the window image of IE during testing. It is recommended to
	 * the getCaptureName method. This will generate a filename based on date
	 * and tenant. Therefore on multiple runs or multiple captures for one
	 * tenant you will not overwrite existing images.
	 * 
	 * @param filename
	 *            - the filename the PNG image will be saved to
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void myWindowCapture(String filename, IE ie) throws Exception {
		myWindowCapture(filename, ie, "png");
	}

	/**
	 * Creates a window capture and saves it to a file. The format parameter
	 * must be one of the supported formats for the ImageIO class. To get a list
	 * of the supported formats, look at the String[] returned by
	 * ImageIO.getReaderFormatNames().
	 * 
	 * Currently supported formats are:
	 * 
	 * BMP bmp jpeg wbmp gif png JPG jpg JPEG WBMP
	 * 
	 * @param filename
	 *            - the filename the image will be saved to
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param format
	 *            - one of bmp, gif, jpeg, jpg, png, wbmp (case-sensitive)
	 * @throws Exception
	 */
	public void myWindowCapture(String filename, IE ie, String format)
			throws Exception {
		int x = ie.top();
		int y = ie.left();
		int h = ie.height();
		int w = ie.width();
		Rectangle r = new Rectangle(x, y, w, h);
		BufferedImage image = (new Robot()).createScreenCapture(r);
		// If there is a directory path, make sure it exists
		int endIndex = filename.lastIndexOf('/');
		if (endIndex != -1) {
			String dirname = filename.substring(0, endIndex);
			File dir = new File(dirname);
			if (!dir.exists())
				dir.mkdirs();
		}
		ImageIO.write(image, format, new File(filename));
	}

	/**
	 * If true the user is an end user, e.g. customer of a tenant. As an end
	 * user they will have limited abilities. For example, They will not be able
	 * to go to the Administration page.
	 * 
	 * The enduser setting defaults to false. This is currently read from the
	 * properties file.
	 * 
	 * @return true if the user is a customer user, false if they are an
	 *         employee user
	 */
	public boolean getEndUser() {
		return enduser;
	}

	/**
	 * Set the end user value in memory. All subsequent calls to getEndUser will
	 * return the new value.
	 * 
	 * @param enduser
	 */
	public void setEndUser(boolean enduser) {
		this.enduser = enduser;
	}

	/**
	 * This is an array of extended features. Currently there are three extended
	 * features:
	 * 
	 * Integration The user will have integration with their ordering systems.
	 * This means that the default page for login will be Product Identify
	 * rather than Add Product. Additionally, in the Administration page they
	 * will have options for setting up product code mappings. Compliance The
	 * user will have a Compliance icon at the top of all pages. JobSites Rather
	 * than having customer and division they will have a job site field for
	 * products. In the Administration section they will have a Manage Job Sites
	 * option.
	 * 
	 * If not configured this will return an empty array.
	 * 
	 * @return an array of extended features as defined in the property file
	 */
	public String[] getExtendedFeatures() {
		return extendedFeatures;
	}

	/**
	 * Returns an array of all the users permissions. There are currently six
	 * permissions available at the moment. They are:
	 * 
	 * tag Has the ability to tag product (add product) config Has the ability
	 * to manage system configuration (everything not sysusers + users) sysusers
	 * Has the ability to manage system users (Manage System Users & Manage User
	 * Registration Request) users Has the ability to manage end users (Manage
	 * Customers) create Has the ability to create inspections edit Has the
	 * ability to edit inspections
	 * 
	 * The permissions for a user are visible from Administration, Manage System
	 * Users. This will put you on the Edit User page. Alternately, from
	 * Administration, Manage Customers, Edit Customer, select a User ID from
	 * Edit Customers. This will also put you on the Edit User page.
	 * 
	 * @return array of user permissions as defined in the property file.
	 */
	public String[] getPermissions() {
		return permissions;
	}

	/**
	 * This method will just replace the existing array of extended features. It
	 * will not append a new set of features to an existing array of features.
	 * 
	 * @param ef
	 *            - an array of extended features
	 */
	public void setExtendedFeatures(String[] ef) {
		this.extendedFeatures = ef;
	}

	/**
	 * Returns an array of all the users order numbers. These are String values
	 * because customers might have alphanumeric order numbers, i.e. don't
	 * assume these order 'numbers' are numbers.
	 * 
	 * @return an array of order numbers as defined ni the property file.
	 */
	public String[] getOrderNumbers() {
		return orderNumbers;
	}

	/**
	 * This class can be initialized with a set of known order numbers for the
	 * tenant. This method will return one order number from the array of order
	 * number.
	 * 
	 * If the index input is larger than the number of order numbers available
	 * it will return an empty string. Otherwise it will return an order number.
	 * 
	 * @param index
	 *            - the index of the order number in an array
	 * @return an order from the array of orders read in by property file
	 */
	public String getOrderNumber(int index) {
		if (orderNumbers.length > index)
			return orderNumbers[index];
		return "";
	}

	/**
	 * This method will replace the existing array of user permissions. It will
	 * not append. If you need to append, get the permissions, add the new
	 * permission to the array and return the new array to this method.
	 * 
	 * @param pm
	 *            - set of user permissions
	 */
	public void setPermissions(String[] pm) {
		this.permissions = pm;
	}

	/**
	 * Get the default user name as defined in the properties file.
	 * 
	 * @return String - default user id
	 */
	public String getUserName() {
		String result = p.getProperty("username", "");
		return result;
	}

	/**
	 * Set the value for user name in memory. All subsequent calls to
	 * getUserName will return the new value.
	 * 
	 * @param value
	 *            - user id
	 */
	public void setUserName(String value) {
		p.setProperty("username", value);
	}

	/**
	 * Get the default user password as defined in the properties file.
	 * 
	 * @return password
	 */
	public String getPassword() {
		String result = p.getProperty("password", "");
		return result;
	}

	/**
	 * Set the value for user password in memory. All subsequent calls to
	 * getPassword will return the new value.
	 * 
	 * @param value
	 *            - password
	 */
	public void setPassword(String value) {
		p.setProperty("password", value);
	}

	/**
	 * Get the default security card RFID as defined in the properties file.
	 * 
	 * @return security RFID value
	 */
	public String getRFID() {
		String result = p.getProperty("securitycard", "");
		return result;
	}

	/**
	 * Set the value for security card value in memory. All subsequent calls to
	 * getRFID will return the new value.
	 * 
	 * @param value
	 *            - security RFID value
	 */
	public void setRFID(String value) {
		p.setProperty("securitycard", value);
	}

	/**
	 * Get the base URL to Field ID as defined in the properties file. This is
	 * typically https://team.n4systems.net/fieldid/ or
	 * https://192.168.2.44/fieldid/ but it can also be localhost or
	 * https://www.fieldid.com/
	 * 
	 * If the base URL was not set during initialization, this method will
	 * default to the testing server 192.168.2.44.
	 * 
	 * @return base URL for Field ID web site
	 */
	public String getBaseURL() {
		String result = p.getProperty("baseurl",
				"https://192.168.2.44/fieldid/");
		return result;
	}

	/**
	 * Set the value for the base URL in memory. All subsequent calls to
	 * getBaseURL will return the new value.
	 * 
	 * This is typically https://team.n4systems.net/fieldid/ or
	 * https://192.168.2.44/fieldid/ but it can also be localhost or
	 * https://www.fieldid.com/
	 * 
	 * @param value
	 *            - base URL for Field ID web site
	 */
	public void setBaseURL(String value) {
		p.setProperty("baseurl", value);
	}

	/**
	 * Get the default company ID as defined in the properties file.
	 * 
	 * Returns an empty string if not set.
	 * 
	 * @return company id
	 */
	public String getTenant() {
		String result = p.getProperty("tenant", "");
		return result;
	}

	/**
	 * Set the value for company ID in memory. All subsequent calls to getTenant
	 * will return the new value.
	 * 
	 * @param value
	 *            - company id
	 */
	public void setTenant(String value) {
		p.setProperty("tenant", value);
	}

	/**
	 * Get the company branded URL as defined in the properties file. This
	 * method uses the getTenant method to build the branded URL. Typically this
	 * is https://team.n4systems.net/fieldid/login/tenant
	 * 
	 * @return a typical login URL
	 */
	public String getLoginURL() {
		String result = getBaseURL();
		String tenant = getTenant();
		if (!result.equals("") && !tenant.equals("")) {
			result += "login.action?companyID=";
			result += tenant;
		}
		return result;
	}

	/**
	 * The branded login should have an image of the company on it. This method
	 * will return the full URL to that image.
	 * 
	 * @return URL to the company logo from the login page
	 */
	public String getLogo() {
		String result = "";
		String logo = p.getProperty("logo", "NOT SET");
		if (getTenant().length() > 0 && !logo.equals("")) {
			result = getBaseURL();
			result += logo;
			result += getTenant();
		}
		return result;
	}

	private String getTenantFeatures() {
		String result = "";
		if (getTenant().length() > 0)
			result = p.getProperty(getTenant(), "");
		return result;
	}

	/**
	 * Selects the Product Search page and confirms all the inputs present.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void goToSearch(IE ie, boolean jobsites) throws Exception {
		ie.link(id("menuAssets")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Product Search"))
			throw new TestCaseFailedException();

		if (!ie.textField(id("reportForm_criteria_rfidNumber")).exists()) // RFID
																			// Number
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_serialNumber")).exists()) // Serial
																			// Number
																			// or
																			// Reel/ID
			throw new TestCaseFailedException();
		if (!ie.selectList(id("reportForm_criteria_productStatus")).exists()) // Product
																				// Status
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_salesAgent")).exists()) // Sales
																			// Agent
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_orderNumber")).exists()) // Order
																			// Number
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_purchaseOrder")).exists()) // Purchase
																				// Order
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_location")).exists()) // Location
			throw new TestCaseFailedException();
		if (!ie.selectList(id("reportForm_criteria_productType")).exists()) // Product
																			// Type
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_fromDate")).exists()) // From Date
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_toDate")).exists()) // To Date
			throw new TestCaseFailedException();
	}

	/**
	 * This method will set all the fields on the Product Search page, submit
	 * the form and confirm we got a "Product Search - Results" page.
	 * 
	 * If the input is null this method will not alter the input. This will
	 * simulate the form remembering the last search criteria.
	 * 
	 * If you want to clear an input then you should be able to input a null
	 * string, i.e. "", and it will clear the selection.
	 * 
	 * The fromDate and toDate fields are not implemented. If you give anything
	 * other than null from these two inputs the method will throw a
	 * NotImplementedYetException.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfidNumber
	 * @param serialNumber
	 * @param productStatus
	 * @param salesAgent
	 * @param orderNumber
	 * @param purchaseOrder
	 * @param jobSite
	 * @param assignedTo
	 * @param customer
	 * @param division
	 * @param location
	 * @param productType
	 * @param fromDate
	 * @param toDate
	 * @throws Exception
	 */
	public void productSearch(IE ie, String rfidNumber, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {
		goToSearch(ie, false);
		if (rfidNumber != null)
			ie.textField(id("reportForm_criteria_rfidNumber")).set(rfidNumber);
		if (serialNumber != null)
			ie.textField(id("reportForm_criteria_serialNumber")).set(
					serialNumber);
		if (productStatus != null
				&& ie.selectList(id("reportForm_criteria_productStatus"))
						.option(text(productStatus)).exists())
			ie.selectList(id("reportForm_criteria_productStatus")).option(
					text(productStatus)).select();
		if (salesAgent != null)
			ie.textField(id("reportForm_criteria_salesAgent")).set(salesAgent);
		if (orderNumber != null)
			ie.textField(id("reportForm_criteria_orderNumber"))
					.set(orderNumber);
		if (purchaseOrder != null)
			ie.textField(id("reportForm_criteria_purchaseOrder")).set(
					purchaseOrder);
		if (jobSite != null
				&& ie.selectList(id("reportForm_criteria_jobSite")).exists()
				&& ie.selectList(id("reportForm_criteria_jobSite")).option(
						text(jobSite)).exists())
			ie.selectList(id("reportForm_criteria_jobSite")).option(
					text(jobSite)).select();
		if (assignedTo != null
				&& ie.selectList(id("reportForm_criteria_assingedUser"))
						.exists()
				&& ie.selectList(id("reportForm_criteria_assingedUser"))
						.option(text(assignedTo)).exists())
			ie.selectList(id("reportForm_criteria_assingedUser")).option(
					text(assignedTo)).select();
		setCustomerOnProductSearch(ie, customer);
		if (division != null
				&& ie.selectList(id("division")).option(text(division)).text()
						.equals(division))
			ie.selectList(id("division")).option(text(division)).select();
		if (location != null)
			ie.textField(id("reportForm_criteria_location")).set(location);
		if (productType != null
				&& ie.selectList(id("reportForm_criteria_productType")).option(
						text(productType)).exists())
			ie.selectList(id("reportForm_criteria_productType")).option(
					text(productType)).select();
		if (fromDate != null)
			throw new NotImplementedYetException();
		if (toDate != null)
			throw new NotImplementedYetException();
		ie.form(id("reportForm")).submit();
		ie.waitUntilReady();
		if (!ie.containsText("Product Search Results"))
			throw new TestCaseFailedException();
	}

	private void setCustomerOnProductSearch(IE ie, String customer)
			throws Exception {
		if (customer != null
				&& ie.selectList(id("reportForm_criteria_customer")).option(
						text(customer)).exists())
			ie.selectList(id("reportForm_criteria_customer")).option(
					text(customer)).select();
		Thread.sleep(hack);
	}

	/**
	 * This method will return a unique filename for the windowCapture method.
	 * Each time you call the method it will update the filename based on the
	 * date and tenant. The format for the filename will be
	 * 
	 * yyyyMMdd-HHmmss-tenant.fmt
	 * 
	 * where:
	 * 
	 * yyyy is the current year MM is the month [01-12] dd is the day of the
	 * month [1-31] HH is the hour of the day [0-23] mm is the minute of the
	 * hour [0-59] ss is the second of the minute [0-59] tenant is the company
	 * ID fmt is the format of the capture file [png, jpg, bmp, gif]
	 * 
	 * @return filename in the format yyyyMMdd-HHmmss-tenant.fmt
	 */
	public String getCaptureName(String format) {
		String result;

		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd-HHmmss");
		result = date.format(new Date());
		result += "-";
		result += getTenant();
		result += ".";
		result += format;
		return result;
	}

	/**
	 * Validate the first page you see after login. The page you see depends on
	 * the extended features your user has set.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param hasCompliance
	 * @param hasJobSites
	 * @param hasIntegration
	 * @return true if page validates
	 * @throws Exception
	 * @deprecated login page is always Home now.
	 */
	public boolean validateIdentifyPage(IE ie, boolean hasCompliance,
			boolean hasJobSites, boolean hasIntegration) throws Exception {
		boolean result = true;

		// There should be a link to Compliance at the top of the page
		if (hasCompliance && !ie.containsText("Compliance"))
			result = false;

		// if they have integration they start at the Identify Products page
		if (hasIntegration) {
			if (!ie.containsText("Identify Products"))
				result = false;
		}
		// else they start at the Add Product page
		else {
			if (!ie.containsText("Add Product"))
				result = false;
		}

		// if they have job sites and no integration, there should be a
		// "Job Site:" field
		if (hasJobSites && !hasIntegration) {
			if (!ie.containsText("Job Site:"))
				result = false;
			if (!ie.selectList(name("jobSite")).exists())
				result = false;
		}

		return result;
	}

	/**
	 * Check the page to see if the correct logo is present and the Company ID
	 * field is not. Currently, I go to the page, verify the Company ID is not
	 * present, display the logo in a separate window and do a window capture of
	 * it. Someone will have to manually check the logo to see it is correct.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param companyID
	 * @throws Exception
	 */
	public void validateBrandedLoginPage(IE ie, String companyID)
			throws Exception {

		if (ie.textField(name("manID")).exists())
			throw new TestCaseFailedException();
		if (ie.textField(id("manID1")).exists())
			throw new TestCaseFailedException();
		if (ie.containsText("Company ID"))
			throw new TestCaseFailedException();

		HtmlElement h1 = ie.htmlElement(tag("h1"));
		if (!h1.exists() || !h1.text().equals("Login"))
			throw new TestCaseFailedException();
		Label l = ie.label(text(companyID));
		if (!l.exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Select the link for the "Forgot Your Password?". Assumes you are on the
	 * main login screen.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoForgotYourPassword(IE ie) throws Exception {
		ie.link(href, "/emailpassword.jsp/").click();
		ie.waitUntilReady();
		if (!ie.containsText("Forgot Password"))
			throw new TestCaseFailedException();
	}

	/**
	 * After you have selected the Forgot Your Password? link, call this to
	 * submit a request for a new password. This code assumes you are already on
	 * the page for requesting a new password.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param companyID
	 * @param userID
	 * @throws Exception
	 */
	public void submitLostPasswordInformation(IE ie, String companyID,
			String userID) throws Exception {
		ie.textField(name("manID")).set(companyID);
		ie.textField(name("userID")).set(userID);
		ie.button("Send").click();
		ie.waitUntilReady();
	}

	/**
	 * Using the given order number, load all products associated with the
	 * order. If the orderNumber is not set this will do nothing.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param orderNumber
	 * @throws Exception
	 */
	public void loadOrderNumber(IE ie, String orderNumber) throws Exception {
		if (orderNumber == null || orderNumber.equals("")) {
			return;
		}
		ie.textField(name("orderNumber")).set(orderNumber);
		ie.button("Load").click();
		ie.waitUntilReady();
	}

	/**
	 * For tenants with Integration, if you load an order number from the
	 * Identify Products page, this method will validate the order came up okay.
	 * It will look for the text in the header of the table.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if Identify Products With Orders has orders.
	 */
	public boolean validateIdentifyProductsWithOrders(IE ie) throws Exception {
		boolean result = true;

		if (!ie.containsText("Product Type"))
			result = false;
		if (!ie.containsText("Description"))
			result = false;
		if (!ie.containsText("Product Code"))
			result = false;
		if (!ie.containsText("Order Quantity"))
			result = false;
		if (!ie.containsText("Tagged Quantity"))
			result = false;

		// if there are no links to orders, fail
		if (ie.links(url("/searchType=ordernumber/")).length() == 0)
			result = false;

		return result;

	}

	/**
	 * Validate the Home page. This method assume you are already on the Home
	 * page and just validates that the expected fields are present.
	 * 
	 * Currently it is checking for
	 * 
	 * The text 'Go To:' The text 'Smart Search:' The text 'Instructional
	 * Videos' The text 'New Features in'
	 * 
	 * The link 'View upcoming Inspections' The link 'View the Inspection
	 * History for a Product' The link 'Find a Product' The link 'Change your
	 * password'
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if everything we expect on the page is there
	 * @throws Exception
	 */
	public boolean validateHomePage(IE ie) throws Exception {
		boolean result = true;

		Link vui = ie.link(text("/View upcoming Inspections/"));
		Link vih = ie.link(text("/View the Inspection History for a Product/"));
		Link fap = ie.link(text("/Find a Product/"));
		Link cyp = ie.link(text("/Change your password/"));

		if (!ie.containsText("Go To:"))
			result = false;
		else if (!ie.containsText("Smart Search:"))
			result = false;
		else if (!ie.containsText("Instructional Videos"))
			result = false;
		else if (!ie.containsText("New Features in"))
			result = false;
		else if (!vui.exists())
			result = false;
		else if (!vih.exists())
			result = false;
		else if (!fap.exists())
			result = false;
		else if (!cyp.exists())
			result = false;

		return result;
	}

	/**
	 * Validate the Home page. This method assume you are already on the Home
	 * page. It will do everything the other validateHomePage method does but it
	 * will also visit the various links on the page and generate a snapshot of
	 * each page then return to the Home page. All snapshots will be store in
	 * the directory provided.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param directory
	 *            - directory to store screen captures
	 * @return true if everything we expect on the page is there
	 */
	public boolean validateHomePage(IE ie, String directory) throws Exception {
		boolean result = true;
		String filename = directory + "/" + getMethodName();

		Link vui = ie.link(text("/View upcoming Inspections/"));
		Link vih = ie.link(text("/View the Inspection History for a Product/"));
		Link fap = ie.link(text("/Find a Product/"));
		Link cyp = ie.link(text("/Change your password/"));

		validateHomePage(ie);

		String captureName = filename + "-" + vui.text().replace(' ', '_')
				+ ".png";
		vui.click();
		ie.waitUntilReady();
		myWindowCapture(captureName, ie);
		gotoHome(ie);

		captureName = filename + "-" + vih.text().replace(' ', '_') + ".png";
		vih.click();
		ie.waitUntilReady();
		myWindowCapture(captureName, ie);
		gotoHome(ie);

		captureName = filename + "-" + fap.text().replace(' ', '_') + ".png";
		fap.click();
		ie.waitUntilReady();
		myWindowCapture(captureName, ie);
		gotoHome(ie);

		captureName = filename + "-" + cyp.text().replace(' ', '_') + ".png";
		cyp.click();
		ie.waitUntilReady();
		myWindowCapture(captureName, ie);
		gotoHome(ie);

		return result;
	}

	/**
	 * Calls gotoInspectionGroupsForProduct and creates an array of inspect
	 * types for a given product. It is essentially going to the 'Manage
	 * Inspections' area and seeing what the different Inspection Types are
	 * listed on the Start New Inspection menu.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productID
	 *            - serial number
	 * @return array of inspection types
	 * @throws Exception
	 */
	public String[] getInspectionTypesForProduct(IE ie, String productID)
			throws Exception {
		gotoInspectionGroupsForProduct(ie, productID);
		Links links = ie.links();
		List<String> inspectionTypes = new ArrayList<String>();
		for (int i = 0; i < links.length(); i++) {
			Link link = links.get(i);
			if (link.href().indexOf("inspectionAdd.action?inspectionGroupId=&") != -1)
				inspectionTypes.add(link.text());
		}

		return inspectionTypes.toArray(new String[0]);
	}

	/**
	 * Returns a String array of product id values from the first page of the
	 * search result.
	 * 
	 * If any parameter is null it will not set that field on the search form.
	 * If you want to clear the value on a form then you should set the value to
	 * an empty string, e.g. "".
	 * 
	 * It is recommended to use this if you want to get some product value. It
	 * will be faster than getting a full list of all products then selecting
	 * one. Additionally, a real test scenario is to go to product search, look
	 * at the first page and grab a product.
	 * 
	 * If the result set is empty this will return null.
	 * 
	 * This method will return any product. Subproducts, master products and
	 * other products behave differently. If you care about these differences
	 * don't use this method.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return array of serial numbers from first page of search results
	 * @throws Exception
	 */
	public String[] getProductsFirstPage(IE ie, String rfid,
			String serialNumber, String productStatus, String salesAgent,
			String orderNumber, String purchaseOrder, String jobSite,
			String assignedTo, String customer, String division,
			String location, String productType, String fromDate, String toDate)
			throws Exception {

		gotoProductSearchResults(ie, rfid, serialNumber, productStatus,
				salesAgent, orderNumber, purchaseOrder, jobSite, assignedTo,
				customer, division, location, productType, fromDate, toDate);

		List<String> result = getProductsFromSearchResultsCurrentPage(ie);
		if (result.size() == 0)
			return null;
		else
			return result.toArray(new String[0]);
	}

	/**
	 * Returns a String array of subproduct id values from the first page of the
	 * search result.
	 * 
	 * If any parameter is null it will not set that field on the search form.
	 * If you want to clear the value on a form then you should set the value to
	 * an empty string, e.g. "".
	 * 
	 * It is recommended to use this if you want to get some product value. It
	 * will be faster than getting a full list of all products then selecting
	 * one. Additionally, a real test scenario is to go to product search, look
	 * at the first page and grab a product.
	 * 
	 * If the result set is empty this will return null.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return array of serial numbers from first page of search results
	 * @throws Exception
	 */
	public String[] getSubProductsFirstPage(IE ie, String rfid,
			String serialNumber, String productStatus, String salesAgent,
			String orderNumber, String purchaseOrder, String jobSite,
			String assignedTo, String customer, String division,
			String location, String productType, String fromDate, String toDate)
			throws Exception {

		gotoProductSearchResults(ie, rfid, serialNumber, productStatus,
				salesAgent, orderNumber, purchaseOrder, jobSite, assignedTo,
				customer, division, location, productType, fromDate, toDate);

		List<String> result = getSubProductsFromSearchResultsCurrentPage(ie);
		if (result.size() == 0)
			return null;
		else
			return result.toArray(new String[0]);
	}

	/**
	 * Returns a String array of master product id values from the first page of
	 * the search result.
	 * 
	 * If any parameter is null it will not set that field on the search form.
	 * If you want to clear the value on a form then you should set the value to
	 * an empty string, e.g. "".
	 * 
	 * It is recommended to use this if you want to get some product value. It
	 * will be faster than getting a full list of all products then selecting
	 * one. Additionally, a real test scenario is to go to product search, look
	 * at the first page and grab a product.
	 * 
	 * If the result set is empty this will return null.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return array of serial numbers from first page of search results
	 * @throws Exception
	 */
	public String[] getMasterProductsFirstPage(IE ie, String rfid,
			String serialNumber, String productStatus, String salesAgent,
			String orderNumber, String purchaseOrder, String jobSite,
			String assignedTo, String customer, String division,
			String location, String productType, String fromDate, String toDate)
			throws Exception {

		gotoProductSearchResults(ie, rfid, serialNumber, productStatus,
				salesAgent, orderNumber, purchaseOrder, jobSite, assignedTo,
				customer, division, location, productType, fromDate, toDate);

		List<String> result = getMasterProductsFromSearchResultsCurrentPage(ie);
		if (result.size() == 0)
			return null;
		else
			return result.toArray(new String[0]);
	}

	/**
	 * After a search result call, i.e. gotoProductSearchResults, you can call
	 * this method to change the columns which will be displayed. Currently,
	 * there are more columns which are possible. The web code will determine
	 * what attributes are common for all the results. For example, if all the
	 * results are chain sling then it will show the attributes for all chain
	 * sling. Still trying to figure out how to support this.
	 * 
	 * The columns I am checking for (see list of parameters) are technically
	 * configurable as well. Therefore, I currently check too see if the column
	 * exists. If the column does not exist, this method will return false. Any
	 * test case which is using this should check to see if it returns false. If
	 * it does then this code needs to be cleaned up.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 *            - input to the form
	 * @param rfidNumber
	 *            - input to the form
	 * @param customerName
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param organization
	 *            - input to the form
	 * @param refNumber
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param lastInspectionDate
	 *            - input to the form
	 * @param identified
	 *            - input to the form
	 * @param identifiedBy
	 *            - input to the form
	 * @param description
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param orderDescription
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @return false if a column could not be set
	 * @throws Exception
	 */
	public boolean setProductSearchResultsColumns(IE ie, boolean serialNumber,
			boolean rfidNumber, boolean customerName, boolean division,
			boolean organization, boolean refNumber, boolean productType,
			boolean productStatus, boolean lastInspectionDate,
			boolean identified, boolean identifiedBy, boolean description,
			boolean location, boolean orderDescription, boolean orderNumber,
			boolean purchaseOrder) throws Exception {

		boolean result = true;

		expandSelectColumns(ie);
		
		if (ie.checkbox(id("chk_product_search_serialnumber")).exists()) {
			ie.checkbox(id("chk_product_search_serialnumber")).set(serialNumber);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_rfidnumber")).exists()) {
			ie.checkbox(id("chk_product_search_rfidnumber")).set(rfidNumber);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_customer")).exists()) {
			ie.checkbox(id("chk_product_search_customer")).set(customerName);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_division")).exists()) {
			ie.checkbox(id("chk_product_search_division")).set(division);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_organization")).exists()) {
			ie.checkbox(id("chk_product_search_organization")).set(organization);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_referencenumber")).exists()) {
			ie.checkbox(id("chk_product_search_referencenumber")).set(refNumber);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_producttype")).exists()) {
			ie.checkbox(id("chk_product_search_producttype")).set(productType);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_productstatus")).exists()) {
			ie.checkbox(id("chk_product_search_productstatus")).set(productStatus);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_lastinspdate")).exists()) {
			ie.checkbox(id("chk_product_search_lastinspdate")).set(lastInspectionDate);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_identified")).exists()) {
			ie.checkbox(id("chk_product_search_identified")).set(identified);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_identifiedby")).exists()) {
			ie.checkbox(id("chk_product_search_identifiedby")).set(identifiedBy);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_description")).exists()) {
			ie.checkbox(id("chk_product_search_description")).set(description);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_location")).exists()) {
			ie.checkbox(id("chk_product_search_location")).set(location);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_order_description")).exists()) {
			ie.checkbox(id("chk_product_search_order_description")).set(orderDescription);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_order_number")).exists()) {
			ie.checkbox(id("chk_product_search_order_number")).set(orderNumber);
		} else {
			result = false;
		}
		if (ie.checkbox(id("chk_product_search_purchaseorder")).exists()) {
			ie.checkbox(id("chk_product_search_purchaseorder")).set(purchaseOrder);
		} else {
			result = false;
		}

		ie.button(id("reportForm_label_Run")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);

		return result;
	}

	/**
	 * This method will generate search results based on the inputs provided. It
	 * will go to the Assets page automatically.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @throws Exception
	 */
	public void gotoProductSearchResults(IE ie, String rfid,
			String serialNumber, String productStatus, String salesAgent,
			String orderNumber, String purchaseOrder, String jobSite,
			String assignedTo, String customer, String division,
			String location, String productType, String fromDate, String toDate)
			throws Exception {

		gotoAssets(ie);

		if (rfid != null)
			ie.textField(id("reportForm_criteria_rfidNumber")).set(rfid);
		if (serialNumber != null)
			ie.textField(id("reportForm_criteria_serialNumber")).set(
					serialNumber);
		if (productStatus != null)
			ie.selectList(id("reportForm_criteria_productStatus")).option(
					text(productStatus)).select();
		if (salesAgent != null)
			ie.textField(id("reportForm_criteria_salesAgent")).set(salesAgent);
		if (orderNumber != null)
			ie.textField(id("reportForm_criteria_orderNumber"))
					.set(orderNumber);
		if (purchaseOrder != null)
			ie.textField(id("reportForm_criteria_purchaseOrder")).set(
					purchaseOrder);
		if (jobSite != null
				&& ie.selectList(id("reportForm_criteria_jobSite")).exists()
				&& ie.selectList(id("reportForm_criteria_jobSite")).option(
						text(jobSite)).exists())
			ie.selectList(id("reportForm_criteria_jobSite")).option(
					text(jobSite)).select();
		if (assignedTo != null
				&& ie.selectList(id("reportForm_criteria_assingedUser"))
						.exists()
				&& ie.selectList(id("reportForm_criteria_assingedUser"))
						.option(text(assignedTo)).exists())
			ie.selectList(id("reportForm_criteria_assingedUser")).option(
					text(assignedTo)).select();
		if (customer != null) {
			ie.selectList(id("reportForm_criteria_customer")).option(
					text(customer)).select();
			Thread.sleep(hack); // selecting customer updates division, need to
								// wait for that
		}
		ie.waitUntilReady();
		if (division != null)
			ie.selectList(id("division")).option(text(division)).select();
		if (location != null)
			ie.textField(id("reportForm_criteria_location")).set(location);
		if (productType != null)
			ie.selectList(id("reportForm_criteria_productType")).option(
					text(productType)).select();
		if (fromDate != null)
			ie.textField(id("reportForm_fromDate")).set(fromDate);
		if (toDate != null)
			ie.textField(id("reportForm_toDate")).set(toDate);

		ie.button(id("reportForm_label_Run")).click();
		ie.waitUntilReady();
	}

	/**
	 * This method will go to the Schedule page and generate search results
	 * based on the inputs provided.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionType
	 *            - input to the form
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @throws Exception
	 */
	public void gotoScheduleSearchResults(IE ie, String inspectionType,
			String rfid, String serialNumber, String productStatus,
			String salesAgent, String orderNumber, String purchaseOrder,
			String jobSite, String assignedTo, String customer,
			String division, String location, String productType,
			String fromDate, String toDate) throws Exception {
		gotoSchedule(ie);

		if (inspectionType != null)
			ie.selectList(id("reportForm_criteria_inspectionType")).option(
					text(inspectionType)).select();
		if (rfid != null)
			ie.textField(id("reportForm_criteria_rfidNumber")).set(rfid);
		if (serialNumber != null)
			ie.textField(id("reportForm_criteria_serialNumber"))
					.set(serialNumber);
		if (productStatus != null)
			ie.selectList(id("reportForm_criteria_productStatus")).option(
					text(productStatus)).select();
		if (salesAgent != null)
			ie.textField(id("reportForm_criteria_salesAgent")).set(salesAgent);
		if (orderNumber != null)
			ie.textField(id("reportForm_criteria_orderNumber")).set(orderNumber);
		if (purchaseOrder != null)
			ie.textField(id("reportForm_criteria_purchaseOrder")).set(
					purchaseOrder);
		if (jobSite != null
				&& ie.selectList(id("reportForm_criteria_jobSite")).exists()
				&& ie.selectList(id("reportForm_criteria_jobSite")).option(
						text(jobSite)).exists())
			ie.selectList(id("reportForm_criteria_jobSite"))
					.option(text(jobSite)).select();
		if (assignedTo != null
				&& ie.selectList(id("reportForm_criteria_assingedUser")).exists()
				&& ie.selectList(id("reportForm_criteria_assingedUser")).option(
						text(assignedTo)).exists())
			ie.selectList(id("reportForm_criteria_assingedUser")).option(
					text(assignedTo)).select();
		if (customer != null) {
			ie.selectList(id("reportForm_criteria_customer")).option(
					text(customer)).select();
			Thread.sleep(hack); // selecting customer updates division, need to
								// wait for that
		}
		ie.waitUntilReady();
		if (division != null)
			ie.selectList(id("division")).option(text(division)).select();
		// if(location != null)
		// ie.textField(id("reportForm_criteria_location")).set(location);
		if (productType != null)
			ie.selectList(id("reportForm_criteria_productType")).option(
					text(productType)).select();
		if (fromDate != null)
			ie.textField(id("reportForm_fromDate")).set(fromDate);
		if (toDate != null)
			ie.textField(id("reportForm_toDate")).set(toDate);

		ie.button(id("reportForm_label_Run")).click();
		ie.waitUntilReady();
	}

	/**
	 * Validates the contents of the Edit Product page. Assumes you are on the
	 * Edit Product page for a given product. Throws an exception if anything
	 * fails.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param product
	 *            - the serial number of the product we are editing
	 * @param jobSites
	 *            - set to true if the company has job sites enabled
	 * @param integration
	 *            - set to true if the company has integration enabled
	 * @param subProduct
	 *            - set to true if the product is a subproduct
	 * @throws Exception
	 */
	public void validateEditProductPage(IE ie, String product,
			boolean jobSites, boolean integration, boolean subProduct)
			throws Exception {
		ie.waitUntilReady();
		if (!ie.containsText("Edit Product - " + product))
			throw new TestCaseFailedException();
		else if (!ie.textField(id("serialNumberText")).exists()) // serial
																	// number
			throw new TestCaseFailedException();
		else if (!ie.link(text("generate")).exists()) // generate serial number
			throw new TestCaseFailedException();
		else if (!ie.textField(id("rfidNumber")).exists()) // RFID
			throw new TestCaseFailedException();
		else if (!ie.textField(id("productUpdate_purchaseOrder")).exists()) // purchase
																			// order
			throw new TestCaseFailedException();
		else if (jobSites && !subProduct
				&& !ie.selectList(id("productUpdate_jobSite")).exists()) // job
																			// site
			throw new TestCaseFailedException();
		else if (jobSites && !subProduct
				&& !ie.selectList(id("productUpdate_assignedUser")).exists()) // assigned
																				// to
			throw new TestCaseFailedException();
		else if (!jobSites
				&& ie.selectList(id("productUpdate_jobSite")).exists()) // job
																		// site
			throw new TestCaseFailedException();
		else if (!jobSites
				&& ie.selectList(id("productUpdate_assignedUser")).exists()) // assigned
																				// to
			throw new TestCaseFailedException();
		else if (jobSites
				&& ie.selectList(id("reportForm_criteria_customer")).exists()) // customer
			throw new TestCaseFailedException();
		else if (jobSites && ie.selectList(id("division")).exists()) // division
			throw new TestCaseFailedException();
		else if (!jobSites && !subProduct
				&& !ie.selectList(id("customer")).exists()) // customer
			throw new TestCaseFailedException();
		else if (!jobSites && !subProduct
				&& !ie.selectList(id("division")).exists()) // division
			throw new TestCaseFailedException();
		else if (!subProduct && !ie.textField(id("location")).exists()) // location
			throw new TestCaseFailedException();
		else if (!ie.selectList(id("productUpdate_productStatus")).exists()) // product
																				// status
			throw new TestCaseFailedException();
		else if (!ie.textField(id("customerRefNumber")).exists()) // reference
																	// number
			throw new TestCaseFailedException();
		else if (!ie.textField(id("identified")).exists()) // identified
			throw new TestCaseFailedException();
		else if (!ie.selectList(id("productType")).exists()) // product type
			throw new TestCaseFailedException();
		else if (!ie.textField(id("comments")).exists()) // comments
			throw new TestCaseFailedException();
		else if (!ie.selectList(id("productUpdate_commentTemplate")).exists()) // comment
																				// template
			throw new TestCaseFailedException();
		else if (!ie.button(name("delete")).exists()) // delete button
			throw new TestCaseFailedException();
		else if (!ie.button(name("cancel")).exists()) // cancel
			throw new TestCaseFailedException();
		else if (!ie.button(id("saveButton")).exists()) // save
			throw new TestCaseFailedException();
	}

	/**
	 * Go to the Edit Product page for a given product serial number.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param product
	 *            - serial number
	 * @throws Exception
	 */
	public void gotoEditProduct(IE ie, String product) throws Exception {
		gotoProductSearchResults(ie, null, product, null, null, null, null,
				null, null, null, null, null, null, null, null);
		ie.link(text("/Info/")).click();
		ie.waitUntilReady();
		if (ie.link(text("Edit Product")).exists()) {
			ie.link(text("Edit Product")).click();
			ie.waitUntilReady();
		}
	}

	/**
	 * Gets a product number from the Assets page. Based on the inputs it will
	 * return the serial number of the first product, with a serial number, from
	 * the search results. If there are no products which match the query it
	 * will return an empty string. This method will return any product.
	 * Subproducts, master products and other products behave differently. This
	 * method will return the products which are not subproduct or master
	 * products.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return a serial number for the first product on the product search
	 *         result page
	 * @throws Exception
	 */
	public String getProduct(IE ie, String rfid, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {
		String[] products = getProductsFirstPage(ie, rfid, serialNumber,
				productStatus, salesAgent, orderNumber, purchaseOrder, jobSite,
				assignedTo, customer, division, location, productType,
				fromDate, toDate);
		if (products == null)
			return "";
		return products[0];
	}

	/**
	 * Gets a master product number from the Assets page. Based on the inputs it
	 * will return the serial number of the first master product, with a serial
	 * number, from the search results. If there are no products which match the
	 * query it will return an empty string.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return a serial number for the first product on the product search
	 *         result page
	 * @throws Exception
	 */
	public String getMasterProduct(IE ie, String rfid, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {
		String[] products = getMasterProductsFirstPage(ie, rfid, serialNumber,
				productStatus, salesAgent, orderNumber, purchaseOrder, jobSite,
				assignedTo, customer, division, location, productType,
				fromDate, toDate);
		if (products == null)
			return "";
		return products[0];
	}

	/**
	 * Gets a subproduct number from the Assets page. Based on the inputs it
	 * will return the serial number of the first product, with a serial number,
	 * from the search results. If there are no subproducts which match the
	 * query it will return an empty string.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return a serial number for the first product on the product search
	 *         result page
	 * @throws Exception
	 */
	public String getSubProduct(IE ie, String rfid, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {
		String[] products = getSubProductsFirstPage(ie, rfid, serialNumber,
				productStatus, salesAgent, orderNumber, purchaseOrder, jobSite,
				assignedTo, customer, division, location, productType,
				fromDate, toDate);
		if (products == null)
			return "";
		return products[0];
	}

	/**
	 * Returns a String array of product id values from the search result.
	 * 
	 * If any parameter is null it will not set that field on the search form.
	 * If you want to clear the value on a form then you should set the value to
	 * an empty string, e.g. "".
	 * 
	 * If the result set is empty this will return null.
	 * 
	 * This method will return any product. Subproducts, master products and
	 * other products behave differently. If you care about these differences
	 * don't use this method.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 *            - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return an array of serial numbers
	 * @throws Exception
	 */
	public String[] getProducts(IE ie, String rfid, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {

		gotoProductSearchResults(ie, rfid, serialNumber, productStatus,
				salesAgent, orderNumber, purchaseOrder, jobSite, assignedTo,
				customer, division, location, productType, fromDate, toDate);

		List<String> tmp = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		boolean loopFlag = true;
		do {
			tmp = getProductsFromSearchResultsCurrentPage(ie);
			if (tmp.size() != 0)
				result.addAll(tmp);
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0)
			return null;
		else
			return result.toArray(new String[0]);
	}

	/**
	 * Returns a String array of product id values from the search result  but
	 * only if the product is a sub-product of another product. If the product
	 * can be used as a subproduct it does not necessarily show up in this list.
	 * If the product is actually attached to a master product then it shows up
	 * in this list.
	 * 
	 * If any parameter is null it will not set that field on the search form.
	 * If you want to clear the value on a form then you should set the value to
	 * an empty string, e.g. "".
	 * 
	 * If the result set is empty this will return null.
	 * 
	 * @param ie
	 * @param rfid - input to the form
	 * @param serialNumber
	 *            - input to the form
	 * @param productStatus
	 *            - input to the form
	 * @param salesAgent
	 *            - input to the form
	 * @param orderNumber
	 *            - input to the form
	 * @param purchaseOrder
	 *            - input to the form
	 * @param jobSite
	 *            - input to the form
	 * @param assignedTo
	 *            - input to the form
	 * @param customer
	 *            - input to the form
	 * @param division
	 *            - input to the form
	 * @param location
	 *            - input to the form
	 * @param productType
	 *            - input to the form
	 * @param fromDate
	 *            - input to the form
	 * @param toDate
	 *            - input to the form
	 * @return an array of serial numbers
	 * @throws Exception
	 */
	public String[] getSubProducts(IE ie, String rfid, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {

		gotoProductSearchResults(ie, rfid, serialNumber, productStatus,
				salesAgent, orderNumber, purchaseOrder, jobSite, assignedTo,
				customer, division, location, productType, fromDate, toDate);

		List<String> tmp = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		boolean loopFlag = true;
		do {
			tmp = getSubProductsFromSearchResultsCurrentPage(ie);
			if (tmp.size() != 0)
				result.addAll(tmp);
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		if (result.size() == 0)
			return null;
		else
			return result.toArray(new String[0]);
	}

	/**
	 * Helper method to get the products from a given search result. The
	 * searchForProductsFirstPage gets some search results then calls this
	 * method.
	 * 
	 * If the list is empty this method will return null.
	 * 
	 * This method will return any product. Subproducts, master products and
	 * other products behave differently. If you care about these differences
	 * don't use this method.
	 * 
	 * @param links
	 * @return
	 * @throws Exception
	 */
	private List<String> getProductsFromSearchResultsCurrentPage(IE ie)
			throws Exception {
		Links links = ie.links();
		List<String> products = new ArrayList<String>();
		for (int i = 0; i < links.length(); i++) {
			Link link = links.get(i);
			if (link.href().indexOf("product.action") != -1 && link.text().contains("Info")) {
				link.click();
				ie.waitUntilReady();
				if (!ie.htmlElement(text("Part Of")).exists()
						&& !ie.htmlElement(text("Sub-Products")).exists()) {
					String serial = ie.htmlElement(tag("h1")).text();
					int k = serial.indexOf('-') + 1; // "Product Information - $serialNumber",
														// the +1 trims the
														// front plus the "- "
					serial = serial.substring(k + 1);
					products.add(serial);
				}
				ie.back();
			}
		}
		return products;
	}

	/**
	 * Helper method to get the sub products from a given search result. The
	 * getSubProductsFirstPage gets some search results then calls this method.
	 * 
	 * If the list is empty this method will return null.
	 * 
	 * @param links
	 * @return
	 * @throws Exception
	 */
	private List<String> getSubProductsFromSearchResultsCurrentPage(IE ie)
			throws Exception {
		Links links = ie.links();
		List<String> products = new ArrayList<String>();
		for (int i = 0; i < links.length(); i++) {
			Link link = links.get(i);
			if (link.href().indexOf("product.action") != -1) {
				link.click();
				ie.waitUntilReady();
				if (ie.htmlElement(text("Part Of")).exists()) {
					String serial = ie.htmlElement(tag("h1")).text();
					int k = serial.indexOf('-') + 1; // "Product Information - $serialNumber",
														// the +1 trims the
														// front plus the "- "
					serial = serial.substring(k + 1);
					products.add(serial);
				}
				ie.back();
			}
		}
		return products;
	}

	/**
	 * Helper method to get the master products from a given search result. The
	 * getMasterProductsFirstPage gets some search results then calls this
	 * method.
	 * 
	 * If the list is empty this method will return null.
	 * 
	 * @param links
	 * @return
	 * @throws Exception
	 */
	private List<String> getMasterProductsFromSearchResultsCurrentPage(IE ie)
			throws Exception {
		Links links = ie.links();
		List<String> products = new ArrayList<String>();
		for (int i = 0; i < links.length(); i++) {
			Link link = links.get(i);
			if (link.href().indexOf("product.action") != -1) {
				link.click();
				ie.waitUntilReady();
				if (ie.htmlElement(text("Sub-Products")).exists()) {
					String serial = ie.htmlElement(tag("h1")).text();
					int k = serial.indexOf('-') + 1; // "Product Information - $serialNumber",
														// the +1 trims the
														// front plus the "- "
					serial = serial.substring(k + 1);
					products.add(serial);
				}
				ie.back();
			}
		}
		return products;
	}

	/**
	 * This method assumes you are in the Inspection Type page of a product.
	 * That is the page which has "$inspectionType on $productSerialNumber"
	 * as the page header.
	 * 
	 * It will check that the page has the appropriate header, a "Product Summary"
	 * section plus a Cancel and Save button.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param product
	 *            - serial number
	 * @param inspectionType
	 * @throws Exception
	 */
	public void validateInspectionTypeOnProductPage(IE ie, String product,
			String inspectionType) throws Exception {
		String title = inspectionType + " on " + product;
		if (!ie.containsText(title))
			throw new TestCaseFailedException();
		if (!ie.containsText("Product Summary"))
			throw new TestCaseFailedException();
		if (!ie.button(text("Cancel")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(id("inspectionCreate_hbutton_save")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Assumes we are on the Inspection Groups for Product page. Returns an
	 * array of inspection IDs so we can pick an inspection to manage.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return an array of inspection IDs
	 * @throws Exception
	 */
	public String[] getInspectionIdsFromInspectionGroupsForProducts(IE ie)
			throws Exception {
		Links ls = ie.links(url("/selectInspectionEdit/"));
		List<String> results = new ArrayList<String>();

		for (int i = 0; i < ls.length(); i++) {
			results.add(ls.get(i).id());
		}

		if (results.size() == 0)
			return null;
		else
			return results.toArray(new String[results.size()]);
	}

	/**
	 * Gets to the screen where we are editing an inspection. The inspectID
	 * comes from the getInspectionIdsFromInspectionGroupsForProducts method.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectID
	 *            - an ID from the
	 *            getInspectionIdsFromInspectionGroupsForProducts method
	 * @throws Exception
	 */
	public void gotoEditInspectionFromInspectionGroupsForProducts(IE ie, String inspectID) throws Exception {
		ie.link(id(inspectID)).click();
		ie.waitUntilReady();
		if (ie.link(text("/edit this event/")).exists()) { // workaround for WEB-550
			ie.link(text("/edit this event/")).click();
		}
	}

	/**
	 * Takes as input an array of inspector names ("Firstname Lastname") and
	 * compares it to the list of names on the Inspector drop down for the Edit
	 * Inspection page. This assumes you are on the page for editing an
	 * inspection.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectors
	 *            - an array of inspection names, not user IDs
	 * @throws Exception
	 */
	public void validateInspectorsOnEditInspection(IE ie, String[] inspectors)
			throws Exception {
		if (inspectors == null) {
			return;
		}
		Set<String> i1 = new TreeSet<String>(Arrays.asList(inspectors));
		Set<String> i2 = new TreeSet<String>();
		Options o = ie.selectList(id("inspectionUpdate_inspector")).options();
		for (int i = 0; i < o.length(); i++) {
			i2.add(o.get(i).text());
		}

		boolean result = i1.equals(i2);
		if (!result) {
			System.err.println(getMethodName());
			System.err.println("Have Inspect Permission: " + i1);
			System.err.println("  On list of Inspectors: " + i2);
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Takes as input an array of inspector names ("Firstname Lastname") and
	 * compares it to the list of names on the Inspector drop down for the
	 * Create Inspection page. This assumes you are on the page for creating an
	 * inspection.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectors
	 *            - an array of inspector names, not user IDs
	 * @throws Exception
	 */
	public void validateInspectorsOnCreateInspection(IE ie, String[] inspectors)
			throws Exception {
		if (inspectors == null) {
			return;
		}
		Set<String> i1 = new TreeSet<String>(Arrays.asList(inspectors));
		Set<String> i2 = new TreeSet<String>();
		Options o = ie.selectList(id("inspectionCreate_inspector")).options();
		for (int i = 0; i < o.length(); i++) {
			i2.add(o.get(i).text());
		}
		boolean result = i1.equals(i2);
		if (!result) {
			System.err.println(getMethodName());
			System.err.println("Have Inspect Permission: " + i1);
			System.err.println("  On list of Inspectors: " + i2);
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Takes as input an array of inspector names ("Firstname Lastname") and
	 * compares it to the list of names on the Inspector drop down for the
	 * Reporting page. This assumes you are on the Report page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectors
	 *            - an array of inspector names, not user IDs
	 * @throws Exception
	 */
	public void validateInspectorsOnReporting(IE ie, String[] inspectors)
			throws Exception {
		if (inspectors == null) {
			return;
		}
		List<String> i1 = new ArrayList<String>(Arrays.asList(inspectors));
		Collections.sort(i1);
		List<String> i2 = new ArrayList<String>();
		Options o = ie.selectList(id("reportForm_criteria_inspector")).options();
		for (int i = 0; i < o.length(); i++) {
			String u = o.get(i).text();
			if (!u.equals(""))
				i2.add(u);
		}
		Collections.sort(i2);
		boolean result = i1.equals(i2);
		if (!result) {
			System.err.println(getMethodName());
			System.err.println("Have Inspect Permission: " + i1);
			System.err.println("  On list of Inspectors: " + i2);
			throw new TestCaseFailedException();
		}
	}

	/**
	 * This method takes a Product ID and InspectionType. It will then go to the
	 * page which starts a new inspection, of type InspectionType, on the given
	 * product. It just goes to the page and does not enter anything or save the
	 * inspection. This allows for things like session timeout or aborting the
	 * inspection.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productID
	 *            - serial number
	 * @param inspectionType
	 * @throws Exception
	 */
	public void gotoInspectionTypeOnProduct(IE ie, String productID,
			String inspectionType) throws Exception {
		gotoInspectionGroupsForProduct(ie, productID);
		ie.link(text(inspectionType)).click();
	}

	/**
	 * Go to the Inspection Groups - productID page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productID
	 *            - serial number
	 * @throws Exception
	 */
	public void gotoInspectionGroupsForProduct(IE ie, String productID)
			throws Exception {
		gotoInspect(ie);
		ie.textField(id("productSearchForm_search")).set(productID);
		ie.button(id("productSearchForm_load")).click();
		ie.waitUntilReady();
	}

	/**
	 * Determines if we are logged into the system. Simply checks for the Logout
	 * link. If it exists we assume we are logged in.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if logged in
	 * @throws Exception
	 */
	public boolean isLoggedIn(IE ie) throws Exception {
		boolean result = false;

		if (ie.link(text("Logout")).exists()) {
			result = true;
		}

		return result;
	}

	/**
	 * Checks to see if the company given matches the company we are currently
	 * set to. Works if you are logged in or not.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param company
	 * @return true if company parameter matches the company currently logged in
	 * @throws Exception
	 */
	public boolean isCurrentCompany(IE ie, String company) throws Exception {
		boolean result = false;

		if (isLoggedIn(ie)) {
			if (company.compareTo(getLoggedInCompany(ie)) == 0)
				result = true;
		}
		if (ie.containsText("Login in to account: " + company))
			result = true;

		return result;
	}

	/**
	 * This method will examine the current page to see if we can determine the
	 * tenant name. Currently, there is session timeout code that is embedded in
	 * every page. This code is javascript and has the variable tenantName. This
	 * is the only way I could see to determine the name of the company
	 * currently logged in.
	 * 
	 * It is assuming the format of the javascript has:
	 * 
	 * tenantName = 'tenant';
	 * 
	 * where tenant is the name of the company and it is surrounded with single
	 * quotes and no spaces. The whitespace outside the single quotes does not
	 * matter.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return company ID, e.g. unirope or hercules
	 * @throws Exception
	 */
	public String getLoggedInCompany(IE ie) throws Exception {
		String result = null;
		HtmlElements hes = ie.htmlElements();
		for (int i = 0; i < hes.length(); i++) {
			HtmlElement he = hes.get(i);
			String s = he.html();
			if (s.contains("tenantName")
					&& he.type().equalsIgnoreCase("text/javascript")) {
				int j = s.indexOf("tenantName");
				j = s.indexOf("'", j) + 1;
				int k = s.indexOf("'", j);
				result = s.substring(j, k);
				break;
			}
		}
		return result;
	}

	/**
	 * This method will check to see if the login page is set for the given
	 * customer. If it is not it will change the customer to the given name.
	 * 
	 * Note: if you give a customer name which does not match any of the given
	 * customers, it will switch to the non-existent customer and you will not
	 * be able to log in. This is by design for security purposes. We don't want
	 * to let hackers know whether they have guessed at a customer name
	 * correctly or not.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customer
	 * @throws Exception
	 */
	public void setTenantAtLoginPage(IE ie, String customer) throws Exception {
		if (!isCurrentCompany(ie, customer)) {
			ie.link(text("This is not the company I want.")).click();
			ie.textField(id("login_companyID")).set(customer);
			ie.button(id("login_label_continue")).click();
		}
	}

	/**
	 * Goes to the Jobs page and looks to see if the job exists by
	 * searching for the job id. It is really looking for the text on the
	 * page. So if the title, customer, division or status matches the job
	 * id, this will return true.

	 * @param ie
	 * @param jobTitle
	 * @return
	 * @throws Exception
	 */
	public boolean isJobPresent(IE ie, String jobTitle) throws Exception {
		boolean result = false;
		gotoJobs(ie);
		List<String> jobs = getJobs(ie);
		if(jobs.contains(jobTitle)) {
			result = true;
		}
		return result;
	}

	/**
	 * Goes to the Projects page and looks to see if the project exists by
	 * searching for the project id. It is really looking for the text on the
	 * page. So if the title, customer, division or status matches the project
	 * id, this will return true.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param projectID
	 * @return true if proejct exists
	 * @throws Exception
	 * @deprecated use isJobPresent
	 */
	public boolean isProjectPresent(IE ie, String projectID) throws Exception {
		boolean result = false;
		gotoProjects(ie);
		if (ie.containsText(projectID)) {
			result = true;
		}
		return result;
	}

	/**
	 * Goes to the Add Product page. If the current tenant has INTEGRATION,
	 * clicking the Identify link will bring you to the Identify Products page
	 * rather than the Add Product page. This method will check for the 'Add
	 * Product' button. If it exists it will skip to the real Add Product page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoAddProduct(IE ie) throws Exception {
		ie.link(text("Identify")).click(); // Should be visible from everywhere
		ie.waitUntilReady(); // Wait for page to load
		Link add = ie.link(xpath("//DIV[@id='contentHeader']/UL/LI/A[contains(text(),'Add') and not(contains(text(),'Multi')) and not(contains(text(),'Order'))]"));
		if (add.exists()) // if Integration then...
			add.click(); // drill down to the real
													// Add Product page
	}

	/**
	 * Validates that the current page is the Add Product page. Returns true of
	 * the page has the basic elements of the Add Product page. Anything which
	 * changes based on user selection, we ignore.
	 * 
	 * Also, if the tenant has JobSites, the jobSites parameter should be set to
	 * true. This will make the method check for the JobSite specific features.
	 * If jobSites is false it will check that the JobSite specific features are
	 * not present.
	 * 
	 * Finally, if the tenant has Integration, the integration parameter should
	 * be set to true. The order field should not be present if the tenant has
	 * Integration. If it is set to false, we check for the Order field to be on
	 * add products.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param jobSites
	 * @param integration
	 * @throws Exception
	 */
	public void validateAddProductPage(IE ie, boolean jobSites,
			boolean integration) throws Exception {
		// once we find one thing wrong we can abort
		if (!ie.containsText("Add Product"))
			throw new TestCaseFailedException();
		else if (!ie.textField(id("serialNumberText")).exists())
			throw new TestCaseFailedException();
		else if (!ie.link(text("generate")).exists())
			throw new TestCaseFailedException();
		else if (!ie.textField(id("rfidNumber")).exists())
			throw new TestCaseFailedException();
		else if (!ie.textField(id("productCreate_purchaseOrder")).exists())
			throw new TestCaseFailedException();
		else if (jobSites
				&& !ie.selectList(id("productCreate_jobSite")).exists())
			throw new TestCaseFailedException();
		else if (jobSites
				&& !ie.selectList(id("productCreate_assignedUser")).exists())
			throw new TestCaseFailedException();
		else if (jobSites
				&& ie.selectList(id("reportForm_criteria_customer")).exists())
			throw new TestCaseFailedException();
		else if (jobSites && ie.selectList(id("division")).exists())
			throw new TestCaseFailedException();
		else if (!jobSites && !ie.selectList(id("customer")).exists())
			throw new TestCaseFailedException();
		else if (!jobSites && !ie.selectList(id("division")).exists())
			throw new TestCaseFailedException();
		else if (!ie.textField(id("location")).exists())
			throw new TestCaseFailedException();
		else if (!ie.selectList(id("productCreate_productStatus")).exists())
			throw new TestCaseFailedException();
		else if (!ie.textField(id("customerRefNumber")).exists())
			throw new TestCaseFailedException();
		else if (!ie.textField(id("identified")).exists())
			throw new TestCaseFailedException();
		else if (!integration
				&& !ie.textField(id("nonIntegrationOrderNumber")).exists())
			throw new TestCaseFailedException();
		else if (integration
				&& ie.textField(id("nonIntegrationOrderNumber")).exists())
			throw new TestCaseFailedException();
		else if (!ie.selectList(id("productType")).exists())
			throw new TestCaseFailedException();
		else if (!ie.textField(id("comments")).exists())
			throw new TestCaseFailedException();
		else if (!ie.selectList(id("productCreate_commentTemplate")).exists())
			throw new TestCaseFailedException();
		else if (!ie.button(value("Reset Form")).exists())
			throw new TestCaseFailedException();
		else if (!ie.button(id("saveButton")).exists())
			throw new TestCaseFailedException();
		else if (!ie.button(id("saveAndInspButton")).exists())
			throw new TestCaseFailedException();
		else if (!ie.button(id("saveAndPrintButton")).exists())
			throw new TestCaseFailedException();
		else if (!ie.button(id("saveAndScheduleButton")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Goes to the login page. It assumes you are either logged in to fieldid or
	 * you are at the login page. If you have been using IE to go to pages
	 * outside fieldid this method will throw an exception.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customer
	 * @throws Exception
	 */
	public void gotoLoginPage(IE ie, String customer) throws Exception {
		// If you are logged in, log out.
		if (ie.link(text("Logout")).exists()) {
			ie.link(text("Logout")).click();
			ie.waitUntilReady();
		}

		if (!isCurrentCompany(ie, customer)) {
			String url = getBaseURL() + "login/" + customer;
			ie.goTo(url);
			setTenantAtLoginPage(ie, customer);
		}
	}

	/**
	 * This method will add a product. If any of the inputs are null it will
	 * skip setting those fields. If you wish to clear the default values send
	 * in a "" string. The serialNumber, productType and identified values are
	 * required.
	 * 
	 * You can pass in null for the serialNumber and true for generate. This
	 * will use the generate link to generate a serialNumber.
	 * 
	 * The method will return the serial number for the product. If generate is
	 * false, it will return the serialNumber you provided. If generate is true
	 * it will return the generated serialNumber. If you have generate as true
	 * and provide a serialNumber, it will use the generated serial number.
	 * 
	 * The format for the identified parameter is currently "MM/DD/YY".
	 * 
	 * The fields which appear after productType change depending on the product
	 * type. The array of strings must contain the values you want to populate
	 * those fields.
	 * 
	 * The 'Save' parameter determines if you want to "Save",
	 * "Save and Inspect", "Save And Print" or "Save and Schedule".
	 * 
	 * TODO: the productParameters feature has not been implemented. Currently,
	 * it is assumed you want blank/default values for the product parameters.
	 * If you attempt to pass in product parameters it will generate random input
	 * for all required fields. This is helpful if you want to create a product
	 * which has required parameters. You can actually pass in the array { "" }
	 * and it will work. In other words, so long as the productParameters is not
	 * null it will set random values.
	 * 
	 * If the add product fails, i.e. there is an error message on the display,
	 * this will return null and print an error message to System.err.
	 * 
	 * If the RFID number is already in use, this will move the RFID number to
	 * the new product. If you don't want this, you will have to see if the
	 * RFID number is in use and pick a different one.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param serialNumber - form input
	 * @param rfidNumber - form input
	 * @param purchaseOrder - form input
	 * @param customer - form input
	 * @param division - form input
	 * @param jobSite - form input
	 * @param assignedTo - form input
	 * @param Location - form input
	 * @param productStatus - form input
	 * @param refNumber - form input
	 * @param identified - form input
	 * @param orderNumber - form input
	 * @param productType - form input
	 * @param productParameters - form input
	 * @param comments - form input
	 * @param commentTemplate - form input
	 * @param button - text on the button you want to click, e.g. "Save"
	 * @throws Exception
	 */
	public String addProduct(IE ie, String serialNumber, boolean generate, String rfidNumber, String purchaseOrder, String customer,
			String division, String jobSite, String assignedTo, String Location, String productStatus, String refNumber,
			String identified, String orderNumber, String productType, String[] productParameters, String comments,
			String[] commentTemplate, String button) throws Exception {
		gotoAddProduct(ie);
		ie.waitUntilReady();

		if (serialNumber != null)
			ie.textField(id("serialNumberText")).set(serialNumber);
		if (generate) {
			ie.link(text("/generate/")).click();
			Thread.sleep(hack);
			serialNumber = ie.textField(id("serialNumberText")).value();
		}
		if (rfidNumber != null)
			ie.textField(id("rfidNumber")).set(rfidNumber);
		if (purchaseOrder != null)
			ie.textField(id("productCreate_purchaseOrder")).set(purchaseOrder);
		if (customer != null) {
			ie.selectList(id("customer")).option(text(customer)).select();
			Thread.sleep(hack);
		}
		if (division != null) {
			ie.selectList(id("division")).option(text(division)).select();
		}
		if (jobSite != null)
			ie.selectList(id("")).option(text(jobSite)).select();
		if (assignedTo != null)
			ie.selectList(id("productCreate_assignedUser")).option(text(assignedTo)).select();
		if (Location != null)
			ie.textField(id("location")).set(Location);
		if (productStatus != null)
			ie.selectList(id("productCreate_productStatus")).option(text(productStatus)).select();
		if (refNumber != null)
			ie.textField(id("customerRefNumber")).set(refNumber);
		if (identified != null)
			ie.textField(id("identified")).set(identified);
		if (orderNumber != null)
			ie.textField(id("nonIntegrationOrderNumber")).set(orderNumber);
		if (productType != null) {
			ie.selectList(id("productType")).option(text(productType)).select();
			ie.selectList(id("productType")).fireEvent("onchange");
			Thread.sleep(hack);
			ie.waitUntilReady();
		}
		if (productParameters != null) {
			Thread.sleep(hack);
			Labels ls = ie.labels();
			for(int i = 0; i < ls.length(); i++) {
				Label l = ls.get(i);
				String id = l.htmlFor();
				if(l.html().contains("required") && !id.equals("serialNumberText")) { // skip serial number
					HtmlElement r = ie.htmlElement(id(id));
					Random ran = new Random();
					if(r.type().equals("select-one")) {	// select box
						SelectList sl = ie.selectList(id(id));
						int max = sl.getAllContents().size();
						int n = ran.nextInt(max-2)+1;	// select from 1 to n-1
						sl.option(n).select();
						ie.textField(id("comments")).focus();
					}
					else if(r.type().equals("text")) {	// Unit of Measure
						TextField tf = ie.textField(id(id));
						if(tf.readOnly()) {
							String uomsID = "unitOfMeasureSelector_" + id;
							ie.link(id(uomsID)).click();
							Thread.sleep(hack);
							String uomID = "unitOfMeasureId_" + id;
							ie.selectList(id(uomID)).option(text("Inches")).select();
							ie.waitUntilReady();
							Thread.sleep(hack);
							String inchID = "1_" + id;
							ie.textField(id(inchID)).set(Integer.toString(ran.nextInt(12)));
							String buttonID = "unitOfMeasureForm_" + id + "_hbutton_submit";
							ie.button(id(buttonID)).click();
						}
						else {							// text field
							tf.set("random" + ran.nextInt());
						}
					}
				}
			}
		}
		if (comments != null)
			ie.textField(id("comments")).set(comments);
		if (commentTemplate != null) {
			for (int i = 0; i < commentTemplate.length; i++)
				ie.selectList(id("productCreate_commentTemplate")).option(
						text(commentTemplate[i])).select();
		}

		ie.waitUntilReady();
		Thread.sleep(hack);
		ie.button(value(button)).click();
		ie.waitUntilReady();
		if(ie.containsText("an RFID Number that already exists in the system.")) {
			validateCheckingRFIDNumber(ie, rfidNumber);
			myWindowCapture("RFID-" + rfidNumber + "-Exists-Replacing.png", ie);
			ie.button(id("confirmRfidChange")).click();
		}
		
		Span error = ie.span(xpath("//SPAN[@class='errorMessage']"));
		if(error.exists()) {
			System.err.println(error.text());
			return null;
		}

		return serialNumber;
	}

	/**
	 * Adds a job. This replaces the addProject method. Projects are
	 * now called Jobs and there are two types of Jobs, Event and
	 * Asset.
	 * 
	 * Does NOT assume you are on the Jobs page. I have not
	 * validated any of the input beyond null and empty string. This will allow
	 * the test to input bad values for the fields as well as correct values.
	 * 
	 * If you want to leave a field blank, enter a null or "" for the parameter.
	 * 
	 * The proper format for a date is currently MM/dd/yy hh:mm am. The date boxes are
	 * text boxes with javascript calendars that can modify them. I just enter
	 * the text into the box directly. I do not use the calendar scripts to
	 * populate them.
	 *  
	 * @param ie - reference to the IE object
	 * @param typeOfJob - "Event" or "Asset" required, defaults to Event
	 * @param jobID - required field
	 * @param title - required field
	 * @param customer - must be from the list of customers
	 * @param division - must be from the list of divisons for the given customer
	 * @param status - free form text
	 * @param open - Is the project open or not?
	 * @param description - free form text box
	 * @param startDate - MM/dd/yy hh:mm am/pm
	 * @param estimateDate - MM/dd/yy hh:mm am/pm
	 * @param actualDate - MM/dd/yy hh:mm am/pm
	 * @param duration - free form text
	 * @param poNumber - Purchuse Order Number
	 * @param workPerformed - free form text box
	 * @throws Exception
	 */
	public void addJob(IE ie, String typeOfJob, String jobID, String title,
			String customer, String division, String status, boolean open, String description,
			String startDate, String estimateDate, String actualDate, String duration, 
			String poNumber, String workPerformed)
			throws Exception {
		
		gotoJobs(ie);
		ie.link(text("Add Job")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Add Job"))
			throw new TestCaseFailedException();
		if (!ie.containsText("Job Details"))
			throw new TestCaseFailedException();
		if (!ie.containsText("Job ID"))
			throw new TestCaseFailedException();
		
		if(typeOfJob != null && typeOfJob.equalsIgnoreCase("Asset"))
			ie.radio(id("jobCreate_eventJobfalse")).set();	// Asset Job
		else
			ie.radio(id("jobCreate_eventJobtrue")).set();	// Event Job

		if (jobID != null && !jobID.equals(""))
			ie.textField(id("jobCreate_projectID")).set(jobID);

		if (title != null && !title.equals(""))
			ie.textField(id("jobCreate_name")).set(title);

		if (customer != null && !customer.equals("")) {
			ie.selectList(id("customer")).option(text(customer)).select();
			Thread.sleep(hack);
		}

		if (division != null && !division.equals(""))
			ie.selectList(id("division")).option(text(division)).select();

		if (status != null && !status.equals(""))
			ie.textField(id("jobCreate_status")).set(status);
		
		ie.checkbox(id("jobCreate_open")).set(open);
		
		if(description != null && !description.equals(""))
			ie.textField(id("jobCreate_description")).set(description);

		if (startDate != null && !startDate.equals(""))
			ie.textField(id("jobCreate_started")).set(startDate);

		if (estimateDate != null && !estimateDate.equals(""))
			ie.textField(id("jobCreate_estimatedCompletion")).set(estimateDate);

		if (actualDate != null && !actualDate.equals(""))
			ie.textField(id("jobCreate_actualCompletion")).set(actualDate);

		if (duration != null && !duration.equals(""))
			ie.textField(id("jobCreate_duration")).set(duration);
		
		if(poNumber != null && !poNumber.equals(""))
			ie.textField(id("jobCreate_poNumber")).set(poNumber);
		
		if(workPerformed != null && !workPerformed.equals(""))
			ie.textField(id("jobCreate_workPerformed")).set(workPerformed);

		ie.button(id("jobCreate_label_save")).click();
		ie.waitUntilReady();

		if (!ie.containsText("Job Details"))
			throw new TestCaseFailedException();
	}
	
	/**
	 * Adds a project. Does NOT assume you are on the Projects page. I have not
	 * validated any of the input beyond null and empty string. This will allow
	 * the test to input bad values for the fields as well as correct values.
	 * 
	 * If you want to leave a field blank, enter a null or "" for the parameter.
	 * 
	 * The proper format for a date is currently MM/dd/yy. The date boxes are
	 * text boxes with javascript calendars that can modify them. I just enter
	 * the text into the box directly. I do not use the calendar scripts to
	 * populate them.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param projectID
	 *            - form input
	 * @param title
	 *            - form input
	 * @param customer
	 *            - form input
	 * @param division
	 *            - form input
	 * @param status
	 *            - form input
	 * @param startDate
	 *            - form input
	 * @param estimateDate
	 *            - form input
	 * @param actualDate
	 *            - form input
	 * @param duration
	 *            - form input
	 * @throws Exception
	 * @deprecated use addEventJob or addAssetJob instead.
	 */
	public void addProject(IE ie, String projectID, String title,
			String customer, String division, String status, String startDate,
			String estimateDate, String actualDate, String duration)
			throws Exception {
		gotoProjects(ie);
		ie.link(text("Add Job")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Add Job"))
			throw new TestCaseFailedException();
		if (!ie.containsText("Job Details"))
			throw new TestCaseFailedException();
		if (!ie.containsText("Job ID"))
			throw new TestCaseFailedException();

		if (projectID != null && !projectID.equals(""))
			ie.textField(id("projectCreate_projectID")).set(projectID);

		if (title != null && !title.equals(""))
			ie.textField(id("projectCreate_name")).set(title);

		if (customer != null && !customer.equals("")) {
			ie.selectList(id("customer")).option(text(customer)).select();
			Thread.sleep(hack);
		}
		
		if (division != null && !division.equals(""))
			ie.selectList(id("division")).option(text(division)).select();

		if (status != null && !status.equals(""))
			ie.textField(id("projectCreate_status")).set(status);

		if (startDate != null && !startDate.equals(""))
			ie.textField(id("projectCreate_started")).set(startDate);

		if (estimateDate != null && !estimateDate.equals(""))
			ie.textField(id("projectCreate_estimatedCompletion")).set(
					estimateDate);

		if (actualDate != null && !actualDate.equals(""))
			ie.textField(id("projectCreate_acutalCompletion")).set(actualDate);

		if (duration != null && !duration.equals(""))
			ie.textField(id("projectCreate_duration")).set(duration);

		ie.button(id("projectCreate_label_save")).click();
		ie.waitUntilReady();

		if (!ie.containsText("Project - " + projectID))
			throw new TestCaseFailedException();
		if (!ie.containsText("Project Details"))
			throw new TestCaseFailedException();
	}

	/**
	 * Selects the Administration page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoAdministration(IE ie) throws Exception {
		ie.link(text("Administration ")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Administration"))
			throw new TestCaseFailedException();
		if (!ie.containsText("System Access and Setup"))
			throw new TestCaseFailedException();
	}

	/**
	 * Goes to the Manage Organizational Units page. Assumes you can
	 * go to the Administration page and the Manage Organizational Units
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageOrganizationalUnits(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Organizational Units")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage Product Statuses page. Assumes you can
	 * go to the Administration page and the Manage Product Statuses
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageProductStatuses(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Product Statuses")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage Inspection Books page. Assumes you can
	 * go to the Administration page and the Manage Inspection Books
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageInspectionBooks(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Inspection Books")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Edit Notification Settings page. Assumes you can
	 * go to the Administration page and the Edit Notification Settings
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoEditNotificationSettings(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Edit Notification Settings")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Auto Attributes Wizard page. Assumes you can
	 * go to the Administration page and the Auto Attributes Wizard
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoAutoAttributesWizard(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Auto Attributes Wizard")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage Comment Templates page. Assumes you can
	 * go to the Administration page and the Manage Comment Templates
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageCommentTemplates(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Comment Templates")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Data Log page. Assumes you can
	 * go to the Administration page and the Data Log
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoDataLog(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Data Log")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage your Safety Network page. Assumes you can
	 * go to the Administration page and the Manage your Safety Network
	 * page. Throws an exception if you cannot.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageYourSafetyNetwork(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage your Safety Network")).click();
		ie.waitUntilReady();
	}

	/**
	 * Selects the Jobs page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoJobs(IE ie) throws Exception {
		ie.link(text("Jobs")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Jobs"))
			throw new TestCaseFailedException();
		if (!ie.link(text("Add Job")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Selects the Projects page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 * @deprecated use gotoJobs
	 */
	public void gotoProjects(IE ie) throws Exception {
		ie.link(text("Projects")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Projects"))
			throw new TestCaseFailedException();
		if (!ie.link(text("Add Project")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Selects the schedule page
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoSchedule(IE ie) throws Exception {
		ie.link(text("Schedule")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Inspection Schedule"))
			throw new TestCaseFailedException();
	}

	/**
	 * Selects the Reporting page
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoReporting(IE ie) throws Exception {
		ie.link(id("menuReport")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Report"))
			throw new TestCaseFailedException();
		if (!ie.button(id("reportForm_label_Run")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Selects the Inspect page
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoInspect(IE ie) throws Exception {
		ie.link(text("/Inspect/")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Inspect"))
			throw new TestCaseFailedException();
		if (!ie.textField(id("productSearchForm_search")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Selects the Assets page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoAssets(IE ie) throws Exception {
		ie.link(text("Assets")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Product Search"))
			throw new TestCaseFailedException();
	}

	/**
	 * Select the Home link so we end up on the Home page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoHome(IE ie) throws Exception {
		ie.link(id("menuHome")).click();
		ie.waitUntilReady();
		if (!ie.containsText("Home"))
			throw new TestCaseFailedException();
	}

	/**
	 * Select an order from a list of products after you have searched for the
	 * products by order number. It is assumed that you have called the
	 * loadOrderNumber method and a list of products associated with the order
	 * number are now displayed in a table.
	 * 
	 * If not index is provided this will select the first product on the list.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param index
	 *            - zero index used to select the product from order we want to
	 *            add to
	 * @throws Exception
	 */
	public void gotoOrderFromIdentifyProducts(IE ie, int index)
			throws Exception {
		// Get a list of all the orders
		Links l = ie.links(url("/searchType=ordernumber/"));

		// pick a random order on the page and click it
		if (index < l.length()) {
			l.get(index).click();
			ie.waitUntilReady();
		}

		ie.waitUntilReady();
	}

	/**
	 * Adds the first product on the list to the order. Same assumption as
	 * gotoOrderFromIdentifyProducts(IE ie, int index).
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoOrderFromIdentifyProducts(IE ie) throws Exception {
		gotoOrderFromIdentifyProducts(ie, 0);
	}

	/**
	 * Validate the contents of the Product Information page. Handles the
	 * differences between the different extended features. Also supports master
	 * products and subproducts.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @param jobSites
	 * @param compliance
	 * @param integration
	 * @param productType
	 * @throws Exception
	 */
//	public void validateProductInfoPage(IE ie, String serialNumber,
//			boolean jobSites, boolean compliance, boolean integration,
//			ProductType productType) throws Exception {
		public void validateProductInfoPage(IE ie, String serialNumber,
				boolean jobSites, boolean compliance, boolean integration) throws Exception {
		gotoProductInfo(ie, serialNumber);
		if (!ie.containsText("Product Information - " + serialNumber)) { // H1
			throw new TestCaseFailedException();
		} else if (!ie.containsText("Product Summary")) { // h2
			throw new TestCaseFailedException();
		} else if (!ie.label(text("/Serial Number/")).exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label(text("/RFID Number/")).exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Product Type/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Product Status/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Identified/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Customer Name/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Division/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Location/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Reference Number/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Purchase Order/").exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.label("/Order Number/").exists()) { // appears in different location if Integration
			throw new TestCaseFailedException();
		} else if (!ie.containsText("Last Inspection")) { // H2
			throw new TestCaseFailedException();
		} else if (!ie.link(text("Manage Inspections")).exists()) {
			throw new TestCaseFailedException();
		} else if (compliance && !ie.link(text("Compliance Check")).exists()) {
			throw new TestCaseFailedException();
		} else if (!compliance && ie.link(text("Compliance Check")).exists()) {
			throw new TestCaseFailedException();
		} else if (jobSites && !ie.containsText("Site Information")) {
			throw new TestCaseFailedException();
		} else if (jobSites && !ie.label("/Job Site/").exists()) {
			throw new TestCaseFailedException();
		} else if (jobSites && !ie.label("/Assigned To/").exists()) { // WEB-399
			throw new TestCaseFailedException();
		} else if (jobSites && ie.containsText("Customer Information")) {
			throw new TestCaseFailedException();
		} else if (!jobSites && ie.containsText("Site Information")) {
			throw new TestCaseFailedException();
		} else if (!jobSites && ie.label("/Job Site/").exists()) {
			throw new TestCaseFailedException();
		} else if (!jobSites && ie.label("/Assigned To/").exists()) { // WEB-399
			throw new TestCaseFailedException();
		} else if (!jobSites && !ie.containsText("Customer Information")) {
			throw new TestCaseFailedException();
		} else if (integration && !ie.containsText("Order Details")) {
			throw new TestCaseFailedException();
		} else if (!integration && ie.containsText("Order Details")) {
			throw new TestCaseFailedException();
		} else if (!integration && ie.link(text("Connect To Order")).exists()) {
			throw new TestCaseFailedException();
//		} else if (productType == ProductType.regular && (ie.containsText("Part of") || ie.containsText("Sub-Products"))) {
//			throw new TestCaseFailedException();
//		} else if (productType == ProductType.master && ie.containsText("Part of")) {
//			throw new TestCaseFailedException();
//		} else if (productType == ProductType.sub && ie.containsText("Sub-Products")) {
//			throw new TestCaseFailedException();
		}
	}

	/**
	 * Go to the Product Information page for a product with a given serial
	 * number.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoProductInfo(IE ie, String serialNumber) throws Exception {
		gotoHomeSmartSearchTextBox(ie);
		TextField ss = ie.textField(id("searchText"));
		ss.fireEvent("onfocus");
		ss.set(serialNumber);
		ie.button(id("smartSearchButton")).click();
		ie.waitUntilReady();
	}
	
	public void deleteAllInspectionTypesFromProductType(final IE ie, String productType) throws Exception {
		gotoSelectInspectionTypes(ie, productType);

		Checkboxes cs = ie.checkboxes();
		for (int i = 0; i < cs.length(); i++) {
			cs.get(i).set(false);
		}
		// set up a thread to answer the "Are you sure?" dialog
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					Wnd w = Wnd.findWindow("#32770",
							"Windows Internet Explorer");
					IEPromptDialog confirm = new IEPromptDialog(w, ie);
					confirm.ok();
				} catch (Exception e) {
				}
			}
		}).start();
		ie.button(id("productTypeEventTypesSave_hbutton_save")).click();
		ie.waitUntilReady();

	}

	/**
	 * Assumes you are already logged into the system. Will create two product
	 * types called master and sub where sub is a subproduct of master. It will
	 * add all inspection types to the master product type. It will also change
	 * one inspection type to a master inspection, if masterInspection != null.
	 * If you don't want to set a master inspection type, set the parameter to
	 * null.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param masterInspection
	 * @throws Exception
	 */
	public void setupMasterAndSubProductTypes(final IE ie, String masterInspection)
			throws Exception {
		gotoManageProductTypes(ie);

		if (!ie.link(text("sub")).exists()) {
			ie.link(text("/Add/")).click();
			ie.waitUntilReady();
			ie.textField(id("productTypeUpdate_name")).set("sub");
			ie.button(id("productTypeUpdate_label_save")).click();
			ie.waitUntilReady();
			ie.link(text("/View All/")).click();
			ie.waitUntilReady();
		} else {
			ie.link(text("sub")).click();
			ie.waitUntilReady();
		}

		deleteAllInspectionTypesFromProductType(ie, "sub");
		
		ie.link(text("/View All/")).click();
		ie.waitUntilReady();

		if (!ie.link(text("master")).exists()) {
			ie.link(text("/Add/")).click();
			ie.waitUntilReady();
			ie.textField(id("productTypeUpdate_name")).set("master");
			ie.button(id("productTypeUpdate_label_save")).click();
			ie.waitUntilReady();
		} else {
			ie.link(text("master")).click();
			ie.waitUntilReady();
		}

		ie.link(text("/Inspection Types/")).click();
		ie.waitUntilReady();
		Checkboxes cs = ie.checkboxes();
		for (int i = 0; i < cs.length(); i++) {
			cs.get(i).set();
		}
		ie.button(id("productTypeEventTypesSave_hbutton_save")).click();
		ie.waitUntilReady();
		ie.link(text("/Sub-Components/")).click();
		ie.waitUntilReady();
		if (!ie.span(text("sub")).exists()) {
			ie.selectList(id("addSubProduct")).option(text("sub")).select();
			ie.button(id("addSubProductbutton")).click();
			ie.waitUntilReady();
			ie.button(id("productTypeConfigurationUpdate_label_save")).click();
			ie.waitUntilReady();
		}
		if (masterInspection != null) {
			gotoManageInspectionTypes(ie);
			ie.link(text(masterInspection)).click();
			ie.waitUntilReady();
			ie.link(text("/Edit/")).click();
			ie.waitUntilReady();
			ie.checkbox(id("inspectionTypeUpdate_master")).set();
			ie.button(id("inspectionTypeUpdate_save")).click();
			ie.waitUntilReady();
		}
		gotoHome(ie);
	}

	/**
	 * Goes to the Manage Inspection Types page inside the Administation area. Will
	 * select the Administration page for you. You do not need to call the
	 * gotoAdministration method first.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageInspectionTypes(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Inspection Types")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage Product Types page inside the Administation area. Will
	 * select the Administration page for you. You do not need to call the
	 * gotoAdministration method first.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageProductTypes(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Product Types")).click();
		ie.waitUntilReady();
	}

	/**
	 * Checks the Add Products page for all the correct fields and buttons.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param jobSites
	 * @throws Exception
	 */
	public void validateAssets(IE ie, boolean jobSites) throws Exception {
		if (!ie.textField(id("reportForm_criteria_rfidNumber")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_serialNumber")).exists())
			throw new TestCaseFailedException();
		if (!ie.selectList(id("reportForm_criteria_productStatus")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_salesAgent")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_orderNumber")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_purchaseOrder")).exists())
			throw new TestCaseFailedException();
		if (jobSites) {
			if (!ie.selectList(id("reportForm_criteria_jobSite")).exists())
				throw new TestCaseFailedException();
			if (!ie.selectList(id("reportForm_criteria_assingedUser")).exists()) // WEB-363
				throw new TestCaseFailedException();
		}
		if (!ie.selectList(id("reportForm_criteria_customer")).exists())
			throw new TestCaseFailedException();
		if (!ie.selectList(id("division")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_criteria_location")).exists())
			throw new TestCaseFailedException();
		if (!ie.selectList(id("reportForm_criteria_productType")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_fromDate")).exists())
			throw new TestCaseFailedException();
		if (!ie.textField(id("reportForm_toDate")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(value("Clear Form")).exists())
			throw new TestCaseFailedException();
		if (!ie.button(id("reportForm_label_Run")).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * check the Reporting page for all the correct fields and buttons
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param jobsite
	 * @throws Exception
	 */
	public void validateReporting(IE ie, boolean jobsite) throws Exception {
		gotoReporting(ie);
		if (!ie.containsText("Report")) {
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_inspectionBook")).exists()) { // inspection book
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_rfidNumber")).exists()) { // RFID
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_serialNumber")).exists()) { // serial #
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_inspectionTypeGroup")).exists()) { // inspection type groups
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_inspector")).exists()) { // inspector name
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_orderNumber")).exists()) { // order #
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_purchaseOrder")).exists()) { // purchase order
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_customer")).exists()) { // customer
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("division")).exists()) { // division
			throw new TestCaseFailedException();
		} else if (jobsite && !ie.selectList(id("reportForm_criteria_jobSite")).exists()) { // jobsite
			throw new TestCaseFailedException();
		} else if (jobsite && !ie.selectList(id("reportForm_criteria_assingedUser")).exists()) { // assigned to
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_productType")).exists()) { // product type
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_productStatus")).exists()) { // product status
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_fromDate")).exists()) { // from date
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_toDate")).exists()) { // to date
			throw new TestCaseFailedException();
		} else if (!ie.button(value("Clear Form")).exists()) { // clear form
			throw new TestCaseFailedException();
		} else if (!ie.button(id("reportForm_label_Run")).exists()) { // search
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Checks the Scheduled Inspection page for all the correct fields and buttons.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param jobsite
	 * @throws Exception
	 */
	public void validateSchedule(IE ie, boolean jobsite) throws Exception {
		gotoSchedule(ie);
		if (!ie.containsText("Inspection Schedule")) {
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_inspectionType")).exists()) { // inspection type
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_rfidNumber")).exists()) { // RFID
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_serialNumber")).exists()) { // serial number
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_productStatus")).exists()) { // product status
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_salesAgent")).exists()) { // sales agent
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_orderNumber")).exists()) { // order #
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_criteria_purchaseOrder")).exists()) { // purchase order
			throw new TestCaseFailedException();
		} else if (jobsite && !ie.selectList(id("reportForm_criteria_jobSite")).exists()) { // job site
			throw new TestCaseFailedException();
		} else if (jobsite && !ie.selectList(id("reportForm_criteria_assingedUser")).exists()) { // assigned to
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_customer")).exists()) { // customer
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("division")).exists()) { // division
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("reportForm_criteria_productType")).exists()) { // product type
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_fromDate")).exists()) { // from date
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("reportForm_toDate")).exists()) { // to date
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Goes to the Mass Update from the Product Search - Results page. This will
	 * take the input give, call up the product search results then go to the
	 * Mass Update page from there.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param rfid
	 * @param serialNumber
	 * @param productStatus
	 * @param salesAgent
	 * @param orderNumber
	 * @param purchaseOrder
	 * @param jobSite
	 * @param assignedTo
	 * @param customer
	 * @param division
	 * @param location
	 * @param productType
	 * @param fromDate
	 * @param toDate
	 * @throws Exception
	 */
	public void gotoMassUpdateProducts(IE ie, String rfid, String serialNumber,
			String productStatus, String salesAgent, String orderNumber,
			String purchaseOrder, String jobSite, String assignedTo,
			String customer, String division, String location,
			String productType, String fromDate, String toDate)
			throws Exception {

		gotoProductSearchResults(ie, rfid, serialNumber, productStatus,
				salesAgent, orderNumber, purchaseOrder, jobSite, assignedTo,
				customer, division, location, productType, fromDate, toDate);
		ie.link(text("Mass Update")).click();
		ie.waitUntilReady();
	}

	/**
	 * Checks the Mass Update page, from Assets, for all the correct fields and buttons.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param jobsite
	 * @throws Exception
	 */
	public void validateProductMassUpdate(IE ie, boolean jobsite)
			throws Exception {
		if (!ie.containsText("Mass Update Products")) {
			throw new TestCaseFailedException();
		} else if (!ie.link(text("Return to search")).exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.button(id("massUpdateProductsSave_hbutton_save"))
				.exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.selectList(id("massUpdateProductsSave_productStatus"))
				.exists()) { // product status
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("massUpdateProductsSave_purchaseOrder"))
				.exists()) { // purchase order
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("massUpdateProductsSave_location"))
				.exists()) { // location
			throw new TestCaseFailedException();
		}

		if (!jobsite) {
			if (!ie.selectList(id("massUpdateProductsSave_customer")).exists()) { // customer
																					// name
				throw new TestCaseFailedException();
			} else if (!ie.selectList(id("division")).exists()) { // division
				throw new TestCaseFailedException();
			}
		} else {
			if (!ie.selectList(id("massUpdateProductsSave_jobSite")).exists()) { // job
																					// site
				throw new TestCaseFailedException();
			} else if (!ie
					.selectList(id("massUpdateProductsSave_assignedUser"))
					.exists()) { // assigned to
				throw new TestCaseFailedException();
			}
		}
	}

	/**
	 * Goes to the My Account page. Assumes you are logged in.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoMyAccount(IE ie) throws Exception {
		Link l = ie.link(text("My Account"));
		if (l != null && l.exists()) {
			l.click();
			ie.waitUntilReady();
		}
	}

	/**
	 * Goes to the My Account page and validates the information on it.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param userName
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param position
	 * @param initials
	 * @param orgUnit
	 * @throws Exception
	 */
	public void validateMyAccount(IE ie, String userName, String email,
			String firstName, String lastName, String position,
			String initials, String orgUnit) throws Exception {
		gotoMyAccount(ie);
		if (!ie.htmlElement(tag("h1")).text().equals("My Account")) // page
																	// header
			throw new TestCaseFailedException();
		else if (!ie.label(text("/User Name/")).exists())
			throw new TestCaseFailedException();
		else if (!ie.label(text("/First Name/")).exists())
			throw new TestCaseFailedException();
		else if (!ie.label(text("/Position/")).exists())
			throw new TestCaseFailedException();
		else if (!ie.label(text("/Organizational Unit/")).exists())
			throw new TestCaseFailedException();
		else if (!ie.label(text("/Email/")).exists())
			throw new TestCaseFailedException();
		else if (!ie.label(text("/Last Name/")).exists())
			throw new TestCaseFailedException();
		else if (!ie.label(text("/Initials/")).exists())
			throw new TestCaseFailedException();
		if (userName != null && !ie.span(text(userName)).exists())
			throw new TestCaseFailedException();
		if (email != null && !ie.span(text(email)).exists())
			throw new TestCaseFailedException();
		if (firstName != null && !ie.span(text(firstName)).exists())
			throw new TestCaseFailedException();
		if (lastName != null && !ie.span(text(lastName)).exists())
			throw new TestCaseFailedException();
		if (position != null && !ie.span(text(position)).exists())
			throw new TestCaseFailedException();
		if (initials != null && !ie.span(text(initials)).exists())
			throw new TestCaseFailedException();
		if (orgUnit != null && !ie.span(text(orgUnit)).exists())
			throw new TestCaseFailedException();
	}

	/**
	 * Goes to the Request An Account page. Assumes you are at the login page.
	 * If you are not at the login page this method will do nothing. Otherwise
	 * it just clicks the link to go to the Request An Account page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoRequestAnAccount(IE ie) throws Exception {
		Link l = ie.link(text("/I want to request an account/"));
		if (l.exists()) {
			l.click();
			ie.waitUntilReady();
		}
	}

	/**
	 * Requests a user account from the current tenant. It assumes you are at
	 * the login page for the tenant you wish to request an account for. This
	 * will automatically go to the Request An Account page. It will also work
	 * from the Request An Account page because the gotoRequestAnAccount will do
	 * nothing if you are not at the login page.
	 * 
	 * It is a requirement of Field ID that you provide a userID and
	 * emailAddress. All other parameters can be set to null and will be
	 * ignored.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param userID
	 * @param emailAddress
	 * @param firstName
	 * @param lastName
	 * @param position
	 * @param timeZone
	 * @param company
	 * @param phone
	 * @param password
	 * @param verifyPassword
	 * @param comments
	 * @param abort
	 * @param reset
	 * @throws Exception
	 */
	public void requestAnAccount(IE ie, String userID, String emailAddress,
			String firstName, String lastName, String position,
			String timeZone, String company, String phone, String password,
			String verifyPassword, String comments, boolean abort, boolean reset)
			throws Exception {
		gotoRequestAnAccount(ie);

		Link returnToLogin = ie.link(text("Return to login page."));
		if (!returnToLogin.exists())
			throw new TestCaseFailedException(); // if link is missing, fail

		if (abort) {
			returnToLogin.click();
			ie.waitUntilReady();
		}

		if (reset) {
			ie.button(value("Reset Form")).click();
			ie.waitUntilReady();
		}

		if (userID != null)
			ie.textField(id("registerUser_userId")).set(userID);
		else
			throw new TestCaseFailedException();

		if (emailAddress != null)
			ie.textField(id("registerUser_emailAddress")).set(emailAddress);
		else
			throw new TestCaseFailedException();

		if (firstName != null)
			ie.textField(id("registerUser_firstName")).set(firstName);

		if (lastName != null)
			ie.textField(id("registerUser_lastName")).set(lastName);

		if (position != null)
			ie.textField(id("registerUser_position")).set(position);

		if (timeZone != null)
			ie.selectList(id("registerUser_timeZone")).option(text(timeZone)).select();

		if (company != null)
			ie.textField(id("registerUser_companyName")).set(company);
		else
			throw new TestCaseFailedException();

		if (phone != null)
			ie.textField(id("registerUser_phoneNumber")).set(phone);
		else
			throw new TestCaseFailedException();

		if (password != null)
			ie.textField(id("registerUser_password")).set(password);
		else
			throw new TestCaseFailedException();

		if (verifyPassword != null)
			ie.textField(id("registerUser_passwordConfirmation")).set(verifyPassword);
		else
			throw new TestCaseFailedException();

		if (comments != null)
			ie.textField(id("registerUser_comment")).set(comments);

		ie.button(id("registerUser_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * After a user has requested an account they are sent to the Register New
	 * User page. This method will validate the contents of the page. It makes
	 * some assumptions about the message sent to the user. If the format of the
	 * message changes, check the new page for grammar errors and update this
	 * test case if necessary.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param userID
	 * @param emailAddress
	 * @throws Exception
	 */
	public void validateRegisterNewUserPage(IE ie, String userID,
			String emailAddress) throws Exception {
		if (!ie.htmlElement(tag("h1")).text().matches("Register New User")) {
			throw new TestCaseFailedException();
		} else if (!ie
				.containsText("/Your account has been created with the user name "
						+ userID + " and email address " + emailAddress + "./")) {
			throw new TestCaseFailedException();
		} else if (!ie
				.containsText("/The administrator of the account will need to verify your information before you can log in./")) {
			throw new TestCaseFailedException();
		} else if (!ie
				.containsText("/You will receive an email when your login has been activated./")) {
			throw new TestCaseFailedException();
		} else if (!ie.link(text("/Return to login page./")).exists()) {
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Go to the Manage Customers page. Assumes you have Administration
	 * privilege.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageCustomers(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Customers")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add a new customer to the current tenant. Assumes you have Administration
	 * privileges and that the customer does not already exist. If the customer
	 * exists this method will return without error or exception but the test
	 * case will be left at the page for adding a new customer with an error
	 * message displayed on the page.
	 * 
	 * Field ID assumes the customerId and customerName are set. All other
	 * fields can be set to null.
	 * 
	 * If the customer is successfully added, you will be left at the Show
	 * Customer page. At this page you can add customer users and divisions or
	 * go back to the customer list.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param contactName
	 * @param contactEmail
	 * @param address
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param country
	 * @param phone1
	 * @param phone2
	 * @param fax
	 * @throws Exception
	 */
	public void addCustomer(IE ie, String customerID, String customerName,
			String contactName, String contactEmail, String address,
			String city, String state, String zipCode, String country,
			String phone1, String phone2, String fax) throws Exception {
		gotoManageCustomers(ie);
		ie.link(text("/Add/")).click();
		ie.waitUntilReady();
		if (customerID != null)
			ie.textField(id("customerEdit_customerId")).set(customerID);
		if (customerName != null)
			ie.textField(id("customerEdit_customerName")).set(customerName);
		if (contactName != null)
			ie.textField(id("customerEdit_contactName")).set(contactName);
		if (contactEmail != null)
			ie.textField(id("customerEdit_accountManagerEmail")).set(
					contactEmail);
		if (address != null)
			ie.textField(id("customerEdit_addressInfo_streetAddress")).set(
					address);
		if (city != null)
			ie.textField(id("customerEdit_addressInfo_city")).set(city);
		if (state != null)
			ie.textField(id("customerEdit_addressInfo_state")).set(state);
		if (zipCode != null)
			ie.textField(id("customerEdit_addressInfo_zip")).set(zipCode);
		if (country != null)
			ie.textField(id("customerEdit_addressInfo_country")).set(country);
		if (phone1 != null)
			ie.textField(id("customerEdit_addressInfo_phone1")).set(phone1);
		if (phone2 != null)
			ie.textField(id("customerEdit_addressInfo_phone2")).set(phone2);
		if (fax != null)
			ie.textField(id("customerEdit_addressInfo_fax1")).set(fax);

		ie.button(id("customerEdit_hbutton_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add a divison to a customer. Assumes the customer exists.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param division
	 * @throws Exception
	 */
	public void addCustomerDivision(IE ie, String customerID,
			String customerName, String division) throws Exception {
		gotoShowCustomer(ie, customerID, customerName);
		ie.link(text("Divisions")).click();
		ie.waitUntilReady();
		ie.textField(id("newDivisionName")).set(division);
		ie.button(value("Submit")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add a customer user to a customer account. It assumes the customer
	 * exists. Additionally, if you specify a division it assumes the division
	 * exists.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param userID
	 * @param userEmail
	 * @param firstName
	 * @param lastName
	 * @param position
	 * @param initials
	 * @param securityRFID
	 * @param timeZone
	 * @param division
	 * @param password
	 * @param verifyPassword
	 * @param createInspections
	 * @param editInspections
	 * @throws Exception
	 */
	public void addCustomerUser(IE ie, String customerID, String customerName,
			String userID, String userEmail, String firstName, String lastName,
			String position, String initials, String securityRFID,
			String timeZone, String division, String password,
			String verifyPassword, boolean createInspections,
			boolean editInspections) throws Exception {
		gotoShowCustomer(ie, customerID, customerName);
		ie.link(text, "Add Customer User").click();
		ie.waitUntilReady();
		if (userID != null)
			ie.textField(id("customerUserSave_userId")).set(userID);
		if (userEmail != null)
			ie.textField(id("customerUserSave_emailAddress")).set(userEmail);
		if (firstName != null)
			ie.textField(id("firstname")).set(firstName);
		if (lastName != null)
			ie.textField(id("lastname")).set(lastName);
		if (position != null)
			ie.textField(id("customerUserSave_position")).set(position);
		if (initials != null)
			ie.textField(id("initials")).set(initials);
		if (securityRFID != null)
			ie.textField(id("customerUserSave_securityRfidNumber")).set(securityRFID);
		if (timeZone != null)
			ie.selectList(id("customerUserSave_timeZoneID")).option(text(timeZone)).select();
		if (division != null)
			ie.selectList(id("division")).option(text(division)).select();
		if (password != null)
			ie.textField(id("customerUserSave_password")).set(password);
		if (verifyPassword != null)
			ie.textField(id("customerUserSave_passwordConfirmation")).set(verifyPassword);
		if (createInspections)
			ie.radio(id("customerUserSave_userPermissions_'" +  InspectPermissions.CREATE.value() + "'_true")).set();
		else
			ie.radio(id("customerUserSave_userPermissions_'" +  InspectPermissions.CREATE.value() + "'_false")).set();
		if (editInspections)
			ie.radio(id("customerUserSave_userPermissions_'" +  InspectPermissions.EDIT.value() + "'_true")).set();
		else
			ie.radio(id("customerUserSave_userPermissions_'" +  InspectPermissions.EDIT.value() + "'_false")).set();

		ie.button(id("customerUserSave_save")).click();
		ie.waitUntilReady();
		Div error = ie.div(xpath("//DIV[@class='errorMessage']"));
		if(error.exists()) {
			throw new TestCaseFailedException();
		}
	}

	public void addEmployeeUser(IE ie, String userID, String emailAddress,
			String firstName, String lastName, String position,
			String initials, String securityRFID, String timeZone,
			String orgUnit, String password, String verifyPassword,
			int[] permissions) throws Exception {
		gotoAddEmployeeUser(ie);
		ie.textField(id("userSave_userId")).set(userID);
		ie.textField(id("userSave_emailAddress")).set(emailAddress);
		if(firstName != null) {
			ie.textField(id("firstname")).set(firstName);
		}
		if(lastName != null) {
			ie.textField(id("lastname")).set(lastName);
		}
		if(position != null) {
			ie.textField(id("userSave_position")).set(position);
		}
		if(initials != null) {
			ie.textField(id("initials")).set(initials);
		}
		if(securityRFID != null) {
			ie.textField(id("userSave_securityRfidNumber")).set(securityRFID);
		}
		if(timeZone != null) {
			ie.selectList(id("userSave_timeZoneID")).option(text(timeZone)).select();
		}
		if(orgUnit != null) {
			ie.selectList(id("userSave_organizationalUnit")).option(text(orgUnit)).select();
		}
		if(password != null) {
			ie.textField(id("userSave_password")).set(password);
		}
		if(verifyPassword != null) {
			ie.textField(id("userSave_passwordConfirmation")).set(verifyPassword);
		}
		for(int i = 0; i < permissions.length; i++) {
			ie.radio(id("userSave_userPermissions_'" +  permissions[i] + "'_true")).set();
		}
		ie.button(id("userSave_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Given a customer ID and customer name, go to the Show Customer page. This
	 * is the page where you can add users and divisions for that customer.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @throws Exception
	 */
	public void gotoShowCustomer(IE ie, String customerID, String customerName)
			throws Exception {
		gotoManageCustomers(ie);
		ie.textField(id("listFilter")).set(customerName);
		ie.button(id("customerList_hbutton_filter")).click();
		ie.waitUntilReady();
		Link l = ie.link(text(customerName + " - (" + customerID + ")"));
		if (l != null && l.exists()) {
			l.click();
			ie.waitUntilReady();
		}
	}

	/**
	 * Checks to see if a customer exists by going to Manage Customer and
	 * checking the list of customers.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @return true if administration second list the user as a customer, not
	 *         employee
	 * @throws Exception
	 */
	public boolean isCustomer(IE ie, String customerID, String customerName)
			throws Exception {
		gotoManageCustomers(ie);
		ie.textField(id("listFilter")).set(customerName);
		ie.button(id("customerList_hbutton_filter")).click();
		ie.waitUntilReady();
		Link l = ie.link(text(customerName + " - (" + customerID + ")"));
		if (l != null)
			return l.exists();
		return false;
	}

	/**
	 * Checks to see if a user exists for the given customer. Assumes the
	 * customer exists and you have administration privileges.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param userID
	 * @return true if the given user ID is a user for the given customer
	 */
	public boolean isCustomerUser(IE ie, String customerID,
			String customerName, String userID) throws Exception {
		gotoShowCustomer(ie, customerID, customerName);
		Link l = ie.link(text(userID));
		if (l != null)
			return l.exists();
		return false;
	}

	/**
	 * Remove an existing user from a given customer. Assumes the customer
	 * exists and you have administration privileges.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerName
	 * @param userID
	 */
	public void deleteCustomerUser(final IE ie, String customerID,
			String customerName, String userID) throws Exception {
		gotoShowCustomer(ie, customerID, customerName);

		String s = ie.link(text(userID)).href(); // find the link to edit the user

		// get the user's unique ID from the edit link
		String uniqueIDstr = "uniqueID=";
		int i = s.indexOf(uniqueIDstr) + uniqueIDstr.length();
		int j = s.indexOf('&', i);
		String uniqueID;
		if (j != -1)
			uniqueID = s.substring(i, j);
		else
			uniqueID = s.substring(i);

		// find the link with the unique ID and the text 'Remove'
		Links ls = ie.links(text("Remove"));
		for (i = 0; i < ls.length(); i++) {
			Link l = ls.get(i);
			if (l.html().contains(uniqueID)) {
				// set up a thread to answer the "Are you sure?" dialog
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1000);
							Wnd w = Wnd.findWindow("#32770", "Windows Internet Explorer");
							IEPromptDialog confirm = new IEPromptDialog(w, ie);
							confirm.ok();
						} catch (Exception e) {
						}
					}
				}).start();
				l.click(); // will block until thread closes the "Are you sure?" dialog
				break;
			}
		}
	}

	/**
	 * Checks to see if a product type exists. It only checkes to see if the
	 * name exists. It does not check to see if the settings for the product
	 * match anything.
	 * 
	 * Assumes you have administration privileges. Assumes you are logged in.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productType
	 * @return true if the product type exists
	 * @throws Exception
	 */
	public boolean isProductType(IE ie, String productType) throws Exception {
		gotoManageProductTypes(ie);
		Link l = ie.link(text(productType));
		if (l != null && l.exists())
			return true;
		return false;
	}

	/**
	 * Adds a product type to the system. Does not check to see if the product
	 * type exists. Will fail if the product type already exists.
	 * 
	 * TODO: Adding a product image is not implemented yet. If you pass in
	 * anything but null it will throw an Exception.
	 * 
	 * TODO: Adding 'attributes' is currently not implemented. If you pass in
	 * anything but null it will throw an Exception.
	 * 
	 * TODO: Adding 'attachments' is not implemented yet. If you pass in
	 * anything but null it will throw an Exception.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param productType
	 * @param warnings
	 * @param instructions
	 * @param cautionURL
	 * @param hasManufacturerCert
	 * @param manufacturerCertText
	 * @param productDescriptionTemplate
	 * @param productImage
	 * @param attributes
	 * @param attachments
	 * @throws Exception
	 */
	public void addProductType(IE ie, String productType, String warnings,
			String instructions, String cautionURL,
			boolean hasManufacturerCert, String manufacturerCertText,
			String productDescriptionTemplate, String productImage,
			ProductTypeAttributes[] attributes, String[] attachments)
			throws Exception {
		gotoManageProductTypes(ie);
		ie.link(text("Add Product Type")).click();
		ie.waitUntilReady();
		if (productType != null)
			ie.textField(id("productTypeUpdate_name")).set(productType);
		if (warnings != null)
			ie.textField(id("productTypeUpdate_warnings")).set(warnings);
		if (instructions != null)
			ie.textField(id("productTypeUpdate_instructions"))
					.set(instructions);
		if (cautionURL != null)
			ie.textField(id("productTypeUpdate_cautionsUrl")).set(cautionURL);
		ie.checkbox(id("productTypeUpdate_hasManufacturerCertificate")).set(
				hasManufacturerCert);
		if (manufacturerCertText != null)
			ie.textField(id("productTypeUpdate_manufacturerCertificateText"))
					.set(manufacturerCertText);
		if (productDescriptionTemplate != null)
			ie.textField(id("productTypeUpdate_descriptionTemplate")).set(
					productDescriptionTemplate);
		if (productImage != null) {
			throw new NotImplementedYetException();
		}
		if (attributes != null)
			throw new NotImplementedYetException();
		if (attachments != null) {
			for (int i = 0; i < attachments.length; i++) {
				// ie.button(text("Attach A File")).click();
				// ie.waitUntilReady();
				throw new NotImplementedYetException();
			}
		}

		ie.button(id("productTypeUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Checks to see if product type two has at least one of product type one as
	 * a sub-product. If there are more than one it will still return true.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param subProductType
	 * @param masterProductType
	 * @return true if product type 1 is a sub-product of product type 2
	 * @throws Exception
	 */
	public boolean isProductTypeOneASubProductOfProductTypeTwo(IE ie,
			String subProductType, String masterProductType) throws Exception {
		gotoProductTypeConfiguration(ie, masterProductType);
		if (ie.span(text, subProductType).exists())
			return true;
		return false;
	}

	/**
	 * Go to the "View Product Type - {productType}" page. From here you can go
	 * to the various other pages. For example, the Product Configuration page.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param productType
	 * @throws Exception
	 */
	public void gotoViewProductType(IE ie, String productType) throws Exception {
		gotoManageProductTypes(ie);
		String s = java.util.regex.Pattern.quote(productType);
		ie.link(text("/" + s + "/")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Product Configuration page of a Product Type. Essentially, go
	 * to Administration, goto Manage Product Types, select the productType, go
	 * to the Product Configuration.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param productType
	 * @throws Exception
	 */
	public void gotoProductTypeConfiguration(IE ie, String productType)
			throws Exception {
		gotoViewProductType(ie, productType);
		ie.link(text("Product Configuration")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add the first product type to the Product Configuration of the second
	 * product type. This essentially makes the first product type a sub-product
	 * and the second product type a master product.
	 * 
	 * Because you can have multiple of the same sub-product type added to a
	 * master product, you can call this method repeatedly and it will just keep
	 * adding more of type one to type two.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param subProductType
	 * @param masterProductType
	 * @throws Exception
	 */
	public void AddProductTypeOneToBeSubProductOfProductTypeTwo(IE ie,
			String subProductType, String masterProductType) throws Exception {
		gotoProductTypeConfiguration(ie, masterProductType);
		ie.selectList(id("addSubProduct")).option(text(subProductType)).select();
		ie.button(id("addSubProductButton")).click();
		ie.waitUntilReady();
		ie.button(id("productTypeConfigurationUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	public void deleteCustomer(final IE ie, String customerID, String customerName) throws Exception {
		gotoEditCustomerFiltered(ie, customerID);
		Link l = ie.link(text("Remove"));
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					Wnd w = Wnd.findWindow("#32770",
							"Windows Internet Explorer");
					IEPromptDialog confirm = new IEPromptDialog(w, ie);
					confirm.ok();
				} catch (Exception e) {
				}
			}
		}).start();
		l.click();
	}

	/**
	 * Generates a random user ID. All names will begin with 'test' and end with
	 * a random number. The length parameter determines the maximum length for
	 * the user id. If the length is zero or negative this function will return
	 * a blank string, i.e. "".
	 * 
	 * @param length
	 * @return a random user ID starting with "test" and ending with a signed
	 *         long integer
	 * @throws Exception
	 */
	public String generateRandomUserID(int length) throws Exception {
		Random n = new Random();
		String user = "test" + n.nextLong();
		if (length < 1) // if length is zero or negative return ""
			return "";
		else if (length >= user.length()) // if length is larger than string,
											// return string
			return user;
		else
			return user.substring(0, length); // trim result down to length
	}

	/**
	 * TODO
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param organization
	 * @param userID
	 * @param company
	 * @param division
	 * @param createInspection
	 * @param editInspection
	 * @throws Exception
	 */
	public void acceptAccountRequest(IE ie, String organization, String userID,
			String company, String division, boolean createInspection,
			boolean editInspection) throws Exception {
		gotoManageUserRegistrationRequest(ie);
		Link l = null;
		/*
		 * Currently, the table with all the user account requests has no
		 * attributes. This code will scan all tables on the page, for each
		 * table it will scan all the rows. If it finds a row with the user id
		 * it will use the xpath to find the link at the end of that row. This
		 * SHOULD be the link to view the account request for the given user. So
		 * when these loops finish, the variable l will be the link to the view
		 * for the user account request.
		 */
		for (int table = 0; table < ie.tables().length(); table++) {
			Table t = ie.tables().get(table);
			for (int row = 0; row < t.rowCount(); row++) {
				TableRow r = t.row(row);
				if (r.html().contains(userID)) {
					l = ie.link(xpath("/HTML/BODY/DIV/DIV/TABLE/TBODY/TR[" + (row + 1) + "]/TD[last()]/A"));
				}
			}
		}
		l.click();
		ie.waitUntilReady();
		Thread.sleep(hack); // until it waits for javascript as well.
		ie.link(text("Accept")).click();

		Thread.sleep(lightbox); // delay for the lightbox to open
		ie.selectList(id("userRequestAccept_organizationalUnit")).option(
				text(organization)).select();
		ie.selectList(id("userRequestAccept_customer")).option(text(company))
				.select();
		ie.waitUntilReady();
		Thread.sleep(hack);

		if (division != null) {
			SelectList sl = ie.selectList(id("division"));
			Option o = sl.option(text(division));
			o.select();
		}

		if (createInspection)
			ie.radio(id("userRequestAccept_userPermissions_'" + InspectPermissions.CREATE.value() + "'_true")).click();
		else
			ie.radio(id("userRequestAccept_userPermissions_'" + InspectPermissions.CREATE.value() + "'_true")).click();

		if (editInspection)
			ie.radio(id("userRequestAccept_userPermissions_'" + InspectPermissions.EDIT.value() + "'_true")).click();
		else
			ie.radio(id("userRequestAccept_userPermissions_'" + InspectPermissions.EDIT.value() + "'_true")).click();

		ie.button(id("userRequestAccept_hbutton_accept")).click();
		ie.waitUntilReady();
	}

	/**
	 * TODO
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoManageUserRegistrationRequest(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage User Registration Request")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add an instance of a subproduct to a master product. It assumes the
	 * subproduct exists, the master product exists, the subproduct is a
	 * subproduct of the master product.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param subSerialNumber
	 * @param subProductType
	 * @param masterSerialNumber
	 * @param label
	 */
	public void addSubProductToMasterProduct(IE ie, String subSerialNumber,
			String subProductType, String masterSerialNumber, String label)
			throws Exception {
		gotoProductConfiguration(ie, masterSerialNumber);
		this.stopMonitorStatus();
		// find the Lookup link for the subProductType
//		Label l = ie.label(text(subProductType));
//		String lookupID = "lookUpSubProduct_" + l.htmlFor();
//		ie.link(id(lookupID)).click();
		ie.link(xpath("//DIV[@id='addComponents']/DIV[@class='component']/DIV[@class='definition']/DIV[text()='" + subProductType + "']/../DIV[@class='createOptions']/A[text()='Find Existing']")).click();
		Thread.sleep(hack);
		ie.textField(id("subProductSearchForm_search")).set(subSerialNumber);
		ie.button(id("subProductSearchForm_load")).click();
		Thread.sleep(hack);
//		Link ssn = ie.link(text(subSerialNumber));
//		String labelID = ssn.href();
//		int beginIndex = labelID.lastIndexOf('=') + 1;
//		labelID = labelID.substring(beginIndex);
//		ssn.click();
		Button select = ie.button(xpath("//DIV[@id='resultsTable']/TABLE/TBODY/TR/TD[text()='" + subProductType + "']/../TD[@class='selectAction']/BUTTON"));
		String labelID = select.html();
		String attribute = "productId=\"";
		int index = labelID.indexOf(attribute);
		labelID = labelID.substring(index + attribute.length());
		index = labelID.indexOf('\"');
		labelID = labelID.substring(0, index);
		select.click();
		Thread.sleep(hack);
		ie.link(xpath("//DIV[@id='subProductDefinition_" + labelID + "']/DIV[@class='identifier']/DIV[@class='subProductLabel ']/A")).click();
		ie.textField(id("subProductForm_" + labelID + "_subProduct_label")).set(label);
		ie.button(id("subProductForm_" + labelID + "_label_save")).click();
		this.startMonitorStatus(ie);
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Product Configuration area of a given product.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoProductConfiguration(IE ie, String serialNumber)
			throws Exception {
		gotoEditProduct(ie, serialNumber);
		ie.waitUntilReady();
		ie.link(text("/Sub-Components/")).click();
		ie.waitUntilReady();
	}

	/**
	 * This is just a helper method so I can identify which method I am
	 * currently in. Good for debug purposes. If I'm outputting debug
	 * information I can prepend or postfix the method name to the message. If I
	 * have:
	 * 
	 * void public testFooBar() { commonFieldIDMethods h = new
	 * commonFieldIDMethods(); System.out.println(h.getMethodName()); }
	 * 
	 * it will print 'testFooBar'.
	 * 
	 * @return the name of the method calling this method
	 */
	public String getMethodName() {
		StackTraceElement[] st = (new Throwable()).getStackTrace(); // generate
																	// a stack
																	// dump
		return st[1].getMethodName(); // st[0] is getMethodName, st[1] is were
										// we call this from
	}

	/**
	 * This is just a helper method so I can identify which method I am
	 * currently in. Good for debug purposes. If I'm outputting debug
	 * information I can prepend or postfix the method name to the message. If I
	 * have:
	 * 
	 * package com.n4systems.fieldid.testcase;
	 * 
	 * void public testFooBar() { commonFieldIDMethods h = new
	 * commonFieldIDMethods(); System.out.println(h.getMethodName()); }
	 * 
	 * it will print 'com.n4systems.fieldid.testcase.testFooBar
	 * (filename:linenumber)' where filename is the name of the source file
	 * containing this code and linenumber is the line on which the
	 * getMethodName call exists.
	 * 
	 * @return see description.
	 */
	public String getFullMethodName() {
		StackTraceElement[] st = (new Throwable()).getStackTrace(); // generate
																	// a stack
																	// dump
		return st[1].getClassName() + "." + st[1].getMethodName() + " ("
				+ st[1].getFileName() + ":" + st[1].getLineNumber() + ")";
	}

	/**
	 * Assumes you have performed a Product Search and are on the Product Search
	 * Result page. It simply clicks the Print all manufacturer certificates
	 * link then closes the lightbox.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void printAllManufacturerCertificates(IE ie) throws Exception {
		ie.link(text("/Print all manufacturer certificates/")).click();
		ie.waitUntilReady();
		clickLightboxOKbutton(ie);
	}

	private void clickLightboxOKbutton(IE ie) throws Exception {
		Thread.sleep(lightbox);
		HtmlElement b = ie.htmlElement(tag("button")); // assumes the lightbox OK button is only <button>
		b.click();
		Thread.sleep(hack); // wait for lightbox to close
	}

	/**
	 * Deletes the given product type. If the product type does not exist this
	 * method will throw an exception. When you delete a product type it gives a
	 * confirmation screen telling you all the things which are going to get
	 * deleted with the product type (products, inspections, etc.). This method
	 * will save a screen shot of the confirmation page under the name of the
	 * product type. For example, if you delete the product type FooBoo there
	 * will be a file called FooBoo.png created.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productType
	 * @throws Exception
	 */
	public void deleteProductType(IE ie, String productType) throws Exception {
		gotoEditProductType(ie, productType);
		ie.button(value("Delete")).click();
		ie.waitUntilReady();
		ie.button(id("label_delete")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Edit Product Type page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productType
	 * @throws Exception
	 */
	public void gotoEditProductType(IE ie, String productType) throws Exception {
		gotoManageProductTypes(ie);
		ie.link(text(productType)).click();
		ie.waitUntilReady();
		ie.link(text, "Edit Product Type").click();
		ie.waitUntilReady();
	}

	/**
	 * Check to see if a product with a given serial number exists. If the
	 * product exists, return true. Otherwise return false. It uses the 
	 * smart search on the Home page to see if the serial number or RFID
	 * is in use.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @return true if a product with the given serial number is visible to the
	 *         current user
	 * @throws Exception
	 */
	public boolean isProduct(IE ie, String serialNumber) throws Exception {
		gotoHome(ie);
		TextField ss = ie.textField(id("productInformation_search"));
		ss.set(serialNumber);
		ie.button(id("productInformation_load")).click();
		ie.waitUntilReady();
		
		if (ie.containsText("There are no products with the given serial number or rfid number."))
			return false;
		return true;
	}

	/**
	 * Add an inspection type. Does not add the inspection form. Look for a
	 * separate method to set up the inspection form for a given inspection. The
	 * strings for the proof types comes TODO: attributes are not implemented
	 * yet. if this is anything but null this will throw an exception.
	 * 
	 * If group is null, go with the default inspection group.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionTypeName
	 * @param group
	 * @param printable
	 * @param master
	 * @param supportedProofTestTypes
	 * @param attributes
	 * @throws Exception
	 */
	public void addInspectionType(IE ie, String inspectionTypeName,
			String group, boolean printable, boolean master,
			String[] supportedProofTestTypes, String[] attributes)
			throws Exception {
		gotoManageInspectionTypes(ie);
		ie.link(text("/Add/")).click();
		ie.waitUntilReady();
		ie.textField(id("inspectionTypeCreate_name")).set(inspectionTypeName);
		if (group != null)
			ie.selectList(id("inspectionTypeCreate_group")).option(text(group)).select();
		ie.checkbox(id("inspectionTypeCreate_printable")).set(printable);
		ie.checkbox(id("inspectionTypeCreate_master")).set(master);
		if (supportedProofTestTypes != null) {
			for (int i = 0; i < supportedProofTestTypes.length; i++) {
				String id = "inspectionTypeCreate_supportedProofTestTypes_'"
						+ supportedProofTestTypes[i] + "'_true";
				ie.radio(id(id)).set();
			}
		}
		if (attributes != null) {
			throw new NotImplementedYetException(); // TODO
		}
		ie.button(id("inspectionTypeCreate_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add the given Inspection Type to the given Product Type. Currently, this
	 * code looks for a row that contains the Inspection Type then selects the
	 * checkbox on that row. If there are two inspections types with similar
	 * names, for example Foo and Foo2, adding Foo might result in adding Foo2
	 * because the 'contains' search will match both rows.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionTypeName
	 * @param productType
	 * @throws Exception
	 */
	public void addInspectionTypeToProductType(IE ie, String inspectionTypeName, String productType) throws Exception {
		gotoViewProductType(ie, productType);
		ie.waitUntilReady();
		ie.link(text("Select Inspection Types")).click();
		ie.waitUntilReady();
		// find the row with the inspection type on it
		TableRows rs = ie.rows(xpath("//TABLE[@class='list']/TBODY/TR"));
		for (int row = 0; row < rs.length(); row++) {
			TableRow r = rs.get(row);
			String id = r.id().replaceFirst("event_", "");
			TableCell tc = r.cell(attribute("class", "name"));
			if(!tc.exists())	// skip the TH cells.
				continue;
			String itn = tc.text().trim();
			if(itn.equals(inspectionTypeName.trim())) {
				Checkbox c = r.checkbox(id(id));
				c.set(true);
				ie.waitUntilReady();
				break;
			}
		}
		ie.button(id("productTypeEventTypesSave_hbutton_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * This will add an inspection to a given product using all the default
	 * values for the inspection.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionTypeName
	 * @param serialNumber
	 * @param master
	 * @throws Exception
	 */
	public void addInspectionToProduct(IE ie, String inspectionTypeName,
			String serialNumber, boolean master) throws Exception {
		gotoInspectionGroupsForProduct(ie, serialNumber);
		ie.waitUntilReady();
		ie.link(text("/" + inspectionTypeName + "/")).click();
		ie.waitUntilReady();
		// if this is a master inspection it will take me to page where I can
		// add subcomponents
		if (master) {
			ie.link(text("/You must perform this event/")).click();
		}
		// do the inspection
		if (!master) {
			ie.button(id("inspectionCreate_hbutton_save")).click();
		} else {
			ie.button(id("baseInspectionCreate_hbutton_store")).click();
			ie.waitUntilReady();
			ie.button(id("subProductForm_label_save")).click();
		}
		ie.waitUntilReady();
	}

	/**
	 * Does what the method name says.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionTypeName
	 * @param productType
	 * @param frequencyInDays
	 * @param autoScheduled
	 * @throws Exception
	 */
	public void addInspectionScheduleToProductType(IE ie,
			String inspectionTypeName, String productType,
			String frequencyInDays, boolean autoScheduled) throws Exception {
		gotoViewProductType(ie, productType);
		ie.waitUntilReady();
		ie.link(text("Inspection Frequencies")).click();
		ie.waitUntilReady();
		TableRows rs = ie.rows();
		String id = null;
		int row;
		for (row = 0; row < rs.length(); row++) {
			String r = rs.get(row).html();
			if (r.contains(inspectionTypeName)) {
				break;
			}
		}
		// if row < rs.length we found it
		if (row < rs.length()) {
			Link l = ie.link(xpath("//TABLE[@class='list']/TBODY/TR["
					+ (row + 1) + "]/TD[2]/DIV/SPAN[@class='actions']/A"));
			id = l.html();
			int i, j;
			i = id.indexOf("( ");
			j = id.indexOf(',', i);
			id = "schedule_" + id.substring(i + 2, j) + "_frequency";
			l.click();
			Thread.sleep(hack);
			ie.textField(id(id)).set(frequencyInDays);
			ie
					.checkbox(
							xpath("//TABLE[@class='list']/TBODY/TR["
									+ (row + 1)
									+ "]/TD[2]/DIV/FORM/SPAN[@class='autoSchedule']/INPUT[1]"))
					.set(autoScheduled);
			ie.link(
					xpath("//TABLE[@class='list']/TBODY/TR[" + (row + 1)
							+ "]/TD[2]/DIV/FORM/SPAN[@class='actions']/A[1]"))
					.click();
		}
	}

	/**
	 * If the first product is a sub-product of the second product this
	 * method will return true. It actually checks to see if there is a
	 * link from the sub-product to the master product and returns
	 * whether this link exists.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param subSerialNumber
	 * @param masterSerialNumber
	 * @return true if product 1 is a sub-product of product 2
	 * @throws Exception
	 */
	public boolean isProductOneASubProductOfProductTwo(IE ie,
			String subSerialNumber, String masterSerialNumber) throws Exception {
		gotoProductInfo(ie, subSerialNumber);
		return ie.link(text(masterSerialNumber)).exists();
	}

	/**
	 * Go to the Manage Inspection Books section and add an inspection book.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customer
	 * @param title
	 * @param open
	 * @throws Exception
	 */
	public void addInspectionBook(IE ie, String customer, String title,
			boolean open) throws Exception {
		gotoManageInspectionBooks(ie);
		ie.link(text("Add Inspection Book")).click();
		ie.waitUntilReady();
		ie.textField(id("inspectionBookCreate_name")).set(title);
		ie.selectList(id("inspectionBookCreate_customerId")).option(
				text(customer)).select();
		ie.checkbox(id("inspectionBookCreate_open")).set(open);
		ie.button(id("inspectionBookCreate_hbutton_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Returns true if an inspection book exists.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customer
	 * @param title
	 * @return true if inspection book exists
	 * @throws Exception
	 */
	public boolean isInspectionBook(IE ie, String customer, String title)
			throws Exception {
		gotoManageInspectionBooks(ie);

		Link l = ie.link(text("Next>"));
		do {
			Links ls = ie.links(text(title));
			for (int i = 0; i < ls.length(); i++) {
				Link t = ls.get(i);
				t.click();
				ie.waitUntilReady();
				SelectList sl = ie.selectList(id("inspectionBookUpdate_customerId"));
				List<String> c = sl.getSelectedItems();
				if (c.get(0).equals(customer)) {
					return true;
				} else {
					ie.back();
					ie.waitUntilReady();
				}
			}
			if (l.exists()) {
				l.click();
				ie.waitUntilReady();
				l = ie.link(text("Next>"));
			} else {
				break;
			}
		} while (true);

		return false;
	}

	/**
	 * If the Administration icon is available on the navbar this returns true,
	 * otherwise it returns false.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if user can see Administration icon
	 * @throws Exception
	 */
	public boolean isAdministration(IE ie) throws Exception {
		return ie.link(id("menuAdministration")).exists();
	}

	/**
	 * Add a division to the specified customer.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param division
	 * @throws Exception
	 */
	public void addDivison(IE ie, String customerID, String customerName,
			String division) throws Exception {
		gotoEditCustomerDivisions(ie, customerID, customerName);
		ie.textField(id("newDivisionName")).set(division);
		ie.button(value("Submit")).click();
		ie.waitUntilReady();
	}

	/**
	 * Go to the Edit Customer page
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @throws Exception
	 */
	public void gotoEditCustomer(IE ie, String customerID, String customerName) throws Exception {
		String customer = customerName + " - (" + customerID + ")";
		gotoEditCustomerFiltered(ie, customerName);
		ie.link(text(customer)).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Does the gotoManageCustomer but applies a filter based on the customerID
	 * string you supply.
	 * 
	 * @param ie
	 * @param customerID
	 * @throws Exception
	 */
	public void gotoEditCustomerFiltered(IE ie, String customerID) throws Exception {
		gotoManageCustomers(ie);
		ie.textField(id("listFilter")).set(customerID);
		ie.button(id("customerList_hbutton_filter")).click();
		ie.waitUntilReady();
	}

	/**
	 * Returns true if the customer has the specified division
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param division
	 * @return true if the given division exists for the given customer
	 * @throws Exception
	 */
	public boolean isCustomerDivision(IE ie, String customerID,
			String customerName, String division) throws Exception {
		gotoEditCustomerDivisions(ie, customerID, customerName);
		boolean result = false;
		TextFields tfs = ie.textFields();
		for (int i = 0; i < tfs.length(); i++) {
			TextField tf = tfs.get(i);
			if (tf.value().equals(division)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private void gotoEditCustomerDivisions(IE ie, String customerID, String customerName) throws Exception {
		gotoEditCustomer(ie, customerID, customerName);
		ie.link(text("Divisions")).click();
		ie.waitUntilReady();
	}

	/**
	 * Replace the fields for a end user. If a field is null it will not change
	 * the value. Otherwise, this will over-write the old value and save the
	 * end user information.
	 * 
	 * @param ie - a reference to the Internet Explorer
	 * @param customerID
	 * @param customerName
	 * @param userID
	 * @param userEmail
	 * @param firstName
	 * @param lastName
	 * @param position
	 * @param initials
	 * @param securityRFID
	 * @param timeZone
	 * @param division
	 * @param createInspections
	 * @param editInspections
	 * @throws Exception
	 */
	public void editCustomerUser(IE ie, String customerID, String customerName,
			String userID, String userEmail, String firstName, String lastName,
			String position, String initials, String securityRFID,
			String timeZone, String division, boolean createInspections,
			boolean editInspections) throws Exception {
		gotoEditCustomer(ie, customerID, customerName);
		ie.link(text(userID)).click();
		ie.waitUntilReady();
		if (userID != null)
			ie.textField(id("userSave_userId")).set(userID);
		if (userEmail != null)
			ie.textField(id("userSave_emailAddress")).set(userEmail);
		if (firstName != null)
			ie.textField(id("firstname")).set(firstName);
		if (lastName != null)
			ie.textField(id("lastname")).set(lastName);
		if (position != null)
			ie.textField(id("userSave_position")).set(position);
		if (initials != null)
			ie.textField(id("initials")).set(initials);
		if (timeZone != null)
			ie.selectList(id("userSave_timeZoneID")).option(text(timeZone))
					.select();
		if (division != null)
			ie.selectList(id("division")).option(text(division)).select();
		if (createInspections)
			ie.radio(id("userSave_userPermissions_'" + InspectPermissions.CREATE.value() + "'_true")).set();
		else
			ie.radio(id("userSave_userPermissions_'" + InspectPermissions.CREATE.value() + "'_false")).set();
		if (editInspections)
			ie.radio(id("userSave_userPermissions_'" + InspectPermissions.EDIT.value() + "'_true")).set();
		else
			ie.radio(id("userSave_userPermissions_'" + InspectPermissions.EDIT.value() + "'_false")).set();

		ie.button(id("userSave_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Return true of Manage Inspection Types has a link to the inspection type.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionType
	 * @return true if the inspection type exists
	 * @throws Exception
	 */
	public boolean isInspectionType(IE ie, String inspectionType) throws Exception {
		gotoManageInspectionTypes(ie);
		if (ie.link(text(inspectionType)).exists())
			return true;
		return false;
	}

	/**
	 * Create a directory with a timestamp of the current time. The format of
	 * the dierctory will be yyyyMMdd-HHmmss, where y = year, M = month, d = day
	 * of month, H = 24 hour, m = minutes and s = seconds. If the method is
	 * successful it will return the directory it created. If it fails it will
	 * return null.
	 * 
	 * @return name of the directory which was created or null if it failed.
	 */
	public String createTimestampDirectory() {
		String timestamp = null;
		SimpleDateFormat now = new SimpleDateFormat("yyyyMMdd-HHmmss");
		timestamp = now.format(new Date());
		File d = new File(timestamp);
		if (d.mkdirs())
			return timestamp;
		return null;
	}

	/**
	 * Edit the inspection type. The proofTestType needs to be the internal name
	 * of the proof test type. If you look at the source code for the Edit
	 * Inspection Type page, you will see the id for the radio buttons have things
	 * like:
	 * 
	 * 		inspectionTypeUpdate_supportedProofTestTypes_'ROBERTS'_true
	 * 
	 * If you want this turned on, add "ROBERTS" to the proodTestType array.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @param group
	 * @param printable
	 * @param master
	 * @param proofTestType
	 * @param attributes
	 * @throws Exception
	 */
	public void editInspectionType(IE ie, String inspectionType, String group,
			boolean printable, boolean master, String[] proofTestType,
			String[] attributes) throws Exception {
		gotoManageInspectionTypes(ie);
		ie.link(text(inspectionType)).click();
		ie.waitUntilReady();
		ie.link(text("/Edit/")).click();
		ie.waitUntilReady();
		if (group != null)
			ie.selectList(id("inspectionTypeUpdate_group")).option(text(group))
					.select();
		ie.checkbox(id("inspectionTypeUpdate_printable")).set(printable);
		ie.checkbox(id("inspectionTypeUpdate_master")).set(master);
		if (proofTestType != null) {
			for (int i = 0; i < proofTestType.length; i++) {
				String id = "inspectionTypeUpdate_supportedProofTestTypes_'"
						+ proofTestType[i] + "'_true";
				ie.radio(id(id)).set();
			}
		}
		if (attributes != null) {
			throw new NotImplementedYetException(); // TODO
		}
		ie.button(id("inspectionTypeUpdate_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Return true if there is an Edit Product link from the Product Information
	 * page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if current user can edit the given product
	 * @throws Exception
	 */
	public boolean isEditProduct(IE ie, String serialNumber) throws Exception {
		gotoProductInfo(ie, serialNumber);
		if (ie.link(text("/Edit/")).exists())
			return true;
		return false;
	}

	/**
	 * Return true if the product has a Upload Multiple Proof Tests button
	 * or has a link to Start New Inspection.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param productID
	 * @return true current user can start a new inspection on given product
	 * @throws Exception
	 */
	public boolean isInspectionProduct(IE ie, String productID)
			throws Exception {
		boolean result = false;

		gotoInspect(ie);
		if (ie.button(text("/Upload Multiple Proof Tests/")).exists())
			result = true;
		gotoInspectionGroupsForProduct(ie, productID);
		if (ie.link(text("/Start New Inspection/")).exists())
			result = true;

		return result;
	}

	/**
	 * This will let you edit an inspection. As always, if any of the inputs are
	 * wrong it will throw an exception.
	 * 
	 * If you go to the Inspection Groups for a product there is no easy way to
	 * identify which inspection you want to edit. The whichInspection variable
	 * is just a count. If you expand all the groups and start counting from
	 * zero, that is the whichInspection number. A value of whichInspection = 0
	 * will give you the first inspection.
	 * 
	 * Don't mix jobSite and customer/division. It will be one or the other.
	 * 
	 * The comments variable will replace the existing comments. In future, I
	 * will provide a method to get the comment from the inspection. You can
	 * then get the comment, add to it programmatically, then use this to put
	 * the new, combined comment back.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @param whichInspection
	 * @param inspectionDate
	 * @param inspector
	 * @param jobSite
	 * @param customer
	 * @param division
	 * @param location
	 * @param inspectionBook
	 * @param result
	 * @param comments
	 * @param printable
	 * @param charge
	 * @throws Exception
	 */
	public void editInspection(IE ie, String serialNumber, int whichInspection,
			String inspectionDate, String inspector, String jobSite,
			String customer, String division, String location,
			String inspectionBook, String result, String comments,
			boolean printable, String charge) throws Exception {

		editInspectionHelper(ie, serialNumber, whichInspection);

		if (inspectionDate != null)
			ie.textField(id("inspectionDate")).set(inspectionDate);
		if (inspector != null)
			ie.selectList(id("inspectionUpdate_inspector")).option(
					text(inspector)).select();
		if (jobSite != null)
			ie.selectList(id("inspectionUpdate_jobSite")).option(text(jobSite))
					.select();
		if (customer != null)
			ie.selectList(id("customer")).option(
					text(customer)).select();
		if (division != null)
			ie.selectList(id("division")).option(text(division)).select();
		if (location != null)
			ie.textField(id("inspectionUpdate_location")).set(location);
		if (inspectionBook != null)
			ie.selectList(id("inspectionBooks")).option(text(inspectionBook))
					.select();
		if (result != null)
			ie.selectList(id("inspectionUpdate_result")).option(text(result))
					.select();
		if (comments != null)
			ie.textField(id("comments")).set(comments);
		ie.checkbox(id("inspectionUpdate_printable")).set(printable);
		if (charge != null)
			ie.textField(id("inspectionUpdate_charge")).set(charge);

		ie.button(id("inspectionUpdate_hbutton_save")).click();
		ie.waitUntilReady();
	}

	private void editInspectionHelper(IE ie, String serialNumber, int whichInspection) throws Exception {
		gotoInspectionGroupsForProduct(ie, serialNumber);

		String[] ids = getInspectionIdsFromInspectionGroupsForProducts(ie);
		if (ids == null || ids.length < whichInspection)
			throw new TestCaseFailedException();

		gotoEditInspectionFromInspectionGroupsForProducts(ie, ids[whichInspection]);
	}

	public void editMasterInspection(IE ie, String serialNumber, int whichInspection, String inspectionDate, String inspector,
			String jobSite, String customer, String division, String location, String inspectionBook, String result, String comments,
			boolean printable, String charge) throws Exception {
		editInspectionHelper(ie, serialNumber, whichInspection);
//		Link l = ie.link(text("/edit this event/"));
	}

	/**
	 * Goes to the last page of the Product, Reporting or Schedule Search
	 * results. Assumes you have already gone to gotoProductSearchResults,
	 * gotoReportSearchResults or gotoScheduleSearchResults. If there is only
	 * one page this will do nothing as the 'Last' link will not exist.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoLastPageSearchResults(IE ie) throws Exception {
		Link last = ie.link(text("Last"));
		if (last.exists())
			last.click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the previous page of the Product, Reporting or Schedule Search
	 * results. Assumes you have already gone to gotoProductSearchResults,
	 * gotoReportSearchResults or gotoScheduleSearchResults. If there is only
	 * one page this will do nothing as the 'Previous' link will not exist.
	 * Additionally, if you are on page 1 it will do nothing as there is no
	 * 'Previous' page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoPreviousPageSearchResults(IE ie) throws Exception {
		Link prev = ie.link(text("/Previous/"));
		if (prev.exists())
			prev.click();
		ie.waitUntilReady();
	}

	/**
	 * Gives the search results based on input. If user logged in is an end user
	 * they will not be able to select customer. They should pass in null for
	 * customer. If they are a division customer they will not be able to select
	 * the division either.
	 * 
	 * @param ie
	 * @param inspectionBook
	 * @param rfidNumber
	 * @param serialNumber
	 * @param inspectionTypeGroup
	 * @param inspectorName
	 * @param orderNumber
	 * @param purchaseOrder
	 * @param jobSite
	 * @param assignedTo
	 * @param customer
	 * @param division
	 * @param productType
	 * @param productStatus
	 * @param fromDate
	 * @param toDate
	 * @throws Exception
	 */
	public void gotoReportSearchResults(IE ie, String inspectionBook,
			String rfidNumber, String serialNumber, String inspectionTypeGroup,
			String inspectorName, String orderNumber, String purchaseOrder,
			String jobSite, String assignedTo, String customer,
			String division, String productType, String productStatus,
			String fromDate, String toDate) throws Exception {
		gotoReporting(ie);
		if (inspectionBook != null)
			ie.selectList(id("reportForm_criteria_inspectionBook")).option(text(inspectionBook)).select();
		if (rfidNumber != null)
			ie.textField(id("reportForm_criteria_rfidNumber")).set(rfidNumber);
		if (serialNumber != null)
			ie.textField(id("reportForm_criteria_serialNumber")).set(serialNumber);
		if (inspectionTypeGroup != null)
			ie.selectList(id("reportForm_criteria_inspectionTypeGroup")).option(
					text(inspectionTypeGroup)).select();
		if (inspectorName != null)
			ie.selectList(id("reportForm_criteria_inspector")).option(
					text(inspectorName)).select();
		if (orderNumber != null)
			ie.textField(id("reportForm_criteria_orderNumber")).set(orderNumber);
		if (purchaseOrder != null)
			ie.textField(id("reportForm_criteria_purchaseOrder"))
					.set(purchaseOrder);
		if (jobSite != null)
			ie.selectList(id("reportForm_criteria_jobSite")).option(text(jobSite))
					.select();
		if (assignedTo != null)
			ie.selectList(id("reportForm_criteria_assingedUser")).option(
					text(assignedTo)).select();
		if (customer != null)
			ie.selectList(id("reportForm_criteria_customer"))
					.option(text(customer)).select();
		if (division != null) {
			Thread.sleep(hack);
			ie.selectList(id("division")).option(text(division)).select();
		}
		if (productType != null)
			ie.selectList(id("reportForm_criteria_productType")).option(
					text(productType)).select();
		if (productStatus != null)
			ie.selectList(id("reportForm_criteria_productStatus")).option(
					text(productStatus)).select();
		if (fromDate != null)
			ie.textField(id("reportForm_fromDate")).set(fromDate);
		if (toDate != null)
			ie.textField(id("reportForm_toDate")).set(toDate);

		ie.button(id("reportForm_label_Run")).click();
		ie.waitUntilReady();
	}

	/**
	 * Will select all checkboxes available on a search result Select Columns
	 * form. Assumes you have already called gotoProductSearchResults,
	 * gotoReportSearchResults or gotoScheduleSearchResults.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void setAllColumnsOnSearchResults(IE ie) throws Exception {
		List<String> s = getSelectColumnsList(ie);
		setSelectColumnForSearchResult(ie, s);
	}

	/**
	 * Assuming you are on a Inspection Groups for $serialNumber page, this
	 * method will expand all the inspection groups on the page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void showAllInspectionGroups(IE ie) throws Exception {
		Links expand = ie.links(id("/^expand_/"));
		for (int i = 0; i < expand.length(); i++) {
			Link e = expand.get(i);
			e.click();
			ie.waitUntilReady();
		}
		Thread.sleep(hack);
	}

	/**
	 * Checks to see if any of the inspection groups for a given product are
	 * editable. It assumes the product exists and we can get to the Inspections
	 * for $serialNumber page.
	 * 
	 * This method will go to the Inspection Group then expand all the
	 * inspection groups before checking for an Edit link on the page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 */
	public boolean isInspectionGroupEditable(IE ie, String serialNumber)
			throws Exception {
		gotoInspectionGroupsForProduct(ie, serialNumber);
		ie.waitUntilReady();
		showAllInspectionGroups(ie);
		if (ie.link(id("/^editInspection_/")).exists())
			return true;
		return false;
	}

	/**
	 * Go to the Inspection Schedule for a given product.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoInspectionSchedule(IE ie, String serialNumber)
			throws Exception {
		gotoProductInfo(ie, serialNumber);
		Link is = ie.link(text("/Inspection Schedule/"));
		is.click();
		ie.waitUntilReady();
	}

	/**
	 * Finds the Edit link for a given Inspection Schedule. Assumes you are on
	 * the Inspection Schedule page for a specific product.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param inspectionType
	 * @return Watij Link to the Edit link on an inspection schedule
	 * @throws Exception
	 */
	private Link helperInspectionSchedule(IE ie, String inspectionType)
			throws Exception {
		TableRows rows = ie.rows();
		Link edit = null;
		int i;

		// scan all the rows for the inspection type
		for (i = 0; i < rows.length(); i++) {
			TableRow row = rows.get(i);
			if (row.html().contains(inspectionType)) {
				edit = ie.link(xpath("//TR[@id='" + row.id()
						+ "']/TD/SPAN[@class='actions']/A"));
				// id = row.id().replace("type_", "");
				break;
			}
		}
		return edit;
	}
	
	/**
	 * Edit the schedule for a given inspection type for a given product. This
	 * code assumes the product exists and its product type has the given
	 * inspection type. If the inspection cannot be found this will throw an
	 * exception.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @param inspectionType
	 * @throws Exception
	 * @deprecated this should never be used. Now there is an addInspectionSchedule and editInspectionSchedule
	 */
	public void setInspectionSchedule(IE ie, String serialNumber,
			String inspectionType, String date) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		Link edit = null;
		String id = null;
		int i;

		edit = helperInspectionSchedule(ie, inspectionType);

		if (edit == null) {
			throw new TestCaseFailedException();
		}

		id = edit.html();
		id = id.replaceFirst(".*editSchedule. ", "");
		id = id.replaceFirst(",.*", "");

		edit.click();
		ie.waitUntilReady();
		Thread.sleep(hack);
		ie.textField(id("date_" + id)).set(date);
		Link save = null;
		Links saves = ie.links();
		for (i = 0; i < saves.length(); i++) {
			if (saves.get(i).html().contains("saveSchedule( " + id + ", ")) {
				save = saves.get(i);
				break;
			}
		}

		if (save == null) {
			throw new TestCaseFailedException();
		}

		save.click();
		ie.waitUntilReady();
	}

	/**
	 * This will go to the bottom of the current page. There is a bad side
	 * effect of sendKeys that it will unmaximize the display if it is
	 * maximized. If you have maximized the display and want to restore it back
	 * to maximized, pass in true for the second parameter.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param fullScreen
	 * @throws Exception
	 */
	public void gotoBottomOfPage(IE ie, boolean fullScreen) throws Exception {
		ie.sendKeys("^{END}");
		if (fullScreen)
			ie.maximize();
		ie.waitUntilReady();
	}

	/**
	 * If the given product allows for editing the given inspection type this
	 * returns true. If we cannot edit it, this returns false.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param serialNumber
	 * @param inspectionType
	 * @return true if the current user can edit the inspection schedule
	 * @throws Exception
	 */
	public boolean isScheduleEditable(IE ie, String serialNumber,
			String inspectionType) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		String id = null;
		TableRows trs = ie.rows();
		TableRow tr = null;
		for(int i = 0; i < trs.length(); i++) {
			tr = trs.get(i);
			String s = tr.text();
			if(s.contains(inspectionType)) {
				id = tr.id().replaceFirst("type_", "");
				break;
			}
		}
		if(id == null)
			return false;
		
		if(tr.link(text("Edit")).exists()) {
			return true;
		}
		
		return false;
	}

	/**
	 * Assumes you have performed a Product Search and are on the Product Search
	 * Result page. Additionally, if you want information other than the default
	 * columns, you have called the method to change the selected columns and
	 * updated the page. It then simply clicks the Export to Excel link then
	 * closes the lightbox.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void exportToExcel(IE ie) throws Exception {
		ie.link(text("/Export to Excel/")).click();
		Thread.sleep(lightbox);
		HtmlElement b = ie.htmlElement(tag("button")); // assumes the lightbox
														// OK button is only
														// <button>
		b.click();
		Thread.sleep(hack); // wait for lightbox to close
	}

	/**
	 * Returns a list of the columns available for selection on the Select
	 * Columns form. This assumes you have created search results for Assets,
	 * Reporting or Schedule and are at a search results page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return a list of the columns available for selection on the Select
	 *         Columns form.
	 * @throws Exception
	 */
	public List<String> getSelectColumnsList(IE ie) throws Exception {
		List<String> cols = new ArrayList<String>();
		Labels columns = ie.labels(xpath("//FORM[@id='reportForm']/DIV[@class='pageSection']/DIV[@id='selectColumnForm']/DIV/DIV/LABEL"));
		for (int i = 0; i < columns.length(); i++) {
			Label column = columns.get(i);
			cols.add(column.text());
		}
		return cols;
	}

	/**
	 * Set the columns for the given search results. This assumes you have
	 * created search results for Assets, Reporting or Schedule and are at the
	 * search results page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param columns
	 * @throws Exception
	 */
	public void setSelectColumnForSearchResult(IE ie, List<String> columns)
			throws Exception {
		ie.link(id("expandSection_reportForm")).click();
		ie.link(id("open_selectColumnForm")).click();
		ie.waitUntilReady();
		Iterator<String> i = columns.iterator();
		while (i.hasNext()) {
			String column = i.next();
			setSelectColumnForSearchResult(ie, column);
		}
		ie.button(id("reportForm_label_Run")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * Lets you set the given column on. This is really a helper function for
	 * setSelectColumnForSearchResult. The setSelectColumnForSearchResult method
	 * will set the columns and update the results. This will just set the
	 * column on but it will not click the update button.
	 * 
	 * In 2009.2 release the search result page changed so the Refine and Column
	 * Select are on the same page. this means the label for the Refine and the
	 * label for the Column Select exists. I need to search for all labels to
	 * get the for attribute but then I have to filter on the for attribute to
	 * get elements which are checkbox and not textField or selectField.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param column
	 * @throws Exception
	 */
	public void setSelectColumnForSearchResult(IE ie, String column)
			throws Exception {
		Labels ls = ie.labels(text(column));
		for(int i = 0; i < ls.length(); i++) {
			Label l = ls.get(i);
			Checkbox c = ie.checkbox(id(l.htmlFor()));
			if(c.exists()) {
				ie.checkbox(id(l.htmlFor())).set();
			}
		}
	}

	/**
	 * Clear the columns for the given search results. This assumes you have
	 * created search results for Assets, Reporting or Schedule and are at the
	 * search results page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param columns
	 * @throws Exception
	 */
	public void clearSelectColumnForSearchResult(IE ie,
			List<String> columns) throws Exception {
		ie.link(id("open_selectColumnForm")).click();
		Iterator<String> i = columns.iterator();
		while (i.hasNext()) {
			String column = i.next();
			clearSelectColumnForSearchResult(ie, column);
		}
		ie.button(id("selectTableColumns_hbutton_update")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * Lets you set the given column on. This is really a helper function for
	 * clearSelectColumnForSearchResult. The clearSelectColumnForSearchResult
	 * method will clear the columns and update the results. This will just
	 * clear the column on but it will not click the update button.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param column
	 * @throws Exception
	 */
	public void clearSelectColumnForSearchResult(IE ie, String column)
			throws Exception {
		Label l = ie.label(text(column));
		ie.checkbox(id(l.htmlFor())).clear();
	}

	/**
	 * Toggle the columns for the given search results. This assumes you have
	 * created search results for Assets, Reporting or Schedule and are at the
	 * search results page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param columns
	 * @throws Exception
	 */
	public void toggleSelectColumnForSearchResult(IE ie,
			List<String> columns) throws Exception {
		ie.link(id("open_selectColumnForm")).click();
		Iterator<String> i = columns.iterator();
		while (i.hasNext()) {
			String column = i.next();
			toggleSelectColumnForSearchResult(ie, column);
		}
		ie.button(id("selectTableColumns_hbutton_update")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * Lets you toggle the given column on. This is really a helper function for
	 * toggleSelectColumnForSearchResult. The toggleSelectColumnForSearchResult
	 * method will toggle the columns and update the results. This will just
	 * toggle the column on but it will not click the update button.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param column
	 * @throws Exception
	 */
	public void toggleSelectColumnForSearchResult(IE ie, String column)
			throws Exception {
		Label l = ie.label(text(column));
		Checkbox c = ie.checkbox(id(l.htmlFor()));
		boolean state = c.isSet();
		c.set(!state);
	}

	/**
	 * Assumes you are on a search result page. Checks to see if a 'Mass Update'
	 * link exists.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @return true if the Mass Update link exists
	 * @throws Exception
	 */
	public boolean isMassUpdateAvailable(IE ie) throws Exception {
		if (ie.link(text("/Mass Update/")).exists())
			return true;
		return false;
	}

	/**
	 * Selects the Print->This Report link on the current Report search results
	 * page. Assumes you have called the gotoReportSearchResults method.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void printReportingThisReport(IE ie) throws Exception {
		Link l = ie.link(text("This Report"));
		l.click();
		ie.waitUntilReady();
		clickLightboxOKbutton(ie);
	}

	/**
	 * Validates the Mass Update page from Report Results. 
	 * Assumes you are already on the page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @param jobsite
	 * @throws Exception
	 */
	public void validateReportingMassUpdate(IE ie, boolean jobsite)
			throws Exception {
		if (!ie.containsText("Mass Update Products")) {
			throw new TestCaseFailedException();
		} else if (!ie.link(text("Return to Report")).exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.button(id("massUpdateInspectionsSave_hbutton_save"))
				.exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.selectList(
				id("massUpdateInspectionsSave_inspectionBook")).exists()) { // inspection
																			// book
			throw new TestCaseFailedException();
		} else if (!ie.textField(id("massUpdateInspectionsSave_location"))
				.exists()) { // location
			throw new TestCaseFailedException();
		} else if (!ie.checkbox(id("massUpdateInspectionsSave_printable"))
				.exists()) { // printable
			throw new TestCaseFailedException();
		}

		if (!jobsite) {
			if (!ie.selectList(id("massUpdateInspectionsSave_customer"))
					.exists()) { // customer name
				throw new TestCaseFailedException();
			} else if (!ie.selectList(id("division")).exists()) { // division
				throw new TestCaseFailedException();
			}
		} else {
			if (!ie.selectList(id("massUpdateInspectionsSave_jobSite"))
					.exists()) { // job site
				throw new TestCaseFailedException();
			} else if (!ie
					.selectList(id("massUpdateProductsSave_assignedUser"))
					.exists()) { // assigned to
				throw new TestCaseFailedException();
			}
		}
	}

	/**
	 * Selects the Mass Update link on the current page. Can be used from
	 * Assets, Reporting or Schedule search results.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoMassUpdate(IE ie) throws Exception {
		Link l = ie.link(text("/Mass Update/"));
		l.click();
		ie.waitUntilReady();
	}

	/**
	 * Selects the Print->All Inspection Certificate link on the current Report
	 * search results page. Assumes you have called the gotoReportSearchResults
	 * method.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void printReportingAllInspectionCertificates(IE ie) throws Exception {
		Link l = ie.link(text("/All PDF Reports/"));
		l.click();
		ie.waitUntilReady();
		clickLightboxOKbutton(ie);
	}

	/**
	 * Selects the Print->All Observation Reports link on the current Report
	 * search results page. Assumes you have called the gotoReportSearchResults
	 * method.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void printReportingAllObservationReports(IE ie) throws Exception {
		Link l = ie.link(text("/All Observation Reports/"));
		l.click();
		ie.waitUntilReady();
		clickLightboxOKbutton(ie);
	}

	/**
	 * Goes to the Summary Report for a Reporting Search Result. It assumes you
	 * are on the Search Results page. It will also expand the Details
	 * information on the Summary Report page.
	 * 
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void gotoReportingSummaryReport(IE ie) throws Exception {
		Link l = ie.link(text("/Summary Report/"));
		l.click();
		ie.waitUntilReady();
		ie.link(id("detailRowProductOpen_master")).click(); // expands the
															// Product Type
															// details
		ie.link(id("detailRowInspectionOpen_Visual Inspection")).click(); // expands the Inspection Type Group details
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * Validates the Mass Update page from Scheduled Inspections Result page.
	 * It assumes you are on the Mass Update page already.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void validateScheduleMassUpdate(IE ie) throws Exception {
		if (!ie.containsText("Mass Update Inspection Schedules")) {
			throw new TestCaseFailedException();
		} else if (!ie.link(text("Return to Schedules")).exists()) {
			throw new TestCaseFailedException();
		} else if (!ie.button(
				id("massUpdateInspectionScheduleSave_hbutton_save")).exists()) { // save
																					// button
			throw new TestCaseFailedException();
		} else if (!ie.textField(
				id("massUpdateInspectionScheduleSave_nextDate")).exists()) { // Next
																				// Inspection
																				// Date
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Save the admin url to the property set.
	 * 
	 * @param string
	 * @throws Exception
	 */
	public void setAdminURL(String string) throws Exception {
		p.setProperty("adminurl", string);
	}

	/**
	 * Get the admin url from the property set. If it is not set return the
	 * default url for localhost.localdomain.
	 * 
	 * @return the base URL for the admin interface.
	 * @throws Exception
	 */
	public String getAdminURL() throws Exception {
		String result = p.getProperty("adminurl",
				"https://192.168.2.44/fieldidadmin/organizationList.action");
		return result;
	}

	/**
	 * Logs into the fieldidadmin/ page. Assumes you are already on the login page.
	 * 
	 * @param ie
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void loginAdminInterface(IE ie, String username, String password) throws Exception {
		ie.textField(id("login_username")).set(username);
		ie.textField(id("login_password")).set(password);
		ie.button(id("login_0")).click();
		ie.waitUntilReady();
	}

	/**
	 * Go to the Edit Notification page and set a notification.
	 * 
	 * @param ie
	 * @param notify
	 * @param when
	 * @param on
	 * @param startingDays
	 * @param nextDays
	 * @param emails
	 * @throws Exception
	 */
	public void setNotification(IE ie, boolean notify, String when, String on,
			int startingDays, int nextDays, String[] emails) throws Exception {
		gotoEditNotificationSettings(ie);
		ie.waitUntilReady();
		if (notify) {
			ie.radio(id("notificationSettings_enabledtrue")).set();
		} else {
			ie.radio(id("notificationSettings_enabledfalse")).set();
		}

		if (when != null && when.equalsIgnoreCase("daily")) {
			ie.radio(id("notificationSettings_cronExpressionNameDAILY")).set();
		} else if (when != null && when.equalsIgnoreCase("weekly")) {
			ie.radio(id("notificationSettings_cronExpressionNameWEEKLY")).set();
		}

		if (on != null) {
			ie.selectList(id("daysMapSelect")).option(text(on)).select();
		}

		// value for radio button is seconds in a day
		// current = 0 * 86400
		// 1 day = 1 * 86400
		// 2 days = 2 * 86400 = 172800
		// 1 week = 7 * 86400 = 604800
		// etc.
		if (startingDays > -1) {
			startingDays *= 86400;
			String id = "notificationSettings_durationStart" + startingDays;
			if (ie.radio(id(id)).exists())
				ie.radio(id(id)).set();
		}

		if (nextDays > 0) {
			nextDays *= 86400;
			String id = "notificationSettings_durationEnd" + nextDays;
			if (ie.radio(id(id)).exists())
				ie.radio(id(id)).set();
		}

		for (int i = 0; i < emails.length; i++) {
			ie.textField(id("addEmailInput")).set(emails[i]);
			ie.link(id("notificationSettings_")).click();
			ie.waitUntilReady();
		}

		ie.button(id("notificationSettings_hbutton_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * This method will get the Field ID Access Code (FIDAC) for the current
	 * tenant. It is assumed that you have logged in as the tenant as a user
	 * with Administration privilege.
	 * 
	 * The FIDAC is used by manufacturer tenants to add this tenant to the
	 * manufacturer's Safety Network.
	 * 
	 * @param ie
	 * @return The Field ID Access Code for the current tenant.
	 * @throws Exception
	 */
	public String getFIDAC(IE ie) throws Exception {
		gotoAdministration(ie);
		String fidac = ie.htmlElement(xpath("//DIV[@class='viewSection']/P[LABEL='Field ID Access Code']/SPAN")).text();
		return fidac;
	}

	/**
	 * Add a tenant to the current tenant's safety network. To get the fidac
	 * for tenant1, you need to log in as tenant1 and call the getFIDAC method.
	 * After that you logout, login as tenant2 and use the FIDAC to add tenant1
	 * to the safety network of tenant2.
	 * 
	 * tenant1 has to be a manufacturer and tenant2 has to be an inspector.
	 * 
	 * Currently, the following are manufacturers:
	 * 
	 * 	PeakWorks
	 * 	crosby
	 * 	n4
	 * 	marine
	 * 	jergens
	 * 
	 * and the following are inspectors:
	 * 
	 * 	allway
	 * 	another
	 *	brs
	 * 	certex
	 * 	cglift
	 * 	CICB
	 * 	cmasters
	 * 	dresser
	 * 	elko
	 * 	halo
	 * 	ercules
	 * 	hysafe
	 * 	johnsakach
	 * 	key
	 * 	lcrane
	 * 	mass
	 * 	MSA
	 * 	n4systems
	 * 	navfac
	 * 	nischain
	 * 	stellar
	 * 	swwr
	 * 	tristate
	 * 	unilift
	 * 	unirope
	 * 	UTS
	 * 	wcwr
	 * 	wiscolift
	 * 
	 * @param ie
	 * @param uniropeFIDAC
	 */
	public void addTenantToSafetyNetwork(IE ie, String fidac) throws Exception {
		gotoManageSafetyNetwork(ie);
		ie.textField(id("safetyNetworkList_fidAC")).set(fidac);
		ie.button(id("safetyNetworkList_label_linktoacompany")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Go to the Manage Safety Network page
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoManageSafetyNetwork(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("/Manage your Safety Network/")).click();
		ie.waitUntilReady();
	}

	/**
	 * This gets the 'Company Name' from the Administration area. This name is used
	 * when you add a company to a manufacturer's safety network. The FIDAC number
	 * is used to add the company to the manufacturer but the 'Company Name' is the
	 * only thing you can see to confirm the link between the two tenants.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public String getCompanyName(IE ie) throws Exception {
		gotoAdministration(ie);
		String name = ie.htmlElement(xpath("//DIV[@class='viewSection']/P[LABEL='Company Name']/SPAN")).text();
		return name;
	}
	
	/**
	 * This gets the 'Company ID' from the Administration area.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public String getCompanyID(IE ie) throws Exception {
		gotoAdministration(ie);
		String name = ie.htmlElement(xpath("//DIV[@class='viewSection']/P[LABEL='Company ID']/SPAN")).text();
		return name;
	}

	/**
	 * Returns true if companyName is part of the Safety Network for the currently
	 * logged in manufacturer.
	 * 
	 * @param ie
	 * @param companyName
	 * @return
	 * @throws Exception
	 */
	public boolean isTenantPartOfSafetyNetwork(IE ie, String companyName) throws Exception {
		boolean result = false;
		gotoManageSafetyNetwork(ie);
		TableCells tcs = ie.cells();
		for(int i = 0; i < tcs.length(); i++) {
			TableCell tc = tcs.get(i);
			String cell = tc.text();
			if(cell.contains(companyName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Go to the Manage Event Type Groups page.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoManageEventTypeGroups(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("/Manage Event Type Groups/")).click();
		ie.waitUntilReady();
	}

	/**
	 * Add an event type group. Things like Visual Inspection would be an
	 * event type group. It will be extended to things like Certificates as well.
	 * 
	 * @param ie
	 * @param name
	 * @param reportTitle
	 * @throws Exception
	 */
	public void addEventTypeGroup(IE ie, String name, String reportTitle) throws Exception {
		gotoManageEventTypeGroups(ie);
		ie.link(text("/Add Event Type Group/")).click();
		ie.waitUntilReady();
		ie.textField(id("eventTypeGroupCreate_name")).set(name);
		ie.textField(id("eventTypeGroupCreate_reportTitle")).set(reportTitle);
		ie.button(id("eventTypeGroupCreate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Assumes the link to the name ends with '?uniqueid=###'
	 * where ### is some number.
	 */
	private String getEventTypeGroupUniqueID(IE ie, String name) throws Exception {
		Link l = ie.link(text(name));
		String s = l.href();
		int index = s.lastIndexOf("=");
		s = s.substring(index+1);
		return s;
	}
	
	/**
	 * Delete the event type group.
	 * 
	 * @param ie
	 * @param name
	 * @throws Exception
	 */
	public void deleteEventTypeGroup(final IE ie, String name) throws Exception {
		gotoManageEventTypeGroups(ie);
		String s = "delete_" + getEventTypeGroupUniqueID(ie, name);
		// set up a thread to answer the "Are you sure?" dialog
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					Wnd w = Wnd.findWindow("#32770",
							"Windows Internet Explorer");
					IEPromptDialog confirm = new IEPromptDialog(w, ie);
					confirm.ok();
				} catch (Exception e) {
				}
			}
		}).start();
		ie.link(id(s)).click();
		ie.waitUntilReady();
	}

	/**
	 * Checks to see if the Event Type Group exists. Looks for a link to the
	 * Event Type Group on the Manage Event Type Groups page.
	 * 
	 * @param ie
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean isEventTypeGroup(IE ie, String name) throws Exception {
		boolean result = true;
		gotoManageEventTypeGroups(ie);
		if(!ie.link(text(name)).exists()) {
			result = false;
		}
		return result;
	}

	/**
	 * Goes to the information page for an event type group.
	 * 
	 * @param ie
	 * @param name
	 * @throws Exception
	 */
	public void gotoEventTypeGroup(IE ie, String name) throws Exception {
		gotoManageEventTypeGroups(ie);
		ie.link(text(name)).click();
		ie.waitUntilReady();
	}

	/**
	 * Edit the Event Type Group. If a parameter is null it will leave the value
	 * unchanged. If the value is anything else it will replace the old value
	 * with the new value.
	 * 
	 * To get to  edit there are three routes. This will go to the Event Type Group
	 * page, click the event type group name then click Edit on the upper right.
	 * 
	 * @param ie
	 * @param name
	 * @param newName
	 * @param newReportTitle
	 * @throws Exception
	 */
	public void editEventTypeGroup(IE ie, String name, String newName, String newReportTitle) throws Exception {
		gotoEventTypeGroup(ie, name);
		ie.link(text("/Edit/")).click();
		ie.waitUntilReady();
		if(newName != null)
			ie.textField(id("eventTypeGroupUpdate_name")).set(newName);
		if(newReportTitle != null)
			ie.textField(id("eventTypeGroupUpdate_reportTitle")).set(newReportTitle);
		ie.button(id("eventTypeGroupUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Edit the Event Type Group. If a parameter is null it will leave the value
	 * unchanged. If the value is anything else it will replace the old value
	 * with the new value.
	 * 
	 * To get to  edit there are three routes. This will go to the Event Type Group
	 * page, click the event type group name then click edit on the left, near Group
	 * Detail.
	 * 
	 * @param ie
	 * @param name
	 * @param newName
	 * @param newReportTitle
	 * @throws Exception
	 */
	public void editEventTypeGroup2(IE ie, String name, String newName, String newReportTitle) throws Exception {
		gotoEventTypeGroup(ie, name);
		ie.link(text("/edit/")).click();
		ie.waitUntilReady();
		if(newName != null)
			ie.textField(id("eventTypeGroupUpdate_name")).set(newName);
		if(newReportTitle != null)
			ie.textField(id("eventTypeGroupUpdate_reportTitle")).set(newReportTitle);
		ie.button(id("eventTypeGroupUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Edit the Event Type Group. If a parameter is null it will leave the value
	 * unchanged. If the value is anything else it will replace the old value
	 * with the new value.
	 * 
	 * To get to  edit there are three routes. This will go to the Event Type Group
	 * page, click the Edit link for the event type group.
	 * 
	 * @param ie
	 * @param name
	 * @param newName
	 * @param newReportTitle
	 * @throws Exception
	 */
	public void editEventTypeGroup3(IE ie, String name, String newName, String newReportTitle) throws Exception {
		gotoManageEventTypeGroups(ie);
		String s = getEventTypeGroupUniqueID(ie, name);
		ie.link(id("edit_" + s)).click();
		ie.waitUntilReady();
		if(newName != null)
			ie.textField(id("eventTypeGroupUpdate_name")).set(newName);
		if(newReportTitle != null)
			ie.textField(id("eventTypeGroupUpdate_reportTitle")).set(newReportTitle);
		ie.button(id("eventTypeGroupUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * There are two locations to delete an Event Type Group. This method
	 * uses the Delete button while editing the Event Type Group.
	 * 
	 * @param ie
	 * @param name
	 * @throws Exception
	 */
	public void deleteEventTypeGroup2(final IE ie, String name) throws Exception {
		gotoEventTypeGroup(ie, name);
		ie.link(text("/Edit/")).click();
		ie.waitUntilReady();
		// set up a thread to answer the "Are you sure?" dialog
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					Wnd w = Wnd.findWindow("#32770",
							"Windows Internet Explorer");
					IEPromptDialog confirm = new IEPromptDialog(w, ie);
					confirm.ok();
				} catch (Exception e) {
				}
			}
		}).start();
		ie.button(value("Delete")).click();
		ie.waitUntilReady();
	}

	/**
	 * From a Reporting Search Result page, this will click the Save Report
	 * link and save the report. If the report has never been saved before
	 * this will save a new report. If the report was saved and run, this
	 * will save over top of the old report. If you give it the same name
	 * it will do nothing. If you save with a different name, you are
	 * essentially renaming the report.
	 * 
	 * @param ie
	 * @param name - the name you wish to save the report as (or rename to if already saved)
	 * @throws Exception
	 */
	public void addSavedReport(IE ie, String name) throws Exception {
		ie.link(text("Save Report")).click();
		ie.textField(id("savedReportCreate_name")).set(name);
		ie.button(value("Save")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Like addSavedReport but when you have run a saved report and 
	 * click Save Report. It goes to a slightly different page.
	 * 
	 * @param ie
	 * @param name
	 * @throws Exception
	 */
	public void updateSavedReport(IE ie, String name) throws Exception {
		ie.link(text("Save Report")).click();
		ie.textField(id("savedReportUpdate_name")).set(name);
		ie.button(value("Save")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * You can have two saved reports with the same name. If you do this it would be
	 * impossible to know which report you want to rename. It is assumed that during
	 * testing, you will not create two reports with the same name.
	 * 
	 * @param ie
	 * @param oldname
	 * @param newname
	 * @throws Exception
	 */
	public void renameSavedReport(IE ie, String oldname, String newname) throws Exception {
		gotoYourSavedReports(ie);
		ie.waitUntilReady();
		Thread.sleep(hack);
		Link oldreport = this.getLinkForSavedReport(ie, oldname);
		oldreport.click();
		ie.waitUntilReady();
		updateSavedReport(ie, newname); 
	}
	
	/**
	 * From the Reporting page there is a 'More' link which takes you to
	 * My Accounts, where the saved reports are listed.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoYourSavedReports(IE ie) throws Exception {
		gotoReporting(ie);
		ie.link(text("More")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Once a report is saved, you can run the report and select the
	 * Save Report As to save the report as a second name. Typically,
	 * I would expect someone to save a report, run the report, change
	 * the search criteria or columns then save the report under a new
	 * name using this Save Report As feature.
	 * 
	 * @param ie
	 * @param name
	 * @throws Exception
	 */
	public void addSavedReportAs(IE ie, String name) throws Exception {
		Link l = ie.link(text("Save Report As"));
		if(l.exists()) {
			l.click();
		}
		else {
			throw new TestCaseFailedException();
		}
		ie.textField(id("savedReportCreate_name")).set(name);
		ie.button(id("savedReportCreate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Get a list of all the product types. It goes to the Add Products
	 * page and uses the product type drop down list.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getProductTypes(IE ie) throws Exception {
		gotoAddProduct(ie);
		List<String> result = new ArrayList<String>();
		SelectList sl = ie.selectList(id("productType"));
		Options o = sl.options();
		for(int i = 0; i < o.length(); i++) {
			String s = o.get(i).text();
			if(!s.equals(""))
				result.add(s);
		}
		return result;
	}

	/**
	 * This method will set up everything for testing with an end user.
	 * It will create a customer, divisions, end users, product types,
	 * inspection type, products, inspections, schedules.
	 * 
	 * The Map which is returned has all the information for the things created.
	 * The map will have the following keys:
	 * <pre>
	 * key                comment
	 * ---                -------
	 * customerID         customer of tenant
	 * customerName       customer name
	 * division0          create division
	 * division1          neither division
	 * division2          edit & create division
	 * division3          edit division
	 * inspectionType-0   master inspection type
	 * inspectionType-1   general inspection type
	 * product-0          master product
	 * product-1          sub-product
	 * product-2          regular product
	 * productType-0      master product
	 * productType-1      sub-product
	 * productType-2      regular product
	 * userName0          user with create permission
	 * userName0Create    true
	 * userName0Edit      false
	 * userName1          user with neither create or edit permission
	 * userName1Create    false
	 * userName1Edit      false
	 * userName2          user with both create and edit permission
	 * userName2Create    true
	 * userName2Edit      true
	 * userName3          user with edit permission
	 * userName3Create    false
	 * userName3Edit      true
	 * userName4          user with both create and edit permission, not division
	 * userName4Create    true
	 * userName4Edit      true
	 * </pre>
	 * 
	 * @param ie
	 * @param emailAddress
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> setupForEndUser(IE ie, String emailAddress, String password) throws Exception {
		final int maxUserNameLength = 15;
		final int maxRecs = 8;	// currently maximum in use by customer is 4 (12-Mar-2009)
		final int maxDefs = 26;	// currently maximum in use by customer is 13 (12-Mar-2009)
		TreeMap<String, String>result = new TreeMap<String, String>();
		Random r = new Random();
		int i, n = r.nextInt();
		
		// save the random number used for most the generated fields.
		result.put("random", Integer.toString(n));
		
		result.put("company", getCompanyName(ie));
		result.put("companyID", getCompanyID(ie));
		
		// add an employee users will full permissions
		String userID = "emp" + n;
		String firstName = "empFirst" + n;
		String lastName = "empLast" + n;
		String position = null;
		String initials = null;
		String securityRFID = null;
		String timeZone = null;
		String orgUnit = null;
		String verifyPassword = password;
		int[] Permissions = { InspectPermissions.CREATE.value(), InspectPermissions.EDIT.value(),
				InspectPermissions.ENDUSERS.value(), InspectPermissions.SYSCONFIG.value(),
				InspectPermissions.SYSUSERS.value(), InspectPermissions.TAG.value() };
		addEmployeeUser(ie, userID, emailAddress, firstName, lastName, position, initials, securityRFID, timeZone, orgUnit, password, verifyPassword, Permissions);
		result.put("employee0", userID);
		result.put("numEmployees", "1");
		
		// add a customer
		String customerID = "ID" + n;
		String customerName = "Name" + n;
		if(!isCustomer(ie, customerID, customerName)) {
			addCustomer(ie, customerID, customerName, "contactName"+n, emailAddress, "address"+n, "city"+n, "state"+n, "zipCode"+n, "country"+n, "phone1"+n, "phone2"+n, "fax"+n);
		}
		result.put("customerID", customerID);
		result.put("customerName", customerName);
		
		// Add end users
		String[] users = { "create", "neither", "both", "editor" };
		boolean create = true;
		boolean edit = false;
		// create/edit will be true/false, false/false, true/true, false/true
		// this matches with   create,     neither,     both,      editor
		for(i = 0; i < users.length; i++, create = !create, edit = (i > 1)) {
			String user = users[i];
			String division = user + "-division";
			if(!isCustomerDivision(ie, customerID, customerName, division)) {
				addCustomerDivision(ie, customerID, customerName, division);
			}
			result.put("division"+i, division);
			user = "u" + i + "-" + user + n;
			if(user.length() > maxUserNameLength)
				user = user.substring(0, maxUserNameLength);	// TODO: hack, the user id cannot be longer than 15 characters
			if(!isCustomerUser(ie, customerID, customerName, user)) {
				addCustomerUser(ie, customerID, customerName, user, emailAddress, "firstName-"+i+"-"+n, "lastName-"+i+"-"+n, "position-"+i+"-"+n, "initials-"+i+"-"+n, null, null, division, password, password, create, edit);
			}
			else {
				setCustomerUser(ie, customerID, customerName, user, emailAddress, "firstName-"+i+"-"+n, "lastName-"+i+"-"+n, "position-"+i+"-"+n, "initials-"+i+"-"+n, null, null, division, password, password, create, edit);
			}
			result.put("userName"+i, user);
			result.put("userName"+i+"Create", create?"true":"false");
			result.put("userName"+i+"Edit", edit?"true":"false");
		}
		
		// create an admin user not in any division
		String user = "u" + i + "-admin" + n;
		if(user.length() > maxUserNameLength)
			user = user.substring(0, maxUserNameLength);	// TODO: hack, the user id cannot be longer than 15 characters
		if(!isCustomerUser(ie, customerID, customerName, user)) {
			addCustomerUser(ie, customerID, customerName, user, emailAddress, "firstName-"+i+"-"+n, "lastName-"+i+"-"+n, "position-"+i+"-"+n, "initials-"+i+"-"+n, null, null, "", password, password, create, edit);
		}
		else {
			setCustomerUser(ie, customerID, customerName, user, emailAddress, "firstName-"+i+"-"+n, "lastName-"+i+"-"+n, "position-"+i+"-"+n, "initials-"+i+"-"+n, null, null, "", password, password, create, edit);
		}
		result.put("userName"+i, user);
		result.put("userName"+i+"Create", "true");
		result.put("userName"+i+"Edit", "true");
		
		// add something so I know how many users are in the Map
		result.put("numUsers", Integer.toString(i+1));

		// create a random number of recommendations and deficiencies
		int numRecs = r.nextInt(maxRecs);
		int numDefs = r.nextInt(maxDefs);
		result.put("recommendations", Integer.toString(numRecs));
		result.put("deficiencies", Integer.toString(numDefs));
		List<String>recs = new ArrayList<String>();
		for(i = 0; i < numRecs; i++) {
			recs.add("Rec #" + i);
		}
		List<String>defs = new ArrayList<String>();
		for(i = 0; i < numDefs; i++) {
			defs.add("Def #" + i);
		}
		String[] supportedProofTestTypes = { "ROBERTS", "NATIONALAUTOMATION", "CHANT", "WIROP", "OTHER" };
		String masterInspectionTypeName = "Master Inspection" + n;
		String group = "Visual Inspection";
		addInspectionType(ie, masterInspectionTypeName, group , true, true, supportedProofTestTypes, null);
		List<String> buttons = getButtonGroups(ie, masterInspectionTypeName);
		int b = r.nextInt(buttons.size());
		ButtonGroup bg = new ButtonGroup(buttons.get(b), true);						// ASSUMES there is at least one button group
		Criteria[] criteria = { new Criteria("Criteria Section", bg, recs, defs) };
		Section[] form = { new Section("Summary", criteria) };
		addInspectionFormToInspectionType(ie, masterInspectionTypeName, form);
		result.put("inspectionType-0", masterInspectionTypeName);
		String inspectionTypeName = "General Inspection" + n;
		addInspectionType(ie, inspectionTypeName, group , true, false, supportedProofTestTypes, null);
		addInspectionFormToInspectionType(ie, inspectionTypeName, form);
		result.put("inspectionType-1", inspectionTypeName);
		
		String masterProductType = "master" + n;
		String subProductType = "sub" + n;
		String productType = "regular" + n;
		addProductType(ie, masterProductType, null, null, null, true, null, null, null, null, null);
		result.put("productType-0", masterProductType);
		addProductType(ie, subProductType, null, null, null, true, null, null, null, null, null);
		result.put("productType-1", subProductType);
		if(!isProductTypeOneASubProductOfProductTypeTwo(ie, subProductType, masterProductType))
			AddProductTypeOneToBeSubProductOfProductTypeTwo(ie, subProductType, masterProductType);
		addInspectionTypeToProductType(ie, masterInspectionTypeName, masterProductType);
		addProductType(ie, productType, null, null, null, true, null, null, null, null, null);
		result.put("productType-2", productType);
		addInspectionTypeToProductType(ie, inspectionTypeName, productType);
		
		addInspectionScheduleToProductType(ie, masterInspectionTypeName, masterProductType, "90", true);
		addInspectionScheduleToProductType(ie, inspectionTypeName, productType, "90", false);
		
		String masterSerialNumber = null;
		String subSerialNumber = null;
		String serialNumber = null;
		
		i = 0;
		for(i = 0; i < users.length; i++) {
			masterSerialNumber = addProduct(ie, null, true, null, null, customerName, result.get("division"+i), null, null, null, null, null, null, null, masterProductType, null, null, null, "Save");
			result.put("master-product-" + i, masterSerialNumber);
			subSerialNumber = addProduct(ie, null, true, null, null, customerName, result.get("division"+i), null, null, null, null, null, null, null, subProductType, null, null, null, "Save");
			result.put("sub-product-" + i, subSerialNumber);
			serialNumber = addProduct(ie, null, true, null, null, customerName, result.get("division"+i), null, null, null, null, null, null, null, productType, null, null, null, "Save");
			result.put("product-" + i, serialNumber);
	
			// and inspection them
			addInspectionToProduct(ie, masterInspectionTypeName, masterSerialNumber, true);
			addInspectionToProduct(ie, inspectionTypeName, serialNumber, false);
		}

		masterSerialNumber = addProduct(ie, null, true, null, null, customerName, "", null, null, null, null, null, null, null, masterProductType, null, null, null, "Save");
		result.put("master-product-" + i, masterSerialNumber);
		subSerialNumber = addProduct(ie, null, true, null, null, customerName, "", null, null, null, null, null, null, null, subProductType, null, null, null, "Save");
		result.put("sub-product-" + i, subSerialNumber);
		serialNumber = addProduct(ie, null, true, null, null, customerName, "", null, null, null, null, null, null, null, productType, null, null, null, "Save");
		result.put("product-" + i, serialNumber);

		// and inspection them
		addInspectionToProduct(ie, masterInspectionTypeName, masterSerialNumber, true);
		addInspectionToProduct(ie, inspectionTypeName, serialNumber, false);

		// number of products of each type, i.e. numProducts * master, numProducts * sub, etc.
		result.put("numProducts", Integer.toString(i+1));
		
		return result;
	}
	
	/**
	 * Dump the contents of the Map to System.err.
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void dumpSetupForEndUser(Map<String, String> data) throws Exception {
		dumpSetupForEndUser(data, System.err);
	}
	
	/**
	 * Dump the contents of the Map to a file. I recommend using this after
	 * any call to setupForEndUser. If you depend on the values of this Map
	 * you can cut and paste the values from the text file and edit the
	 * parameters as you debug rather than running the setupForEndUser method
	 * again.
	 * 
	 * @param data
	 * @param filename
	 * @throws Exception
	 */
	public void dumpSetupForEndUser(Map<String, String> data, String filename) throws Exception {
		PrintStream stream = new PrintStream(filename);
		dumpSetupForEndUser(data, stream);
		stream.close();
	}
	
	/**
	 * Dump the contents of the Map to a PrintStream.
	 * 
	 * @param data
	 * @param filename
	 * @throws Exception
	 */
	public void dumpSetupForEndUser(Map<String, String> data, PrintStream stream) throws Exception {
		Set<String>keys = data.keySet();
		Iterator<String> i = keys.iterator();
		String format = "%1$-20s";
		stream.println(String.format(format, "key") + " = value");
		while(i.hasNext()) {
			String key = i.next();
			String value = data.get(key);
			String key2 = String.format(format, key);
			stream.println(key2 + " = " + value);
		}
	}
	
	/**
	 * Add an inspection form to an inspection type.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @param form - this is a complex data type for defining everything about a form
	 * @throws Exception
	 */
	public void addInspectionFormToInspectionType(IE ie, String inspectionType, Section[] form) throws Exception {
		gotoManageInspectionTypes(ie);
		ie.link(text(inspectionType)).click();
		ie.link(text("/Inspection Form/")).click();
		int i, j, k;
		
		if(form == null)
			return;
		
		for(i = 0; i < form.length; i++) {
			Section s = form[i];
			String sectionName = s.getSectionName();
			ie.button(text("Add Section")).click();
			Thread.sleep(hack);
			ie.textField(id("criteriaSections_" + i + "__title")).set(sectionName);
			ie.link(id("expandSection_" + i)).click();
			for(j = 0; j < s.getNumberCriteria(); j++) {
				Criteria c = s.getCriteria(j);
				String criteriaLabel = c.getCriteriaLabel();
				ie.button(text("Add Criteria")).click();
				Thread.sleep(hack);
				ie.textField(id("criteriaSections_" + i +"__criteria_" + j + "__displayText")).set(criteriaLabel);
				ButtonGroup bg = c.getButtonGroup();
				String buttonGroup = bg.getButtonGroup();
				ie.selectList(id("criteriaSections_" + i + "__criteria_" + j + "__states_iD")).option(text(buttonGroup)).select();
				ie.checkbox(id("criteriaSections_" + i + "__criteria_" + j + "__principal")).set(bg.getSetsResult());
				ie.link(id("obs_open_" + i + "_" + j)).click();
				for(k = 0; k < c.getNumberRecomendations(); k++) {
					Button b = ie.button(id("addRecommendation_" + i + "_" + j));
					b.click();
					String rec = c.getRecommendation(k);
					Thread.sleep(hack);
					ie.textField(id("criteriaSections_" + i + "__criteria_" + j + "__recommendations_" + k + "_")).set(rec);
				}
				for(k = 0; k < c.getNumberDeficiencies(); k++) {
					Button b = ie.button(id("addDeficiencies_" + i + "_" + j));
					b.click();
					String def = c.getDeficiency(k);
					Thread.sleep(hack);
					ie.textField(id("criteriaSections_" + i + "__criteria_" + j + "__deficiencies_" + k + "_")).set(def);
				}
			}
		}
		ie.button(id("inspectionTypeFormSave_hbutton_save")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Goes to an end user account and changes the values for that account.
	 * If a parameter is null it will be unchanged.
	 * 
	 * @param ie
	 * @param customerID
	 * @param customerName
	 * @param userID
	 * @param userEmail
	 * @param firstName
	 * @param lastName
	 * @param position
	 * @param initials
	 * @param securityRFID
	 * @param timeZone
	 * @param division
	 * @param password
	 * @param verifyPassword
	 * @param createInspections
	 * @param editInspections
	 * @throws Exception
	 */
	public void setCustomerUser(IE ie, String customerID, String customerName,
			String userID, String userEmail, String firstName, String lastName,
			String position, String initials, String securityRFID,
			String timeZone, String division, String password,
			String verifyPassword, boolean createInspections,
			boolean editInspections) throws Exception {

		this.gotoListUsers(ie, userID);
		
		ie.link(text, userID).click();
		ie.waitUntilReady();
		if (userEmail != null)
			ie.textField(id("userSave_emailAddress")).set(userEmail);
		if (firstName != null)
			ie.textField(id("firstname")).set(firstName);
		if (lastName != null)
			ie.textField(id("lastname")).set(lastName);
		if (position != null)
			ie.textField(id("userSave_position")).set(position);
		if (initials != null)
			ie.textField(id("initials")).set(initials);
		if (securityRFID != null)
			ie.textField(id("userSave_securityRfidNumber")).set(securityRFID);
		if (timeZone != null)
			ie.selectList(id("userSave_timeZoneID")).option(text(timeZone)).select();
		if (division != null)
			ie.selectList(id("division")).option(text(division)).select();
		if (password != null)
			ie.textField(id("userSave_password")).set(password);
		if (verifyPassword != null)
			ie.textField(id("userSave_passwordConfirmation")).set(verifyPassword);
		if (createInspections)
			ie.radio(id("userSave_userPermissions_'2'_true")).set();
		else
			ie.radio(id("userSave_userPermissions_'2'_false")).set();
		if (editInspections)
			ie.radio(id("userSave_userPermissions_'6'_true")).set();
		else
			ie.radio(id("userSave_userPermissions_'6'_false")).set();

		ie.button(id("userSave_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Like the gotoListUsers with one parameter but this will also
	 * set the filter value.
	 * 
	 * @param ie
	 * @param userID
	 */
	public void gotoListUsers(IE ie, String filter) throws Exception {
		gotoListUsers(ie);
		ie.textField(id("nameFilter")).set(filter);
		ie.button(id("userList_search")).click();
	}

	/**
	 * Get a list of product types but only if they have inspection types
	 * associated with them.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getProductTypeWithInspectionTypes(IE ie) throws Exception {
		gotoAssets(ie);

		// get a list of all product types
		List<String> tmp = new ArrayList<String>();
		SelectList sl = ie.selectList(id("reportForm_criteria_productType"));
		Options o = sl.options();
		int i = 0;
		for(i = 0; i < o.length(); i++) {
			String s = o.get(i).text();
			if(!s.equals(""))
				tmp.add(s);
		}
		gotoManageProductTypes(ie);

		// filter down to product types with inspection types
		List<String> result = new ArrayList<String>();
		Iterator<String> pt = tmp.iterator();
		while(pt.hasNext()) {
			String productType = pt.next();
			String pattern = "/\\s*" + Pattern.quote(productType) + "\\s*/";
			ie.link(text(pattern)).click();
			ie.waitUntilReady();
			ie.link(text("/Select Inspection Types/")).click();
			ie.waitUntilReady();
			Checkboxes cs = ie.checkboxes(xpath("//INPUT[@type='checkbox']"));
			for(i = 0; i < cs.length(); i++) {
				Checkbox c = cs.get(i);
				if(c.checked()) {
					result.add(productType);
					break;
				}
			}
			ie.link(text("/Product Type List/")).click();
			ie.waitUntilReady();
		}
		
		return result;
	}

	/**
	 * Creates and returns a random, 16 character RFID number.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRandomRFID() throws Exception {
		Random r = new Random();
		long n = r.nextLong();
		String rfidNumber = String.format("%1$016X", n);
		return rfidNumber;
	}

	/**
	 * When you go to add a product using an RFID number that was used on a previous
	 * product, you get a dialog telling you the RFID is in use and asking if you
	 * want to transfer it to the new product.
	 * 
	 * This method checks that all the rigth information is being displayed.
	 * 
	 * @param ie
	 * @param rfidNumber
	 */
	public void validateCheckingRFIDNumber(IE ie, String rfidNumber) throws Exception{
		if(!ie.containsText("Checking RFID number")) {
			throw new TestCaseFailedException();
		}

		HtmlElement p = ie.htmlElement(xpath("//DIV[@id='modalBox']/DIV/P"));
		String warning = p.text();
		if(!warning.contains("Warning: You are about to add a product with an RFID Number that already exists in the system. Doing so will remove the RFID from the following products.")) {
			throw new TestCaseFailedException();
		}
		
		if(!ie.button(id("cancelRfidChange")).exists()) {
			throw new TestCaseFailedException();
		}

		if(!ie.button(id("confirmRfidChange")).exists()) {
			throw new TestCaseFailedException();
		}
	}

	/**
	 * As the method name implies, this goes to edit a user.
	 * It goes through the Manage System Users section.
	 * 
	 * @param ie
	 * @param userName
	 * @throws Exception
	 */
	public void gotoEditUser(IE ie, String userName) throws Exception {
		gotoManageSystemUsers(ie);
		ie.textField(id("nameFilter")).set(userName);
		ie.button(id("userList_search")).click();
		ie.waitUntilReady();
		ie.link(text(userName)).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the My Account page then selects the Saved Report specified.
	 * 
	 * @param ie
	 * @param reportName
	 * @throws Exception
	 */
	public void gotoSavedReport(IE ie, String reportName) throws Exception {
		gotoMyAccount(ie);
		Link report = getLinkForSavedReport(ie, reportName);
		if(report.exists())
			report.click();
		else
			throw new TestCaseFailedException();
		ie.waitUntilReady();
	}

	private Link getLinkForSavedReport(IE ie, String reportName) throws Exception {
		Link l = ie.link(text(reportName));
		Link next = ie.link(text("Next>"));
		while(!l.exists() && next.exists()) {
			next.click();
			ie.waitUntilReady();
			l =  ie.link(text(reportName));
			next = ie.link(text("Next>"));
		}
		return l;
	}

	/**
	 * Goes to the My Account page then deletes the Saved Report specified.
	 * 
	 * @param ie
	 * @param reportName
	 * @throws Exception
	 */
	public void deleteSavedReport(IE ie, String reportName) throws Exception {
		gotoMyAccount(ie);
		ie.waitUntilReady();
		Thread.sleep(hack);
		Link l = ie.link(text(reportName));
		String id = l.href();
		int index = id.indexOf('=');
		id = id.substring(index+1);
		Links ls = ie.links(xpath("//A[@class='savedReportDeleteLink']"));
		for(int i = 0; i < ls.length(); i++) {
			l = ls.get(i);
			String matchString = ".*=" + id + "$";
			if(l.href().matches(matchString)) {
				break;
			}
		}
		l.click();
		Thread.sleep(hack);
	}

	/**
	 * Goes to the My Account page then shares the Saved Report specified.
	 * 
	 * @param ie
	 * @param reportName
	 * @param userName
	 * @throws Exception
	 */
	public void shareSavedReport(IE ie, String reportName, String userName) throws Exception {
		gotoMyAccount(ie);
		ie.waitUntilReady();
		Thread.sleep(hack);
		Link l = ie.link(text(reportName));
		String id = l.href();
		int index = id.indexOf('=');
		id = id.substring(index+1);
		Links ls = ie.links(xpath("//A[@class='savedReportShareLink']"));
		for(int i = 0; i < ls.length(); i++) {
			l = ls.get(i);
			String matchString = ".*=" + id + "$";
			if(l.href().matches(matchString)) {
				break;
			}
		}
		l.click();
		Thread.sleep(hack);
		expandTreeView(ie);
		Label userLabel = ie.label(userName);
		String forValue = userLabel.htmlFor();
		ie.checkbox(id(forValue)).set(true);
		ie.button(id("selectShareUsers_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * If you have any tree views with an expand.gif this method
	 * will click on all the expand.gif images, thus expanding
	 * the tree.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	private void expandTreeView(IE ie) throws Exception {
		Images expand = ie.images(src("/expand.gif/"));
		for(int i = 0; i < expand.length(); i++) {
			expand.get(i).click();
		}
	}

	/**
	 * Goes to a saved report from the Reporting page rather than the
	 * My Account page.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoSavedReportFromReportPage(IE ie) throws Exception {
		Link l = ie.link(url("/savedReports/"));
		l.click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage Product Type Groups page. Goes to the 
	 * Administration page automatically.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoManageProductTypeGroups(IE ie) throws Exception {
		gotoAdministration(ie);
		ie.link(text("Manage Product Type Groups")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Add Product Type Group page from anywhere in the system.
	 * Assumes you have the appropriate permissions for Administration,
	 * Manage Product Type Groups and Add Product Type Group.
	 * 
	 * @param ie
	 * @param name
	 * @throws Exception
	 */
	public void addProductTypeGroups(IE ie, String name) throws Exception {
		gotoManageProductTypeGroups(ie);
		ie.link(text("/Add Product Type Group/")).click();
		ie.textField(id("productTypeGroupCreate_name")).set(name);
		ie.button(id("productTypeGroupCreate_label_save")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Goes to the Edit Product Type Group page from anywhere in the system.
	 * Assumes you have the appropriate permissions for Administration,
	 * Manage Product Type Groups and Edit Product Type Group.
	 * 
	 * @param ie
	 * @param productTypeGroupName
	 * @throws Exception
	 */
	public void gotoEditProductTypeGroup(IE ie, String productTypeGroupName) throws Exception {
		gotoManageProductTypeGroups(ie);
		Link l = ie.link(text(productTypeGroupName));
		String id = l.href();
		int index = id.indexOf('=') + 1;
		id = "edit_" + id.substring(index);
		l = ie.link(id(id));
		l.click();
		ie.waitUntilReady();
	}

	/**
	 * Actually goes to AND edits the product type group.
	 * 
	 * @param ie
	 * @param productTypeGroupName
	 * @param newProductTypeGroupName
	 * @throws Exception
	 */
	public void editProductTypeGroup(IE ie, String productTypeGroupName, String newProductTypeGroupName) throws Exception {
		gotoEditProductTypeGroup(ie, productTypeGroupName);
		ie.textField(id("productTypeGroupUpdate_name")).set(newProductTypeGroupName);
		ie.button(id("productTypeGroupUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the Manage Product Type Group page from anywhere in the system.
	 * Then it clicks the delete link for the given group.
	 * 
	 * Assumes you have the appropriate permissions for Administration,
	 * Manage Product Type Groups and Manage Product Type Group.
	 * 
	 * @param ie
	 * @param productTypeGroupName
	 * @throws Exception
	 */
	public void deleteProductTypeGroup(IE ie, String productTypeGroupName) throws Exception {
		gotoManageProductTypeGroups(ie);
		Link l = ie.link(text(productTypeGroupName));
		String id = l.href();
		int index = id.indexOf('=') + 1;
		id = "delete_" + id.substring(index);
		l = ie.link(id(id));
		l.click();
		ie.waitUntilReady();
		ie.button(id("label_delete")).click();
	}

	/**
	 * Links the product type to a product type group.
	 * 
	 * @param ie
	 * @param productType
	 * @param productTypeGroupName
	 * @throws Exception
	 */
	public void addProductTypeToProductTypeGroup(IE ie, String productType, String productTypeGroupName) throws Exception {
		gotoEditProductType(ie, productType);
		ie.selectList(id("productTypeUpdate_group")).option(text(productTypeGroupName)).select();
		ie.button(id("productTypeUpdate_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Get the user name from the user ID. Basically gets the first and last name
	 * from the user id then combines it into a single string using
	 * "first-name last-name"
	 * 
	 * @param ie
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public String getUserNameFromUserID(IE ie, String userID) throws Exception {
		String username = null;
		gotoEditUser(ie, userID);
		username  = getUserNameFromEditUserPage(ie);
		return username;
	}
	
	private Link getLinkForJob(IE ie, String title) throws Exception {
		Link l = null;
		l = ie.link(text(title));
		while(l == null || !l.exists()) {
			if(isNextPage(ie)) {
				gotoNextPage(ie);
			}
			l = ie.link(text(title));
		}
		return l;
	}
	
	/**
	 * Goes to the All Jobs list and deletes the job specified by title.
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void deleteJob(final IE ie, String title) throws Exception {
		this.deleteJob(ie, title, false);
	}

	/**
	 * Goes to the Jobs list and deletes the job specified by title.
	 * 
	 * @param ie
	 * @param title
	 * @param onlyMyJobs - if true will go to jobs assigned to me otherwise goes to all jobs
	 * @throws Exception
	 */
	public void deleteJob(final IE ie, String title, boolean onlyMyJobs) throws Exception {
		if(onlyMyJobs) {
			this.gotoJobsAssignedToMe(ie);
		} else {
			gotoJobs(ie);
		}
		
		Link l = getLinkForJob(ie, title);
		String id = l.href();
		int index = id.indexOf("uniqueID");
		id = id.substring(index);
		Links ls = ie.links(text("Delete"));
		for(index = 0; index < ls.length(); index++) {
			l = ls.get(index);
			if(l.href().matches(".*\\D" + id + "\\D.*")) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1000);
							Wnd w = Wnd.findWindow("#32770",
									"Windows Internet Explorer");
							IEPromptDialog confirm = new IEPromptDialog(w, ie);
							confirm.ok();
						} catch (Exception e) {
						}
					}
				}).start();
				l.click();
				ie.waitUntilReady();
				break;
			}
		}
	}

	private boolean isNextPage(IE ie) throws Exception {
		return ie.link(text("Next>")).exists();
	}

	/**
	 * Edit an existing Job. This works for Event or Asset jobs.
	 * It requires the title of the job to find and edit it. It
	 * does not allow you to change the title of the job.
	 * 
	 * @param ie
	 * @param title
	 * @param jobID
	 * @param customer
	 * @param division
	 * @param status
	 * @param open
	 * @param description
	 * @param startDate
	 * @param estimateDate
	 * @param actualDate
	 * @param duration
	 * @param poNumber
	 * @param workPerformed
	 * @throws Exception
	 */
	public void editJob(IE ie, String title, String jobID, String customer,
			String division, String status, boolean open, String description,
			String startDate, String estimateDate, String actualDate,
			String duration, String poNumber, String workPerformed) throws Exception {
		editJob(ie, title, jobID, null, customer, division, status, open, description, startDate, estimateDate, actualDate, duration, poNumber, workPerformed);
	}

	/**
	 * Edit an existing Job. This works for Event or Asset jobs.
	 * It requires the title of the job to find and edit it. It
	 * does allow you to change the title of the job.
	 * 
	 * @param ie
	 * @param title
	 * @param jobID
	 * @param newTitle
	 * @param customer
	 * @param division
	 * @param status
	 * @param open
	 * @param description
	 * @param startDate
	 * @param estimateDate
	 * @param actualDate
	 * @param duration
	 * @param poNumber
	 * @param workPerformed
	 * @throws Exception
	 */
	public void editJob(IE ie, String title, String jobID, String newTitle, String customer,
			String division, String status, boolean open, String description,
			String startDate, String estimateDate, String actualDate,
			String duration, String poNumber, String workPerformed) throws Exception {
		gotoEditJob(ie, title);
		
		if(jobID != null) {
			ie.textField(id("jobUpdate_projectID")).set(jobID);
		}
		
		if(newTitle != null) {
			ie.textField(id("jobUpdate_name")).set(newTitle);
		}
		
		if(customer != null) {
			ie.selectList(id("customer")).select(customer);
			Thread.sleep(hack);
		}
		
		if(division != null) {
			ie.selectList(id("division")).select(division);
		}
		
		if(status != null) {
			ie.textField(id("jobUpdate_status")).set(status);
		}
		
		ie.checkbox(id("jobUpdate_open")).set(open);
		
		if(description != null) {
			ie.textField(id("jobUpdate_description")).set(description);
		}
		
		if(startDate != null) {
			ie.textField(id("jobUpdate_started")).set(startDate);
		}
		
		if(estimateDate != null) {
			ie.textField(id("jobUpdate_started")).set(estimateDate);
		}
		
		if(actualDate != null) {
			ie.textField(id("jobUpdate_actualCompletion")).set(actualDate);
		}
		
		if(duration != null) {
			ie.textField(id("jobUpdate_duration")).set(duration);
		}
		
		if(poNumber != null) {
			ie.textField(id("jobUpdate_poNumber")).set(poNumber);
		}
		
		if(workPerformed != null) {
			ie.textField(id("jobUpdate_workPerformed")).set(workPerformed);
		}
		
		ie.button(id("jobUpdate_label_save")).click();
		ie.waitUntilReady();
	}
	
	/**
	 *  Immediately after a called to editJob, call this method.
	 *  If the edit failed this will throw an exception.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void validateEditJob(IE ie) throws Exception {
		if(!ie.containsText("Job Saved.")) {
			throw new TestCaseFailedException();	// confirm the job saved.
		}
	}

	/**
	 * Goes to the edit page for a given job. works for Event or Asset jobs
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void gotoEditJob(IE ie, String title) throws Exception {
		gotoJob(ie, title);
		ie.link(text("Edit")).click();
		ie.waitUntilReady();
	}

	/**
	 * Adds a note to the job. If attachment is anything other than null,
	 * at this time, we throw an exception.
	 * 
	 * @param ie
	 * @param title
	 * @param note
	 * @param attachment
	 * @throws Exception
	 */
	public void addJobNote(final IE ie, String title, String note, String attachment) throws Exception {
		gotoJob(ie, title);
		addNote(ie, note, attachment);
	}

	/**
	 * Goes to a Job view for the given title.
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void gotoJob(IE ie, String title) throws Exception {
		gotoJobs(ie);
		Link l = getLinkForJob(ie, title);
		l.click();
		ie.waitUntilReady();
	}

	/**
	 * Same as addJobNote but this adds it from the Notes page rather than
	 * the View Job page.
	 *  
	 * @param ie
	 * @param title
	 * @param note
	 * @param attachment
	 * @throws Exception
	 */
	public void addJobNote2(IE ie, String title, String note, String attachment) throws Exception {
		gotoJobNotes(ie, title);
		addNote(ie, note, attachment);
	}

	/**
	 * This is a helper method that does all the work. It is used by the
	 * addJobNote and addJobNote2 methods.
	 * 
	 * @param ie
	 * @param note
	 * @param attachment
	 * @throws Exception
	 */
	private void addNote(IE ie, String note, String attachment) throws Exception {
		ie.link(text("add a note")).click();
		Thread.sleep(hack);
		
		if(note != null) {
			ie.textField(id("noteComments")).set(note);
		}
		
		if(attachment != null) {
			throw new NotImplementedYetException();
		}
		
		ie.button(id("addNote_label_save")).click();
		ie.waitUntilReady();
	}

	/**
	 * Go to the Notes section of a job. Goes to the Jobs list, clicks
	 * the title of the job you specify then clicks the Notes link.
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void gotoJobNotes(IE ie, String title) throws Exception {
		gotoJob(ie, title);
		ie.link(text("Notes")).click();
		ie.waitUntilReady();
		if(!ie.containsText("Notes on Job - " + title))
			throw new TestCaseFailedException();
	}

	/**
	 * Deletes the note from a job. Because the only way to identify a note is
	 * by the text and create date, this method will find the first note which
	 * contains the string provided and the specified create date. If you put
	 * just part of the create date string or all of it, it will match. If the
	 * strings provided occurs in multiple notes this method will delete the
	 * first instance.
	 * 
	 * This deletes the note from the View Job page.
	 * 
	 * @param ie
	 * @param title
	 * @param note
	 * @param createDate can be a substring of the text you see on the web page.
	 * @throws Exception
	 */
	public void deleteJobNote(final IE ie, String title, String note, String createDate) throws Exception {
		gotoJob(ie, title);
		String id = findDeleteNote(ie, note, createDate);
		deleteNote(ie, note, createDate, id);
	}

	/**
	 * Deletes the note from a job. Because the only way to identify a note is
	 * by the text and create date, this method will find the first note which
	 * contains the string provided and the specified create date. If you put
	 * just part of the create date string or all of it, it will match. If the
	 * strings provided occurs in multiple notes this method will delete the
	 * first instance.
	 * 
	 * This deletes the note from the Notes page.
	 * 
	 * @param ie
	 * @param title
	 * @param note
	 * @param date
	 * @throws Exception
	 */
	public void deleteJobNote2(IE ie, String title, String note, String date) throws Exception {
		gotoJobNotes(ie, title);
		String id = findDeleteNote2(ie, note, date);
		deleteNote(ie, note, date, id);
	}

	private void deleteNote(final IE ie, String note, String date, String id) throws Exception {
		if(id != null) {
			Link l = ie.link(attribute("noteid", id));
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
						Wnd w = Wnd.findWindow("#32770",
								"Windows Internet Explorer");
						IEPromptDialog confirm = new IEPromptDialog(w, ie);
						confirm.ok();
					} catch (Exception e) {
					}
				}
			}).start();
			l.click();
		}
	}

	/**
	 * This finds the link to click for the delete. This finds the link on the
	 * View Job page.
	 * 
	 * @param ie
	 * @param note
	 * @param createDate
	 * @throws Exception
	 */
	private String findDeleteNote(final IE ie, String note, String createDate) throws Exception {
		/**
		 * For some bizarre reason this code is needed. Either Watij, Field ID
		 * or Internet Explorer converts the "\n" character to a " \r\n". Also
		 * it trims the whitespace at the end of the note. I think this is
		 * Field ID trimming at the end.
		 */
		note = note.replaceAll("\n", " \r\n").trim();
		String id = null;
		Divs notes = ie.divs(xpath("//DIV[@id='notes']/DIV[@class='note']"));
		for(int i = 0; i < notes.length(); i++) {
			HtmlElement n = notes.get(i);
			HtmlElement nn = n.htmlElement(xpath("DIV/P[@class='noteText']"));
			HtmlElement cd = n.htmlElement(xpath("DIV/P[@class='noteCreation']"));
			String s1 = nn.text();
			String s2 = cd.text();
			if(createDate != null) {	// if searching by text and create date
				if(s1.contains(note) && s2.contains(createDate)) {
					id = n.id().replaceFirst("note_", "");
					break;
				}
			}
			else {
				if(s1.contains(note)) {	// searching by just text
					id = n.id().replaceFirst("note_", "");
					break;
				}
			}
		}

		return id;
	}

	/**
	 * This does all the work for the two deleteJobNote methods. This
	 * finds the link on the Notes page.
	 * 
	 * @param ie
	 * @param note
	 * @param createDate
	 * @return the id attribute for the link to click
	 * @throws Exception
	 */
	private String findDeleteNote2(final IE ie, String note, String createDate) throws Exception {
		/**
		 * For some bizarre reason this code is needed. Either Watij, Field ID
		 * or Internet Explorer converts the "\n" character to a " \r\n". Also
		 * it trims the whitespace at the end of the note. I think this is
		 * Field ID trimming at the end.
		 */
		note = note.replaceAll("\n", " \r\n").trim();
		String id = null;
		Divs notes = ie.divs(xpath("//DIV[@class='pageSection']/DIV[@class='sectionContent']/DIV"));
		for(int i = 0; i < notes.length(); i++) {
			HtmlElement n = notes.get(i);
			HtmlElement nn = n.htmlElement(xpath("DIV/P[@class='noteText']"));
			HtmlElement cd = n.htmlElement(xpath("DIV/P[@class='noteCreation']"));
			String s1 = nn.text();
			String s2 = cd.text();
			if(createDate != null) {	// if searching by text and create date
				if(s1.contains(note) && s2.contains(createDate)) {
					id = n.id().replaceFirst("note_", "");
					break;
				}
			}
			else {
				if(s1.contains(note)) {	// searching by just text
					id = n.id().replaceFirst("note_", "");
					break;
				}
			}
		}
		return id;
	}

	/**
	 * Goes the Events page of a job. Assumes the job is an event job
	 * and not an Assets job.
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void gotoJobEvents(IE ie, String title) throws Exception {
		gotoJob(ie, title);
		ie.link(text("Events")).click();
		ie.waitUntilReady();
	}

	/**
	 * Returns true if the job is an event job.
	 * 
	 * @param ie
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public boolean isJobEventJob(IE ie, String title) throws Exception {
		boolean result = false;
		gotoJobs(ie);
		String id = findJobUniqueID(ie, title);
		if(id != null) {
			TableCell c = ie.cell(xpath("//TR[@id='project_" + id + "']/TD[1]"));
			if(c != null && c.text().contains("Event"))
				result = true;
		}
		return result;
	}

	private String findJobUniqueID(IE ie, String title) throws Exception {
		String id = null;
		Link l = getLinkForJob(ie, title);
		String s = l.href();
		int i = s.indexOf("uniqueID");
		i = s.indexOf("=", i) + 1;
		id = s.substring(i);
		return id;
	}

	/**
	 * Returns true if the job is an asset job.
	 * 
	 * @param ie
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public boolean isJobAssetJob(IE ie, String title) throws Exception {
		boolean result = false;
		gotoJobs(ie);
		String id = findJobUniqueID(ie, title);
		if(id != null) {
			TableCell c = ie.cell(xpath("//TR[@id='project_" + id + "']/TD[1]"));
			if(c != null && c.text().contains("Asset"))
				result = true;
		}
		return result;
	}

	/**
	 * Goes the Assets page of a job. Assumes the job is an asset job
	 * and not an event job.
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void gotoJobAssets(IE ie, String title) throws Exception {
		gotoJob(ie, title);
		// find the link which is not the Assets at the top of the display
		Links ls = ie.links(text("Assets"));
		for(int i = 0; i < ls.length(); i++) {
			Link l = ls.get(i);
			if(!l.id().equals("menuAssets")) {
				l.click();
				break;
			}
		}
		ie.waitUntilReady();
	}

	/**
	 * Add an asset to a job from the View Job page.
	 * 
	 * @param ie
	 * @param title
	 * @param serialNumber
	 * @throws Exception
	 */
	public void addAssetToJob(IE ie, String title, String serialNumber) throws Exception {
		gotoJob(ie, title);
		addAsset(ie, serialNumber);
	}

	/**
	 * Add an asset to a job from the Assets page.
	 * 
	 * @param ie
	 * @param title
	 * @param serialNumber
	 * @throws Exception
	 */
	public void addAssetToJob2(IE ie, String title, String serialNumber) throws Exception {
		gotoJobAssets(ie, title);
		addAsset(ie, serialNumber);
		ie.link(text(serialNumber)).click();
		Thread.sleep(hack);
	}

	private void addAsset(IE ie, String serialNumber) throws Exception {
		ie.link(text("add an asset")).click();
		Thread.sleep(hack);
		ie.textField(id("projectAssetSearch_search")).set(serialNumber);
		ie.button(id("projectAssetSearch_load")).click();
		Thread.sleep(hack);
		ie.waitUntilReady();
	}

	/**
	 * Deletes an asset fom a job via the Job View page.
	 * 
	 * @param ie
	 * @param title
	 * @param serialNumber
	 * @throws Exception
	 */
	public void deleteAssetFromJob(IE ie, String title, String serialNumber) throws Exception {
		gotoJob(ie, title);
		deleteAsset(ie, serialNumber);
	}

	private void deleteAsset(IE ie, String serialNumber) throws Exception {
		String s = "uniqueID=";
		Link l = ie.link(text(serialNumber));
		String id = l.href();
		int i = id.indexOf(s) + s.length();
		id = id.substring(i);
		l = ie.link(attribute("assetid", id));
		l.click();
	}

	/**
	 * Deletes an asset from a job via the Assets page of the job.
	 * 
	 * @param ie
	 * @param title
	 * @param serialNumber
	 * @throws Exception
	 */
	public void deleteAssetFromJob2(IE ie, String title, String serialNumber) throws Exception {
		gotoJobAssets(ie, title);
		deleteAsset(ie, serialNumber);
	}

	/**
	 * Goes to an Event Job, clicks Add Event, generates a list of schedules based on
	 * the search criteria provided then selects the row specified. If the value of row
	 * is -1 then it assigns all the schedules to the job. Rows are zero indexed. So row
	 * zero would be the first row.
	 * 
	 * This goes to the Add Event via the View Job page.
	 * 
	 * It is assumed (a) that there is a product at the row position specified and that
	 * (b) the schedule is not already added to the job. If the product does not have a schedule
	 * on the row specified or that schedule is already assigned to the job, this will crash.
	 * 
	 * @param ie
	 * @param title
	 * @param scheduleStatus
	 * @param serialNumber
	 * @param customer
	 * @param division
	 * @param inspectionType
	 * @param productType
	 * @param productStatus
	 * @param fromDate
	 * @param toDate
	 * @param row
	 * @throws Exception
	 */
	public void addEventToEventJob(IE ie, String title, String scheduleStatus,
			String serialNumber, String customer, String division,
			String inspectionType, String productType, String productStatus,
			String fromDate, String toDate, int row) throws Exception {
		gotoJob(ie, title);
		addEvent(ie, scheduleStatus, serialNumber, customer, division, inspectionType, productType, productStatus, fromDate, toDate, row);
		Thread.sleep(hack);
	}

	/**
	 * Goes to an Event Job, clicks Add Event, generates a list of schedules based on
	 * the search criteria provided then selects the row specified. If the value of row
	 * is -1 then it assigns all the schedules to the job. Rows are zero indexed. So row
	 * zero would be the first row.
	 * 
	 * This goes to the Add Event via the Events view.
	 * 
	 * It is assumed (a) that there is a product at the row position specified and that
	 * (b) the schedule is not already added to the job. If the product does not have a schedule
	 * on the row specified or that schedule is already assigned to the job, this will crash.
	 * 
	 * @param ie
	 * @param title
	 * @param scheduleStatus
	 * @param serialNumber
	 * @param customer
	 * @param division
	 * @param inspectionType
	 * @param productType
	 * @param productStatus
	 * @param fromDate
	 * @param toDate
	 * @param row
	 * @throws Exception
	 */
	public void addEventToEventJob2(IE ie, String title, String scheduleStatus,
			String serialNumber, String customer, String division,
			String inspectionType, String productType, String productStatus,
			String fromDate, String toDate, int row) throws Exception {
		gotoJobEvents(ie, title);
		addEvent(ie, scheduleStatus, serialNumber, customer, division, inspectionType, productType, productStatus, fromDate, toDate, row);
		Thread.sleep(hack);
	}

	/**
	 * Goes to an Event Job, clicks Add Event, generates a list of schedules based on
	 * the search criteria provided then selects the row specified. If the value of row
	 * is -1 then it assigns all the schedules to the job. Rows are zero indexed. So row
	 * zero would be the first row.
	 * 
	 * This assumes you are already on a page with an Add Event link.
	 * 
	 * It is assumed (a) that there is a product at the row position specified and that
	 * (b) the schedule is not already added to the job. If the product does not have a schedule
	 * on the row specified or that schedule is already assigned to the job, this will crash.
	 * 
	 * @param ie
	 * @param title
	 * @param scheduleStatus
	 * @param serialNumber
	 * @param customer
	 * @param division
	 * @param inspectionType
	 * @param productType
	 * @param productStatus
	 * @param fromDate
	 * @param toDate
	 * @param row
	 * @throws Exception
	 */
	public void addEvent(IE ie, String scheduleStatus, String serialNumber, String customer, String division,
			String inspectionType, String productType, String productStatus, String fromDate, String toDate, int row) throws Exception {
		this.runEventJobAddEventSearch(ie, scheduleStatus, serialNumber, customer, division, inspectionType, productType, productStatus, fromDate, toDate);
		if(row < 0) {
			ie.link(text("Assign all to Job")).click();
		}
		else {
			// find the row and click the link
			TableRows trs = ie.rows(xpath("//TABLE[@id='resultsTable']/TBODY/TR"));
			for(int i = 0; i < trs.length(); i++) {
				TableRow tr = trs.get(i);
				if(tr.html().contains("inspection_schedule_serialnumber_"+row)) {
					Link l = tr.link(text("/Assign To Job/"));
					l.click();
				}
			}
		}
	}
	
	/**
	 * This will run the Add Event search criteria for the current Job.
	 * It assumes you are on the Job page. It will just fill in the search
	 * criteria and run the search. It does not assign any of the results
	 * to the current job.
	 * 
	 * @param ie
	 * @param scheduleStatus
	 * @param serialNumber
	 * @param customer
	 * @param division
	 * @param inspectionType
	 * @param productType
	 * @param productStatus
	 * @param fromDate
	 * @param toDate
	 * @throws Exception
	 */
	public void runEventJobAddEventSearch(IE ie, String scheduleStatus, String serialNumber, String customer, String division,
			String inspectionTypeGroup, String productType, String productStatus, String fromDate, String toDate) throws Exception {
		ie.link(text("/add an event/")).click();
		ie.waitUntilReady();
		
		if(scheduleStatus != null) {
			ie.selectList(id("reportForm_criteria_status")).option(text(scheduleStatus)).select();
		}
		
		if(serialNumber != null) {
			ie.textField(id("reportForm_criteria_serialNumber")).set(serialNumber);
		}
		
		if(customer != null){
			ie.selectList(id("reportForm_criteria_customer")).option(text(customer)).select();
			Thread.sleep(hack);
		}
		
		if(division != null) {
			ie.selectList(id("division")).option(text(division)).select();
		}
		
		if(inspectionTypeGroup != null) {
			ie.selectList(id("reportForm_criteria_inspectionType")).option(text(inspectionTypeGroup)).select();
		}
		
		if(productType != null) {
			ie.selectList(id("reportForm_criteria_productType")).option(text(productType)).select();
		}
		
		if(productStatus != null) {
			ie.selectList(id("reportForm_criteria_productStatus")).option(text(productStatus)).select();
		}
		
		if(fromDate != null) {
			ie.textField(id("reportForm_fromDate")).set(fromDate);
		}
		
		if(toDate != null) {
			ie.textField(id("reportForm_toDate")).set(toDate);
		}
		
		ie.button(id("reportForm_label_Run")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * Checks the schedule for a given product to see if there is a job
	 * associated with the schedule. It will scan the table for a row
	 * which contains the inspection type, the job title and the next
	 * schedule inspection date.
	 * 
	 * If it finds a row with all three parameters it returns true.
	 * 
	 * @param ie
	 * @param jobTitle
	 * @param serialNumber
	 * @param inspectionType
	 * @param nextScheduledDate
	 * @return
	 * @throws Exception
	 */
	public boolean isScheduleAssignedToJob(IE ie, String jobTitle,
			String serialNumber, String inspectionType, String nextScheduledDate) throws Exception {
		
		boolean result = false;
		
		gotoInspectionSchedule(ie, serialNumber);
		TableRows trs = ie.rows(id("/^type_/"));
		for(int i = 0; i <  trs.length(); i++) {
			TableRow tr = trs.get(i);
			TableCell type = tr.cell(xpath("./TD[@class='name' and text()='" + inspectionType + "']"));
			Span date = tr.span(xpath("./TD/SPAN[text()='" + nextScheduledDate + "']"));
			Span job = tr.span(xpath("./TD/SPAN[text()='" + jobTitle + "']"));
			if(date.exists() && job.exists() && type.exists()) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Goes to the Schedule For - $serialNumber page.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @throws Exception
	 * @deprecated use gotoInspectionSchedule
	 */
	public void gotoScheduleFor(IE ie, String serialNumber) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
	}

	/**
	 * Log into Field ID as a tenant with the extended feature of
	 * JobSites and Compliance. Currently this is Key Constructors (key).
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void loginJobSitesComplianceTenant(IE ie, String user, String password) throws Exception {
		String tenant = "key";

		setUserName(user);
		setPassword(password);
		setTenant(tenant);
		start(ie, getLoginURL());
		ie.maximize();
		loginBrandedDefaultRegular(ie, false);
	}

	/**
	 * Log into Field ID as a tenant with the extended feature of
	 * Jobs. Currently this is HySafe (hysafe).
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void loginJobsTenant(IE ie, String user, String password) throws Exception {
		String tenant = "hysafe";

		setUserName(user);
		setPassword(password);
		setTenant(tenant);
		start(ie, getLoginURL());
		ie.maximize();
		loginBrandedDefaultRegular(ie, false);
	}

	/**
	 * Get a list of all the inspection types.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getInspectionTypes(IE ie) throws Exception {
		gotoManageInspectionTypes(ie);

		List<String> result = new ArrayList<String>();
		Links ls = ie.links(xpath("//TABLE[@class='list']/TBODY/TR/TD/A"));
		for(int i = 0; i < ls.length(); i++) {
			String s = ls.get(i).text();
			if(!s.equals("") && !s.contains("Edit"))
				result.add(s);
		}
		return result;
	}

	/**
	 * Picks a random product from the first page of a product search result.
	 * The search criteria used is null for all the fields. If the fields had
	 * been filled in previously, the old values would be retained.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public String getProduct(IE ie) throws Exception {
		String serialNumber = null;
		gotoProductSearchResults(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		TableCells tcs = ie.cells(id("/^product_search_serialnumber_/"));
		Random r = new Random();
		int n = r.nextInt(tcs.length());
		TableCell c = tcs.get(n);
		serialNumber = c.text();
		return serialNumber;
	}

	/**
	 * Gets a list of all the jobs in the system.
	 * Assumes you are on page 1 of the Jobs list.
	 * A call to gotoJobs or gotoJobsAssignedToMe
	 * is required.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getJobs(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		boolean loopFlag = true;
		do {
			List<String> tmp = getJobsFromCurrentPage(ie);
			if (tmp.size() != 0)
				result.addAll(tmp);
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		return result;
	}

	/**
	 * Gets a list of the jobs on the current Jobs page.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getJobsFromCurrentPage(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		TableRows trs = ie.rows();
		for(int i = 0; i < trs.length(); i++) {
			TableRow tr = trs.get(i);
			Link l = tr.link(xpath("TD[3]/A"));
			if(l.exists()) {
				result.add(l.text());
			}
		}
		return result;
	}

	/**
	 * Get a list of Jobs from the Jobs section, go to the Inspection Schedule
	 * for the given product and compare the jobs on the drop down list to the
	 * list from the Jobs section.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @param jobs
	 * @return
	 * @throws Exception
	 */
	public boolean validateJobsOnInspectionSchedule(IE ie, String serialNumber, List<String> jobs) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		boolean result = true;
		Set<String> s1 = new TreeSet<String>(jobs);
		Set<String> s2 = new TreeSet<String>();
		Options os = ie.selectList(id("newSchedule_project")).options();
		for(int i = 0; i < os.length(); i++) {
			String s = os.get(i).text();
			if(s != null && !s.equals(""))
				s2.add(s);
		}
		result = s1.equals(s2);
		return result;
	}

	/**
	 * Return a list of the button group names.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @return
	 * @throws Exception
	 */
	public List<String> getButtonGroups(IE ie, String inspectionType) throws Exception {
		List<String> result = new ArrayList<String>();
		gotoManageInspectionTypes(ie);
		ie.link(text(inspectionType)).click();
		ie.waitUntilReady();
		ie.link(text("Inspection Form")).click();
		ie.waitUntilReady();
		ie.link(text("Manage")).click();
		ie.waitUntilReady();
		Table t = ie.table(id("buttonGroups"));
		TableRows trs = t.rows();
		for(int i = 1; i < trs.length(); i++) {	// start at 1 to skip the <TH> row
			TableRow tr = trs.get(i);
			TextField bn = tr.textField(id("stateSet_" + (i-1) + "_form_name"));
			String s = bn.value();
			result.add(s);
		}
		return result;
	}

	/**
	 * Check to see if an Inspection Type is a Master or Standard inspection type.
	 * Returns true if master inspection type and false if standard.
	 *  
	 * @param ie
	 * @param inspectionType
	 * @return
	 * @throws Exception
	 */
	public boolean isInspectionTypeMaster(IE ie, String inspectionType) throws Exception {
		boolean result = false;
		gotoInspectionType(ie, inspectionType);
		Span s = ie.span(xpath("//DIV[@id='pageContent']/DIV[@class='viewSection']/P[3]/SPAN[@class='fieldValue']"));
		String type = s.text().trim();
		result = type.equals("Master");
		return result;
	}

	/**
	 * Go to the Inspection Type page in Manage Inspection Types.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @throws Exception
	 */
	public void gotoInspectionType(IE ie, String inspectionType) throws Exception {
		gotoManageInspectionTypes(ie);
		ie.link(text(inspectionType)).click();
		ie.waitUntilReady();
	}

	/**
	 * Get a list of all the Event Jobs.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getEventJobs(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		gotoJobs(ie);
		boolean loopFlag = true;
		do {
			List<String> tmp = getEventJobsFromCurrentPage(ie);
			if (tmp.size() != 0)
				result.addAll(tmp);
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		return result;
	}

	/**
	 * Get the Event Jobs from the current Jobs page.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getEventJobsFromCurrentPage(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		TableRows trs = ie.rows();
		for(int i = 1; i < trs.length(); i++) {
			TableRow tr = trs.get(i);
			Link l = tr.link(xpath("TD[3]/A"));
			TableCell c = tr.cell(xpath("TD[1]"));
			String type = c.text();
			if(l.exists() && type.contains("Event")) {
				String job = l.text();
				result.add(job);
			}
		}
		return result;
	}

	/**
	 * Find inspection schedule by inspection type and date then change the job to
	 * the new job. To change the job to No Job, input a value of "" for the jobTitle.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @param inspectionType
	 * @param date
	 * @param jobTitle
	 * @throws Exception
	 */
	public void editJobForInspectionSchedule(IE ie, String serialNumber, String inspectionType, String date, String jobTitle) throws Exception {
		String oldDate = date, newDate = null;
		editInspectionSchedule(ie, serialNumber, inspectionType, oldDate, newDate, jobTitle);
	}

	/**
	 * Finds the Edit link for a schedule on the Schedule For page by
	 * scanning the table for a row with the correct inspection type and date.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @param oldDate
	 * @return
	 * @throws Exception
	 */
	private Link findEditLinkForInspectionSchedule(IE ie, String inspectionType, String oldDate) throws Exception {
		Link result = null;
		TableRows trs = ie.rows(id("/^type_/"));
		TableRow tr = null;
		int i = 0;
		for(i = 0; i < trs.length(); i++) {
			tr = trs.get(i);
			TableCell type = tr.cell(xpath("./TD[@class='name' and text()='" + inspectionType + "']"));
			Span date = tr.span(xpath("./TD/SPAN[text()='" + oldDate + "']"));
			if(date.exists() && type.exists()) {
				result = tr.link(text("Edit"));
				break;
			}
		}
		
		return result;
	}

	/**
	 * Finds the ID for link and text fields for a schedule on the Schedule For page by
	 * scanning the table for a row with the correct inspection type and date.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @param oldDate
	 * @return
	 * @throws Exception
	 */
	private String findEditIDForInspectionSchedule(IE ie, String inspectionType, String oldDate) throws Exception {
		TableRows trs = ie.rows(id("/^type_/"));
		TableRow tr = null;
		String id = null;
		int i = 0;
		for(i = 0; i < trs.length(); i++) {
			tr = trs.get(i);
			TableCell type = tr.cell(xpath("./TD[@class='name' and text()='" + inspectionType + "']"));
			Span date = tr.span(xpath("./TD/SPAN[text()='" + oldDate + "']"));
			if(date.exists() && type.exists()) {
				id = tr.id().replace("type_", "");
				break;
			}
		}
		
		return id;
	}

	/**
	 * Edit the inspection schedule. You need the inspectionType and oldDate to find the schedule
	 * and the newDate and jobTitle to edit it. If any parameter is to remain unchanged, set it
	 * to null.
	 *  
	 * @param ie
	 * @param serialNumber
	 * @param inspectionType
	 * @param oldDate
	 * @param newDate
	 * @param jobTitle
	 * @throws Exception
	 */
	public void editInspectionSchedule(IE ie, String serialNumber, String inspectionType, String oldDate, String newDate, String jobTitle) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		String id = this.findEditIDForInspectionSchedule(ie, inspectionType, oldDate);
		Link edit = this.findEditLinkForInspectionSchedule(ie, inspectionType, oldDate);
		edit.click();
		Thread.sleep(hack);

		// re-capture the table rows because clicking Edit will have changed them
		if(newDate != null) {
			String dateID = "schedule_" + id + "_nextDate";
			ie.textField(id(dateID)).set(newDate);
		}
		
		if(jobTitle != null) {
			String jobID = "schedule_" + id + "_project";
			ie.selectList(id(jobID)).option(text(jobTitle)).select();
		}

		Link save = ie.link(xpath("//FORM[@id='schedule_" + id + "']/SPAN/A[text()='Save']"));
		save.click();
	}

	/**
	 * Goes to the next page on list. This method assumes there is
	 * a next page. If you call this and there is no next page it will
	 * throw an exception. Assumes you are already looking at the page
	 * with the list.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoNextPage(IE ie) throws Exception {
		ie.link(text("Next>")).click();
		ie.waitUntilReady();
	}

	/**
	 * Checks to see if the Jobs page has the correct header ("Jobs")
	 * and a link to add jobs. If either test fails this will throw
	 * an exception. It does NOT assume you are on the Jobs page.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void validateJobsPage(IE ie) throws Exception {
		gotoJobs(ie);
		HtmlElement header = ie.htmlElement(xpath("//DIV[@id='contentHeader']/H1"));
		if(!header.exists() || !header.text().equals("Jobs")) {
			throw new TestCaseFailedException();
		}
		Link aj = ie.link(text("Add Job"));
		if(!aj.exists()) {
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Goes to a specific page of the list. Assumes the page
	 * exists. If not it will throw an exception. Assumes you
	 * are already looking at the page with the list.
	 *  
	 * @param ie
	 * @param page
	 * @throws Exception
	 */
	public void gotoPage(IE ie, String page) throws Exception {
		ie.link(text(page)).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the previous page on a list. This method assumes there is
	 * a previous page. If you call this and there is no previous page it will
	 * throw an exception. Assumes you are already looking at the page with the
	 * list.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoPreviousPage(IE ie) throws Exception {
		ie.link(text("<Previous")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the last page on a list. This method assumes there is
	 * a last page. If you call this and there is no last page it will
	 * throw an exception. Assumes you are already looking at the page with a
	 * list.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoLastPage(IE ie) throws Exception {
		ie.link(text("Last")).click();
		ie.waitUntilReady();
	}

	/**
	 * Goes to the first page on a list. This method assumes there is
	 * a first page. If you call this and there is no first page it will
	 * throw an exception. Assumes you are already looking at the page with a
	 * list.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoFirstPage(IE ie) throws Exception {
		ie.link(text("First")).click();
		ie.waitUntilReady();
	}
	
	/**
	 * Go to the request for a user account.
	 * 
	 * @param ie
	 * @param userID
	 * @throws Exception
	 */
	public void gotoRequestForUserAccount(IE ie, String userID) throws Exception {
		Link view = getViewLinkToRequestForUserAccount(ie, userID);
		if(view != null) {
			view.click();
			ie.waitUntilReady();
		}
	}
	
	private Link getViewLinkToRequestForUserAccount(IE ie, String userID) throws Exception {
		gotoManageUserRegistrationRequest(ie);
		TableRows trs = ie.rows(xpath("//DIV[@id='pageContent']/TABLE[@class='list']/TBODY/TR"));
		for(int i = 0; i < trs.length(); i++) {
			TableRow tr = trs.get(i);
			TableCell userid = tr.cell(1);
			String id = userid.text();
			if(id.equals(userID)) {
				return tr.link(text("View"));
			}
		}
		return null;
	}

	/**
	 * Reject the request for a user account.
	 * 
	 * @param ie
	 * @param userID
	 * @throws Exception
	 */
	public void rejectRequestForUserAccount(IE ie, String userID) throws Exception {
		gotoRequestForUserAccount(ie, userID);
		ie.link(text("Reject")).click();
		ie.waitUntilReady();
		if(!ie.containsText("Customer account removed.")) {
			throw new TestCaseFailedException();
		}
	}
	
	/**
	 * Checks to see if there is a request for a user account.
	 *  
	 * @param ie
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public boolean isRequestForUserAccount(IE ie, String userID) throws Exception {
		boolean result = false;
		Link view = getViewLinkToRequestForUserAccount(ie, userID);
		if(view != null)
			result = true;
		
		return result;
	}

	/**
	 * Generates a random string no longer than the specified length.
	 * 
	 * @param length
	 */
	public String generateRandomString(int length) throws Exception {
		Random r = new Random();
		int n = r.nextInt();
		String result = Integer.toString(n);
		return result;
	}

	/**
	 * This will select a random customer from the first page of the
	 * Manage Customer list. If there are no customers on the list
	 * it will return null.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public String getRandomCustomer(IE ie) throws Exception {
		String result = null;
		gotoManageCustomers(ie);
		Links ls = ie.links(href("/customerShow/"));
		if(ls != null && ls.length() > 0) {
			Random r = new Random();
			int n = r.nextInt(ls.length());
			Link l = ls.get(n);
			result = l.text();
			n = result.indexOf(" - ");
			result = result.substring(0, n).trim();
		}
		return result;
	}

	/**
	 * Return a random division from the given customer. If the customer
	 * has no divisions this will return null. If customer is null this
	 * will return null.
	 * 
	 * @param ie
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public String getRandomDivision(IE ie, String customer) throws Exception {
		String result = null;
		if(customer != null) {
			gotoEditCustomerFiltered(ie, customer);
			ie.link(text("/" + customer + "/")).click();
			ie.waitUntilReady();
			ie.link(text("Divisions")).click();
			TextFields tfs = ie.textFields(xpath("//DIV[@id='divisionContainer']/INPUT"));
			if(tfs != null && tfs.length() > 0) {
				Random r = new Random();
				int n = r.nextInt(tfs.length());
				TextField tf = tfs.get(n);
				result = tf.get().trim();
			}
		}
		return result;
	}

	/**
	 * Returns a random product type. If there are no product types
	 * it will return null. However, there should always be at least
	 * one product type or that is a defect.
	 * 
	 * The product type can be a standard or master product type.
	 * The product type may also have auto attributes and required
	 * fields.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public String getRandomProductType(IE ie) throws Exception {
		String result = null;
		gotoManageProductTypes(ie);
		Links ls =ie.links(href("/productType.action/"));
		if(ls != null && ls.length() > 0) {
			Random r = new Random();
			int n = r.nextInt(ls.length());
			Link l = ls.get(n);
			result = l.text().trim();
		}
		return result;
	}

	/**
	 * Goes to the Manage Product Status section and selects a random entry.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public String getRandomProductStatus(IE ie) throws Exception {
		String result = null;
		gotoManageProductStatuses(ie);
		TableCells tcs = ie.cells(xpath("//TD[1]"));
		if(tcs != null && tcs.length() > 0) {
			Random r = new Random();
			int n = r.nextInt(tcs.length());
			TableCell tc = tcs.get(n);
			result = tc.text().trim();
		}
		return result;
	}

	/**
	 * Get a random inspection type for a given product type.
	 * 
	 * @param ie
	 * @param productType
	 * @return
	 * @throws Exception
	 */
	public String getRandomInspectionType(IE ie, String productType) throws Exception {
		String result = null;
		ArrayList<String> its = new ArrayList<String>();
		gotoSelectInspectionTypes(ie, productType);
		TableRows trs = ie.rows(xpath("//TR"));
		for(int i = 1; i < trs.length(); i++) {
			TableRow tr = trs.get(i);
			Checkbox cb = tr.checkbox(0);
			if(cb.checked()) {
				TableCell tc = tr.cell(1);
				its.add(tc.text());
			}
		}
		if(its.size() > 0) {
			Random r = new Random();
			int n = r.nextInt(its.size());
			result = its.get(n).trim();
		}
		return result;
	}

	/**
	 * Go to the Select Inspection Types page from the View Product Type page.
	 * @param ie
	 * @param productType
	 * @throws Exception
	 */
	public void gotoSelectInspectionTypes(IE ie, String productType) throws Exception {
		gotoViewProductType(ie, productType);
		ie.link(text("/Inspection Types/")).click();
		ie.waitUntilReady();
	}

	/**
	 * Get the inspection type group for a given inspection type.
	 * 
	 * @param ie
	 * @param inspectionType
	 * @return
	 * @throws Exception
	 */
	public String getInspectionTypeGroup(IE ie, String inspectionType) throws Exception {
		String result = null;
		gotoInspectionType(ie, inspectionType);
		Link l = ie.link(xpath("//DIV[@class='viewSection']/P/SPAN[@class='fieldValue']/A"));
		if(l != null) {
			result = l.text().trim();
		}
		return result;
	}

	/**
	 * This method will validate that all the fields on the search criteria match
	 * the given values. If you execute runEventJobAddEventSearch, the fields used
	 * for that method call should still be set for this validation call.
	 *  
	 * @param ie
	 * @param scheduleStatus
	 * @param serialNumber
	 * @param customer
	 * @param division
	 * @param inspectionTypeGroup
	 * @param productType
	 * @param productStatus
	 * @param fromDate
	 * @param toDate
	 */
	public void validateEventJobAddEventSearch(IE ie, String scheduleStatus,
			String serialNumber, String customer, String division,
			String inspectionTypeGroup, String productType,
			String productStatus, String fromDate, String toDate) throws Exception {
		
		if(scheduleStatus != null) {
			if(!ie.selectList(id("reportForm_criteria_status")).getSelectedItems().get(0).equals(scheduleStatus)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(serialNumber != null) {
			if(!ie.textField(id("reportForm_criteria_serialNumber")).value().equals(serialNumber)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(customer != null){
			if(!ie.selectList(id("reportForm_criteria_customer")).getSelectedItems().get(0).equals(customer)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(division != null) {
			if(!ie.selectList(id("division")).getSelectedItems().get(0).equals(division)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(inspectionTypeGroup != null) {
			if(!ie.selectList(id("reportForm_criteria_inspectionType")).getSelectedItems().get(0).equals(inspectionTypeGroup)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(productType != null) {
			if(!ie.selectList(id("reportForm_criteria_productType")).getSelectedItems().get(0).equals(productType)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(productStatus != null) {
			if(!ie.selectList(id("reportForm_criteria_productStatus")).getSelectedItems().get(0).equals(productStatus)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(fromDate != null) {
			if(!ie.textField(id("reportForm_fromDate")).value().equals(fromDate)) {
				throw new TestCaseFailedException();
			}
		}
		
		if(toDate != null) {
			if(!ie.textField(id("reportForm_toDate")).value().equals(toDate)) {
				throw new TestCaseFailedException();
			}
		}
	}

	/**
	 * Go to add an employee user within Manage System Users.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoAddEmployeeUser(IE ie) throws Exception {
		gotoManageSystemUsers(ie);
		ie.link(text("/Add Employee User/")).click();
		ie.waitUntilReady();
	}

	/**
	 * Return the string for today's date in the format MM/dd/yy.
	 * 
	 * @return
	 */
	public String getTodayWithoutTime() {
		String result = null;
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy");
		result = date.format(new Date());
		return result;
	}

	/**
	 * Get today's date with time in the format MM/dd/yy hh:mm aa.
	 * 
	 * @return
	 */
	public String getTodayWithTime() {
		String result = null;
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy hh:mm aa");
		result = date.format(new Date());
		return result;
	}

	/**
	 * Does what you expect.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @param inspectionType
	 * @param date
	 * @throws Exception
	 * @deprecated use the addInspectionSchedule with five parameters and set last parameter to null
	 */
	public void addInspectionSchedule(IE ie, String serialNumber, String inspectionType, String date) throws Exception {
	}

	/**
	 * Like the original addInspection but this allows you to assign a job as well.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @param scheduleDate
	 * @param inspectionType
	 * @param title
	 * @throws Exception
	 */
	public void addInspectionSchedule(IE ie, String serialNumber, String inspectionType, String scheduleDate, String jobTitle) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		ie.textField(id("newSchedule_nextDate")).set(scheduleDate);
		ie.selectList(id("newSchedule_type")).option(text(inspectionType)).select();
		if(jobTitle != null)
			ie.selectList(id("newSchedule_project")).option(text(jobTitle)).select();
		ie.button(id("newSchedule_label_save")).click();
	}

	/**
	 * Validates that the date and job information remains
	 * selected when you edit an inspection schedule
	 * 
	 * @param ie
	 * @param serialNumber
	 * @param inspectionType
	 * @param scheduleDate
	 * @param jobTitle
	 * @throws Exception
	 */
	public void validateInspectionSchedule(IE ie, String serialNumber, String inspectionType, String scheduleDate, String jobTitle) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		String id = findEditIDForInspectionSchedule(ie, inspectionType, scheduleDate);
		Link edit = findEditLinkForInspectionSchedule(ie, inspectionType, scheduleDate);
		edit.click();
		Thread.sleep(hack);
		
		if(!ie.textField(id("schedule_" + id + "_nextDate")).value().equals(scheduleDate)) {
			throw new TestCaseFailedException();
		}
		
		List<String> selectedItems = ie.selectList(id("schedule_" + id + "_project")).getSelectedItems();
		if(selectedItems.size() != 1 && !selectedItems.get(0).equals(jobTitle)) {
			throw new TestCaseFailedException();
		}
	}

	/**
	 * Go to the add event page for a given job. Assumes the job is an event job.
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void gotoAddEvent(IE ie, String title) throws Exception {
		gotoJob(ie, title);
		ie.link(text("/add an event/")).click();
	}
	
	/**
	 * Delete all the events from a job. It will go to the add event page
	 * and select "Remove all from Job".
	 * 
	 * @param ie
	 * @param title
	 * @throws Exception
	 */
	public void deleteAllEventsFromJob(IE ie, String title) throws Exception {
		gotoAddEvent(ie, title);
		ie.link(text("Remove all from Job")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * To be called immediately after deleteAllEventsFromJob to see
	 * if the message for removing all jobs is present and correct.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public boolean validateRemoveAllFromJob(IE ie) throws Exception {
		boolean result = false;
		if(ie.containsText("schedule(s) successfully removed from the Job."))
			result = true;
		return result;
	}

	/**
	 * To be called immediately after deleteAllEventsFromJob to see
	 * if the message for removing all jobs is present and correct.
	 * 
	 * The count is the number of events removed from the job.
	 * 
	 * @param ie
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public boolean validateRemoveAllFromJob(IE ie, String count) throws Exception {
		boolean result = false;
		if(ie.containsText(count + " schedule(s) successfully removed from the Job."))
			result = true;
		return result;
	}

	/**
	 * Get a list of all the customers on the current tenant.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getCustomerList(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		gotoManageCustomers(ie);
		
		boolean loopFlag = true;
		do {
			List<String> tmp = getCustomersFromCurrentPage(ie);
			for (String customer : tmp) {
				int i = customer.indexOf(" - ");
				String c = customer.substring(0, i);
				result.add(c);
			}
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		return result;
	}

	/**
	 * Get a list of customers but only if they have divisions defined.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getCustomerWithDivisionsList(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		gotoManageCustomers(ie);
		
		boolean loopFlag = true;
		do {
			List<String> tmp = getCustomersFromCurrentPage(ie);
			for (String customer : tmp) {
				ie.link(text(customer)).click();
				ie.link(text("Divisions")).click();
				TextFields tfs = ie.textFields(xpath("//DIV[@id='divisionContainer']/INPUT"));
				if(tfs != null && tfs.length() > 0) {
					int i = customer.indexOf(" - ");
					String c = customer.substring(0, i);
					result.add(c);
				}
				ie.back();
			}
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		return result;
	}

	private List<String> getCustomersFromCurrentPage(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		Links ls = ie.links(href("/customerShow.action/"));
		for (int i = 0; i < ls.length(); i++) {
			Link link = ls.get(i);
			result.add(link.text().trim());
		}
		
		return result;
	}

	/**
	 * Get a list of all the divisions for a given customer.
	 * 
	 * @param ie
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public List<String> getDivisionList(IE ie, String customer) throws Exception {
		List<String> result = new ArrayList<String>();
		
		gotoEditCustomerDivisions(ie, customer);
		TextFields tfs = ie.textFields(xpath("//DIV[@id='divisionContainer']/INPUT"));
		for(int i = 0; i < tfs.length(); i++) {
			TextField tf = tfs.get(i);
			String division = tf.value();
			result.add(division);
		}
		
		return result;
	}

	/**
	 * Go to the page for editing the divisions for a customer.
	 * 
	 * @param ie
	 * @param customer
	 * @throws Exception
	 */
	public void gotoEditCustomerDivisions(IE ie, String customer) throws Exception {
		gotoEditCustomer(ie, customer);
		ie.link(text("Divisions")).click();
	}

	private void gotoEditCustomer(IE ie, String customer) throws Exception {
		gotoEditCustomerFiltered(ie, customer);
		ie.link(text("/" + customer + "/")).click();
		ie.waitUntilReady();
	}

	/**
	 * Given a customer name return the customer ID.
	 * 
	 * @param ie
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public String getCustomerID(IE ie, String customer) throws Exception {
		String result = null;
		gotoEditCustomer(ie, customer);
		Div d = ie.div(xpath("//DIV/SPAN[text()='Customer ID:']/.."));
		result = d.span(1).text();	// assumes specific HTML structure
		return result;
	}

	/**
	 * Validate the contents for a job. All inputs which are not null will
	 * be compared against what is in the system.
	 * 
	 * @param ie
	 * @param jobID
	 * @param title
	 * @param customer
	 * @param division
	 * @param status
	 * @param open
	 * @param description
	 * @param startDate
	 * @param estimateDate
	 * @param actualDate
	 * @param duration
	 * @param poNumber
	 * @param workPerformed
	 * @return
	 * @throws Exception
	 */
	public boolean validateEventJobDetails(IE ie, String jobID, String title,
			String customer, String division, String status, boolean open,
			String description, String startDate, String estimateDate,
			String actualDate, String duration, String poNumber,
			String workPerformed)  throws Exception {
		boolean result = true;
		
		gotoEditJob(ie, title);

		if (!ie.containsText("Job Details"))
			result = false;

		if (jobID != null && !ie.textField(id("jobUpdate_projectID")).value().equals(jobID)) {
			result = false;
		}
		
		if (title != null && ie.textField(id("jobUpdate_name")).value().equals(title)) {
			result = false;
		}

		if (customer != null) {
			List<String> selected = ie.selectList(id("customer")).getAllContents();
			if(!selected.get(0).equals(customer))
				result = false;
		}

		if (division != null) {
			List<String> selected = ie.selectList(id("division")).getAllContents();
			if(!selected.get(0).equals(division))
				result = false;
		}
	
		if (status != null && ie.textField(id("jobUpdate_status")).value().equals(status)) {
			result = false;
		}
		
		if(open && !ie.checkbox(id("jobUpdate_open")).isSet()) {
			result = false;
		}
		
		if(!open && ie.checkbox(id("jobUpdate_open")).isSet()) {
			result = false;
		}
		
		if(description != null && ie.textField(id("jobUpdate_description")).value().equals(description)) {
			result = false;
		}
		
		if (startDate != null && ie.textField(id("jobUpdate_started")).value().equals(startDate)) {
			result = false;
		}
		
		if (estimateDate != null && ie.textField(id("jobUpdate_estimatedCompletion")).value().equals(estimateDate)) {
			result = false;
		}
		
		if (actualDate != null && ie.textField(id("jobUpdate_actualCompletion")).value().equals(actualDate)) {
			result = false;
		}
		
		if (duration != null && ie.textField(id("jobUpdate_duration")).value().equals(duration)) {
			result = false;
		}
		
		if(poNumber != null && ie.textField(id("jobUpdate_poNumber")).value().equals(poNumber)) {
			result = false;
		}
		
		if(workPerformed != null && !ie.textField(id("jobUpdate_workPerformed")).value().equals(workPerformed)) {
			result = false;
		}

		return result;
	}

	/**
	 * Go to the Check Compliance page from the Product Info page. This
	 * method assumes (a) the product exists, (b) the tenant has the extra
	 * feature Compliance and (c) the product has some sort of compliance
	 * we can check.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoComplianceCheck(IE ie, String serialNumber) throws Exception {
		this.gotoProductInfo(ie, serialNumber);
		ie.link(text("Compliance Check")).click();
	}
	
	/**
	 * This gets called immediately after gotoComplianceCheck. If
	 * there was an error this will return false.
	 *  
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public boolean validateComplianceCheckNoSchedules(IE ie) throws Exception {
		boolean result = true;
		if(!ie.containsText("This product does not have any inspection schedules set up on it. You need to schedule inspections to perform a compliance check.")) {
			result = false;
		}
		return result;
	}

	/**
	 * There was a bug where the Assets link was available even when you
	 * were on the Assets page. This checks that this condition is not
	 * happening again.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public boolean validateJobAssets(IE ie) throws Exception {
		boolean result = true;
		
		Links ls = ie.links(text("Assets"));
		if(ls != null && ls.length() > 1) {	// There is Assets on the top menu bar
			result = false;
		}
		
		return result;
	}
	
	/**
	 * This is used to expand the Select Columns area of searching.
	 * It works if you are on the Assets, Reporting or Schedule page.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void expandSelectColumns(IE ie) throws Exception {
		ie.link(id("open_selectColumnForm")).click();
		ie.waitUntilReady();
		Thread.sleep(hack);
	}

	/**
	 * There is some issue with selecting product type on the the search
	 * criteria form not changing the product attribute columns available.
	 * This method will select a product type then fire the onchange event
	 * so the form will detect the change and update the product attribute
	 * columns.
	 * 
	 * @param ie
	 * @param productType
	 * @throws Exception
	 */
	public void selectProductTypeOnSearchForm(IE ie, String productType) throws Exception {
		ie.selectList(id("reportForm_criteria_productType")).option(text(productType)).select();
		ie.selectList(id("reportForm_criteria_productType")).fireEvent("onchange");
		ie.waitUntilReady();
		Thread.sleep(hack);
	}
	
	/**
	 * Used to convert a filename to something acceptable by Windows.
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public String filenameFixer(String filename) throws Exception {
		String result = filename;
		result = result.replace("\\", "-backslash-");
		result = result.replace("/", "-slash-");
		result = result.replace("?", "-question-");
		result = result.replace("%", "-percent-");
		result = result.replace("*", "-asterisk-");
		result = result.replace(":", "-colon-");
		result = result.replace("|", "-pipe-");
		result = result.replace("\"", "-quote-");
		result = result.replace("<", "-lt-");
		result = result.replace(">", "-gt-");
		result = result.replace("$", "-dollar-");
		result = result.replace("^", "-carat-");
		result = result.replace("!", "-exclaim-");
		result = result.replace("(", "");
		result = result.replace(")", "");

		return result;
	}

	/**
	 * Validate the product attribute on the Search form against a given set
	 * of attributes. This is to test a bug where the attributes were not
	 * reflective of the product type selected.
	 * 
	 * @param ie
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	public boolean validateProductAttributesOnSearchForm(IE ie, Set<String> attributes) throws Exception {
		boolean result = true;
		Set<String> current = new TreeSet<String>();
		Divs divs = ie.divs(xpath("//DIV[@class='pageSection']/DIV[@id='selectColumnForm']/DIV[@id='product_search_product_info_options']/DIV"));
		for(int i = 0; i < divs.length(); i++) {
			Div div = divs.get(i);
			current.add(div.text().trim());
		}
		result = current.equals(attributes);
		
		return result;
	}

	/**
	 * Add resources (employees) to a job.
	 * 
	 * @param ie
	 * @param jobTitle
	 * @param employee
	 * @throws Exception
	 */
	public void addResourceToJob(IE ie, String jobTitle, String employee) throws Exception {
		gotoJob(ie, jobTitle);
		openAssignResource(ie);
		ie.waitUntilReady();
		Thread.sleep(hack);
		ie.selectList(id("employee")).option(text(employee)).select();
		ie.button(id("addResource_label_assign")).click();
	}

	private void openAssignResource(IE ie) throws Exception {
		ie.link(text("assign resource")).click();
	}

	/**
	 * validate that the employees in assigned to a job match the given set of employees.
	 * 
	 * @param ie
	 * @param jobTitle
	 * @param employees
	 * @return
	 * @throws Exception
	 */
	public boolean validateJobAssignedResources(IE ie, String jobTitle, Set<String> employees) throws Exception {
		boolean result = true;
		gotoJob(ie, jobTitle);
		Set<String> emps = getResourcesAssignedToJob(ie, jobTitle);
		result = emps.equals(employees);
		return result;
	}

	/**
	 * Get a list of the resources (employees) assigned to a job.
	 * 
	 * @param ie
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public Set<String> getResourcesAssignedToJob(IE ie, String title) throws Exception {
		gotoJob(ie, title);
		Spans spans = ie.spans(xpath("//DIV[@id='jobResources']/DIV[@class='jobResource ']/SPAN[@class='jobResourceName']"));
		Set<String> emps = new TreeSet<String>();
		for(int i = 0; i < spans.length(); i++) {
			emps.add(spans.get(i).text().trim());
		}
		return emps;
	}
	
	/**
	 * This method will compare the resources assigned to a job
	 * against the names on the drop down list. If there are names
	 * on both this returns false.
	 * 
	 * @param ie
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public boolean validateJobAssignedResourcesDropDown(IE ie, String title) throws Exception {
		boolean result = true;
		gotoJob(ie, title);
		Set<String> assigned = getResourcesAssignedToJob(ie, title);
		Options unassigned = ie.selectList(id("employee")).options();
		for(int i = 0; i < unassigned.length(); i++) {
			String name = unassigned.get(i).text().trim();
			if(assigned.contains(name)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * Remove a resource (employee) from a job.
	 * 
	 * @param ie
	 * @param jobTitle
	 * @param resource
	 * @throws Exception
	 */
	public void deleteResourceAssignedToJob(IE ie, String jobTitle, String resource) throws Exception {
		gotoJob(ie, jobTitle);
		Divs divs = ie.divs(xpath("//DIV[@id='jobResources']/DIV"));
		Div div = null;
		for(int i = 0; i < divs.length(); i++) {
			if(divs.get(i).text().contains(resource)) {
				div = divs.get(i);
				break;
			}
		}
		if(div != null) {
			Link l = div.link(0);
			l.click();
		}
	}

	/**
	 * Get the list of jobs on the Home page.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public Set<String> getJobsOnHomePage(IE ie) throws Exception {
		Set<String> jobs = new TreeSet<String>();
		TableRows trs = ie.rows(xpath("//DIV[@id='jobs']/TABLE/TBODY/TR"));
		// First row is colspan="4" and has "You current have xx Open job(s) assigned to you."
		for(int i = 2; i < trs.length(); i++) {
			TableCell td = trs.get(i).cell(1);
			jobs.add(td.text().trim());
		}
		return jobs;
	}

	/**
	 * validate that the given username (employee) is assigne to the given job.
	 * 
	 * @param ie
	 * @param job
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public boolean validateJobAssignedToEmployee(IE ie, String job,	String userName) throws Exception {
		boolean result = true;
		
		Set<String> users = getResourcesAssignedToJob(ie, job);
		result = users.contains(userName);

		return result;
	}

	/**
	 * Clicks on the link for jobs assigned to me from the Home page.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoJobsAssignedToMe(IE ie) throws Exception {
		gotoJobs(ie);
		ie.link(text("/Only Jobs I am assigned to/")).click();
		ie.waitUntilReady();
	}

	/**
	 * checks to see if a user is assigned to a job.
	 * 
	 * @param ie
	 * @param jobTitle
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public boolean isUserAssignedToJob(IE ie, String jobTitle, String userName) throws Exception {
		boolean result = false;
		gotoJob(ie, jobTitle);
		result = this.validateJobAssignedToEmployee(ie, jobTitle, userName);
		return result;
	}

	/**
	 * Delete a scheduled inspection for a given product. If the
	 * tenant does not have Jobs (Projects) then set the jobTitle
	 * to null. If you want to delete the first schedule which
	 * matches inspection type and schedule date regardless of 
	 * the Job, you can pass in a null for jobTitle on a Jobs
	 * tenant and it will ignore the job title.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @param inspectionType
	 * @param nextScheduledDate
	 * @param jobTitle
	 * @throws Exception
	 */
	public void deleteScheduleFromProduct(IE ie, String serialNumber,
			String inspectionType, String nextScheduledDate, String jobTitle) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		TableRows trs = ie.rows(id("/^type_/"));
		for(int i = 0; i <  trs.length(); i++) {
			TableRow tr = trs.get(i);
			TableCell type = tr.cell(xpath("./TD[@class='name' and text()='" + inspectionType + "']"));
			Span date = tr.span(xpath("./TD/SPAN[text()='" + nextScheduledDate + "']"));
			Span job;
			if(jobTitle != null) {
				job = tr.span(xpath("./TD/SPAN[text()='" + jobTitle + "']"));
			} else {
				job = date;
			}
			if(date.exists() && job.exists() && type.exists()) {
				Link l = tr.link(text("/Remove/"));
				l.click();
				break;
			}
		}
	}

	/**
	 * delete all the scheduled inspections for a given product.
	 * 
	 * @param ie
	 * @param serialNumber
	 * @throws Exception
	 */
	public void deleteAllScheduleFromProduct(IE ie, String serialNumber) throws Exception {
		gotoInspectionSchedule(ie, serialNumber);
		Links ls = ie.links(xpath("//TBODY/TR/TD/SPAN/A[text()='Remove']"));
		// As I click the Remove links, the size of Links decreases.
		// If the Links collection has 5 elements, clicking on element
		// zero 5 times will delete the entire list.
		for(int i = 0; i < ls.length(); i++) {
			Link l = ls.get(0);
			l.click();
			Thread.sleep(hack);
		}
	}

	/**
	 * validate that there is a link on the Jobs page for "Only Jobs I am assigned to".
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void validateJobsAssignedToMe(IE ie) throws Exception {
		if(ie.link(text("/Only Jobs I am assigned to/")).exists()) {
			throw new TestCaseFailedException();
		}
		if(!ie.link(text("/All Jobs/")).exists()) {
			throw new TestCaseFailedException();			
		}
	}

	/**
	 * 
	 * @param ie
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public List<String> getListOfResourcesAssignable(IE ie, String title) throws Exception {
		List<String> employees = new ArrayList<String>();
		gotoJob(ie, title);
		openAssignResource(ie);
		Options os = ie.options(xpath("//SPAN/SELECT[@id='employee']/OPTION"));
		for(int i = 0; i < os.length(); i++) {
			Option o = os.get(i);
			employees.add(o.text());
		}
		return employees;
	}

	/**
	 * Get a list of product types but only those with manufacturer certificates.
	 * 
	 * @param ie
	 * @return
	 * @throws Exception
	 */
	public List<String> getProductTypesWithManufacturersCert(IE ie) throws Exception {
		List<String> result = new ArrayList<String>();
		gotoManageProductTypes(ie);
		Links edits = ie.links(text("Edit"));
		for(int i = 0; i < edits.length(); i++) {
			Link l = edits.get(i);
			l.click();
			ie.waitUntilReady();
			if(ie.checkbox(id("productTypeUpdate_hasManufacturerCertificate")).getState()) {
				String productType = ie.textField(id("productTypeUpdate_name")).value();
				result.add(productType);
			}
			ie.back();
			ie.waitUntilReady();
		}
		return result;
	}

	/**
	 * Select the list of asset types and event types then publish the
	 * catalog.
	 * 
	 * @param ie
	 * @param assetTypes
	 * @param eventTypes
	 * @throws Exception
	 */
	public void publishCatalog(IE ie, List<String> assetTypes, List<String> eventTypes) throws Exception {
		gotoPublish(ie);
		Divs assets = ie.divs(xpath("//FORM[@id='publishForm']/DIV[@class='selectOptions']/DIV[@class='customSelection']/H3[text()='Asset Types']/../DIV[@class='customSelectionType']"));
		for(int i = 0; i < assets.length(); i++) {
			Div type = assets.get(i);
			String s = type.label(0).text();
			Checkbox c = type.checkbox(xpath("SPAN/INPUT"));
			c.set(assetTypes.contains(s));
		}
		
		Divs events = ie.divs(xpath("//FORM[@id='publishForm']/DIV[@class='selectOptions']/DIV[@class='customSelection']/H3[text()='Event Types']/../DIV[@class='customSelectionType']"));
		for(int i = 0; i < events.length(); i++) {
			Div type = events.get(i);
			String s = type.label(0).text();
			Checkbox c = type.checkbox(xpath("SPAN/INPUT"));
			c.set(eventTypes.contains(s));
		}
		ie.button(id("publish")).click();
		ie.waitUntilReady();
	}

	/**
	 * go to the Publish page in the Manage Safety Network area.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void gotoPublish(IE ie) throws Exception {
		gotoManageSafetyNetwork(ie);
		ie.link(text("Publish")).click();
		ie.waitUntilReady();
	}

	/*
	 * There are startMonitorStatus and stopMonitorStatus methods. The
	 * startMonitorStatus will check the status bar of IE once a second.
	 * If the status bar does not change after a period of time (max)
	 * we assume IE is hung and send a refresh signal. If the period of
	 * time gets reset, i.e. the status bar changes, we record the reset.
	 * If the page appears to be hung, we refresh the page and reset the
	 * counter.
	 */
	static long refreshes = 1;
	static long resets = 1;
	static final int TIMEOUT = 180;	// number of seconds before Watij times out 
	static boolean running = false;
	
	/**
	 * The running variable keeps the thread created in startMonitorStatus
	 * looping. If this variable is set to false then the thread in
	 * startMonitorStatus exits.
	 * 
	 * You should call this method before you call ie.close() or ie = null.
	 * 
	 * @throws Exception
	 */
	public void stopMonitorStatus() throws Exception {
		running = false;
	}
	
	/**
	 * For various reasons, e.g. transparent PNGs cause pages to occasionally
	 * fail to load in Internet Explorer, you need to run this method once the
	 * IE window is open. It will monitor the status bar of the window. Every
	 * second the thread will wake up and see what the status bar is displaying.
	 * If it has not changed a counter is incremented. Once the counter reaches
	 * a maximum value, we assume IE is stuck and needs to be refreshed. So we
	 * refresh the page and start waiting again. Every time the page status
	 * changes, we reset the counter.
	 * 
	 * Any time you do an ie.start(), ie.attach(), ie.navigate(), ie.goTo()
	 * you should call this method and start a monitoring thread for that
	 * instance of IE. When you are going to quit IE, call the stopMonitorStatus
	 * method to kill the thread.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void startMonitorStatus(final IE ie) throws Exception {
		final IWebBrowser2 wb2 = ie.iWebBrowser2();
		new Thread(new Runnable() {
			public void run() {
				try {
					running = true;
					int count = 0;
					int max = TIMEOUT / 2;
					while(running) {
						String previous = wb2.getStatusText().toString();
						Thread.sleep(1000);
						String current = wb2.getStatusText().toString();
						if(current.equals(previous)) {
							count++;
						} else {
							count = 0;
						}
						if(count > max) {
							wb2.refresh();
							count = 0;
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}
	
	/**
	 * return the number of times the startMonitorStatus has reset
	 * the counter for the web browser.
	 * 
	 * @return
	 */
	public long getNumberOfResets() {
		return resets;
	}
	
	/**
	 * return the number of times the startMonitorStatus has needed
	 * to refresh the web browser because it appears to have gotten
	 * stuck.
	 * 
	 * @return
	 */
	public long getNumberOfRefreshes() {
		return refreshes;
	}

	/**
	 * Whenever you start IE, use this method instead of calling the ie.start()
	 * method directly. It will start a thread to monitor the status of IE.
	 * If the status does not change within a period of time it will refresh
	 * the page in hopes of restoring it.
	 * 
	 * @param ie
	 * @param loginURL
	 * @throws Exception
	 */
	public void start(IE ie, String loginURL) throws Exception {
		ie.start(loginURL);
		startMonitorStatus(ie);
	}

	/**
	 * See the start() with two parameters.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void start(IE ie) throws Exception {
		ie.start();
		startMonitorStatus(ie);
	}
}
