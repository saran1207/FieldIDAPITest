package com.n4systems.fileprocessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.NonDataSourceBackedConfigContext;

public class RobertsFileProcessorTest {

	RobertsFileProcessor processor;
	
	@Before
	public void setUp() throws Exception {
		processor = new RobertsFileProcessor();
		ConfigContext.setCurrentContext(new NonDataSourceBackedConfigContext());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_process_file_with_test_method() {
		FileDataContainer dataContainer = new FileDataContainer();
		try {
			processor.processFile(dataContainer, produceRobertsFile("roberts_testMethod2xWLL.log"));
		} catch (FileProcessingException fpe) {
			fail("there should not have been a processing exception");
		}
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		assertEquals("94.0", dataContainer.getPeakLoadDuration());
	}
	
	@Test
	public void test_process_file_with_test_method_adjust() {
		FileDataContainer dataContainer = new FileDataContainer();
		try {
			processor.processFile(dataContainer, produceRobertsFile("roberts_testMethod1.5xWLL.log"));
		} catch (FileProcessingException fpe) {
			fail("there should not have been a processing exception");
		}
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		assertEquals("144.0", dataContainer.getPeakLoadDuration());
	}
	

	private void confirmRegularDataFromRobertsProcessing(FileDataContainer dataContainer) {
		assertEquals(ProofTestType.ROBERTS, dataContainer.getFileType());
		assertEquals("164.0", dataContainer.getTestDuration());
		assertEquals("121350.0", dataContainer.getPeakLoad());
	}
	
	@Test
	public void test_process_file_with_misformed_test_method() {
		FileDataContainer dataContainer = new FileDataContainer();
		
		try {
			processor.processFile(dataContainer, produceRobertsFile("roberts_testMethod_invalid.log"));
		} catch (FileProcessingException fpe) {
			fail("there should not have been a processing exception");
		}
		
		confirmRegularDataFromRobertsProcessing(dataContainer);
		assertNull(dataContainer.getPeakLoadDuration());
	}
	
	
	private InputStream produceRobertsFile(String testFileName) {
		return RobertsFileProcessorTest.class.getResourceAsStream(testFileName);
	}

	
	
		
}
