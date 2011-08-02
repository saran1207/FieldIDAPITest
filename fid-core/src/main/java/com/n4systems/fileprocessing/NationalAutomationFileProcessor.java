package com.n4systems.fileprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;

public class NationalAutomationFileProcessor extends FileProcessor {
	private static Logger logger = Logger.getLogger( NationalAutomationFileProcessor.class );

	private final String DATA_SHEET_NAME="Test Data";
	private final String EXTRA_INFO_SHEET_NAME="Report";
	
	private final int PEAK_LOAD_ROW = 17;
	private final short PEAK_LOAD_CELL = 3;
	
	private final int TEST_DURATION_ROW = 18;
	private final short TEST_DURATION_CELL = 1;
	
	private final int IDENTIFIER_ROW = 8;
	private final short IDENTIFIER_CELL = 3;
	
	private final int DATE_PERFORMED_ROW = 17;
	private final short DATE_PERFORMED_CELL = 1;
	
	public void processFile(FileDataContainer fileDataContainer, InputStream file) throws FileProcessingException {
		fileDataContainer.setFileType(ProofTestType.NATIONALAUTOMATION);
		
		try {
			
			XYSeries xySeries = new XYSeries("National Automation Chart");
		
			POIFSFileSystem fs = new POIFSFileSystem(file);
			
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			// Extract the chart data
			HSSFSheet dataSheet = wb.getSheet(DATA_SHEET_NAME);
			
			double time = 1; 
			int rowCount = 0;
			HSSFRow dataRow = dataSheet.getRow(rowCount);
			HSSFCell dataCell = dataRow.getCell((short)0);
			
			while (dataCell != null) {
				double data = dataCell.getNumericCellValue();
				xySeries.add(time, data);
				
				time++;
				rowCount++;
				
				dataRow = dataSheet.getRow(rowCount);
				if (dataRow != null) {
					dataCell = dataRow.getCell((short)0);
				} else {
					dataCell = null;
				}
			}
					
			if (xySeries.getItemCount() > 0) {
				XYSeriesCollection xyDataset = new XYSeriesCollection(xySeries);
				JFreeChart chart = ChartFactory.createXYLineChart("National Automation Chart", "", "Load", xyDataset, PlotOrientation.VERTICAL, false, false, false);
				chart.setAntiAlias(true);
//				chart.setTextAntiAlias(true);
				chart.setTitle("");
				chart.setBackgroundPaint(Color.white);
				chart.setBorderPaint(Color.black);
				BufferedImage bufferedImage = chart.createBufferedImage(510, 131);
				
				
				try {
					byte[] image = ChartUtilities.encodeAsPNG(bufferedImage);
					fileDataContainer.setChart(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
			
			// And pull off some extra information
			HSSFSheet extraInfoSheet = wb.getSheet(EXTRA_INFO_SHEET_NAME);
			
			if (extraInfoSheet != null) {
				HSSFRow peakLoadRow = extraInfoSheet.getRow(PEAK_LOAD_ROW);
				HSSFCell peakLoadCell = peakLoadRow.getCell(PEAK_LOAD_CELL);
				
				if (peakLoadCell != null) {
					if (peakLoadCell.getRichStringCellValue() != null) {
						fileDataContainer.setPeakLoad(peakLoadCell.getRichStringCellValue().getString());
					}
				}
				
				HSSFRow testDurationRow = extraInfoSheet.getRow(TEST_DURATION_ROW);
				HSSFCell testDurationCell = testDurationRow.getCell(TEST_DURATION_CELL);
				
				if (testDurationCell != null) {
					if (testDurationCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						fileDataContainer.setTestDuration(String.valueOf(testDurationCell.getNumericCellValue()));
					} else {
						fileDataContainer.setTestDuration(testDurationCell.getRichStringCellValue().getString());
					}
				}
				
				HSSFRow identifierRow = extraInfoSheet.getRow(IDENTIFIER_ROW);
				HSSFCell identifierCell = identifierRow.getCell(IDENTIFIER_CELL);
				
				if (identifierCell != null) {
					
					String identifierString = null;
					
					switch( identifierCell.getCellType() ){
						case HSSFCell.CELL_TYPE_NUMERIC:
							
							identifierString = String.valueOf( identifierCell.getNumericCellValue() );
							// reading the numeric value returns a double and will add .0 to the end of the string.
							// if we find a .0 at the end string remove it.
							if( identifierString.endsWith( ".0" ) ) {
								identifierString = identifierString.substring( 0, identifierString.length() - 2 );
								logger.warn( "Trimmed .0 off serial number  original value = " + identifierCell.getNumericCellValue()
										+ "  trimmed value " + identifierString );
							}
						
							break;
						case HSSFCell.CELL_TYPE_STRING:
						default:
							if (identifierCell.getRichStringCellValue() != null) {
								identifierString = identifierCell.getRichStringCellValue().getString();
							}
							break;
					}
					// Replace new line characters with commas for multiple serial numbers...
					if (identifierString != null) {
						identifierString = identifierString.replace('\n', ',');
					}
					
					fileDataContainer.setIdentifiers(identifierString);
			
				}
				
				HSSFRow datePerformedRow = extraInfoSheet.getRow(DATE_PERFORMED_ROW);
				HSSFCell datePerformedCell = datePerformedRow.getCell(DATE_PERFORMED_CELL);
				
				if (datePerformedCell != null) {
					if (datePerformedCell.getDateCellValue() != null) {
						fileDataContainer.setDatePerformed(datePerformedCell.getDateCellValue());
					} else if (datePerformedCell.getRichStringCellValue() != null) {
						String datePerformedString = datePerformedCell.getRichStringCellValue().getString();
						// since we don't what format this date will be in, this is the best we can do
						fileDataContainer.setDatePerformed(DateFormat.getDateInstance().parse(datePerformedString));
					}
				}
			}
			
		} catch (Exception e) {
			throw new FileProcessingException();
		}
	}
}
