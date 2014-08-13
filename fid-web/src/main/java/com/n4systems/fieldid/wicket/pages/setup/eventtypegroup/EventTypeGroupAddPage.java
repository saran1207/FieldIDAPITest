package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.text.LabelledRequiredTextField;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-08-06.
 */
public class EventTypeGroupAddPage extends EventTypeGroupPage {

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    private FIDFeedbackPanel feedbackPanel;
    protected IModel<EventTypeGroup> eventTypeGroupModel;

    public EventTypeGroupAddPage(){
        super();
        eventTypeGroupModel = Model.of(eventTypeGroupService.getNewEventTypeGroup());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        OuterEventForm form;
        add(form = new OuterEventForm("outerEventForm"){

            private static final long serialVersionUID = 1L;

            @Override
            protected void onValidate() {
                super.onValidate();
            }
        });

    }

    class OuterEventForm extends Form {

        public OuterEventForm(String id) {
            super(id);

            add(new LabelledRequiredTextField<String>("name", "label.name", new PropertyModel<String>(eventTypeGroupModel, "name")));

            add(new LabelledRequiredTextField<String>("reportTitle", "label.report_title", new PropertyModel<String>(eventTypeGroupModel, "reportTitle")));

            add(new PDFReportStylePanel("printOutSection", eventTypeGroupModel, PrintOut.PrintOutType.CERT));

            add(new PDFReportStylePanel("printOutSection2", eventTypeGroupModel, PrintOut.PrintOutType.OBSERVATION));

            Button saveButton = new Button("saveButton");
            saveButton.add(new DisableButtonBeforeSubmit());
            add(saveButton);
            add(createCancelLink("cancelLink"));
        }


        @Override
        protected void onSubmit() {
            if(eventTypeGroupModel.getObject().getPrintOut().getName().equals("No Thanks, I do not need a PDF Report for this Event Type Group.")) {
                eventTypeGroupModel.getObject().setPrintOut(null);
            }
            if(eventTypeGroupModel.getObject().getObservationPrintOut().getName().equals("No Thanks, I do not need a PDF Observation Report for this Event Type Group.")) {
                eventTypeGroupModel.getObject().setObservationPrintOut(null);
            }
            eventTypeGroupService.create(eventTypeGroupModel.getObject(), eventTypeGroupService.getUser(FieldIDSession.get().getSessionUser().getId()), FieldIDSession.get().getTenant());
            setResponsePage(new EventTypeGroupListPage());
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/event/event_base.css");
        response.renderCSSReference("style/legacy/newCss/event/event_schedule.css");
        response.renderCSSReference("style/legacy/pageStyles/eventTypeGroup.css");
        response.renderJavaScriptReference("javascript/prototype.js");
        response.renderJavaScriptReference("javascript/eventTypeGroup.js");
    }

    protected Component createCancelLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                setResponsePage(new EventTypeGroupListPage());
            }
        };
    }

}
