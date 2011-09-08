package com.n4systems.model.builders;

import com.n4systems.model.Score;

public class ScoreBuilder extends BaseBuilder<Score> {

    private final Double value;
    private final boolean na;
    private final String name;

    public ScoreBuilder(Double value, boolean na, String name) {
        this.value = value;
        this.na = na;
        this.name = name;
    }

    public static ScoreBuilder aScore() {
        return new ScoreBuilder(null, false, null);
    }

    public ScoreBuilder value(Double value) {
        return makeBuilder(new ScoreBuilder(value, na, name));
    }

    public ScoreBuilder name(String name) {
        return makeBuilder(new ScoreBuilder(value, na, name));
    }

    public ScoreBuilder na(boolean na) {
        return makeBuilder(new ScoreBuilder(value, na, name));
    }

    @Override
    public Score createObject() {
        Score score = new Score();

        score.setValue(value);
        score.setNa(na);
        score.setName(name);

        return score;
    }
}
