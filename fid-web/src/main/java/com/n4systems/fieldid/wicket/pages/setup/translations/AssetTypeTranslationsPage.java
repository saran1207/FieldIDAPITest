package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.localization.LocalizedField;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import rfid.ejb.entity.InfoFieldBean;

import java.util.List;
import java.util.Locale;

public class AssetTypeTranslationsPage  extends TranslationsPage<AssetType> {

    public AssetTypeTranslationsPage() {
        super();
        add(new RenderHint("assettypes.descriptionTemplate", "top-level"));
    }

    @Override
    protected DropDownChoice<AssetType> createChoice(String id) {
        return new GroupedAssetTypePicker(id, Model.of(new AssetType()), new GroupedAssetTypesForTenantModel(Model.of((AssetTypeGroup) null)));
    }

    @Override
    protected List<String> initExcludedFields() {
        return Lists.newArrayList("schedules", "group", "eventTypes", "subTypes", "autoAttributeCriteria", "unfilteredInfoOptions" );
    }

    @Override
    protected Component createLinksForItem(String id, ListItem<LocalizedField> item, IModel<List<Locale>> languages) {
        LocalizedField field = item.getModelObject();
        if (field!=null && field.getEntity() instanceof InfoFieldBean) {
            return new AssetLinks(id, item.getModel(), languages);
        }
        return super.createLinksForItem(id, item, languages);
    }


    class AssetLinks extends Fragment {

        public AssetLinks(String id, final IModel<LocalizedField> model, final IModel<List<Locale>> languages) {
            super(id, "assetLinks", AssetTypeTranslationsPage.this);
            final InfoFieldBean ifb = (InfoFieldBean) model.getObject().getEntity();

            add(new AjaxLink("options") {
                @Override public void onClick(AjaxRequestTarget target) {
                    showLocalizationDialogFor(Model.of(ifb), Lists.newArrayList("unfilteredInfoOptions"), target, languages);
                }
                @Override public boolean isVisible() {
                    return ifb.isComboBox() || ifb.isSelectBox();
                }
            });
        }
    }

}
