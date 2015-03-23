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

import java.util.ArrayList;
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

    private CheckBox useObservationCountForResultCheckbox;

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
        eventForm = new EventForm();
        if (eventForm != eventTypeModel.getObject().getEventForm()) {
            eventForm.setTenant(eventTypeModel.getObject().getTenant());
            eventForm = eventFormService.copyEventFormSettings(eventTypeModel.getObject().getEventForm(), eventForm);
            eventForm.setSections(eventTypeModel.getObject().getEventForm().getSections());
        }
    }

    class ObservationCountConfigurationForm extends Form<EventForm> {

        public ObservationCountConfigurationForm(String id) {
            super(id, new CompoundPropertyModel<>(eventForm));
            add(new FIDFeedbackPanel("feedbackPanel"));

            DropDownChoice<ObservationCount> observationCountDropDownChoicePass;
            DropDownChoice<ObservationCount> observationCountDropDownChoiceFail;
            ScoreResultRangePanel passRangePanel;
            ScoreResultRangePanel failRangePanel;

            //Populate depending on whether the eventform has a group associated with it.
            if (eventForm.getObservationCountGroup() == null) {
                observationCountDropDownChoicePass = new DropDownChoice<>("observationCountPass", new ArrayList<>(), new ObservationCountChoiceRenderer());
                observationCountDropDownChoiceFail = new DropDownChoice<>("observationCountFail", new ArrayList<>(), new ObservationCountChoiceRenderer());
            } else {
                observationCountDropDownChoicePass = new DropDownChoice<>("observationCountPass", eventForm.getObservationCountGroup().getObservationCounts(), new ObservationCountChoiceRenderer());
                observationCountDropDownChoiceFail = new DropDownChoice<>("observationCountFail", eventForm.getObservationCountGroup().getObservationCounts(), new ObservationCountChoiceRenderer());
            }

            observationCountDropDownChoicePass.setNullValid(true);
            add(observationCountDropDownChoicePass);

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

            add(useObservationCountForResultCheckbox = new CheckBox("useObservationCountForResult", new PropertyModel<Boolean>(eventForm, "useObservationCountForResult")) {
                //Force the CheckBox to notify us when the value changes.  This is important.  Failing to do this makes
                //the component essentially unaware of its state.
                @Override
                protected boolean wantOnSelectionChangedNotifications() {
                    return true;
                }
            });
            add(new CheckBox("displayScoreSectionTotals", new PropertyModel<>(eventTypeModel, "displayObservationSectionTotals")));
            add(new CheckBox("displayObservationPercentage", new PropertyModel<>(eventTypeModel, "displayObservationPercentage")));

            boolean passPercentage = eventForm.getObservationcountPassCalculationType().equals(ScoreCalculationType.AVERAGE);
            passRangePanel = new ScoreResultRangePanel("passRangePanel", new PropertyModel<ResultRange>(eventForm, "observationcountPassRange"), passPercentage) {
                @Override
                protected boolean isValidationRequired() {
                    return useObservationCountForResultCheckbox.getModelObject();
                }
            };
            add(passRangePanel);

            boolean failPercentage = eventForm.getObservationcountFailCalculationType().equals(ScoreCalculationType.AVERAGE);
            failRangePanel = new ScoreResultRangePanel("failRangePanel", new PropertyModel<ResultRange>(eventForm, "observationcountFailRange"), failPercentage){
                @Override
                protected boolean isValidationRequired() {
                    return useObservationCountForResultCheckbox.getModelObject();
                }
            };
            add(failRangePanel);

            DropDownChoice<ScoreCalculationType> passCalculationType = new DropDownChoice<ScoreCalculationType>("observationcountPassCalculationType", Arrays.asList(ScoreCalculationType.values()), new CalculationChoiceRenderer()) {
                @Override
                protected boolean wantOnSelectionChangedNotifications() {
                    return true;
                }

                @Override
                protected void onSelectionChanged(ScoreCalculationType type){
                    if(type == ScoreCalculationType.AVERAGE) {
                        passRangePanel.showPercentage(true);
                    } else {
                        passRangePanel.showPercentage(false);
                    }
                }
            };
            add(passCalculationType);

            DropDownChoice<ScoreCalculationType> failCalculationType = new DropDownChoice<ScoreCalculationType>("observationcountFailCalculationType", Arrays.asList(ScoreCalculationType.values()), new CalculationChoiceRenderer()) {
                @Override
                protected boolean wantOnSelectionChangedNotifications() {
                    return true;
                }

                @Override
                protected void onSelectionChanged(ScoreCalculationType type){
                    if(type == ScoreCalculationType.AVERAGE) {
                        failRangePanel.showPercentage(true);
                    } else {
                        failRangePanel.showPercentage(false);
                    }
                }
            };
            add(failCalculationType);

            add(new NonWicketLink("cancelLink", "eventType.action?uniqueID="+eventTypeId));
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            //Validate for null Observation Group and Observation selection
            if(eventForm.getObservationCountGroup() == null){
                getTopFeedbackPanel().error(new FIDLabelModel("error.select_observation").getObject());
            } else if (useObservationCountForResultCheckbox.getModelObject() && (eventForm.getObservationCountFail() == null || eventForm.getObservationCountPass() == null)) {
                getTopFeedbackPanel().error(new FIDLabelModel("error.select_pass_fail_observation_count").getObject());
            }
            else {
                EventType eventType = eventTypeModel.getObject();
                if (eventType.getEventForm() == null) {
                    eventForm.setTenant(getTenant());
                    eventType.setEventForm(eventForm);
                    eventTypeService.update(eventType);
                    persistenceService.update(eventForm);
                } else {
                    eventTypeService.update(eventType);
                    eventFormService.saveNewEventFormAfterObservationChange(eventType.getId(), eventForm);
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
