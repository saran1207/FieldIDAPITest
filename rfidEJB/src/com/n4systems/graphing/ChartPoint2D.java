package com.n4systems.graphing;

import java.io.Serializable;

public class ChartPoint2D implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double x;
	private double y;
	
	public ChartPoint2D() {}
	
	public ChartPoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
