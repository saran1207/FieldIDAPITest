package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;


@SuppressWarnings("serial")
public class ChartData<X> implements Serializable {
	TreeMap<X, Chartable<X>> data = new TreeMap<X, Chartable<X>>();
	
	public ChartData() { 
	}
	
	public ChartData(List<? extends Chartable<X>> data) {
		for (Chartable<X> chartable:data) { 
			add(chartable);
		}
	}

	public ChartData<X> add(final X x, final Number y) {
		return add( new SimpleChartable<X>(x,y) );
	}
	
	public ChartData<X> add(Chartable<X> chartable) { 
		data.put(chartable.getX(), chartable);
		return this;
	}
	
	public String toJavascriptString() {
		StringBuffer buff = new StringBuffer("[");		
		for (Chartable<X> cdp:data.values()) { 
			buff.append(cdp.toJavascriptString());
			buff.append(",");
		}
		buff.append("]");
		return buff.toString();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public X getFirstX() {
		return data.firstEntry().getKey();
	}

	public X getLastX() {
		return data.lastEntry().getKey();
	}

	public Chartable<X> get(X x) {
		return data.get(x);
	}
	
}
