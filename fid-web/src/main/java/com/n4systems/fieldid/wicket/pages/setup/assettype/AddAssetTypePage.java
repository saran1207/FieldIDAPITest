package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.Watermark;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.assettype.*;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.FileAttachment;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AddAssetTypePage extends FieldIDFrontEndPage {

    protected IModel<AssetType> assetType;

    @SpringBean
    protected AssetTypeService assetTypeService;

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    private AssetTypeImagePanel imagePanel;
    private AssetTypeAttachmentsPanel attachmentsPanel;
    private AssetTypeAttributePanel attributePanel;
    private WebMarkupContainer moreInfo;
    private DialogModalWindow modalWindow;
    private AjaxLink showHide;
    private WebMarkupContainer showHideIcon;

    public AddAssetTypePage(PageParameters params) {
        assetType = Model.of(getAssetType(params));
        add(new AssetTypeForm("form", assetType));
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(modalWindow = new DialogModalWindow("modalWindow"));
        modalWindow.setContent(new DescriptionTemplatePanel(modalWindow.getContentId()));
        modalWindow.setInitialHeight(450);
    }

    protected AssetType getAssetType(PageParameters params) {
        AssetType assetType = new AssetType();
        assetType.setTenant(getTenant());
        if(!params.get("group").isNull()) {
            AssetTypeGroup group = assetTypeGroupService.getGroupById(params.get("group").toLong());
            assetType.setGroup(group);
        }
        return assetType;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new AssetTypeTitleLabel(labelId, assetType);
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        if(assetType.getObject().isNew())
            return new Label(labelId, new FIDLabelModel("title.asset_type_add"));
        else
            return new Label(labelId, new FIDLabelModel("title.asset_type_edit"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/buttons.css");
        response.renderCSSReference("style/newCss/assetType/assetType.css");
    }

    public boolean isEdit() {
        return false;
    }

    public boolean isCopy() {
        return false;
    }

    private class AssetTypeForm extends Form<AssetType> {

        private AssetTypeForm(String id, IModel<AssetType> model) {
            super(id, model);

            setMultiPart(true);

            RequiredTextField nameField;

            add(nameField = new RequiredTextField<String>("name", new PropertyModel<String>(model, "name")));
            nameField.add(new AbstractValidator() {
                @Override
                protected void onValidate(IValidatable validatable) {
                    String name = (String) validatable.getValue();
                    if (assetTypeService.isNameExists(name) && assetType.getObject().isNew()) {
                        ValidationError error = new ValidationError();
                        error.addMessageKey("error.assettypenameduplicate");
                        validatable.error(error);
                    }
                }
            });
            nameField.add(new Watermark(new FIDLabelModel("label.asset_type.form.name").getObject()));
            add(new FidDropDownChoice<AssetTypeGroup>("group", new PropertyModel<AssetTypeGroup>(model, "group"),
                    assetTypeService.getAssetTypeGroupsByOrder(), new ListableChoiceRenderer<AssetTypeGroup>()).setNullValid(true));
            add(imagePanel = new AssetTypeImagePanel("image", model));
            add(attributePanel = new AssetTypeAttributePanel("attributes", model));
            add(new TextField<String>("descriptionTemplate", new PropertyModel<String>(model, "descriptionTemplate")));
            add(new AjaxLink<Void>("templateExample") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    modalWindow.show(target);
                }
            });

            add(moreInfo = new WebMarkupContainer("moreInfo"));
            moreInfo.add(new CheckBox("linkable", new PropertyModel<Boolean>(model, "linkable")));
            moreInfo.add(new CheckBox("hasManufacturerCert", new PropertyModel<Boolean>(model, "hasManufactureCertificate")));
            moreInfo.add(new TextArea<String>("manufacturerCertText", new PropertyModel<String>(model, "manufactureCertificateText"))
                    .add(new Watermark(new FIDLabelModel("label.asset_type.form.manufacturer_cert").getObject())));
            moreInfo.add(attachmentsPanel = new AssetTypeAttachmentsPanel("attachments", model));
            moreInfo.add(new TextArea<String>("warnings", new PropertyModel<String>(model, "warnings")));
            moreInfo.add(new TextArea<String>("instructions", new PropertyModel<String>(model, "instructions")));

            TextField cautionUrl;
            moreInfo.add(cautionUrl = new TextField<String>("cautionUrl", new PropertyModel<String>(model, "cautionUrl")));
            cautionUrl.add(new UrlValidator());
            moreInfo.setOutputMarkupPlaceholderTag(true);
            moreInfo.setVisible(false);

            add(showHide = new AjaxLink("showHide") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    moreInfo.setVisible(!moreInfo.isVisible());
                    if(moreInfo.isVisible()) {
                        showHideIcon.add(new AttributeModifier("class", "entypo-icons entypo-caret-up"));
                    } else {
                        showHideIcon.add(new AttributeModifier("class", "entypo-icons entypo-caret-down"));
                    }
                    target.add(moreInfo, showHide);
                }
            });
            showHide.add(showHideIcon = new WebMarkupContainer("showHideIcon"));

            add(new SubmitLink("save"));
            add(new BookmarkablePageLink("cancel", AssetTypeListPage.class));
            BookmarkablePageLink deleteLink;
            add(deleteLink = new BookmarkablePageLink("delete", ConfirmDeleteAssetTypePage.class, PageParametersBuilder.uniqueId(assetType.getObject().getId())));
            deleteLink.setVisible(assetType.getObject().getId() != null);

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

            if(attributePanel.getInfoFields(assetType).isEmpty()) {
                assetType.setInfoFields(Lists.<InfoFieldBean>newArrayList());
            } else {
                if(isCopy()) {
                    assetType.getInfoFields().clear();
                }
                processInfoFields(attributePanel.getInfoFields(assetType));
                processInfoOptions(attributePanel.getInfoFields(assetType), attributePanel.getEditInfoOptions(assetType));
            }

            try {
               assetType = assetTypeService.saveAssetType(assetType, attachments, imageData);
            } catch (ImageAttachmentException e) {
                error("Failed to attach files to Asset Type: " + assetType.getDisplayName());
            }

            if (isEdit()) {
                FieldIDSession.get().info(new FIDLabelModel("message.asset_type.edit").getObject());
            } else {
                FieldIDSession.get().info(new FIDLabelModel("message.asset_type.add").getObject());
            }

            setResponsePage(AssetTypeListPage.class);
        }

        private void processInfoFields(List<InfoFieldInput> infoFields) {
            List<InfoFieldBean> deleted = new ArrayList<InfoFieldBean>();
            for (InfoFieldInput input : infoFields) {
                if (input.getUniqueID() == null) {
                    if (!input.isDeleted()) {
                        InfoFieldBean addedInfoField = new InfoFieldBean();
                        addedInfoField.setName(input.getName().trim());
                        addedInfoField.setWeight(input.getWeight());
                        addedInfoField.setRequired(input.isRequired());
                        addedInfoField.setIncludeTime(input.isIncludeTime());

                        input.setInfoFieldFieldType(addedInfoField);
                        addedInfoField.setUnfilteredInfoOptions(new HashSet<InfoOptionBean>());
                        addedInfoField.setRetired(input.isRetired());
                        assetType.getObject().getInfoFields().add(addedInfoField);
                        assetType.getObject().associateFields();

                        if (input.getDefaultUnitOfMeasure() != null) {
                            addedInfoField.setUnitOfMeasure(assetTypeService.getUnitOfMeasure(input.getDefaultUnitOfMeasure()));
                        } else {
                            addedInfoField.setUnitOfMeasure(null);
                        }
                        input.setInfoField(addedInfoField);
                    }
                } else {
                    for (InfoFieldBean infoField : assetType.getObject().getInfoFields()) {
                        if (infoField.getUniqueID() != null && infoField.getUniqueID().equals(input.getUniqueID())) {
                            if (input.isDeleted()) {
                                deleted.add(infoField);
                            } else {
                                infoField.setName(input.getName().trim());
                                infoField.setWeight(input.getWeight());
                                infoField.setRequired(input.isRequired());
                                infoField.setIncludeTime(input.isIncludeTime());
                                input.setInfoFieldFieldType(infoField);

                                infoField.setRetired(input.isRetired());
                                if (input.getDefaultUnitOfMeasure() != null) {
                                    infoField.setUnitOfMeasure(assetTypeService.getUnitOfMeasure(input.getDefaultUnitOfMeasure()));
                                } else {
                                    infoField.setUnitOfMeasure(null);
                                }
                            }
                            input.setInfoField(infoField);
                        }
                    }
                }
            }
            assetType.getObject().getInfoFields().removeAll(deleted);
        }

        private void processInfoOptions(List<InfoFieldInput> infoFields, List<InfoOptionInput> editInfoOptions) {
            for (InfoOptionInput input : editInfoOptions) {
                if (input.getInfoFieldIndex().intValue() < infoFields.size()) {
                    InfoFieldBean infoField = infoFields.get(input.getInfoFieldIndex().intValue()).getInfoField();
                    if (infoField != null) {
                        if (input.getUniqueID() == null) {
                            if (!input.isDeleted() && infoField.hasStaticInfoOption()) {

                                InfoOptionBean addedInfoOption = new InfoOptionBean();
                                addedInfoOption.setName(input.getName().trim());
                                addedInfoOption.setWeight(input.getWeight());
                                addedInfoOption.setStaticData(true);
                                infoField.getUnfilteredInfoOptions().add(addedInfoOption);
                                infoField.associateOptions();
                            }
                        } else {
                            for (InfoOptionBean infoOption : infoField.getUnfilteredInfoOptions()) {

                                if (infoOption.getUniqueID() != null
                                        && infoOption.getUniqueID().equals(input.getUniqueID())) {
                                    if (input.isDeleted() || !infoField.hasStaticInfoOption()) {
                                        infoOption.setStaticData(false);
                                        infoOption.setWeight(0L);
                                    } else {
                                        infoOption.setName(input.getName().trim());
                                        infoOption.setWeight(input.getWeight());
                                        infoOption.setStaticData(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
