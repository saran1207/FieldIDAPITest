package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * The Divisions panel has two sets of content. One is to list the current divisions (divided into active and archived)
 * and the other is to add/edit a division.
 */
public class CustomerDivisionsPanel extends Panel {

    private enum DivisionSubPanel {EDIT_DIVISION, LIST_DIVISIONS}

    private IModel<Long> customerSelectedForEditModel;
    private LoaderFactory loaderFactory;
    private IModel<DivisionSubPanel> currentlyDisplayedPage;
    private CompoundPropertyModel currentDivision;

    public CustomerDivisionsPanel(String id, IModel<Long> customerSelectedForEditModel, LoaderFactory loaderFactory) {
        super(id);
        currentlyDisplayedPage = Model.of(DivisionSubPanel.LIST_DIVISIONS);
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        this.loaderFactory = loaderFactory;
        currentDivision = new CompoundPropertyModel<>(new DivisionOrg());
        addComponents();
    }

    private void addComponents() {

        final WebMarkupContainer customerListSection = new WebMarkupContainer("customerListSection") {
            @Override
            public boolean isVisible() {
                return currentlyDisplayedPage.getObject() == DivisionSubPanel.LIST_DIVISIONS;
            }
        };
        customerListSection.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(customerListSection);

        final WebMarkupContainer customerAddSection = new WebMarkupContainer("customerAddSection") {
            @Override
            public boolean isVisible() {
                return currentlyDisplayedPage.getObject() == DivisionSubPanel.EDIT_DIVISION;
            }
        };
        customerAddSection.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(customerAddSection);

        final CustomerDivisionsListPanel customerDivisionsListPanel =
                new CustomerDivisionsListPanel("customerListPanel", customerSelectedForEditModel, loaderFactory) {
                    @Override
                    void invokeEdit(DivisionOrg division, AjaxRequestTarget target) {
                        currentlyDisplayedPage.setObject(DivisionSubPanel.EDIT_DIVISION);
                        currentDivision.setObject(division);
                        target.add(customerListSection);
                        target.add(customerAddSection);
                    }
                };
        customerListSection.add(customerDivisionsListPanel);

        final CustomerDivisionsEditPanel customerDivisionsAddPanel =
                new CustomerDivisionsEditPanel("customerAddPanel", currentDivision, customerSelectedForEditModel) {
                    @Override
                    void postCancelAction(AjaxRequestTarget target) {
                        currentlyDisplayedPage.setObject(DivisionSubPanel.LIST_DIVISIONS);
                        target.add(customerListSection);
                        target.add(customerAddSection);
                    }

                    @Override
                    void postSaveAction(AjaxRequestTarget target) {
                        currentlyDisplayedPage.setObject(DivisionSubPanel.LIST_DIVISIONS);
                        target.add(customerListSection);
                        target.add(customerAddSection);
                    }
                };
        customerAddSection.add(customerDivisionsAddPanel);

        customerListSection.add(new AjaxLink("addDivision") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                currentlyDisplayedPage.setObject(DivisionSubPanel.EDIT_DIVISION);
                currentDivision.setObject(null);
                target.add(customerListSection);
                target.add(customerAddSection);
            }
        });

        customerAddSection.add(new AjaxLink("listDivisions") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                currentlyDisplayedPage.setObject(DivisionSubPanel.LIST_DIVISIONS);
                target.add(customerListSection);
                target.add(customerAddSection);
            }
        });
    }
}
