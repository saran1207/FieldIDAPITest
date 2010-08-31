package com.n4systems.model.builders;

import com.n4systems.model.AddressInfo;

public class AddressInfoBuilder extends BaseBuilder<AddressInfo> {

	private final String streetAddress;
	private final String city;
	private final String state;
	private final String country;
	private final String zip;
	private final String phone1;
	private final String phone2;
	private final String fax1;
	
	public static AddressInfoBuilder anAddress() {
		return new AddressInfoBuilder("", "", "", "", "", "", "", "");
	}
	
	public static AddressInfoBuilder anAddressWithTestData() {
		return new AddressInfoBuilder("100 Some St", "Toronto", "Ontario", "Canada", "M5V 2N3", "1-800-123-4567", "416-614-4141", "555-555-5555");
	}
	
	public AddressInfoBuilder(String streetAddress, String city, String state, String country, String zip, String phone1, String phone2, String fax1) {
		super();
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zip = zip;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.fax1 = fax1;
	}
	
	public AddressInfoBuilder streetAddress(String streetAddress) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder city(String city) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder state(String state) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder country(String country) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder zip(String zip) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder phone1(String phone1) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder phone2(String phone2) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}
	
	public AddressInfoBuilder fax1(String fax1) {
		return new AddressInfoBuilder(streetAddress, city, state, country, zip, phone1, phone2, fax1);
	}

	@Override
	public AddressInfo build() {
		AddressInfo address = new AddressInfo();
		address.setId(id);
		address.setStreetAddress(streetAddress);
		address.setCity(city);
		address.setState(state);
		address.setCountry(country);
		address.setZip(zip);
		address.setPhone1(phone1);
		address.setPhone2(phone2);
		address.setFax1(fax1);
		return address;
	}

}
