package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsEventType extends WsModel {
	private String name;
	private String description;
	private boolean printable;
	private boolean master;
	private boolean assignedToAvailable;
	private WsEventTypeGroup group;
	private WsEventForm form;
	private List<String> infoFieldNames = new ArrayList<String>();
	
	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name="Printable")
	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	@XmlElement(name="Master")
	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	@XmlElement(name="AssignedToAvailable")
	public boolean isAssignedToAvailable() {
		return assignedToAvailable;
	}

	public void setAssignedToAvailable(boolean assignedToAvailable) {
		this.assignedToAvailable = assignedToAvailable;
	}

	@XmlElement(name="Group")
	public WsEventTypeGroup getGroup() {
		return group;
	}

	public void setGroup(WsEventTypeGroup group) {
		this.group = group;
	}
	
	@XmlElement(name="Form")
	public WsEventForm getForm() {
		return form;
	}

	public void setForm(WsEventForm form) {
		this.form = form;
	}

	@XmlElement(name="InfoFieldNames")
	public List<String> getInfoFieldNames() {
		return infoFieldNames;
	}

	public void setInfoFieldNames(List<String> infoFieldNames) {
		this.infoFieldNames = infoFieldNames;
	}
	
}
