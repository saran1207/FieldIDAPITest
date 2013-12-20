package com.n4systems.util.timezone;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
	
	public Country getCountryByName(String name) {
		Country country = null;
		for (Country cntry : countries) {
			if (cntry.getName().equals(name)) {
				country = cntry;
				break;
			}
		}
		return country;
	}
	
	public Country getCountryByFullName(String id) {
		return id==null ? null : getCountryByName(id.substring(0, id.indexOf(':')));
	}
	
	public Region getRegionByFullId(String id) {
		Country country = getCountryByFullName(id);
		return (country == null) ? null : country.getRegionById(id.substring(id.indexOf(':') + 1));
	}

    public List<Region> getAllRegions() {
        SortedSet<Region> regions = Sets.newTreeSet();
        for (Country country:getCountries()) {
            regions.addAll(country.getRegions());
        }
        return Lists.newArrayList(regions);
    }
}
