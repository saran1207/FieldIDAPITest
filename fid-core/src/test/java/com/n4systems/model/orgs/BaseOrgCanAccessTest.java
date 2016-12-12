package com.n4systems.model.orgs;

import com.n4systems.model.builders.OrgBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
	private CustomerOrg customer_of_s1_1;
	private CustomerOrg customer_of_s1_2;
	private CustomerOrg customer_of_s2_1;
	private CustomerOrg customer_of_s2_2;
	private DivisionOrg division_of_c2;
	private DivisionOrg division_of_customer_of_s1_2;
	private DivisionOrg division_of_customer_of_s2_2;
	
	@Before
	public void setup_org_tree() {
		p1 = OrgBuilder.aPrimaryOrg().buildPrimary();
		s1 = OrgBuilder.aSecondaryOrg().buildSecondary();
		s2 = OrgBuilder.aSecondaryOrg().buildSecondary();
		c1 = OrgBuilder.aCustomerOrg().buildCustomer();
		c2 = OrgBuilder.aCustomerOrg().buildCustomer();
		customer_of_s1_1 = OrgBuilder.aCustomerOrg().buildCustomer();
		customer_of_s1_2 = OrgBuilder.aCustomerOrg().buildCustomer();
		customer_of_s2_1 = OrgBuilder.aCustomerOrg().buildCustomer();
		customer_of_s2_2 = OrgBuilder.aCustomerOrg().buildCustomer();
		division_of_c2 = OrgBuilder.aDivisionOrg().buildDivision();
		division_of_customer_of_s1_2 = OrgBuilder.aDivisionOrg().buildDivision();
		division_of_customer_of_s2_2 = OrgBuilder.aDivisionOrg().buildDivision();
		
		s1.setPrimaryOrg(p1);
		s2.setPrimaryOrg(p1);
		c1.setParent(p1);
		c2.setParent(p1);
		customer_of_s1_1.setParent(s1);
		customer_of_s1_2.setParent(s1);
		customer_of_s2_1.setParent(s2);
		customer_of_s2_2.setParent(s2);
		division_of_c2.setParent(c2);
		division_of_customer_of_s1_2.setParent(customer_of_s1_2);
		division_of_customer_of_s2_2.setParent(customer_of_s2_2);
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
		assertCanAccess(p1, customer_of_s1_1, true);
		assertCanAccess(p1, customer_of_s1_2, true);
		assertCanAccess(p1, customer_of_s2_1, true);
		assertCanAccess(p1, customer_of_s2_2, true);
		assertCanAccess(p1, division_of_c2, true);
		assertCanAccess(p1, division_of_customer_of_s1_2, true);
		assertCanAccess(p1, division_of_customer_of_s2_2, true);
	}

	@Test
	public void myorg_is_s1() {
		assertCanAccess(s1, p1, true);
		assertCanAccess(s1, s1, true);
		assertCanAccess(s1, s2, false);
		assertCanAccess(s1, c1, false);
		assertCanAccess(s1, c2, false);
		assertCanAccess(s1, customer_of_s1_1, true);
		assertCanAccess(s1, customer_of_s1_2, true);
		assertCanAccess(s1, customer_of_s2_1, false);
		assertCanAccess(s1, customer_of_s2_2, false);
		assertCanAccess(s1, division_of_c2, false);
		assertCanAccess(s1, division_of_customer_of_s1_2, true);
		assertCanAccess(s1, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_s2() {
		assertCanAccess(s2, p1, true);
		assertCanAccess(s2, s1, false);
		assertCanAccess(s2, s2, true);
		assertCanAccess(s2, c1, false);
		assertCanAccess(s2, c2, false);
		assertCanAccess(s2, customer_of_s1_1, false);
		assertCanAccess(s2, customer_of_s1_2, false);
		assertCanAccess(s2, customer_of_s2_1, true);
		assertCanAccess(s2, customer_of_s2_2, true);
		assertCanAccess(s2, division_of_c2, false);
		assertCanAccess(s2, division_of_customer_of_s1_2, false);
		assertCanAccess(s2, division_of_customer_of_s2_2, true);
	}
	
	@Test
	public void myorg_is_c1() {
		assertCanAccess(c1, p1, false);
		assertCanAccess(c1, s1, false);
		assertCanAccess(c1, s2, false);
		assertCanAccess(c1, c1, true);
		assertCanAccess(c1, c2, false);
		assertCanAccess(c1, customer_of_s1_1, false);
		assertCanAccess(c1, customer_of_s1_2, false);
		assertCanAccess(c1, customer_of_s2_1, false);
		assertCanAccess(c1, customer_of_s2_2, false);
		assertCanAccess(c1, division_of_c2, false);
		assertCanAccess(c1, division_of_customer_of_s1_2, false);
		assertCanAccess(c1, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_c2() {
		assertCanAccess(c2, p1, false);
		assertCanAccess(c2, s1, false);
		assertCanAccess(c2, s2, false);
		assertCanAccess(c2, c1, false);
		assertCanAccess(c2, c2, true);
		assertCanAccess(c2, customer_of_s1_1, false);
		assertCanAccess(c2, customer_of_s1_2, false);
		assertCanAccess(c2, customer_of_s2_1, false);
		assertCanAccess(c2, customer_of_s2_2, false);
		assertCanAccess(c2, division_of_c2, true);
		assertCanAccess(c2, division_of_customer_of_s1_2, false);
		assertCanAccess(c2, division_of_customer_of_s2_2, false);
	}

	@Test
	public void myorg_is_c3() {
		assertCanAccess(customer_of_s1_1, p1, false);
		assertCanAccess(customer_of_s1_1, s1, false);
		assertCanAccess(customer_of_s1_1, s2, false);
		assertCanAccess(customer_of_s1_1, c1, false);
		assertCanAccess(customer_of_s1_1, c2, false);
		assertCanAccess(customer_of_s1_1, customer_of_s1_1, true);
		assertCanAccess(customer_of_s1_1, customer_of_s1_2, false);
		assertCanAccess(customer_of_s1_1, customer_of_s2_1, false);
		assertCanAccess(customer_of_s1_1, customer_of_s2_2, false);
		assertCanAccess(customer_of_s1_1, division_of_c2, false);
		assertCanAccess(customer_of_s1_1, division_of_customer_of_s1_2, false);
		assertCanAccess(customer_of_s1_1, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_c4() {
		assertCanAccess(customer_of_s1_2, p1, false);
		assertCanAccess(customer_of_s1_2, s1, false);
		assertCanAccess(customer_of_s1_2, s2, false);
		assertCanAccess(customer_of_s1_2, c1, false);
		assertCanAccess(customer_of_s1_2, c2, false);
		assertCanAccess(customer_of_s1_2, customer_of_s1_1, false);
		assertCanAccess(customer_of_s1_2, customer_of_s1_2, true);
		assertCanAccess(customer_of_s1_2, customer_of_s2_1, false);
		assertCanAccess(customer_of_s1_2, customer_of_s2_2, false);
		assertCanAccess(customer_of_s1_2, division_of_c2, false);
		assertCanAccess(customer_of_s1_2, division_of_customer_of_s1_2, true);
		assertCanAccess(customer_of_s1_2, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_c5() {
		assertCanAccess(customer_of_s2_1, p1, false);
		assertCanAccess(customer_of_s2_1, s1, false);
		assertCanAccess(customer_of_s2_1, s2, false);
		assertCanAccess(customer_of_s2_1, c1, false);
		assertCanAccess(customer_of_s2_1, c2, false);
		assertCanAccess(customer_of_s2_1, customer_of_s1_1, false);
		assertCanAccess(customer_of_s2_1, customer_of_s1_2, false);
		assertCanAccess(customer_of_s2_1, customer_of_s2_1, true);
		assertCanAccess(customer_of_s2_1, customer_of_s2_2, false);
		assertCanAccess(customer_of_s2_1, division_of_c2, false);
		assertCanAccess(customer_of_s2_1, division_of_customer_of_s1_2, false);
		assertCanAccess(customer_of_s2_1, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_c6() {
		assertCanAccess(customer_of_s2_2, p1, false);
		assertCanAccess(customer_of_s2_2, s1, false);
		assertCanAccess(customer_of_s2_2, s2, false);
		assertCanAccess(customer_of_s2_2, c1, false);
		assertCanAccess(customer_of_s2_2, c2, false);
		assertCanAccess(customer_of_s2_2, customer_of_s1_1, false);
		assertCanAccess(customer_of_s2_2, customer_of_s1_2, false);
		assertCanAccess(customer_of_s2_2, customer_of_s2_1, false);
		assertCanAccess(customer_of_s2_2, customer_of_s2_2, true);
		assertCanAccess(customer_of_s2_2, division_of_c2, false);
		assertCanAccess(customer_of_s2_2, division_of_customer_of_s1_2, false);
		assertCanAccess(customer_of_s2_2, division_of_customer_of_s2_2, true);
	}
	
	@Test
	public void myorg_is_d1() {
		assertCanAccess(division_of_c2, p1, false);
		assertCanAccess(division_of_c2, s1, false);
		assertCanAccess(division_of_c2, s2, false);
		assertCanAccess(division_of_c2, c1, false);
		assertCanAccess(division_of_c2, c2, false);
		assertCanAccess(division_of_c2, customer_of_s1_1, false);
		assertCanAccess(division_of_c2, customer_of_s1_2, false);
		assertCanAccess(division_of_c2, customer_of_s2_1, false);
		assertCanAccess(division_of_c2, customer_of_s2_2, false);
		assertCanAccess(division_of_c2, division_of_c2, true);
		assertCanAccess(division_of_c2, division_of_customer_of_s1_2, false);
		assertCanAccess(division_of_c2, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_d2() {
		assertCanAccess(division_of_customer_of_s1_2, p1, false);
		assertCanAccess(division_of_customer_of_s1_2, s1, false);
		assertCanAccess(division_of_customer_of_s1_2, s2, false);
		assertCanAccess(division_of_customer_of_s1_2, c1, false);
		assertCanAccess(division_of_customer_of_s1_2, c2, false);
		assertCanAccess(division_of_customer_of_s1_2, customer_of_s1_1, false);
		assertCanAccess(division_of_customer_of_s1_2, customer_of_s1_2, false);
		assertCanAccess(division_of_customer_of_s1_2, customer_of_s2_1, false);
		assertCanAccess(division_of_customer_of_s1_2, customer_of_s2_2, false);
		assertCanAccess(division_of_customer_of_s1_2, division_of_c2, false);
		assertCanAccess(division_of_customer_of_s1_2, division_of_customer_of_s1_2, true);
		assertCanAccess(division_of_customer_of_s1_2, division_of_customer_of_s2_2, false);
	}
	
	@Test
	public void myorg_is_d3() {
		assertCanAccess(division_of_customer_of_s2_2, p1, false);
		assertCanAccess(division_of_customer_of_s2_2, s1, false);
		assertCanAccess(division_of_customer_of_s2_2, s2, false);
		assertCanAccess(division_of_customer_of_s2_2, c1, false);
		assertCanAccess(division_of_customer_of_s2_2, c2, false);
		assertCanAccess(division_of_customer_of_s2_2, customer_of_s1_1, false);
		assertCanAccess(division_of_customer_of_s2_2, customer_of_s1_2, false);
		assertCanAccess(division_of_customer_of_s2_2, customer_of_s2_1, false);
		assertCanAccess(division_of_customer_of_s2_2, customer_of_s2_2, false);
		assertCanAccess(division_of_customer_of_s2_2, division_of_c2, false);
		assertCanAccess(division_of_customer_of_s2_2, division_of_customer_of_s1_2, false);
		assertCanAccess(division_of_customer_of_s2_2, division_of_customer_of_s2_2, true);
	}

}
