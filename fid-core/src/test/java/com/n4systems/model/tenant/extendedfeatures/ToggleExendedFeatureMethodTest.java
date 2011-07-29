package com.n4systems.model.tenant.extendedfeatures;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.util.persistence.TestingTransaction;


public class ToggleExendedFeatureMethodTest {
	private static final ExtendedFeature AN_EXTENDED_FEATURE = ExtendedFeature.AssignedTo;
	
	TestingTransaction transaction = new TestingTransaction();
	PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().build();
	
	final ExtendedFeatureDisabler featureSwitch = createMock(ExtendedFeatureDisabler.class);
	
	@Test
	public void should_disable_feature_when_flag_is_off() throws Exception{
		featureSwitch.disableFeature(transaction);
		replay(featureSwitch);
		ExendedFeatureToggler toggler = new ExendedFeatureToggler(AN_EXTENDED_FEATURE, false) {
			@Override
			protected ExtendedFeatureDisabler getDisabler(PrimaryOrg primaryOrg) {
				return featureSwitch;
			}
		};
		
		toggler.applyTo(primaryOrg, transaction);
		
		verify(featureSwitch);
	}
	
	
}
