package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;


@SuppressWarnings("serial")
public class ChartData<X> implements Serializable {
	TreeMap<X, Chartable<X>> data = new TreeMap<X, Chartable<X>>();
	private String label;
	
	public ChartData() { 
	}
	
	public ChartData(String label) {
		this.label = label;
	}
	
	public ChartData(String label, List<? extends Chartable<X>> data) {
		this.label = label;
		add(data);
	}
	
	public ChartData<X> add(List<? extends Chartable<X>> data) { 		
		for (Chartable<X> chartable:data) { 
			add(chartable);
		}
		return this;
	}

	public ChartData<X> add(Chartable<X> chartable) { 
		data.put(chartable.getX(), chartable);
		return this;
	}
	
	//	e.g. {data:[[0,12],[87,9.3]], label:'hello'}	
	public String toJavascriptString() {
		StringBuffer buff = new StringBuffer("{data:[");
		
		for (Chartable<X> cdp:data.values()) { 
			buff.append(cdp.toJavascriptString());
			buff.append(",");
		}
		buff.append("]");
		buff.append(label!=null ? ", label:'" + label + "'}" : "}");				
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
