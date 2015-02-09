package com.n4systems.ejb.impl;

import com.n4systems.model.*;
import com.n4systems.model.EventResult;

public class EventResultCalculator {

    public EventResult findEventResult(AbstractEvent event) {
        if (event.getEventForm() == null) {
            return EventResult.NA;
        }

        ObservationResult observationCountResult = calculateObservationCount(event);
        event.setFailObservations(observationCountResult.failResult);
        event.setPassObservations(observationCountResult.failResult);

        Double score = calculateScore(event);
        event.setScore(score);

        if(event.getEventForm().isUseObservationCountForResult()) {
            return findResultFromObservationCount(event, observationCountResult);
        }else if (event.getEventForm().isUseScoreForResult()) {
            return findResultFromScore(event, score);
        } else {
            return findResultFromOneClicks(event);
        }
	}

    private EventResult findResultFromObservationCount(AbstractEvent event, ObservationResult observationCountResult) {
        ScoreCalculationType failCalculationType = event.getType().getEventForm().getObservationcountFailCalculationType();
        ScoreCalculationType passCalculationType = event.getType().getEventForm().getObservationcountPassCalculationType();
        if (insideRange(event.getEventForm().getObservationcountFailRange(), observationCountResult.failResult, failCalculationType)) {
            return EventResult.FAIL;
        } else if (insideRange(event.getEventForm().getObservationcountPassRange(), observationCountResult.passResult, passCalculationType)) {
            return EventResult.PASS;
        }
        return EventResult.NA;
    }

    private EventResult findResultFromScore(AbstractEvent event, Double score) {
        ScoreCalculationType scoreCalculationType = event.getType().getEventForm().getScoreCalculationType();
        if (insideRange(event.getEventForm().getFailRange(), score, scoreCalculationType)) {
            return EventResult.FAIL;
        } else if (insideRange(event.getEventForm().getPassRange(), score, scoreCalculationType)) {
            return EventResult.PASS;
        }
        return EventResult.NA;
    }

    private ObservationResult calculateObservationCount(AbstractEvent<?,?> event) {

        double totalObservations = 0;
        double totalFail = 0;
        double totalPass = 0;

        ObservationResult result = new ObservationResult();

        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult.getCriteria().getCriteriaType() == CriteriaType.OBSERVATION_COUNT) {
                for (ObservationCountResult count: ((ObservationCountCriteriaResult) criteriaResult).getObservationCountResults()) {
                    if (count.getObservationCount().isCounted()) {
                        totalObservations += count.getValue();

                        if(count.getObservationCount().equals(event.getEventForm().getObservationCountFail())) {
                            totalFail += count.getValue();
                        }

                        if(count.getObservationCount().equals(event.getEventForm().getObservationCountPass())) {
                            totalPass += count.getValue();
                        }
                    }
                }
            }
        }

        if (event.getEventForm().getObservationcountFailCalculationType().equals(ScoreCalculationType.AVERAGE)) {
            result.failResult = totalFail / totalObservations;
        } else {
            result.failResult = totalFail;
        }

        if (event.getEventForm().getObservationcountPassCalculationType().equals(ScoreCalculationType.AVERAGE)) {
            result.passResult = totalPass / totalObservations;
        } else {
            result.passResult = totalPass;
        }
        return result;
    }

    private Double calculateScore(AbstractEvent<?,?> event) {
        double total = 0d;
        int count = 0;
        int countScoreCriteria = 0;
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult.getCriteria().getCriteriaType() == CriteriaType.SCORE) {
                countScoreCriteria++;
                ScoreCriteriaResult scoreResult = (ScoreCriteriaResult) criteriaResult;
                if (scoreResult.getScore() != null && !scoreResult.getScore().isNa()) {
                    total += scoreResult.getScore().getValue();
                    count += 1;
                }
            }
        }
        if (event.getEventForm().getScoreCalculationType() == ScoreCalculationType.AVERAGE && count > 0) {
            total /= count;
        }
        if (countScoreCriteria == 0) {
            return null;
        }
        return total;
    }

    private boolean insideRange(ResultRange range, Double score, ScoreCalculationType scoreCalculationType) {
        if (score == null) {
            return false;
        }

        if(scoreCalculationType.equals(ScoreCalculationType.AVERAGE)) {
            score *= 100;
        }

        if (range.getComparator() == ScoreComparator.LE) {
            return score <= range.getValue1();
        } else if (range.getComparator() == ScoreComparator.GE) {
            return score >= range.getValue1();
        } else {
            return score >= range.getValue1() && score <= range.getValue2();
        }
    }

    private EventResult findResultFromOneClicks(AbstractEvent<?,?> event) {
        EventResult eventResult = EventResult.NA;
        for (CriteriaResult result : event.getResults()) {
            if (result instanceof OneClickCriteriaResult) {
                OneClickCriteriaResult oneClickResult = (OneClickCriteriaResult) result;

                eventResult = adjustStatus(eventResult, oneClickResult.getResult());

                if (eventResult == EventResult.FAIL) {
                    break;
                }
            }

        }
        return eventResult;
    }

    public EventResult adjustStatus(EventResult currentEventResult, EventResult newEventResult) {
        if (currentEventResult == EventResult.FAIL || newEventResult == EventResult.FAIL) {
			currentEventResult = EventResult.FAIL;
		} else if (newEventResult == EventResult.PASS) {
			currentEventResult = EventResult.PASS;
		}
		return currentEventResult;
    }

    private class ObservationResult {
        protected double failResult = 0;
        protected double passResult = 0;
    }

}
