package com.n4systems.fieldid.actions.subscriptions.view.model;

import java.util.SortedSet;

import com.n4systems.model.api.Listable;
import com.n4systems.subscription.AddressInfo;
import com.n4systems.util.timezone.TimeZoneSelectionHelper;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class AddressInfoDecorator extends AddressInfo {

	private AddressInfo delegateAddress;
	
	
	public AddressInfoDecorator() {
		this(new AddressInfo());
	}
	
	public AddressInfoDecorator(AddressInfo address) {
		this.delegateAddress = address;
	}

	
	
	
	public String getAddressLine1() {
		return delegateAddress.getAddressLine1();
	}

	public String getAddressLine2() {
		return delegateAddress.getAddressLine2();
	}

	public String getCity() {
		return delegateAddress.getCity();
	}

	public String getCountry() {
		return delegateAddress.getCountry();
	}

	public String getPostal() {
		return delegateAddress.getPostal();
	}

	public String getState() {
		return delegateAddress.getState();
	}

	@RequiredStringValidator(message="", key="error.address_required")
	public void setAddressLine1(String addressLine1) {
		delegateAddress.setAddressLine1(addressLine1);
	}

	public void setAddressLine2(String addressLine2) {
		delegateAddress.setAddressLine2(addressLine2);
	}

	@RequiredStringValidator(message="", key="error.city_required")
	public void setCity(String city) {
		delegateAddress.setCity(city);
	}

	@RequiredStringValidator(message="", key="error.country_required")
	public void setCountry(String country) {
		delegateAddress.setCountry(country);
		delegateAddress.setCountryFullName(findFullName(country));
	}

	private String findFullName(String country) {
		if (country == null || country.trim().isEmpty()) {
			return null;
		}
		for (Listable<String> countryEntry : getCountries()) {
			if (countryEntry.getId().equalsIgnoreCase(country.trim()) ) {
				return countryEntry.getDisplayName();
			}
		}
		return null;
	}

	@RequiredStringValidator(message="", key="error.zip_code_required")
	public void setPostal(String postal) {
		delegateAddress.setPostal(postal);
	}

	@RequiredStringValidator(message="", key="error.state_required")
	public void setState(String state) {
		delegateAddress.setState(state);
	}

	public AddressInfo getDelegateAddress() {
		return delegateAddress;
	}
	
	public SortedSet<? extends Listable<String>> getCountries() {
		return TimeZoneSelectionHelper.getCountries();
	}
}
