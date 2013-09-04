package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AssetTypeTranslationsPage  extends TranslationsPage{

    private IModel<AssetType> assetTypeModel;

    public AssetTypeTranslationsPage() {
       assetTypeModel = Model.of(new AssetType());

       add(new GroupedAssetTypePicker("assetType", assetTypeModel, new GroupedAssetTypesForTenantModel(Model.of((AssetTypeGroup) null))));

    }
}
