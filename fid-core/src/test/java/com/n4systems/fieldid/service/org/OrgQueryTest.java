package com.n4systems.fieldid.service.org;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import junit.framework.TestCase;
import org.junit.Test;

public class OrgQueryTest extends TestCase {
    
    @Test
    public void test_HappyPathInputs() {
        OrgQuery orgQuery = new OrgQuery("hello");
        assertEquals(orgQuery.getSearchClass(), BaseOrg.class);
        System.out.println(orgQuery.getWhere());

        orgQuery = new OrgQuery("hello:");
        assertEquals(orgQuery.getPrimaryTerm(), "hello");
        assertEquals(CustomerOrg.class, orgQuery.getSearchClass() );
        System.out.println(orgQuery.getWhere());

        orgQuery = new OrgQuery("hello:world:");
        assertEquals(orgQuery.getPrimaryTerm(), "hello");
        assertEquals(orgQuery.getCustomerTerm(), "world");
        assertEquals(orgQuery.getTrailingTerm(),"");
        assertEquals(DivisionOrg.class, orgQuery.getSearchClass());
        System.out.println(orgQuery.getWhere());

        orgQuery = new OrgQuery("::division");
        assertEquals(orgQuery.getPrimaryTerm(), "");
        assertEquals(orgQuery.getCustomerTerm(), "");
        assertEquals(orgQuery.getTrailingTerm(),"division");
        assertEquals(DivisionOrg.class, orgQuery.getSearchClass());
        System.out.println(orgQuery.getWhere());

        orgQuery = new OrgQuery(":foo:");
        assertEquals(orgQuery.getPrimaryTerm(), "");
        assertEquals(orgQuery.getCustomerTerm(), "foo");
        assertEquals(orgQuery.getTrailingTerm(),"");
        assertEquals(DivisionOrg.class, orgQuery.getSearchClass());
        System.out.println(orgQuery.getWhere());

    }

}
