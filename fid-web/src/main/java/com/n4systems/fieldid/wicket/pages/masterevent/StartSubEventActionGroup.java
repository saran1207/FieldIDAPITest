package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

    private DialogModalWindow modalWindow;

    public StartSubEventActionGroup(String id, IModel<ThingEvent> model) {
        super(id, model);

        add(modalWindow = new DialogModalWindow("subEventModal"));

        add(new ListView<SubAsset>("linkedAsset", getSubAssetsListModel(model.getObject().getAsset())) {
            @Override
            protected void populateItem(ListItem<SubAsset> item) {
                SubAsset subAsset = item.getModelObject();
                item.add(new AjaxLink<Void>("selectAssetLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modalWindow.setContent(new SelectSubEventPanel(modalWindow.getContentId(), model, subAsset.getAsset()) {
                            @Override
                            protected void onPerformSubEvent(IModel<ThingEvent> model) {
                                StartSubEventActionGroup.this.onPerformSubEvent(model);
                            }
                        });
                        modalWindow.show(target);
                    }
                }.add(new FlatLabel("assetType", new PropertyModel<String>(subAsset, "asset.type.displayName")))
                 .add(new FlatLabel("identifier", new PropertyModel<String>(subAsset, "asset.identifier"))));
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
