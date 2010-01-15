package com.n4systems.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class AddressInfoTest {

	
	@Test
	public void should_change_null_street_address_to_blank_string() throws Exception {
		AddressInfo sut = new AddressInfo();
		sut.setStreetAddress(null);
		assertEquals("",sut.getStreetAddress());
	}
	
	@Test
	public void should_trim_street_address() throws Exception {
		AddressInfo sut = new AddressInfo();
		sut.setStreetAddress("    as  ");
		assertEquals("as",sut.getStreetAddress());
	}
	
	@Test
	public void should_replace_the_second_street_address_with_a_blank_string_if_it_is_null() throws Exception {
		AddressInfo sut = new AddressInfo();
		sut.setStreetAddress("some Address", null);
		
		assertEquals("some Address",sut.getStreetAddress());
	}
	
	@Test
	public void should_replace_the_first_street_address_with_a_blank_string_if_it_is_null() throws Exception {
		AddressInfo sut = new AddressInfo();
		sut.setStreetAddress(null, "some Address");
		
		assertEquals("some Address",sut.getStreetAddress());
	}
	
	@Test
	public void should_replace_the_street_address_with_a_blank_string_if_both_street_address_parts_are_null() throws Exception {
		AddressInfo sut = new AddressInfo();
		sut.setStreetAddress(null, null);
		
		assertEquals("",sut.getStreetAddress());
	}
}
