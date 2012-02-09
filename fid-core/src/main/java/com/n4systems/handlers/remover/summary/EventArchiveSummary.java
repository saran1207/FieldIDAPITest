package com.n4systems.handlers.remover.summary;


public class EventArchiveSummary extends RemovalSummary {

	private Long deleteEvents;
	private Long deleteSchedules;
	
	public EventArchiveSummary() {
		this(0L, 0L);
	}

	public EventArchiveSummary(Long deleteEvents, Long deleteSchedules) {
		this.deleteEvents = deleteEvents;
		this.deleteSchedules = deleteSchedules;
	}

	public Long getDeleteEvents() {
		return deleteEvents;
	}

	public EventArchiveSummary setDeleteEvents(Long deleteEvents) {
		this.deleteEvents = deleteEvents;
		return this;
	}

	public Long getDeleteCompletedSchedules() {
		return deleteSchedules;
	}

	public EventArchiveSummary setDeleteSchedules(Long deleteSchedules) {
		this.deleteSchedules = deleteSchedules;
		return this;
	}

}
