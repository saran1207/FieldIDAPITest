package com.n4systems.util.chart;

@SuppressWarnings("serial")
public class StringChartable extends AbstractChartable<String> {

	private Long xLong;
	
	public StringChartable(String x, Number y) {
		this(x,y,-1L);
	}

	public StringChartable(String x, Number y, Long xLong) {
		super(x, y);	
		this.xLong = xLong;
	}

	@Override
	public Long getLongX() {
		return xLong;
	}

}
