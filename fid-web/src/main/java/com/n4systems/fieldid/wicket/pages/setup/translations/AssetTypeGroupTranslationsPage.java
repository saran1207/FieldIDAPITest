package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetTypeGroupTranslationsPage extends TranslationsPage<AssetTypeGroup> {


    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    public AssetTypeGroupTranslationsPage() {
        super();
        add(new RenderHint("assettypegroups.name","Name", ""));
    }

    @Override
    protected FidDropDownChoice<AssetTypeGroup> createChoice(String id) {
        return new FidDropDownChoice<AssetTypeGroup>(id, Model.of(new AssetTypeGroup()), assetTypeGroupService.getAllAssetTypeGroups(), getChoiceRenderer());
    }

    public IChoiceRenderer<AssetTypeGroup> getChoiceRenderer() {
        return new IChoiceRenderer<AssetTypeGroup>() {
            @Override public Object getDisplayValue(AssetTypeGroup group) {
                return group.getDisplayName();
            }

            @Override public String getIdValue(AssetTypeGroup group, int index) {
                return group.getId()+"";
            }
        };
    }


}
