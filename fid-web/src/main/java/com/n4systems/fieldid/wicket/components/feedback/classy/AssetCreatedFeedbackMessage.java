package com.n4systems.fieldid.wicket.components.feedback.classy;

import java.io.Serializable;

public class AssetCreatedFeedbackMessage implements Serializable {

    private Long newAssetId;
    private String identifier;

    public AssetCreatedFeedbackMessage(Long newAssetId, String identifier) {
        this.newAssetId = newAssetId;
        this.identifier = identifier;
    }

    public Long getNewAssetId() {
        return newAssetId;
    }

    public void setNewAssetId(Long newAssetId) {
        this.newAssetId = newAssetId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
