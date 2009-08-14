package com.n4systems.model.inspection;

import static org.junit.Assert.*;

import org.junit.Test;


public class InspectionArchiveSummaryTest {

	@Test
	public void should_be_archivable_in_default_state() {
		assertTrue(new InspectionArchiveSummary().inspectionsCanBeArchived());
	}
	
	@Test
	public void should_be_archivable_when_inspections_part_of_master_inspection_is_0() {
		InspectionArchiveSummary sut = new InspectionArchiveSummary();
		sut.setInspectionsPartOfMaster(0L);
		sut.setDeleteInspections(10021L);
		assertTrue(sut.inspectionsCanBeArchived());
	}
	
	@Test
	public void should_be_archivable_when_inspections_part_of_master_inspection_is_not_0() {
		InspectionArchiveSummary sut = new InspectionArchiveSummary();
		sut.setInspectionsPartOfMaster(4L);
		sut.setDeleteInspections(10021L);
		assertFalse(sut.inspectionsCanBeArchived());
	}
	
}
