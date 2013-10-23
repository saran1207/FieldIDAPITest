package com.n4systems.fieldid.wicket.pages.useraccount;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.useraccount.table.AssetProfileLinkCell;
import com.n4systems.fieldid.wicket.pages.useraccount.table.RemoveActionCell;
import com.n4systems.model.Asset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class MobileOfflineProfilePage extends FieldIDFrontEndPage {

    @SpringBean
    private OfflineProfileService offlineProfileService;

    @SpringBean
    private AssetService assetService;

    private IModel<OfflineProfile> offlineProfileModel;

    private DataTable<Asset> dataTable;

    private WebMarkupContainer displayContainer;

    public MobileOfflineProfilePage() {
        offlineProfileModel = Model.of(offlineProfileService.find(getCurrentUser()));

        add(displayContainer = new WebMarkupContainer("displayContainer"));
        displayContainer.setOutputMarkupId(true);

        displayContainer.add(new Label("totalAssets", new PropertyModel<String>(offlineProfileModel, "assets.size")));
        displayContainer.add(new AjaxLink<Void>("clearAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                offlineProfileModel.getObject().getAssets().clear();
                offlineProfileModel.getObject().getOrganizations().clear();
                offlineProfileService.update(offlineProfileModel.getObject());
                target.add(displayContainer);
            }
        });

        FieldIDDataProvider<Asset> dataProvider = new FieldIDDataProvider<Asset>() {
            @Override
            public Iterator<? extends Asset> iterator(int first, int count) {
                List<Asset> assetList = Lists.newArrayList();
                List<String> assetIds = new ArrayList(offlineProfileModel.getObject().getAssets());
                for(int i = first; i < first + count; i++) {
                    assetList.add(assetService.findByMobileId(assetIds.get(i)));
                }
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

        displayContainer.add(dataTable = new SimpleDefaultDataTable<Asset>("assetsTable", getTableColumns(), dataProvider, 10));
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

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.my_account_offline_profile"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.details").page("myAccount.action").build(),
                aNavItem().label("nav.notification_settings").page("notificationSettings.action").build(),
                aNavItem().label("nav.change_password").page("editPassword.action").build(),
                aNavItem().label("nav.mobile_passcode").page("viewMobilePasscode.action").build(),
                aNavItem().label("nav.mobile_profile").page(MobileOfflineProfilePage.class).build(),
                aNavItem().label("nav.downloads").page("showDownloads.action").build(),
                aNavItem().label("nav.excel_export").page("exportEvent.action").build()
        ));
    }
}
