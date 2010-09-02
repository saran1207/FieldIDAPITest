package com.n4systems.fieldid.selenium.datatypes;

import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ProductStatus {

	public final String name;

	public ProductStatus(String name) {
		this.name = name;
	}

	public static ProductStatus aVaildProductStatus() {
		return new ProductStatus(randomStatusName());
	}

	private static String randomStatusName() {
		return MiscDriver.getRandomString(25);
	}

	@Override
	public String toString() {
		return "ProductStatus [name=" + name + "]";
	}

}
