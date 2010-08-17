package com.n4systems.fieldid.minimaldata.security;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.assets.page.AssetSearch;
import com.n4systems.fieldid.selenium.assets.page.AssetSearchResults;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.reporting.page.Reporting;
import com.n4systems.fieldid.selenium.schedule.page.Schedules;

@RunWith(Parameterized.class)
public class MinimalSecurityTest extends FieldIDTestCase { 
	private static Collection<String> serialNumberList = Arrays.asList( new String[]{ 
				"EC-0009", "EC-LE-0013", "EC-LE-BACK-0015", "EC-LE-FRONT-0014", "EC-UE-0010", "EC-UE-LEFT-0011", "EC-UE-RIGHT-0012", 
				"N-0001", "NS-0016", "NS-RELAX-0017", "NS-UPTIGHT-0018", "SS-0019", "SS-BRIGHT-0020", "SS-DULL-0021", 
				"WC-0002", "WC-NW-0003", "WC-NW-COLD-0005", "WC-NW-HOT-0004", "WC-SW-0006", "WC-SW-BLUE-0008", "WC-SW-RED-0007" });
	private Login login;
	private String user;
	private String password;
	private int numberOfProductsVisible;
	private int numberOfInspectionsVisible;
	private int numberOfIncompleteSchedulesVisible;
	private int numberOfCompleteSchedulesVisible;
	private Collection<String> serailNumberThatCanBeViewed;
	private Collection<String> serailNumberThatCanNotBeViewed;

