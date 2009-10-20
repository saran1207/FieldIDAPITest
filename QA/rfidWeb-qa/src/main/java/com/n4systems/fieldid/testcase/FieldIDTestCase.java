package com.n4systems.fieldid.testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import watij.runtime.ie.IE;
import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Inspect;
import com.n4systems.fieldid.Jobs;
import com.n4systems.fieldid.Login;
import com.n4systems.fieldid.MyAccount;
import com.n4systems.fieldid.Reporting;
import com.n4systems.fieldid.Schedule;
import com.n4systems.fieldid.admin.ManageCustomers;
import com.n4systems.fieldid.admin.ManageEventTypeGroups;
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageOrganizations;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.admin.ManageSystemSettings;
import com.n4systems.fieldid.admin.ManageUsers;
import com.n4systems.fieldid.admin.ManageYourSafetyNetwork;

import junit.framework.TestCase;

public abstract class FieldIDTestCase extends TestCase {

	protected IE ie = new IE();
	protected FieldIDMisc misc = new FieldIDMisc(ie);
	protected Login login = new Login(ie);
	protected Home home = new Home(ie);
	protected Identify identify = new Identify(ie);
	protected Inspect inspect = new Inspect(ie);
	protected Assets assets = new Assets(ie);
	protected Reporting reporting = new Reporting(ie);
	protected Schedule schedule = new Schedule(ie);
	protected Jobs jobs = new Jobs(ie);
	protected MyAccount myAccount = new MyAccount(ie);
	protected Admin admin = new Admin(ie);
	protected ManageOrganizations mos = new ManageOrganizations(ie);
	protected ManageCustomers mcs = new ManageCustomers(ie);
	protected ManageUsers mus = new ManageUsers(ie);
	protected ManageSystemSettings mss = new ManageSystemSettings(ie);
	protected ManageProductTypes mpts = new ManageProductTypes(ie);
	protected ManageInspectionTypes mits = new ManageInspectionTypes(ie);
	protected ManageEventTypeGroups metgs = new ManageEventTypeGroups(ie);
	protected ManageYourSafetyNetwork mysn = new ManageYourSafetyNetwork(ie);
	protected static String timestamp = null;
	protected static boolean once = true;
	protected String loginURL = "https://n4.team.n4systems.com/fieldid/";	// default to staging
	protected Properties prop;
	protected InputStream in;
	protected String propertyFile;
	protected File f;
		
	protected void setUp() throws Exception {
		super.setUp();
		if(once) {
			once = false;
			timestamp = misc.createTimestampDirectory() + "/";
		}
		try {
			prop = new Properties();
			// if className.properties exists, load it
			propertyFile = getClass().getName() + ".properties";
			f = new File(propertyFile);
			if(f.exists()) {
				in = new FileInputStream(propertyFile);
				prop.load(in);
				in.close();
			}
	
			// if testCase.properties exists, load it
			propertyFile = getName() + ".properties";
			f = new File(propertyFile);
			if(f.exists()) {
				in = new FileInputStream(propertyFile);
				prop.load(in);
				in.close();
			}
			// NOTE: if the same property exists in both files, testCase.properties overwrites
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing the test case");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		}

		// if loginurl is defined, override the default
		loginURL = prop.getProperty("loginurl", loginURL);
		misc.start();
		FieldIDMisc.initMonitor();
		FieldIDMisc.startMonitor();
		login.gotoLoginPage(loginURL);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		FieldIDMisc.stopMonitor();
		FieldIDMisc.quitMonitor();
		misc.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
		misc.forcefullyKillInternetExplorer();
	}
}
