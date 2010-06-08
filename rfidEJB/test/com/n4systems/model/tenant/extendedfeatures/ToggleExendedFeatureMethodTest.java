package com.n4systems.model.tenant.extendedfeatures;

import static org.easymock.classextension.EasyMock.*;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.util.persistence.TestingTransaction;


public class ToggleExendedFeatureMethodTest {
	private static final ExtendedFeature AN_EXTENDED_FEATURE = ExtendedFeature.AssignedTo;
	
	TestingTransaction transaction = new TestingTransaction();
	PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().build();
	
	final ExtendedFeatureSwitch featureSwitch = createMock(ExtendedFeatureSwitch.class);
	
	

	@Test
	public void should_enable_feature_when_flag_is_on() throws Exception {
		
		featureSwitch.enableFeature(transaction);
		replay(featureSwitch);
		
		ToggleExendedFeatureMethod toggler = new ToggleExendedFeatureMethod(AN_EXTENDED_FEATURE, true) {
			@Override
			protected ExtendedFeatureSwitch switchFor(PrimaryOrg primaryOrg) {
				return featureSwitch;
			}
		};
		toggler.applyTo(primaryOrg, transaction);

		
		verify(featureSwitch);
	}
	
	@Test
	public void should_disable_feature_when_flag_is_off() throws Exception{
		featureSwitch.disableFeature(transaction);
		replay(featureSwitch);
		ToggleExendedFeatureMethod toggler = new ToggleExendedFeatureMethod(AN_EXTENDED_FEATURE, false) {
			@Override
			protected ExtendedFeatureSwitch switchFor(PrimaryOrg primaryOrg) {
				return featureSwitch;
			}
		};
		
		toggler.applyTo(primaryOrg, transaction);
		
		verify(featureSwitch);
	}
	
	
}
