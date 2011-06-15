package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "datefield_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class DateFieldCriteriaResult extends CriteriaResult {

    @Column
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
