package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.AssetTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class AssetDetailsCriteriaPanel extends Panel {

    private GroupedAssetTypePicker groupedAssetTypePicker;
    private IModel<List<AssetType>> availableAssetTypesModel;

    public AssetDetailsCriteriaPanel(String id,  IModel<?> model) {
        super(id, model);

        add(new FidDropDownChoice<AssetStatus>("assetStatus", new LocalizeModel<List<AssetStatus>>(new AssetStatusesForTenantModel()), new ListableChoiceRenderer<AssetStatus>()).setNullValid(true));
        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
        final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(getDefaultModel(), "assetType");
        availableAssetTypesModel = new LocalizeModel<List<AssetType>>(new GroupedAssetTypesForTenantModel(assetTypeGroupModel));
        add(createAssetTypeGroupChoice(assetTypeGroupModel, assetTypeModel, availableAssetTypesModel));
        add(groupedAssetTypePicker = new GroupedAssetTypePicker("assetType", new PropertyModel<AssetType>(getDefaultModel(), "assetType"), availableAssetTypesModel));
        groupedAssetTypePicker.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onAssetTypeOrGroupUpdated(target, assetTypeModel.getObject(), availableAssetTypesModel.getObject());
            }
        });
        groupedAssetTypePicker.setNullValid(true);
        groupedAssetTypePicker.add(new AttributeAppender("data-placeholder", " "));

        add(new FidDropDownChoice<Boolean>("hasGps",new PropertyModel<Boolean>(getDefaultModel(), "hasGps"),
                Lists.newArrayList(Boolean.TRUE, Boolean.FALSE), new IChoiceRenderer<Boolean>() {
            @Override
            public Object getDisplayValue(Boolean object) {
                if(object)
                    return new FIDLabelModel("label.has_gps").getObject();
                else
                    return new FIDLabelModel("label.no_gps").getObject();
            }

            @Override
            public String getIdValue(Boolean object, int index) {
                return object.toString();
            }
        }).setNullValid(true));

    }

    private FidDropDownChoice<AssetTypeGroup> createAssetTypeGroupChoice(IModel<AssetTypeGroup> assetTypeGroupModel, final IModel<AssetType> assetTypeModel, final IModel<List<AssetType>> availableAssetTypesModel) {
        FidDropDownChoice<AssetTypeGroup> assetTypeGroupDropDownChoice = new FidDropDownChoice<AssetTypeGroup>("assetTypeGroup",
                assetTypeGroupModel, new LocalizeModel<List<AssetTypeGroup>>(new AssetTypeGroupsForTenantModel()), new ListableChoiceRenderer<AssetTypeGroup>());
        assetTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onAssetTypeOrGroupUpdated(target, assetTypeModel.getObject(), availableAssetTypesModel.getObject());
                target.add(groupedAssetTypePicker);
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
