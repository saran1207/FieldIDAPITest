package com.n4systems.model.inspectiontype;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;

import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Cleaner;



public class AggragateInspectionTypeCleanerTest {

	InspectionType inspectionType = anInspectionType().build();
	
	@Test
	public void should_run_with_out_errors_when_no_cleaners_given() throws Exception {
		Cleaner<InspectionType> sut = new AggragateInspectionTypeCleaner();
		
		sut.clean(inspectionType);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_push_clean_call_to_supplied_inspection_type_cleaner() throws Exception {
		
		
		Cleaner<InspectionType> subCleaner = createMock(Cleaner.class);
		subCleaner.clean(inspectionType);
		replay(subCleaner);
		
		AggragateInspectionTypeCleaner sut = new AggragateInspectionTypeCleaner();
		sut.addCleaner(subCleaner);
		sut.clean(inspectionType);
		
		verify(subCleaner);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_push_clean_call_to_supplied_inspection_type_cleaners() throws Exception {
		Cleaner<InspectionType> subCleaner = createMock(Cleaner.class);
		subCleaner.clean(inspectionType);
		replay(subCleaner);
		
		Cleaner<InspectionType> subCleaner2 = createMock(Cleaner.class);
		subCleaner2.clean(inspectionType);
		replay(subCleaner2);
		
		AggragateInspectionTypeCleaner sut = new AggragateInspectionTypeCleaner();
		sut.addCleaner(subCleaner);
		sut.addCleaner(subCleaner2);
		
		sut.clean(inspectionType);
		
		verify(subCleaner);
		verify(subCleaner2);
	}
	
}
