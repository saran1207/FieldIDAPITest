package com.n4systems.model.downloadlink;

public enum DownloadState {
	REQUESTED	("label.requested",		false), 
	INPROGRESS	("label.inprogress",	false),
	COMPLETED	("label.completed",		true),
	FAILED		("label.failed",		false);
	
	private final String label;
	private final boolean ready;
	
	DownloadState(String label, boolean ready) {
		this.label = label;
		this.ready = ready;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isReady() {
		return ready;
	}
}
