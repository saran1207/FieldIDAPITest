package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="signature_criteria")
@PrimaryKeyJoinColumn(name="id")
public class SignatureCriteria extends Criteria {

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.SIGNATURE;
    }

    @Override
    public boolean isSignatureCriteria() {
        return true;
    }

}
