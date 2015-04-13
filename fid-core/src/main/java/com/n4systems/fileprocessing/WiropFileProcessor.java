package com.n4systems.fileprocessing;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.graphing.Chart;
import com.n4systems.graphing.ChartPoint2D;
import com.n4systems.graphing.ChartSeries;
import com.n4systems.graphing.GraphFactory;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.UnitHelper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiropFileProcessor extends FileProcessor {
	private final Logger logger = Logger.getLogger(WiropFileProcessor.class);
	private static final String headerDelim = "\\$";
	private static final String headerDateFormat = "MM/dd/yy";
	private static final String lineParseRegex = "^\\s*(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*$";
	private static final String tstHeaderMarker = "####";
	private static final String universalHeaderEnd = " 0.00000" + Character.toString((char)9) + " 0.00000" + Character.toString((char)9) + " 0.00000";
	
	private static final int loadGroup = 1;
	private static final int timeGroup = 3;

	//Positions of key data in the TST file header...
	private static final int TST_CUSTOMER_NAME = 0;
	private static final int TST_SERIAL_NUMBER = 2;
	private static final int TST_TEST_DATE = 5;

	//Positions of key data in the TXT file header...
	private static final int TXT_CUSTOMER_NAME = 7;
	private static final int TXT_SERIAL_NUMBER = 0;
	private static final int TXT_TEST_DATE = 1;

	public enum WiropFileType {
		TST, TXT
	}

	private WiropFileType fileType;
	
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
			
			//Below, we use the fileType to determine what positions to look for various info at.
			
			// populate the data container with our header info
			fileDataContainer.setIdentifiers(this.fileType.equals(WiropFileType.TST) ? headers[TST_SERIAL_NUMBER].trim() : headers[TXT_SERIAL_NUMBER].trim());
			logger.info("Wirop log serial number [" + fileDataContainer.getIdentifiers().get(0) + "]");
			
			// parse the date ... this step is so stupid ... 
			Date datePerformed = this.fileType.equals(WiropFileType.TST) ? DateHelper.string2Date(headerDateFormat, headers[TST_TEST_DATE].trim()) : DateHelper.string2Date(headerDateFormat, headers[TXT_TEST_DATE].trim());
			fileDataContainer.setDatePerformed(datePerformed);
			logger.info("Wirop log test date [" + fileDataContainer.getDatePerformed() + "]");
			
			fileDataContainer.setCustomerName(this.fileType.equals(WiropFileType.TST) ? headers[TST_CUSTOMER_NAME].trim() : headers[TXT_CUSTOMER_NAME].trim());
			logger.info("Wirop log customer name [" + fileDataContainer.getCustomerName() + "]");
			
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

		//We now look for the end of the header line, rather than for the "####" marker.  The reason for this is that
		//only one of the two file types has that marker, which allows us to tell the difference between them.
		while(!fullHeaderLine.endsWith(universalHeaderEnd)) {
			String line = reader.readLine();
			if(line != null) {
				fullHeaderLine += line;
			} else {
				throw new IOException("End of file has been reached without the universal header end showing up.");
			}
		}

		//We need to record what type of file we're dealing with, since the different formats hold the data we're
		//looking for at different positions.
		this.fileType = fullHeaderLine.contains(tstHeaderMarker) ? WiropFileType.TST : WiropFileType.TXT;
		logger.info("Processing a " + (this.fileType.equals(WiropFileType.TST) ? ".tst" : ".txt") + " file.");
		
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

            //Convert Load to lbs, like should have been done all along...
            load = UnitHelper.convertKgToLbs(load);

			point = new ChartPoint2D(time, load);
		}
		
		return point;
	}
	
	

}
