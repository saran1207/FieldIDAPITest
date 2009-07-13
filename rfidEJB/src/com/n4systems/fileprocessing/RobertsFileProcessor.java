package com.n4systems.fileprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;

/**
 * Process' Roberts Logs file
 * @author Jesse Miller
 *
 */
public class RobertsFileProcessor extends FileProcessor {
	
	private static final Logger logger = Logger.getLogger(RobertsFileProcessor.class);
	private static final int NON_EXISTENT_LINE_NUMBER = 0;
	
	private Integer workingLoadLimit = null;
	private Float multiplier = null;
	private Double timeAtOrAbovePeakLoad = 0.0;
	private boolean peakLoadReached = false;
	private boolean peakLoadLost = false;
	
	private String oneLine;
	
	//XXX - this method should be broken up into is smaller ones.  it is just too long.
	public void processFile(FileDataContainer fileDataContainer, InputStream file) throws FileProcessingException {
		byte[] image = null;
		
		fileDataContainer.setFileType(ProofTestType.ROBERTS);

		try {
			
			if (file != null) {
				BufferedReader in = null;
				in = new BufferedReader(new InputStreamReader(file));
				
				if (in != null) {
					Double testDuration = null;
					Double peakLoad = new Double(0);
					String serialNumber = null;
					String inspectionDate = null;
					
					XYSeries xySeries = new XYSeries("Roberts Chart");
					
					try {
						oneLine = in.readLine();
						int lineNumber = 1;
						double time = 1;
						String version = "0";
						
						int customerNumberLine = 2;
						int commentNumberLine = 5;
						int serialNumberLine = 14;
						int dataStartLine = 27;
						int inspectionDateLine = 19;
						int testMethodLine = NON_EXISTENT_LINE_NUMBER;
						int workingLoadLimitLine = NON_EXISTENT_LINE_NUMBER;
						String splitDataAround = "@";
						
						
						
						String[] versionStrings = oneLine.split("=");
						if (versionStrings.length >= 2) {
							version = versionStrings[1].trim();
						}
						
						if (version.equals("7")) {
							customerNumberLine = 2;
							commentNumberLine = 5;
							serialNumberLine = 14;
							inspectionDateLine = 19;
							dataStartLine = 27;
							splitDataAround = "@";
						} else if (version.equals("8")) {
							customerNumberLine = 2;
							commentNumberLine = 5;
							serialNumberLine = 14;
							inspectionDateLine = 19;
							dataStartLine = 27;
							splitDataAround = ",";						
						} else if (version.equals("9")) {
							customerNumberLine = 2;
							commentNumberLine = 5;
							serialNumberLine = 14;
							inspectionDateLine = 19;
							dataStartLine = 27;
							splitDataAround = ",";
							testMethodLine = 7;
							workingLoadLimitLine = 8; 
						} else {
							logger.warn("Unknown roberts log file version "+version);
						}
						
						Double first;
						String[] splitLine, twoNumbers;
						while (oneLine != null) {
							// In version 9 every line is prepended by a label and an = sign
							// We split it and just take the right part of it
							if (version.equals("9")) {
								splitLine = oneLine.split("=", 2);
								oneLine = splitLine[1];
								
								// dump all fields before the data lines into our product option map
								// XXX - note that this is only supported for version 9 since previous versions do not have a label
								if(lineNumber < dataStartLine) {
									// only take lines that have both a label and value
									if(splitLine.length > 1) {
										fileDataContainer.getExtraInfo().put(splitLine[0].trim(), splitLine[1].trim());
									}
								}
							}
							
							if (lineNumber == serialNumberLine) {
								serialNumber = oneLine.trim();
							}
							
							if (lineNumber == inspectionDateLine) {
								inspectionDate = oneLine.trim();
							}
							
							if(lineNumber == customerNumberLine) {
								fileDataContainer.setCustomerName(oneLine.trim());
							}
							
							if(lineNumber == commentNumberLine) {
								fileDataContainer.setComments(oneLine.trim());
							}
							
							if (lineNumber == workingLoadLimitLine && version.equals("9")) {
								workingLoadLimit = Integer.parseInt(oneLine.trim());
							}
							
							if (lineNumber == testMethodLine && version.equals("9")) {
								multiplier = parseProofTestMethod(oneLine);
							}
							
							
							// Make sure we are past all the header stuff
							if (lineNumber >= dataStartLine && oneLine != null) {
								twoNumbers = oneLine.split(splitDataAround);
								if (twoNumbers.length >= 2) {
									first = Double.parseDouble(twoNumbers[0].trim());
									
									// for now the second number seems useless, so plot time vs. first
									xySeries.add(time, first);
									
									if (first.doubleValue() > peakLoad.doubleValue()) {
										peakLoad = first;
									}
									
									calculatePeakLoadDuration(first);
								}
								time ++;
							}
							
							oneLine = in.readLine();
							lineNumber++;
						}
						
						testDuration = new Double(time);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if (xySeries.getItemCount() > 0) {
						XYSeriesCollection xyDataset = new XYSeriesCollection(xySeries);
						JFreeChart chart = ChartFactory.createXYLineChart("Roberts Chart", "Time (s)", "Load (lbs)", xyDataset, PlotOrientation.VERTICAL, false, false, false);
						chart.setAntiAlias(true);
//						chart.setTextAntiAlias(true);
						chart.setTitle("");
						chart.setBackgroundPaint(Color.white);
						chart.setBorderPaint(Color.black);
						BufferedImage bufferedImage = chart.createBufferedImage(510, 131);
						
						try {
							image = ChartUtilities.encodeAsPNG(bufferedImage);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					fileDataContainer.setPeakLoad(peakLoad.toString());
					fileDataContainer.setSerialNumbers(convertRangeToCsv(serialNumber));
					
					if (inspectionDate != null) {
						Date realInspectionDate = DateHelper.string2Date("MM/dd/yy", inspectionDate);
						
						DateFormat dateFormat = DateFormat.getDateInstance();
						fileDataContainer.setInspectionDate(dateFormat.format(realInspectionDate));
					}
					
					if (testDuration != null) {
						fileDataContainer.setTestDuration(testDuration.toString());
					}
					
					if (canCalculatePeakLoadDuration()) {
						fileDataContainer.setPeakLoadDuration(timeAtOrAbovePeakLoad.toString());
					} else {
						fileDataContainer.setPeakLoadDuration(null);
					}
				}
			}
			
			fileDataContainer.setChart(image);
		} catch (Exception e) {
			logger.error("Exception thrown while processing file",e);
			throw new FileProcessingException(e);
		}
	}

	private void calculatePeakLoadDuration(Double first) {
		if (canCalculatePeakLoadDuration()) {
			if (!peakLoadLost) {
				if (first.doubleValue() >= multiplier * workingLoadLimit) {
					peakLoadReached = true;
					
					timeAtOrAbovePeakLoad++;
				} else if (peakLoadReached) {
					peakLoadLost = true;
				}
			}
		}
	}

	private boolean canCalculatePeakLoadDuration() {
		return workingLoadLimit != null && multiplier != null;
	}

	private Float parseProofTestMethod(String oneLine) {
		Float multiplier = null;
		Pattern proofTestMethodPattern = Pattern.compile("PROOF ?= ?([0-9]\\.[0-9]+) ?x ?WLL",Pattern.CASE_INSENSITIVE);
		Matcher testMethodMatcher = proofTestMethodPattern.matcher(oneLine);
		while (testMethodMatcher.find()) {
			multiplier = Float.parseFloat(testMethodMatcher.group(1));
		}
		return multiplier;
	}
	
	private String convertRangeToCsv(String serialNumber) {
		String parsedSerial = serialNumber;
		
		//XXX - this should be refactored.  The whole thing could be done in a couple lines of regex
		if (serialNumber.toLowerCase().contains("to")) {
			// Find the index of the "TO"
			int indexOfTo = serialNumber.toLowerCase().indexOf("to");
			
			String fromSerialNumber = serialNumber.substring(0, indexOfTo).trim();
			String toSerialNumber = serialNumber.substring(indexOfTo + 2).trim();
			
			String prefix = fromSerialNumber.replaceAll("[0-9]", "").trim();
			String[] splitFromSerialNumber = fromSerialNumber.split("[a-zA-Z]+", 2);
			String[] splitToSerialNumber = toSerialNumber.split("[a-zA-Z]+", 2);
			
			Integer startingNumber = Integer.valueOf(splitFromSerialNumber[splitFromSerialNumber.length - 1]);
			Integer endingNumber = Integer.valueOf(splitToSerialNumber[splitToSerialNumber.length - 1]);
			
			String properSerialNumberList = "";
			for (int i=startingNumber; i <= endingNumber; i++) {
				if (i != startingNumber) {
					properSerialNumberList += ",";
				}
				properSerialNumberList += prefix+i;
			}
			
			parsedSerial = properSerialNumberList;
			
		}
		
		return parsedSerial;
	}

	
}
