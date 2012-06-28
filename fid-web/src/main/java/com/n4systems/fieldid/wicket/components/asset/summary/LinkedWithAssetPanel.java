package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.pages.asset.AssetViewPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LinkedWithAssetPanel extends Panel{

    @SpringBean
    protected AssetService assetService;
    
    public LinkedWithAssetPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();
        
        Asset parent =  assetService.parentAsset(asset);
        
        add(new Label("assetType", new PropertyModel<Asset>(parent, "type.name")));
        Link assetLink;
        add(assetLink = new BookmarkablePageLink("linkedAssetLink", AssetViewPage.class, new PageParameters().add("uniqueID", parent.getId())));
        assetLink.add(new Label("assetIdentifier", new PropertyModel<Asset>(parent, "identifier")));
    }
}
