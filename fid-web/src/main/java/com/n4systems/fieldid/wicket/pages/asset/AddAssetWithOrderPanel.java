package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.exceptions.OrderProcessingException;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.components.table.StyledAjaxPagingNavigator;
import com.n4systems.fieldid.wicket.data.SmartSearchDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.model.Asset;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.TagOption;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.plugins.PluginFactory;
import com.n4systems.plugins.integration.OrderResolver;
import com.n4systems.security.Permissions;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by agrabovskis on 2017-10-31.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.TAG})
public class AddAssetWithOrderPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AddAssetWithOrderPanel.class);

    private IModel<SecurityFilter> securityFilterModel;
    private IModel<SystemSecurityGuard> securityGuardModel;
    private IModel<SessionUser> sessionUserModel;

    @SpringBean
    private Option optionManager;
    @SpringBean
    private OrderManager orderManager;

    private WebMarkupContainer orderDetailsSection;
    private WebMarkupContainer errorResultSection;

    public AddAssetWithOrderPanel(String id, IModel<SecurityFilter> securityFilterModel,
                                  IModel<SystemSecurityGuard> securityGuardModel,
                                  IModel<SessionUser> sessionUserModel) {

        super(id);
        this.securityFilterModel = securityFilterModel;
        this.securityGuardModel = securityGuardModel;
        this.sessionUserModel = sessionUserModel;
        addComponents();
    }

    private void addComponents() {

        IModel<TagOption> selectedTag;
        List<TagOption> tagOptions = getTagOptions();
        if (tagOptions.size() > 0)
            selectedTag = Model.of(getTagOptions().get(0));
        else
            selectedTag = Model.of((TagOption)null);
        IModel<String> orderNumberModel = Model.of("");
        IModel<Order> selectedOrderModel = Model.of((Order)null);
        LineItemIDataProvider lineItemIDataProvider = new LineItemIDataProvider();

        addSearchSection(selectedTag, orderNumberModel, selectedOrderModel, lineItemIDataProvider);
        orderDetailsSection = new OrderDetailsPanel("orderDetailsPanel", selectedOrderModel, lineItemIDataProvider);
        add(orderDetailsSection);
        errorResultSection = createEmptyResultSection(selectedTag, orderNumberModel);
        add(errorResultSection);

        orderDetailsSection.setVisible(false);
        errorResultSection.setVisible(false);
    }

    private void addSearchSection(IModel<TagOption> selectedTag, IModel<String> orderNumberModel, IModel<Order> selectedOrderIModel, LineItemIDataProvider lineItemDataProdiver) {

        Form orderSearchForm = new Form("searchOrderForm") {
            public void onSubmit() {
                System.out.println("Search order form submit clicked");
                System.out.println("... order number " + orderNumberModel.getObject());
                System.out.println("... selected tag " + selectedTag.getObject());
                boolean orderFound = findOrder(selectedTag.getObject(), orderNumberModel.getObject(), selectedOrderIModel, lineItemDataProdiver);
                if (orderFound) {
                    errorResultSection.setVisible(false);
                    orderDetailsSection.setVisible(true);
                }
                else {
                    errorResultSection.setVisible(true);
                    orderDetailsSection.setVisible(false);
                }
            }
        };
        final DropDownChoice<TagOption> tagSelection = new DropDownChoice<TagOption>("tagOptions",
                selectedTag,
                new LoadableDetachableModel<List<TagOption>>() {
                    @Override
                    protected List<TagOption> load() {
                        return getTagOptions();
                    }
                },
                new IChoiceRenderer<TagOption>() {
                    @Override
                    public Object getDisplayValue(TagOption object) {
                        return object.getText();
                    }

                    @Override
                    public String getIdValue(TagOption object, int index) {
                        return object.getId().toString();
                    }
                }
        ) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

        };
        tagSelection.setRequired(true);
        orderSearchForm.add(tagSelection);

        TextField orderNumber = new TextField("orderNumber", orderNumberModel);
        orderSearchForm.add(orderNumber);
        add(orderSearchForm);
    }



    private WebMarkupContainer createEmptyResultSection(IModel<TagOption> selectedTag, IModel<String> orderNumberModel) {
        WebMarkupContainer emptyResultSection = new WebMarkupContainer("emptyListResult");
        emptyResultSection.add(new Label("notFoundMessage",
                new SimpleModel<String>() {
                    public String getObject() {
                        return new FIDLabelModel("message.ordernotfound",
                                selectedTag.getObject().getText(), "\"" + orderNumberModel.getObject()).getObject() + "\"";
                    }
                }
        ));
        return emptyResultSection;
    }

    private List<TagOption> getTagOptions() {
        return optionManager.findTagOptions(securityFilterModel.getObject());
    }

    private boolean findOrder(TagOption tagOption, String orderNumber, IModel<Order> selectedOrderModel, LineItemIDataProvider lineItemDataProvider) {

        System.out.println("Looking for order " + tagOption + ", " + orderNumber);
        selectedOrderModel.setObject(null);

        Order.OrderType orderType = tagOption.getOptionKey().getOrderType();
        if(orderType == null || orderNumber == null) {
            return false;
        }

        Order order = null;
        if(tagOption.getResolverClassName() == null) {
            // no resolver class is set, lookup in the usual way
            order = orderManager.findOrder(orderType, orderNumber, getTenantId(), securityFilterModel.getObject());
        } else {
            // this tagOption has a resolver class set, we'll need to initialize a plugin
            OrderResolver resolver = PluginFactory.createResolver(tagOption.getResolverClassName());

            try {
                order = orderManager.processOrderFromPlugin(resolver, orderNumber, orderType, getTenantId());
            } catch(OrderProcessingException e) {
                logger.error("Failed loading order [" + orderNumber + "], type [" + orderType.name() + "] from plugin system", e);
                //addFlashErrorText("error.failedfindingorder");
                return false;
            }

            // if the order is still null, try looking up in the database
            if(order == null) {
                order = orderManager.findOrder(orderType, orderNumber, getTenantId(), securityFilterModel.getObject());
            }
        }

        if(order != null) {
            System.out.println("Found order");
            lineItemDataProvider.setLineItems(orderManager.findLineItems(order));
        }
        else
            lineItemDataProvider.setLineItems(new ArrayList<LineItem>());

        selectedOrderModel.setObject(order);
        return order != null;
    }

    private Long getTenantId() {
        return securityGuardModel.getObject().getTenantId();
    }

    private String formatDate(Date date, boolean convertTimeZone) {
        return new FieldIdDateFormatter(date, sessionUserModel.getObject(), convertTimeZone, false).format();
    }


    private class OrderDetailsPanel extends Panel {

        private IModel<Order> orderModel;
        private Panel shopOrderListSection;
        private Panel customerOrderListSection;

        public OrderDetailsPanel(String id, IModel<Order> orderModel, LineItemIDataProvider lineItemIDataProvider) {

            super(id);
            this.orderModel = orderModel;
            add(new Label("orderNumber", new SimpleModel<String>() {
                @Override
                public String getObject() {
                    return orderModel.getObject().getOrderNumber();
                }
            }));
            add(new Label("orderDate", new SimpleModel<String>() {
                @Override
                public String getObject() {
                    return formatDate(orderModel.getObject().getOrderDate(), false);
                }
            }));
            add(new Label("orderDescription", new SimpleModel<String>() {
                @Override
                public String getObject() {
                    return blankIfNull(orderModel.getObject().getDescription());
                }
            }));
            add(new Label("orderPoNumber", new SimpleModel<String>() {
                @Override
                public String getObject() {
                    return blankIfNull(orderModel.getObject().getPoNumber());
                }
            }));
            add(new Label("orderOwnerCustomerOrgName", new SimpleModel<String>() {
                @Override
                public String getObject() {
                    BaseOrg owner = orderModel.getObject().getOwner();
                    if (owner != null) {
                        CustomerOrg customerOrg = owner.getCustomerOrg();
                        if (customerOrg != null)
                            return blankIfNull(customerOrg.getName());
                    }
                    return "";
                }
            }));
            add(new Label("orderOwnerDivisionOrgName", new SimpleModel<String>() {
                @Override
                public String getObject() {
                    BaseOrg owner = orderModel.getObject().getOwner();
                    if (owner != null) {
                        DivisionOrg divisionOrg = owner.getDivisionOrg();
                        if (divisionOrg != null)
                            return blankIfNull(divisionOrg.getName());
                    }
                    return "";
                }
            }));

            shopOrderListSection = new ShopOrderListPanel("shopOrderList", orderModel, lineItemIDataProvider);
            shopOrderListSection.setVisible(false);
            add(shopOrderListSection);
            customerOrderListSection = new CustomerOrderPanel("customerOrderList", securityGuardModel);
            add(customerOrderListSection);
            customerOrderListSection.setVisible(false);

        }

        @Override
        public void onConfigure() {
            System.out.println("OrderDetailsPanel.onConfigure");
            shopOrderListSection.setVisible(false);
            customerOrderListSection.setVisible(false);
            Order order = orderModel.getObject();
            if (order != null) {
                if (order.getOrderType().equals(Order.OrderType.SHOP))
                    shopOrderListSection.setVisible(true);
                else
                if (order.getOrderType().equals(Order.OrderType.CUSTOMER))
                    customerOrderListSection.setVisible(true);
            }
        }

        private String blankIfNull(String str) {
            return str == null ? "" : str;
        }

    }

    private class ShopOrderListPanel extends Panel {

        IModel<Order> orderModel;
        LineItemIDataProvider lineItemIDataProvider;
        WebMarkupContainer resultsTableSection;
        WebMarkupContainer emptyListSection;

        public ShopOrderListPanel(String id, IModel<Order> orderModel, LineItemIDataProvider lineItemIDataProvider) {

            super(id);
            this.orderModel = orderModel;
            this.lineItemIDataProvider = lineItemIDataProvider;
            resultsTableSection = new WebMarkupContainer("resultsTable");
            resultsTableSection.setOutputMarkupId(true);
            final DataView<LineItem> lineItemsView =
                    new DataView<LineItem>("shopOrderListItems", lineItemIDataProvider) {
                        @Override
                        public void populateItem(final Item<LineItem> item) {
                            LineItem lineItem = item.getModelObject();
                            item.add(new Link("identifyLink") {
                                public void onClick() {
                                    getRequestCycle().setResponsePage(IdentifyOrEditAssetPage.class,
                                            PageParametersBuilder.param("lineItemId", lineItem.getId()));
                                }
                            });
                            item.add(new Label("assetCode", lineItem.getAssetCode()));
                            item.add(new Label("description", blankIfNull(lineItem.getDescription())));
                            item.add(new Label("quantity", (new Long(lineItem.getQuantity())).toString()));
                            item.add(new Label("identifiedAssetCount",
                                    (new Integer(orderManager.countAssetsTagged(lineItem))).toString()));
                        }
                    };
            resultsTableSection.add(lineItemsView);

            emptyListSection = new WebMarkupContainer("emptyList");
            emptyListSection.setOutputMarkupId(true);

            add(resultsTableSection);
            add(emptyListSection);
        }

        @Override
        public void onConfigure() {
            if (lineItemIDataProvider.size() > 0) {
                resultsTableSection.setVisible(true);
                emptyListSection.setVisible(false);
            }
            else {
                resultsTableSection.setVisible(false);
                emptyListSection.setVisible(true);
            }
        }

        private String blankIfNull(String str) {
            return str == null ? "" : str;
        }
    }

    private class CustomerOrderPanel extends Panel {

        private SmartSearchDataProvider searchDataProvider;
        private IModel<SystemSecurityGuard> securityGuardModel;
        private WebMarkupContainer orderResultSection;
        private WebMarkupContainer noResutsSection;

        public CustomerOrderPanel(String id, IModel<SystemSecurityGuard> securityGuardModel) {
            super(id);
            this.securityGuardModel = securityGuardModel;
            searchDataProvider = new SmartSearchDataProvider(null);
            addComponents();
        }
        private void addComponents() {
            TextField assetSearchTerm = new TextField("assetSearchTerm", new Model<String>(""));
            Form assetSearchForm = new Form("assetSearchForm") {
                @Override
                protected void onSubmit() {
                    System.out.println("Perform asset search called");
                    Object searchTerm = assetSearchTerm.getModel().getObject();
                    if (searchTerm != null && !searchTerm.toString().isEmpty()) {
                        searchDataProvider.setSearchTerm(searchTerm.toString());
                        System.out.println("Searching returned " + searchDataProvider.size());
                        if (searchDataProvider.size() > 0) {
                            orderResultSection.setVisible(true);
                            noResutsSection.setVisible(false);
                        }
                        else {
                            orderResultSection.setVisible(false);
                            noResutsSection.setVisible(true);
                        }
                    }
                    //processAssetSearch(searchDataProvider, foundSearchResult, assetSearchTerm, foundSearchResult,
                    //        notFoundSearchResult, multipleResultsMsg, target);
                }
            };

            //assetSearchTerm.add(new AttributeAppender("onkeyup", new Model("setAssetSearchButtonStatus(this);"), ";"));
            // onpaste and oncut events are called before the element is updated so a brief pause is added
            // to wait till after the update is done
            //assetSearchTerm.add(new AttributeAppender("onpaste",
            //        new Model("var e = this; setTimeout(function(){setAssetSearchButtonStatus(e);}, 4);"), ";"));
            //assetSearchTerm.add(new AttributeAppender("oncut",
            //        new Model("var e = this; setTimeout(function(){setAssetSearchButtonStatus(e);}, 4);"), ";"));
            assetSearchForm.add(assetSearchTerm);

            add(assetSearchForm);

            orderResultSection = createCustomerOrderResultsSection("customerOrderResultList");
            orderResultSection.setOutputMarkupId(true);
            add(orderResultSection);
            orderResultSection.setVisible(false);

            noResutsSection = new WebMarkupContainer("noResultsSection");
            add(noResutsSection);
            noResutsSection.setVisible(false);
        }

        private WebMarkupContainer createCustomerOrderResultsSection(String id) {
            WebMarkupContainer orderResultsSection = new WebMarkupContainer(id);

            FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            orderResultsSection.add(new Label("identifierLabel", securityGuardModel.getObject().getPrimaryOrg().getIdentifierLabel()));

            final DataView<Asset> dataView =
                    new DataView<Asset>("result.list", searchDataProvider) {
                        @Override
                        public void populateItem(final Item<Asset> item) {
                            Asset asset = item.getModelObject();
                            final Long selectedAssetId = asset.getID();
                            item.add(new AjaxLink("connectAsset") {
                                @Override
                                public void onClick(AjaxRequestTarget target) {
                                    System.out.println("User has clicked on asset " + selectedAssetId);
                                    //winningAsset.setObject(assetManager.findAssetAllFields(selectedAssetId, getSecurityFilter()));
                                   // closeContainer(target, step2Container, step2ToggledContainer);
                                   // openContainer(target, step3Container, step3ToggledContainer);
                                    info("INFO MESSAGE");
                                    error("User clicked on asset " + selectedAssetId);
                                    target.add(feedbackPanel);
                                }
                            });
                            item.add(new Label("result.identifier", asset.getIdentifier()));
                            item.add(new Label("result.rfidNumber", asset.getRfidNumber()));
                            item.add(new Label("result.ownerName", asset.getOwner().getName()));
                            item.add(new Label("result.typeName", asset.getType().getName()));
                            item.add(new Label("result.customerRefNumber", asset.getCustomerRefNumber()));
                        }
                    };
            dataView.setOutputMarkupId(true);
            dataView.setItemsPerPage(10);
            orderResultsSection.add(dataView);
            orderResultsSection.add(new StyledAjaxPagingNavigator("resultTableNavigator", dataView, orderResultsSection) {
                @Override
                protected void onAjaxEvent(AjaxRequestTarget target) {
                    target.add(orderResultsSection);
                }
            });

            return orderResultsSection;
        }
     }


    private class LineItemIDataProvider implements IDataProvider<LineItem> {

        private List<LineItem> lineItems;

        public LineItemIDataProvider() {
        }

        public void setLineItems(List<LineItem> lineItems) {
            this.lineItems = lineItems;
        }
        @Override
        public Iterator<? extends LineItem> iterator(int first, int count) {
            return lineItems.iterator();
        }

        @Override
        public int size() {
            return lineItems.size();
        }

        @Override
        public IModel<LineItem> model(LineItem object) {
            return Model.of(object);
        }

        @Override
        public void detach() {

        }
    };

    /** Model implementation to reduce boilerplate code required */
    abstract private class SimpleModel<T> implements IModel<T> {
        abstract public T getObject();
        public void setObject(final T object) { }
        public void detach() {}
    }
}
