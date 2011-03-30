package com.n4systems.model.downloadlink;

public enum DownloadState {
	REQUESTED	("label.requested",		false, 	false), 
	INPROGRESS	("label.inprogress",	false, 	false),
	COMPLETED	("label.completed",		true, 	true),
	DOWNLOADED	("label.completed",		true, 	true),
	DELETED		("label.deleted",		true, 	true),
	FAILED		("label.failed",		false, 	true);
	
	private final String label;
	private final boolean ready;
	private final boolean willExpire;
	
	DownloadState(String label, boolean ready, boolean willExpire) {
		this.label = label;
		this.ready = ready;
		this.willExpire = willExpire;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isReady() {
		return ready;
	}

	public boolean isWillExpire() {
		return willExpire;
	}
	
}
