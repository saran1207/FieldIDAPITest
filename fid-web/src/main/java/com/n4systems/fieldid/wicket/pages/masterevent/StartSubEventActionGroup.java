package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.fieldid.wicket.behavior.validation.DisableNavigationConfirmationBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.model.Asset;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.SubAsset;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class StartSubEventActionGroup extends Panel {

    @SpringBean
    AssetService assetService;

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;

    private DialogModalWindow modalWindow;

    public StartSubEventActionGroup(String id, IModel<ThingEvent> masterEventModel) {
        super(id, masterEventModel);

        add(modalWindow = new DialogModalWindow("subEventModal"));

        add(new ListView<SubAsset>("linkedAsset", getSubAssetsListModel(masterEventModel.getObject().getAsset())) {
            @Override
            protected void populateItem(ListItem<SubAsset> item) {
                SubAsset subAsset = item.getModelObject();

                List<AssociatedEventType> associatedEventTypes = associatedEventTypesService.getAssociatedEventTypes(subAsset.getAsset().getType());

                WebMarkupContainer selectLink;

                if (associatedEventTypes.size() > 1) {

                    item.add(selectLink = new AjaxLink<Void>("selectAssetLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            modalWindow.setContent(new SelectSubEventPanel(modalWindow.getContentId(), masterEventModel, subAsset.getAsset()) {
                                @Override
                                protected void onPerformSubEvent(IModel<ThingEvent> model) {
                                    StartSubEventActionGroup.this.onPerformSubEvent(model);
                                }
                            });
                            modalWindow.show(target);
                        }
                    });

                } else {
                   item.add(selectLink = new Link<Void>("selectAssetLink") {
                       @Override
                       public void onClick() {
                           onPerformSubEvent(masterEventModel);
                           setResponsePage(new PerformSubEventPage(masterEventModel, subAsset.getAsset().getId(), associatedEventTypes.get(0).getEventType().getId()));
                       }
                   });
                    selectLink.add(new DisableNavigationConfirmationBehavior());
                }

                selectLink.add(new FlatLabel("assetType", new PropertyModel<String>(subAsset, "asset.type.displayName")));
                selectLink.add(new FlatLabel("identifier", new PropertyModel<String>(subAsset, "asset.identifier")));

            }

            @Override
            public boolean isVisible() {
                return getViewSize() > 0;
            }
        });
    }

    protected void onPerformSubEvent(IModel<ThingEvent> masterEventModel) {}

    private LoadableDetachableModel<List<SubAsset>> getSubAssetsListModel(Asset asset) {
        return new LoadableDetachableModel<List<SubAsset>>() {
            @Override
            protected List<SubAsset> load() {
                return assetService.findSubAssets(asset);
            }
        };
    }


}
