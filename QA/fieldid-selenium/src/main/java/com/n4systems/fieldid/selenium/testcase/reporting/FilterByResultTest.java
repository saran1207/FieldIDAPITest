package com.n4systems.fieldid.selenium.testcase.reporting;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

@RunWith(Parameterized.class)
public class FilterByResultTest extends LoggedInTestCase {

	private String resultName;

	public FilterByResultTest(String resultName) {
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
		goToReporting();
		
		selenium.select("css=#reportForm_criteria_status", resultName);
		
		submitForm();

		verifyInspectionResultsAreCorrectOnThisPage();
		goToLastPage();
		verifyInspectionResultsAreCorrectOnThisPage();
	}

	private void goToReporting() {
		selenium.open("/fieldid/report.action");
	}

	private void submitForm() {
		selenium.click("css=#reportForm_label_Run");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
	}

	private void verifyInspectionResultsAreCorrectOnThisPage() {
		verifyEquals(resultName, selenium.getText("css=#inspection_search_inspectionresult_0"));
		
		String pageNumber = selenium.getText("css=.currentPage span");
		String totalResults = selenium.getText("css=.total:contains('Total Inspections')");
		
		int page = Integer.parseInt(pageNumber.trim());
		int total = Integer.parseInt(totalResults.replaceFirst("Total.*Inspections", "").trim());
		
		int lastResultOnPage = total - (20 * (page - 1));
		
		verifyEquals(resultName, selenium.getText("css=#inspection_search_inspectionresult_" + ((lastResultOnPage > 20) ? 19 : lastResultOnPage - 1)  ));
	}

	private void goToLastPage() {
		selenium.click("link=Last");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
	}

}
