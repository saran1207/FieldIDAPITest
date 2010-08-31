package com.n4systems.util;

import java.util.ArrayList;
import java.util.List;

public class StringListingPair {
	private String id;
	
	private String name;

	public StringListingPair(String id) {
		super();
		this.id = id;
		this.name = id;
	}
	
	public StringListingPair(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public static List<StringListingPair> convertToListingPair(String[] strings) {
		List<StringListingPair> result = new ArrayList<StringListingPair>();
		for (String string : strings) {
			result.add(new StringListingPair(string));
		}
		return result;
	}
}
