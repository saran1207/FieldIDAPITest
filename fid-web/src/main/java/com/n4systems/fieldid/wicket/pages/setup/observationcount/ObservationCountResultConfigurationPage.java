package com.n4systems.fieldid.wicket.pages.setup.observationcount;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.eventtype.EventTypePage;
import com.n4systems.fieldid.wicket.pages.setup.score.result.ScoreResultRangePanel;
import com.n4systems.model.*;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;

/**
 * Created by rrana on 2015-01-22.
 */
public class ObservationCountResultConfigurationPage extends EventTypePage{

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private ObservationCountService observationCountService;

    @SpringBean
    private EventFormService eventFormService;

    private EventForm eventForm;

    public ObservationCountResultConfigurationPage(PageParameters params) {
        super(params);

        add(new ObservationCountConfigurationForm("observationCountConfigurationForm"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/scoreResult.css");
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        super.storePageParameters(params);
        eventForm = eventTypeModel.getObject().getEventForm();
        if (eventForm == null) {
            eventForm = new EventForm();
        }
    }

    class ObservationCountConfigurationForm extends Form<EventForm> {

        public ObservationCountConfigurationForm(String id) {
            super(id, new CompoundPropertyModel<EventForm>(eventForm));
            add(new FIDFeedbackPanel("feedbackPanel"));

            DropDownChoice<ObservationCount> observationCountDropDownChoicePass = new DropDownChoice<ObservationCount>("observationCountPass", eventForm.getObservationCountGroup().getObservationCounts(), new ObservationCountChoiceRenderer());
            observationCountDropDownChoicePass.setNullValid(true);
            add(observationCountDropDownChoicePass);

            DropDownChoice<ObservationCount> observationCountDropDownChoiceFail = new DropDownChoice<ObservationCount>("observationCountFail", eventForm.getObservationCountGroup().getObservationCounts(), new ObservationCountChoiceRenderer());
            observationCountDropDownChoiceFail.setNullValid(true);
            add(observationCountDropDownChoiceFail);

            DropDownChoice<ObservationCountGroup> observationCountGroupDropDownChoice = new DropDownChoice<ObservationCountGroup>("observationCountGroup", observationCountService.getObservationCountGroups(), new ObservationCountGroupChoiceRenderer()) {
                @Override
                protected boolean wantOnSelectionChangedNotifications() {
                    return true;
                }

                @Override
                protected void onSelectionChanged(ObservationCountGroup group){
                    eventForm.setObservationCountGroup(group);
                    observationCountDropDownChoiceFail.setChoices(group.getObservationCounts());
                    observationCountDropDownChoicePass.setChoices(group.getObservationCounts());
                    observationCountDropDownChoiceFail.getModel().setObject(null);
                    observationCountDropDownChoicePass.getModel().setObject(null);
                }
            };

            add(observationCountGroupDropDownChoice);

            add(new CheckBox("useObservationCountForResult", new PropertyModel<Boolean>(eventForm, "useObservationCountForResult")));

            add(new DropDownChoice<ScoreCalculationType>("observationcountPassCalculationType", Arrays.asList(ScoreCalculationType.values()), new CalculationChoiceRenderer()));
            add(new DropDownChoice<ScoreCalculationType>("observationcountFailCalculationType", Arrays.asList(ScoreCalculationType.values()), new CalculationChoiceRenderer()));

            add(new ScoreResultRangePanel("passRangePanel", new PropertyModel<ResultRange>(eventForm, "observationcountPassRange")));
            add(new ScoreResultRangePanel("failRangePanel", new PropertyModel<ResultRange>(eventForm, "observationcountFailRange")));

            add(new NonWicketLink("cancelLink", "eventType.action?uniqueID="+eventTypeId));
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            //Validate for null Observation Group and Observation selection
            if(eventForm.getObservationCountGroup() == null){
                getTopFeedbackPanel().error(new FIDLabelModel("error.select_observation").getObject());
            } else if (eventForm.getObservationCountFail() == null || eventForm.getObservationCountPass() == null) {
                getTopFeedbackPanel().error(new FIDLabelModel("error.select_pass_fail_observation_count").getObject());
            }
            else {
                EventType eventType = eventTypeModel.getObject();
                if (eventType.getEventForm() == null) {
                    eventForm.setTenant(getTenant());
                    eventType.setEventForm(eventForm);
                    persistenceService.update(eventType);
                    persistenceService.update(eventForm);
                } else {
                    eventFormService.saveNewEventFormAfterObservationChange(eventForm, eventForm.getSections(), eventType);
                }
                FieldIDSession.get().storeInfoMessageForStruts(new FIDLabelModel("label.observation_config_saved").getObject());
                throw new RedirectToUrlException("/eventType.action?uniqueID=" + eventTypeId);
            }
        }

    }


    static class ObservationCountChoiceRenderer implements IChoiceRenderer<ObservationCount> {
        @Override
        public Object getDisplayValue(ObservationCount object) {
            return new FIDLabelModel(object.getName()).getObject();
        }

        @Override
        public String getIdValue(ObservationCount object, int index) {
            return object.getName();
        }
    }

    static class CalculationChoiceRenderer implements IChoiceRenderer<ScoreCalculationType> {
        @Override
        public Object getDisplayValue(ScoreCalculationType object) {
            if(object.getLabel().contains("sum")) {
                return new FIDLabelModel("Total").getObject();
            } else {
                return new FIDLabelModel("Percentage").getObject();
            }
        }

        @Override
        public String getIdValue(ScoreCalculationType object, int index) {
            return object.name();
        }
    }

    static class ObservationCountGroupChoiceRenderer implements IChoiceRenderer<ObservationCountGroup> {
        @Override
        public Object getDisplayValue(ObservationCountGroup object) {
            return new FIDLabelModel(object.getName()).getObject();
        }

        @Override
        public String getIdValue(ObservationCountGroup object, int index) {
            return object.getName();
        }
    }

}
