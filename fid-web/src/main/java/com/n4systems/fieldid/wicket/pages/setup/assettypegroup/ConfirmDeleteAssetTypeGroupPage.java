package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.assettype.*;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-07-31.
 */
public class ConfirmDeleteAssetTypeGroupPage extends FieldIDTemplatePage {

    @SpringBean
    protected AssetTypeGroupService assetTypeGroupService;

    private AssetTypeGroup assetTypeGroup;
    private AssetTypeGroupRemovalSummary removalSummary;

    public ConfirmDeleteAssetTypeGroupPage(PageParameters params) {
        super(params);

        assetTypeGroup = assetTypeGroupService.getAssetTypeGroupById(params.get("uniqueID").toLong());;
        removalSummary = assetTypeGroupService.getRemovalSummary(assetTypeGroup);

        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new Label("instructions", new FIDLabelModel("instruction.deleteassettypegroup", assetTypeGroup.getDisplayName())));
        add(new Label("assetTypesToDetach", new PropertyModel<Long>(removalSummary, "assetTypesConnected")));
        add(new Label("reportsAndSearchesToDelete", new PropertyModel<Long>(removalSummary, "savedReportsConnected")));

        Form buttonForm = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                assetTypeGroupService.deleteAssetTypeGroup(assetTypeGroup);
                FieldIDSession.get().info(new FIDLabelModel("message.assettypegroupdeleted").getObject());
                setResponsePage(AssetTypeGroupListPage.class);
            }
        };
        Button deleteButton = new Button("deleteButton");
        buttonForm.add(deleteButton);
        deleteButton.setOutputMarkupId(true);

        buttonForm.add(new BookmarkablePageLink<Void>("cancelLink", AssetTypeGroupListPage.class, PageParametersBuilder.uniqueId(assetTypeGroup.getId())));

        add(buttonForm);

    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.confirmassettypegroupdelete"));
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
                aNavItem().label("nav.view").page(ViewAssetTypeGroupPage.class).params(PageParametersBuilder.uniqueId(assetTypeGroup.getId())).build(),
                aNavItem().label("nav.edit").page(EditAssetTypeGroupPage.class).params(PageParametersBuilder.uniqueId(assetTypeGroup.getId())).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }

}
