package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.service.org.CustomerMergerService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.codehaus.xfire.transport.Session;
import rfid.web.helper.SessionUser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by agrabovskis on 2018-02-05.
 */
public class CustomerMergePanel extends Panel {

    private static final int MAX_CUSTOMER_RESULTS = 10;
    private static Logger logger = Logger.getLogger(CustomerMergePanel.class);

    private IModel<Long> customerSelectedForEditModel;
    private IModel<SessionUser> sessionUserModel;
    private LoaderFactory loaderFactory;

    @SpringBean
    private OrgService orgService;

    @SpringBean
    private CustomerMergerService customerMergerService;

    private CompoundPropertyModel<CustomerOrg> losingCustomer;
    private CompoundPropertyModel<CustomerOrg> winningCustomer;

    private enum SECTIONS {STEP1, STEP2, STEP3}
    private SECTIONS currentlyOpenSection;
    private boolean pageRefreshing; /* True if the panel is in the process of refreshing. Informs us to reset our models */


    public CustomerMergePanel(String id, IModel<Long> customerSelectedForEditModel,
                              IModel<SessionUser> sessionUserModel, LoaderFactory loaderFactory) {
        super(id);
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        this.sessionUserModel = sessionUserModel;
        this.loaderFactory = loaderFactory;
        losingCustomer = new CompoundPropertyModel<CustomerOrg>((CustomerOrg)null);
        winningCustomer = new CompoundPropertyModel<CustomerOrg>((CustomerOrg)null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSS(".emptyList h2 {color: #333;}", "legacyStyle");
        response.renderCSSReference("style/legacy/steps.css");
    }

    @Override
    protected void onBeforeRender() {
        Long customerId = customerSelectedForEditModel.getObject();
        CustomerOrg customer = (CustomerOrg) orgService.findById(customerId);
        if (customer == null || customer.isNew()) {
            logger.error("CustomerMerge cannot find losing customer " + customerId);
            throw new MissingEntityException();
        }
        losingCustomer.setObject(customer);
        currentlyOpenSection = SECTIONS.STEP1;
        pageRefreshing = true;
        super.onBeforeRender();
    }

    @Override
    protected void onAfterRender() {
        pageRefreshing = false;
        super.onAfterRender();
    }

    private void addComponents() {

        final WebMarkupContainer stepsContainer = createHideableWebMarkupContainer("steps");
        stepsContainer.setVisible(true);
        add(stepsContainer);

        stepsContainer.add(createStep1Content(stepsContainer));
        stepsContainer.add(createStep2Content(stepsContainer));
        stepsContainer.add(createStep3Content(stepsContainer));
    }

    private WebMarkupContainer createHideableWebMarkupContainer(String id) {
        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        return container;
    }

    private WebMarkupContainer createHideableWebMarkupContainer(String id, SECTIONS parentSection) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return currentlyOpenSection == parentSection;
            }
        };
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        return container;
    }

    private WebMarkupContainer createStep1Content(final WebMarkupContainer stepsContainer) {

        WebMarkupContainer step1Container = new WebMarkupContainer("step1Container");
        step1Container.add(new AttributeAppender("class", Model.of("stepClosed"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return currentlyOpenSection != SECTIONS.STEP1;
            }
        });
        step1Container.setOutputMarkupId(true);
        step1Container.setOutputMarkupPlaceholderTag(true);

        step1Container.add(new Label("step1Title", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return MessageFormat.format(getString("label.duplicate_customer"), losingCustomer.getObject().getName());
            }
        }));

        WebMarkupContainer step1ToggledContainer = createHideableWebMarkupContainer("step1ToggledContainer", SECTIONS.STEP1);
        step1Container.add(step1ToggledContainer);

        step1ToggledContainer.add(new Label("step1Instructions", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                    return MessageFormat.format(getString("instruction.select_duplicate_customer"),
                            losingCustomer.getObject().getName(), losingCustomer.getObject().getName());
            }
        }));

        step1ToggledContainer.add(new AjaxLink("step1Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                currentlyOpenSection = SECTIONS.STEP2;
                target.add(stepsContainer);
            }
        });

        return step1Container;
    }

    private WebMarkupContainer createStep2Content(final WebMarkupContainer stepsContainer) {

        final List<CustomerOrg> customerSearchResult = new ArrayList<CustomerOrg>();
        final MutableInt customerSearchResultCount = new MutableInt(-1); /* -1 indicates no search was run */
        final IModel<String> searchTermModel = new Model<>("");

        final WebMarkupContainer step2Container = new WebMarkupContainer("step2Container") {

            @Override
            protected void onBeforeRender() {
                if (pageRefreshing) {
                    customerSearchResultCount.setValue(-1);
                    searchTermModel.setObject(null);
                }
                super.onBeforeRender();
            }
        };
        step2Container.setOutputMarkupId(true);
        step2Container.setOutputMarkupPlaceholderTag(true);
        step2Container.add(new AttributeAppender("class", Model.of("stepClosed"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return currentlyOpenSection != SECTIONS.STEP2;
            }
        });
        final WebMarkupContainer step2ToggledContainer =
                createHideableWebMarkupContainer("step2ToggledContainer", SECTIONS.STEP2);
        step2Container.add(step2ToggledContainer);

        Form customerSearchForm = new Form("customerSearchForm");
        step2ToggledContainer.add(customerSearchForm);
        TextField<String> customerSearchTerm = new TextField<>("customerSearchTerm", searchTermModel);
        customerSearchForm.add(customerSearchTerm);

        final AjaxButton customerSearchButton = new AjaxButton("performCustomerSearch", customerSearchForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                List<CustomerOrg> customers = getCustomers(customerSearchTerm.getModelObject());
                customerSearchResultCount.setValue(customers.size());
                customerSearchResult.clear();
                customerSearchResult.addAll(customers);
                target.add(step2ToggledContainer);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
        customerSearchForm.add(customerSearchButton);

        /* Section displayed when results exist and are within the allowed maximum */
        final WebMarkupContainer searchResultSection = new WebMarkupContainer("foundSearchResult") {
            @Override
            public boolean isVisible() {
                return customerSearchResultCount.intValue() > 0;
            }
        };
        searchResultSection.setOutputMarkupId(true);
        searchResultSection.setOutputMarkupPlaceholderTag(true);
        step2ToggledContainer.add(searchResultSection);

        final DataView<CustomerOrg> dataView =
                new DataView<CustomerOrg>("result.dataView", new ListDataProvider<CustomerOrg>(customerSearchResult)) {
                    @Override
                    public void populateItem(final Item<CustomerOrg> item) {
                        final CustomerOrg customer = item.getModelObject();
                        item.add(new AjaxLink("selectedWinningCustomer") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                winningCustomer.setObject(customer);
                                currentlyOpenSection = SECTIONS.STEP3;
                                target.add(stepsContainer);
                            }
                        });
                        item.add(new Label("result.name", customer.getName()));
                        item.add(new Label("result.id", customer.getCode()));
                        item.add(new Label("result.internalOrg.name", customer.getInternalOrg() != null ? customer.getInternalOrg().getName() : null));
                        item.add(new Label("result.created", formatAnyDate(customer.getCreated())));
                    }
                };
        dataView.setOutputMarkupId(true);
        dataView.setOutputMarkupPlaceholderTag(true);
        dataView.setItemsPerPage(MAX_CUSTOMER_RESULTS);
        searchResultSection.add(dataView);

        /* Section displayed when results are greater than allowed maximum */
        WebMarkupContainer tooManyResultsSection = new WebMarkupContainer("tooManySearchResults") {
            @Override
            public boolean isVisible() {
                return customerSearchResultCount.intValue() > MAX_CUSTOMER_RESULTS;
            }
        };
        tooManyResultsSection.setOutputMarkupId(true);
        tooManyResultsSection.setOutputMarkupPlaceholderTag(true);
        step2ToggledContainer.add(tooManyResultsSection);
        tooManyResultsSection.add(new Label("tooManySearchResultsMsg", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return MessageFormat.format(getString("message.customer_merge_search_refine_results"), MAX_CUSTOMER_RESULTS, customerSearchResultCount.intValue());
            }
        }));

        /* Section displayed when no results found */
        WebMarkupContainer noResultsSection = new WebMarkupContainer("notFoundSearchResult") {
            @Override
            public boolean isVisible() {
              return customerSearchResultCount.intValue() == 0;
          }
        };
        noResultsSection.setOutputMarkupId(true);
        noResultsSection.setOutputMarkupPlaceholderTag(true);
        step2ToggledContainer.add(noResultsSection);

        step2ToggledContainer.add(new AjaxLink("backToStep1Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                currentlyOpenSection = SECTIONS.STEP1;
                target.add(stepsContainer);
            }
        });

        return step2Container;
    }

    private WebMarkupContainer createStep3Content(final WebMarkupContainer stepsContainer) {
        WebMarkupContainer step3Container = new WebMarkupContainer("step3Container");
        step3Container.setOutputMarkupId(true);
        step3Container.setOutputMarkupPlaceholderTag(true);

        final WebMarkupContainer step3ToggledContainer =
                createHideableWebMarkupContainer("step3ToggledContainer", SECTIONS.STEP3);
        step3Container.add(step3ToggledContainer);

        /* When this code is called the losingCustomer and winningCustomer models don't have their values set yet
         * so label values need to be obtained dynamically from the models when the label is actually rendered. */
        step3ToggledContainer.add(new Label("losingCustomer.name", losingCustomer.bind("name")));
        step3ToggledContainer.add(new Label("losingCustomer.id", losingCustomer.bind("code")));
        step3ToggledContainer.add(new Label("losingCustomer.created", losingCustomer.bind("created")) {
            public IConverter getConverter(Class type) {
                return new IConverter() {
                    public String convertToString(Object value, Locale locale) {
                        return formatAnyDate((Date) value);
                    }
                    public Object convertToObject(String value, Locale locale) {
                        // Only used by FormComponents
                        return null;
                    }
                };
            }
        });

        step3ToggledContainer.add(new Label("winningCustomer.name", winningCustomer.bind("name")));
        step3ToggledContainer.add(new Label("winningCustomer.id", winningCustomer.bind("code")));
        step3ToggledContainer.add(new Label("winningCustomer.created", winningCustomer.bind("created")) {
            public IConverter getConverter(Class type) {
                return new IConverter() {
                    public String convertToString(Object value, Locale locale) {
                        return formatAnyDate((Date) value);
                    }
                    public Object convertToObject(String value, Locale locale) {
                        // Only used by FormComponents
                        return null;
                    }
                };
            }
        });

        step3ToggledContainer.add(new AjaxLink("mergeCustomers") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (winningCustomer.getObject() == null) {
                    error(getString("error.you_must_choose_a_valid_customer_to_merge_into"));
                    target.addChildren(getPage(), FeedbackPanel.class);
                }
                else {
                    customerMergerService.merge(winningCustomer.getObject(), losingCustomer.getObject());
                    PageParameters params = new PageParameters();
                    params.add(CustomerMergeSuccessPage.LOSING_CUSTOMER_NAME_KEY, losingCustomer.getObject().getName());
                    params.add(CustomerMergeSuccessPage.WINNING_CUSTOMER_NAME_KEY, winningCustomer.getObject().getName());
                    params.add(CustomerMergeSuccessPage.WINNING_CUSTOMER_ID_KEY, winningCustomer.getObject().getId().toString());
                    getRequestCycle().setResponsePage(CustomerMergeSuccessPage.class, params);
                }
            }
        });

        step3ToggledContainer.add(new AjaxLink("backToStep2Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                currentlyOpenSection = SECTIONS.STEP2;
                target.add(stepsContainer);
            }
        });

        return step3Container;
    }

    private List<CustomerOrg> getCustomers(String searchTerm) {
        List<CustomerOrg> customersList = getLoaderFactory().createCustomerOrgListLoader()
                .withLinkedOrgs()
                .withNameFilter(searchTerm)
                .setPostFetchFields("createdBy")
                .load();
        customersList.remove(losingCustomer);
        return customersList;
    }

    private String formatAnyDate(Date date) {
        return new FieldIdDateFormatter(date, getSessionUser(), true, true).format();
    }

    private SessionUser getSessionUser() {
        return sessionUserModel.getObject();
    }

    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }
}
