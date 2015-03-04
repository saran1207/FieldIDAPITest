package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

public class StartRegularOrMasterEventPage extends FieldIDAuthenticatedPage {

    private @SpringBean PersistenceService persistenceService;

    public StartRegularOrMasterEventPage(PageParameters params) {
        Long assetId = params.get("assetId").toLongObject();
        Long eventTypeId = params.get("type").toLongObject();

        StringValue scheduleIdParam = params.get("scheduleId");
        Long scheduleId = scheduleIdParam.isEmpty() ? null : scheduleIdParam.toLongObject();
        EventType type = persistenceService.find(EventType.class, eventTypeId);

        if (type instanceof ThingEventType && ((ThingEventType)type).isMaster()) {
            String strutsAction = String.format("/masterEventAdd.action?assetId=%d&type=%d", assetId, eventTypeId);
            if (scheduleId != null) {
                strutsAction += "&scheduleId="+scheduleId;
            }
            throw new RedirectToUrlException(strutsAction);
        } else {
            PageParameters nextParams = new PageParameters().add("assetId", assetId).add("type", eventTypeId);
            if (scheduleId != null) {
                nextParams.add("scheduleId", scheduleId);
            }
            setResponsePage(PerformEventPage.class, nextParams);
        }
    }

}
