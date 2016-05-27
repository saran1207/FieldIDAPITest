package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2015-01-21.
 */

@SuppressWarnings("serial")
@Entity
@Table(name="observationcount_groups")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ObservationCountGroup extends ArchivableEntityWithTenant implements Listable, NamedEntity {

    @Column(nullable=false)
    private @Localized
    String name;

    @OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name="observationcount_groups_observationcounts", joinColumns = @JoinColumn(name="observationcount_group_id"), inverseJoinColumns = @JoinColumn(name="observationcount_id"))
    @OrderColumn(name="orderIdx")
    @org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ObservationCount> observationCounts = new ArrayList<ObservationCount>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObservationCount> getObservationCounts() {
        return observationCounts;
    }

    public void setObservationCounts(List<ObservationCount> observationCounts) {
        this.observationCounts.clear();
        this.observationCounts.addAll(observationCounts);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public ObservationCount getScore(String scoreName) {
        for (ObservationCount observationCount: getObservationCounts()) {
            if (observationCount.getName().equalsIgnoreCase(scoreName)) {
                return observationCount;
            }
        }
        return null;
    }


}
