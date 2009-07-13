package com.n4systems.graphing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class Chart implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String displayName;
	private String xAxisName;
	private String xAxisUnit;
	private boolean usingPeakMarkers = false;
	private boolean usingPeakArrows = true;
	private boolean usingPeakDots = true;
	private boolean legendOnSide = false;
	private boolean showChartName = false;
	private int height = 200;
	private int width = 550;
	
	private List<ChartSeries> series = new ArrayList<ChartSeries>();
	
	public Chart() {}
	
	public Chart(String displayName, String xAxisName, String xAxisUnit) {
		this();
		this.displayName = displayName;
		this.xAxisName = xAxisName;
		this.xAxisUnit = xAxisUnit;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	public String getXAxisUnit() {
		return xAxisUnit;
	}

	public void setXAxisUnit(String axisUnit) {
		xAxisUnit = axisUnit;
	}

	public List<ChartSeries> getSeries() {
		return series;
	}

	public void setSeries(List<ChartSeries> series) {
		this.series = series;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public String getXAxisLabel() {
		String display = xAxisName;
		if(xAxisUnit != null) {
			display += " (" + xAxisUnit + ")";
		}
		return display;
	}

	public boolean isUsingPeakMarkers() {
		return usingPeakMarkers;
	}

	public void setUsingPeakMarkers(boolean usingPeakMarkers) {
		this.usingPeakMarkers = usingPeakMarkers;
	}

	public boolean isUsingPeakArrows() {
		return usingPeakArrows;
	}

	public void setUsingPeakArrows(boolean usingPeakArrows) {
		this.usingPeakArrows = usingPeakArrows;
	}

	public boolean isUsingPeakDots() {
		return usingPeakDots;
	}

	public void setUsingPeakDots(boolean usingPeakDots) {
		this.usingPeakDots = usingPeakDots;
	}

	public boolean isLegendOnSide() {
		return legendOnSide;
	}

	public void setLegendOnSide(boolean chartOnSide) {
		this.legendOnSide = chartOnSide;
	}

	public boolean isShowChartName() {
		return showChartName;
	}

	public void setShowChartName(boolean showChartName) {
		this.showChartName = showChartName;
	}

	@Override
	public String toString() {
		String debugString = 
			"displayName: " + displayName + "\n" + 
			"xAxisName: " + xAxisName + "\n" + 
			"xAxisUnit: " + xAxisUnit + "\n" + 
			"height: " + height + "\n" +
			"width: " + width + "\n";
		
		for(ChartSeries s: series) {
			debugString += "\tSeries: " + s + "\n";
		}
		
		return debugString;
	}
	
	public static Chart newInstance() {
		Chart chart = new Chart();
		
		chart.setWidth(ConfigContext.getCurrentContext().getInteger(ConfigEntry.GRAPHING_CHART_SIZE_X));
		chart.setHeight(ConfigContext.getCurrentContext().getInteger(ConfigEntry.GRAPHING_CHART_SIZE_Y));
		chart.setUsingPeakArrows(ConfigContext.getCurrentContext().getBoolean(ConfigEntry.GRAPHING_CHART_PEAK_ARROWS));
		chart.setUsingPeakDots(ConfigContext.getCurrentContext().getBoolean(ConfigEntry.GRAPHING_CHART_PEAK_DOTS));
		chart.setUsingPeakMarkers(ConfigContext.getCurrentContext().getBoolean(ConfigEntry.GRAPHING_CHART_PEAK_MARKERS));

		return chart;
	}
}
