package com.n4systems.handlers.remover.summary;

public class SavedReportDeleteSummary extends RemovalSummary{
	
	private Long savedReportsToRemove;
	
	public SavedReportDeleteSummary() {
		this(0L);
	}
	
	public SavedReportDeleteSummary(long savedReportsToRemove) {
		super();
		this.savedReportsToRemove = savedReportsToRemove;
	}

	@Override
	public boolean canBeRemoved() {
		return true;
	}


	public Long getSavedReportsToDelete() {
		return savedReportsToRemove;
	}


	public void setSavedReportsToRemove(Long savedReportsToRemove) {
		this.savedReportsToRemove = savedReportsToRemove;
	}
	
}
