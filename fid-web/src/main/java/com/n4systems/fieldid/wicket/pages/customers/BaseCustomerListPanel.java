package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.table.StyledAjaxPagingNavigator;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.Iterator;


@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
abstract public class BaseCustomerListPanel extends Panel {

    final private static int CUSTOMERS_PER_PAGE = 20;

    private static final Logger logger = Logger.getLogger(BaseCustomerListPanel.class);

    @SpringBean
    private PlaceService placeService;

    private WebMarkupContainer resultsPanel;
    private WebMarkupContainer noResultsPanel;
    private CustomerFilterPanel filterPanel;
    private WebMarkupContainer customResultPanel;

    private IModel<WebSessionMap> webSessionMapModel;
    private LoaderFactory loaderFactory;
    private IModel<CustomerOrgPaginatedLoader> customerLoaderModel;
    private boolean filteringResult = false;
    private int currentResultCount;

    public BaseCustomerListPanel(String id, IModel<WebSessionMap> webSessionMapModel, LoaderFactory loaderFactory) {
        super(id);
        this.webSessionMapModel = webSessionMapModel;
        this.loaderFactory = loaderFactory;
        initializeCustomerLoader();
        addComponents();
    }

    private void addComponents() {

        /* Create the paged result section, hidden if no results were found */
        resultsPanel = new WebMarkupContainer("resultList") {
            @Override
            public boolean isVisible() {
                return currentResultCount > 0;
            }
        };
        resultsPanel.setOutputMarkupId(true);
        resultsPanel.setOutputMarkupPlaceholderTag(true);

        /* Create the section to display a message if no result */
        noResultsPanel = new WebMarkupContainer("emptyListResult"){
            @Override
            public boolean isVisible() {
                return showNoResultSection(currentResultCount,  filteringResult);
            }
        };
        noResultsPanel.setOutputMarkupId(true);
        noResultsPanel.setOutputMarkupPlaceholderTag(true);

        /* Create the section to display a custom result if implemented */
        customResultPanel = new WebMarkupContainer("customResultContainer") {
            @Override
            public boolean isVisible() {
                return showCustomResultSection(currentResultCount,  filteringResult);
            }
        };
        customResultPanel.setOutputMarkupId(true);
        customResultPanel.setOutputMarkupPlaceholderTag(true);
        customResultPanel.add(createCustomNoResultSection("customResult"));

        /* Create the filter panel */
        filterPanel = new CustomerFilterPanel("filterPanel", loaderFactory) {

            @Override
            protected void applyFilter(AjaxRequestTarget target) {
                regenerateResults(target);
            }

            @Override
            protected void applyClear(AjaxRequestTarget target) {
                regenerateResults(target);
            }

            @Override
            public boolean isVisible() {
                return showFilterSection(currentResultCount,  filteringResult);
            }
        };
        filterPanel.setOutputMarkupId(true);
        filterPanel.setOutputMarkupPlaceholderTag(true);

        IDataProvider customerDataProvider = new IDataProvider<CustomerOrg>() {

            @Override
            public void detach() {
            }

            /* Gets an iterator for the subset of total data */
            @Override
            public Iterator iterator(int first, int count) {
                int currentPage = first / CUSTOMERS_PER_PAGE + 1;
                customerLoaderModel.getObject().setPage(currentPage);
                return customerLoaderModel.getObject().load().getList().iterator();
             }

            /* Gets total number of items in the collection represented by the DataProvider */
            @Override
            public int size() {
                return currentResultCount;
            }

            @Override
            public IModel model(CustomerOrg object) {
                return new AbstractReadOnlyModel<CustomerOrg>() {
                    @Override
                    public CustomerOrg getObject() {
                        return object;
                    }
                };
            }
        };
        final DataView<CustomerOrg> lineItemsView =
                new DataView<CustomerOrg>("result.list", customerDataProvider) {
                    @Override
                    public void populateItem(final Item<CustomerOrg> item) {
                        populateItemInTable(item);
                }
            };
        lineItemsView.setOutputMarkupId(true);
        lineItemsView.setItemsPerPage(CUSTOMERS_PER_PAGE);

        resultsPanel.add(new StyledAjaxPagingNavigator("resultTableNavigatorTop", lineItemsView, resultsPanel) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultsPanel);
            }
        });
        resultsPanel.add(lineItemsView);
        resultsPanel.add(new StyledAjaxPagingNavigator("resultTableNavigatorBottom", lineItemsView, resultsPanel) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultsPanel);
            }
        });
        resultsPanel.add(new Label("totalResults", new IModel<Integer>() {
            @Override
            public void detach() {
            }

            @Override
            public Integer getObject() {
                return customerDataProvider.size();
            }

            @Override
            public void setObject(Integer object) {
            }
        }));

        add(filterPanel);
        add(resultsPanel);
        add(noResultsPanel);
        add(customResultPanel);
    }

    /**
     * Override to create a custom no result section. The implementor has
     * to control this section visibility. The default is an empty not visible panel.
     * @param id wicket markup id
     * @return the created component
     */
    protected Component createCustomNoResultSection(String id) {
        return new EmptyPanel(id);
    }

    /**
     * Override to control the visibility of the custom result section
     * @param resultCount
     * @param filteringResult
     * @return true to show the section, false to hide it
     */
    protected boolean showCustomResultSection(int resultCount, boolean filteringResult) {
        return false;
    }

    /**
     * Override to show/hide the regular no result section
     * @param resultCount
     * @param filteringResult
     * @return
     */
    protected boolean showNoResultSection(int resultCount, boolean filteringResult) {
        return resultCount == 0;
    }

    /**
     * Override to show/hide the filter section
     * @param resultCount
     * @param filteringResult
     * @return
     */
    protected boolean showFilterSection(int resultCount, boolean filteringResult) {
        return true;
    }

    abstract void populateItemInTable(final Item<CustomerOrg> item);

    private void initializeCustomerLoader() {

        customerLoaderModel = new LoadableDetachableModel<CustomerOrgPaginatedLoader>() {

            protected CustomerOrgPaginatedLoader load() {
                CustomerOrgPaginatedLoader loader = getLoaderFactory().createCustomerOrgPaginatedLoader();
                loader.setPostFetchFields("modifiedBy", "createdBy");
                loader.setPageSize(CUSTOMERS_PER_PAGE);
                loader.setArchivedOnly(isArchivedOnly());
                return loader;
            }
        };
        customerLoaderModel.getObject().setNameFilter(null);
        customerLoaderModel.getObject().setIdFilter(null);
        customerLoaderModel.getObject().setOrgFilter(null);
        currentResultCount = (int) customerLoaderModel.getObject().load().getTotalResults();
    }

    abstract protected boolean isArchivedOnly();

    private void regenerateResults(AjaxRequestTarget target) {
        filteringResult = (filterPanel.getNameFilter() != null) || (filterPanel.getIdFilter() != null) ||
                (filterPanel.getOrgFilter() != null);

        customerLoaderModel.getObject().setNameFilter(filterPanel.getNameFilter());
        customerLoaderModel.getObject().setIdFilter(filterPanel.getIdFilter());
        customerLoaderModel.getObject().setOrgFilter(filterPanel.getOrgFilter());
        currentResultCount = (int) customerLoaderModel.getObject().load().getTotalResults();
        target.add(resultsPanel);
        target.add(noResultsPanel);
        target.add(filterPanel);
        target.add(customResultPanel);
    }

    protected void refreshResults(AjaxRequestTarget target) {
        target.add(resultsPanel);
    }

    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }

    protected String formatDateTime(Date date) {
        return formatAnyDate(date, true, true);
    }

    protected String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
        return new FieldIdDateFormatter(date, webSessionMapModel.getObject().getSessionUser(), convertTimeZone, showTime).format();
    }

    protected void doArchive(CustomerOrg customer, AjaxRequestTarget target) {
        if (setCustomerActive(customer, false)) {
            Session.get().info("Archive successful");
            refreshResults(target);
            target.addChildren(getPage(), FeedbackPanel.class);
        }
    }

    protected void doUnarchive(CustomerOrg customer, AjaxRequestTarget target) {
        if (setCustomerActive(customer, true)) {
            Session.get().info("Unarchive successful");
            refreshResults(target);
            target.addChildren(getPage(), FeedbackPanel.class);
        }
    }

    private boolean setCustomerActive(CustomerOrg customer, boolean active) {

        if (customer == null) {
            Session.get().error("Customer not found");
            return false;
        }
        else {
            try {
                if (!active) {
                    placeService.archive(customer);
                }
                else {
                    placeService.unarchive(customer);
                }
                currentResultCount = (int) customerLoaderModel.getObject().load().getTotalResults();
                return true;
            } catch (Exception e) {
                logger.error("Failed updating customer", e);
                Session.get().error(new FIDLabelModel("error.updatingcustomer").getObject());
                return false;
            }
        }
    }

    protected PlaceService getPlaceService() {
        return placeService;
    }

    /**
     * Action to take when the 'edit' link is clicked for a customer in the list.
     * @param customerId
     */
    abstract protected void invokeCustomerEdit(Long customerId, AjaxRequestTarget target);

    /**
     * Action to take when the 'show' ink is clicked for a customer in the list.
     * @param customerId
     * @param target
     */
    abstract protected void invokeCustomerShow(Long customerId, AjaxRequestTarget target);
}
