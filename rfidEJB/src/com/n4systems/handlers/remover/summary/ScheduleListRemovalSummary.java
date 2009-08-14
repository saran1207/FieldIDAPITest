package com.n4systems.handlers.remover.summary;

public class ScheduleListRemovalSummary extends RemovalSummary {

	private int schedulesToRemove;
	
	public ScheduleListRemovalSummary() {
		this(0);
	}
	
	public ScheduleListRemovalSummary(int schedulesToRemove) {
		super();
		this.schedulesToRemove = schedulesToRemove;
	}

	@Override
	public boolean canBeRemoved() {
		return true;
	}

	public int getSchedulesToRemove() {
		return schedulesToRemove;
	}

	public void setSchedulesToRemove(int schedulesToRemove) {
		this.schedulesToRemove = schedulesToRemove;
	}

}
