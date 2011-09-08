package com.n4systems.ejb.impl;

import com.n4systems.model.Event;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreCalculationType;
import com.n4systems.model.ScoreCriteriaResult;
import com.n4systems.model.ScoreResultRange;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.EventFormBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.OneClickCriteriaBuilder;
import com.n4systems.model.builders.OneClickCriteriaResultBuilder;
import com.n4systems.model.builders.ScoreBuilder;
import com.n4systems.model.builders.ScoreCriteriaBuilder;
import com.n4systems.model.builders.ScoreCriteriaResultBuilder;
import com.n4systems.model.builders.ScoreResultRangeBuilder;
import com.n4systems.model.builders.StateBuilder;
import com.n4systems.model.builders.StateSetBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventResultCalculatorTest {

    private EventResultCalculator eventResultCalculator;
    private State passState;
    private State failState;
    private State naState;
    private StateSet passFailNaStateSet;

    private ScoreResultRange atLeastTen;
    private ScoreResultRange atMostFive;

    @Before
    public void setup() {
        eventResultCalculator = new EventResultCalculator();
        passState = StateBuilder.aState().displayText("Pass").status(Status.PASS).build();
        failState = StateBuilder.aState().displayText("Fail").status(Status.FAIL).build();
        naState = StateBuilder.aState().displayText("NA").status(Status.NA).build();
        passFailNaStateSet = StateSetBuilder.aStateSet().states(passState, failState, naState).build();

        atLeastTen = ScoreResultRangeBuilder.aScoreResultRange().atLeast(10d).build();
        atMostFive = ScoreResultRangeBuilder.aScoreResultRange().atMost(5d).build();
    }

    @Test
    public void simple_single_non_principal_pass() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(false).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passState).criteria(criteria).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(Status.NA, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_single_principal_pass() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passState).criteria(criteria).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(Status.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void several_criteria_pass() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passState).criteria(criteria).build();

        OneClickCriteria criteria2 = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result2 = OneClickCriteriaResultBuilder.aCriteriaResult().state(passState).criteria(criteria2).build();

        OneClickCriteria criteria3 = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result3 = OneClickCriteriaResultBuilder.aCriteriaResult().state(naState).criteria(criteria3).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2, result3).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(Status.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void several_criteria_fail() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passState).criteria(criteria).build();

        OneClickCriteria criteria2 = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result2 = OneClickCriteriaResultBuilder.aCriteriaResult().state(failState).criteria(criteria2).build();

        OneClickCriteria criteria3 = OneClickCriteriaBuilder.aCriteria().withStateSet(passFailNaStateSet).withPrincipal(true).build();
        OneClickCriteriaResult result3 = OneClickCriteriaResultBuilder.aCriteriaResult().state(naState).criteria(criteria3).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2, result3).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(Status.FAIL, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_fail() {
        Score score = ScoreBuilder.aScore().value(1d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.FAIL, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_pass() {
        Score score = ScoreBuilder.aScore().value(10d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_na() {
        Score score = ScoreBuilder.aScore().value(7d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.NA, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_pass_with_range() {
        Score score = ScoreBuilder.aScore().value(10d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        ScoreResultRange failRange = ScoreResultRangeBuilder.aScoreResultRange().between(0d).and(5d).build();
        ScoreResultRange passRange = ScoreResultRangeBuilder.aScoreResultRange().between(8d).and(10d).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, failRange, passRange);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_average_score_pass() {
        Score score = ScoreBuilder.aScore().value(6d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        Score score2 = ScoreBuilder.aScore().value(20d).build();
        ScoreCriteriaResult result2 = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score2).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.AVERAGE, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_average_score_fail() {
        Score score = ScoreBuilder.aScore().value(0d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        Score score2 = ScoreBuilder.aScore().value(6d).build();
        ScoreCriteriaResult result2 = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score2).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.AVERAGE, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.FAIL, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void longer_sum_score_pass() {
        Score score = ScoreBuilder.aScore().value(1d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        Score score2 = ScoreBuilder.aScore().value(2d).build();
        ScoreCriteriaResult result2 = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score2).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        Score score3 = ScoreBuilder.aScore().value(3d).build();
        ScoreCriteriaResult result3 = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score3).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        Score score4 = ScoreBuilder.aScore().value(4d).build();
        ScoreCriteriaResult result4 = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score4).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2, result3, result4).ofType(eventType).build();
        setEventForm(event);

        assertEquals(Status.PASS, eventResultCalculator.findEventResult(event));
    }

    private EventType createEventType() {
        return createEventType(false, null, null, null);
    }

    private void setEventForm(Event event) {
        event.setEventForm(event.getType().getEventForm());
    }

    private EventType createEventType(boolean useScore, ScoreCalculationType scoreCalculationType, ScoreResultRange failRange, ScoreResultRange passRange) {
        EventForm eventForm = EventFormBuilder.anEventForm().useScore(useScore).scoreCalculationType(scoreCalculationType).failRange(failRange).passRange(passRange).build();
        return EventTypeBuilder.anEventType().withEventForm(eventForm).build();
    }

}
