package com.n4systems.fieldid.selenium.datatypes;

import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class AssetStatus {

	public final String name;

	public AssetStatus(String name) {
		this.name = name;
	}

	public static AssetStatus aVaildAssetStatus() {
		return new AssetStatus(randomStatusName());
	}

	private static String randomStatusName() {
		return MiscDriver.getRandomString(25);
	}

	@Override
	public String toString() {
		return "AssetStatus [name=" + name + "]";
	}

}
