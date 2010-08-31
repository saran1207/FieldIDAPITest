package com.n4systems.webservice.dto.inspectionschedule;

import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import com.n4systems.webservice.dto.RequestInformation;

public class InspectionScheduleRequest extends RequestInformation {
	
	private InspectionScheduleServiceDTO scheduleService = null;

	public InspectionScheduleServiceDTO getScheduleService() {
		return scheduleService;
	}

	public void setScheduleService(InspectionScheduleServiceDTO scheduleService) {
		this.scheduleService = scheduleService;
	}
	
	

}
