package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.datatypes.Organization;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FindConnectionResultsPage extends FieldIDPage {

    public FindConnectionResultsPage(Selenium selenium) {
        super(selenium);
    }

    public List<Organization> getOrganizationSearchResults() {
        List<Organization> organizations = new ArrayList<Organization>();

        for (int i = 1; i <= getNumberOfPages(); i++) {
            gotoPage(i);

            List<String> orgNames = collectTableValuesUnderCellForCurrentPage(1, 2, "span");
            Iterator<String> websiteHrefs = collectTableAttributesUnderCellForCurrentPage(1,2,"a/@href").iterator();

            for (String orgName : orgNames) {
                Organization org = new Organization();
                org.setName(orgName);
                org.setCompanyWebsite(websiteHrefs.next());
                organizations.add(org);
            }
        }

        return organizations;
    }

}
