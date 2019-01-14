package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.catalog.PublishedCatalogService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndWithFeedbackPage;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Tenant;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.CatalogImportTask;
import com.n4systems.util.ListingPair;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;


@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
public class ImportCommonTemplatesPage extends FieldIDFrontEndWithFeedbackPage {

    private static final Logger logger = Logger.getLogger(ImportCommonTemplatesPage.class);

    @SpringBean
    private PublishedCatalogService publishedCatalogService;

    public ImportCommonTemplatesPage(PageParameters params) {
        super(params);
        addComponents();
    }

    public ImportCommonTemplatesPage() {
        super();
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.import_common_templates"));
    }

    private void addComponents() {

        final List<AjaxCheckBox> checkBoxes = new ArrayList<>();
        final Map<String, Boolean> checkBoxState = new HashMap<String, Boolean>();

        Form form = new Form("form");
        add(form);

        AjaxLink selectAll = new AjaxLink("selectAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                for (String key : checkBoxState.keySet()) {
                    checkBoxState.put(key, Boolean.TRUE);
                }
                /* Mark only the checkboxes for refresh */
                for (AjaxCheckBox ajaxCheckBox : checkBoxes) {
                    target.add(ajaxCheckBox);
                }
            }
        };
        form.add(selectAll);

        AjaxLink selectNone = new AjaxLink("selectNone") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                for (String key : checkBoxState.keySet()) {
                    checkBoxState.put(key, Boolean.FALSE);
                }
                /* Mark only the checkboxes for refresh */
                for (AjaxCheckBox ajaxCheckBox : checkBoxes) {
                    target.add(ajaxCheckBox);
                }
            }
        };
        form.add(selectNone);

        final DataView<AssetTypeGroup> assetTypeGroupList =
                new DataView<AssetTypeGroup>("publishedAssetTypeGroups", new ListDataProvider<AssetTypeGroup>() {
                    @Override
                    protected List<AssetTypeGroup> getData() {
                        Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
                        return new ArrayList(publishedCatalogService.getPublishedAssetTypeGroups(houseAccountTenant));
                    }
                }) {
                    @Override
                    protected void populateItem(Item<AssetTypeGroup> item) {
                        final String groupName = item.getModelObject().getName();
                        item.add(new Label("groupName", groupName));
                        final DataView<ListingPair> assetTypeList = new DataView<ListingPair>("publishedAssetTypeList", new ListDataProvider<ListingPair>(){
                            @Override
                            protected List<ListingPair> getData() {
                                Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
                                return publishedCatalogService.getPublishedAssetTypesForGroup(houseAccountTenant, groupName);
                            }
                        }) {
                            @Override
                            protected void populateItem(Item<ListingPair> item) {
                                populateAssetTypeSelection(checkBoxes, checkBoxState, item);
                            }
                        };
                        item.add(assetTypeList);
                    }
                };
        form.add(assetTypeGroupList);

        final DataView<ListingPair> assetTypeUngroupedList = new DataView<ListingPair>("publishedAssetTypeUngroupedList", new ListDataProvider<ListingPair>() {
            @Override
            protected List<ListingPair> getData() {
                Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
                return publishedCatalogService.getPublishedAssetTypesUngrouped(houseAccountTenant);
            }
        }) {
            @Override
            protected void populateItem(Item<ListingPair> item) {
               populateAssetTypeSelection(checkBoxes, checkBoxState, item);
            }
        };

        form.add(assetTypeUngroupedList);

        Button importButton = new Button("import") {
            @Override
            public void onSubmit() {
                doImport(checkBoxState);
            }
        };
        form.add(importButton);
    }

    private void populateAssetTypeSelection(List<AjaxCheckBox> checkBoxes, Map<String, Boolean> checkBoxState, Item<ListingPair> item) {

        String assetTypeId = item.getModelObject().getId().toString();
        checkBoxState.put(assetTypeId, Boolean.FALSE);
        PropertyModel<Boolean> checkboxModel = new PropertyModel(checkBoxState, assetTypeId);

        AjaxCheckBox ajaxCheckBox = new AjaxCheckBox("publishedAssetTypeSelection", checkboxModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // No action needed
            }
        };
        checkBoxes.add(ajaxCheckBox);
        item.add(ajaxCheckBox);
        item.add(new Label("publishedAssetTypeName", item.getModelObject().getName()));
    }

    private void doImport(final Map<String, Boolean> checkBoxState) {

        Set<Long> selectedAssetIds = new HashSet<>();
        for (String key : checkBoxState.keySet()) {
            if (checkBoxState.get(key)) {
                selectedAssetIds.add(new Long(key));
            }
        }

        Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
        try {
            CatalogImportTask importTask = new CatalogImportTask();

            importTask.setImportEventTypeIds(new HashSet<Long>());
            importTask.setImportAssetTypeIds(selectedAssetIds);
            importTask.setPrimaryOrg(this.getSecurityGuard().getPrimaryOrg());
            importTask.setLinkedTenant(houseAccountTenant);
            importTask.setUsingPackages(true);
            importTask.setUser(getCurrentUser());

            TaskExecutor.getInstance().execute(importTask);

            logger.info("Could not schedule import of asset types from " + houseAccountTenant.getName());
            Session.get().info(getString("msg.scheduleImport.email"));

        } catch (Exception e) {
            logger.error("Could not schedule import of asset types from " + houseAccountTenant.getName(), e);
            Session.get().error(getString("msg.scheduleImport.fail"));
        }

    }
}
