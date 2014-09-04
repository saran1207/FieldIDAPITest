package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assettypegroup.AssetTypeGroupListPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-07-31.
 */
public class AssetTypeGroupListPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    private AssetTypeGroupReorderForm reorderButtonForm;
    private AssetTypeGroupListPanel listPanel;
    private WebMarkupContainer noResults;

    public AssetTypeGroupListPage() {

        add(reorderButtonForm = new AssetTypeGroupReorderForm("reorderButtonForm"));
        reorderButtonForm.setOutputMarkupId(true);
        add(listPanel = new AssetTypeGroupListPanel("assetTypeGroupListPanel"));
        listPanel.setOutputMarkupId(true);
        add(noResults = new WebMarkupContainer("noResults"));
        noResults.setOutputMarkupPlaceholderTag(true);
        noResults.setVisible(listPanel.isEmpty());

    }


    private class AssetTypeGroupReorderForm extends Form {

        public AssetTypeGroupReorderForm(String id) {
            super(id);
            add(new Button("reorderButton"));
        }

        @Override
        protected void onSubmit() {
            setResponsePage(ReorderAssetTypeGroupPage.class);
        }

    }

    @Override
    protected void addNavBar(String navBarId) {
        NavigationBar navBar = new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", assetTypeGroupService.countAssetTypeGroups())).page(AssetTypeGroupListPage.class).build(),
                aNavItem().label("nav.add").page(AddAssetTypeGroupPage.class).onRight().build());
        add(navBar);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_asset_type_groups.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/newCss/assetTypeGroup/assetTypeGroupListPage.css");
    }
}
