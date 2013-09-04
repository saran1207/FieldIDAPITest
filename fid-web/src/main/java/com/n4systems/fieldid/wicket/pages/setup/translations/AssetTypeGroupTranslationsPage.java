package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AssetTypeGroupTranslationsPage extends TranslationsPage {

    private IModel<AssetTypeGroup> assetTypeGroupModel;

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    public AssetTypeGroupTranslationsPage() {
        assetTypeGroupModel = Model.of(new AssetTypeGroup());

        add(new FidDropDownChoice<AssetTypeGroup>("assetTypeGroup", assetTypeGroupModel, assetTypeGroupService.getAllAssetTypeGroups(), getChoiceRenderer()));
    }

    public IChoiceRenderer<AssetTypeGroup> getChoiceRenderer() {
        return new IChoiceRenderer<AssetTypeGroup>() {
            @Override
            public Object getDisplayValue(AssetTypeGroup group) {
                return group.getDisplayName();
            }

            @Override
            public String getIdValue(AssetTypeGroup group, int index) {
                return group.getId()+"";
            }
        };
    }
}
