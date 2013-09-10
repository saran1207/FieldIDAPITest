package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.EventStatus;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventStatusTranslationsPage extends TranslationsPage<EventStatus> {

    @SpringBean
    private EventStatusService eventStatusService;

    public EventStatusTranslationsPage() {
        super();
    }

    protected List<String> initExcludedFields() {
        return Lists.newArrayList("events");
    }

    @Override
    protected DropDownChoice<EventStatus> createChoice(String id) {
        return new FidDropDownChoice<EventStatus>(id, Model.of(new EventStatus()), eventStatusService.getActiveStatuses(), getChoiceRenderer());
    }

    public IChoiceRenderer<EventStatus> getChoiceRenderer() {
        return new IChoiceRenderer<EventStatus>() {
            @Override
            public Object getDisplayValue(EventStatus status) {
                return status.getDisplayName();
            }

            @Override
            public String getIdValue(EventStatus status, int index) {
                return status.getId()+"";
            }
        };
    }

}
