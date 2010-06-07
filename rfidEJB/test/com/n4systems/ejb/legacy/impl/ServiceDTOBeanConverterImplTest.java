package com.n4systems.ejb.legacy.impl;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.webservice.dto.TenantServiceDTO;


public class ServiceDTOBeanConverterImplTest {

	
	@Test
	public void should_set_using_assigned_to_when_the_primary_org_has_extended_feature_assigned_to() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withExtendedFeatures(ExtendedFeature.AssignedTo).build();
		
		ServiceDTOBeanConverterImpl sut = new ServiceDTOBeanConverterImpl();
	
		TenantServiceDTO tenantServiceDTO = sut.convert(primaryOrg);
		
		assertThat(tenantServiceDTO.isUsingAssignedTo(), is(true));
		
	}
}
