package com.n4systems.model;


import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="scores")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Score extends ArchivableEntityWithTenant {

    @Column(name="name")
    private @Localized String name;

    @Column(name="value")
    private Double value;

    @Column(name="na")
    private boolean na = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public boolean isNa() {
        return na;
    }

    public void setNa(boolean na) {
        this.na = na;
    }
    
    @Override
	public String toString() { 
    	return  name + " : " +  
    		(isNa() ? "n/a" :+ value);
    }
}
