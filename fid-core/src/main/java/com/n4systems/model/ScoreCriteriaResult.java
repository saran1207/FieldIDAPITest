package com.n4systems.model;

import com.n4systems.util.DoubleFormatter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "score_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ScoreCriteriaResult extends CriteriaResult {

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="score_id")
    private Score score;

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

	@Override
	public String getResultString() {
        if (score == null) {
            return "";
        }
        return score.isNa() ? "N/A" : DoubleFormatter.simplifyDouble(score.getValue());
	}

}
