package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class AssetPage extends FieldIDFrontEndPage {

    @SpringBean
    protected AssetService assetService;

    @SpringBean
    protected UserService userService;

    protected Long assetId;
    protected IModel<Asset> assetModel;

    public AssetPage(PageParameters params) {
        super(params);
    }
    
    @Override
    protected void storePageParameters(PageParameters params) {
        assetId = params.get("uniqueID").toLong();
        assetModel = new EntityModel<Asset>(Asset.class, assetId);
        assetService.fillInSubAssetsOnAsset(assetModel.getObject());
    }
    
    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.asset"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view").page(AssetViewPage.class).build(),
                aNavItem().label("nav.edit").page("assetEdit.action?uniqueID=" + assetId).build(),
                aNavItem().label("nav.traceability").page("assetTraceability.action?uniqueID=" + assetId + "&useContext=false").build(),
                aNavItem().label("nav.event_history").page("assetEvents.action?uniqueID=" + assetId + "&useContext=false").build(),
                aNavItem().label("nav.schedules").page("eventScheduleList.action?assetId=" + assetId + "&useContext=false").build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/asset.css");
    }
}
