package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Created by agrabovskis on 2018-01-03.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
abstract public class CustomerArchivedListPanel extends AbstractCustomerListPanel {

    public CustomerArchivedListPanel(String id, IModel<WebSessionMap> webSessionMapModel, LoaderFactory loaderFactory) {
        super(id, webSessionMapModel, loaderFactory);
    }

    @Override
    void populateItemInTable(Item<CustomerOrg> item) {
        final CustomerOrg customer = item.getModelObject();
        WebMarkupContainer nameSection = new WebMarkupContainer("result.name");
        item.add(nameSection);

        nameSection.add(new Label("customerName", Model.of(customer.getName())){
            @Override
            public boolean isVisible() {
                return customer.isArchived();
            }
        });
        //TODO copied from struts version but all customers here should have EntityState archived.
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

        final WebMarkupContainer editActions = new WebMarkupContainer("result.editActions");
        item.add(editActions);

        editActions.add(new WebMarkupContainer("customerLinkedMsg") {
            @Override
            public boolean isVisible() {
                return customer.isLinked();
            }
        });

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

        //TODO copied from struts version but all customers here should have EntityState archived.
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
                        return new StringBuilder("if(!confirm('").append(
                                new FIDLabelModel("label.areyousurearchivecustomer").getObject()).
                                append("')) { return false; };").append(script);
                    }
                };
            }
        };
       regularCustomerActions.add(customerArchiveLink);
    }

    @Override
    protected boolean isArchivedOnly() {
        return true;
    }

}
