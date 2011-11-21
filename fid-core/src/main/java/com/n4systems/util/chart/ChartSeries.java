package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.n4systems.util.chart.FlotOptions.Lines;


@SuppressWarnings("serial")
public class ChartSeries<X> implements Serializable {
	private static final Logger logger = Logger.getLogger(ChartSeries.class);

	// typical json representation might look like this..    { label: "Foo", data: [ [10, 1], [17, -14], [30, 5] ] }	
	private ChartableMap<X> data = new ChartableMap<X>();
	private String color;
	private String label;
	private Lines lines = new Lines();
	
	// fields NOT included in Json representation.
	private transient ChartManager<X> chartManager = null;
	private transient boolean normalized = false;
	
	public ChartSeries(List<? extends Chartable<X>> data) {
		this(null,data);
	}
	
	public ChartSeries(String label, List<? extends Chartable<X>> data) {
		this.setLabel(label);
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
		return data.isEmpty() ? null : data.firstEntry().getKey();
	}

	public X getLastX() {		
		return data.isEmpty() ? null : data.lastEntry().getKey();
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

	public void updateOptions(FlotOptions<X> options, int index) { 
		getChartManager().updateOptions(this, options, index);		
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setFillColor(String fillColor) {
		this.lines.fillColor = fillColor;
	}

	public String getFillColor() {
		return lines.fillColor;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
	
}	 


