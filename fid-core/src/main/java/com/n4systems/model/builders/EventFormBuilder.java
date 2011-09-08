package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.ScoreCalculationType;
import com.n4systems.model.ScoreResultRange;

public class EventFormBuilder extends EntityWithTenantBuilder<EventForm> {

    private final List<CriteriaSection> sections;
    private final boolean useScore;
    private final ScoreCalculationType scoreCalculationType;
    private final ScoreResultRange failRange;
    private final ScoreResultRange passRange;

    public EventFormBuilder(List<CriteriaSection> sections, boolean useScore, ScoreCalculationType scoreCalculationType, ScoreResultRange failRange, ScoreResultRange passRange) {
        this.sections = sections;
        this.useScore = useScore;
        this.scoreCalculationType = scoreCalculationType;
        this.failRange = failRange;
        this.passRange = passRange;
    }

    public static EventFormBuilder anEventForm() {
        return new EventFormBuilder(new ArrayList<CriteriaSection>(), false, ScoreCalculationType.SUM, null, null);
    }

    public EventFormBuilder withSections(CriteriaSection... sections) {
        return makeBuilder(new EventFormBuilder(listOf(sections), useScore, scoreCalculationType, failRange, passRange));
    }

    public EventFormBuilder useScore(boolean useScore) {
        return makeBuilder(new EventFormBuilder(sections, useScore, scoreCalculationType, failRange, passRange));
    }

    public EventFormBuilder scoreCalculationType(ScoreCalculationType scoreCalculationType) {
        return makeBuilder(new EventFormBuilder(sections, useScore, scoreCalculationType, failRange, passRange));
    }

    public EventFormBuilder failRange(ScoreResultRange failRange) {
        return makeBuilder(new EventFormBuilder(sections, useScore, scoreCalculationType, failRange, passRange));
    }

    public EventFormBuilder passRange(ScoreResultRange passRange) {
        return makeBuilder(new EventFormBuilder(sections, useScore, scoreCalculationType, failRange, passRange));
    }


    @Override 
    public EventForm createObject() {
        EventForm eventForm = new EventForm();
        eventForm.setUseScoreForResult(useScore);
        eventForm.setScoreCalculationType(scoreCalculationType);
        eventForm.setFailRange(failRange);
        eventForm.setPassRange(passRange);

        for (CriteriaSection section : sections) {
            eventForm.getSections().add(section);
        }

        assignAbstractFields(eventForm);

        return eventForm;
    }

}
