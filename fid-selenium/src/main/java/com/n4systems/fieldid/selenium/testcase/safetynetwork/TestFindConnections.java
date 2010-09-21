package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.FindConnectionResultsPage;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestFindConnections extends PageNavigatingTestCase<SafetyNetworkPage> {

    @Override
    protected SafetyNetworkPage navigateToPage() {
        return startAsCompany("n4").login().clickSafetyNetworkLink();
    }
    
    @Test
    public void testFindConnections() throws Exception {
        FindConnectionResultsPage resultsPage = page.findConnections("crane ma");
        List<Organization> orgs = resultsPage.getOrganizationSearchResults();

        assertEquals(2, orgs.size());

        Organization aacm = findOrgNamed("All American Crane Maintenance", orgs);
        assertEquals("http://www.aacm.net/", aacm.getCompanyWebsite());

        Organization craneMasters = findOrgNamed("Crane Masters", orgs);
        assertNull(craneMasters.getCompanyWebsite());
    }

    @Test
    public void testFindConnectionsNoResults() throws Exception {
        FindConnectionResultsPage resultsPage = page.findConnections("This will have no results");
        List<Organization> orgs = resultsPage.getOrganizationSearchResults();
        assertEquals(0, orgs.size());
    }

    @Test
    public void testFindConnectionsMoreThanOnePage() throws Exception {
        FindConnectionResultsPage resultsPage = page.findConnections("inc");

        assertEquals(2, resultsPage.getNumberOfPages());

        List<Organization> orgs = resultsPage.getOrganizationSearchResults();

        assertEquals(18, orgs.size());

        assertNotNull(findOrgNamed("Key Constructors Inc.", orgs));
        assertNotNull(findOrgNamed("Peintre inc", orgs));
    }

    @Test
    public void testFindConnectionsNoHttpInWebsiteAddress() throws Exception {
        FindConnectionResultsPage resultsPage = page.findConnections("West Coast Wire Rope");
        List<Organization> orgs = resultsPage.getOrganizationSearchResults();

        assertEquals(1, orgs.size());
        Organization wcwr = orgs.iterator().next();

        assertEquals("http://www.wcwr.com", wcwr.getCompanyWebsite());
        assertEquals("West Coast Wire Rope", wcwr.getName());
    }


    public Organization findOrgNamed(String name, List<Organization> orgList) {
        for (Organization org : orgList) {
            if (name.equals(org.getName())) {
                return org;
            }
        }
        return null;
    }

}
