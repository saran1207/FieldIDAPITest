package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.EventSchedule;
import org.apache.wicket.model.PropertyModel;

public class MassSchedulePage extends FieldIDFrontEndPage {

    private EventSchedule eventSchedule;

    public MassSchedulePage() {
        eventSchedule = new EventSchedule();
        add(new SchedulePicker("schedulePicker", new PropertyModel<EventSchedule>(this, "eventSchedule"), new EventTypesForTenantModel(), new EventJobsForTenantModel()));
    }

}
