package com.n4systems.fileprocessing.roberts;

import java.util.Date;
import java.util.List;

import org.jfree.data.xy.XYSeries;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;

abstract public class LegacyRobertsParser implements RobertsParser {
	private static final int CUSTOMER_LINE = 1;
	private static final int COMMENT_LINE = 4;
	private static final int SERIAL_NUMBER_LINE = 13;
	private static final int INSPECTION_DATE_LINE = 18;
	private static final int DATA_START_LINE = 26;

	private final RobertsSerialNumberConverter serialConverter;
	private final RobertsChartGenerator chartGen;
	
	private final String dataSeperator;
	
	protected LegacyRobertsParser(String dataSeperator, RobertsSerialNumberConverter serialConverter, RobertsChartGenerator chartGen) {
		this.dataSeperator = dataSeperator;
		this.serialConverter = serialConverter;
		this.chartGen = chartGen;
	}
	
	protected LegacyRobertsParser(String dataSeperator) {
		this(dataSeperator, new RobertsSerialNumberConverter(), new RobertsChartGenerator());
	}
	
	public void extractData(FileDataContainer fileDataContainer, List<String> lines) throws FileProcessingException {
		parseHeaderSection(fileDataContainer, lines);
		parseDataSection(fileDataContainer, lines);
	}

	private void parseHeaderSection(FileDataContainer fileDataContainer, List<String> lines) {
		fileDataContainer.setFileType(ProofTestType.ROBERTS);
		fileDataContainer.setCustomerName(lines.get(CUSTOMER_LINE));
		fileDataContainer.setComments(lines.get(COMMENT_LINE));
		fileDataContainer.setSerialNumbers(serialConverter.toCSV(lines.get(SERIAL_NUMBER_LINE)));
		fileDataContainer.setInspectionDate(parseDate(lines.get(INSPECTION_DATE_LINE)));
	}

	private void parseDataSection(FileDataContainer fileDataContainer, List<String> lines) {
		XYSeries xySeries = new XYSeries("Roberts Chart");
		
		String[] dataSeries;
		double peakLoad = 0;
		double timeStep = 1, load;
		for (int i = DATA_START_LINE; i < lines.size(); i++) {
			
			dataSeries = lines.get(i).split(dataSeperator);

			// note we have no use for the second series
			load = Double.valueOf(dataSeries[0]);
			xySeries.add(timeStep, load);
			
			if (load > peakLoad) {
				peakLoad = load;
			}

			timeStep++;
		}
		
		fileDataContainer.setTestDuration(String.valueOf(timeStep));
		fileDataContainer.setPeakLoad(String.valueOf(peakLoad));
		fileDataContainer.setChart(chartGen.generate(xySeries));
	}

	private Date parseDate(String inputDate) {
		if (inputDate == null) {
			return null;
		}
		return DateHelper.string2Date("MM/dd/yy", inputDate);
	}
	
}
