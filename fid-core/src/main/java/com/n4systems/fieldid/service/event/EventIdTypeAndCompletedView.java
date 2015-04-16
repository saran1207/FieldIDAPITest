package com.n4systems.fieldid.service.event;

import java.util.Date;

public class EventIdTypeAndCompletedView {
	private final Long id;
	private final Long typeId;
	private final Date completed;

	public EventIdTypeAndCompletedView(Long id, Long typeId, Date completed) {
		this.id = id;
		this.typeId = typeId;
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public Long getTypeId() {
		return typeId;
	}

	public Date getCompleted() {
		return completed;
	}

}
