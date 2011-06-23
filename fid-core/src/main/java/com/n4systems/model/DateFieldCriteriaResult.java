package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "datefield_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class DateFieldCriteriaResult extends CriteriaResult {

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date value;

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

}
