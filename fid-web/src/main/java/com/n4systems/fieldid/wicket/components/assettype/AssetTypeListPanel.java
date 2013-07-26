package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.assettype.CopyAssetTypePage;
import com.n4systems.fieldid.wicket.pages.setup.assettype.EditAssetTypePage;
import com.n4systems.model.AssetType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AssetTypeListPanel extends Panel {

    private Long assetTypeGroupId;

    private String name;

    @SpringBean
    private AssetTypeService assetTypeService;


    public AssetTypeListPanel(String id) {
        super(id);

        add(new ListView<AssetType>("list", getAssetTypes()) {
            @Override
            protected void populateItem(ListItem<AssetType> item) {
                AssetType assetType = item.getModelObject();
                TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();
                NonWicketLink nameLink;
                NonWicketLink groupLink;

                item.add(nameLink = new NonWicketLink("nameLink", "assetType.action?uniqueID=" + assetType.getId()));
                nameLink.add(new Label("name", new PropertyModel<String>(assetType, "displayName")));
                item.add(groupLink = new NonWicketLink("groupLink", "assetTypeGroup.action?uniqueID=" + (assetType.getGroup() != null ? assetType.getGroup().getId() : "")));
                groupLink.add(new Label("group", new PropertyModel<String>(assetType, "group.displayName")));
                item.add(new Label("createdBy", new PropertyModel<String>(assetType, "createdBy.displayName")).setVisible(assetType.getCreatedBy() != null ));
                item.add(new Label("createdDate", new DayDisplayModel(new PropertyModel<Date>(assetType, "created"), true, timeZone)));
                item.add(new Label("modifiedBy", new PropertyModel<String>(assetType, "modifiedBy.displayName")).setVisible(assetType.getModifiedBy() != null));
                item.add(new Label("modifiedDate", new DayDisplayModel(new PropertyModel<Date>(assetType, "modified"), true, timeZone)));
                item.add(new BookmarkablePageLink<Void>("edit", EditAssetTypePage.class, PageParametersBuilder.uniqueId(assetType.getId())));
                item.add(new BookmarkablePageLink<Void>("copy", CopyAssetTypePage.class, PageParametersBuilder.uniqueId(assetType.getId())));
            }
        });

    }

    private LoadableDetachableModel<List<AssetType>> getAssetTypes() {
        return new LoadableDetachableModel<List<AssetType>>() {
            @Override
            protected List<AssetType> load() {
                return assetTypeService.getAssetTypes(assetTypeGroupId, name);
            }
        };
    }

    public Long getAssetTypeGroupId() {
        return assetTypeGroupId;
    }

    public void setAssetTypeGroupId(Long assetTypeGroupId) {
        this.assetTypeGroupId = assetTypeGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
