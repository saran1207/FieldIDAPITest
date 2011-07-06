package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.wicket.FieldIDWicketApp;
import com.n4systems.fieldid.wicket.components.renderer.AssetTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;

import java.util.List;

public class GroupedAssetTypePicker extends GroupedDropDownChoice<AssetType, AssetTypeGroup> {

    public GroupedAssetTypePicker(String id, IModel<AssetType> assetTypeModel, IModel<List<AssetType>> assetTypesModel) {
        super(id, assetTypeModel, assetTypesModel, new AssetTypeChoiceRenderer());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        checkComponentTag(tag, "select");
        super.onComponentTag(tag);
    }

    @Override
    protected AssetTypeGroup getGroup(AssetType choice) {
        return choice.getGroup();
    }

    @Override
    protected String getGroupLabel(AssetTypeGroup group) {
        if (group == null) {
            return FieldIDWicketApp.get().getResourceSettings().getLocalizer().getString("label.ungrouped", this);
        }
        return group.getDisplayName();
    }

}
