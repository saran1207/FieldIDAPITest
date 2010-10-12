package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.misc.DateUtil;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.schedules.SchedulesSearchResultsPage;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MassUpdateSchedulesTest extends PageNavigatingTestCase<SchedulesSearchPage> {

    @Override
    protected SchedulesSearchPage navigateToPage() {
        return startAsCompany("halo").login().clickSchedulesLink();
    }

    @Test
    public void test_mass_update_schedules_next_date() throws Exception {
        page.enterSerialNumber("2010-000041");
        SchedulesSearchResultsPage resultsPage = page.clickRunSearchButton();

        assertEquals(2, resultsPage.getTotalResultsCount());
        Date beginningDate = DateUtil.parseDate(resultsPage.getScheduledDateForResult(1));
        SchedulesMassUpdatePage scheduleMassUpdatePage = resultsPage.clickMassUpdate();
        String nextDate = DateUtil.formatDate(DateUtil.theDayAfter(beginningDate));

        scheduleMassUpdatePage.enterNextInspectionDate(nextDate);
        resultsPage = scheduleMassUpdatePage.clickSaveButtonAndConfirm();

        assertEquals(nextDate, resultsPage.getScheduledDateForResult(1));
        assertEquals(nextDate, resultsPage.getScheduledDateForResult(2));
    }

}
