package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.test.helpers.Asserts.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
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
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.test.helpers.FluentHashSet;
import com.n4systems.util.DataUnit;

public class AccountUpgradeHandlerImplTest extends TestUsesTransactionBase {

	
	@Before
	public void setUp() {
		mockTransaction();
	}
	
	@Test
	public void should_apply_features_to_tenant() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		HashSet<ExtendedFeature> expectedFeatures = new FluentHashSet<ExtendedFeature>(upgradePackage.getExtendedFeatures());
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		Set<ExtendedFeature> actualFeatures = primaryOrg.getExtendedFeatures();
		
		assertTrue("package must add extended features for test to be valid", upgradePackage.getExtendedFeatures().length > 0);
		assertSetsEquals(expectedFeatures, actualFeatures);
	}

	
	@Test
	public void should_apply_additional_features_to_tenant() throws Exception {
		ExtendedFeature currentExtendedFeatureNotIncludedInPackage = ExtendedFeature.DedicatedProgramManager;
		PrimaryOrg primaryOrg = aPrimaryOrg().withExtendedFeatures(currentExtendedFeatureNotIncludedInPackage).build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		HashSet<ExtendedFeature> expectedFeatures = new FluentHashSet<ExtendedFeature>(upgradePackage.getExtendedFeatures())
				.stickOn(currentExtendedFeatureNotIncludedInPackage);
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		Set<ExtendedFeature> actualFeatures = primaryOrg.getExtendedFeatures();
		
		assertTrue("package must add extended features for test to be valid", expectedFeatures.size() > 0);
		assertTrue("package must not have extended feature " + currentExtendedFeatureNotIncludedInPackage.name() + " for test to be valid", !upgradePackage.includesFeature(currentExtendedFeatureNotIncludedInPackage));
		assertSetsEquals(expectedFeatures, actualFeatures);
	}
	
	
	@Test
	public void should_use_extended_feature_switches_to_add_extended_features() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		
		UpgradeHandlerImplTenatSwitchOverride sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		assertEquals(new FluentArrayList<ExtendedFeature>(upgradePackage.getExtendedFeatures()), sut.requestedFeatureSwitches);
		for (ExtendedFeatureSwitchTestDouble feautureSwitch : sut.featureSwitches) {
			assertEquals(1, feautureSwitch.callsToFeatureSetup);
		}
	}


	
	@Test
	public void should_save_primary_org_after_upgrading() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withNoExtendedFeatures().build();
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		
		OrgSaver orgSaver = successfulOrgSaver(primaryOrg);
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, orgSaver);
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		verify(orgSaver);
	}
	
	
	@Test
	public void should_increase_the_asset_limit_from_25_to_unlimited_when_upgrading_from_Free_to_Plus() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		
		PrimaryOrg primaryOrg = aPrimaryOrg().withAssetLimit(25L).build();
		
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		Long actualAssetLimit = primaryOrg.getLimits().getAssets();
		
		assertEquals(upgradePackage.getAssets(), actualAssetLimit);
	}
	
	@Test
	public void should_keep_the_current_number_of_assets_when_an_account_already_has_more_assets_than_the_new_package_provides() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		
		long startingAssetLimit = upgradePackage.getAssets() + 25L;
		
		PrimaryOrg primaryOrg = aPrimaryOrg().withAssetLimit(startingAssetLimit).build();
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		long actualAssetLimit = primaryOrg.getLimits().getAssets();
		
		assertEquals(startingAssetLimit, actualAssetLimit);
	}
	
	@Test
	public void should_keep_the_current_number_of_assets_when_an_account_already_has_unlimited_assets() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Basic;
		PrimaryOrg primaryOrg = aPrimaryOrg().withAssetLimit(TenantLimit.UNLIMITED).build();
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		long actualAssetLimit = primaryOrg.getLimits().getAssets();
		
		assertEquals(TenantLimit.UNLIMITED.longValue(), actualAssetLimit);
	}


	
	@Test
	public void should_increase_the_diskspace_limit_from_5M_to_1000MB_when_upgrading_from_Free_to_Plus() throws Exception {
		SignUpPackageDetails upgradePackage = SignUpPackageDetails.Plus;
		
		PrimaryOrg primaryOrg = aPrimaryOrg().withDiskSpaceLimit(5L, DataUnit.MEBIBYTES).build();
		
		
		UpgradeHandler sut = new UpgradeHandlerImplTenatSwitchOverride(primaryOrg, successfulOrgSaver(primaryOrg));
		
		sut.upgradeTo(upgradePackage, mockTransaction);
		
		Long actualDiskSpaceLimit = DataUnit.BYTES.convertTo(primaryOrg.getLimits().getDiskSpaceInBytes(), DataUnit.MEBIBYTES);
		
		assertEquals(upgradePackage.getDiskSpaceInMB(), actualDiskSpaceLimit);
	}
	
	private OrgSaver successfulOrgSaver(PrimaryOrg primaryOrg) {
		OrgSaver orgSaver = createMock(OrgSaver.class);
		expect(orgSaver.update(mockTransaction, primaryOrg)).andReturn(primaryOrg);
		replay(orgSaver);
		return orgSaver;
	}
	
	private class UpgradeHandlerImplTenatSwitchOverride extends UpgradeHandlerImpl {
		private List<ExtendedFeature> requestedFeatureSwitches = new ArrayList<ExtendedFeature>();
		private List<ExtendedFeatureSwitchTestDouble> featureSwitches = new ArrayList<ExtendedFeatureSwitchTestDouble>();
		
		public UpgradeHandlerImplTenatSwitchOverride(PrimaryOrg primaryOrg, OrgSaver orgSaver) {
			super(primaryOrg, orgSaver);
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


