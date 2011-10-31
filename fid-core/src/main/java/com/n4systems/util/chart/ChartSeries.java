package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class ChartSeries<X> implements Serializable {
	private static final Logger logger = Logger.getLogger(ChartSeries.class);

	// typical json representation might look like this..    { label: "Foo", data: [ [10, 1], [17, -14], [30, 5] ] }	
	private ChartableMap<X> data = new ChartableMap<X>();
//    lines: specific lines options       
//    bars: specific bars options          these are other FLOT options i haven't included yet.
//    points: specific points options
	public String color;
	private Integer xaxis;
	private Integer yaxis;
	private Boolean clickable;
	private Boolean hoverable;
	private Integer shadowSize;		
	private String label;
	
	// fields NOT included in Json representation.
	private transient ChartManager<X> chartManager = null;
	private transient boolean normalized = false;
	
	public ChartSeries(List<? extends Chartable<X>> data) {
		this(null,data);
	}
	
	public ChartSeries(String label, List<? extends Chartable<X>> data) {
		this.label = label;
		add(data);
	}
	
	/*pkg protected */ChartSeries<X> add(List<? extends Chartable<X>> data) {
		for (Chartable<X> chartable:data) { 
			add(chartable);
		}
		normalize();
		return this;
	}
	
	/*pkg protected */ChartSeries<X> add(Chartable<X> chartable) {
		data.put(chartable.getX(), chartable);
		return this;
	}	

	private void normalize() {
		if (!normalized) {
			normalized = true;
			getChartManager().normalize(this);
		}
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

	public ChartSeries<X> withChartManager(ChartManager<X> chartManager) {
		this.chartManager = chartManager;  // advised not to change this after you build & use it.   set only once.
		normalized = false;
		normalize();
		return this;
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

	public Long sumX() {
		long sum = 0;
		for (Chartable<X> chartable:data.getChartables()) { 
			sum += chartable.getLongX();
		}
		return sum;
	}

	public Double sumY() {
		Double sum = 0.0;
		for (Chartable<X> chartable:data.getChartables()) { 
			sum += chartable.getY().doubleValue();
		}
		return sum;
	}

	public void remove(List<Chartable<X>> values) {
		for (Chartable<X> chartable:values) {
			data.remove(chartable.getX());
		}
	}

}	 


