package com.n4systems.util.uri;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.ConfigEntry;


public class BaseUrlBuilderTest {

	
	private ConfigContextOverridableTestDouble configContext;

	private final class BlankPathBaseUrlBuilder extends BaseUrlBuilder {
		private BlankPathBaseUrlBuilder(URI baseUri, ConfigContext configContext) {
			super(baseUri, configContext);
		}

		@Override
		protected String path() {
			return "";
		}
	}
	
	private final class SuppliedPathBaseUrlBuilder extends BaseUrlBuilder {
		private final String path;

		private SuppliedPathBaseUrlBuilder(URI baseUri, ConfigContext configContext, String path) {
			super(baseUri, configContext);
			this.path = path;
		}

		@Override
		protected String path() {
			return path;
		}
	}

	
	@Before
	public void setup() {
		configContext = new ConfigContextOverridableTestDouble();
		configContext.addConfigurationValue(ConfigEntry.SYSTEM_PROTOCOL, "https");
	}
	
	@Test
	public void should_not_change_the_protocol_on_the_uri_when_it_matches_the_config_entry() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("https://somedomain.com/"), configContext);
		
		assertEquals("https://somedomain.com/", sut.build());
	}

	@Test
	public void should_change_the_protocol_on_the_uri_when_it_matches_the_config_entry() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("http://somedomain.com/"), configContext);
		assertEquals("https://somedomain.com/", sut.build());
	}
	
	
	@Test
	public void should_maintain_the_context_in_the_base_url() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("https://somedomain.com/fieldid/"), configContext);
		assertEquals("https://somedomain.com/fieldid/", sut.build());
	}
	
	
	@Test
	public void should_keep_file_and_query_string_of_target_when_correcting_the_protocol() throws Exception {
		BaseUrlBuilder sut = new SuppliedPathBaseUrlBuilder(URI.create("http://somedomain.com/fieldid/"), configContext, "somefile.action?query=string");
		assertEquals("https://somedomain.com/fieldid/somefile.action?query=string", sut.build());
	}
	
	@Test
	public void should_keep_file_and_query_string_of_target_when_not_correcting_the_protocol() throws Exception {
		BaseUrlBuilder sut = new SuppliedPathBaseUrlBuilder(URI.create("https://somedomain.com/fieldid/"), configContext, "somefile.action?query=string");
		assertEquals("https://somedomain.com/fieldid/somefile.action?query=string", sut.build());
	}
	
	@Test
	public void should_force_sub_domain_change_when_supplied_with_a_company_target() throws Exception {
		Tenant tenant =  aTenant().named("alternate-tenant").build();
		BaseUrlBuilder sut = new SuppliedPathBaseUrlBuilder(URI.create("https://company.somedomain.com/fieldid/"), configContext, "somefile.action");
		sut.setCompany(tenant);
		assertEquals("https://alternate-tenant.somedomain.com/fieldid/somefile.action", sut.build());
	}
	
	
	@Test
	public void should_add_parameters_to_path_when_given() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("https://somedomain.com/fieldid/"), configContext);
		sut.addParameter("param1", 4);
		assertThat(sut.build(), endsWith("?param1=4"));
	}	
	
	
	@Test
	public void should_add_multiple_parameters_with_amps_between_them() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("https://somedomain.com/fieldid/"), configContext);
		sut.addParameter("param1", 4).addParameter("param2", "hello").addParameter("param3", 'c');
		
		assertThat(sut.build(), endsWith("?param1=4&param2=hello&param3=c"));
	}
	
	
	@Test
	public void should_url_escape_the_values_of_a_parameter() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("https://somedomain.com/fieldid/"), configContext);
		sut.addParameter("param", "= &");
		
		assertThat(sut.build(), endsWith("?param=%3D+%26"));
	}
	
	@Test
	public void should_url_escape_the_name_of_a_parameter() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder(URI.create("https://somedomain.com/fieldid/"), configContext);
		sut.addParameter("param 1", "somevalue");
		
		assertThat(sut.build(), endsWith("?param+1=somevalue"));
	}
	
}
