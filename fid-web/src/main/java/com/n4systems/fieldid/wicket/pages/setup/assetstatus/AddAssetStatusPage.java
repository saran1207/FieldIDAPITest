package com.n4systems.fieldid.wicket.pages.setup.assetstatus;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.assetstatus.AssetStatusUniqueNameValidator;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;
import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page allows the user to edit a new or existing AssetStatus and save it.
 *
 * These records are very tiny, so there are only a couple fields.
 *
 * Created by Jordan Heath on 05/08/14.
 */
public class AddAssetStatusPage extends FieldIDTemplatePage {

    @SpringBean
    protected AssetStatusService assetStatusService;

    protected IModel<AssetStatus> thisStatus;

    /**
     * This constructor accepts a <b>PageParameters</b> object and uses it to initialize the page.
     *
     * @param params - A <b>PageParameters</b> object that isn't used in this instance of the page.
     */
    public AddAssetStatusPage(PageParameters params) {
        thisStatus = Model.of(getAssetStatus(params));

        add(new AssetStatusEditForm("editOrAddAssetForm", thisStatus));

        Form<Void> form = new Form<Void>("form") {
            /**
             * Create custom onSubmit behaviour to save or update the AssetStatus then provide a confirmation message
             * to the user.
             */
            @Override
            public void onSubmit() {
                if(isEdit()) {
                    assetStatusService.update(thisStatus.getObject(), getCurrentUser());
                } else {
                    assetStatusService.saveAssetStatus(thisStatus.getObject(), getCurrentUser());
                }

                AssetStatusListAllPage nextPage = new AssetStatusListAllPage();
                nextPage.info(new FIDLabelModel("message.saved_asset_status",
                              thisStatus.getObject().getDisplayName()).getObject());
                setResponsePage(nextPage);
            }
        };

        //This is a required field...
        RequiredTextField name;
        form.add(name = new RequiredTextField<String>("name", new PropertyModel<String>(thisStatus.getObject(), "name")));
        //...and also must have a unique name.
        name.add(new AssetStatusUniqueNameValidator(thisStatus.getObject().getId()));

        Button submitButton;
        form.add(submitButton = new Button("saveButton"));
        submitButton.setOutputMarkupId(true);
        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new AssetStatusListAllPage());
            }
        });

        add(form);

        add(new FIDFeedbackPanel("feedbackPanel"));
    }

    /**
     * Create a new <b>AssetStatus</b> entity to be edited by the user.
     *
     * @param params - A PageParameters object that - in this instance - does absolutely nothing.
     * @return A new <b>AssetStatus</b> entity.
     */
    protected AssetStatus getAssetStatus(PageParameters params) {
        AssetStatus assetStatus = new AssetStatus();
        assetStatus.setTenant(getTenant());

        return assetStatus;
    }

    /**
     * Create a custom Title Label displaying, "Add Asset Status."
     *
     * @param labelId - A String value representing the Wicket ID of the Title component.
     * @return A Wicket Component representing the Title Label.
     */
    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_asset_statuses"));
    }

    /**
     * Create a custom "Back to" link on the page, to return to the "Setup - Assets & Events" page.
     *
     * @param linkId - A String value representing the Wicket ID of the link component.
     * @param linkLabelId - A String value representing the Wicket ID of the link's label.
     * @return A Wicket Component representing the BackTo link.
     */
    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    /**
     * Create a custom navBar with a "View All," "View Archived," and "Add" tab.
     *
     * @param navBarId - A String value representing the Wicket ID of the component.
     */
    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                         assetStatusService.getActiveStatusCount()))
                                  .page(AssetStatusListAllPage.class)
                                  .build(),
                        aNavItem().label(new FIDLabelModel("nav.view_all_archived.count",
                                assetStatusService.getArchivedStatusCount()))
                                  .page(AssetStatusListArchivedPage.class)
                                  .build(),
                        aNavItem().label(new FIDLabelModel("nav.add"))
                                  .page(AddAssetStatusPage.class)
                                  .onRight()
                                  .build()
                )
        );
    }

    /**
     * This is the Form used for editing the Asset Status.
     */
    private class AssetStatusEditForm extends Form<AssetStatus> {
        /**
         * Initialize the AssetStatus form and build all of the components.
         *
         * @param id - The Wicket ID of the AssetStatus form.
         * @param assetStatus - An AssetStatus IModel to be edited or created.
         */
        public AssetStatusEditForm(String id, IModel<AssetStatus> assetStatus) {
            super(id, assetStatus);

            add(new LabelledTextField<String>("displayNameField",
                                              "label.name",
                                              ProxyModel.of(thisStatus,
                                                            on(AssetStatus.class).getName())).required());


            add(new SubmitLink("save"));
            add(new BookmarkablePageLink("cancel", AssetStatusListAllPage.class));
        }

        /**
         * Create custom onSubmit behaviour to save or update the AssetStatus then provide a confirmation message
         * to the user.
         */
        @Override
        public void onSubmit() {
            if(isEdit()) {
                assetStatusService.update(thisStatus.getObject(), getCurrentUser());
            } else {
                assetStatusService.saveAssetStatus(thisStatus.getObject(), getCurrentUser());
            }

            AssetStatusListAllPage nextPage = new AssetStatusListAllPage();
            nextPage.info(new FIDLabelModel("message.saved_asset_status",
                                            thisStatus.getObject().getDisplayName()).getObject());
            setResponsePage(nextPage);
        }
    }

    /**
     * This method simply determines whether the page is used for Editing or adding a new AssetStatus.
     *
     * If you extend this page, you can override this method to return a new value.
     *
     * @return
     */
    public boolean isEdit() {
        return false;
    }
}
