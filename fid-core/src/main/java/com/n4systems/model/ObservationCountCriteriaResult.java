package com.n4systems.model;

import com.n4systems.util.DoubleFormatter;

import javax.persistence.*;

/**
 * Created by rrana on 2015-01-21.
 */

@Entity
@Table(name = "observationcount_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class ObservationCountCriteriaResult extends CriteriaResult{

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="observationcount_id")
    private ObservationCount observationCount;

    public ObservationCount getObservationCount() {
        return observationCount;
    }

    public void setObservationCount(ObservationCount score) {
        this.observationCount = score;
    }

    @Override
    public String getResultString() {
        return "";
    }
}
