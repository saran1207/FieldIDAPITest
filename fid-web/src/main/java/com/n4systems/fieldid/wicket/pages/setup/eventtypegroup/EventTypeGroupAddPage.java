package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.text.LabelledRequiredTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.EventTypeGroup;
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

    private PDFReportStylePanel printOutPanel;
    private ObservationReportStylePanel observationPrintOutPanel;

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

            printOutPanel = new PDFReportStylePanel("printOutSection", eventTypeGroupModel.getObject().getPrintOut());
            add(printOutPanel);

            observationPrintOutPanel = new ObservationReportStylePanel("printOutSection2", eventTypeGroupModel.getObject().getObservationPrintOut());
            add(observationPrintOutPanel);

            Button saveButton = new Button("saveButton");
            saveButton.add(new DisableButtonBeforeSubmit());
            add(saveButton);
            add(createCancelLink("cancelLink"));
        }


        @Override
        protected void onSubmit() {
            eventTypeGroupModel.getObject().setPrintOut(printOutPanel.getPrintOut());
            eventTypeGroupModel.getObject().setObservationPrintOut(observationPrintOutPanel.getPrintOut());

            if(eventTypeGroupService.exists(eventTypeGroupModel.getObject().getName())) {
                FieldIDSession.get().error(new FIDLabelModel("error.eventtypegroupnameduplicated").getObject());
            } else {
                EventTypeGroup createdEvent = eventTypeGroupService.create(eventTypeGroupModel.getObject(), eventTypeGroupService.getUser(FieldIDSession.get().getSessionUser().getId()), FieldIDSession.get().getTenant());
                FieldIDSession.get().info(new FIDLabelModel("message.eventtypegroupsaved").getObject());
                setResponsePage(EventTypeGroupViewPage.class, PageParametersBuilder.uniqueId(createdEvent.getID()));
            }
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
