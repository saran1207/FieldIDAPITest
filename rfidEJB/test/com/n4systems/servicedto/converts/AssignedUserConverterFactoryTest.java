package com.n4systems.servicedto.converts;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.persistence.loaders.LoaderFactory;


public class AssignedUserConverterFactoryTest {

	private static final LoaderFactory A_LOADER_FACTORY = null;

	@Test
	public void should_produce_an_assigned_user_converter_that_will_translate_the_assigned_to_user_when_the_assignedTo_feature_is_enabled() throws Exception {
		SystemSecurityGuard systemSecurityGuard = createMock(SystemSecurityGuard.class);
		expect(systemSecurityGuard.isAssignedToEnabled()).andReturn(true);
		replay(systemSecurityGuard);
		
		
		AssignedUserConverterFactory sut = new AssignedUserConverterFactory(systemSecurityGuard, A_LOADER_FACTORY);
		
		
		assertThat(sut.getAssignedUserConverter(), instanceOf(PopulateAssignedUserConverter.class));
	}
	
	@Test
	public void should_produce_a_null_assigned_user_converter_that_will_translate_the_assigned_to_user_when_the_assignedTo_feature_is_disabled() throws Exception {
		SystemSecurityGuard systemSecurityGuard = createMock(SystemSecurityGuard.class);
		expect(systemSecurityGuard.isAssignedToEnabled()).andReturn(false);
		replay(systemSecurityGuard);
		
		
		AssignedUserConverterFactory sut = new AssignedUserConverterFactory(systemSecurityGuard, A_LOADER_FACTORY);
		
		
		assertThat(sut.getAssignedUserConverter(), instanceOf(NullAssignedUserConverter.class));
	}
	
	
}
 