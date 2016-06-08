package com.n4systems.fieldid.wicket.pages.useraccount.mobileofflineprofile;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.table.AssetProfileLinkCell;
import com.n4systems.fieldid.wicket.pages.useraccount.table.RemoveActionCell;
import com.n4systems.model.Asset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OfflineProfilePanel extends Panel{

    @SpringBean
    private OfflineProfileService offlineProfileService;

    @SpringBean
    private AssetService assetService;

    private IModel<OfflineProfile> offlineProfileModel;

    private DataTable<Asset> dataTable;

    public OfflineProfilePanel(String id, final User user) {
        super(id);
        offlineProfileModel = new LoadableDetachableModel<OfflineProfile>() {
            @Override
            protected OfflineProfile load() {
                return offlineProfileService.find(user);
            }
        };

        setOutputMarkupId(true);

        add(new Label("totalAssets", new PropertyModel<String>(offlineProfileModel, "assets.size")));
        add(new Label("platform", new PropertyModel<String>(offlineProfileModel, "currentPlatform")));
        add(new AjaxLink<Void>("clearAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                offlineProfileModel.getObject().getAssets().clear();
                offlineProfileModel.getObject().getOrganizations().clear();
                offlineProfileService.update(offlineProfileModel.getObject());
                target.add(OfflineProfilePanel.this);
            }
        });

        FieldIDDataProvider<Asset> dataProvider = new FieldIDDataProvider<Asset>() {
            @Override
            public Iterator<? extends Asset> iterator(int first, int count) {
                List<Asset> assetList = Lists.newArrayList();
                List<String> assetIds = new ArrayList(offlineProfileModel.getObject().getAssets());
                List<String> archivedAssetIds = Lists.newArrayList();
                for(int i = first; i < first + count; i++) {
                    Asset asset = assetService.findByMobileId(assetIds.get(i));
                    if(asset != null)
                        assetList.add(asset);
                    else
                        archivedAssetIds.add(assetIds.get(i));
                }
                offlineProfileModel.getObject().getAssets().removeAll(archivedAssetIds);
                return assetList.iterator();
            }

            @Override
            public int size() {
                return offlineProfileModel.getObject().getAssets().size();
            }

            @Override
            public IModel<Asset> model(Asset object) {
                return Model.of(object);
            }
        };

        add(dataTable = new SimpleDefaultDataTable<Asset>("assetsTable", getTableColumns(), dataProvider, 10));
        dataTable.setOutputMarkupId(true);
    }

    private List<IColumn<Asset>> getTableColumns() {
        List<IColumn<Asset>> columns = Lists.newArrayList();

        columns.add(new AbstractColumn<Asset>(new FIDLabelModel("label.identifier")) {
            @Override
            public void populateItem(Item<ICellPopulator<Asset>> cellItem, String componentId, IModel<Asset> rowModel) {
                cellItem.add(new AssetProfileLinkCell(componentId, rowModel));
            }
        });
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.assettype"),"type.displayName" ));
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.organization"),"owner.rootOrgName" ));
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.customer"),"owner.customerOrg.name" ));
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.division"),"owner.divisionOrg.name" ));
        columns.add(new AbstractColumn<Asset>(new FIDLabelModel("label.remove")) {
            @Override
            public void populateItem(Item<ICellPopulator<Asset>> cellItem, String componentId, IModel<Asset> rowModel) {
                cellItem.add(new RemoveActionCell(componentId, rowModel, offlineProfileModel.getObject()) {
                    @Override
                    public void onRemoveAsset(AjaxRequestTarget target) {
                        target.add(dataTable);
                    }
                });
            }
        });

        return columns;
    }
}
