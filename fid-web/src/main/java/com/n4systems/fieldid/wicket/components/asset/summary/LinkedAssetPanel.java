package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.components.asset.AutoCompleteSearch;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.user.User;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class LinkedAssetPanel extends Panel {

    @SpringBean
    protected AssetService assetService;

    @SpringBean
    protected UserService userService;

    private Asset assetForLinking;
    private IModel<Asset> assetModel;
    private AjaxLink addLink;
    private FIDFeedbackPanel feedbackPanel;
    private AjaxSubmitLink saveLink;

    public LinkedAssetPanel(String id, final IModel<Asset> assetModel) {
        super(id);

        this.assetModel = assetModel;

        final WebMarkupContainer linkedAssetsList = new WebMarkupContainer("linkedAssetList");
        add(linkedAssetsList);

        linkedAssetsList.setOutputMarkupId(true);
        final IModel<List<SubAsset>> linkedAssetsModel = createLinkedAssetsModel();

        final boolean isEditevent = FieldIDSession.get().getSessionUser().hasAccess("editevent");

        linkedAssetsList.add(new ListView<SubAsset>("linkedAssetListView", linkedAssetsModel) {
            @Override
            protected void populateItem(final ListItem<SubAsset> item) {
                item.setOutputMarkupId(true);
                ContextImage sortImage;
                item.add(sortImage = new ContextImage("sortImage", "images/drag-icon.png"));
                sortImage.setVisible(isEditevent);

                item.add(new Label("assetType", new PropertyModel(item.getModelObject(), "asset.type.name")));
                Link linkedAssetLink;
                item.add(linkedAssetLink = new BookmarkablePageLink("linkedAssetLink", AssetSummaryPage.class, new PageParameters().add("uniqueID", item.getModelObject().getAsset().getId())));
                linkedAssetLink.add(new Label("assetIdentifier", new PropertyModel<Object>(item.getModelObject(), "asset.identifier")));
                AjaxLink removeLink;
                item.add(removeLink = new AjaxLink<Asset>("removeLinkedAssetLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        linkedAssetsModel.getObject().remove(item.getModelObject());

                        Asset asset = assetModel.getObject();
                        asset.setSubAssets(linkedAssetsModel.getObject());
                        try {
                            asset = assetService.update(asset, getCurrentUser());
                        } catch (SubAssetUniquenessException e) {
                            error("Unable to remove Linked Asset:" + item.getModelObject().getAsset().getIdentifier());
                        }
                        target.add(linkedAssetsList);
                    }
                });
                removeLink.setVisible(isEditevent);
            }
        });

        Behavior sortableBehavior = new SimpleSortableAjaxBehavior<WebMarkupContainer>() {
            @Override
            public void onUpdate(WebMarkupContainer component, int index, AjaxRequestTarget target) {
                SubAsset item = (SubAsset) component.getDefaultModelObject();
                int oldIndex = linkedAssetsModel.getObject().indexOf(item);

                linkedAssetsModel.getObject().remove(oldIndex);
                linkedAssetsModel.getObject().add(index, item);

                Asset asset = assetModel.getObject();
                asset.setSubAssets(linkedAssetsModel.getObject());
                try {
                    asset = assetService.update(asset, getCurrentUser());
                } catch (SubAssetUniquenessException e) {
                    error("Unable to reorder Linked Assets");
                }

                target.add(linkedAssetsList);
            }
        };

        if(isEditevent) {
            linkedAssetsList.add(sortableBehavior);
        }


        final Form<Void> form = new Form<Void>("form");

        add(addLink = new AjaxLink<Void>("addLinkedAsset") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addLink.setVisible(false);
                form.setVisible(true);
                target.add(LinkedAssetPanel.this);
            }
        });

        addLink.setVisible(isEditevent);

        AutoCompleteSearch autoCompleteSearch;
        form.add(autoCompleteSearch = (AutoCompleteSearch) new AutoCompleteSearch("autocompletesearch", new PropertyModel<Asset>(this, "assetForLinking")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target, String hiddenInput, String fieldInput) {
                if(assetForLinking != null && assetService.parentAsset(assetForLinking) != null) {
                    target.appendJavaScript("$('#showAlreadyLinkedConfirm').val('true'); ");
                }
                //This is a bit of a hack, the onUpdate gets triggered again when the autocomplete input field loses
                // focus when the submit button is clicked but submit ajax event gets tiggered first and it changes the form
                // visibility so the second update call is left in limbo because the component is gone. So we force the
                // input to lose focus before the submit button can be clicked.
                target.appendJavaScript("$('#searchText').focus();");
            }
        }.withAutoUpdate(true));
        autoCompleteSearch.getAutocompleteField().setRequired(true);
        ValidationBehavior.addValidationBehaviorToComponent(autoCompleteSearch.getAutocompleteField());
        autoCompleteSearch.getAutocompleteField().setMarkupId("linkedAssetAutoComplete");

        saveLink = new AjaxSubmitLink("saveLink", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                if(assetForLinking != null) {
                    Asset asset = assetModel.getObject();
                    assetService.fillInSubAssetsOnAsset(asset);

                    if(!asset.getId().equals(assetForLinking.getId())) {
                        SubAsset subasset = new SubAsset(assetForLinking, asset);
                        asset.getSubAssets().add(subasset);
                        try {
                            assetService.update(asset, getCurrentUser());
                            form.setVisible(false);
                            addLink.setVisible(true);
                            assetForLinking = null;
                            target.add(LinkedAssetPanel.this);
                        } catch (SubAssetUniquenessException e) {
                            asset.getSubAssets().remove(subasset);
                            error(new FIDLabelModel("error.samesubasset").getObject());
                            target.add(feedbackPanel);
                        }
                    }else {
                        error(new FIDLabelModel("error.self_linking").getObject());
                        target.add(feedbackPanel);
                    }
                } else {
                    error(new FIDLabelModel("error.selectsubasset").getObject());
                    target.add(feedbackPanel);
                    target.appendJavaScript("$('#linkedAssetAutoComplete').val('')");
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }

        };

        saveLink.setOutputMarkupId(true);

        saveLink.add(AttributeModifier.prepend("onclick",
                "if($('#showAlreadyLinkedConfirm').val() == 'true') { "
                + "if(!confirm('"
                + new FIDLabelModel("massage.warn_linked_asset").getObject()
                + "')) { return false; }} "));

        form.add(saveLink);

        form.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addLink.setVisible(true);
                form.setVisible(false);
                target.add(LinkedAssetPanel.this);
            }
        });

        form.setVisible(false);
        add(form);

        form.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
    }

    private User getCurrentUser() {
        return userService.getUser(FieldIDSession.get().getSessionUser().getId());
    }

    private LoadableDetachableModel<List<SubAsset>> createLinkedAssetsModel() {
        return new LoadableDetachableModel<List<SubAsset>>() {
            @Override
            protected List<SubAsset> load() {
                return assetService.findSubAssets(assetModel.getObject());
            }
        };
    }


}

/*
if($('#showAlreadyLinkedConfirm').val() == "true") {
        if(!confirm('" + new FIDLabelModel("label.confirm_delete_action").getObject() + "')) {
        return false;
}
        } else {
        return false;
}
*/

