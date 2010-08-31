package com.n4systems.graphing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Drawable;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.VerticalAlignment;


public class GraphFactory {
	
	/**
	 * sets basic rendering defaults for the jfreechart object
	 */
	private static void setChartDefaults(JFreeChart jfChart) {
		jfChart.setAntiAlias(true);
		jfChart.setTextAntiAlias(true);
		jfChart.setBackgroundPaint(Color.white);
		jfChart.setBorderPaint(Color.black);
	}
	
	
	// XXX - these methods should not be static
	/**
	 * Creates the chart and returns a png image in a byte array
	 */
	public static byte[] createPNGChartImage(Chart chart) throws IOException {
		
		// prune empty series before sending to JFreeChart 
		pruneEmptySeries(chart);
		
		JFreeChart jfChart = generateChart(chart);
		
		BufferedImage chartImage = jfChart.createBufferedImage(chart.getWidth(), chart.getHeight());
		
		return ChartUtilities.encodeAsPNG(chartImage);
	}
	
	/**
	 * Removes empty series from the chart.  Needed since JFreeChart does not deal well with empty series.
	 * @param chart The Chart
	 */
	private static void pruneEmptySeries(Chart chart) {
		List<ChartSeries> emptySeries = new ArrayList<ChartSeries>();
		
		// find any series with null or empty points list 
		for(ChartSeries series: chart.getSeries()) {
			if (series.getPoints() == null || series.getPoints().isEmpty()) {
				emptySeries.add(series);
			}
		}
		
		// remove these series from the chart.
		for (ChartSeries series: emptySeries) {
			chart.getSeries().remove(series);
		}
	}
	
	/**
	 * main class to generate JFreeChart objects.  This creates the dataset and produces the chart.
	 */
	public static JFreeChart generateChart(Chart chart) {
		XYSeriesCollection dataset = new XYSeriesCollection(); 
		
		// create each series and add it to the dataset
		XYSeries xySeries;
		for(ChartSeries series: chart.getSeries()) {
			xySeries = processSeries(series);
			dataset.addSeries(xySeries);
		}
		
		// create the chart object
		return createJFreeChart(chart, dataset);
	}
	
	/**
	 * generates the JFreeChart object and sets up the legend, markers and annotations
	 */
	private static JFreeChart createJFreeChart(Chart chart, XYDataset dataset) {
		
		String ylabel= getSeriesLabels(chart.getSeries());
		
		
		boolean sideLegend = false;
		boolean bottomLegend = false;
		// charts with a single series do not get a legend
		if(chart.getSeries().size() > 1) {
			// decide where to put the legend if it has one
			if(chart.isLegendOnSide()) {
				sideLegend = true;
				bottomLegend = false;
			} else {
				sideLegend = false;
				bottomLegend = true;
			}
		}
		
		String chartName = (chart.isShowChartName()) ? chart.getDisplayName() : null;
		
		JFreeChart jfChart = ChartFactory.createXYLineChart(chartName, chart.getXAxisLabel(), ylabel, dataset, PlotOrientation.VERTICAL, bottomLegend, false, false);

		// get the plot to generate legend, annotations and markers
		XYPlot plot = jfChart.getXYPlot();

		// add effects such as markers and annotations
		generateMarkers(plot, chart);
		generateAnnotations(plot, chart);
		
		if(sideLegend) {
			jfChart.addLegend(generateLegend(plot));
		}
		
		setChartDefaults(jfChart);
		
		return jfChart;
	}
	
	/**
	 * finds the item number of the first peak item in the series
	 */
	private static int getPeakSeriesItem(XYDataset dataset, int series) {
		int peakItem = 0;
		double peakYValue = 0;
		for (int item = 0; item < dataset.getItemCount(series); item++) {
			//compare the new Y value to our current peak.  update if larger.
			if (dataset.getYValue(series, item) > peakYValue) {
				peakYValue = dataset.getYValue(series, item);
				peakItem = item;
			}
		}
		
		return peakItem;
	}
	
	/**
	 * generates the dot and pointer annotations
	 */
	private static void generateAnnotations(XYPlot plot, final Chart chart) {
        int peakItem;
        double xValue, yValue;
        Paint paint;
        
        XYDataset dataset = plot.getDataset();
        
		for (int series = 0; series < dataset.getSeriesCount(); series++) {
			
			peakItem = getPeakSeriesItem(plot.getDataset(), series);
			xValue = dataset.getXValue(series, peakItem);
			yValue = dataset.getYValue(series, peakItem);
			
			paint = Color.BLACK;
			if(chart.isUsingPeakDots()) {
				plot.addAnnotation(createPeakDot(xValue, yValue, paint));
			}
			
			if(chart.isUsingPeakArrows()) {
				plot.addAnnotation(createPeakArrow(xValue, yValue, paint));
			}
		}
	}
	
