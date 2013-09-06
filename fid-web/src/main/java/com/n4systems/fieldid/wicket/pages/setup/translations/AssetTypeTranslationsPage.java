package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

public class AssetTypeTranslationsPage  extends TranslationsPage<AssetType> {

    public AssetTypeTranslationsPage() {
        super();
    }

    @Override
    protected DropDownChoice<AssetType> createChoice(String id) {
        return new GroupedAssetTypePicker(id, Model.of(new AssetType()), new GroupedAssetTypesForTenantModel(Model.of((AssetTypeGroup) null)));
    }

}
