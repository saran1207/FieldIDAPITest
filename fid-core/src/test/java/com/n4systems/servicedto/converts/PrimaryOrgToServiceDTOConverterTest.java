package com.n4systems.servicedto.converts;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.webservice.dto.TenantServiceDTO;
import org.junit.Test;

import static com.n4systems.model.builders.PrimaryOrgBuilder.aPrimaryOrg;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class PrimaryOrgToServiceDTOConverterTest {

	
	@Test
	public void should_set_using_assigned_to_when_the_primary_org_has_extended_feature_assigned_to() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withExtendedFeatures(ExtendedFeature.AssignedTo).build();
		
		PrimaryOrgToServiceDTOConverter sut = new PrimaryOrgToServiceDTOConverter();
	
		TenantServiceDTO tenantServiceDTO = sut.convert(primaryOrg);
		
		assertThat(tenantServiceDTO.isUsingAssignedTo(), is(true));
		
	}
}
