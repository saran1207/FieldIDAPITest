package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Arrays;

/**
 * Created by agrabovskis on 2018-01-03.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
public class CustomerArchivedListPanel extends AbstractCustomerListPanel {

    public CustomerArchivedListPanel(String id, IModel<WebSessionMap> webSessionMapModel, LoaderFactory loaderFactory) {
        super(id, webSessionMapModel, loaderFactory);
    }

    @Override
    void populateItemInTable(Item<CustomerOrg> item) {
        final CustomerOrg customer = item.getModelObject();
        if (customer.isArchived())
            item.add(new Label("result.name", Model.of(customer.getName())));
        else {
            item.add(new WicketLinkGeneratorWidget("result.name",
                    Arrays.asList(new WicketLinkGeneratorDescriptor(
                            "customerShow.action?uniqueID=" + customer.getId(), null, customer.getName(), null))));
        }

        item.add(new Label("result.id", customer.getCode()));
        item.add(new Label("result.organization", customer.getInternalOrg().getName()));

        if (customer.isLinked()) {
            item.add(new WicketLinkGeneratorWidget("result.editAction",
                    Arrays.asList(
                            new WicketLinkGeneratorDescriptor(null, null,
                                    new FIDLabelModel("label.linked_customer").getObject(), null))));
        } else if (customer.isArchived()) {
            WicketLinkGeneratorClickHandler unarchiveLinkClickHandler = new WicketLinkGeneratorClickHandler() {
                @Override
                public void onClick() {
                    doUnarchive(customer);
                }
            };
            item.add(new WicketLinkGeneratorWidget("result.editAction",
                    Arrays.asList(
                            new WicketLinkGeneratorDescriptor(
                                    null,
                                    unarchiveLinkClickHandler,
                                    new FIDLabelModel("label.unarchive").getObject(), null))));
        } else {
            WicketLinkGeneratorClickHandler archiveLinkClickHandler = new WicketLinkGeneratorClickHandler() {
                @Override
                public void onClick() {
                    doArchive(customer);
                }
            };
            item.add(new WicketLinkGeneratorWidget("result.editAction",
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
                                    getArchiveConfirmJsFunction()))));
        }
    }

    @Override
    protected boolean isArchivedOnly() {
        return true;
    }

    protected String getArchiveConfirmJsFunction() {
        return "confirm('" + new FIDLabelModel("label.areyousurearchivecustomer").getObject() + "');";
    }

}
