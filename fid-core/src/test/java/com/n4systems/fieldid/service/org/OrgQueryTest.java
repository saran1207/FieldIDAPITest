package com.n4systems.fieldid.service.org;

import junit.framework.TestCase;
import org.junit.Test;

public class OrgQueryTest extends TestCase {
    
    @Test
    public void test_HappyPathInputs() {
        OrgQueryParser orgQuery = new OrgQueryParser("hello");
        assertEquals("hello", orgQuery.getSearchTerm());

        orgQuery = new OrgQueryParser("hello:");
        assertEquals(1, orgQuery.getParentTerms().size());
        assertEquals("hello", orgQuery.getParentTerms().get(0));
        assertEquals("", orgQuery.getSearchTerm());

        orgQuery = new OrgQueryParser("hello:derek");
        assertEquals(1, orgQuery.getParentTerms().size());
        assertEquals("hello", orgQuery.getParentTerms().get(0));
        assertEquals("derek", orgQuery.getSearchTerm());

        orgQuery = new OrgQueryParser("hello:world:");
        assertEquals(2, orgQuery.getParentTerms().size());
        assertEquals("hello", orgQuery.getParentTerms().get(0));
        assertEquals("world", orgQuery.getParentTerms().get(1));
        assertEquals("", orgQuery.getSearchTerm());

        orgQuery = new OrgQueryParser("x:y:foobar");
        assertEquals(2, orgQuery.getParentTerms().size());
        assertEquals("x", orgQuery.getParentTerms().get(0));
        assertEquals("y", orgQuery.getParentTerms().get(1));
        assertEquals("foobar", orgQuery.getSearchTerm());

    }

}
