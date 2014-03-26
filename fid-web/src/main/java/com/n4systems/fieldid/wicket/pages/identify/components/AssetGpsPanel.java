package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetGpsPanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private AssetService assetService;

    private WebMarkupContainer assetGPSContainer;

    public AssetGpsPanel(String id, IModel<Asset> assetModel) {
        super(id);

//        if (!assetModel.getObject().isNew()) {
//            attachments.addAll(assetService.findAssetAttachments(assetModel.getObject()));
//        }
//
//
//        existingAttachmentsContainer = new WebMarkupContainer("existingAttachmentsContainer");
//        existingAttachmentsContainer.setOutputMarkupPlaceholderTag(true);





    }




}
