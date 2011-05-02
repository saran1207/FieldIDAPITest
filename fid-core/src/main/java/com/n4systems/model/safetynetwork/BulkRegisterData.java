package com.n4systems.model.safetynetwork;

import com.n4systems.model.AssetType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkRegisterData {

    private Map<Long, BulkRegisterItem> items = new HashMap<Long, BulkRegisterItem>();

    public BulkRegisterItem getItemForType(Long assetTypeId) {
        return items.get(assetTypeId);
    }

    public void addItemOfType(AssetType type) {
        if (items.get(type.getId()) == null) {
            items.put(type.getId(), new BulkRegisterItem(type));
        }
        items.get(type.getId()).incrementCount();
    }

    public List<BulkRegisterItem> getItems() {
        return new ArrayList<BulkRegisterItem>(items.values());
    }

}
