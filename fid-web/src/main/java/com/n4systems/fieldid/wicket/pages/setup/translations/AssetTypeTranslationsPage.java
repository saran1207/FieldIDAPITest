package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

import java.util.List;

public class AssetTypeTranslationsPage  extends TranslationsPage<AssetType> {

    public AssetTypeTranslationsPage() {
        super();
        // TODO DD : need to add sorting so name is first field.
        add(new RenderHint("assettypes.manufactureCertificateText", "Manufacturer Certificate Text", ""));
        add(new RenderHint("assettypes.warnings", "Warnings", ""));
        add(new RenderHint("assettypes.name", "Name", "top-level"));
        add(new RenderHint("assettypes.descriptionTemplate", "Description Template", "top-level"));
        add(new RenderHint("infofield.name", "Attribute Name", "top-level"));
    }

    @Override
    protected DropDownChoice<AssetType> createChoice(String id) {
        return new GroupedAssetTypePicker(id, Model.of(new AssetType()), new GroupedAssetTypesForTenantModel(Model.of((AssetTypeGroup) null)));
    }

    @Override
    protected List<String> initExcludedFields() {
        return Lists.newArrayList("group", "eventTypes", "schedules");
    }
}
