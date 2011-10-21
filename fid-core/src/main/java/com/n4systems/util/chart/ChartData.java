package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TreeMap;


@SuppressWarnings("serial")
public class ChartData implements Serializable {
	TreeMap<Long, ChartDataPoint> data = new TreeMap<Long, ChartDataPoint>();
	
	@Deprecated //testing only
	public ChartData(Number... data) {		
		int size = data.length / 2; 
		for (int i = 0 ; i < size; i+=2) { 
			add(data[i].longValue(), data[i+1]);
		}		
	}
	
	public ChartData() { 
	}
	
	public ChartData(List<? extends Chartable> data) {
		for (Chartable chartable:data) { 
			add(chartable);
		}
	}

	public ChartData add(Long x, Number y) {
		data.put(x, new ChartDataPoint(x, y));
		return this;		
	}
	
	public ChartData add(Chartable chartable) { 
		return add(chartable.getX(), chartable.getY());
	}
	
	public String toJavascriptString() {
		StringBuffer buff = new StringBuffer("[");		
		for (ChartDataPoint cdp:data.values()) { 
			buff.append(cdp.toJavascriptString());
			buff.append(",");
		}
		buff.append("]");
		return buff.toString();
	}

	// TODO DD : implement generics for ChartData so i know if they are dates or not.
	public ChartData normalize(ChartDataPeriod period, long value) {
		return this;
//		if (data.isEmpty()) {		
//			return this;
//		}
//		Calendar start = Calendar.getInstance();
//		start.setTimeInMillis(data.get(data.firstKey()).getX());
//		System.out.println(new Date(start.getTimeInMillis()));
//		
//		long end = data.get(data.lastKey()).getX();
//		System.out.println(new Date(end));
//		for (long time=start; time<end; time+=period.getMsDelta()) {
//			System.out.println(new Date(time));
//
//			ChartDataPoint cdp = data.get(time);
//			if (cdp==null) { 
//				add(time,0);
//			}
//		}
//		return this;
	}

	public ChartData normalize(ChartDataPeriod period) {
		return normalize(period,0);
	}
	
	

	
	class ChartDataPoint implements Serializable, Comparable<ChartDataPoint> {

	    private Number y;
	    private Long x;

	    private Calendar xValueAsCalendar;
	    
	    public ChartDataPoint(Long x, Number y) { 
	    	setX(x);
	    	setY(y);
	    }    

		public ChartDataPoint(Calendar x, Number y) { 
	    	setX(x);
	    	setY(y);
	    }    

	    public long getX() {
	        return x;
	    }

	    public void setX(long xValue) {
	        this.x = xValue;
	    }
	   
	    public void setX(Calendar xValueAsCalendar) {	    	
	    	// NOTE : Flot JS charting library has different interpretation of time.  they are all GMT+0.
	        this.xValueAsCalendar = xValueAsCalendar;
	        GregorianCalendar lGmtCalendar = new GregorianCalendar(new SimpleTimeZone(0,"GMT+0"));
	        lGmtCalendar.setTimeInMillis(this.xValueAsCalendar.getTimeInMillis());
	        this.x  = lGmtCalendar.getTimeInMillis();
	    }

	    public Number getY() {
	        return y;
	    }

	    public void setY(Number yValue) {
	        this.y = yValue;
	    }

	    
	    public String toJavascriptString() {
	    	return "[" + x + "," + y + "]"; 
	    }

		@Override
		public int compareTo(ChartDataPoint o) {
			return (o == null) ? -1 : (int)(getX() - o.getX());			
		}

	}

}
