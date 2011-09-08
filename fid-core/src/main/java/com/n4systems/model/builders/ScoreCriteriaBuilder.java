package com.n4systems.model.builders;

import com.n4systems.model.ScoreCriteria;

public class ScoreCriteriaBuilder extends BaseBuilder<ScoreCriteria> {

    public static ScoreCriteriaBuilder aScoreCriteria() {
        return new ScoreCriteriaBuilder();
    }

    @Override
    public ScoreCriteria createObject() {
        return new ScoreCriteria();
    }

}
