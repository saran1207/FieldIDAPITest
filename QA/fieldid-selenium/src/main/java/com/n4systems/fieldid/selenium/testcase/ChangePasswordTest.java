package com.n4systems.fieldid.selenium.testcase;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

@RunWith(value = Parameterized.class)
public class ChangePasswordTest extends LoggedInTestCase {
	
	private String editUserFormId;

	public ChangePasswordTest(String editUserFormId) {
		this.editUserFormId = editUserFormId;
	}
	
	@Parameters
	public static Collection<String[]> data() {
		Collection<String[]> data = new ArrayList<String[]>();
		data.add(new String[]{"#employeeUserUpdate"});
		data.add(new String[]{"#customerUserUpdate"});
		return data;
	}

	@Test
	public void should_cancel_back_to_edit_page_of_user() throws Exception {
		// user the search filter to change from employee/customer
		if(editUserFormId.contains("employee")) {
			selenium.open("/fieldid/userList.action?currentPage=1&listFilter=&userType=EMPLOYEES&search=Search");
		} else {
			selenium.open("/fieldid/userList.action?currentPage=1&listFilter=&userType=CUSTOMERS&search=Search");
		}
		// click on the first user in the table. assumes there is at least one employee and one customer
		String locator = "xpath=//table[@id='userList']/tbody/tr[3]/td[1]/a";
		String userName = selenium.getText(locator);
		selenium.click(locator);
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		selenium.click("link=Change Password");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		selenium.click("label.cancel");
		misc.waitForPageToLoadAndCheckForOopsPage();
		
		assertTrue("should be on the user edit page", selenium.isElementPresent("css=" + editUserFormId));
		assertEquals(userName, selenium.getValue("css=input[name='userId']"));
	}
}
