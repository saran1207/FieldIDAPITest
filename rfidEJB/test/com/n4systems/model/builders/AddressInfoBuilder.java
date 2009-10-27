package com.n4systems.model.builders;

import com.n4systems.model.AddressInfo;
import com.n4systems.testutils.TestHelper;

public class AddressInfoBuilder extends BaseBuilder<AddressInfo> {

	public AddressInfoBuilder() {
		super();
	}
	
	public static AddressInfoBuilder anAddress() {
		return new AddressInfoBuilder();
	}
	
	@Override
	public AddressInfo build() {
		AddressInfo address = new AddressInfo();
		address.setStreetAddress(TestHelper.randomString());
		address.setCity(TestHelper.randomString());
		address.setState(TestHelper.randomString());
		address.setZip(TestHelper.randomString());
		address.setPhone1(TestHelper.randomString());
		address.setPhone2(TestHelper.randomString());
		address.setFax1(TestHelper.randomString());
		return address;
	}

}
