package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by rrana on 2015-01-21.
 */

@Entity
@Table(name="observationcount_criteria")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ObservationCountCriteria extends Criteria {

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="observationcount_group_id")
    private ObservationCountGroup observationCountGroup;

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.OBSERVATION_COUNT;
    }

    public ObservationCountGroup getObservationCountGroup() {
        return observationCountGroup;
    }

    public void setObservationCountGroup(ObservationCountGroup observationCountGroup) {
        this.observationCountGroup = observationCountGroup;
    }

}
