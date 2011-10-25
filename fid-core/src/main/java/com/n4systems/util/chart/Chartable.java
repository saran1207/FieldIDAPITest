package com.n4systems.util.chart;

import java.io.Serializable;


public interface Chartable<X> extends Serializable {
	X getX();
	Number getY();
	String toJavascriptString();  // these two methods are always the same i think...refactor? DD.
	Long getLongX();
}
