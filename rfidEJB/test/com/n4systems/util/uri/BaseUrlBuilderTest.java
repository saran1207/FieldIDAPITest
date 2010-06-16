package com.n4systems.util.uri;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BaseUrlBuilderTest {
	
	private class SuppliedPathUrlBuilder extends BaseUrlBuilder {
		private final String path;
		
		private SuppliedPathUrlBuilder(String baseUri, String path) {
			super(baseUri);
			this.path = path;
		}

		@Override
		protected String path() {
			return path;
		}
	}
	
	private class BlankPathBaseUrlBuilder extends SuppliedPathUrlBuilder {
		private BlankPathBaseUrlBuilder(String baseUri) {
			super(baseUri, "");
		}
	}
	
	@Test 
	public void should_preserve_base_path_with_relative_path() {
		BaseUrlBuilder sut = new SuppliedPathUrlBuilder("http://www.bleh.com/hello/", "world/");
		assertEquals("http://www.bleh.com/hello/world/", sut.build());
	}
	
	@Test 
	public void should_wipe_base_path_with_absolute_path() {
		BaseUrlBuilder sut = new SuppliedPathUrlBuilder("http://www.bleh.com/hello/", "/world/");
		assertEquals("http://www.bleh.com/world/", sut.build());
	}
	
	@Test 
	public void should_wipe_base_file_with_any_path() {
		String base = "http://www.bleh.com/hello";
		
		BaseUrlBuilder sut = new SuppliedPathUrlBuilder(base, "/world/");
		assertEquals("http://www.bleh.com/world/", sut.build());
		
		sut = new SuppliedPathUrlBuilder(base, "world/");
		assertEquals("http://www.bleh.com/world/", sut.build());
	}
	
	@Test
	public void should_add_parameters_to_path_when_given() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder("https://somedomain.com/fieldid/");
		sut.addParameter("param1", 4);
		assertThat(sut.build(), endsWith("?param1=4"));
	}	
	
	
	@Test
	public void should_add_multiple_parameters_with_amps_between_them() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder("https://somedomain.com/fieldid/");
		sut.addParameter("param1", 4).addParameter("param2", "hello").addParameter("param3", 'c');
		
		assertThat(sut.build(), endsWith("?param1=4&param2=hello&param3=c"));
	}
	
	
	@Test
	public void should_url_escape_the_values_of_a_parameter() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder("https://somedomain.com/fieldid/");
		sut.addParameter("param", "= &");
		
		assertThat(sut.build(), endsWith("?param=%3D+%26"));
	}
	
	@Test
	public void should_url_escape_the_name_of_a_parameter() throws Exception {
		BaseUrlBuilder sut = new BlankPathBaseUrlBuilder("https://somedomain.com/fieldid/");
		sut.addParameter("param 1", "somevalue");
		
		assertThat(sut.build(), endsWith("?param+1=somevalue"));
	}
	
}
