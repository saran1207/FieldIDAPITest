package com.n4systems.util.timezone;

import java.util.TimeZone;

import com.n4systems.model.api.Listable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Region")
public class Region implements Comparable<Region>, Listable<String>  {
	
	@XStreamAlias("name")
   	@XStreamAsAttribute
	private String name;
	
	@XStreamAlias("zone")
   	@XStreamAsAttribute
	private String timeZoneId;
	
	public Region() {
		this(null, null);
	}
	
	public Region(String name, String timeZoneId) {
		this.name = name;
		this.timeZoneId = timeZoneId;
	}
	
	public int compareTo(Region other) {
		return name.compareTo(other.getName());
	}
	
	public String getDisplayName() {
		return name;
	}
	
	@Override
	public String toString() {
		return String.format("%1$s (%2$s)", name, timeZoneId);
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
	
	public String getTimeZoneId() {
		return timeZoneId;
	}
	
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	
	public TimeZone getTimeZone() { 
		return TimeZone.getTimeZone(timeZoneId);
	}

}
