package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.persistence.localization.Localized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by rrana on 2015-01-21.
 */

@SuppressWarnings("serial")
@Entity
@Table(name="observationcount")
@PrimaryKeyJoinColumn(name="id")
public class ObservationCount extends ArchivableEntityWithTenant{

    @Column(name="name")
    private @Localized
    String name;

    @Column(name="counted")
    private boolean counted = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

}
