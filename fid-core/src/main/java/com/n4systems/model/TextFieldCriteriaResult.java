package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name = "textfield_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TextFieldCriteriaResult extends CriteriaResult {

    public static final Logger logger = Logger.getLogger(TextFieldCriteriaResult.class);

    public static final int VALUE_FIELD_LENGTH = 500;
    @Column
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {

        if (value != null && !value.isEmpty()) {
            int fieldSize = value.length();
            if (fieldSize > VALUE_FIELD_LENGTH) {
                value = value.substring(0, VALUE_FIELD_LENGTH);
                logger.warn("TextFieldCriteriaResult value field was truncated to " + VALUE_FIELD_LENGTH);
            }
        }
        this.value = value;
    }

	@Override
	public String getResultString() {
		return value!=null ? value : "";
	}

}
