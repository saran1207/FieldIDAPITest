package com.n4systems.webservice.server;

import com.n4systems.model.EventSchedule;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.EventSchedule.ScheduleStatus;
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
		
		EventSchedule eventSchedule = loadExistingInspectionSchedule(inspectionScheduleServiceDTO);
		
		//somehow it is deleted, then we don't bother to update
		if (eventSchedule != null)
			updateInspectionSchedule(inspectionScheduleServiceDTO, eventSchedule);
	
	}

	public void removeInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		EventSchedule eventSchedule = loadExistingInspectionSchedule(inspectionScheduleServiceDTO);
		
		if (eventSchedule != null)
			removeInspectionSchedule(inspectionScheduleServiceDTO, eventSchedule);
	
	}

	
	private EventSchedule loadExistingInspectionSchedule(
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		//load inspection schedule either using id or mobileGuid
		EventSchedule eventSchedule = inspectionScheduleByMobileGuidLoader.
														setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid()).
														setId(inspectionScheduleServiceDTO.getId()).
														load();
		
		return eventSchedule;
		
	}
	
	private void updateInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO,
			EventSchedule eventSchedule) {
		
		//update only when it is not completed
		if (eventSchedule.getStatus() != ScheduleStatus.COMPLETED) {
			eventSchedule.setNextDate( AbstractBaseServiceDTO.stringToDate(inspectionScheduleServiceDTO.getNextDate()) );
			saver.saveOrUpdate(eventSchedule);
		}
	}

	private void removeInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO,
			EventSchedule eventSchedule) {
		
		try {
			//remove only when it is not completed
			if (eventSchedule.getStatus() != ScheduleStatus.COMPLETED) {
				saver.remove(eventSchedule);
			}
		} catch (EntityStillReferencedException e) {
			logger.error("Could not delete inspection schedule", e);
			
		}
	}
	
	

}
