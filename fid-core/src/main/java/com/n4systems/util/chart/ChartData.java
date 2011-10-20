package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;


public class ChartData {
	List<FlotDataPoint> set = new ArrayList<FlotDataPoint>();
	
	@Deprecated //testing only
	public ChartData(Number... data) {
		int size = data.length / 2; 
		for (int i = 0 ; i < size; i+=2) { 
			add(data[i].longValue(), data[i+1]);
		}		
	}
	
	public ChartData() { 
	}
	
	public ChartData add(Long x, Number y) {
		set.add(new FlotDataPoint(x, y));
		return this;		
	}
	
	public ChartData add(Chartable chartable) { 
		return add(chartable.getX(), chartable.getY());
	}
	
	public String toJavascriptString() { 
		return set.toString();
	}

	
	
	@SuppressWarnings("serial")
	class FlotDataPoint implements Serializable {

	    private Number y;
	    private Long x;

	    private Calendar xValueAsCalendar;
	    
	    public FlotDataPoint(Long x, Number y) { 
	    	setX(x);
	    	setY(y);
	    }    

	    public FlotDataPoint(Calendar x, Number y) { 
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

	    
	    // CAVEAT : method used to generate javascript string...do NOT change without verifying JS code still functions.
	    // @see toJavascriptString() above
	    @Override
		public String toString() { 
	    	return "[" + x + "," + y + "]"; 
	    }

	}	
	
}
