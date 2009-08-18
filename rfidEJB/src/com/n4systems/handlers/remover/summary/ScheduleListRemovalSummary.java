package com.n4systems.handlers.remover.summary;

public class ScheduleListRemovalSummary extends RemovalSummary {

	private Long schedulesToRemove;
	
	public ScheduleListRemovalSummary() {
		this(0L);
	}
	
	public ScheduleListRemovalSummary(Long schedulesToRemove) {
		super();
		this.schedulesToRemove = schedulesToRemove;
	}

	@Override
	public boolean canBeRemoved() {
		return true;
	}

	public Long getSchedulesToRemove() {
		return schedulesToRemove;
	}

	public void setSchedulesToRemove(Long schedulesToRemove) {
		this.schedulesToRemove = schedulesToRemove;
	}

}
