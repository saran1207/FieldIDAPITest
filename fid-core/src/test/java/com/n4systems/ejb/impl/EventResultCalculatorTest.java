package com.n4systems.ejb.impl;

import com.n4systems.model.*;
import com.n4systems.model.EventResult;
import com.n4systems.model.builders.*;
import com.n4systems.model.builders.ButtonGroupBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventResultCalculatorTest {

    private EventResultCalculator eventResultCalculator;
    private Button passButton;
    private Button failButton;
    private Button naButton;
    private ButtonGroup passFailNaButtonGroup;

    private ScoreResultRange atLeastTen;
    private ScoreResultRange atMostFive;

    @Before
    public void setup() {
        eventResultCalculator = new EventResultCalculator();
        passButton = StateBuilder.aState().displayText("Pass").status(EventResult.PASS).build();
        failButton = StateBuilder.aState().displayText("Fail").status(EventResult.FAIL).build();
        naButton = StateBuilder.aState().displayText("NA").status(EventResult.NA).build();
        passFailNaButtonGroup = ButtonGroupBuilder.aButtonGroup().buttons(passButton, failButton, naButton).build();

        atLeastTen = ScoreResultRangeBuilder.aScoreResultRange().atLeast(10d).build();
        atMostFive = ScoreResultRangeBuilder.aScoreResultRange().atMost(5d).build();
    }

    @Test
    public void simple_single_non_principal_pass() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(false).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passButton).criteria(criteria).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(EventResult.NA, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_single_principal_pass() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passButton).criteria(criteria).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(EventResult.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void several_criteria_pass() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passButton).criteria(criteria).build();

        OneClickCriteria criteria2 = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result2 = OneClickCriteriaResultBuilder.aCriteriaResult().state(passButton).criteria(criteria2).build();

        OneClickCriteria criteria3 = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result3 = OneClickCriteriaResultBuilder.aCriteriaResult().state(naButton).criteria(criteria3).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2, result3).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(EventResult.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void several_criteria_fail() {
        OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result = OneClickCriteriaResultBuilder.aCriteriaResult().state(passButton).criteria(criteria).build();

        OneClickCriteria criteria2 = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result2 = OneClickCriteriaResultBuilder.aCriteriaResult().state(failButton).criteria(criteria2).build();

        OneClickCriteria criteria3 = OneClickCriteriaBuilder.aCriteria().withButtonGroup(passFailNaButtonGroup).withPrincipal(true).build();
        OneClickCriteriaResult result3 = OneClickCriteriaResultBuilder.aCriteriaResult().state(naButton).criteria(criteria3).build();

        Event event = EventBuilder.anEvent().withCriteriaResults(result, result2, result3).ofType(createEventType()).build();
        setEventForm(event);

        assertEquals(EventResult.FAIL, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_fail() {
        Score score = ScoreBuilder.aScore().value(1d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(EventResult.FAIL, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_pass() {
        Score score = ScoreBuilder.aScore().value(10d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(EventResult.PASS, eventResultCalculator.findEventResult(event));
    }

    @Test
    public void simple_sum_score_na() {
        Score score = ScoreBuilder.aScore().value(7d).build();
        ScoreCriteriaResult result = ScoreCriteriaResultBuilder.aScoreCriteriaResult().score(score).criteria(ScoreCriteriaBuilder.aScoreCriteria().build()).build();

        EventType eventType = createEventType(true, ScoreCalculationType.SUM, atMostFive, atLeastTen);
        Event event = EventBuilder.anEvent().withCriteriaResults(result).ofType(eventType).build();
        setEventForm(event);

        assertEquals(EventResult.NA, eventResultCalculator.findEventResult(event));
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

        assertEquals(EventResult.PASS, eventResultCalculator.findEventResult(event));
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

        assertEquals(EventResult.PASS, eventResultCalculator.findEventResult(event));
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

        assertEquals(EventResult.FAIL, eventResultCalculator.findEventResult(event));
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

        assertEquals(EventResult.PASS, eventResultCalculator.findEventResult(event));
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
