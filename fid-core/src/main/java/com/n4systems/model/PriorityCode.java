package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prioritycode")
public class PriorityCode extends ArchivableEntityWithTenant implements Listable<Long>, Saveable {
    private static final long serialVersionUID = 1L;

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
