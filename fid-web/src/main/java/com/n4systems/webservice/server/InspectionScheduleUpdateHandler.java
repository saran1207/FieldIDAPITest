package com.n4systems.webservice.server;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.model.eventschedule.EventScheduleByGuidOrIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InspectionScheduleUpdateHandler {
	
	private static Logger logger = Logger.getLogger(InspectionScheduleUpdateHandler.class);
	private EventScheduleByGuidOrIdLoader eventScheduleByMobileGuidLoader;
	private SimpleEventSaver saver;
	
	public InspectionScheduleUpdateHandler(
			EventScheduleByGuidOrIdLoader eventScheduleByMobileGuidLoader,
            SimpleEventSaver eventSaver) {
		
		super();
		this.eventScheduleByMobileGuidLoader = eventScheduleByMobileGuidLoader;
		this.saver = eventSaver;
		
	}
	
	public void updateInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		Event eventSchedule = loadExistingInspectionSchedule(inspectionScheduleServiceDTO);
		
		//somehow it is deleted, then we don't bother to update
		if (eventSchedule != null)
			updateInspectionSchedule(inspectionScheduleServiceDTO, eventSchedule);
	
	}

	public void removeInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		Event eventSchedule = loadExistingInspectionSchedule(inspectionScheduleServiceDTO);
		
		if (eventSchedule != null)
			removeInspectionSchedule(inspectionScheduleServiceDTO, eventSchedule);
	
	}

	
	private Event loadExistingInspectionSchedule(
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		//load inspection schedule either using id or mobileGuid
		Event eventSchedule = eventScheduleByMobileGuidLoader.
														setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid()).
														setId(inspectionScheduleServiceDTO.getId()).
														load();
		
		return eventSchedule;
		
	}
	
	private void updateInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO,
			Event event) {
		
		//update only when it is not completed
		if (event.getWorkflowState() == WorkflowState.OPEN) {
            event.setDueDate(stringToDate(inspectionScheduleServiceDTO.getNextDate()));
			saver.saveOrUpdate(event);
		}
	}

	private void removeInspectionSchedule(InspectionScheduleServiceDTO inspectionScheduleServiceDTO,
			Event eventSchedule) {
		
		try {
			//remove only when it is still open

			if (eventSchedule.getWorkflowState() == WorkflowState.OPEN) {
				saver.remove(eventSchedule);
			}
		} catch (EntityStillReferencedException e) {
			logger.error("Could not delete inspection schedule", e);
			
		}
	}
	
	public static Date stringToDate(String originalDate) {
		SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
		Date dateConvert = null;		
		try {
			dateConvert = DF.parse(originalDate);
		} catch (ParseException e) {
			// do nothing, return null
		} catch (NullPointerException e) {
			// do nothing, return null
		}
		return dateConvert;
	}
}