	/**
	 * generates a marker for each series
	 */
	private static void generateMarkers(XYPlot plot, final Chart chart) {
		/*
		 * These markers aare generated inside an annotation because we get the Paint from the series.
		 * when done outside the annotation, the Paint is null.  I'm sure there's a better way to do this
		 * but JFreeChart is confusing and poorly documented ..... oh and stupid ... 
		 */
		if(chart.isUsingPeakMarkers()) {
			plot.addAnnotation(new XYAnnotation() {
				
				public void draw(Graphics2D graphics, XYPlot plot, Rectangle2D rect, ValueAxis domain, ValueAxis range, int index, PlotRenderingInfo info) {
			        int peakItem;
			        double yValue;
			        Paint paint;
			        
			        XYDataset dataset = plot.getDataset();
			        
					for (int series = 0; series < dataset.getSeriesCount(); series++) {
						
						peakItem = getPeakSeriesItem(plot.getDataset(), series);
						yValue = dataset.getYValue(series, peakItem);
					
						paint = plot.getRenderer().getSeriesPaint(series);		
						plot.addRangeMarker(createMarker(yValue, paint));
							
						 
					}
				}
			});
		
		}
		
	}
	
	/**
	 * Creates a horizontal marker at the yValue
	 */
	private static Marker createMarker(double y, Paint paint) {
		Marker marker = new ValueMarker(y);
		
		//this generates the label slightly above the line and a little right of the left graph border
		marker.setLabel(Double.toString(y));
		marker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		marker.setLabelOffset(new RectangleInsets(5, 50, 0, 0));
		
		// we set the color of the marker to the same as the series, this is the part that breaks when
		// not inside the draw method ... 
		marker.setLabelPaint(paint);
		marker.setPaint(paint);
		
		// this creates the dashed line.  You can set the segment length by changing the dashSegs float value
		float[] dashSegs = { 5.0f };
		marker.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dashSegs, 0));
		
		return marker;
	}

	/**
	 * creates a peak dot at the x/y value
	 */
	private static XYAnnotation createPeakDot(double x, double y, final Paint paint) {

		// places a black dot at the peak point
        XYAnnotation peakDot = new XYDrawableAnnotation(x, y, 5, 5, new Drawable() {
        	public void draw(Graphics2D graph, Rectangle2D rect) {
        		graph.setPaint(paint);
        		graph.fillOval((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
        	}
        });
        
        return peakDot;
	}
	
	/**
	 * creates a peak arrow and label at the x/y value
	 */
	private static XYPointerAnnotation createPeakArrow(double x, double y, final Paint paint) {
		
		// places an arrow and text at 1:30 clockwise (45deg) 
		XYPointerAnnotation peakArrow = new XYPointerAnnotation(Double.toString(y), x, y, Math.PI * -1.0/3.0);
		peakArrow.setBaseRadius(25.0);
		peakArrow.setTipRadius(5.0);
		peakArrow.setLabelOffset(12.0);
		peakArrow.setPaint(paint);
		peakArrow.setTextAnchor(TextAnchor.TOP_RIGHT);
		
        return peakArrow;
	}

	/**
	 * creates a legend, vertically aligned on the right hand side
	 */
	private static LegendTitle generateLegend(XYPlot plot) {
		LegendTitle legend = new LegendTitle(plot.getRenderer());
		
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setVerticalAlignment(VerticalAlignment.CENTER);
		legend.setBorder(1.0f, 1.0f, 1.0f, 1.0f);
		
		return legend;
	}
	
	/**
	 * this generates a y axis label string.  it appends the y axis units seperated by /'s
	 */
	private static String getSeriesLabels(List<ChartSeries> seriesList) {
		String axisLabel = "";
		
		// we put the series into a hashset first because we don't want duplicate units
		Set<String> labelSet = new HashSet<String>();
		for(ChartSeries series: seriesList) {
			labelSet.add(series.getYAxisLabel());
		}
		
		for(String label: labelSet) {
			// the first label should not have a /
			if(axisLabel.length() > 1) {
				axisLabel += " / ";
			}
			axisLabel += label;
		}
		
		return axisLabel;
	}
	
	/**
	 * Converts a ChartSeries into an XYSeries
	 */
	private static XYSeries processSeries(ChartSeries series) {
		// create the series with an id of the y axis name, this will be used in legend generation
		XYSeries xySeries = new XYSeries(series.getYAxisName());
		
		// we set the description to the label but it doesn't appear to be used anywhere
		xySeries.setDescription(series.getYAxisLabel());
		
		// add each point to the xy series
		for(ChartPoint2D point: series.getPoints()) {
			xySeries.add(point.getX(), point.getY());
		}
		
		return xySeries;
	}
}
