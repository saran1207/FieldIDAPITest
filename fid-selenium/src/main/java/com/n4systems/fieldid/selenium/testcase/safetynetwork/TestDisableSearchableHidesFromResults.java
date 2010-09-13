package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.FindConnectionResultsPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkSettingsPage;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDisableSearchableHidesFromResults extends PageNavigatingTestCase<SafetyNetworkPage> {

    @Override
    protected SafetyNetworkPage navigateToPage() {
        return startAsCompany("aacm").login().clickSafetyNetworkLink();
    }

    @Test
    public void should_find_company_when_searchable_is_on() throws Exception {
        ensureVisibilityIsSetTo(true, page);

        FindConnectionResultsPage resultsPage = page.findConnections("all american");

        List<Organization> matchingOrgs = resultsPage.getOrganizationSearchResults();
        assertEquals(1, matchingOrgs.size());
        assertEquals("All American Crane Maintenance", matchingOrgs.get(0).getName());
    }

    @Test
    public void should_not_find_company_when_searchable_is_off() throws Exception {
        ensureVisibilityIsSetTo(false, page);

        FindConnectionResultsPage resultsPage = page.findConnections("all american");

        List<Organization> matchingOrgs = resultsPage.getOrganizationSearchResults();
        assertEquals(0, matchingOrgs.size());
    }

    public void ensureVisibilityIsSetTo(boolean visible, SafetyNetworkPage page) {
        SafetyNetworkSettingsPage settingsPage = page.clickSettings();
        if (settingsPage.isVisibilityChecked() != visible) {
            if (visible) {
                settingsPage.checkVisibilityBox();
            } else {
                settingsPage.unCheckVisibilityBox();
            }
            settingsPage.saveSettings();
        }
        page.clickSafetyNetworkLink();
    }

}
