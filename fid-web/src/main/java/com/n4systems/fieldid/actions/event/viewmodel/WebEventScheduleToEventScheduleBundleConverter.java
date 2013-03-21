package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.persistence.loaders.LoaderFactory;

import java.util.Date;

public class WebEventScheduleToEventScheduleBundleConverter {

	private final LoaderFactory loaderFactory;
	private final SessionUserDateConverter dateConverter;

	public WebEventScheduleToEventScheduleBundleConverter(LoaderFactory loaderFactory, SessionUserDateConverter createUserDateConverter) {
		this.loaderFactory = loaderFactory;
		this.dateConverter = createUserDateConverter;
	}

	public EventScheduleBundle convert(WebEventSchedule nextSchedule, Asset asset) {
		Date scheduleDate;
		EventType scheduleType;
		Project scheduleJob;
		Assignable assignee = null;
        
		scheduleDate = dateConverter.convertDate(nextSchedule.getDate());
		scheduleType = loaderFactory.createFilteredIdLoader(EventType.class).setId(nextSchedule.getType()).load();

        if (nextSchedule.getAssignee() != null) {
            if (nextSchedule.getAssignee().startsWith("U")) {
                assignee = loaderFactory.createFilteredIdLoader(User.class).setId(Long.valueOf(nextSchedule.getAssignee().substring(1))).load();
            } else if (nextSchedule.getAssignee().startsWith("G")) {
                assignee = loaderFactory.createFilteredIdLoader(UserGroup.class).setId(Long.valueOf(nextSchedule.getAssignee().substring(1))).load();
            }
        }

		if (nextSchedule.getJob() != null) {
			scheduleJob = loaderFactory.createFilteredIdLoader(Project.class).setId(nextSchedule.getJob()).load();
		} else {
			scheduleJob = null;
		}
		
		return new EventScheduleBundle(asset, scheduleType, scheduleJob, scheduleDate, assignee);
		
	}

}
