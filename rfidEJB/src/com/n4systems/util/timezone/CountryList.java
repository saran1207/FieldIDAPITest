package com.n4systems.util.timezone;

import java.io.InputStream;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("CountryList")
public class CountryList implements Iterable<Country> {
	
	@XStreamOmitField
	private static CountryList self;
	
	public static synchronized CountryList getInstance() {
		if (self == null) {
			 self = loadFromResource();
		}
		return self;
	}
	
	private static CountryList loadFromResource() {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(CountryList.class);
		
		InputStream dbStream = CountryList.class.getResourceAsStream(CountryList.class.getSimpleName() + ".xml");
		return (CountryList)xstream.fromXML(dbStream);
	}
	
	@XStreamImplicit(itemFieldName="Country")
	private final TreeSet<Country> countries = new TreeSet<Country>();
	
	private CountryList() {}
	
	public SortedSet<Country> getCountries() {
		return new TreeSet<Country>(countries);
	}

	public synchronized void setCountries(SortedSet<Country> countries) {
		this.countries.clear();
		this.countries.addAll(countries);
	}
	
	public synchronized void addCountry(Country country) {
		countries.add(country);
	}

	public Iterator<Country> iterator() {
		return countries.iterator();
	}
	
	public Country getCountryById(String id) {
		Country country = null;
		for (Country cntry: countries) {
			if (cntry.getId().equals(id)) {
				country = cntry;
				break;
			}
		}
		return country;
	}
	
	public Country getCountryByFullId(String id) {
		return getCountryById(id.substring(0, id.indexOf(':')));
	}
	
	public Region getRegionByFullId(String id) {
		Country country = getCountryByFullId(id);
		return (country == null) ? null : country.getRegionById(id.substring(id.indexOf(':') + 1));
	}
}
