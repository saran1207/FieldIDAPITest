package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="score_criteria")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
