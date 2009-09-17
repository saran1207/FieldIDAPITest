package com.n4systems.util.timezone;

import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.model.api.Listable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Country")
public class Country implements Comparable<Country>, Listable<String> {
	
	@XStreamAlias("name")
   	@XStreamAsAttribute
	private String name;
	
	@XStreamAlias("code")
	@XStreamAsAttribute
	private String code;
	
	@XStreamImplicit(itemFieldName="Region")
	private TreeSet<Region> regions = new TreeSet<Region>();
	
	public Country() {}
	
	public Country(String name) {
		this.name = name;
	}

	public int compareTo(Country other) {
		return name.compareTo(other.getName());
	}
	
	public String getDisplayName() {
		return name;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(name);
		
		for (Region region: regions) {
			str.append("\n\t").append(region.toString().replace("\n", "\n\t"));
		}
		
		return str.toString();
	}
	
	public String getId() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SortedSet<Region> getRegions() {
		return regions;
	}

	public void setRegions(TreeSet<Region> regions) {
		this.regions = regions;
	}
	
	public Region getRegionById(String regionId) {
		Region region = null;
		for (Region reg: regions) {
			if (reg.getId().equals(regionId)) {
				region = reg;
				break;
			}
		}
		
		return region;
	}
	
	public String getFullId(Region region) {
		return getId() + ":" + region.getId();
	}
}
