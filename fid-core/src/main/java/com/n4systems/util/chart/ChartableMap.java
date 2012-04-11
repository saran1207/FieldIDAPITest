package com.n4systems.util.chart;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

// this is really just a marker so we can register a json serializer against it. 
//  (as opposed to having a json serializer for *all* treeMaps which could be problematic.
public class ChartableMap<X> extends TreeMap<X,Chartable<X>> {

    public ChartableMap(Comparator<X> comparator) {
        super(comparator);
    }

    public ChartableMap() {
        super();
    }

    public Collection<Chartable<X>> getChartables() { 
		return  super.values();
	}
}
