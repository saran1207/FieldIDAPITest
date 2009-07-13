package com.n4systems.fieldid.testcase;

import static watij.finders.FinderFactory.*;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import watij.elements.Options;
import watij.runtime.ie.IE;

/**
 * The drop down list for inspectors on Reporting has way too many people in the list.
 * 
 * @author Darrell
 *
 */
public class WEB_552 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setUserName("n4systems");
		helper.setPassword("makemore$");
		helper.setTenant("brs");
		helper.start(ie, helper.getLoginURL());
		ie.maximize();
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);
	}
	
	public void testInspectorListOnReportingOnlyHasUsersWithInspectPermission() throws Exception {
		int i;
		String[] tmp = helper.getListOfUserNamesWithInspectPermission(ie, null, null, true);
		Set<String> inspectors1 = new TreeSet<String>();
		for(i = 0; i < tmp.length; i++) {
			if(!tmp[i].equals("") && tmp[i] != null)
				inspectors1.add(tmp[i]);
		}
		
		helper.gotoReporting(ie);
		Set<String> inspectors2 = new TreeSet<String>();
		Options o = ie.selectList(id ("reportForm_criteria_inspector")).options();
		for(i = 0; i < o.length(); i++) {
			String u = o.get(i).text();
			if(!u.equals(""))
				inspectors2.add(u);
		}

		if(!inspectors1.equals(inspectors2)) {
			Iterator<String> it = inspectors1.iterator();
			int j, size = inspectors1.size() > inspectors2.size() ? inspectors1.size() : inspectors2.size();
			String[] dups = new String[size];
			size = 0;
			while(it.hasNext()) {
				String user = it.next();
				// if the element is in inspector1 and inspector2, record it
				if(inspectors2.contains(user)) {
					dups[size++] = user;
				}
			}
			// remove duplicates
			for(j = 0; j < size; j++) {
				inspectors1.remove(dups[j]);
				inspectors2.remove(dups[j]);
			}
			System.err.println("People on both lists have been removed.");
			System.err.println("Because people might have had inspect permission, everyone should be on the second list.");
			System.err.println("If the person is on the second list you need to see if they are in System Users.");
			System.err.println("If they are not, check the database to see if they are deleted. Everything else is a bug.");
			System.err.println("Has only inspection permission: " + inspectors1);
			System.err.println("Only on the list of Inspectors: " + inspectors2);
			
			// NOTE: if a user was deleted they will not be found by my automation
			// but they are still in the system (hidden). This will report the user
			// should not be on the list BUT we need to check the database to see
			// if the user is there but flagged as 'deleted'.
			throw new TestCaseFailedException();
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
