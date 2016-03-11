package com.n4systems.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "datefield_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class DateFieldCriteriaResult extends CriteriaResult {

	@Temporal(TemporalType.TIMESTAMP)
    private Date value;

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

	@Override
	public String getResultString() {
		return value == null ? "" : new SimpleDateFormat().format(value);
	}
    

}
