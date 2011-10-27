package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class ChartData<X> implements Serializable {
	private static final Logger logger = Logger.getLogger(ChartData.class);
	
	private TreeMap<X, Chartable<X>> data = new TreeMap<X, Chartable<X>>();
	private String label;
	private ChartManager<X> chartManager = null;
	
	public ChartData() { 
	}
	
	public ChartData<X> add(List<? extends Chartable<X>> data) {
		if (data.size()>500) {
			// just to warn of potential performance problems. 
			logger.warn("Very large dataset used for chart : (" + data.size() + ")" );
		}		
		int i = 0;
		for (Chartable<X> chartable:data) { 
			add(getChartManager().preprocess(chartable, i++));
		}
		return this;
	}
	
	public ChartData<X> add(Chartable<X> chartable) {
		data.put(chartable.getX(), chartable);
		trim();
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

	public ChartData<X> withChartManager(ChartManager<X> chartManager) {
		this.chartManager = chartManager;  // advised not to change this after you build & use it.   set only once.
		return this;
	}
	
	private void trim() {		
		// TODO DD : optimization... remove data just in case you get a huge result set.  
	//	int count = data.size()-getChartManager().getLimit();  // remove the first N entries if data is > limit.
	}

	public Long getMinX() {
		if (data.size()==0){ 
			return null; 
		}
		return getChartManager().getMinX(this);
	}
	
	public Long getPanMin() {
		if (data.size()==0){ 
			return null; 
		}
		return getChartManager().getPanMin(this); 
	}
	
	public Long getPanMax() {
		if (data.size()==0){ 
			return null; 
		}
		return getChartManager().getPanMax(this); 
	}

	private ChartManager<X> getChartManager() {
		if (chartManager==null) { 
			chartManager=new SimpleChartManager<X>();
		}
		return chartManager;
	}

	public Chartable<X> getFirstEntry() {
		return data.size()>0 ? data.firstEntry().getValue() : null;
	}

	public Chartable<X> getLastEntry() {
		return data.size()>0 ? data.lastEntry().getValue() : null;
	}

	public int size() {
		return data.size();
	}

	public Set<Entry<X, Chartable<X>>> getEntrySet() {
		return data.entrySet();
	}
	
}	 


