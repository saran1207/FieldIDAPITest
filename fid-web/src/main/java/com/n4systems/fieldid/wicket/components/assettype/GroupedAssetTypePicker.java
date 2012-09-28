package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.wicket.FieldIDWicketApp;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.AssetTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.model.IModel;

import java.util.List;

public class GroupedAssetTypePicker extends GroupedDropDownChoice<AssetType, AssetTypeGroup> {

    private boolean useJChosen;

    public GroupedAssetTypePicker(String id, IModel<AssetType> assetTypeModel, IModel<List<AssetType>> assetTypesModel) {
        this(id, assetTypeModel, assetTypesModel, true);
    }

    public GroupedAssetTypePicker(String id, IModel<AssetType> assetTypeModel, IModel<List<AssetType>> assetTypesModel, boolean useJChosen) {
        super(id, assetTypeModel, assetTypesModel, new AssetTypeChoiceRenderer());
        setOutputMarkupId(true);
        this.useJChosen = useJChosen;
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

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (useJChosen) {
            add(new JChosenBehavior());
        }
    }
}
