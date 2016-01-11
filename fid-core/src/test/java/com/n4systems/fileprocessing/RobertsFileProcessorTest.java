package com.n4systems.fileprocessing;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.services.config.ConfigServiceTestManager;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.NonDataSourceBackedConfigContext;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

public class RobertsFileProcessorTest {
	private Date datePerformed;
	private RobertsFileProcessor processor;
	
	@Before
	public void setUp() throws Exception {
		processor = new RobertsFileProcessor();
		
		datePerformed = (new SimpleDateFormat("dd-MM-yyyy")).parse("16-01-2009");

		ConfigServiceTestManager.setInstance(new NonDataSourceBackedConfigContext());
	}
	
	@Test
	public void test_process_file_with_test_method() throws FileProcessingException {
		FileDataContainer dataContainer = new FileDataContainer();
		processor.processFile(dataContainer, produceRobertsFile("roberts_testMethod2xWLL.log"));
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		confirmExtraInfo(dataContainer.getExtraInfo());
		assertEquals("94.0", dataContainer.getPeakLoadDuration());
	}
	
	@Test
	public void test_process_file_with_test_method_adjust() throws FileProcessingException {
		FileDataContainer dataContainer = new FileDataContainer();
		processor.processFile(dataContainer, produceRobertsFile("roberts_testMethod1.5xWLL.log"));
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		confirmExtraInfo(dataContainer.getExtraInfo());
		assertEquals("144.0", dataContainer.getPeakLoadDuration());
	}
	
	@Test
	public void test_process_file_with_misformed_test_method() throws FileProcessingException {
		FileDataContainer dataContainer = new FileDataContainer();
		processor.processFile(dataContainer, produceRobertsFile("roberts_testMethod_invalid.log"));
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		confirmExtraInfo(dataContainer.getExtraInfo());
		assertNull(dataContainer.getPeakLoadDuration());
	}
	
	@Test
	public void test_version_8() throws FileProcessingException {
		FileDataContainer dataContainer = new FileDataContainer();
		processor.processFile(dataContainer, produceRobertsFile("roberts_v8.roberts"));
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		assertNull(dataContainer.getPeakLoadDuration());
	}
	
	@Test
	public void test_version_7() throws FileProcessingException {
		FileDataContainer dataContainer = new FileDataContainer();
		processor.processFile(dataContainer, produceRobertsFile("roberts_v7.roberts"));
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		assertNull(dataContainer.getPeakLoadDuration());
	}
	
	@Test
	public void allows_negative_load_values() throws FileProcessingException {
		FileDataContainer dataContainer = new FileDataContainer();
		processor.processFile(dataContainer, produceRobertsFile("roberts_negative_values.log"));

		assertEquals("125.0", dataContainer.getTestDuration());
	}
	
	private void confirmRegularDataFromRobertsProcessing(FileDataContainer dataContainer) {
		assertEquals(ProofTestType.ROBERTS, dataContainer.getFileType());
		assertEquals("164.0", dataContainer.getTestDuration());
		assertEquals("121350.0", dataContainer.getPeakLoad());
		assertEquals("<COMMENTS GO HERE>", dataContainer.getComments());
		assertEquals(Arrays.asList("SERIALNUMBER"), dataContainer.getIdentifiers());
		assertEquals("<CUSTOMER NAME>", dataContainer.getCustomerName());
		assertEquals(datePerformed, dataContainer.getDatePerformed());
		assertTrue(dataContainer.getChart().length > 0);
	}
	
	private void confirmExtraInfo(Map<String, String> extraInfo) {
		assertEquals("9", extraInfo.get("VERSION"));
		assertEquals("<CUSTOMER NAME>", extraInfo.get("Customer"));
		assertEquals("<addr1>", extraInfo.get("Addr1"));
		assertEquals("<addr2>", extraInfo.get("Addr2"));
		assertEquals("<COMMENTS GO HERE>", extraInfo.get("User Paragraph"));
		assertEquals("35 TON SHACKLE", extraInfo.get("Description"));
		assertEquals("60000", extraInfo.get("WLL"));
		assertEquals("11260", extraInfo.get("Test Number"));
		assertEquals("30 TON", extraInfo.get("Size"));
		assertEquals("12 ft", extraInfo.get("Length"));
		assertEquals("ON12345", extraInfo.get("Order No."));
		assertEquals("Proof Test Time: 02:03 min:sec", extraInfo.get("Test Info"));
		assertEquals("SERIALNUMBER", extraInfo.get("Serial No."));
		assertEquals("0", extraInfo.get("Test Time"));
		assertEquals("600000", extraInfo.get("Maximum Load"));
		assertEquals("lbs.", extraInfo.get("Load Units"));
		assertEquals("Chris Pruett", extraInfo.get("Operator"));
		assertEquals("01/16/09", extraInfo.get("Test Date"));
		assertEquals("121400", extraInfo.get("Peak Load"));
		assertEquals("0", extraInfo.get("DoingLength"));
		assertEquals("10", extraInfo.get("Full Scale Displacement"));
		assertEquals("ft.", extraInfo.get("Lenght Units"));
		assertEquals("#,##0.00", extraInfo.get("Lenght Format"));
		assertEquals("0", extraInfo.get("Max. Displacement Detected"));
		assertEquals("0", extraInfo.get("Disp. At Peak"));
	}
	
	private InputStream produceRobertsFile(String testFileName) {
		return getClass().getResourceAsStream(testFileName);
	}
		
}
