package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeAttachmentsPanel;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeAttributePanel;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeImagePanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.FileAttachment;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;

import java.util.List;

public class AddOrEditAssetTypePage extends FieldIDFrontEndPage {

    private IModel<AssetType> assetType;

    @SpringBean
    private AssetTypeService assetTypeService;

    private AssetTypeImagePanel imagePanel;
    private AssetTypeAttachmentsPanel attachmentsPanel;
    private AssetTypeAttributePanel attributePanel;
    private WebMarkupContainer moreInfo;

    public AddOrEditAssetTypePage(PageParameters params) {
        if(params.get("uniqueID").isNull()) {
            assetType = Model.of(getNewAssetType());
        } else {
            assetType = Model.of(assetTypeService.getAssetType(params.get("uniqueID").toLong()));
        }
        add(new AssetTypeForm("form", assetType));
        add(new FIDFeedbackPanel("feedbackPanel"));
    }

    private AssetType getNewAssetType() {
        AssetType assetType = new AssetType();
        assetType.setTenant(getTenant());
        return assetType;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.asset_type_add"));
    }

    private class AssetTypeForm extends Form<AssetType> {

        private AssetTypeForm(String id, IModel<AssetType> model) {
            super(id, model);
            add(new RequiredTextField<String>("name", new PropertyModel<String>(model, "name")));
            add(new FidDropDownChoice<AssetTypeGroup>("group", new PropertyModel<AssetTypeGroup>(model, "group"), assetTypeService.getAssetTypeGroupsByOrder(), new ListableChoiceRenderer<AssetTypeGroup>()));
            add(imagePanel = new AssetTypeImagePanel("image", model));
            add(attributePanel = new AssetTypeAttributePanel("attributes", model));
            add(new TextField<String>("descriptionTemplate", new PropertyModel<String>(model, "descriptionTemplate")));

            add(moreInfo = new WebMarkupContainer("moreInfo"));
            moreInfo.add(new CheckBox("linkable", new PropertyModel<Boolean>(model, "linkable")));
            moreInfo.add(new CheckBox("hasManufacturerCert", new PropertyModel<Boolean>(model, "hasManufactureCertificate")));
            moreInfo.add(new TextArea<String>("manufacturerCertText", new PropertyModel<String>(model, "manufactureCertificateText")));
            moreInfo.add(attachmentsPanel = new AssetTypeAttachmentsPanel("attachments", model));
            moreInfo.add(new TextArea<String>("warnings", new PropertyModel<String>(model, "warnings")));
            moreInfo.add(new TextArea<String>("instructions", new PropertyModel<String>(model, "instructions")));
            moreInfo.add(new TextField<String>("cautionUrl", new PropertyModel<String>(model, "cautionUrl")));
            moreInfo.setOutputMarkupPlaceholderTag(true);
            moreInfo.setVisible(false);

            add(new AjaxLink("showHide") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    moreInfo.setVisible(!moreInfo.isVisible());
                    target.add(moreInfo);
                }
            });

            add(new SubmitLink("save"));
            add(new BookmarkablePageLink("cancel", AssetTypeListPage.class));
        }

        @Override
        protected void onSubmit() {
            AssetType assetType = getModelObject();

            List<FileAttachment> attachments = null;
            byte [] imageData = null;

            if(imagePanel.isImageUpdated()) {
                imageData = imagePanel.getAssetTypeImageBytes();
                assetType.setImageName(imagePanel.getClientFileName());
            }

            if(!attachmentsPanel.getAttachments().isEmpty()) {
                attachments = attachmentsPanel.getAttachments();
            }

            if(attributePanel.getAttributes().isEmpty()) {
                assetType.setInfoFields(Lists.<InfoFieldBean>newArrayList());
            } else {
                assetType.setInfoFields(attributePanel.getAttributes());
            }

            try {
                assetTypeService.saveAssetType(assetType, attachments, imageData);
            } catch (ImageAttachmentException e) {
                error("Failed to attach files to Asset Type: " + assetType.getDisplayName());
            }

            setResponsePage(AssetTypeListPage.class);
        }
    }
}
