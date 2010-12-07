package com.n4systems.model.eventtype;

public class EventTypeFormVersion {
	private final long id;
	private final long version;

	public EventTypeFormVersion(long id, long version){
		this.id = id;
		this.version = version;
	}
	
	public long getId() {
		return id;
	}

	public long getVersion() {
		return version;
	}
	
}
