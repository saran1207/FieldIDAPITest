package com.n4systems.util.chart;



// typically used for horizontal bar chars because their x,y values are flip flopped.

@SuppressWarnings("serial")
public class TransposedChartable implements Chartable<Long> {

	private Chartable<Long> chartable;
	private Long x;
	private Number y;

	public TransposedChartable(Chartable<Long> chartable) {
		this.chartable = chartable;
	}

	@Override
	public Long getLongX() {
		return getX().longValue();
	}
	
	@Override 
	public Long getX() { 
		return chartable.getY().longValue();
	}
	
	@Override 
	public Number getY() { 
		return chartable.getX(); 
	}
	
	@Override 
	public String toJavascriptString() { 	
		return "[" + getLongX() + "," + getY()+"]";
	}		
	

}
