package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.loto.ProcedureTitleLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.Asset;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class LotoPage extends FieldIDTemplatePage {

    @SpringBean
    protected AssetService assetService;

    protected Long assetId;
    protected IModel<Asset> assetModel;

    public LotoPage(PageParameters params) {
        super(params);
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        assetId = params.get("uniqueID").toLong();
        assetModel = new EntityModel<Asset>(Asset.class, assetId);
        assetService.fillInSubAssetsOnAsset(assetModel.getObject());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/loto/procedures.css");
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new ProcedureTitleLabel(labelId, assetModel);
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedures"));
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.active")).page(ProcedureDefinitionListPage.class).params(PageParametersBuilder.uniqueId(getAssetId())).build(),
                aNavItem().label(new FIDLabelModel("label.previously_published")).page(PreviouslyPublishedListPage.class).params(PageParametersBuilder.uniqueId(getAssetId())).build(),
                aNavItem().label(new FIDLabelModel("label.completed_inprogress")).page(ProceduresListPage.class).params(PageParametersBuilder.uniqueId(getAssetId())).build())
        );
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new LotoActionGroup(actionGroupId, assetModel);
    }

}
