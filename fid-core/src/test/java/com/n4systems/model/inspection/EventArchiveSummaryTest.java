package com.n4systems.model.inspection;

import static org.junit.Assert.*;

import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import org.junit.Test;


public class EventArchiveSummaryTest {

	@Test
	public void should_be_archivable_in_default_state() {
		assertTrue(new EventArchiveSummary().canBeRemoved());
	}
	
	@Test
	public void should_be_archivable_when_inspections_part_of_master_inspection_is_0() {
		EventArchiveSummary sut = new EventArchiveSummary();
		sut.setEventsPartOfMaster(0L);
		sut.setDeleteEvents(10021L);
		assertTrue(sut.canBeRemoved());
	}
	
	@Test
	public void should_be_archivable_when_inspections_part_of_master_inspection_is_not_0() {
		EventArchiveSummary sut = new EventArchiveSummary();
		sut.setEventsPartOfMaster(4L);
		sut.setDeleteEvents(10021L);
		assertFalse(sut.canBeRemoved());
	}
	
}