	@Parameters
	public static Collection<Object[]> data() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { "primary", "makemore$", 21, serialNumberList});
		data.add(new Object[] { "back_division", "makemore$", 1, ImmutableList.of("EC-LE-BACK-0015") });
		data.add(new Object[] { "blue_division", "makemore$", 1, ImmutableList.of("WC-SW-BLUE-0008") });
		data.add(new Object[] { "cold_division", "makemore$", 1, ImmutableList.of("WC-NW-COLD-0005") });
		data.add(new Object[] { "ec_secondary", "makemore$", 14, ImmutableList.of("EC-0009", "EC-LE-0013", "EC-LE-BACK-0015","EC-LE-FRONT-0014", "EC-UE-0010", "EC-UE-LEFT-0011", "EC-UE-RIGHT-0012", 
						"N-0001", "NS-0016", "NS-RELAX-0017", "NS-UPTIGHT-0018", "SS-0019", "SS-BRIGHT-0020", "SS-DULL-0021") });
		data.add(new Object[] { "front_division", "makemore$", 1, ImmutableList.of("EC-LE-FRONT-0014") });
		data.add(new Object[] { "hot_division", "makemore$", 1, ImmutableList.of("WC-NW-HOT-0004") });
		data.add(new Object[] { "le_customer", "makemore$", 3, ImmutableList.of("EC-LE-0013", "EC-LE-BACK-0015", "EC-LE-FRONT-0014") });
		data.add(new Object[] { "left_division", "makemore$", 1, ImmutableList.of("EC-UE-LEFT-0011") });
		data.add(new Object[] { "ns_customer", "makemore$", 3, ImmutableList.of("NS-0016", "NS-RELAX-0017", "NS-UPTIGHT-0018") });
		data.add(new Object[] { "nw_customer", "makemore$", 3, ImmutableList.of("WC-NW-0003", "WC-NW-COLD-0005", "WC-NW-HOT-0004") });
		data.add(new Object[] { "red_division", "makemore$", 1, ImmutableList.of("WC-SW-RED-0007") });
		data.add(new Object[] { "relaxed_divsion", "makemore$", 1, ImmutableList.of("NS-RELAX-0017") });
		data.add(new Object[] { "right_division", "makemore$", 1, ImmutableList.of("EC-UE-RIGHT-0012") });
		data.add(new Object[] { "ss_customer", "makemore$", 3, ImmutableList.of("SS-0019", "SS-BRIGHT-0020", "SS-DULL-0021") });
		data.add(new Object[] { "sw_customer", "makemore$", 3, ImmutableList.of("WC-SW-0006", "WC-SW-BLUE-0008", "WC-SW-RED-0007") });
		data.add(new Object[] { "ue_customer", "makemore$", 3, ImmutableList.of("EC-UE-0010", "EC-UE-LEFT-0011", "EC-UE-RIGHT-0012") });
		data.add(new Object[] { "uptight_div", "makemore$", 1, ImmutableList.of("NS-UPTIGHT-0018") });
		data.add(new Object[] { "wc_secondary", "makemore$", 14, ImmutableList.of("WC-0002", "WC-NW-0003", "WC-NW-COLD-0005", "WC-NW-HOT-0004", "WC-SW-0006", "WC-SW-BLUE-0008", "WC-SW-RED-0007", 
						"N-0001", "NS-0016", "NS-RELAX-0017", "NS-UPTIGHT-0018", "SS-0019", "SS-BRIGHT-0020", "SS-DULL-0021" ) });
		data.add(new Object[] { "bright_division", "makemore$", 1, ImmutableList.of("SS-BRIGHT-0020") });
		data.add(new Object[] { "dull_division", "makemore$", 1, ImmutableList.of("SS-DULL-0021") });
		return data;
	}

	public MinimalSecurityTest(String user, String password, Integer numberOfProductsVisible, Collection<String> serialNumberThatCanBeViewed) {
		this.user = user;
		this.password = password;
		this.numberOfProductsVisible = numberOfProductsVisible;
		this.numberOfInspectionsVisible = numberOfProductsVisible;
		this.numberOfIncompleteSchedulesVisible = numberOfProductsVisible;
		this.numberOfCompleteSchedulesVisible = numberOfProductsVisible;
		
		this.serailNumberThatCanBeViewed = new ArrayList<String>(serialNumberThatCanBeViewed);
		this.serailNumberThatCanNotBeViewed = new ArrayList<String>();
		
		for (String serialNumber : serialNumberList) {
			if (!serialNumberThatCanBeViewed.contains(serialNumber)) {
				this.serailNumberThatCanNotBeViewed.add(serialNumber);
			}
		}
		
		assertThat(numberOfProductsVisible, equalTo(serialNumberThatCanBeViewed.size()));
		assertThat(serialNumberList.size(), equalTo(this.serailNumberThatCanBeViewed.size() + this.serailNumberThatCanNotBeViewed.size()));
	}

	@Before
	public void signIn() throws Exception {
		setCompany("security-check");
		login = new Login(selenium, misc);
		login.signInAllTheWay(user, password);
	}

	@After
	public void signOut() throws Exception {
		login.signOut();
	}

	@Test
	public void should_find_the_correct_number_of_products_visible() throws Exception {
		AssetSearch search = new AssetSearch(selenium, misc);
		AssetSearchResults searchResults = new AssetSearchResults(selenium, misc);
		misc.gotoAssets();
		search.runSearch();
		Assert.assertEquals(numberOfProductsVisible, searchResults.totalResults());
	}

	@Test
	public void should_find_the_correct_number_of_inspections_visible() throws Exception {
		Reporting reporting = new Reporting(selenium, misc);
		misc.gotoReporting();
		reporting.runReport();
		Assert.assertEquals(numberOfInspectionsVisible, reporting.totalResults());
	}

	@Test
	public void should_find_the_correct_number_of_incomplete_schedules_visible() throws Exception {
		Schedules schedules = new Schedules(selenium, misc);
		misc.gotoSchedule();
		schedules.changeScheduleStatus("Incomplete");
		schedules.runSchedules();
		Assert.assertEquals(numberOfIncompleteSchedulesVisible, schedules.totalResults());
	}

	@Test
	public void should_find_the_correct_number_of_complete_schedules_visible() throws Exception {
		Schedules schedules = new Schedules(selenium, misc);
		misc.gotoSchedule();
		schedules.changeScheduleStatus("Complete");
		schedules.runSchedules();
		Assert.assertEquals(numberOfCompleteSchedulesVisible, schedules.totalResults());
	}

	@Test
	public void should_find_the_correct_number_of_schedules_visible() throws Exception {
		Schedules schedules = new Schedules(selenium, misc);
		misc.gotoSchedule();
		schedules.changeScheduleStatus("All");
		schedules.runSchedules();
		Assert.assertEquals((numberOfCompleteSchedulesVisible + numberOfIncompleteSchedulesVisible), schedules.totalResults());
	}
	
	@Test
	public void should_be_able_to_load_each_asset_in_the_visible_list() throws Exception {
		for (String serialNumber : serailNumberThatCanBeViewed) {
			loadAsset(serialNumber);
			assertEquals(serialNumber, selenium.getText("css=.serialNumber"));
		}
	}
	
	@Test
	public void should_not_be_able_to_load_each_asset_in_the_not_visible_list() throws Exception {
		for (String serialNumber : serailNumberThatCanNotBeViewed) {
			loadAsset(serialNumber);
			assertEquals("There are no products with the given serial, RFID or customer reference number.", selenium.getText("css=.emptyList p"));
		}
	}

	private void loadAsset(String serialNumber) {
		misc.gotoHome();
		
		misc.setSmartSearch(serialNumber);
		misc.submitSmartSearch();
	}
}