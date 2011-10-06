package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.FindConnectionResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestFindConnections extends PageNavigatingTestCase<SafetyNetworkPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.primaryOrgFor("test2").setName("The Second Selenium Test Organization");
        scenario.primaryOrgFor("test2").setWebSite("news.bbc.co.uk");
    }

    @Override
    protected SafetyNetworkPage navigateToPage() {
        return start().login().clickSafetyNetworkLink();
    }
    
    @Test
    public void testFindConnections() throws Exception {
        FindConnectionResultsPage resultsPage = page.findConnections("The Second Selenium Test Organization");
        List<Organization> orgs = resultsPage.getOrganizationSearchResults();

        assertEquals(1, orgs.size());

        assertEquals("http://news.bbc.co.uk", orgs.get(0).getCompanyWebsite());
    }

    @Test
    public void testFindConnectionsNoResults() throws Exception {
        FindConnectionResultsPage resultsPage = page.findConnections("This will have no results");
        List<Organization> orgs = resultsPage.getOrganizationSearchResults();
        assertEquals(0, orgs.size());
    }

}
