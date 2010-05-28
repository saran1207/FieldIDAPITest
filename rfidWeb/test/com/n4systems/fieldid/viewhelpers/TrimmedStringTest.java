package com.n4systems.fieldid.viewhelpers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class TrimmedStringTest {

	
	@Test
	public void should_create_an_empty_trimmed_string() throws Exception {
	
		assertThat(new TrimmedString(""), hasToString(equalTo("")));
		assertThat(new TrimmedString(null), hasToString(equalTo("")));
		
		assertThat(new TrimmedString("       "), hasToString(equalTo("")));
	}
	
	@Test
	public void should_trim_string_given_to_it() throws Exception {
		assertThat(new TrimmedString(" bob"), hasToString(equalTo("bob")));
		assertThat(new TrimmedString(" bob "), hasToString(equalTo("bob")));
		assertThat(new TrimmedString("bob   "), hasToString(equalTo("bob")));
		
		assertThat(new TrimmedString(" bob mc  donald"), hasToString(equalTo("bob mc  donald")));
	}
	
	
	
	
	
}
