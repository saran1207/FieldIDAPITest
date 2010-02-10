package com.n4systems.model.utils;

import java.util.UUID;

public class GlobalID {
	
	public static GlobalID create() {
		return new GlobalID();
	}
	
	public static String getId() {
		return create().toString();
	}
	
	private final String ident;
	
	public GlobalID() {
		ident = UUID.randomUUID().toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GlobalID)) {
			return false;
		}
		return ident.equals(((GlobalID)obj).ident);
	}

	@Override
	public int hashCode() {
		return ident.hashCode();
	}

	@Override
	public String toString() {
		return ident;
	}
}
