package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeAttachmentsPanel;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeImagePanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetTypePage extends FieldIDFrontEndPage {

    private IModel<AssetType> assetType;

    @SpringBean
    private AssetTypeService assetTypeService;

    public AssetTypePage() {
        assetType = Model.of(new AssetType());

        add(new AssetTypeForm("form", assetType));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.asset_type_add"));
    }

    private class AssetTypeForm extends Form<AssetType> {

        private AssetTypeForm(String id, IModel<AssetType> model) {
            super(id, model);

            add(new FidDropDownChoice<AssetTypeGroup>("group", new PropertyModel<AssetTypeGroup>(model, "group"), assetTypeService.getAssetTypeGroupsByOrder(), new ListableChoiceRenderer<AssetTypeGroup>()));
            add(new TextField<String>("name", new PropertyModel<String>(model, "name")));
            add(new CheckBox("linkable", new PropertyModel<Boolean>(model, "linkable")));
            add(new TextArea<String>("warnings", new PropertyModel<String>(model, "warnings")));
            add(new TextArea<String>("instructions", new PropertyModel<String>(model, "instructions")));
            add(new TextField<String>("cautionUrl", new PropertyModel<String>(model, "cautionUrl")));
            add(new CheckBox("hasManufacturerCert", new PropertyModel<Boolean>(model, "hasManufactureCertificate")));
            add(new TextArea<String>("manufacturerCertText", new PropertyModel<String>(model, "manufactureCertificateText")));
            add(new TextField<String>("descriptionTemplate", new PropertyModel<String>(model, "descriptionTemplate")));
            add(new AssetTypeImagePanel("image", model));
            add(new AssetTypeAttachmentsPanel("attachments", model));
        }
    }
}
