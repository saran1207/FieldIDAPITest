package com.n4systems.fileprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.graphing.Chart;
import com.n4systems.graphing.ChartPoint2D;
import com.n4systems.graphing.ChartSeries;
import com.n4systems.graphing.GraphFactory;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;

public class WiropFileProcessor extends FileProcessor {
	private final Logger logger = Logger.getLogger(WiropFileProcessor.class);
	private static final String headerDelim = "\\$";
	private static final String headerDateFormat = "MM/dd/yy";
	private static final String lineParseRegex = "^\\s*(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*$";
	private static final String headerSectionEnd = "####";
	
	private static final int loadGroup = 1;
	private static final int timeGroup = 3;
	
	private static final int CUSTOMER_NAME = 0;
	private static final int SERIAL_NUMBER = 2;
	private static final int TEST_DATE = 5;
	
	public void processFile(FileDataContainer fileDataContainer, InputStream file) throws FileProcessingException {
		Chart chart = fileDataContainer.getChartData();
		chart.setUsingPeakDots(true);
		chart.setDisplayName("Proof Test");
		chart.setXAxisName("Time");
		chart.setXAxisUnit("s");
		
		fileDataContainer.setFileType(ProofTestType.WIROP);
		
		Reader reader = null;
		BufferedReader bRead = null;
		
		logger.info("Started processing of Wirop log");
		try {
			reader = new InputStreamReader(file);
			bRead = new BufferedReader(reader);
			
			// the first line of the file is the header line
			String[] headers = parseHeaders(bRead);
			
			
			
			// populate the data container with our header info
			fileDataContainer.setSerialNumbers(headers[SERIAL_NUMBER].trim());
			logger.info("Wirop log serial number [" + headers[SERIAL_NUMBER] + "]");
			
			// parse the date ... this step is so stupid ... 
			Date datePerformed = DateHelper.string2Date(headerDateFormat, headers[TEST_DATE].trim());
			fileDataContainer.setDatePerformed(datePerformed);
			logger.info("Wirop log test date [" + headers[TEST_DATE] + "]");
			
			fileDataContainer.setCustomerName(headers[CUSTOMER_NAME].trim());
			logger.info("Wirop log customer name [" + headers[CUSTOMER_NAME] + "]");
			
			// now to create the chart data and image
			ChartSeries series = createChartSeries(bRead);
			chart.getSeries().add(series);
			
			// create the image
			fileDataContainer.setChart(GraphFactory.createPNGChartImage(chart));
			
			fileDataContainer.setPeakLoad(series.getPeak().toString());
			
			// this is actually safe since time for these files always starts at 0
			fileDataContainer.setTestDuration(String.valueOf(series.calculatePeakX()));
			
		} catch(Exception e) {
			logger.info("Wirop log processing failed", e);
			throw new FileProcessingException(e);
		} finally {
			IOUtils.closeQuietly(bRead);
			IOUtils.closeQuietly(reader);
		}
	}

	private String[] parseHeaders(BufferedReader reader) throws IOException {
		
		// read the first part of the header
		String fullHeaderLine = reader.readLine();
		
		/*
		 * Lame alert: In some cases, the wirop format places a crtl-m after the
		 * customer name (first field).  In this case we will have to read more then once.
		 * We'll detect that we've read the full header by reading until we see the headerSectionEnd.
		 */
		
		while(!fullHeaderLine.contains(headerSectionEnd)) {
			fullHeaderLine += reader.readLine();
		}
		
		return fullHeaderLine.split(headerDelim);
	}
	
	private ChartSeries createChartSeries(BufferedReader reader) throws IOException { 
		ChartSeries series = new ChartSeries("Load", "lbs");
		series.setPeakName("Peak Load");
		
		// compile the pattern to parse the load lines
		Pattern linePattern = Pattern.compile(lineParseRegex);
		
		// first point should always be zero
		series.getPoints().add(new ChartPoint2D(0.0d, 0.0d));
		
		String line;
		ChartPoint2D point;
		while((line = reader.readLine()) != null) {
			// parse a point from the file and add it
			point = createChartPoint(line.trim(), linePattern);
			if(point != null) {
				series.getPoints().add(point);
			}
		}
		
		// find our peak load and set it
		series.setPeak(series.calculatePeakY());
		
		return series;
	}
	
	private ChartPoint2D createChartPoint(String line, Pattern pattern) {
		Matcher matcher = pattern.matcher(line);
		double load, time;
		
		ChartPoint2D point = null;
		if(matcher.matches()) {
			
			load = Double.parseDouble(matcher.group(loadGroup).trim());
			time = Double.parseDouble(matcher.group(timeGroup).trim());
			
			point = new ChartPoint2D(time, load);
		}
		
		return point;
	}
	
	

}
