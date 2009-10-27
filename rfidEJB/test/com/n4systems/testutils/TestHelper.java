package com.n4systems.testutils;

import java.util.UUID;

public class TestHelper {
	
	public static String randomString() {
		return UUID.randomUUID().toString();
	}
}
