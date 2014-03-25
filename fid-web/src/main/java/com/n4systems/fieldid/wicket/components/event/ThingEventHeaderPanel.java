package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ThingEventHeaderPanel extends Panel {

    @SpringBean
    protected UserService userService;
    @SpringBean
    protected AssetService assetService;

    public ThingEventHeaderPanel(String id, IModel<Asset> assetModel, Boolean isView) {
        super(id, assetModel);

        final Asset asset = assetModel.getObject();

        add(new Label("assetType", asset.getType().getName()));
        add(new Label("assetIdentifier", asset.getIdentifier()));

        BaseOrg owner = asset.getOwner();

        add(new Label("ownerInfo", getOwnerLabel(owner, asset.getAdvancedLocation())));

        add(new BookmarkablePageLink<AssetSummaryPage>("assetSummary", AssetSummaryPage.class, PageParametersBuilder.uniqueId(asset.getId())));

    }

    private String getOwnerLabel(BaseOrg owner, Location advancedLocation) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(owner.getHierarchicalDisplayName());

        if(advancedLocation != null && !advancedLocation.getFullName().isEmpty()) {
            stringBuilder.append(", ").append(advancedLocation.getFullName());
        }
        return stringBuilder.toString();
    }

}
