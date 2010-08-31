package com.n4systems.uitags.views;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.tenant.TenantLimit;


public class PercentBarComponentTest {

	
	@Test
	public void should_have_the_same_value_for_unlimited_as_the_tenant_limit_to_allow_for_the_correct_visualization_of_tenant_limits_and_usage() throws Exception {
		assertThat(PercentBarComponent.UNLIMITED, equalTo(TenantLimit.UNLIMITED.intValue()));
	}
}
