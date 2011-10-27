package com.n4systems.util.chart;



@SuppressWarnings("serial")
public class NumericChartable<X extends Number> extends AbstractChartable<X> {

	public NumericChartable(X x, Number y) {
		super(x,y);
	}

	@Override
	public Long getLongX() {
		return getX().longValue();
	}

}
