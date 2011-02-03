package com.n4systems.fieldid.selenium.testcase.reporting;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FilterByResultTest extends PageNavigatingTestCase<HomePage> {

	private String resultName;

	public FilterByResultTest(String resultName) {
		this.resultName = resultName;
	}

    @Override
    protected HomePage navigateToPage() {
        return start().login();
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
	public void show_just_events_that_passed_when_pass_selected_in_the_result_filter() throws Exception {
        page.clickReportingLink();

		selenium.select("css=#reportForm_criteria_status", resultName);
		
		submitForm();

		verifyEventResultsAreCorrectOnThisPage();
		goToLastPage();
		verifyEventResultsAreCorrectOnThisPage();
	}

	private void submitForm() {
		selenium.click("css=#reportForm_label_Run");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
	}

	private void verifyEventResultsAreCorrectOnThisPage() {
		assertEquals(resultName, selenium.getText("css=#event_search_eventresult_0"));
		
		String pageNumber = selenium.getText("css=.currentPage span");
		String totalResults = selenium.getText("css=.total:contains('Total Events')");
		
		int page = Integer.parseInt(pageNumber.trim());
		int total = Integer.parseInt(totalResults.replaceFirst("Total.*Events", "").trim());
		
		int lastResultOnPage = total - (20 * (page - 1));
		
		assertEquals(resultName, selenium.getText("css=#event_search_eventresult_" + ((lastResultOnPage > 20) ? 19 : lastResultOnPage - 1)  ));
	}

	private void goToLastPage() {
		selenium.click("link=Last");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
	}

}
