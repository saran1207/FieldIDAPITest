package com.n4systems.model.builders;

import com.n4systems.model.ScoreComparator;
import com.n4systems.model.ScoreResultRange;

public class ScoreResultRangeBuilder extends BaseBuilder<ScoreResultRange> {

    private final Double value1;
    private final Double value2;
    private final ScoreComparator comparator;

    public ScoreResultRangeBuilder(Double value1, Double value2, ScoreComparator comparator) {
        this.value1 = value1;
        this.value2 = value2;
        this.comparator = comparator;
    }

    public static ScoreResultRangeBuilder aScoreResultRange() {
        return new ScoreResultRangeBuilder(null, null, null);
    }

    public ScoreResultRangeBuilder atLeast(Double value) {
        return makeBuilder(new ScoreResultRangeBuilder(value, value2, ScoreComparator.GE));
    }

    public ScoreResultRangeBuilder atMost(Double value) {
        return makeBuilder(new ScoreResultRangeBuilder(value, value2, ScoreComparator.LE));
    }

    public ScoreResultRangeBuilder between(Double value) {
        return makeBuilder(new ScoreResultRangeBuilder(value, value2, ScoreComparator.BETWEEN));
    }

    public ScoreResultRangeBuilder and(Double value2) {
        return makeBuilder(new ScoreResultRangeBuilder(value1, value2, comparator));
    }

    @Override
    public ScoreResultRange createObject() {
        ScoreResultRange range = new ScoreResultRange();

        range.setValue1(value1);
        range.setValue2(value2);
        range.setComparator(comparator);

        return range;
    }

}
