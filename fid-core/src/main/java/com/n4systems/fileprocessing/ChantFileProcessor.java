package com.n4systems.fileprocessing;

import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.graphing.Chart;
import com.n4systems.graphing.ChartPoint2D;
import com.n4systems.graphing.ChartSeries;
import com.n4systems.graphing.GraphFactory;
import com.n4systems.tools.FileDataContainer;

/*
 * This is a pulltest processor for Chant files, implementing the FileProcessor interface.  Chant files
 * are xml based and contain a large amount of data, including product and customer information.
 * There is actually enough information to add a product although we do not currently support this.
 */
public class ChantFileProcessor extends FileProcessor {
	private final Logger logger = Logger.getLogger(ChantFileProcessor.class);
	private static final String ROOT_NODE_PATH = "/TestCertificate";
	private static final char XPATH_SEP = '/';
	private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"; //ISO8601 date format see rfc3339
	
	private Document doc;
	
	private void initialize(InputStream file) throws DocumentException {
		// open the stream as a dom4j document
		SAXReader reader = new SAXReader();
		doc = reader.read(file);
	}
	
	@SuppressWarnings("unchecked")
	protected void processFile(FileDataContainer fileDataContainer, InputStream file) throws FileProcessingException {
		
		fileDataContainer.setFileType(ProofTestType.CHANT);
		Chart chart = fileDataContainer.getChartData();
		chart.setDisplayName("Proof Test");
		
		logger.debug("Started processing of Chant log");
		try {
			// initialize the parser
			initialize(file);
		
			// lets begin by setting up the fileDataContainer
			populateFileDataContainer(fileDataContainer, doc);
			
			// now we'll pull the series metadata
			List<Node> chartSeriesNodes = (List<Node>)doc.selectNodes(formXPath("dtChartInfo"));

			if(chartSeriesNodes.size() > 0) {
				/* 
				 * the X axis should be the same for all channels in the series 
				 * a graph with different units on the x series would be dumb
				 * so we'll just use the first series
				 */ 
				chart.setXAxisName(findChartXSeriesName(chartSeriesNodes.get(0)));
//				chart.setXAxisUnit(findChartXSeriesUnit(chartSeriesNodes.get(0)));
			} else {
				// we must at least one series otherwise what's the point.
				throw new FileProcessingException("No series defined in file");
			}
			logger.debug("Found " + chartSeriesNodes.size() + " series");
			
			// create a chart series for each channel
			for(Node chartSeriesNode: chartSeriesNodes) {
				chart.getSeries().add(processChartSeriesNode(chartSeriesNode));
			}
			
			/*
			 * XXX - We will set the file data containers peak load from the first series,
			 * this may not be correct but it's the best we can do for now.
			 */
			String peakLoadString = NumberFormat.getInstance().format(chart.getSeries().get(0).getPeak());
			fileDataContainer.setPeakLoad(peakLoadString);
			
			fileDataContainer.setChart(GraphFactory.createPNGChartImage(chart));
			
		} catch(Exception e) {
			throw new FileProcessingException("Chant file processing failed", e);
		
		} finally {
			// document objects can be large so we'll clear it asap
			doc = null;
		}
		logger.debug("Chant log parsing completed sucessfully");
	}

	/*
	 * Convenience method for formXPath.  Forces absolute paths, 
	 */
	private String formXPath(String...pathParts) {
		return formXPath(false, pathParts);
	}
	
	/*
	 * Compresses an array of path part strings into an XPath path.
	 * the relative param decides if to add the root node element
	 */
	private String formXPath(boolean relative, String...pathParts) {
		StringBuilder pathBuilder = new StringBuilder();
		
		if(relative) {
			// current node
			pathBuilder.append(".");
		} else {
			pathBuilder.append(ROOT_NODE_PATH);
		}
		
		// compress the path parts prepending path seps 
		for(String pathPart: pathParts) {
			pathBuilder.append(XPATH_SEP);
			pathBuilder.append(pathPart);
		}
		
		return pathBuilder.toString();
	}

	/*
	 * Selects the child node of 'node' with name 'childNodeName' and returns its text or null if no child node is found
	 */
	private String getChildNodeText(Node node, String childNodeName) {
		Node childNode = node.selectSingleNode(formXPath(true, childNodeName));
		return (childNode != null) ? childNode.getText(): null;
	}
	
