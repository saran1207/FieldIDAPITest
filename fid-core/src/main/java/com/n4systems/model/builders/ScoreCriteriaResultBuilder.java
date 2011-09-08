package com.n4systems.model.builders;

import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.ScoreCriteriaResult;

public class ScoreCriteriaResultBuilder extends BaseBuilder<ScoreCriteriaResult> {

    private final Score score;
    private final ScoreCriteria criteria;

    public static ScoreCriteriaResultBuilder aScoreCriteriaResult() {
        return new ScoreCriteriaResultBuilder(null, null);
    }

    public ScoreCriteriaResultBuilder(Score score, ScoreCriteria criteria) {
        this.score = score;
        this.criteria = criteria;
    }

    public ScoreCriteriaResultBuilder score(Score score) {
        return makeBuilder(new ScoreCriteriaResultBuilder(score, criteria));
    }

    public ScoreCriteriaResultBuilder criteria(ScoreCriteria criteria) {
        return makeBuilder(new ScoreCriteriaResultBuilder(score, criteria));
    }

    @Override
    public ScoreCriteriaResult createObject() {
        ScoreCriteriaResult criteriaResult = new ScoreCriteriaResult();
        criteriaResult.setScore(score);
        criteriaResult.setCriteria(criteria);

        return criteriaResult;
    }

}
