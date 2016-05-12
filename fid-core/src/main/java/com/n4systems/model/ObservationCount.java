package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by rrana on 2015-01-21.
 */

@SuppressWarnings("serial")
@Entity
@Table(name="observationcount")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ObservationCount extends ArchivableEntityWithTenant{

    @Column(name="name")
    private @Localized
    String observationCount;

    @Column(name="counted")
    private boolean counted = true;

    public ObservationCount() {
        super(null);
    }

    public ObservationCount(Tenant tenant) {
        super(tenant);
    }

    public ObservationCount(ObservationCount count) {
        this(count.getTenant());
        this.observationCount = count.getName();
        this.counted = count.isCounted();
    }

    public String getName() {
        return observationCount;
    }

    public void setName(String name) {
        this.observationCount = name;
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

}
