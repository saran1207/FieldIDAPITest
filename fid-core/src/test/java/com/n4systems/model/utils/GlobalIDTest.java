package com.n4systems.model.utils;

import org.junit.Test;

import java.util.UUID;

public class GlobalIDTest {

	@Test
	public void get_id_creates_uuid() {
		String id = GlobalID.getId();
		
		// this will throw an exception if it's not a uuid
		UUID.fromString(id);
	}
	
}
