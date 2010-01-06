package com.n4systems.webservice.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.webservice.server.bundles.ProofTestBundle;
import com.n4systems.webservice.server.bundles.ProofTestStatusBundle;
import com.n4systems.webservice.server.bundles.WebServiceStatus;


public class InspectionServiceImplTest {

	
	
	private ProofTestBundle bundle;
	private Map<String, Inspection> inspectionMap;
	private List<ProofTestStatusBundle> statusListCollector;
	

	@Test(expected=NoSerialNumbersException.class)
	public void should_throw_an_exception_if_no_serial_numbers_are_in_the_inspection_map() throws Exception {
		statusListCollector = new ArrayList<ProofTestStatusBundle>();
		inspectionMap = new HashMap<String, Inspection>(); 
		bundle = null;
		
		
		InspectionServiceImpl sut = new InspectionServiceImpl();
		
		sut.applyProcessingResults(statusListCollector, inspectionMap, bundle);
	}
	
	
	@Test
	public void should_add_success_to_the_status_list_collector_when_there_is_a_serial_number_in_the_inspection_map() throws Exception {
		Inspection aCreatedInspection = new Inspection();
		
		statusListCollector = new ArrayList<ProofTestStatusBundle>();
		
		inspectionMap = new HashMap<String, Inspection>();
		inspectionMap.put("serial_number", aCreatedInspection);
		
		bundle = createProofTestBundle();
		
		InspectionServiceImpl sut = new InspectionServiceImpl();
		
		sut.applyProcessingResults(statusListCollector, inspectionMap, bundle);
		
		assertStatusListHasOneEntry(WebServiceStatus.SUCCESSFUL);
	}


	private void assertStatusListHasOneEntry(WebServiceStatus expectedStatus) {
		assertEquals(1, statusListCollector.size());
		assertEquals(bundle.getFileName(), statusListCollector.get(0).getFileName());
		assertEquals(expectedStatus, statusListCollector.get(0).getStatus());
	}
	
	@Test
	public void should_add_a_single_success_to_the_status_list_collector_when_there_are_multiple_serial_numbers_in_the_inspection_map() throws Exception {
		Inspection aCreatedInspection = new Inspection();
		
		statusListCollector = new ArrayList<ProofTestStatusBundle>();
		
		inspectionMap = new HashMap<String, Inspection>();
		inspectionMap.put("serial_number", aCreatedInspection);
		inspectionMap.put("serial_number_2", aCreatedInspection);
		
		bundle = createProofTestBundle();
		
		InspectionServiceImpl sut = new InspectionServiceImpl();
		
		sut.applyProcessingResults(statusListCollector, inspectionMap, bundle);
		
		assertStatusListHasOneEntry(WebServiceStatus.SUCCESSFUL);
	}
	
	@Test
	public void should_add_a_single_failure_to_the_status_list_collector_when_there_is_a_serial_numbers_in_the_inspection_map_but_no_inspection_tied_to_it() throws Exception {
		
		statusListCollector = new ArrayList<ProofTestStatusBundle>();
		
		inspectionMap = new HashMap<String, Inspection>();
		inspectionMap.put("serial_number", null);
		
		
		bundle = createProofTestBundle();
		
		InspectionServiceImpl sut = new InspectionServiceImpl();
		
		sut.applyProcessingResults(statusListCollector, inspectionMap, bundle);
		
		assertStatusListHasOneEntry(WebServiceStatus.FAILED);
	}


	private ProofTestBundle createProofTestBundle() {
		ProofTestBundle bundle = new ProofTestBundle();
		bundle.setFileName("some file_name");
		return bundle;
	}
	
}
