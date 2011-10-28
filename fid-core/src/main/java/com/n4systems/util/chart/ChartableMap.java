package com.n4systems.util.chart;

import java.util.Collection;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class ChartableMap<X> extends TreeMap<X,Chartable<X>> {
	
	public Collection<Chartable<X>> getChartables() { 
		return  super.values();
	}
}
