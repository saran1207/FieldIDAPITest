package com.n4systems.model;

import javax.persistence.*;

@Entity
@Table(name="score_criteria")
@PrimaryKeyJoinColumn(name="id")
public class ScoreCriteria extends Criteria {

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="group_id")
    private ScoreGroup scoreGroup;

    public ScoreCriteria() {
        super();
        setRequired(true);
    }

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.SCORE;
    }

    public ScoreGroup getScoreGroup() {
        return scoreGroup;
    }

    public void setScoreGroup(ScoreGroup scoreGroup) {
        this.scoreGroup = scoreGroup;
    }
}
