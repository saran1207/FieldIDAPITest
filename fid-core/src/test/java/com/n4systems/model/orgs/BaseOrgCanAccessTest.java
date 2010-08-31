package com.n4systems.model.orgs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;

/**
 * Tests the canAccess rules for BaseOrg.
 * <p/>
 * See <a href='http://fieldid.jira.com/wiki/x/GwAz'>http://fieldid.jira.com/wiki/x/GwAz</a> for org structure and access table
 */
public class BaseOrgCanAccessTest {
	private PrimaryOrg p1;
	private SecondaryOrg s1;
	private SecondaryOrg s2;
	private CustomerOrg c1;
	private CustomerOrg c2;
	private CustomerOrg c3;
	private CustomerOrg c4;
	private CustomerOrg c5;
	private CustomerOrg c6;
	private DivisionOrg d1;
	private DivisionOrg d2;
	private DivisionOrg d3;
	
	@Before
	public void setup_org_tree() {
		p1 = OrgBuilder.aPrimaryOrg().buildPrimary();
		s1 = OrgBuilder.aSecondaryOrg().buildSecondary();
		s2 = OrgBuilder.aSecondaryOrg().buildSecondary();
		c1 = OrgBuilder.aCustomerOrg().buildCustomer();
		c2 = OrgBuilder.aCustomerOrg().buildCustomer();
		c3 = OrgBuilder.aCustomerOrg().buildCustomer();
		c4 = OrgBuilder.aCustomerOrg().buildCustomer();
		c5 = OrgBuilder.aCustomerOrg().buildCustomer();
		c6 = OrgBuilder.aCustomerOrg().buildCustomer();
		d1 = OrgBuilder.aDivisionOrg().buildDivision();
		d2 = OrgBuilder.aDivisionOrg().buildDivision();
		d3 = OrgBuilder.aDivisionOrg().buildDivision();
		
		s1.setPrimaryOrg(p1);
		s2.setPrimaryOrg(p1);
		c1.setParent(p1);
		c2.setParent(p1);
		c3.setParent(s1);
		c4.setParent(s1);
		c5.setParent(s2);
		c6.setParent(s2);
		d1.setParent(c2);
		d2.setParent(c4);
		d3.setParent(c6);
	}
	
	private void assertCanAccess(BaseOrg myOrg, BaseOrg otherOrg, boolean shouldPass) {
		assertTrue(myOrg.canAccess(otherOrg) == shouldPass);
	}
	
	@Test
	public void myorg_is_p1() {
		assertCanAccess(p1, p1, true);
		assertCanAccess(p1, s1, true);
		assertCanAccess(p1, s2, true);
		assertCanAccess(p1, c1, true);
		assertCanAccess(p1, c2, true);
		assertCanAccess(p1, c3, true);
		assertCanAccess(p1, c4, true);
		assertCanAccess(p1, c5, true);
		assertCanAccess(p1, c6, true);
		assertCanAccess(p1, d1, true);
		assertCanAccess(p1, d2, true);
		assertCanAccess(p1, d3, true);
	}

	@Test
	public void myorg_is_s1() {
		assertCanAccess(s1, p1, true);
		assertCanAccess(s1, s1, true);
		assertCanAccess(s1, s2, false);
		assertCanAccess(s1, c1, false);
		assertCanAccess(s1, c2, false);
		assertCanAccess(s1, c3, true);
		assertCanAccess(s1, c4, true);
		assertCanAccess(s1, c5, false);
		assertCanAccess(s1, c6, false);
		assertCanAccess(s1, d1, false);
		assertCanAccess(s1, d2, true);
		assertCanAccess(s1, d3, false);
	}
	
	@Test
	public void myorg_is_s2() {
		assertCanAccess(s2, p1, true);
		assertCanAccess(s2, s1, false);
		assertCanAccess(s2, s2, true);
		assertCanAccess(s2, c1, false);
		assertCanAccess(s2, c2, false);
		assertCanAccess(s2, c3, false);
		assertCanAccess(s2, c4, false);
		assertCanAccess(s2, c5, true);
		assertCanAccess(s2, c6, true);
		assertCanAccess(s2, d1, false);
		assertCanAccess(s2, d2, false);
		assertCanAccess(s2, d3, true);
	}
	
	@Test
	public void myorg_is_c1() {
		assertCanAccess(c1, p1, false);
		assertCanAccess(c1, s1, false);
		assertCanAccess(c1, s2, false);
		assertCanAccess(c1, c1, true);
		assertCanAccess(c1, c2, false);
		assertCanAccess(c1, c3, false);
		assertCanAccess(c1, c4, false);
		assertCanAccess(c1, c5, false);
		assertCanAccess(c1, c6, false);
		assertCanAccess(c1, d1, false);
		assertCanAccess(c1, d2, false);
		assertCanAccess(c1, d3, false);
	}
	
