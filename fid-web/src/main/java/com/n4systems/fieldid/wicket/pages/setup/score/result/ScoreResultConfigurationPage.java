package com.n4systems.fieldid.wicket.pages.setup.score.result;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.eventtype.EventTypePage;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.ScoreCalculationType;
import com.n4systems.model.ScoreResultRange;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;

public class ScoreResultConfigurationPage extends EventTypePage {

    @SpringBean
    private PersistenceService persistenceService;

    private EventForm eventForm;

    public ScoreResultConfigurationPage(PageParameters params) {
        super(params);

        add(new ScoreConfigurationForm("scoreConfigurationForm"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/scoreResult.css");
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        super.storePageParameters(params);
        eventForm = eventTypeModel.getObject().getEventForm();
        if (eventForm == null) {
            eventForm = new EventForm();
        }
    }

    class ScoreConfigurationForm extends Form<EventForm> {

        public ScoreConfigurationForm(String id) {
            super(id, new CompoundPropertyModel<EventForm>(eventForm));
            add(new FIDFeedbackPanel("feedbackPanel"));

            add(new CheckBox("useScoreForResult"));
            add(new DropDownChoice<ScoreCalculationType>("scoreCalculationType", Arrays.asList(ScoreCalculationType.values()), new CalculationChoiceRenderer()));
            add(new ScoreResultRangePanel("failRangePanel", new PropertyModel<ScoreResultRange>(eventForm, "failRange")));
            add(new ScoreResultRangePanel("passRangePanel", new PropertyModel<ScoreResultRange>(eventForm, "passRange")));

            add(new NonWicketLink("cancelLink", "eventType.action?uniqueID="+eventTypeId));
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            EventType eventType = eventTypeModel.getObject();
            if (eventType.getEventForm() == null) {
                eventForm.setTenant(getTenant());
                eventType.setEventForm(eventForm);
                persistenceService.update(eventType);
            }
            persistenceService.update(eventForm);
            FieldIDSession.get().storeInfoMessageForStruts(new FIDLabelModel("label.score_config_saved").getObject());
            throw new RedirectToUrlException("/eventType.action?uniqueID=" + eventTypeId);
        }

    }

    static class CalculationChoiceRenderer implements IChoiceRenderer<ScoreCalculationType> {
        @Override
        public Object getDisplayValue(ScoreCalculationType object) {
            return new FIDLabelModel(object.getLabel()).getObject();
        }

        @Override
        public String getIdValue(ScoreCalculationType object, int index) {
            return object.name();
        }
    }

}
