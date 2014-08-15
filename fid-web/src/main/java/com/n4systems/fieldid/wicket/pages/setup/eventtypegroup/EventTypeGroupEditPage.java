package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by rrana on 2014-08-12.
 */
public class EventTypeGroupEditPage extends EventTypeGroupPage{


    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    private FIDFeedbackPanel feedbackPanel;
    protected IModel<EventTypeGroup> eventTypeGroupModel;
    private Long eventTypeGroupId;

    private PDFReportStylePanel printOutPanel;
    private ObservationReportStylePanel observationPrintOutPanel;

    public EventTypeGroupEditPage(){
        super();
    }

    public EventTypeGroupEditPage(PageParameters params) {
        super();
        eventTypeGroupModel = Model.of(eventTypeGroupService.getEventTypeGroupById(params.get("uniqueID").toLong()));
        eventTypeGroupId = eventTypeGroupModel.getObject().getId();
    }

    public EventTypeGroupEditPage(EventTypeGroup eventTypeGroup){
        super();
        eventTypeGroupModel = Model.of(eventTypeGroup);
        eventTypeGroupId = eventTypeGroup.getId();
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all")).page(EventTypeGroupListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived")).page(EventTypeGroupListArchivePage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view")).page(EventTypeGroupViewPage.class).params(PageParametersBuilder.uniqueId(eventTypeGroupId)).build(),
                aNavItem().label(new FIDLabelModel("nav.edit")).page(EventTypeGroupEditPage.class).params(PageParametersBuilder.uniqueId(eventTypeGroupId)).build(),
                aNavItem().label(new FIDLabelModel("nav.add")).page(EventTypeGroupAddPage.class).onRight().build()
        ));
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

            eventTypeGroupService.update(eventTypeGroupModel.getObject(), eventTypeGroupService.getUser(FieldIDSession.get().getSessionUser().getId()));
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
