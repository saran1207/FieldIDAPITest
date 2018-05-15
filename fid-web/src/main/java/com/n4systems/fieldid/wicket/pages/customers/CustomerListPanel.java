package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;

import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.security.Permissions;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Created by agrabovskis on 2018-01-18.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
abstract public class CustomerListPanel extends AbstractCustomerListPanel {


    public CustomerListPanel(String id, IModel<WebSessionMap> webSessionMapModel, LoaderFactory loaderFactory) {
        super(id, webSessionMapModel, loaderFactory);
    }

    @Override
    void populateItemInTable(Item<CustomerOrg> item) {
        final CustomerOrg customer = item.getModelObject();

        WebMarkupContainer nameSection = new WebMarkupContainer("result.name");
        item.add(nameSection);

        //TODO copied from Struts action but shouldn't all customerOrg entities here be non archived?
        nameSection.add(new Label("customerName", Model.of(customer.getName())){
            @Override
            public boolean isVisible() {
                return customer.isArchived();
            }
        });
        nameSection.add(new Link("customerShowLink") {
            @Override
            public void onClick() {
                invokeCustomerShow(customer.getId());
            }
            @Override
            public boolean isVisible() {
                return !customer.isArchived();
            }
        }.add(new Label("label", Model.of(customer.getName()))));

        item.add(new Label("result.id", customer.getCode()));
        item.add(new Label("result.organization", customer.getInternalOrg().getName()));

        String created = "";
        if (customer.getCreatedBy() != null)
            created = customer.getCreatedBy().getFullName() + ", ";
        if (customer.getCreated() != null)
            created += formatDateTime(customer.getCreated());
        item.add(new Label("result.created", Model.of(created)));

        String modified = "";
        if (customer.getModifiedBy() != null)
            modified = customer.getModifiedBy().getFullName() + ", ";
        if (customer.getModified() != null)
            modified += formatDateTime(customer.getModified());
        item.add(new Label("result.last_modified", Model.of(modified)));

        /* Edit actions to be displayed depend on the attributes of the customer */
        final WebMarkupContainer editActions = new WebMarkupContainer("result.editActions");
        item.add(editActions);

        final WebMarkupContainer linkedActions = new WebMarkupContainer("customerLinkedActions") {
            @Override
            public boolean isVisible() {
                return customer.isLinked();
            }
        };
        editActions.add(linkedActions);
        linkedActions.add(new Link("customerEditLink") {

            @Override
            public void onClick() {
                invokeCustomerEdit(customer.getId());
            }
        });

        //TODO copied from Struts action but shouldn't all customerOrg entities here be non archived?
        editActions.add(new AjaxLink("customerUnarchiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doUnarchive(customer, target);
            }

            @Override
            public boolean isVisible() {
                return customer.isArchived() && !customer.isLinked();
            }
        });

        final WebMarkupContainer regularCustomerActions = new WebMarkupContainer("customerEditActions") {
            @Override
            public boolean isVisible() {
                return !customer.isArchived() && !customer.isLinked();
            }
        };
        editActions.add(regularCustomerActions);
        regularCustomerActions.add(new Link("customerEditLink") {
            @Override
            public void onClick() {
                invokeCustomerEdit(customer.getId());
            }
        });
        final AjaxLink customerArchiveLink = new AjaxLink("customerArchiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doArchive(customer, target);
            }

            @Override
            public IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    @Override
                    public CharSequence decorateScript(Component c, CharSequence script) {
                        Long assetCount = getPlaceService().getAssetCount(customer.getId());
                        if(assetCount != null && assetCount > 0) {
                            return "alert('" + new FIDLabelModel("message.customer_owns_assets").getObject() + "'); return false;";
                        } else {
                            return new StringBuilder("if(!confirm('").append(
                                    new FIDLabelModel("message.customer_archive_warning").getObject()).
                                    append("')) { return false; };").append(script);

                        }
                    }
                };
            }
        };
        regularCustomerActions.add(customerArchiveLink);
    }

    @Override
    protected boolean isArchivedOnly() {
        return false;
    }

    /**
     * If there are no non archived customers in the system display a panel with a link to create
     * a customer instead of the regular no results section.
     * @param id wicket markup id
     * @return
     */
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
