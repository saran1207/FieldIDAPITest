package com.n4systems.util.chart;


@SuppressWarnings("serial")
public class SimpleChartable<X> implements Chartable<X> {

	private X x;
	private Number y;
	
	public SimpleChartable(X x, Number y) {
		this.x = x;
		this.y = y;
	}

	protected void setX(X x) { 
		this.x = x;
	}
	
	protected void setY(Long y) { 
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
		return "[" + getJavascriptX() + "," + y+"]";
	}

	protected String getJavascriptX() {
		return getX().toString();
	}
	
	
}
