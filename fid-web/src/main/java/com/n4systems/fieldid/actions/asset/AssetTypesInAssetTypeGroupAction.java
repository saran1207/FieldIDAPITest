package com.n4systems.fieldid.actions.asset;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.AssetType;
import com.n4systems.model.assettype.AssetTypesByAssetGroupIdLoader;

import java.util.List;

public class AssetTypesInAssetTypeGroupAction extends AbstractAction {

    private Long assetTypeGroupId;
    private List<AssetType> assetTypes;

    public AssetTypesInAssetTypeGroupAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doList() {
        AssetTypesByAssetGroupIdLoader loader = getLoaderFactory().createAssetTypesByGroupListLoader();

        loader.setAssetTypeGroupId(assetTypeGroupId);

        assetTypes = loader.load();

        return SUCCESS;
    }

    public Long getAssetTypeGroupId() {
        return assetTypeGroupId;
    }

    public void setAssetTypeGroupId(Long assetTypeGroupId) {
        this.assetTypeGroupId = assetTypeGroupId;
    }

    public List<AssetType> getAssetTypes() {
        return assetTypes;
    }
}
