package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.model.*;
import com.n4systems.persistence.loaders.LoaderFactory;

import java.util.Date;

public class WebEventScheduleToScheduleConverter {

	private final LoaderFactory loaderFactory;
	private final SessionUserDateConverter dateConverter;

	public WebEventScheduleToScheduleConverter(LoaderFactory loaderFactory, SessionUserDateConverter dateConverter) {
		this.loaderFactory = loaderFactory;
		this.dateConverter = dateConverter;
	}
	
	public Event convert(WebEventSchedule webSchedule, Asset asset) {
		Date scheduledDate = dateConverter.convertDateTime(webSchedule.getDate());
        if (scheduledDate == null) {
            // NOTE : try to parse a date without hour/mins.  not all schedules require it.
            // because scheduled dates are currently the only one with this "flexibility" the code is here but in the
            // future the dateConverter should be made more tolerant.
            scheduledDate = dateConverter.convertDate(webSchedule.getDate());
        }

		EventType eventType = loaderFactory.createFilteredIdLoader(EventType.class).setId(webSchedule.getType()).load();

        Event openEvent = new Event();
        openEvent.setTenant(asset.getTenant());
        openEvent.setAsset(asset);
        openEvent.setType(eventType);
        openEvent.setNextDate(scheduledDate);
        openEvent.setGroup(new EventGroup());
        openEvent.setOwner(asset.getOwner());
        openEvent.setStatus(Status.VOID);
		
		if (webSchedule.getJob() != null) {
			Project scheduleJob = loaderFactory.createFilteredIdLoader(Project.class).setId(webSchedule.getJob()).load();
			openEvent.setProject(scheduleJob);
		} 
		return openEvent;
	}
}
