package com.n4systems.model;

import com.n4systems.util.DoubleFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "score_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
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
