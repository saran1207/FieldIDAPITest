package com.n4systems.fieldid.wicket.pages.setup.eventStatus;

import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.EventStatusPage;
import com.n4systems.model.EventStatus;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EventStatusFormPage extends EventStatusPage{

    @SpringBean
    private EventStatusService eventStatusService;

    public EventStatusFormPage(PageParameters params) {
        super(params);

        Form<Void> form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                EventStatus eventStatus = eventStatusModel.getObject();
                if(eventStatus.isNew()) {
                    eventStatus.setTenant(FieldIDSession.get().getTenant());
                    eventStatusService.create(eventStatus);
                }else {
                    eventStatusService.update(eventStatus);
                }
                setResponsePage(new EventStatusListPage(new PageParameters()));
            }
        };
        RequiredTextField name;
        form.add(name = new RequiredTextField<String>("name", new PropertyModel<String>(eventStatusModel.getObject(), "name")));
        name.add(new EventStatusUniqueNameValidator());

        Button submitButton;
        form.add(submitButton = new Button("saveButton"));
        submitButton.setOutputMarkupId(true);
        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new EventStatusListPage(new PageParameters()));
            }
        });

        add(form);
        
        add(new FIDFeedbackPanel("feedbackPanel"));
    }

}
