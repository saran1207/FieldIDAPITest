package com.n4systems.fieldid.wicket.components.search;

import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.AssetTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class AssetDetailsCriteriaPanel extends Panel {

    private GroupedAssetTypePicker groupedAssetTypePicker;
    private GroupedAssetTypesForTenantModel availableAssetTypesModel;

    public AssetDetailsCriteriaPanel(String id,  IModel<?> model) {
        super(id, model);

        add(new DropDownChoice<AssetStatus>("assetStatus", new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>()).setNullValid(true));
        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
        final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(getDefaultModel(), "assetType");
        availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
        add(createAssetTypeGroupChoice(assetTypeGroupModel, assetTypeModel, availableAssetTypesModel));
        add(groupedAssetTypePicker = new GroupedAssetTypePicker("assetType", new PropertyModel<AssetType>(getDefaultModel(), "assetType"), availableAssetTypesModel));
        groupedAssetTypePicker.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onAssetTypeOrGroupUpdated(target, assetTypeModel.getObject(), availableAssetTypesModel.getObject());
            }
        });
        groupedAssetTypePicker.setNullValid(true);

    }

    private DropDownChoice<AssetTypeGroup> createAssetTypeGroupChoice(IModel<AssetTypeGroup> assetTypeGroupModel, final IModel<AssetType> assetTypeModel, final GroupedAssetTypesForTenantModel availableAssetTypesModel) {
        DropDownChoice<AssetTypeGroup> assetTypeGroupDropDownChoice = new DropDownChoice<AssetTypeGroup>("assetTypeGroup",
                assetTypeGroupModel, new AssetTypeGroupsForTenantModel(), new ListableChoiceRenderer<AssetTypeGroup>());
        assetTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onAssetTypeOrGroupUpdated(target, assetTypeModel.getObject(), availableAssetTypesModel.getObject());
                target.addComponent(groupedAssetTypePicker);
            }
        });
        assetTypeGroupDropDownChoice.setNullValid(true);
        return assetTypeGroupDropDownChoice;
    }

    public IModel<List<AssetType>> getAvailableAssetTypesModel() {
        return availableAssetTypesModel;
    }

    protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {}

}
