package com.n4systems.util.chart;

import java.util.Collection;
import java.util.TreeMap;

// this is really just a marker so we can register a json serializer against it. 
//  (as opposed to having a json serializer for *all* treeMaps which could be problematic.
@SuppressWarnings("serial")
public class ChartableMap<X> extends TreeMap<X,Chartable<X>> {
	
	public Collection<Chartable<X>> getChartables() { 
		return  super.values();
	}
}
