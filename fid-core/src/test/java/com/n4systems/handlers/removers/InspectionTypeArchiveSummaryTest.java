package com.n4systems.handlers.removers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.handlers.remover.summary.InspectionArchiveSummary;
import com.n4systems.handlers.remover.summary.InspectionTypeArchiveSummary;


public class InspectionTypeArchiveSummaryTest {
	
	@Test
	public void should_be_archivable_in_default_state() {
		InspectionTypeArchiveSummary sut = new InspectionTypeArchiveSummary();
		assertTrue(sut.canBeRemoved());
	}
	
	
	@Test
	public void should_not_be_archiveable_if_inspection_archive_summary_is_not() {
		InspectionArchiveSummary stubSummary = new InspectionArchiveSummary() {
													public boolean canBeRemoved() {
														return false;
													}
												};
		
		InspectionTypeArchiveSummary sut = new InspectionTypeArchiveSummary();
		sut.setInspectionArchiveSummary(stubSummary);
		
		assertFalse(sut.canBeRemoved());
	}
	
	@Test
	public void should__e_archiveable_if_inspection_archive_summary_is_archivable() {
		InspectionArchiveSummary stubSummary = new InspectionArchiveSummary() {
													@SuppressWarnings("unused")
													public boolean inspectionsCanBeArchived() {
														return true;
													}
												};
		
		InspectionTypeArchiveSummary sut = new InspectionTypeArchiveSummary();
		sut.setInspectionArchiveSummary(stubSummary);
		
		assertTrue(sut.canBeRemoved());
	}
}
