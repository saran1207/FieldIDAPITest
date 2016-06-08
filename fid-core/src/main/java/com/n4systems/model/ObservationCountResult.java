package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by rrana on 2015-02-02.
 */

@SuppressWarnings("serial")
@Entity
@Table(name="observationcount_result")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ObservationCountResult extends ArchivableEntityWithTenant {

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="observationcount_id")
    private ObservationCount observationCount;

    @Column(name="value")
    private int value;

    public ObservationCount getObservationCount() {
        return observationCount;
    }

    public void setObservationCount(ObservationCount observationCount){
        this.observationCount = observationCount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
