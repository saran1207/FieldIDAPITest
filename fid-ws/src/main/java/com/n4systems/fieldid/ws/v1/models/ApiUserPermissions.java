package com.n4systems.fieldid.ws.v1.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApiUserPermissions {
	private boolean identify;
	private boolean createEvent;
	private boolean editEvent;

	public boolean isIdentify() {
		return identify;
	}

	public void setIdentify(boolean identify) {
		this.identify = identify;
	}

	public boolean isCreateEvent() {
		return createEvent;
	}

	public void setCreateEvent(boolean createEvent) {
		this.createEvent = createEvent;
	}

	public boolean isEditEvent() {
		return editEvent;
	}

	public void setEditEvent(boolean editEvent) {
		this.editEvent = editEvent;
	}

}
