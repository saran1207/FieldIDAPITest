package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

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

    @Transient
    private String temporaryFileId;

    public String getTemporaryFileId() {
        return temporaryFileId;
    }

    public void setTemporaryFileId(String temporaryFileId) {
        this.temporaryFileId = temporaryFileId;
    }

}
