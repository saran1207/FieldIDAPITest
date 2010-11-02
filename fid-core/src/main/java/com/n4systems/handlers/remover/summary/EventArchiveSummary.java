package com.n4systems.handlers.remover.summary;


public class EventArchiveSummary extends RemovalSummary {
	private Long deleteEvents;
	private Long eventsPartOfMaster;
	private Long deleteSchedules;
	
	
	
	public EventArchiveSummary() {
		this(0L, 0L, 0L);
	}

	public EventArchiveSummary(Long deleteEvents, Long eventsPartOfMaster, Long deleteSchedules) {
		super();
		this.deleteEvents = deleteEvents;
		this.eventsPartOfMaster = eventsPartOfMaster;
		this.deleteSchedules = deleteSchedules;
	}

	public boolean canBeRemoved() {
		return eventsPartOfMaster == 0L;
	}
	
	public Long getEventsPartOfMaster() {
		return eventsPartOfMaster;
	}
	
	public EventArchiveSummary setEventsPartOfMaster(Long eventsPartOfMaster) {
		this.eventsPartOfMaster = eventsPartOfMaster;
		return this;
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
