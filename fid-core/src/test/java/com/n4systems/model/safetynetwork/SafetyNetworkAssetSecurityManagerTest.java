package com.n4systems.model.safetynetwork;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.orgs.BaseOrg;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;


public class SafetyNetworkAssetSecurityManagerTest {
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
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(primary);
        BaseOrg otherOrg = OrgBuilder.aPrimaryOrg().build();
        assertFalse(assetSecurityManager.isAssigned(AssetBuilder.anAsset().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_false_on_linked_owners_for_other_orgs() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(OrgBuilder.aPrimaryOrg().buildPrimary());
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(primary);
		
		assertFalse(assetSecurityManager.isAssigned(AssetBuilder.anAsset().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_true_on_orgs_linked_to_my_org() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(primary);
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(primary);
		
		assertTrue(assetSecurityManager.isAssigned(AssetBuilder.anAsset().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_true_on_orgs_linked_to_my_sub_orgs() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(secondary);
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(primary);
		
		assertTrue(assetSecurityManager.isAssigned(AssetBuilder.anAsset().withOwner(otherOrg).build()));
	}
	
	@Test
	public void is_assigned_returns_true_on_orgs_linked_to_my_primary() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(primary);
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(secondary);
		
		assertTrue(assetSecurityManager.isAssigned(AssetBuilder.anAsset().withOwner(otherOrg).build()));
	}
	
	@Test
	public void list_contains_an_assigned_asset_returns_true_with_one_assigned_asset() {
		CustomerOrg otherOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg.setLinkedOrg(primary);
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(secondary);
		
		List<Asset> assets = Arrays.asList(
				AssetBuilder.anAsset().withOwner(primary).build(),
				AssetBuilder.anAsset().withOwner(otherOrg).build(),
				AssetBuilder.anAsset().withOwner(primary).build(),
				AssetBuilder.anAsset().withOwner(primary).build());
		
		
		assertTrue(assetSecurityManager.listContainsAnAssignedAsset(assets));
	}
	
	@Test
	public void list_contains_an_assigned_asset_returns_true_with_more_than_one_assigned_asset() {
		CustomerOrg otherOrg1 = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg1.setLinkedOrg(primary);
		
		CustomerOrg otherOrg2 = OrgBuilder.aCustomerOrg().buildCustomer();
		otherOrg2.setLinkedOrg(secondary);
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(secondary);
		
		List<Asset> assets = Arrays.asList(
				AssetBuilder.anAsset().withOwner(primary).build(),
				AssetBuilder.anAsset().withOwner(otherOrg1).build(),
				AssetBuilder.anAsset().withOwner(otherOrg2).build(),
				AssetBuilder.anAsset().withOwner(primary).build(),
				AssetBuilder.anAsset().withOwner(primary).build());
		
		
		assertTrue(assetSecurityManager.listContainsAnAssignedAsset(assets));
	}
	
	@Test
	public void list_contains_an_assigned_asset_returns_false_with_no_assigned_asset() {
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(secondary);
		
		List<Asset> assets = Arrays.asList(
				AssetBuilder.anAsset().withOwner(primary).build(),
				AssetBuilder.anAsset().withOwner(primary).build(),
				AssetBuilder.anAsset().withOwner(primary).build());
		
		
		assertFalse(assetSecurityManager.listContainsAnAssignedAsset(assets));
	}
	
	@Test
	public void filter_list_test() {		
		CustomerOrg linkedOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		linkedOrg.setLinkedOrg(primary);
		
		SafetyNetworkAssetSecurityManager assetSecurityManager = new SafetyNetworkAssetSecurityManager(secondary);
		
		List<Asset> assets = Arrays.asList(
				AssetBuilder.anAsset().withOwner(OrgBuilder.aCustomerOrg().build()).build(),
				AssetBuilder.anAsset().withOwner(secondary).build(),
				AssetBuilder.anAsset().withOwner(linkedOrg).build(),
				AssetBuilder.anAsset().withOwner(OrgBuilder.aCustomerOrg().build()).build(),
				AssetBuilder.anAsset().withOwner(OrgBuilder.aDivisionOrg().build()).build());
		
		List<Asset> filtered = assetSecurityManager.filterOutExternalNotAssignedAssets(assets);
		assertEquals(2, filtered.size());
		
		assertEquals(secondary, filtered.get(0).getOwner());
		assertEquals(linkedOrg, filtered.get(1).getOwner());
	}
}
