package com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;

import java.util.ArrayList;
import java.util.List;

public class ApiEventType extends ApiReadOnlyModel2 {
	private String name;
	private String groupName;
	private String description;
	private boolean printable;
	private boolean hasPrintOut;
	private boolean hasObservationPrintOut;
	private boolean master;
	private boolean assignedToAvailable;
	private List<String> attributes = new ArrayList<>();
	private Long formId;
	private boolean action;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String group) {
		this.groupName = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public boolean isAssignedToAvailable() {
		return assignedToAvailable;
	}

	public void setAssignedToAvailable(boolean assignedToAvailable) {
		this.assignedToAvailable = assignedToAvailable;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public boolean isHasPrintOut() {
		return hasPrintOut;
	}

	public void setHasPrintOut(boolean hasPrintOut) {
		this.hasPrintOut = hasPrintOut;
	}
	
	public boolean isHasObservationPrintOut() {
		return hasObservationPrintOut;
	}

	public void setHasObservationPrintOut(boolean hasObservationPrintOut) {
		this.hasObservationPrintOut = hasObservationPrintOut;
	}	
}
