package com.n4systems.util;

public class GUIDHelper {

	private static final String NULL_GUID = "00000000-0000-0000-0000-000000000000";

	public static boolean isNullGUID(String guid) {
		return (guid == null || guid.trim().length() == 0 || guid.equals(NULL_GUID));
	}
}
