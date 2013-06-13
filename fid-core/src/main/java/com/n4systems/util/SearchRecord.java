package com.n4systems.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.n4systems.model.parents.AbstractEntity;

import java.util.Map;

public class SearchRecord  extends AbstractEntity {
    private String displayName;
    private String type = "asset";  // TODO DD : support different type of search results, org/location, asset, event, user, etc...
    private Map<String ,String> values;

    public SearchRecord(Long assetId, String displayName) {
        this.id = assetId;
        this.displayName = displayName;
        this.values = Maps.newHashMap();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SearchRecord addValue(String attribute, String value) {
        Preconditions.checkState(values.get(attribute)==null,"trying to add attribute " + attribute + " twice!  already has value of '" + values.get(attribute) + "'");
        values.put(attribute,value);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "asset:" + displayName + " " +
                values;
    }
}
