package com.n4systems.fieldid.wicket.components.score;

import com.n4systems.model.Score;
import com.n4systems.model.ScoreGroup;

public class ScoreGroupCopyUtil {

    public ScoreGroup copy(ScoreGroup scoreGroup) {
        ScoreGroup copiedGroup = new ScoreGroup();
        copiedGroup.setTenant(scoreGroup.getTenant());

        copiedGroup.setName(scoreGroup.getName());

        for (Score score : scoreGroup.getScores()) {
            Score copiedScore = new Score();
            copiedScore.setTenant(score.getTenant());
            copiedScore.setName(score.getName());
            copiedScore.setValue(score.getValue());
            copiedScore.setNa(score.isNa());
            copiedScore.setState(score.getEntityState());

            copiedGroup.getScores().add(copiedScore);
        }

        return copiedGroup;
    }

}
