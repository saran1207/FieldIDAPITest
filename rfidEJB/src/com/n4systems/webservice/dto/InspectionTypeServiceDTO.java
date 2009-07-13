package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class InspectionTypeServiceDTO extends AbstractBaseServiceDTO {
	
	private String name;
	private String description;	
	private boolean printable;	
	private boolean master;
	private long groupId;
	private long formVersion;
	private List<CriteriaSectionServiceDTO> sections = new ArrayList<CriteriaSectionServiceDTO>();
	private List<InfoFieldNameServiceDTO> infoFieldNames = new ArrayList<InfoFieldNameServiceDTO>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public List<CriteriaSectionServiceDTO> getSections() {
		return sections;
	}
	public void setSections(List<CriteriaSectionServiceDTO> sections) {
		this.sections = sections;
	}
	public List<InfoFieldNameServiceDTO> getInfoFieldNames() {
		return infoFieldNames;
	}
	public void setInfoFieldNames(List<InfoFieldNameServiceDTO> infoFieldNames) {
		this.infoFieldNames = infoFieldNames;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public long getFormVersion() {
    	return formVersion;
    }
	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
    }			
}
