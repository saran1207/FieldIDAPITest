package com.n4systems.util.timezone;

import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.model.api.Listable;

public class TimeZoneSelectionHelper {

	public static Country defaultCountry() {
		return getCountryById("United States");
	}
	
	public static SortedSet<? extends Listable<String>> getCountries() {
		return CountryList.getInstance().getCountries();
	}

	public static SortedSet<? extends Listable<String>> getTimeZones(Country country) {
		return (country != null) ? country.getRegions() : new TreeSet<Listable<String>>();
	}
	
	public static Country getCountryById(String countryId) {
		return CountryList.getInstance().getCountryById(countryId);
	}	
}
