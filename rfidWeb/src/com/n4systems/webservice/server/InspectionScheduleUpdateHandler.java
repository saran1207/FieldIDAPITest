package com.n4systems.webservice.server;

import org.apache.log4j.Logger;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.inspectionschedule.InspectionScheduleByGuidOrIdLoader;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;

import fieldid.web.services.dto.AbstractBaseServiceDTO;

public class InspectionScheduleUpdateHandler {
	
	private static Logger logger = Logger.getLogger(InspectionScheduleUpdateHandler.class);
	private InspectionScheduleByGuidOrIdLoader inspectionScheduleByMobileGuidLoader;
	private InspectionScheduleSaver saver;
	
	public InspectionScheduleUpdateHandler(
			InspectionScheduleByGuidOrIdLoader inspectionScheduleByMobileGuidLoader,
			InspectionScheduleSaver inspectionScheduleSaver) {
		
		super();
		this.inspectionScheduleByMobileGuidLoader = inspectionScheduleByMobileGuidLoader;
		this.saver = inspectionScheduleSaver;
		
	}
	
	public void updateInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		InspectionSchedule inspectionSchedule = loadExistingInspectionSchedule(inspectionScheduleServiceDTO);
		
		//somehow it is deleted, then we don't bother to update
		if (inspectionSchedule != null)
			updateInspectionSchedule(inspectionScheduleServiceDTO, inspectionSchedule);
	
	}

	public void removeInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		InspectionSchedule inspectionSchedule = loadExistingInspectionSchedule(inspectionScheduleServiceDTO);
		
		if (inspectionSchedule != null)
			removeInspectionSchedule(inspectionScheduleServiceDTO, inspectionSchedule);
	
	}

	
	private InspectionSchedule loadExistingInspectionSchedule(
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		//load inspection schedule either using id or mobileGuid
		InspectionSchedule inspectionSchedule = inspectionScheduleByMobileGuidLoader.
														setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid()).
														setId(inspectionScheduleServiceDTO.getId()).
														load();
		
		return inspectionSchedule;
		
	}
	
	private void updateInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO,
			InspectionSchedule inspectionSchedule) {
		
		//update only when it is not completed
		if (inspectionSchedule.getStatus() != ScheduleStatus.COMPLETED) {
			inspectionSchedule.setNextDate( AbstractBaseServiceDTO.stringToDate(inspectionScheduleServiceDTO.getNextDate()) );
			saver.saveOrUpdate(inspectionSchedule);
		}
	}

	private void removeInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO,
			InspectionSchedule inspectionSchedule) {
		
		try {
			//remove only when it is not completed
			if (inspectionSchedule.getStatus() != ScheduleStatus.COMPLETED) {
				saver.remove(inspectionSchedule);
			}
		} catch (EntityStillReferencedException e) {
			logger.error("Could not delete inspection schedule", e);
			
		}
	}
	
	

}
