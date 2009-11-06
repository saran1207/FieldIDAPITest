package com.n4systems.fileprocessing.roberts;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class RobertsChartGenerator {
	private static Logger logger = Logger.getLogger(RobertsChartGenerator.class);
	
	public byte[] generate(XYSeries xySeries) {
		byte[] image = null;
		
		if (xySeries.getItemCount() > 0) {
			XYSeriesCollection xyDataset = new XYSeriesCollection(xySeries);
			JFreeChart chart = ChartFactory.createXYLineChart("Roberts Chart", "Time (s)", "Load (lbs)", xyDataset, PlotOrientation.VERTICAL, false, false, false);
			chart.setAntiAlias(true);
			chart.setTitle("");
			chart.setBackgroundPaint(Color.white);
			chart.setBorderPaint(Color.black);
			BufferedImage bufferedImage = chart.createBufferedImage(510, 131);
			
			try {
				image = ChartUtilities.encodeAsPNG(bufferedImage);
			} catch (IOException e) {
				logger.warn("Could not encode chart image, ignoring", e);
			}
		}
		
		return image;
	}
	
}
