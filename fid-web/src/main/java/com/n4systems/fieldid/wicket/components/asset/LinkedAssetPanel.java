package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.AutoCompleteSearch;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetViewPage;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
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

    public LinkedAssetPanel(String id, final IModel<Asset> assetModel) {
        super(id);

        this.assetModel = assetModel;

        final WebMarkupContainer linkedAssetsList = new WebMarkupContainer("linkedAssetList");
        add(linkedAssetsList);

        linkedAssetsList.setOutputMarkupId(true);
        final IModel<List<SubAsset>> linkedAssetsModel = createLinkedAssetsModel();

        linkedAssetsList.add(new ListView<SubAsset>("linkedAssetListView", linkedAssetsModel) {
            @Override
            protected void populateItem(final ListItem<SubAsset> item) {
                item.setOutputMarkupId(true);
                item.add(new Label("assetType", new PropertyModel(item.getModelObject(), "asset.type.name")));
                Link linkedAssetLink;
                item.add(linkedAssetLink = new BookmarkablePageLink("linkedAssetLink", AssetViewPage.class, new PageParameters().add("uniqueID", item.getModelObject().getAsset().getId())));
                linkedAssetLink.add(new Label("assetIdentifier", new PropertyModel<Object>(item.getModelObject(), "asset.identifier")));
                item.add(new AjaxLink<Asset>("removeLinkedAssetLink") {
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

            }
        };

        linkedAssetsList.add(sortableBehavior);


        final Form<Void> form = new Form<Void>("form");

        add(addLink = new AjaxLink<Void>("addLinkedAsset") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addLink.setVisible(false);
                form.setVisible(true);
                target.add(LinkedAssetPanel.this);
            }
        });


        form.add(new AutoCompleteSearch("autocompletesearch", new PropertyModel<Asset>(this, "assetForLinking")));
        form.add(new AjaxSubmitLink("saveLink", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
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
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
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
