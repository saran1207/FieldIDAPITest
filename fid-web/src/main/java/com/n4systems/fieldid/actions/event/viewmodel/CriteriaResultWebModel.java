package com.n4systems.fieldid.actions.event.viewmodel;

import java.util.List;

import com.n4systems.model.Deficiency;
import com.n4systems.model.Recommendation;

public class CriteriaResultWebModel {

    private String type;
    private Long id;
    private Long criteriaId;
    private Long stateId;
    private String textValue;
    private String secondaryTextValue;
    private String signatureFileId;
    private List<Recommendation> recommendations;
    private List<Deficiency> deficiencies;
    private boolean signed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<Deficiency> getDeficiencies() {
        return deficiencies;
    }

    public void setDeficiencies(List<Deficiency> deficiencies) {
        this.deficiencies = deficiencies;
    }

    public Long getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Long criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecondaryTextValue() {
        return secondaryTextValue;
    }

    public void setSecondaryTextValue(String secondaryTextValue) {
        this.secondaryTextValue = secondaryTextValue;
    }

    public String getSignatureFileId() {
        return signatureFileId;
    }

    public void setSignatureFileId(String signatureFileId) {
        this.signatureFileId = signatureFileId;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

}
