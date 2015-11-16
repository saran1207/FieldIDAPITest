package com.n4systems.model;

import com.n4systems.model.api.Listable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2015-01-21.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "observationcount_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class ObservationCountCriteriaResult extends CriteriaResult implements Listable {

    @OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name="observationcount_criteriaresult_observationcountsresults", joinColumns = @JoinColumn(name="observationcountcriteriaresult_id"), inverseJoinColumns = @JoinColumn(name="observationcountresult_id"))
    @OrderColumn(name="orderIdx")
    private List<ObservationCountResult> observationCountResults = new ArrayList<ObservationCountResult>();

    public List<ObservationCountResult> getObservationCountResults() {
        return observationCountResults;
    }

    public void setObservationCountResults(List<ObservationCountResult> observationCountResults) {
        this.observationCountResults.clear();
        this.observationCountResults.addAll(observationCountResults);
    }

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public String getResultString() {
       return "";
    }

}
