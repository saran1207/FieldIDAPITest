package com.n4systems.util.chart;

@SuppressWarnings("serial")
public class StringChartable extends AbstractChartable<String> {

	private Long xLong;
	private String tooltip;

	public StringChartable(String x, Number y) {
		this(x,y,-1L,null);
	}
	
	public StringChartable(String x, Number y, String tooltip) {
		this(x,y,-1L, tooltip);
	}

	public StringChartable(String x, Number y, Long xLong, String tooltip) {
		super(x, y);
		this.tooltip = tooltip;
		this.xLong = xLong;
	}

	@Override
	public Long getLongX() {
		return xLong;
	}
	
	public String getTooltip() { 
		return tooltip;
	}

}
