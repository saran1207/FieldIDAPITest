package com.n4systems.fieldid.wicket.components.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.assettype.EditAssetTypePage;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.ViewAssetTypeGroupPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by tracyshi on 2014-08-01.
 */

public class GroupAssetTypeListPanel extends Panel {

    @SpringBean
    private AssetTypeService assetTypeService;

    private ListView listView;
    private Long assetTypeGroupId;

    public GroupAssetTypeListPanel(String id, Long groupId) {
        super(id);
        assetTypeGroupId = groupId;

        listView = new ListView<AssetType>("list", getAssetTypes()) {
            @Override
            protected void populateItem(ListItem<AssetType> item) {

                AssetType assetType = item.getModelObject();

                Link nameLink = new BookmarkablePageLink<EditAssetTypePage>("nameLink", EditAssetTypePage.class, PageParametersBuilder.uniqueId(assetType.getId()));
                nameLink.add(new Label("name", new PropertyModel<String>(assetType, "displayName")));
                item.add(nameLink);
            }
        };

        add(listView);

    }

    private LoadableDetachableModel<List<AssetType>> getAssetTypes() {
        return new LoadableDetachableModel<List<AssetType>>() {
            @Override
            protected List<AssetType> load() {
                return assetTypeService.getAssetTypes(assetTypeGroupId);
            }
        };
    }

    public boolean isEmpty() {
        return listView.getList().isEmpty();
    }
}

