package com.n4systems.fieldid.selenium.testcase;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.misc.Misc;

@RunWith(value = Parameterized.class)
public class ChangePasswordTest extends LoggedInTest {

	
	private String userName;
	private String editUserFormId;

	public ChangePasswordTest(String editUserFormId, String userName) {
		super("n4systems", "makemore$");
		this.userName = userName;
		this.editUserFormId = editUserFormId;
	}
	
	

	@Parameters
	public static Collection<String[]> data() {
		Collection<String[]> data = new ArrayList<String[]>();
		data.add(new String[]{"#employeeUserUpdate", "aaitken"});
		data.add(new String[]{"#customerUserUpdate", "user1"});
		return data;
	}
	

	@Test
	public void should_cancel_back_to_edit_page_of_user() throws Exception {
		
		selenium.open("/fieldid/userList.action");
		selenium.click("link=" + userName);
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		selenium.click("link=Change Password");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		selenium.click("label.cancel");
		misc.waitForPageToLoadAndCheckForOopsPage();
		
		assertTrue("should be on the user edit page", selenium.isElementPresent("css=" + editUserFormId));
		assertEquals(userName, selenium.getValue("css=input[name='userId']"));
	}




	
	
}
