package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.administration.page.ManageUsers;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;

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
		ManageUsersPage manageUsersPage = homePage.clickSetupLink().clickManageUsers();
		
		// user the search filter to change from employee/customer
		if(editUserFormId.contains("employee")) {
			manageUsersPage.selectSearchUserType(ManageUsers.USER_TYPE_EMPLOYEE);
		} else {
			manageUsersPage.selectSearchUserType(ManageUsers.USER_TYPE_CUSTOMER);
		}
		manageUsersPage.clickSearchButton();

		// click on the first user in the table. assumes there is at least one employee and one customer
		String userName = manageUsersPage.clickFirstSearchResult();

		manageUsersPage.clickChangePasswordTab();
		manageUsersPage.clickCancelChangePassword();
		
		assertEquals("Edit", manageUsersPage.getCurrentTab());
		assertEquals(userName, manageUsersPage.getUserId());
	}
}