	/*
	 * Populates the extra data in the fileDataContainer from this chant file 
	 */
	private void populateFileDataContainer(FileDataContainer fileDataContainer, Document doc) throws FileProcessingException {
		
		fileDataContainer.setFileType(ProofTestType.CHANT);
		
		Node fixDataNode = doc.selectSingleNode(formXPath("dtFixedData"));
		
		Date datePerformed = parseDatePerformed(fixDataNode);
		fileDataContainer.setDatePerformed(datePerformed);
		
		/*
		 * XXX - for now we're going to pull the duration directly as a string from the file
		 * at some point we should probably changed to a real number with a unit
		 */
		String duration = getChildNodeText(fixDataNode, "Duration");
		fileDataContainer.setTestDuration(duration);
		
		String serialNumber = getChildNodeText(fixDataNode, "Serial_x0020_Number");
		fileDataContainer.setSerialNumbers(serialNumber);
		
		logger.debug("Chant file is for serial number [" + serialNumber + "] and date performed [" + String.valueOf(datePerformed) + "]");
		// note: FileDataContainer.PEAK_LOAD is set later on when processing series data
	}
	
	/*
	 * Parses the date performed from a dtFixedData node.  Can throw a FileProcessingException if
	 * the date fails parsing
	 */
	private Date parseDatePerformed(Node fixedDataNode) throws FileProcessingException {
		String dateString = getChildNodeText(fixedDataNode, "Date");
		SimpleDateFormat inputFormatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
		
		Date datePerformed = null;
		try {
			String replacedDate = correctTimeZoneAndMillisecondFormatting(dateString);
			datePerformed = inputFormatter.parse(replacedDate);
		} catch(Exception e) {
			// we must fail if a valid date performed cannot be found
			throw new FileProcessingException("Unable to parse date performed", e);
		}
		
		return datePerformed;
	}

	protected String correctTimeZoneAndMillisecondFormatting(String dateString) {
		
		//the date string is *almost* in ISO8601 format except that the offset is -04:00 rather then -0400 or GMT-04:00.
		// this removes the last ':' to put it into 'Z' format
		String replacedDate = dateString.replaceFirst("([-+]\\d+):(\\d+)$", "$1$2");
		
		
		replacedDate = replacedDate.replaceFirst("\\.\\d+([-+])", "$1");
		return replacedDate;
	}
	
	/*
	 * Takes in a dtChartInfo node and returns a string name for that X series
	 */
	private String findChartXSeriesName(Node chartSeries) {
		return getChildNodeText(chartSeries, "XName");
	}

	/*
	 * Takes in a dtChartInfo node and returns a string unit for that X series
	 */
	@SuppressWarnings("unused")
	private String findChartXSeriesUnit(Node chartSeries) {
		return getChildNodeText(chartSeries, "XUnits");
	}

	/*
	 * Takes in a dtChartInfo node and returns a chart series for this channel
	 */
	private ChartSeries processChartSeriesNode(Node chartSeries) throws FileProcessingException {
		ChartSeries series = new ChartSeries();
		
		// pull all the series meta data
		series.setYAxisName(getChildNodeText(chartSeries, "YName"));
		logger.debug("Processing series metadata with axis name [" + series.getYAxisName() + "]");
		
		series.setYAxisUnit(getChildNodeText(chartSeries, "YUnits"));
		series.setPeakName(getChildNodeText(chartSeries, "YPeakName"));
		
		String peakString = getChildNodeText(chartSeries, "YPeak");
		Double yPeak = null;
		if(peakString != null) {
			try {
				yPeak = Double.valueOf(peakString);
			} catch(Exception e) {
				// if the peak fails to parse it's should not be the end of the world
				logger.warn("Could not process peak from series: " + series.getYAxisName(), e);
			}
		}
		series.setPeak(yPeak);
		
		
		// now let's grab the series data for this channel
		populateSeriesData(series, getChildNodeText(chartSeries, "ChannelID"));
		
		return series;
	}
	
	
	@SuppressWarnings("unchecked")
	private void populateSeriesData(ChartSeries series, String channelId) throws FileProcessingException {
		// this will select all dtCH nodes with a CH attribute that equals our channelId
		List<Node> dataNodes = doc.selectNodes("//dtCH[@CH='" + channelId + "']");
		
		Element dataElement;
		ChartPoint2D point;
		for(Node dataNode: dataNodes) {
			// if the data nodes are not elements then we have the xml schema wrong
			if(dataNode.getNodeType() != Node.ELEMENT_NODE) {
				throw new FileProcessingException("Unexpected node type on node: " + dataNode.getName() + ".  Expected: ELEMENT_NODE, was: " + dataNode.getNodeTypeName());
			}
			dataElement = (Element)dataNode;
			
			point = new ChartPoint2D();
			try {
				// one point failing should not halt processing
				point.setX(Double.parseDouble(dataElement.attribute("X").getText()));
				point.setY(Double.parseDouble(dataElement.attribute("Y").getText()));
				
			} catch(Exception e) {
				logger.warn("Failed processing datapoint on channel: " + channelId, e);
			}
			
			//points that have failed will be entered as empty points
			series.getPoints().add(point);
		}
		
	}

}
