package com.n4systems.fieldid.selenium.testcase.reporting;


import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.n4systems.fieldid.selenium.testcase.LoggedInTest;


@RunWith(value = Parameterized.class)
public class FilterByResultTest extends LoggedInTest {

	private String resultName;


	public FilterByResultTest(String resultName) {
		super("n4systems", SYSTEM_USER_PASSWORD);
		this.resultName = resultName;
	}
	
	@Parameters
	public static Collection<String[]> data() {
		ArrayList<String[]> dataList = new ArrayList<String[]>();
		dataList.add(new String[]{"Pass"});
		dataList.add(new String[]{"N/A"});
		dataList.add(new String[]{"Fail"});
		return dataList;
	}
	
	@Test
	public void show_just_inspections_that_passed_when_pass_selected_in_the_result_filter() throws Exception {
		selenium.open("/fieldid/report.action");
		
		
		selenium.select("css=#reportForm_criteria_status", resultName);
		selenium.click("css=#reportForm_label_Run");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		
		verifyFalse(selenium.isElementPresent("css[id^='#inspection_search_inspectionresult_']:not(:contains('" +  resultName + "'))"));
		
		selenium.click("link=Last");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		verifyFalse(selenium.isElementPresent("css[id^='#inspection_search_inspectionresult_']:not(:contains('" +  resultName + "'))"));
	}

}
