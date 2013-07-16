package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetTypeListFilterPanel extends Panel {

    @SpringBean
    private AssetTypeService assetTypeService;

    public AssetTypeListFilterPanel(String id) {
        super(id);
        add(new AssetTypeFilterForm("form"));
    }

    public void onFilter(AjaxRequestTarget target, String name, AssetTypeGroup group) {};

    private class AssetTypeFilterForm extends Form {

        private AssetTypeGroup group;
        private String name;

        public AssetTypeFilterForm(String id) {
            super(id);
            add(new TextField<String>("name", new PropertyModel<String>(this, "name")));
            add(new FidDropDownChoice<AssetTypeGroup>("group", new PropertyModel<AssetTypeGroup>(this, "group"), assetTypeService.getAssetTypeGroupsByOrder(), new ListableChoiceRenderer<AssetTypeGroup>()));
            add(new AjaxSubmitLink("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onFilter(target, name, group);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });
        }

        public AssetTypeGroup getGroup() {
            return group;
        }

        public void setGroup(AssetTypeGroup group) {
            this.group = group;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
