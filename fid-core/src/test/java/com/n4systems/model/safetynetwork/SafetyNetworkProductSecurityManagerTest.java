package com.n4systems.model.safetynetwork;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;


public class SafetyNetworkProductSecurityManagerTest {
	private PrimaryOrg primary;
	private SecondaryOrg secondary;
	
	@Before
	public void setup_orgs() {
		primary = OrgBuilder.aPrimaryOrg().buildPrimary();
		secondary = OrgBuilder.aSecondaryOrg().buildSecondary();
		
		secondary.setPrimaryOrg(primary);
	}
	
	@Test
	public void is_assigned_returns_false_on_non_linked_owners() {
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(primary);
		assertFalse(productSecurityManager.isAssigned(ProductBuilder.aProduct().build()));
	}
	
	@Test
	public void is_assigned_returns_false_on_linked_owners_for_other_orgs() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(OrgBuilder.aPrimaryOrg().buildPrimary());
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(primary);
		
		assertFalse(productSecurityManager.isAssigned(ProductBuilder.aProduct().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_true_on_orgs_linked_to_my_org() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(primary);
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(primary);
		
		assertTrue(productSecurityManager.isAssigned(ProductBuilder.aProduct().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_true_on_orgs_linked_to_my_sub_orgs() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(secondary);
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(primary);
		
		assertTrue(productSecurityManager.isAssigned(ProductBuilder.aProduct().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_true_on_orgs_linked_to_my_primary() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(primary);
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(secondary);
		
		assertTrue(productSecurityManager.isAssigned(ProductBuilder.aProduct().withOwner(otherOrg).build()));
	}
	
	@Test
	public void list_contains_an_assigned_product_returns_true_with_one_assigned_product() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(primary);
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(secondary);
		
		List<Product> products = Arrays.asList(
				ProductBuilder.aProduct().build(),
				ProductBuilder.aProduct().withOwner(otherOrg).build(),
				ProductBuilder.aProduct().build(),
				ProductBuilder.aProduct().build());
		
		
		assertTrue(productSecurityManager.listContainsAnAssignedProduct(products));
	}
	
	@Test
	public void list_contains_an_assigned_product_returns_true_with_more_than_one_assigned_product() {
		CustomerOrg otherOrg1 = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg1.setLinkedOrg(primary);
		
		CustomerOrg otherOrg2 = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg2.setLinkedOrg(secondary);
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(secondary);
		
		List<Product> products = Arrays.asList(
				ProductBuilder.aProduct().build(),
				ProductBuilder.aProduct().withOwner(otherOrg1).build(),
				ProductBuilder.aProduct().withOwner(otherOrg2).build(),
				ProductBuilder.aProduct().build(),
				ProductBuilder.aProduct().build());
		
		
		assertTrue(productSecurityManager.listContainsAnAssignedProduct(products));
	}
	
	@Test
	public void list_contains_an_assigned_product_returns_false_with_no_assigned_product() {		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(secondary);
		
		List<Product> products = Arrays.asList(
				ProductBuilder.aProduct().build(),
				ProductBuilder.aProduct().build(),
				ProductBuilder.aProduct().build());
		
		
		assertFalse(productSecurityManager.listContainsAnAssignedProduct(products));
	}
	
	@Test
	public void filter_list_test() {		
		CustomerOrg linkedOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		linkedOrg.setLinkedOrg(primary);
		
		SafetyNetworkProductSecurityManager productSecurityManager = new SafetyNetworkProductSecurityManager(secondary);
		
		List<Product> products = Arrays.asList(
				ProductBuilder.aProduct().withOwner(OrgBuilder.aCustomerOrg().build()).build(),
				ProductBuilder.aProduct().withOwner(secondary).build(),
				ProductBuilder.aProduct().withOwner(linkedOrg).build(),
				ProductBuilder.aProduct().withOwner(OrgBuilder.aCustomerOrg().build()).build(),
				ProductBuilder.aProduct().withOwner(OrgBuilder.aDivisionOrg().build()).build());
		
		List<Product> filtered = productSecurityManager.filterOutExternalNotAssignedProducts(products);
		assertEquals(2, filtered.size());
		
		assertEquals(secondary, filtered.get(0).getOwner());
		assertEquals(linkedOrg, filtered.get(1).getOwner());
	}
}
