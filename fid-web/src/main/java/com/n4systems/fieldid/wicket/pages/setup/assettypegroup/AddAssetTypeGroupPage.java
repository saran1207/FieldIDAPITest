package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-08-05.
 */
public class AddAssetTypeGroupPage extends FieldIDTemplatePage{

    @SpringBean
    protected AssetTypeGroupService assetTypeGroupService;

    protected IModel<AssetTypeGroup> assetTypeGroup;

    public AddAssetTypeGroupPage(PageParameters params) {
        super(params);
        assetTypeGroup = Model.of(getAssetTypeGroup(params));
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddGroupDetailsForm("addAssetTypeGroupForm", assetTypeGroup));
    }


    private class AddGroupDetailsForm extends Form<AssetTypeGroup> {

        private AddGroupDetailsForm(String id, IModel<AssetTypeGroup> model) {
            super(id, model);

            setMultiPart(true);

            WebMarkupContainer groupDetailsContainer = new WebMarkupContainer("groupDetailsContainer");

            RequiredTextField nameField = new RequiredTextField<String>("name", new PropertyModel<String>(model, "name"));
            nameField.add(new AbstractValidator() {
                @Override
                protected void onValidate(IValidatable validatable) {
                    String name = (String) validatable.getValue();
                    if (assetTypeGroupService.isNameExists(name) && assetTypeGroup.getObject().isNew()) {
                        ValidationError error = new ValidationError();
                        error.addMessageKey("error.assettypegroupnameduplicate");
                        validatable.error(error);
                    }
                }
            });

            groupDetailsContainer.add(nameField);
            add(groupDetailsContainer);
            groupDetailsContainer.setOutputMarkupId(true);

            Button saveButton = new Button("saveButton");
            add(saveButton);
            add(new BookmarkablePageLink("cancelLink", AssetTypeGroupListPage.class));

        }

        @Override
        protected void onSubmit() {

        }

    }

    protected AssetTypeGroup getAssetTypeGroup(PageParameters params) {
        AssetTypeGroup assetTypeGroup = new AssetTypeGroup();
        assetTypeGroup.setTenant(getTenant());
        return assetTypeGroup;
    }

    @Override
    protected void addNavBar(String navBarId) {
        NavigationBar navBar = new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeGroupListPage.class).build(),
                aNavItem().label("nav.add").page(AddAssetTypeGroupPage.class).onRight().build());
        add(navBar);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
//        response.renderCSSReference("style/legacy/newCss/assetType/assetType.css");
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    protected boolean isEdit() {
        return false;
    }

}
