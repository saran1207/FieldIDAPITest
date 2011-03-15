package com.n4systems.fieldid.wicket.model.columnlayout;

import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import org.apache.wicket.model.IModel;

public class CssForTypeModel implements IModel<String> {

    private static final String CUSTOM_GROUP_KEY = "custom_fields";

    private IModel<ColumnMapping> mappingViewModel;
    private String cssPrefix;

    public CssForTypeModel(String cssPrefix, IModel<ColumnMapping> typeModel) {
        this.mappingViewModel = typeModel;
        this.cssPrefix = cssPrefix;
    }

    public String getObject() {
        ColumnMappingGroup group = mappingViewModel.getObject().getGroup();
        if (group != null) {
            return cssPrefix + group.getGroupKey();
        } else {
            // It's a custom group.
            return cssPrefix + CUSTOM_GROUP_KEY;
        }
    }

    public void setObject(String object) {
    }

    public void detach() {
        mappingViewModel.detach();
        cssPrefix = null;
    }

}
