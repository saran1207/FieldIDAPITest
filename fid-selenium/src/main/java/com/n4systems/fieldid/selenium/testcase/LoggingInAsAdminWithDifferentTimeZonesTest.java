package com.n4systems.fieldid.selenium.testcase;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.page.Login;

/**
 * WEB-1430
 * 
 * @author dgrainge
 *
 */
@RunWith(value = Parameterized.class)
public class LoggingInAsAdminWithDifferentTimeZonesTest extends FieldIDTestCase {

	Login login;
	private String userId;
	private String companyName;
	
	public LoggingInAsAdminWithDifferentTimeZonesTest(String companyName, String userId) {
		this.userId = userId;
		this.companyName = companyName;
	}

	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		// XXX: SQL code to update all users to have the same, known password
	}
	
	@Parameters
	public static Collection<String[]> data() {
		Collection<String[]> data = new ArrayList<String[]>();
		
		/**
		 * This is a list of tenants and the admin user for
		 * that tenant. It assumes the password for these
		 * users have been set to the password used in the
		 * Test below. Each tenant was selected because
		 * their timezone is different from the others in
		 * this list.
		 */
		data.add(new String[]{"unilift", "sarah"}); 
		data.add(new String[]{"aoc", "AOC"});
		data.add(new String[]{"bhp", "adam"});
		data.add(new String[]{"brogansafety", "dbodnar"});
		data.add(new String[]{"brs", "chrisf"});
		data.add(new String[]{"halo", "tsmith"});
		data.add(new String[]{"harriman", "ashley"});
		data.add(new String[]{"hesco", "ed"});
		data.add(new String[]{"illinois", "kcarver"});
		data.add(new String[]{"jcrenfroe", "jseibert"});
		data.add(new String[]{"lcrane", "lance"});
		data.add(new String[]{"redflame", "rcooper"});
		data.add(new String[]{"saturn", "wklassen"});
		
		return data;
	}

	@Test
	public void shouldBeAbleToLogIntoFieldIDAsAdminForTenantsWithDifferentTimeZones() throws Exception {
		// Assumes all users have had their password set to the same value
		String password = "makemore$";
		
		startAsCompany(companyName);
		login.signInAllTheWayToHome(userId, password);
		login.verifySignedIn();
		misc.gotoSignOut();
	}

}
