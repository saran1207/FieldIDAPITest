package com.n4systems.ws.model.eventtype;

import com.n4systems.ws.model.WsModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class WsEventType extends WsModel {
	private String name;
	private String description;
	private boolean active;
	private boolean printable;
	private boolean master;
	private boolean assignedToAvailable;
	private Long formId;
	private WsEventTypeGroup group;
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
	
	@XmlElement(name="Active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
	
	@XmlElement(name="FormId")
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	@XmlElement(name="Group")
	public WsEventTypeGroup getGroup() {
		return group;
	}

	public void setGroup(WsEventTypeGroup group) {
		this.group = group;
	}

	@XmlElement(name="InfoFieldNames")
	public List<String> getInfoFieldNames() {
		return infoFieldNames;
	}

	public void setInfoFieldNames(List<String> infoFieldNames) {
		this.infoFieldNames = infoFieldNames;
	}
	
}
