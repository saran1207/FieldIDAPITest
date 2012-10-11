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
//    chartWidgetFactory.createWithData('flot57a',[{"data":[[2,2]^]^,"label":"Critical","lines":{}},
//        {"data":[[3,3]^]^,"label":"High","lines":{}},
//        {"data":[[2,2]^]^,"label":"Low","lines":{}},
//        {"data":[[1,1]^]^,"label":"Normal","lines":{}}]^,
//        {"lines":{},"points":{"show":false,"symbol":"circle","radius":3},"yaxis":{"min":0,"tickDecimals":0,"ticks":[["1","Normal","Normal"]^]^,"tickLength":0,"color":"#999999"},"xaxis":{"timeformat":"%m/%y","color":"#999999"},"grid":{"hoverable":true,"clickable":true,"show":true,"borderColor":"#CDCDCD","borderWidth":1},"pan":{},"bars":{"show":true,"barWidth":0.5,"horizontal":true,"lineWidth":0,"clickable":true},
//        "legend":{"margin":4,"noColumns":4},"colors":["#32578B"]^,"tooltipFormat":"\u003cp\u003e{month} {day}, {year}: \u003cb\u003e{y}\u003c/b\u003e\u003c/p\u003e","fieldIdOptions":{"clickable":true,"url":"././savedReport?wdf\u003d844\u0026source\u003dwidget"}});
}
