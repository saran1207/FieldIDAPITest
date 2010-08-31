package com.n4systems.fieldid.actions.webStoreSSO;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;


public class WebStoreSSOUrlBuilderTest {
	
	@Test
	public void should_have_the_create_url_targeting_netsuite_sign_in_page() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		
		String url = sut.build();
		
		assertThat(url, startsWith(WebStoreSSOUrlBuilder.STORE_URL));
	}

	
	@Test
	public void should_attach_the_base_domain_to_the_url_as_a_parameter_encoded_correctly() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		
		sut.withBaseDomain("https://company.domain.com/xxxxx/");
		
		String url = sut.build();
		
		assertThat(url, containsString("baseDomain=https%3A%2F%2Fcompany.domain.com%2Fxxxxx%2F"));
	}
	
	@Test
	public void should_attach_the_external_auth_key_to_the_url_as_a_parameter_encoded_correctly() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		
		UUID authKey = UUID.randomUUID();
		sut.authKey(authKey);
		
		String url = sut.build();
		
		assertThat(url, containsString("externalAuthKey=" + authKey.toString()));
	}
	
	
	@Test
	public void should_attach_the_target_action_to_the_url_as_a_parameter_encoded_correctly() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		
		sut.targetAction("ajax/externalAuthVerification.action");
		String url = sut.build();
		
		assertThat(url, containsString("targetAction=" + "ajax%2FexternalAuthVerification.action"));
	}
	
	
	@Test(expected=Exception.class)
	public void should_throw_when_null_is_given_for_base_domain_or_it_is_never_set() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		sut.withBaseDomain(null);
		sut.build();
	}
	
	@Test(expected=Exception.class)
	public void should_throw_when_null_is_given_for_target_action_or_it_is_never_set() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		sut.targetAction(null);
		sut.build();
	}
	@Test(expected=Exception.class)
	public void should_throw_when_null_is_given_for_external_auth_key_or_it_is_never_set() throws Exception {
		WebStoreSSOUrlBuilder sut = createFullyBuiltUrlBuilder();
		sut.authKey(null);
		sut.build();
	}
	
	private WebStoreSSOUrlBuilder createFullyBuiltUrlBuilder() {
		WebStoreSSOUrlBuilder sut = new WebStoreSSOUrlBuilder();
		sut.withBaseDomain("").authKey(UUID.randomUUID()).targetAction("ajax/externalAuthVerification.action");
		return sut;
	}

}
