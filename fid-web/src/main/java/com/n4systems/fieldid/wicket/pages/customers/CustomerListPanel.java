package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.WicketLinkGeneratorClickHandler;
import com.n4systems.fieldid.wicket.pages.WicketLinkGeneratorComponent;
import com.n4systems.fieldid.wicket.pages.WicketLinkGeneratorDescriptor;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Arrays;

/**
 * Created by agrabovskis on 2018-01-18.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
public class CustomerListPanel extends AbstractCustomerListPanel {


    public CustomerListPanel(String id, IModel<WebSessionMap> webSessionMapModel, LoaderFactory loaderFactory) {
        super(id, webSessionMapModel, loaderFactory);
    }

    @Override
    void populateItemInTable(Item<CustomerOrg> item) {
        final CustomerOrg customer = item.getModelObject();
        if (customer.isArchived())
            item.add(new Label("result.name", Model.of(customer.getName())));
        else {
            item.add(new WicketLinkGeneratorComponent("result.name",
                    Arrays.asList(new WicketLinkGeneratorDescriptor(
                            "customerShow.action?uniqueID=" + customer.getId(), null, customer.getName(), null))));
        }

        item.add(new Label("result.id", customer.getCode()));
        item.add(new Label("result.organization", customer.getInternalOrg().getName()));

        String created = "";
        if (customer.getCreatedBy() != null)
            created = customer.getCreatedBy().getFullName() + ", ";
        if (customer.getCreated() != null)
            created+=formatDateTime(customer.getCreated());
        item.add(new Label("result.created", Model.of(created)));

        String modified = "";
        if (customer.getModifiedBy() != null)
            modified = customer.getModifiedBy().getFullName() + ", ";
        if (customer.getModified() != null)
            modified+=formatDateTime(customer.getModified());
        item.add(new Label("result.last_modified", Model.of(modified)));

        if (customer.isLinked()) {
            item.add(new WicketLinkGeneratorComponent("result.editAction",
                    Arrays.asList(
                            new WicketLinkGeneratorDescriptor(
                                    "customerEdit.action?uniqueID=" + customer.getId(), null,
                                    new FIDLabelModel("label.edit").getObject(), null),
                            new WicketLinkGeneratorDescriptor(null, null,
                                    " | " + new FIDLabelModel("label.linked_customer").getObject(), null))));
        }
        else
        if (customer.isArchived()) {
            WicketLinkGeneratorClickHandler unarchiveLinkClickHandler = new WicketLinkGeneratorClickHandler() {
                @Override
                public void onClick() {
                    doUnarchive(customer);
                }
            };
            item.add(new WicketLinkGeneratorComponent("result.editAction",
                    Arrays.asList(
                            new WicketLinkGeneratorDescriptor(
                                    null,
                                    unarchiveLinkClickHandler,
                                    new FIDLabelModel("label.unarchive").getObject(), null))));
        }
        else {
            WicketLinkGeneratorClickHandler archiveLinkClickHandler = new WicketLinkGeneratorClickHandler() {
                @Override
                public void onClick() {
                    doArchive(customer);
                }
            };
            item.add(new WicketLinkGeneratorComponent("result.editAction",
                    Arrays.asList(
                            new WicketLinkGeneratorDescriptor(
                                    "customerEdit.action?uniqueID=" + customer.getId(), null,
                                    new FIDLabelModel("label.edit").getObject(), null),
                            new WicketLinkGeneratorDescriptor(null, null,
                                    " | ", null),
                            new WicketLinkGeneratorDescriptor(
                                    null,
                                    archiveLinkClickHandler,
                                    new FIDLabelModel("label.archive").getObject(),
                                    getArchiveConfirmJsFunction(customer.getId())))));
        }
    }

    @Override
    protected boolean isArchivedOnly() {
        return false;
    }

    @Override
    protected Component createCustomNoResultSection(String id) {
        return new CustomerCreatePanel(id);
    }

    @Override
    protected boolean showNoResultSection(int resultCount, boolean filteringResult) {
        return resultCount == 0 && filteringResult;
    }

    @Override
    protected boolean showCustomResultSection(int resultCount, boolean filteringResult) {
        return resultCount == 0 && !filteringResult;
    }

    @Override
    protected boolean showFilterSection(int resultCount, boolean filteringResult) {
        return (resultCount > 0) || (resultCount == 0 && filteringResult);
    }

    protected String getArchiveConfirmJsFunction(Long customerId) {
        Long assetCount = getPlaceService().getAssetCount(customerId);
        if(assetCount != null && assetCount > 0) {
            return "alert('" + getString("CUSTOMER_OWNS_ASSETS") + "'); return false;";
        } else {
            return "return confirm('" + getString("CUSTOMER_ARCHIVE_WARNING") + "');";
        }
    }

    private class CustomerCreatePanel extends Panel {

        public CustomerCreatePanel(String id) {
            super(id);
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            super.renderHead(response);
            response.renderCSSReference("style/legacy/pageStyles/customerListPanel.css");
        }
    }
}
