package com.n4systems.ejb.impl;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaType;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.ScoreCalculationType;
import com.n4systems.model.ScoreComparator;
import com.n4systems.model.ScoreCriteriaResult;
import com.n4systems.model.ScoreResultRange;
import com.n4systems.model.Status;

public class EventResultCalculator {

    public Status findEventResult(AbstractEvent event) {
        if (event.getEventForm() == null) {
            return Status.NA;
        }

        Double score = calculateScore(event);
        event.setScore(score);

        if (event.getEventForm().isUseScoreForResult()) {
            return findResultFromScore(event, score);
        } else {
            return findResultFromOneClicks(event);
        }
	}

    private Status findResultFromScore(AbstractEvent event, Double score) {
        if (insideRange(event.getEventForm().getFailRange(), score)) {
            return Status.FAIL;
        } else if (insideRange(event.getEventForm().getPassRange(), score)) {
            return Status.PASS;
        }
        return Status.NA;
    }

    private Double calculateScore(AbstractEvent event) {
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

    private boolean insideRange(ScoreResultRange range, Double score) {
        if (score == null) {
            return false;
        }

        if (range.getComparator() == ScoreComparator.LE) {
            return score <= range.getValue1();
        } else if (range.getComparator() == ScoreComparator.GE) {
            return score >= range.getValue1();
        } else {
            return score >= range.getValue1() && score <= range.getValue2();
        }
    }

    private Status findResultFromOneClicks(AbstractEvent event) {
        Status eventResult = Status.NA;
        for (CriteriaResult result : event.getResults()) {
            if (result instanceof OneClickCriteriaResult) {
                OneClickCriteriaResult oneClickResult = (OneClickCriteriaResult) result;

                eventResult = adjustStatus(eventResult, oneClickResult.getResult());

                if (eventResult == Status.FAIL) {
                    break;
                }
            }

        }
        return eventResult;
    }

    public Status adjustStatus(Status currentStatus, Status newStatus) {
        if (newStatus == Status.FAIL) {
			currentStatus = Status.FAIL;
		} else if (newStatus == Status.PASS) {
			currentStatus = Status.PASS;
		}
		return currentStatus;
    }

}
