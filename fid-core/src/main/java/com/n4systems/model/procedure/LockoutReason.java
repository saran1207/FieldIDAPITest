package com.n4systems.model.procedure;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="lockout_reasons")
@Cacheable
@org.hibernate.annotations.Cache(region = "ProcedureCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LockoutReason extends ArchivableEntityWithTenant implements Listable<Long>, Saveable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
