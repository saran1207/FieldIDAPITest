package com.n4systems.graphing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChartSeries implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String yAxisName;
	private String yAxisUnit;
	private String peakName;
	private Double peak;
	
	private List<ChartPoint2D> points = new ArrayList<ChartPoint2D>(); 

	public ChartSeries() {}
	
	public ChartSeries(String yAxisName, String yAxisUnit) {
		this.yAxisName = yAxisName;
		this.yAxisUnit = yAxisUnit;
	}
	
	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}
	
	public String getYAxisUnit() {
		return yAxisUnit;
	}

	public void setYAxisUnit(String axisUnit) {
		yAxisUnit = axisUnit;
	}

	public String getPeakName() {
		return peakName;
	}

	public void setPeakName(String peakName) {
		this.peakName = peakName;
	}

	public Double getPeak() {
		return peak;
	}

	public void setPeak(Double peak) {
		this.peak = peak;
	}
	
	public List<ChartPoint2D> getPoints() {
		return points;
	}

	public void setPoints(List<ChartPoint2D> points) {
		this.points = points;
	}

	public String getYAxisLabel() {
		String display = yAxisName;
		if(yAxisUnit != null) {
			display += " (" + yAxisUnit + ")";
		}
		return display;
	}
	
	public double calculatePeakY() {
		double peakY = Double.NEGATIVE_INFINITY;
		
		for(ChartPoint2D point: points) {
			if(point.getY() > peakY) {
				peakY = point.getY();
			}
		}
		
		return peakY;
	}
	
	public double calculatePeakX() {
		double peakX = Double.NEGATIVE_INFINITY;
		
		for(ChartPoint2D point: points) {
			if(point.getX() > peakX) {
				peakX = point.getX();
			}
		}
		
		return peakX;
	}
	
	@Override
	public String toString() {
		String debugString = 
			"yAxisName: " + yAxisName + "\n\t" + 
			"yAxisUnit: " + yAxisUnit + "\n\t" + 
			"peakName: " + peakName + "\n\t" +
			"peak: " + peak + "\n";
		
		for(ChartPoint2D point: points) {
			debugString += "\t\tPoint: " + point + "\n";
		}
		
		return debugString;
	}

}
