package com.n4systems.model.builders;

import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.State;

public class OneClickCriteriaResultBuilder extends BaseBuilder<OneClickCriteriaResult> {

    private final OneClickCriteria criteria;
    private final State state;

    public static OneClickCriteriaResultBuilder aCriteriaResult() {
        return new OneClickCriteriaResultBuilder(null, null);
    }

    public OneClickCriteriaResultBuilder(OneClickCriteria criteria, State state) {
        this.criteria = criteria;
        this.state = state;
    }

    public OneClickCriteriaResultBuilder state(State state) {
        return makeBuilder(new OneClickCriteriaResultBuilder(criteria, state));
    }

    public OneClickCriteriaResultBuilder criteria(OneClickCriteria criteria) {
        return makeBuilder(new OneClickCriteriaResultBuilder(criteria, state));
    }

    @Override
    public OneClickCriteriaResult createObject() {
        OneClickCriteriaResult criteriaResult = new OneClickCriteriaResult();
        criteriaResult.setCriteria(criteria);
        criteriaResult.setState(state);

        return criteriaResult;
    }

}
