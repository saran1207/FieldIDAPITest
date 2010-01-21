package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeServiceDTO extends AbstractBaseServiceDTO {
	
	private String name;
	private List<Long> inspectionTypeIds = new ArrayList<Long>();
	private List<InfoFieldServiceDTO> infoFields = new ArrayList<InfoFieldServiceDTO>();
	private List<ProductTypeScheduleServiceDTO> schedules = new ArrayList<ProductTypeScheduleServiceDTO>();
	private List<Long> subTypes = new ArrayList<Long>();
	private long groupId;
	private boolean master;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<InfoFieldServiceDTO> getInfoFields() {
		return infoFields;
	}

	public void setInfoFields(List<InfoFieldServiceDTO> infoFields) {
		this.infoFields = infoFields;
	}

	public List<Long> getInspectionTypeIds() {
		return inspectionTypeIds;
	}

	public void setInspectionTypeIds(List<Long> inspectionTypeIds) {
		this.inspectionTypeIds = inspectionTypeIds;
	}

	public List<ProductTypeScheduleServiceDTO> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ProductTypeScheduleServiceDTO> schedules) {
		this.schedules = schedules;
	}

	public List<Long> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(List<Long> subTypes) {
		this.subTypes = subTypes;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}
}
