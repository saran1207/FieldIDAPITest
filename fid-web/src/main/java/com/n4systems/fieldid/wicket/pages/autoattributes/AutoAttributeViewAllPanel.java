package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.model.AssetType;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;

import java.util.List;

/**
 * Created by agrabovskis on 2018-10-30.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
abstract public class AutoAttributeViewAllPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeViewAllPanel.class);

    @SpringBean
    private AssetTypeService assetTypeService;

    private WebMarkupContainer resultsPanel;
    private WebMarkupContainer noResultsPanel;

    private LoadableDetachableModel<List<AssetType>> assetTypeModel;
    private int currentResultCount = 0;

    public AutoAttributeViewAllPanel(String id, IModel<WebSessionMap> webSessionMapModel, LoaderFactory loaderFactory) {
        super(id);
        createDataModel();
        addComponents();
    }

    private void addComponents() {

        /* Create the result section, hidden if no results were found */
        resultsPanel = new WebMarkupContainer("resultList") {
            @Override
            public boolean isVisible() {
                if (!assetTypeModel.isAttached())
                    assetTypeModel.getObject(); // attach model in order to update currentResultCount
                return currentResultCount > 0;
            }
        };
        resultsPanel.setOutputMarkupId(true);
        resultsPanel.setOutputMarkupPlaceholderTag(true);

        final DataView<AssetType> lineItemsView =
                new DataView<AssetType>("assetTypeList", new ListDataProvider<AssetType>() {
                    @Override
                    protected List<AssetType> getData() {
                        return assetTypeModel.getObject();
                    }
                }) {
                    @Override
                    public void populateItem(final Item<AssetType> item) {
                        item.add(new Link("assetTypeLink") {
                            @Override
                            public void onClick() {
                                if (item.getModelObject().hasCriteria()) {
                                    // go to definition tab page
                                    assetTypeWithCriteriaChosen(
                                            item.getModelObject().getId(),
                                            item.getModelObject().getAutoAttributeCriteria().getId());
                                    assetTypeModel.detach();
                                }
                                else {
                                    // go to edit tab page
                                    AssetType assetType = assetTypeService.getAssetTypeWithPostFetches(
                                            item.getModelObject().getId());
                                    if (!assetHasEnoughFields(assetType)) {
                                        Session.get().error(getString("error.asset_needs_2_attr"));
                                    }
                                    else
                                    if (!assetHasStaticFields(assetType)) {
                                        Session.get().error(getString("error.asset_needs_select_or_combo"));
                                    }
                                    {
                                        assetTypeWithoutCriteriaChosen(assetType.getId());
                                        assetTypeModel.detach();
                                    }
                                }
                            }
                        }.add(new Label("assetTypeName", item.getModelObject().getName())));
                    }
                };
        lineItemsView.setOutputMarkupId(true);
        resultsPanel.add(lineItemsView);

        /* Create the section to display a message if no result */
        noResultsPanel = new WebMarkupContainer("emptyListResult"){
            @Override
            public boolean isVisible() {
                return showNoResultSection(currentResultCount);
            }
        };
        noResultsPanel.setOutputMarkupId(true);
        noResultsPanel.setOutputMarkupPlaceholderTag(true);

        add(resultsPanel);
        add(noResultsPanel);
    }

    public List<AssetType> getAssetTypes() {
        List<AssetType> assetTypes = assetTypeService.getAssetTypes();
        currentResultCount = assetTypes.size();
        return assetTypes;
    }

    protected boolean showNoResultSection(int resultCount) {
        return resultCount == 0;
    }

    abstract protected void assetTypeWithCriteriaChosen(Long assetTypeId, Long criteriaId);
    abstract protected void assetTypeWithoutCriteriaChosen(Long assetTypeId);

    /**
     * an assetType requires at least 2 fields to have a template.
     *
     */
    private boolean assetHasEnoughFields(AssetType assetType) {
        return assetType.getAvailableInfoFields().size() >= 2;
    }

    private boolean assetHasStaticFields(AssetType assetType) {
        for (InfoFieldBean infoField : assetType.getAvailableInfoFields()) {
            if (infoField.hasStaticInfoOption()) {
                return true;
            }
        }
        return false;
    }

    private void createDataModel() {
        assetTypeModel = new LoadableDetachableModel<List<AssetType>>() {

            protected List<AssetType> load() {
                List<AssetType> assetTypes = getAssetTypes();
                currentResultCount = assetTypes.size();
                return assetTypes;
            }
        };
    }
}
