package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class ChartData<X> implements Serializable {
	private static final Logger logger = Logger.getLogger(ChartData.class);
	
	private static final int LIMIT_FOR_GRANULAR_DATA = 200;	// put a reasonable limit on the number of points per graph... 
	
	TreeMap<X, Chartable<X>> data = new TreeMap<X, Chartable<X>>();
	private String label;
	// TODO DD : refactor out of here...these are only applicable for Calendar based charts. 
	private transient ChartDataGranularity granularity;
	private transient Integer dataLimit;
	
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
		trim();
		return this;
	}
	
	private int limitedSize() {
		return dataLimit != null ? dataLimit : 
			granularity != null ? LIMIT_FOR_GRANULAR_DATA : 
				Integer.MAX_VALUE;
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

	public ChartData<X> withGranularity(ChartDataGranularity granularity) {
		// trim size here...max 100 pts...??
		this.granularity = granularity;
		trim();
		return this;
	}

	private void trim() {		
		int count = data.size()-limitedSize();  // remove the first N entries if data is > limit.
		// TODO DD : optimization... remove data just in case you pass a huge result set.  
	}

	public ChartData<X> withDataLimit(int dataLimit) {
		this.dataLimit = dataLimit;
		trim();
		return this;
	}

	public Long getMinX() {
		if (data.size()==0){ 
			return null; 
		}
		if (granularity!=null) { 
			Long last = data.lastEntry().getValue().getLongX();
			return last-granularity.delta();
		} else { 
			return data.firstEntry().getValue().getLongX();
		}		
	}
	
	public Long getPanMin() {
		return data.size()>0 ? 
				data.firstEntry().getValue().getLongX() : null; 
	}
	
	public Long getPanMax() {
		return data.size()>0 ? 
				data.lastEntry().getValue().getLongX() : null; 
	}
	
}	 


