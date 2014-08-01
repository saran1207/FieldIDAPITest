package com.n4systems.fieldid.wicket.components.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.DeleteAssetTypeGroupPage;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.EditAssetTypeGroupPage;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.ViewAssetTypeGroupPage;
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

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by tracyshi on 2014-07-31.
 */
public class AssetTypeGroupListPanel extends Panel {

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    private ListView listView;

    public AssetTypeGroupListPanel(String id) {
        super(id);

        listView = new ListView<AssetTypeGroup>("list", getAssetTypeGroups()) {
            @Override
            protected void populateItem(ListItem<AssetTypeGroup> item) {

                AssetTypeGroup assetTypeGroup = item.getModelObject();
                Long assetTypeGroupId = assetTypeGroup.getId();
                TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();

                Link nameLink = new BookmarkablePageLink<ViewAssetTypeGroupPage>("nameLink", ViewAssetTypeGroupPage.class, PageParametersBuilder.uniqueId(assetTypeGroupId));
                nameLink.add(new Label("name", new PropertyModel<String>(assetTypeGroup, "displayName")));

                item.add(nameLink);
                item.add(new Label("createdBy", new PropertyModel<String>(assetTypeGroup, "createdBy.displayName")).setVisible(assetTypeGroup.getCreatedBy() != null ));
                item.add(new Label("createdDate", new DayDisplayModel(new PropertyModel<Date>(assetTypeGroup, "created"), true, timeZone)));
                item.add(new Label("modifiedBy", new PropertyModel<String>(assetTypeGroup, "modifiedBy.displayName")).setVisible(assetTypeGroup.getModifiedBy() != null));
                item.add(new Label("modifiedDate", new DayDisplayModel(new PropertyModel<Date>(assetTypeGroup, "modified"), true, timeZone)));
                item.add(new BookmarkablePageLink<Void>("edit", EditAssetTypeGroupPage.class, PageParametersBuilder.uniqueId(assetTypeGroupId)));
                item.add(new BookmarkablePageLink<Void>("delete", DeleteAssetTypeGroupPage.class, PageParametersBuilder.uniqueId(assetTypeGroupId)));

            }
        };

        add(listView);

    }

    private LoadableDetachableModel<List<AssetTypeGroup>> getAssetTypeGroups() {
        return new LoadableDetachableModel<List<AssetTypeGroup>>() {
            @Override
            protected List<AssetTypeGroup> load() {
                return assetTypeGroupService.getAllAssetTypeGroups();
            }
        };
    }

    public boolean isEmpty() {
        return listView.getList().isEmpty();
    }

}
