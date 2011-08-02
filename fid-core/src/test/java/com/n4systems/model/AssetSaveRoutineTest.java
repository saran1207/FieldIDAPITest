package com.n4systems.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class AssetSaveRoutineTest {

	
	@Test
	public void should_null_blank_string_identifier() {
		Asset sut = new Asset();
		sut.setIdentifier(" ");
		
		sut.onCreate();
		
		assertNull(sut.getIdentifier());
	}
	
	@Test
	public void should_handle_null_identifier() {
		Asset sut = new Asset();
		sut.setIdentifier(null);
		
		sut.onCreate();
		
		assertNull(sut.getIdentifier());
	}
	
	@Test
	public void should_trim_identifier() {
		Asset sut = new Asset();
		sut.setIdentifier(" some identifier  ");
		
		sut.onCreate();
		
		assertEquals("some identifier", sut.getIdentifier());
	}
	
	@Test
	public void should_leave_identifier_as_is() {
		Asset sut = new Asset();
		sut.setIdentifier("some identifier");
		
		sut.onCreate();
		
		assertEquals("some identifier", sut.getIdentifier());
	}
	
	@Test
	public void should_null_blank_string_rfid_number() {
		Asset sut = new Asset();
		sut.setRfidNumber(" ");
		
		sut.onCreate();
		
		assertNull(sut.getRfidNumber());
	}
	
	@Test
	public void should_handle_null_rfid_number() {
		Asset sut = new Asset();
		sut.setRfidNumber(null);
		
		sut.onCreate();
		
		assertNull(sut.getRfidNumber());
	}
	
	@Test
	public void should_trim_rfid_number() {
		Asset sut = new Asset();
		sut.setRfidNumber(" 43332ABD33122131  ");
		
		sut.onCreate();
		
		assertEquals("43332ABD33122131", sut.getRfidNumber());
	}
	
	@Test
	public void should_leave_rfid_number_as_is() {
		Asset sut = new Asset();
		sut.setRfidNumber("43332ABD33122131");
		
		sut.onCreate();
		
		assertEquals("43332ABD33122131", sut.getRfidNumber());
	}
}
