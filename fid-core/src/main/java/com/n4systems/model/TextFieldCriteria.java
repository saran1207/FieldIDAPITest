package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "textfield_criteria")
@PrimaryKeyJoinColumn(name="id")
public class TextFieldCriteria extends Criteria {

    @Override
    public boolean isTextFieldCriteria() {
        return true;
    }

}
