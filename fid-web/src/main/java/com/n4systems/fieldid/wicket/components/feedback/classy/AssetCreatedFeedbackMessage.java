package com.n4systems.fieldid.wicket.components.feedback.classy;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;

public class AssetCreatedFeedbackMessage extends ClassyFeedbackMessage<AssetCreatedFeedbackMessageDisplayPanel> {

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

    @Override
    public AssetCreatedFeedbackMessageDisplayPanel createComponent(String id) {
        return new AssetCreatedFeedbackMessageDisplayPanel(id, this);
    }

    @Override
    public String getPlainMessage() {
        return new FIDLabelModel("message.asset_created_successfully").getObject();
    }
}
