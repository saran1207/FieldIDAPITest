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
	private List<WsCriteriaSection> sections = new ArrayList<WsCriteriaSection>();

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

	@XmlElement(name="Sections")
	public List<WsCriteriaSection> getSections() {
		return sections;
	}

	public void setSections(List<WsCriteriaSection> sections) {
		this.sections = sections;
	}
}
