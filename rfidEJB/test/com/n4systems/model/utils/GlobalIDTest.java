package com.n4systems.model.utils;

import java.util.UUID;

import org.junit.Test;

public class GlobalIDTest {

	@Test
	public void get_id_creates_uuid() {
		String id = GlobalID.getId();
		
		// this will throw an exception if it's not a uuid
		UUID.fromString(id);
	}
	
}
