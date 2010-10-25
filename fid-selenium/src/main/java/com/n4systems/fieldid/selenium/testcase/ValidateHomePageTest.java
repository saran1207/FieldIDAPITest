package com.n4systems.fieldid.selenium.testcase;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.page.AssetSearch;
import com.n4systems.fieldid.selenium.home.page.Home;
import com.n4systems.fieldid.selenium.home.page.InstructionalVideos;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.reporting.page.Reporting;
import com.n4systems.fieldid.selenium.schedule.page.Schedules;

public class ValidateHomePageTest extends FieldIDTestCase {

	Login login;
	Home home;
	Schedules schedule;
	Reporting reporting;
	AssetSearch assets;
	InstructionalVideos videos;
	
	@Before
	public void setUp() throws Exception {
		login = new Login(selenium, misc);
		home = new Home(selenium, misc);
		schedule = new Schedules(selenium, misc);
		reporting = new Reporting(selenium, misc);
		assets = new AssetSearch(selenium, misc);
		videos = new InstructionalVideos(selenium, misc);
	}
	
	@Test
	public void validate_we_arrive_at_home_page() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("companyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.assertHomePage();
	}
	
	@Test
	public void validate_home_page_quick_setup_wizard_for_admin_user() throws Exception {
		String username = getStringProperty("adminusername");
		String password = getStringProperty("adminpassword");
		String company = getStringProperty("companyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.assertHomePageQuickSetupWizard();
	}
	
	@Test
	public void validate_home_page_jobs_section() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("jobscompanyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.assertHomePageJobsSection();
	}
	
	@Test
	public void validate_going_to_upcoming_inspections_from_home_page() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("companyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.clickViewUpcomingInspections();
		schedule.assertSchedulesSearchResultsPageHeader();
	}
	
	@Test
	public void validate_going_to_inspection_history_for_asset_from_home_page() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("companyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.clickViewInspectionHistoryForAnAsset();
		reporting.assertReportingPageHeader();
	}
	
	@Test
	public void validate_going_to_find_an_asset_from_home_page() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("companyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.clickFindAnAsset();
		assets.assertAssetsPage();
	}
	
	@Test
	public void validate_going_to_instruction_videos_from_home_page() throws Exception {
		String username = getStringProperty("username");
		String password = getStringProperty("password");
		String company = getStringProperty("companyid");

		startAsCompany(company);
		login.signInAllTheWayToHome(username, password);
		home.clickMoreForInstructionalVideos();
		videos.assertInstructionalVideosPageHeader();
	}
	
}
