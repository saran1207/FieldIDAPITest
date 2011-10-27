package com.n4systems.util.chart;


@SuppressWarnings("serial")
public abstract class AbstractChartable<X> implements Chartable<X> {

	private X x;
	private Number y;
	
	public AbstractChartable(X x, Number y) {
		this.x = x;
		this.y = y;
	}

	@Override 
	public X getX() { 
		return x;
	}
	
	@Override 
	public Number getY() { 
		return y; 
	}
	
	@Override 
	public String toJavascriptString() { 	
		return "[" + getLongX() + "," + y+"]";
	}	
	
}
