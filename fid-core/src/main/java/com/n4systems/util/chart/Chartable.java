package com.n4systems.util.chart;

import java.io.Serializable;


public interface Chartable<X> extends Serializable {
	X getX();
	Number getY();
	String toJavascriptString();
}
