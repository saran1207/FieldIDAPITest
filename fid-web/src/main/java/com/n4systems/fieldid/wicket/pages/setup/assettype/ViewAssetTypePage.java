package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.YesOrNoModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ViewAssetTypePage extends FieldIDFrontEndPage {

    private IModel<AssetType> assetType;

    @SpringBean
    private AssetTypeService assetTypeService;

    public ViewAssetTypePage(PageParameters params) {
        super(params);
        assetType = Model.of(assetTypeService.getAssetType(params.get("uniqueID").toLong()));

        add(new Label("group", new PropertyModel<String>(assetType, "group.displayName")));
        add(new Label("name", new PropertyModel<String>(assetType, "displayName")));
        add(new Label("linkable", new YesOrNoModel(new PropertyModel<Boolean>(assetType, "linkable"))));
        add(new Label("warnings", new PropertyModel<String>(assetType, "warnings")));
        add(new Label("moreinfo", new PropertyModel<String>(assetType, "instructions")));
        add(new Label("hasmanufacturercert", new YesOrNoModel(new PropertyModel<Boolean>(assetType, "hasManufactureCertificate"))));
        add(new Label("manufacturercerttext", new PropertyModel<String>(assetType, "manufactureCertificateText")));
        add(new Label("assetdescription", new PropertyModel<String>(assetType, "descriptionTemplate")));
        
        add(new ListView<InfoFieldBean>("attribute", Lists.newArrayList(assetType.getObject().getInfoFields())) {
            @Override
            protected void populateItem(ListItem<InfoFieldBean> item) {
                IModel<InfoFieldBean> infoField = item.getModel();
                item.add(new Label("name", new PropertyModel<String>(infoField, "name")));
                item.add(new Label("type", new PropertyModel<String>(infoField, "type.label")));
                item.add(new Label("required", new YesOrNoModel(new PropertyModel<Boolean>(infoField, "required"))));
                ListView<InfoOptionBean> optionsList;
                item.add(optionsList = new ListView<InfoOptionBean>("option", infoField.getObject().getInfoOptions()) {
                    @Override
                    protected void populateItem(ListItem<InfoOptionBean> item) {
                        item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
                    }
                });
                optionsList.setVisible(!infoField.getObject().getInfoOptions().isEmpty());
            }
        });

        add(new ListView<FileAttachment>("attachment", assetType.getObject().getAttachments()) {
            @Override
            protected void populateItem(ListItem<FileAttachment> item) {
                IModel<FileAttachment> fileAttachment = item.getModel();
                if(fileAttachment.getObject().isImage()) {
                    String downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetTypeAttachedFile.action?fileName="
                            + fileAttachment.getObject().getFileName()
                            + "&uniqueID=" + assetType.getObject().getId()
                            + "&attachmentID=" + fileAttachment.getObject().getId());

                    item.add(new ExternalImage("thumbnail", downloadUrl));
                } else
                    item.add(new Image("thumbnail", new ContextRelativeResource("images/file-icon.png")));

                item.add(new Label("fileName", new PropertyModel<String>(fileAttachment, "fileName")));
                item.add(new Label("comment", new PropertyModel<String>(fileAttachment, "comments")));
            }
        });

        if(assetType.getObject().hasImage()) {
            String downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetTypeImage.action?uniqueID=" + assetType.getObject().getId());
            add(new ExternalImage("image", downloadUrl));
        }
        else {
            add(new WebMarkupContainer("image").setVisible(false));
        }
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.view_asset_type"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        Long assetTypeId = assetType.getObject().getId();
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.view").page("assetType.action").params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.edit").page(EditAssetTypePage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_type_associations").page(EventTypeAssociationsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_frequencies").page("eventFrequencies.action").params(PageParametersBuilder.param("assetTypeId", assetTypeId)).build(),
                aNavItem().label("label.recurring_events").page(RecurringAssetTypeEventsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("label.subassets").page("assetTypeConfiguration.action").params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }

}
