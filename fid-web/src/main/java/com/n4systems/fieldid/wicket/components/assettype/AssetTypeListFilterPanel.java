package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

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
            add(new FidDropDownChoice<AssetTypeGroup>("group", new PropertyModel<AssetTypeGroup>(this, "group"), getAssetTypeGroups(), new ListableChoiceRenderer<AssetTypeGroup>()).setNullValid(true));
            add(new AjaxSubmitLink("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onFilter(target, name, group);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {}
            });
            add(new AjaxSubmitLink("clear") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    name = null;
                    group = null;
                    form.clearInput();
                    target.add(AssetTypeFilterForm.this);
                    onFilter(target, name, group);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {}
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

    private List<AssetTypeGroup> getAssetTypeGroups() {
        List<AssetTypeGroup> assetTypeGroups = assetTypeService.getAssetTypeGroupsByOrder();

        AssetTypeGroup notInAGroup = new AssetTypeGroup();
        notInAGroup.setId(-1L);
        notInAGroup.setName(new FIDLabelModel("label.not_in_a_group").getObject());
        assetTypeGroups.add(0, notInAGroup);

        return assetTypeGroups;
    }

}
