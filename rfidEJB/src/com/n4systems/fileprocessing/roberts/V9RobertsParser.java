package com.n4systems.fileprocessing.roberts;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.data.xy.XYSeries;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;

public class V9RobertsParser implements RobertsParser {
	private static final int DATA_START_LINE = 26;

	private final RobertsSerialNumberConverter serialConverter;
	private final RobertsChartGenerator chartGen;
	
	private boolean peakLoadReached;
	private boolean peakLoadLost;
	
	public V9RobertsParser(RobertsSerialNumberConverter serialConverter, RobertsChartGenerator chartGen) {
		this.serialConverter = serialConverter;
		this.chartGen = chartGen;
	}
	
	public V9RobertsParser() {
		this(new RobertsSerialNumberConverter(), new RobertsChartGenerator());
	}
	
	@Override
	public void extractData(FileDataContainer fileDataContainer, List<String> lines) throws FileProcessingException {
		Map<String, String> headerMap = createHeaderDataMap(lines);
		
		fileDataContainer.setFileType(ProofTestType.ROBERTS);
		fileDataContainer.setCustomerName(headerMap.get("Customer"));
		fileDataContainer.setComments(headerMap.get("User Paragraph"));
		fileDataContainer.setInspectionDate(parseDate(headerMap.get("Test Date")));
		fileDataContainer.setSerialNumbers(serialConverter.toCSV(headerMap.get("Serial No.")));
		
		// we'll dump the entire header map into the extra info so the fields are available to dynamic processors later on
		fileDataContainer.getExtraInfo().putAll(headerMap);

		Double peakLimit = calcPeakLimit(headerMap.get("WLL"), headerMap.get("Test Method"));
		
		parseChartData(fileDataContainer, lines, peakLimit);
	}
	
	private void parseChartData(FileDataContainer fileDataContainer, List<String> lines, Double peakLimit) {
		peakLoadReached = false;
		peakLoadLost = false;
		
		XYSeries xySeries = new XYSeries("Roberts Chart");
		
		Pattern pattern = Pattern.compile("^.*=\\s*(\\d*),.*$");
		Matcher dataMatcher;

		double peakLoad = 0;
		double peakLoadDuration = 0;
		double timeStep = 1, load;
		for (int i = DATA_START_LINE; i < lines.size(); i++) {
			dataMatcher = pattern.matcher(lines.get(i));
			
			if (!dataMatcher.matches()) {
				continue;
			}
			
			try {
				load = Double.valueOf(dataMatcher.group(1));
			} catch(NumberFormatException e) {
				load = 0;
			}
			
			xySeries.add(timeStep, load);
			
			if (load > peakLoad) {
				peakLoad = load;
			}
			
			if (peakLimit != null) {
				peakLoadDuration = calculatePeakLoadDuration(peakLoadDuration, load, peakLimit);
			}
			
			timeStep++;
		}
		
		fileDataContainer.setTestDuration(String.valueOf(timeStep));
		fileDataContainer.setPeakLoad(String.valueOf(peakLoad));
		
		if (peakLimit != null) {
			fileDataContainer.setPeakLoadDuration(String.valueOf(peakLoadDuration));
		}
		
		fileDataContainer.setChart(chartGen.generate(xySeries));
	}
	
	private Date parseDate(String inputDate) {
		return DateHelper.string2Date("MM/dd/yy", inputDate);
	}
	
	private double calculatePeakLoadDuration(double peakLoadDuration, double load, double peakLimit) {
		if (!peakLoadLost) {
			if (load >= peakLimit) {
				peakLoadReached = true;
				
				peakLoadDuration++;
			} else if (peakLoadReached) {
				peakLoadLost = true;
			}
		}
		
		return peakLoadDuration;
	}

	private Double calcPeakLimit(String wllLine, String testMethodLine) {
		Double peakLimit;
		try {
			Double workingLoadLimit = parseWorkingLoadLimit(wllLine);
			Double multiplier = parseProofTestMethod(testMethodLine);
			
			peakLimit = (workingLoadLimit * multiplier);
		} catch (RuntimeException e) {
			/*
			 * we're going to consume any exceptions generated while calculating
			 * the peakLimit, these could include NullPointers, NumberFormats,
			 * and ones generated from Pattern/Matcher.  We'll default to null
			 * on any problem.
			 */
			peakLimit = null;
		}
		
		return peakLimit;
	}
	
	private Double parseProofTestMethod(String methodLine) {
		Matcher testMethodMatcher = Pattern.compile("^.*=\\s*(\\d+\\.\\d+)\\D*$").matcher(methodLine);
		
		if (!testMethodMatcher.matches()) {
			return null;
		}
		
		Double testMethod = Double.valueOf(testMethodMatcher.group(1));
		return testMethod;
	}
	
	private Double parseWorkingLoadLimit(String wllLine) {
		return Double.valueOf(wllLine);
	}
	
	private Map<String, String> createHeaderDataMap(List<String> lines) {
		Map<String, String> dataMap = new HashMap<String, String>();
		
		String line;
		String[] splitLine;
		for (int i = 0; i < DATA_START_LINE; i++) {
			line = lines.get(i);
			
			if (line.indexOf('=') == -1) {
				// only convert valid lines
				continue;
			}
			
			splitLine = line.split("=", 2);
			dataMap.put(splitLine[0].trim(), splitLine[1].trim());
		}
		
		return dataMap;
	}

}
