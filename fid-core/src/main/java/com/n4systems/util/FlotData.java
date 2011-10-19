package com.n4systems.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

public class FlotData implements Serializable {

    private double y;
    private long x;

    private Calendar xValueAsCalendar;
    
    public FlotData(long x, double y) { 
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
        this.xValueAsCalendar = xValueAsCalendar;
        GregorianCalendar lGmtCalendar = new GregorianCalendar(new SimpleTimeZone(0,"GMT+0"));
        lGmtCalendar.setTimeInMillis(this.xValueAsCalendar.getTimeInMillis());
        this.x  = lGmtCalendar.getTimeInMillis();
    }

    public double getY() {
        return y;
    }

    public void setY(double yValue) {
        this.y = yValue;
    }
    
    // CAVEAT : used to generate javascript string...do not change without verifying JS code still functions.
    @Override
	public String toString() { 
    	return "[" + x + "," + y + "]"; 
    }

}