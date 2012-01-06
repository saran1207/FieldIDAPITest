package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.MyAccountPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.user.User;

public class DownloadLinksTest extends PageNavigatingTestCase<MyAccountPage>{

	private static final String PASSWORD = "password";
	private static final String USERID = "userid";
	private User defaultUser;

	@Override
	protected MyAccountPage navigateToPage() {
		return startAsCompany("test1").login(USERID, PASSWORD).clickMyAccount(defaultUser.getDisplayName());
	}

	@Override
	public void setupScenario(Scenario scenario) {
		User user = scenario.aUser()
							.withUserId(USERID)
							.withEmailAddress(USERID)
							.withPassword(PASSWORD)
							.build();
		
		scenario.aDownloadLink()
		        .withTenant(scenario.defaultTenant())
		        .withUser(user)
		        .build();
		defaultUser = scenario.defaultUser();
	}
	
	@Test
	public void view_downloads_no_downloads() throws Exception {
		page.clickSignOut().login().clickMyAccount("N4 Admin").clickDownloads();
		assertEquals(0, page.getNumberOfItemsInDownloadList());	
	}
	
	@Test
	public void view_downloads() {
		page.clickDownloads();
		assertEquals(1, page.getNumberOfItemsInDownloadList());			
	}
	
	@Test
	public void edit_and_save_download_name() throws Exception {
		page.clickDownloads();
		page.editDownLoadName(1);
		page.enterName("new name");
		page.clickSave();
		assertEquals("new name | Edit", page.getDownloadNames().get(0));
	}
	
	@Test
	public void edit_and_cancel_download_name() throws Exception {
		page.clickDownloads();
		page.editDownLoadName(1);
		page.enterName("new name");
		page.clickCancel();
		assertEquals("downloadLink | Edit", page.getDownloadNames().get(0));
	}
	
	@Test
	public void delete_a_download() {
		page.clickDownloads();
		page.clickDelete(1);
		assertEquals(0, page.getNumberOfItemsInDownloadList());	
	}

}