	@Test
	public void myorg_is_c2() {
		assertCanAccess(c2, p1, false);
		assertCanAccess(c2, s1, false);
		assertCanAccess(c2, s2, false);
		assertCanAccess(c2, c1, false);
		assertCanAccess(c2, c2, true);
		assertCanAccess(c2, c3, false);
		assertCanAccess(c2, c4, false);
		assertCanAccess(c2, c5, false);
		assertCanAccess(c2, c6, false);
		assertCanAccess(c2, d1, true);
		assertCanAccess(c2, d2, false);
		assertCanAccess(c2, d3, false);
	}

	@Test
	public void myorg_is_c3() {
		assertCanAccess(c3, p1, false);
		assertCanAccess(c3, s1, false);
		assertCanAccess(c3, s2, false);
		assertCanAccess(c3, c1, false);
		assertCanAccess(c3, c2, false);
		assertCanAccess(c3, c3, true);
		assertCanAccess(c3, c4, false);
		assertCanAccess(c3, c5, false);
		assertCanAccess(c3, c6, false);
		assertCanAccess(c3, d1, false);
		assertCanAccess(c3, d2, false);
		assertCanAccess(c3, d3, false);
	}
	
	@Test
	public void myorg_is_c4() {
		assertCanAccess(c4, p1, false);
		assertCanAccess(c4, s1, false);
		assertCanAccess(c4, s2, false);
		assertCanAccess(c4, c1, false);
		assertCanAccess(c4, c2, false);
		assertCanAccess(c4, c3, false);
		assertCanAccess(c4, c4, true);
		assertCanAccess(c4, c5, false);
		assertCanAccess(c4, c6, false);
		assertCanAccess(c4, d1, false);
		assertCanAccess(c4, d2, true);
		assertCanAccess(c4, d3, false);
	}
	
	@Test
	public void myorg_is_c5() {
		assertCanAccess(c5, p1, false);
		assertCanAccess(c5, s1, false);
		assertCanAccess(c5, s2, false);
		assertCanAccess(c5, c1, false);
		assertCanAccess(c5, c2, false);
		assertCanAccess(c5, c3, false);
		assertCanAccess(c5, c4, false);
		assertCanAccess(c5, c5, true);
		assertCanAccess(c5, c6, false);
		assertCanAccess(c5, d1, false);
		assertCanAccess(c5, d2, false);
		assertCanAccess(c5, d3, false);
	}
	
	@Test
	public void myorg_is_c6() {
		assertCanAccess(c6, p1, false);
		assertCanAccess(c6, s1, false);
		assertCanAccess(c6, s2, false);
		assertCanAccess(c6, c1, false);
		assertCanAccess(c6, c2, false);
		assertCanAccess(c6, c3, false);
		assertCanAccess(c6, c4, false);
		assertCanAccess(c6, c5, false);
		assertCanAccess(c6, c6, true);
		assertCanAccess(c6, d1, false);
		assertCanAccess(c6, d2, false);
		assertCanAccess(c6, d3, true);
	}
	
	@Test
	public void myorg_is_d1() {
		assertCanAccess(d1, p1, false);
		assertCanAccess(d1, s1, false);
		assertCanAccess(d1, s2, false);
		assertCanAccess(d1, c1, false);
		assertCanAccess(d1, c2, false);
		assertCanAccess(d1, c3, false);
		assertCanAccess(d1, c4, false);
		assertCanAccess(d1, c5, false);
		assertCanAccess(d1, c6, false);
		assertCanAccess(d1, d1, true);
		assertCanAccess(d1, d2, false);
		assertCanAccess(d1, d3, false);
	}
	
	@Test
	public void myorg_is_d2() {
		assertCanAccess(d2, p1, false);
		assertCanAccess(d2, s1, false);
		assertCanAccess(d2, s2, false);
		assertCanAccess(d2, c1, false);
		assertCanAccess(d2, c2, false);
		assertCanAccess(d2, c3, false);
		assertCanAccess(d2, c4, false);
		assertCanAccess(d2, c5, false);
		assertCanAccess(d2, c6, false);
		assertCanAccess(d2, d1, false);
		assertCanAccess(d2, d2, true);
		assertCanAccess(d2, d3, false);
	}
	
	@Test
	public void myorg_is_d3() {
		assertCanAccess(d3, p1, false);
		assertCanAccess(d3, s1, false);
		assertCanAccess(d3, s2, false);
		assertCanAccess(d3, c1, false);
		assertCanAccess(d3, c2, false);
		assertCanAccess(d3, c3, false);
		assertCanAccess(d3, c4, false);
		assertCanAccess(d3, c5, false);
		assertCanAccess(d3, c6, false);
		assertCanAccess(d3, d1, false);
		assertCanAccess(d3, d2, false);
		assertCanAccess(d3, d3, true);
	}


}
