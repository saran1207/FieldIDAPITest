package com.n4systems.handlers.removers;

import static org.junit.Assert.*;

import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
import org.junit.Test;


public class EventTypeArchiveSummaryTest {
	
	@Test
	public void should_be_archivable_in_default_state() {
		EventTypeArchiveSummary sut = new EventTypeArchiveSummary();
		assertTrue(sut.canBeRemoved());
	}
	
	
	@Test
	public void should_not_be_archiveable_if_event_archive_summary_is_not() {
		EventArchiveSummary stubSummary = new EventArchiveSummary() {
													public boolean canBeRemoved() {
														return false;
													}
												};
		
		EventTypeArchiveSummary sut = new EventTypeArchiveSummary();
		sut.setEventArchiveSummary(stubSummary);
		
		assertFalse(sut.canBeRemoved());
	}
	
	@Test
	public void should_be_archiveable_if_event_archive_summary_is_archivable() {
		EventArchiveSummary stubSummary = new EventArchiveSummary() {
													@SuppressWarnings("unused")
													public boolean inspectionsCanBeArchived() {
														return true;
													}
												};
		
		EventTypeArchiveSummary sut = new EventTypeArchiveSummary();
		sut.setEventArchiveSummary(stubSummary);
		
		assertTrue(sut.canBeRemoved());
	}
}
