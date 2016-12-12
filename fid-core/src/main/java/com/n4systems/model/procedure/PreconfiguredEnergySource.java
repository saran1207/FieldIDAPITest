package com.n4systems.model.procedure;

import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table (name="preconfigured_energy_sources")
public class PreconfiguredEnergySource extends AbstractEntity implements UnsecuredEntity {

    @Enumerated(EnumType.STRING)
    private IsolationPointSourceType isolationPointSourceType;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private String source;

    public IsolationPointSourceType getIsolationPointSourceType() {
        return isolationPointSourceType;
    }

    public void setIsolationPointSourceType(IsolationPointSourceType isolationPointSourceType) {
        this.isolationPointSourceType = isolationPointSourceType;
    }
}
