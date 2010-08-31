package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.test.helpers.Asserts.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactoryTestDouble;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitchTestDouble;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.n4systems.subscription.UpgradeSubscription;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.test.helpers.FluentHashSet;
import com.n4systems.util.DataUnit;

public class UpgradePlanHandlerImplTest extends TestUsesTransactionBase {

	
	private UpgradeRequest upgradeRequest = new UpgradeRequest();

	@Before
	public void setUp() {
		mockTransaction();
	}
	
	@Test
	public void should_apply_features_to_tenant() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		
		upgradeRequest.setUpgradePackage(upgradePackage);
		
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		HashSet<ExtendedFeature> expectedFeatures = new FluentHashSet<ExtendedFeature>(upgradePackage.getExtendedFeatures());
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		Set<ExtendedFeature> actualFeatures = primaryOrg.getExtendedFeatures();
		
		assertTrue("package must add extended features for test to be valid", upgradePackage.getExtendedFeatures().length > 0);
		assertSetsEquals(expectedFeatures, actualFeatures);
	}

	
	@Test
	public void should_apply_additional_features_to_tenant() throws Exception {
		ExtendedFeature currentExtendedFeatureNotIncludedInPackage = ExtendedFeature.DedicatedProgramManager;
		PrimaryOrg primaryOrg = aPrimaryOrg().withExtendedFeatures(currentExtendedFeatureNotIncludedInPackage).build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		upgradeRequest.setUpgradePackage(upgradePackage);
		
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		HashSet<ExtendedFeature> expectedFeatures = new FluentHashSet<ExtendedFeature>(upgradePackage.getExtendedFeatures())
				.stickOn(currentExtendedFeatureNotIncludedInPackage);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		Set<ExtendedFeature> actualFeatures = primaryOrg.getExtendedFeatures();
		
		assertTrue("package must add extended features for test to be valid", expectedFeatures.size() > 0);
		assertTrue("package must not have extended feature " + currentExtendedFeatureNotIncludedInPackage.name() + " for test to be valid", !upgradePackage.includesFeature(currentExtendedFeatureNotIncludedInPackage));
		assertSetsEquals(expectedFeatures, actualFeatures);
	}
	
	
	@Test
	public void should_use_extended_feature_switches_to_add_extended_features() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		upgradeRequest.setUpgradePackage(upgradePackage);
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		
		UpgradeHandlerImplTenatSwitchOverride sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		assertEquals(new FluentArrayList<ExtendedFeature>(upgradePackage.getExtendedFeatures()), sut.requestedFeatureSwitches);
		for (ExtendedFeatureSwitchTestDouble feautureSwitch : sut.featureSwitches) {
			assertEquals(1, feautureSwitch.callsToFeatureSetup);
		}
	}


	
	@Test
	public void should_save_primary_org_after_upgrading() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		upgradeRequest.setUpgradePackage(upgradePackage);
		OrgSaver orgSaver = successfulOrgSaver(primaryOrg);
		
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, orgSaver, subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		verify(orgSaver);
	}
	
	
	@Test
	public void should_increase_the_asset_limit_from_25_to_unlimited_when_upgrading_from_Free_to_Plus() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		upgradeRequest.setUpgradePackage(upgradePackage);
		PrimaryOrg primaryOrg = aPrimaryOrg().withAssetLimit(25L).build();
		
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		Long actualAssetLimit = primaryOrg.getLimits().getAssets();
		
		assertEquals(upgradePackage.getAssets(), actualAssetLimit);
	}
	
	@Test
	public void should_keep_the_current_number_of_assets_when_an_account_already_has_more_assets_than_the_new_package_provides() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		upgradeRequest.setUpgradePackage(upgradePackage);
		long startingAssetLimit = upgradePackage.getAssets() + 25L;
		
		PrimaryOrg primaryOrg = aPrimaryOrg().withAssetLimit(startingAssetLimit).build();
		
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		long actualAssetLimit = primaryOrg.getLimits().getAssets();
		
		assertEquals(startingAssetLimit, actualAssetLimit);
	}
	
	@Test
	public void should_keep_the_current_number_of_assets_when_an_account_already_has_unlimited_assets() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		PrimaryOrg primaryOrg = aPrimaryOrg().withAssetLimit(TenantLimit.UNLIMITED).build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		upgradeRequest.setUpgradePackage(upgradePackage);
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		long actualAssetLimit = primaryOrg.getLimits().getAssets();
		
		assertEquals(TenantLimit.UNLIMITED.longValue(), actualAssetLimit);
	}


	
	@Test
	public void should_increase_the_diskspace_limit_from_5M_to_1000MB_when_upgrading_from_Free_to_Plus() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		PrimaryOrg primaryOrg = aPrimaryOrg().withDiskSpaceLimit(5L, DataUnit.MEBIBYTES).build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		upgradeRequest.setUpgradePackage(upgradePackage);
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		Long actualDiskSpaceLimit = DataUnit.BYTES.convertTo(primaryOrg.getLimits().getDiskSpaceInBytes(), DataUnit.MEBIBYTES);
		
		assertEquals(upgradePackage.getDiskSpaceInMB(), actualDiskSpaceLimit);
	}
	
	
	
	@Test
	public void should_send_upgrade_information_to_subscription_agent() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		upgradeRequest.setUpgradePackage(upgradePackage);
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
		verify(subscriptionAgent);
	}
	
	@Test
	public void should_not_upgrade_primary_org_when_subscription_agent_reports_failure() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		upgradeRequest.setUpgradePackage(upgradePackage);
		
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().withAssetLimit(0L).withDiskSpaceLimit(0L, DataUnit.BYTES).build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForFailingUpgrade();
		
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, null, subscriptionAgent);
		
		assertNull(sut.upgradeTo(upgradeRequest, mockTransaction));
		
		assertTrue(primaryOrg.getExtendedFeatures().isEmpty());
		assertEquals(new Long(0), primaryOrg.getLimits().getAssets());
		assertEquals(new Long(0), primaryOrg.getLimits().getDiskSpaceInBytes());
		verify(subscriptionAgent);
	}
	
	@Test(expected=CommunicationException.class)
	public void should_not_upgrade_primary_org_when_there_is_a_communication_error() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		upgradeRequest.setUpgradePackage(upgradePackage);
		
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.upgrade((UpgradeSubscription)anyObject())).andThrow(new CommunicationException());
		replay(subscriptionAgent);
		
		
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(null, null, subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
	}
	
	@Test(expected=BillingInfoException.class)
	public void should_not_upgrade_primary_org_when_there_is_a_billing_information_error() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		upgradeRequest.setUpgradePackage(upgradePackage);
		
		
		
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.upgrade((UpgradeSubscription)anyObject())).andThrow(new BillingInfoException());
		replay(subscriptionAgent);
		
		
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(null, null, subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
		
	}
	
	
	
	
	@Test
	public void should_find_price_for_upgrade_package() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = createSubscriptionAgentForUpgrade(new UpgradeCost(300.0F, 4000.0F, "DEC. 10 2009"));
		
		upgradeRequest.setUpgradePackage(upgradePackage);
		
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, null, subscriptionAgent);
		
		UpgradeCost expectedCost = new UpgradeCost(300.0F, 4000.0F, "DEC. 10 2009");
		
		assertEquals(expectedCost, sut.priceForUpgrade(upgradeRequest));
	}
	
	
	
	@Test
	public void should_get_an_upgrade_response() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		upgradeRequest.setUpgradePackage(upgradePackage);
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg), subscriptionAgent);
		
		
		
		UpgradeResponse actualResponse = sut.upgradeTo(upgradeRequest, mockTransaction);
		
		assertNotNull(actualResponse);
		
		verify(subscriptionAgent);
	}
	
	
	
	@Test(expected=UpgradeCompletionException.class)
	public void should_respond_with_a_failed_to_apply_upgrade_if_upgrade_was_successful_but_changes_were_not_saved() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		upgradeRequest.setUpgradePackage(upgradePackage);
		UpgradeAccountHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, failingOrgSaver(primaryOrg), subscriptionAgent);
		
		sut.upgradeTo(upgradeRequest, mockTransaction);
	}
	
	private SubscriptionAgent createSubscriptionAgentForUpgrade(UpgradeCost upgradeCost) throws CommunicationException {
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.costToUpgradeTo((UpgradeSubscription)anyObject())).andReturn(upgradeCost);
		replay(subscriptionAgent);
		return subscriptionAgent;
	}
	
	private SubscriptionAgent subScriptionAgentForSuccessfulUpgrade() throws Exception {
		return createSubscriptionAgentForUpgrade(true);
	}
	
	private SubscriptionAgent subScriptionAgentForFailingUpgrade() throws Exception {
		return createSubscriptionAgentForUpgrade(false);
	}

	private SubscriptionAgent createSubscriptionAgentForUpgrade(boolean upgradeResult) throws Exception {
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		UpgradeResponse upgradeResponse = upgradeResult ? new UpgradeResponse(new UpgradeCost(1L, 1L, ""), 1L) : null;
		expect(subscriptionAgent.upgrade((UpgradeSubscription)anyObject())).andReturn(upgradeResponse);
		replay(subscriptionAgent);
		return subscriptionAgent;
	}
	
	private OrgSaver successfulOrgSaver(PrimaryOrg primaryOrg) {
		OrgSaver orgSaver = createMock(OrgSaver.class);
		expect(orgSaver.update(mockTransaction, primaryOrg)).andReturn(primaryOrg);
		replay(orgSaver);
		return orgSaver;
	}
	
	private OrgSaver failingOrgSaver(PrimaryOrg primaryOrg) {
		OrgSaver orgSaver = createMock(OrgSaver.class);
		expect(orgSaver.update(mockTransaction, primaryOrg)).andThrow(new RuntimeException("something failed while saving"));
		replay(orgSaver);
		return orgSaver;
	}
	
	private class UpgradeHandlerImplTenatSwitchOverride extends UpgradePlanHandlerImpl {
		private List<ExtendedFeature> requestedFeatureSwitches = new ArrayList<ExtendedFeature>();
		private List<ExtendedFeatureSwitchTestDouble> featureSwitches = new ArrayList<ExtendedFeatureSwitchTestDouble>();
		
		public UpgradeHandlerImplTenatSwitchOverride(PrimaryOrg primaryOrg, OrgSaver orgSaver, SubscriptionAgent subscriptionAgent) {
			super(primaryOrg, orgSaver, subscriptionAgent);
		}

		@Override
		protected ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature) {
			requestedFeatureSwitches.add(feature);
			ExtendedFeatureSwitchTestDouble newSwitch = ExtendedFeatureFactoryTestDouble.getSwitchFor(feature, getPrimaryOrg());
			featureSwitches.add(newSwitch);
			return newSwitch;
		}
	}		
		
}


