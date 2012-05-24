package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class AssetPage extends FieldIDFrontEndPage {

    @SpringBean
    protected AssetService assetService;

    @SpringBean
    protected UserService userService;

    @SpringBean
    protected EventScheduleService eventScheduleService;

    protected Long assetId;
    protected IModel<Asset> assetModel;
    protected Boolean useContext;

    public AssetPage(PageParameters params) {
        super(params);
    }
    
    @Override
    protected void storePageParameters(PageParameters params) {
        assetId = params.get("uniqueID").toLong();
        assetModel = new EntityModel<Asset>(Asset.class, assetId);
        useContext = params.get("useContext").toBoolean();
        assetService.fillInSubAssetsOnAsset(assetModel.getObject());
    }
    
    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.asset"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/asset.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public IModel<Asset> getAssetModel() {
        return assetModel;
    }

    public void setAssetModel(IModel<Asset> assetModel) {
        this.assetModel = assetModel;
    }

    public Boolean getUseContext() {
        return useContext;
    }

    public void setUseContext(Boolean useContext) {
        this.useContext = useContext;
    }

    public boolean isInVendorContext() {
        return (FieldIDSession.get().getVendorContext() != null && useContext );
    }

}
