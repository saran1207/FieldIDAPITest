package com.n4systems.model;

import javax.persistence.*;

/**
 * Created by rrana on 2015-01-21.
 */

@Entity
@Table(name="observationcount_criteria")
@PrimaryKeyJoinColumn(name="id")
public class ObservationCountCriteria extends Criteria {

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="observationcount_group_id")
    private ObservationCountGroup observationCountGroup;

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.OBSERVATION_COUNT;
    }

    public ObservationCountGroup getScoreGroup() {
        return observationCountGroup;
    }

    public void setScoreGroup(ObservationCountGroup observationCountGroup) {
        this.observationCountGroup = observationCountGroup;
    }

}
