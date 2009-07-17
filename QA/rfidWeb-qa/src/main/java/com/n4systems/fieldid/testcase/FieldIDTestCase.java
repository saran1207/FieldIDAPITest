package com.n4systems.fieldid.testcase;

import java.text.SimpleDateFormat;
import java.util.Random;
import watij.runtime.ie.IE;
import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.Assets;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.Home;
import com.n4systems.fieldid.Identify;
import com.n4systems.fieldid.Inspect;
import com.n4systems.fieldid.Login;
import com.n4systems.fieldid.MyAccount;
import com.n4systems.fieldid.Reporting;
import com.n4systems.fieldid.Schedule;
import com.n4systems.fieldid.admin.ManageCustomers;
import com.n4systems.fieldid.admin.ManageInspectionTypes;
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.admin.ManageUsers;

import junit.framework.TestCase;

public abstract class FieldIDTestCase extends TestCase {
	IE ie = new IE();
	FieldIDMisc misc = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Admin admin = new Admin(ie);
	ManageCustomers mcs = new ManageCustomers(ie); 
	ManageUsers mus = new ManageUsers(ie);
	ManageProductTypes mpts = new ManageProductTypes(ie);
	ManageInspectionTypes mits = new ManageInspectionTypes(ie);
	Identify identify = new Identify(ie);
	Home home = new Home(ie);
	Assets assets = new Assets(ie);
	MyAccount myAccount = new MyAccount(ie);
	Inspect inspect = new Inspect(ie);
	Reporting reporting = new Reporting(ie);
	Schedule schedule = new Schedule(ie);

	static String timestamp = null;
	static boolean once = true;
	final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
	static Random r = new Random();

	String loginURL = "https://localhost.localdomain/fieldid/";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = misc.createTimestampDirectory() + "/";
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		ie.close();
	}
}
