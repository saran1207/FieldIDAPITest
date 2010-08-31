package com.n4systems.fieldid.actions.subscriptions.plansandpricing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.model.builders.TenantBuilder;

public class PlansAndPricingUrlBuilderTest {
	private Tenant tenant;
	
	@Before
	public void setup() {
		tenant = TenantBuilder.aTenant().named("mytenant").build();
	}
	
	@Test
	public void should_not_modify_url_path() {
		PlansAndPricingUrlBuilder sut = new PlansAndPricingUrlBuilder("http://some.url.com/hello/", tenant, null);
		
		assertThat(sut.build(), startsWith("http://some.url.com/hello/"));
	}
	
	@Test
	public void should_set_company_id_from_tenant() {
		PlansAndPricingUrlBuilder sut = new PlansAndPricingUrlBuilder("http://some.url.com/hello/", tenant, null);
		
		assertThat(sut.build(), containsString("companyId=mytenant"));
	}
	
	@Test
	public void should_not_set_ref_code_when_null() {
		PlansAndPricingUrlBuilder sut = new PlansAndPricingUrlBuilder("http://some.url.com/hello/", tenant, null);
		
		assertThat(sut.build(), not(containsString("refCode")));
	}
	
	@Test
	public void should_not_set_ref_code_when_blank() {
		PlansAndPricingUrlBuilder sut = new PlansAndPricingUrlBuilder("http://some.url.com/hello/", tenant, "  ");
		
		assertThat(sut.build(), not(containsString("refCode")));
	}
	
	@Test
	public void should_include_refcode_when_not_null() {
		PlansAndPricingUrlBuilder sut = new PlansAndPricingUrlBuilder("http://some.url.com/hello/", tenant, "code");
		
		assertThat(sut.build(), containsString("refCode=code"));
	}
}
