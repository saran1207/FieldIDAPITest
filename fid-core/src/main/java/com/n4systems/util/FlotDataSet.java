package com.n4systems.util;

import java.util.ArrayList;
import java.util.List;

public class FlotDataSet {
	List<FlotData> set = new ArrayList<FlotData>();
	
	@Deprecated //testing only
	public FlotDataSet(List<FlotData> data) { 
		this.set = data;
	}
	
	public FlotDataSet() { 
	}
	
	public FlotDataSet add(FlotData data) {
		set.add(data);
		return this;		
	}	
	
	public String toJavascriptString() { 
		return set.toString();
	}
	
}
