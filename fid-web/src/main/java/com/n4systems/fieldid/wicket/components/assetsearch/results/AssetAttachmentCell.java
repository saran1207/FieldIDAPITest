package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.util.views.RowView;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-11-20.
 */
public class AssetAttachmentCell extends Panel {

    private @SpringBean
    AssetService assetService;

    public AssetAttachmentCell(String id, IModel<RowView> rowModel) {
        super(id);

        Asset asset = (Asset) rowModel.getObject().getEntity();

        WebMarkupContainer actionsLink = new WebMarkupContainer("attachmentIcon");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new AttributeAppender("class", new Model<String>("icon-link"), ""));

        actionsLink.setVisible(assetService.hasAssetAttachments(asset.getId()));
        //actionsLink.setVisible(asset.getHasAttachments());
        add(actionsLink);
    }
}
