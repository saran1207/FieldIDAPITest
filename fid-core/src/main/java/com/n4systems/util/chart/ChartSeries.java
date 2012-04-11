package com.n4systems.util.chart;

import com.n4systems.util.chart.FlotOptions.Lines;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public class ChartSeries<X extends Comparable> implements Serializable {
	private static final Logger logger = Logger.getLogger(ChartSeries.class);

	// typical json representation might look like this..    { label: "Foo", data: [ [10, 1], [17, -14], [30, 5] ] }	
	private ChartableMap<X> data;
	private String color;
	private String label;
	private Object id;
	private Lines lines = new Lines();
	
	public ChartSeries(Object id, String label, List<? extends Chartable<X>> chartables) {
        this.data = createChartableMap();
		this.id = id;
		this.setLabel(label);
		add(chartables);
	}

    protected ChartableMap<X> createChartableMap() {
        // override if you want to add comparator or different flavour of map.
        return new ChartableMap<X>();
    }

    public ChartSeries(List<? extends Chartable<X>> data) {
		this(null, null, data);
	}
	
	public ChartSeries(String label, List<? extends Chartable<X>> data) {
		this(null, label, data);
	}
	
	/*pkg protected */ChartSeries<X> add(List<? extends Chartable<X>> data) {
		for (Chartable<X> chartable:data) { 
			add(chartable);
		}
		return this;
	}
	
	/*pkg protected */ChartSeries<X> add(Chartable<X> chartable) {
		data.put(chartable.getX(), chartable);
		return this;
	}	

	public boolean isEmpty() {
		return data.isEmpty();
	}

    public Long getMinLong() {
        Chartable<X> first = getFirstEntry();
        return first==null ? null : first.getLongX();
    }

    public X getFirstX() {
		return data.isEmpty() ? null : data.firstEntry().getKey();
	}

	public X getLastX() {		
		return data.isEmpty() ? null : data.lastEntry().getKey();
	}

	public Chartable<X> getFirstEntry() {
		return data.size()>0 ? data.firstEntry().getValue() : null;
	}

	public Chartable<X> getLastEntry() {
		return data.size()>0 ? data.lastEntry().getValue() : null;
	}

	public Chartable<X> get(X x) {
		return data.get(x);
	}

	public int size() {
		return data.size();
	}

	public Set<Entry<X, Chartable<X>>> getEntrySet() {
		return data.entrySet();
	}

    public Collection<Chartable<X>> values() {
        return data.values();
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

	public void removeAll(List<Chartable<X>> values) {
		for (Chartable<X> chartable:values) {
			data.remove(chartable.getX());
		}
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

	@Override
	public String toString() { 
		return data.toString();
	}

	public Object getId() {
		return id;
	}	
}	 


