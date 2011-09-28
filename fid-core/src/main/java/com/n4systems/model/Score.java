package com.n4systems.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.n4systems.model.parents.ArchivableEntityWithTenant;

@Entity
@Table(name="scores")
@PrimaryKeyJoinColumn(name="id")
public class Score extends ArchivableEntityWithTenant {

    @Column(name="name")
    private String name;

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
