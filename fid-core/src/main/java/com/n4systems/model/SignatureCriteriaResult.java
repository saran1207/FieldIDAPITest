package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="signature_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class SignatureCriteriaResult extends CriteriaResult {

    @Column(name="signed")
    private boolean signed = false;

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

}
