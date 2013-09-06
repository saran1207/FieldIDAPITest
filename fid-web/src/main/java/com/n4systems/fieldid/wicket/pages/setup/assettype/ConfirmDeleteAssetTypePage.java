package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeTitleLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.AssetType;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.util.AssetTypeRemovalSummary;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ConfirmDeleteAssetTypePage extends FieldIDFrontEndPage {

    protected IModel<AssetType> assetType;

    private String confirmation;
    private Button submitButton;

    private FeedbackPanel feedbackPanel;

    @SpringBean
    protected AssetTypeService assetTypeService;

    public ConfirmDeleteAssetTypePage(PageParameters params) {
        super(params);

        add(new FIDFeedbackPanel("feedbackPanel"));

        assetType = Model.of(getAssetType(params));

        AssetTypeRemovalSummary summary = assetTypeService.testArchive(assetType.getObject());

        add(new Label("instructions", new FIDLabelModel("instruction.deleteassettype", assetType.getObject().getDisplayName())));
        add(new Label("assetsToDelete", new PropertyModel<Long>(summary, "assetsToDelete")));
        add(new Label("eventsToDelete", new PropertyModel<Long>(summary, "eventsToDelete")));
        add(new Label("schedulesToDelete", new PropertyModel<Long>(summary, "schedulesToDelete")));
        add(new Label("assetCodeMappingsToDelete", new PropertyModel<Long>(summary, "assetCodeMappingsToDelete")).setVisible(getSecurityGuard().isIntegrationEnabled()));
        add(new Label("assetTypesToDetachFrom", new PropertyModel<Long>(summary, "assetTypesToDetachFrom")));
        add(new Label("masterAssetsToDetach", new PropertyModel<Long>(summary, "masterAssetsToDetach")));
        add(new Label("subAssetsToDetach", new PropertyModel<Long>(summary, "subAssetsToDetach")));
        add(new Label("assetsToDetachFromProjects", new PropertyModel<Long>(summary, "assetsToDetachFromProjects")).setVisible(getSecurityGuard().isProjectsEnabled()));

        Form form;
        TextField<String> input;
        add(form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                if (checkDelete()) {
                    assetTypeService.archive(assetType.getObject(), getCurrentUser().getId(), new FIDLabelModel("label.beingdeleted").getObject());
                    FieldIDSession.get().info(new FIDLabelModel("message.assettypedeleted").getObject());
                    setResponsePage(AssetTypeListPage.class);
                }
            }
        });
        form.add(input = new RequiredTextField<String>("confirmationField", new PropertyModel<String>(this, "confirmation")));
        form.add(submitButton = new Button("submitButton"));
        submitButton.setEnabled(false);
        submitButton.setOutputMarkupId(true);

        input.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (checkDelete()) {
                    submitButton.setEnabled(true);
                    target.add(submitButton);
                } else {
                    submitButton.setEnabled(false);
                    target.add(submitButton);
                }
            }
        });

        input.add(new AttributeAppender("class", "confirmText"));

        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(EditAssetTypePage.class, PageParametersBuilder.uniqueId(assetType.getObject().getId()));
            }
        });
    }

    private boolean checkDelete() {
        Matcher matcher = Pattern.compile("delete", Pattern.CASE_INSENSITIVE).matcher(ConfirmDeleteAssetTypePage.this.confirmation);
        return matcher.matches();
    }

    private AssetType getAssetType(PageParameters params) {
        AssetType assetType = assetTypeService.getAssetType(params.get("uniqueID").toLong());
        PostFetcher.postFetchFields(assetType, "subTypes");
        return assetType;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavaScript("$('.confirmText').bind('keypress', function (e) {" +
                                            "    if (e.which == 13) { " +
                                            "        e.preventDefault(); " +
                                            "    } " +
                                            "});");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new AssetTypeTitleLabel(labelId, assetType);
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.asset_type_delete"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        Long assetTypeId = assetType.getObject().getId();
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.edit").page(EditAssetTypePage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_type_associations").page(EventTypeAssociationsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.schedules").page(AssetTypeSchedulesPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }
}
